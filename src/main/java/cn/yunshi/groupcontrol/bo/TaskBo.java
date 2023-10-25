package cn.yunshi.groupcontrol.bo;

import cn.yunshi.groupcontrol.vo.CommentVo;
import cn.yunshi.groupcontrol.vo.SupportVo;
import lombok.Data;

import java.util.Optional;

/**
 * @Description: 封装任务数据
 * @Author: xietao
 * @Date: 2023-10-18 15:27
 **/
@Data
public class TaskBo {

    //平台
    private String platform;
    //群控内容url
    private String contentUrl;
    //评论事件
    private CommentVo commentVo;
    //点赞事件
    private SupportVo supportVo;

    /**
     * 获取事件总数
     */
    public int getEventSums(){
        int sums = 0;
        SupportVo supportVo = this.getSupportVo();
        CommentVo commentVo = this.getCommentVo();

        if (supportVo != null){
            sums += Optional.ofNullable(supportVo.getNums()).orElse(0);
        }
        if (commentVo != null){
            sums += Optional.ofNullable(commentVo.getNums()).orElse(0);
        }
        return sums;
    }

}
