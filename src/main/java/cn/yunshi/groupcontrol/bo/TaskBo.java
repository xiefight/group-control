package cn.yunshi.groupcontrol.bo;

import cn.yunshi.groupcontrol.vo.WeixinExtraVo;
import cn.yunshi.groupcontrol.vo.event.*;
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
    //微信视频号搜索方式 1：视频链接 默认方式  2：视频号名字全称
//    private String searchMethod;
    //评论事件
    private CommentVo commentVo;
    //点赞事件
    private SupportVo supportVo;
    //浏览事件
    private BrowseVo browseVo;
    //转发事件
    private ForwardVo forwardVo;
    //微信小火苗事件
    private WxFireVo wxFireVo;

    //微信视频号-视频号名字全称-搜索视频 额外参数
    private WeixinExtraVo weixinExtraVo;

    /**
     * 获取事件总数
     */
    public int getEventSums() {
        int sums = 0;
        SupportVo supportVo = this.getSupportVo();
        CommentVo commentVo = this.getCommentVo();
        BrowseVo browseVo = this.getBrowseVo();
        ForwardVo forwardVo = this.getForwardVo();
        WxFireVo wxFireVo = this.getWxFireVo();

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
        if (wxFireVo != null) {
            sums += Optional.ofNullable(wxFireVo.getNums()).orElse(0);
        }
        return sums;
    }

}
