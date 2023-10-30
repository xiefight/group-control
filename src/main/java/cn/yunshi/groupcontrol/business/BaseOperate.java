package cn.yunshi.groupcontrol.business;

import cn.hutool.core.collection.CollUtil;
import cn.yunshi.groupcontrol.bo.TaskBo;
import cn.yunshi.groupcontrol.common.GroupTaskStatus;
import cn.yunshi.groupcontrol.dao.GroupEventDao;
import cn.yunshi.groupcontrol.entity.GroupEventEntity;
import cn.yunshi.groupcontrol.service.IControlScriptService;
import cn.yunshi.groupcontrol.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Description:
 * @Author: xietao
 * @Date: 2023-10-27 15:22
 **/
public abstract class BaseOperate {

    @Autowired
    protected CommonUtil commonUtil;
    @Autowired
    private GroupEventDao groupEventDao;

    public void reportFlow(Integer taskId, List<String> androidIds,
                           TaskBo taskBo, IControlScriptService scriptService) {
        //1.构建事件基础信息
        GroupEventEntity groupEventEntity = saveGroupEvent(taskId, taskBo);
        //2.1点赞事件需要先排除已点过赞的设备id
        androidIds = excludeAndroidIds(taskBo.getContentUrl(), androidIds);
        if (CollUtil.isEmpty(androidIds)) {
            updateEvent(groupEventEntity, "", false, "无可用设备");
            return;
        }
        //2.2获取锁
        String androidId = commonUtil.getAndroidLock(androidIds);
        //3.执行事件
        boolean eventRes = executeEvent(scriptService, groupEventEntity, taskBo.getContentUrl(), androidId);
        //4.更新事件状态
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

    protected List<String> excludeAndroidIds(String contentUrl, List<String> androidIds) {
        return androidIds;
    }

    protected abstract boolean executeEvent(IControlScriptService scriptService,
                                            GroupEventEntity groupEventEntity,
                                            String contentUrl, String androidId);

    protected abstract GroupEventEntity saveGroupEvent(Integer taskId, TaskBo taskBo);

}
