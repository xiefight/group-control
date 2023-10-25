package cn.yunshi.groupcontrol.entity;

import cn.yunshi.groupcontrol.enums.ControlTypeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 评论配置信息
 * @Author: xietao
 * @Date: 2023-10-18 14:07
 **/
public class CommentEntity {

    /**
     * 群控类型
     */
    private int eventType = ControlTypeEnum.COMMENT.getCode();

    /**
     * 数量
     */
    @Getter
    @Setter
    private int nums;

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
    private int interval;


}
