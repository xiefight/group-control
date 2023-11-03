package cn.yunshi.groupcontrol.entity;

import cn.yunshi.groupcontrol.common.GroupTaskStatus;
import cn.yunshi.groupcontrol.vo.GroupEventRespVo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Description: 任务
 * @Author: xietao
 * @Date: 2023-10-19 16:42
 **/
@Data
@TableName("groupTask")
public class GroupTaskEntity {

    //主键id
    @TableId(type = IdType.AUTO)
    private Integer id;

    //平台
    private String platform;

    //群控内容url
    private String contentUrl;

    //群控内容描述
//    private String contentDesc;

    //群控评论量--数量从事件详情表查询
//    private Integer commentNum;
    //群控点赞量
//    private Integer supportNum;
    //群控浏览量
//    private Integer browseNum;
    //群控转发量
//    private Integer forwardNum;

    /**
     * 状态
     * {@link GroupTaskStatus}
     */
    private Integer status;

    //任务发起人id
    private String userId;

    //任务发起人名称
    private String userName;

    //创建时间
    private Date createTime;

    //更新时间
    private Date updateTime;

    //失败原因
    private String errMsg;

    /**
     * 视频号-公众号全称
     */
    private String weixinVideoName;

    /**
     * 视频号-视频序号
     */
    private Integer weixinVideoNameSort;

    //关联的事件集合
    @TableField(exist = false)
    private GroupEventRespVo eventRespVo;

}
