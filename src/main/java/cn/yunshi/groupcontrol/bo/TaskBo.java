package cn.yunshi.groupcontrol.bo;

import cn.yunshi.groupcontrol.vo.event.BrowseVo;
import cn.yunshi.groupcontrol.vo.event.CommentVo;
import cn.yunshi.groupcontrol.vo.event.ForwardVo;
import cn.yunshi.groupcontrol.vo.event.SupportVo;
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
    //浏览事件
    private BrowseVo browseVo;
    //转发事件
    private ForwardVo forwardVo;

    /**
     * 获取事件总数
     */
    public int getEventSums() {
        int sums = 0;
        SupportVo supportVo = this.getSupportVo();
        CommentVo commentVo = this.getCommentVo();
        BrowseVo browseVo = this.getBrowseVo();
        ForwardVo forwardVo = this.getForwardVo();

        if (supportVo != null) {
            sums += Optional.ofNullable(supportVo.getNums()).orElse(0);
        }
        if (commentVo != null) {
            sums += Optional.ofNullable(commentVo.getNums()).orElse(0);
        }
        if (browseVo != null) {
            sums += Optional.ofNullable(browseVo.getNums()).orElse(0);
        }
        if (forwardVo != null) {
            sums += Optional.ofNullable(forwardVo.getNums()).orElse(0);
        }
        return sums;
    }

}
