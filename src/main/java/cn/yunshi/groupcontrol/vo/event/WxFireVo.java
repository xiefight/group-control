package cn.yunshi.groupcontrol.vo.event;

import cn.yunshi.groupcontrol.enums.ControlTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 微信视频号点赞小火苗vo
 * @Author: xietao
 * @Date: 2023/11/7 10:25
 */
@ToString
public class WxFireVo {

    /**
     * 群控类型
     */
    @Getter
    private Integer eventType = ControlTypeEnum.WXFIRE.getCode();

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
