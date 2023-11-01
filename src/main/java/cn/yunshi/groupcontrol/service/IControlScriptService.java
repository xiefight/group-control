package cn.yunshi.groupcontrol.service;

import java.util.List;

/**
 * @Description: 群控脚本服务层
 * @Author: xietao
 * @Date: 2023-10-18 17:31
 **/
public interface IControlScriptService {

    List<String> getAndroidIds();

    /**
     * 打开app，并找到视频
     */
    boolean openAppFindVideo(String androidId, String contentUrl) throws InterruptedException;

    /**
     * 点赞逻辑
     */
    boolean support(String androidId, String contentUrl) throws InterruptedException;

    /**
     * 评论逻辑
     */
    boolean comment(String androidId, String contentUrl, String commentText) throws InterruptedException;


    /**
     * 浏览逻辑
     */
    boolean browse(String androidId, String contentUrl, Integer browseTime) throws InterruptedException;


    /**
     * 转发逻辑
     */
    boolean forward(String androidId, String contentUrl, String forwardFriendName) throws InterruptedException;


    /**
     * 踩逻辑
     */
//    void dislike();

}
