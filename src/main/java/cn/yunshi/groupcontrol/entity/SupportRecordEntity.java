package cn.yunshi.groupcontrol.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Description: 点赞记录表
 * @Author: xietao
 * @Date: 2023-10-30 13:33
 **/
@Data
@TableName("supportRecord")
public class SupportRecordEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 点赞设备id
     */
    private String androidId;

    /**
     * 点赞的视频路径
     */
    private String contentUrl;

    /**
     * 视频路径的md5值
     */
    private String contentMd5;

}
