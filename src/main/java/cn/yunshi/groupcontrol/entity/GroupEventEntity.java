package cn.yunshi.groupcontrol.entity;

import cn.yunshi.groupcontrol.common.GroupTaskStatus;
import cn.yunshi.groupcontrol.enums.ControlTypeEnum;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Description: 事件实体
 * @Author: xietao
 * @Date: 2023-10-23 11:30
 **/
@Data
@TableName("groupEvent")
public class GroupEventEntity {

    //主键id
    @TableId(type = IdType.AUTO)
    private String id;

    /**
     * 群控类型
     * {@link ControlTypeEnum}
     */
    private Integer eventType;

    /**
     * 所属任务id
     */
    private Integer taskId;

    /**
     * 执行机器id
     */
    private String androidId;

    /**
     * 状态
     * {@link GroupTaskStatus}
     */
    private Integer status;

    /**
     * 评论内容（评论事件有）
     */
    private String commentText;
}
