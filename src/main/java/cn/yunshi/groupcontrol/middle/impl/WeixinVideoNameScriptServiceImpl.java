package cn.yunshi.groupcontrol.middle.impl;

import cn.yunshi.groupcontrol.entity.GroupTaskEntity;
import cn.yunshi.groupcontrol.vo.action.ClickVo;
import cn.yunshi.groupcontrol.vo.action.CopyVo;
import cn.yunshi.groupcontrol.vo.action.SwipeVo;
import org.springframework.stereotype.Service;

/**
 * 微信视频号--通过在视频号中搜索公众号的名字，然后滑动查找视频的方式，进行群控操作
 */
@Service("weixinNameService")
public class WeixinVideoNameScriptServiceImpl extends BaseWeixinVideoScriptService {

    @Override
    public boolean openAppFindVideo(String androidId, String contentUrl, GroupTaskEntity groupTaskEntity) throws InterruptedException {

        //1.点击home按钮到桌面
        if (!click(new ClickVo(androidId, 543, 2157))) {
            return false;
        }
        System.out.println("1.点击home按钮到桌面");
        Thread.sleep(2000);

        //2.点击微信坐标-打开微信
        if (!click(new ClickVo(androidId, 339, 261))) {
            return false;
        }
        System.out.println("2.点击微信坐标-打开微信");
        Thread.sleep(2000);
        //3.点击微信坐标-点击发现
        if (!click(new ClickVo(androidId, 675, 1998))) {
            return false;
        }
        System.out.println("3.点击微信坐标-点击发现");
        Thread.sleep(2000);

        //4.点击-视频号
        if (!click(new ClickVo(androidId, 168, 372))) {
            return false;
        }
        System.out.println("4.点击-视频号");
        Thread.sleep(2000);

        //5.点击-搜索框
        if (!click(new ClickVo(androidId, 906, 123))) {
            return false;
        }
        System.out.println("5.点击-搜索框");
        Thread.sleep(3000);

        //6.输入要搜索的视频号的名称（名称后台输入配置）
        if (!copy(new CopyVo(androidId, groupTaskEntity.getWeixinVideoName()))) {
            return false;
        }
        System.out.println("6.输入要搜索的视频号的名称（名称后台输入配置）");
        Thread.sleep(3000);

        //7.点击搜索按钮
        if (!click(new ClickVo(androidId, 960, 117))) {
            return false;
        }
        System.out.println("7.点击搜索按钮");
        Thread.sleep(2000);

        //8.点击-搜索出来的列表项
        if (!click(new ClickVo(androidId, 240, 474))) {
            return false;
        }
        System.out.println("8.点击-搜索出来的列表项");
        Thread.sleep(5000);

        //9.点击-第一条视频进入滑动列表
        if (!click(new ClickVo(androidId, 200, 1167))) {
            return false;
        }
        System.out.println("9.点击-第一条视频进入滑动列表");
        Thread.sleep(2000);
        //10.开始滑动，找到相关视频
        for (int i = 0; i < groupTaskEntity.getWeixinVideoNameSort() - 1; i++) {
            swipeUp(new SwipeVo(androidId));
            Thread.sleep(2000);
        }
        System.out.println("10.开始滑动，找到相关视频");
        Thread.sleep(5000);

        return true;
    }

    public boolean back(String androidId) throws InterruptedException {
        //14.返回
        if (!click(new ClickVo(androidId, 63, 126))) {
            return false;
        }
        System.out.println("14.返回");
        Thread.sleep(3000);
        //15.返回
        if (!click(new ClickVo(androidId, 63, 126))) {
            return false;
        }
        Thread.sleep(3000);
        //16.点取消-返回
        if (!click(new ClickVo(androidId, 63, 126))) {
            return false;
        }
        Thread.sleep(3000);
        //16.点取消-返回
        if (!click(new ClickVo(androidId, 63, 126))) {
            return false;
        }
        Thread.sleep(3000);
        //16.点取消-返回
        if (!click(new ClickVo(androidId, 63, 126))) {
            return false;
        }
        return true;
    }


    /**
     * 一些清除操作
     *
     * @param androidId
     * @throws InterruptedException
     */
    public void clear(String androidId) throws InterruptedException {
    }


}
