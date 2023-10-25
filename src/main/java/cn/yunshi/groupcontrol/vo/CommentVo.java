package cn.yunshi.groupcontrol.vo;

import cn.yunshi.groupcontrol.enums.ControlTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 评论vo
 * @Author: xietao
 * @Date: 2023-10-18 14:07
 **/
@ToString
public class CommentVo {

    /**
     * 群控类型
     */
    @Getter
    private Integer eventType = ControlTypeEnum.COMMENT.getCode();

    /**
     * 数量
     */
    @Getter
    @Setter
    private Integer nums;

    /**
     * 评论内容
     */
    @Getter
    @Setter
    private String comment;

    /**
     * 评论间隔时间
     */
    @Getter
    @Setter
    private Integer interval;


}
