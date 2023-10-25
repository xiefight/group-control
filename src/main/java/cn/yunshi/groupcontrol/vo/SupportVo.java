package cn.yunshi.groupcontrol.vo;

import cn.yunshi.groupcontrol.enums.ControlTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 点赞vo
 * @Author: xietao
 * @Date: 2023-10-18 14:05
 **/
@ToString
public class SupportVo {

    /**
     * 群控类型
     */
    @Getter
    private Integer eventType = ControlTypeEnum.SUPPORT.getCode();

    /**
     * 数量
     */
    @Getter
    @Setter
    private Integer nums;

    /**
     * 间隔时间
     */
    @Getter
    @Setter
    private Integer interval;

}
