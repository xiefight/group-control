package cn.yunshi.groupcontrol.operate;

import cn.hutool.core.collection.CollUtil;
import cn.yunshi.groupcontrol.bo.TaskBo;
import cn.yunshi.groupcontrol.business.dao.GroupTaskDao;
import cn.yunshi.groupcontrol.middle.IControlScriptService;
import cn.yunshi.groupcontrol.common.GroupTaskStatus;
import cn.yunshi.groupcontrol.business.dao.GroupEventDao;
import cn.yunshi.groupcontrol.entity.GroupEventEntity;
import cn.yunshi.groupcontrol.util.CommonUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Description:
 * @Author: xietao
 * @Date: 2023-10-27 15:22
 **/
public abstract class BaseOperate {

    public static ThreadLocal<String> contentUrlMap = new ThreadLocal<>();

    @Autowired
    protected CommonUtil commonUtil;
    @Autowired
    private GroupTaskDao groupTaskDao;
    @Autowired
    private GroupEventDao groupEventDao;

    public void reportFlow(Integer taskId, List<String> androidIds,
                           TaskBo taskBo, IControlScriptService scriptService) {
        contentUrlMap.set(taskBo.getContentUrl());
        //1.构建事件基础信息
        GroupEventEntity groupEventEntity = saveGroupEvent(taskId, taskBo);
        //2.1点赞事件需要先排除已点过赞的设备id
        androidIds = excludeAndroidIds(androidIds);
        if (CollUtil.isEmpty(androidIds)) {
            updateEvent(groupEventEntity, "", false, "无可用设备");
            return;
        }
        //2.2获取锁
        String androidId = getAndroidLock(androidIds);
        if (StringUtils.isEmpty(androidId)) {
            //点赞会出现设备锁越来越少的情况，评论等不会，所以这个判断是针对点赞的
            updateEvent(groupEventEntity, "", false, "无可用设备");
            return;
        }
        //3.查找视频
        try {
            scriptService.openAppFindVideo(androidId, contentUrlMap.get(), groupTaskDao.selectById(taskId));
        } catch (InterruptedException e) {
            e.printStackTrace();
            //todo 查找视频失败 更新事件状态
        }
        //4.执行事件
        boolean eventRes = executeEvent(scriptService, groupEventEntity, androidId);
        //5.更新事件状态
        updateEvent(groupEventEntity, androidId, eventRes, "");
    }

    protected void updateEvent(GroupEventEntity groupEventEntity, String androidId, boolean eventRes, String errMsg) {
        GroupEventEntity update = new GroupEventEntity();
        update.setId(groupEventEntity.getId());
        update.setAndroidId(androidId);
        update.setStatus(eventRes ? GroupTaskStatus.SUCCESS.getCode() : GroupTaskStatus.FAIL.getCode());
        update.setErrMsg(errMsg);
        groupEventDao.updateById(update);

    }

    protected List<String> excludeAndroidIds(List<String> androidIds) {
        return androidIds;
    }

    protected abstract boolean executeEvent(IControlScriptService scriptService,
                                            GroupEventEntity groupEventEntity, String androidId);

    protected abstract GroupEventEntity saveGroupEvent(Integer taskId, TaskBo taskBo);

    protected String getAndroidLock(List<String> androidIds) {
        String androidId = commonUtil.getAndroidLock(androidIds);
        //一轮没找到就重复遍历，直到找到为止
        if (StringUtils.isEmpty(androidId)) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return this.getAndroidLock(androidIds);
        }
        return androidId;
    }

}
