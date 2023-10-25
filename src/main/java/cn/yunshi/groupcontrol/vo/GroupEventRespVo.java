package cn.yunshi.groupcontrol.vo;

import cn.yunshi.groupcontrol.common.GroupTaskStatus;
import cn.yunshi.groupcontrol.entity.GroupEventEntity;
import cn.yunshi.groupcontrol.enums.ControlTypeEnum;
import lombok.Getter;

/**
 * @Description: 任务列表中事件响应vo
 * @Author: xietao
 * @Date: 2023-10-24 14:59
 **/
public class GroupEventRespVo {

    @Getter
    private EventExecuteInner supportEvent = new EventExecuteInner(ControlTypeEnum.SUPPORT.getCode(), 0, 0, 0, 0);
    @Getter
    private EventExecuteInner commentEvent = new EventExecuteInner(ControlTypeEnum.COMMENT.getCode(), 0, 0, 0, 0);
    @Getter
    private EventExecuteInner browseEvent = new EventExecuteInner(ControlTypeEnum.BROWSE.getCode(), 0, 0, 0, 0);
    @Getter
    private EventExecuteInner forwardEvent = new EventExecuteInner(ControlTypeEnum.FORWARD.getCode(), 0, 0, 0, 0);


    /**
     * 根据类型分
     * @param eventEntity
     */
    public void distributeType(GroupEventEntity eventEntity) {

        Integer eventType = eventEntity.getEventType();
        if (ControlTypeEnum.SUPPORT.getCode() == eventType) {
            EventExecuteInner supportEvent = this.getSupportEvent();
            supportEvent.setTotalNum();
            distributeStatus(eventEntity, supportEvent);
        }
        if (ControlTypeEnum.COMMENT.getCode() == eventType) {
            EventExecuteInner commentEvent = this.getCommentEvent();
            commentEvent.setTotalNum();
            distributeStatus(eventEntity, commentEvent);
        }
        if (ControlTypeEnum.BROWSE.getCode() == eventType) {
            EventExecuteInner browseEvent = this.getBrowseEvent();
            browseEvent.setTotalNum();
            distributeStatus(eventEntity, browseEvent);
        }
        if (ControlTypeEnum.FORWARD.getCode() == eventType) {
            EventExecuteInner forwardEvent = this.getForwardEvent();
            forwardEvent.setTotalNum();
            distributeStatus(eventEntity, forwardEvent);
        }

    }

    /**
     * 根据状态分
     * @param eventEntity
     * @param eventExecuteInner
     */
    private void distributeStatus(GroupEventEntity eventEntity, EventExecuteInner eventExecuteInner) {
        if (eventEntity.getStatus() == GroupTaskStatus.FAIL.getCode()) {
            eventExecuteInner.setFailNum();
        }
        if (eventEntity.getStatus() == GroupTaskStatus.SUCCESS.getCode()) {
            eventExecuteInner.setSucNum();
        }
        if (eventEntity.getStatus() == GroupTaskStatus.EXECUTE.getCode()) {
            eventExecuteInner.setExeNum();
        }
    }

}
