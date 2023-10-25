package cn.yunshi.groupcontrol.service;

import cn.yunshi.groupcontrol.entity.GroupEventEntity;

import java.util.List;

/**
 * @Description: 群控脚本服务层
 * @Author: xietao
 * @Date: 2023-10-18 17:31
 **/
public interface IControlScriptService {

    List<String> getAndroidIds();

    /**
     * 点赞逻辑
     */
    boolean support(GroupEventEntity groupEventEntity, String contentUrl) throws InterruptedException;

    /**
     * 评论逻辑
     */
    boolean comment(GroupEventEntity groupEventEntity, String contentUrl) throws InterruptedException;


    /**
     * 浏览逻辑
     */
//    void browse();


    /**
     * 转发逻辑
     */
//    void forward();


    /**
     * 踩逻辑
     */
//    void dislike();

}
