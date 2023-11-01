package cn.yunshi.groupcontrol.business;

import cn.yunshi.groupcontrol.bo.TaskBo;
import cn.yunshi.groupcontrol.common.GroupTaskStatus;
import cn.yunshi.groupcontrol.dao.GroupEventDao;
import cn.yunshi.groupcontrol.entity.GroupEventEntity;
import cn.yunshi.groupcontrol.enums.ControlTypeEnum;
import cn.yunshi.groupcontrol.service.IControlScriptService;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ForwardEventOperate extends BaseOperate {
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private GroupEventDao groupEventDao;

    @Override
    protected GroupEventEntity saveGroupEvent(Integer taskId, TaskBo taskBo) {
        //构建事件基础信息
        GroupEventEntity groupEventEntity = new GroupEventEntity();
        groupEventEntity.setEventType(ControlTypeEnum.FORWARD.getCode());
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
        boolean forwardRes = false;
        try {
            forwardRes = scriptService.forward(androidId, contentUrl);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            redissonClient.getLock(androidId).unlock();
            System.out.println("转发事件 >>>>>>>>>>>> " + Thread.currentThread().getId() + " 释放了锁：" + androidId);
        }
        return forwardRes;
    }
}
