package cn.yunshi.groupcontrol.middle.impl;

import cn.yunshi.groupcontrol.middle.BaseScriptService;
import cn.yunshi.groupcontrol.vo.action.ClickVo;
import cn.yunshi.groupcontrol.vo.action.CopyVo;

/**
 * @Description:
 * @Author: xietao
 * @Date: 2023-11-02 17:25
 **/
public abstract class BaseWeixinVideoScriptService extends BaseScriptService {

    @Override
    public boolean support(String androidId, String contentUrl) throws InterruptedException {
        long start = System.currentTimeMillis();

        //9.点赞
        if (!click(new ClickVo(androidId, 636, 1947))) {
            return false;
        }
        System.out.println("9.点赞");
        Thread.sleep(4000);

        clear(androidId);
        back(androidId);

        System.out.println(androidId + " 花费时间：" + (System.currentTimeMillis() - start));
        return true;
    }

    /**
     * @param contentUrl 要操作的内容的url
     */
    @Override
    public boolean comment(String androidId, String contentUrl, String commentText) throws InterruptedException {
        long start = System.currentTimeMillis();

        //9.打开评论
        if (!click(new ClickVo(androidId, 1005, 1935))) {
            return false;
        }
        System.out.println("9.打开评论");
        Thread.sleep(3000);

        //10.评论的光标定位
        if (!click(new ClickVo(androidId, 183, 2016))) {
            return false;
        }
        System.out.println("10.评论的光标定位");
        Thread.sleep(2000);


        //11.输入评论
        if (!copy(new CopyVo(androidId, ""))) {
            return false;
        }
        if (!copy(new CopyVo(androidId, commentText))) {
            return false;
        }
        System.out.println("11.输入评论");
        Thread.sleep(5000);

        //12.发送评论
        if (!click(new ClickVo(androidId, 978, 1842))) {
            return false;
        }
        System.out.println("12.发送评论");
        Thread.sleep(2000);

        //13.关闭评论弹窗
        if (!click(new ClickVo(androidId, 453, 66))) {
            return false;
        }
        System.out.println("13.关闭评论弹窗");
        Thread.sleep(2000);


        clear(androidId);

        back(androidId);

        System.out.println(androidId + " 花费时间：" + (System.currentTimeMillis() - start));
        return true;
    }

    @Override
    public boolean browse(String androidId, String contentUrl, Integer browseTime) throws InterruptedException {
        long start = System.currentTimeMillis();

        Thread.sleep(browseTime * 1000);
        clear(androidId);

        back(androidId);

        System.out.println(androidId + " 花费时间：" + (System.currentTimeMillis() - start));
        return true;
    }

    @Override
    public boolean forward(String androidId, String contentUrl, String forwardFriendName) throws InterruptedException {

        long start = System.currentTimeMillis();

        //点击-转发图标
        if (!click(new ClickVo(androidId, 744, 1926))) {
            return false;
        }
        Thread.sleep(2000);

        //点击-转发给好友
        if (!click(new ClickVo(androidId, 81, 1461))) {
            return false;
        }
        Thread.sleep(2000);

        //点击输入框
        if (!click(new ClickVo(androidId, 375, 249))) {
            return false;
        }
        Thread.sleep(2000);

        //搜索框输入自己微信名
        if (!copy(new CopyVo(androidId, forwardFriendName))) {
            return false;
        }
        Thread.sleep(2000);

        //点击搜索出来的自己的头像-位置
        if (!click(new ClickVo(androidId, 75, 288))) {
            return false;
        }
        Thread.sleep(2000);

        //点击弹窗的发送按钮
        if (!click(new ClickVo(androidId, 756, 1518))) {
            return false;
        }
        Thread.sleep(5000);

        //todo 返回等操作  待完善

        clear(androidId);

        back(androidId);

        System.out.println(androidId + " 花费时间：" + (System.currentTimeMillis() - start));

        return true;
    }


    /**
     * 返回操作
     *
     * @param androidId
     * @return
     * @throws InterruptedException
     */
    abstract protected boolean back(String androidId) throws InterruptedException;


    /**
     * 一些清除操作
     *
     * @param androidId
     * @throws InterruptedException
     */
    abstract protected void clear(String androidId) throws InterruptedException;

}
