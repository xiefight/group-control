package cn.yunshi.groupcontrol.vo.event;

import cn.yunshi.groupcontrol.enums.ControlTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 转发vo
 *
 * @Author: xietao
 * @Date: 2023/11/1 14:39
 */
@ToString
public class ForwardVo {

    /**
     * 群控类型
     */
    @Getter
    private Integer eventType = ControlTypeEnum.FORWARD.getCode();

    /**
     * 数量
     */
    @Getter
    @Setter
    private Integer nums;

    /**
     * 转发好友名称
     */
    @Getter
    @Setter
    private String friendName;

    /**
     * 间隔时间
     */
    @Getter
    @Setter
    private Integer interval;

}
