package cn.yunshi.groupcontrol.business;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.crypto.digest.MD5;
import cn.yunshi.groupcontrol.bo.TaskBo;
import cn.yunshi.groupcontrol.common.GroupTaskStatus;
import cn.yunshi.groupcontrol.dao.GroupEventDao;
import cn.yunshi.groupcontrol.dao.SupportRecordDao;
import cn.yunshi.groupcontrol.entity.GroupEventEntity;
import cn.yunshi.groupcontrol.entity.SupportRecordEntity;
import cn.yunshi.groupcontrol.enums.ControlTypeEnum;
import cn.yunshi.groupcontrol.service.IControlScriptService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: xietao
 * @Date: 2023-10-27 15:33
 **/
@Component
public class SupportEventOperate extends BaseOperate {
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private GroupEventDao groupEventDao;
    @Autowired
    private SupportRecordDao supportRecordDao;

    @Override
    protected GroupEventEntity saveGroupEvent(Integer taskId, TaskBo taskBo) {
        //构建事件基础信息
        GroupEventEntity groupEventEntity = new GroupEventEntity();
        groupEventEntity.setEventType(ControlTypeEnum.SUPPORT.getCode());
        //状态执行中
        groupEventEntity.setStatus(GroupTaskStatus.EXECUTE.getCode());
        groupEventEntity.setTaskId(taskId);
        int eventId = groupEventDao.insert(groupEventEntity);
        return groupEventEntity;
    }

    @Override
    protected boolean executeEvent(IControlScriptService scriptService,
                                   GroupEventEntity groupEventEntity,
                                   String contentUrl, String androidId) {
        //执行事件
        boolean supportRes = false;
        try {
            supportRes = scriptService.support(androidId, contentUrl);
            //点赞成功后，记录点赞信息
            if (supportRes) {
                SupportRecordEntity supportRecord = new SupportRecordEntity();
                supportRecord.setAndroidId(androidId);
                supportRecord.setContentUrl(contentUrl);
                supportRecord.setContentMd5(MD5.create().digestHex16(contentUrl));
                supportRecordDao.insert(supportRecord);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            redissonClient.getLock(androidId).unlock();
            System.out.println("点赞事件 >>>>>>>>>>>> " + Thread.currentThread().getId() + " 释放了锁：" + androidId);
        }
        return supportRes;
    }

    /**
     * 点赞事件剔除已点过赞的设备
     *
     * @param contentUrl 点赞的视频url
     * @param androidIds 设备集合
     * @return 可以点赞的设备id
     */
    @Override
    protected List<String> excludeAndroidIds(String contentUrl, List<String> androidIds) {
        List<String> allAndroidIds = new ArrayList<>(androidIds);
        //1.从数据库中查找已点过赞的设备
        QueryWrapper<SupportRecordEntity> qw = new QueryWrapper();
        qw.eq("content_md5", MD5.create().digestHex16(contentUrl));
        List<SupportRecordEntity> supportRecordEntities = supportRecordDao.selectList(qw);
        if (CollUtil.isEmpty(supportRecordEntities)){
            return allAndroidIds;
        }
        List<String> usedAndroidIds = supportRecordEntities.stream().map(supportRecordEntity -> supportRecordEntity.getAndroidId()).collect(Collectors.toList());
        //所有的设备和已使用的设备去重
        allAndroidIds.removeAll(usedAndroidIds);
        return allAndroidIds;
    }
}
