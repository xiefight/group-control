package cn.yunshi.groupcontrol.service.impl;

import cn.yunshi.groupcontrol.service.BaseScriptService;
import cn.yunshi.groupcontrol.vo.action.ClickVo;
import cn.yunshi.groupcontrol.vo.action.CopyVo;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * @Description: 抖音平台群控逻辑封装
 * @Author: xietao
 * @Date: 2023-10-18 17:41
 **/
@Primary
@Service("douyinScriptService")
public class DouyinScriptServiceImpl extends BaseScriptService {


    @Override
    public boolean openAppFindVideo(String androidId, String contentUrl) throws InterruptedException {
        //1.点击home按钮到桌面
        if (!click(new ClickVo(androidId, 543, 2157))) {
            return false;
        }

        Thread.sleep(2000);

        //2.点击抖音坐标
        if (!click(new ClickVo(androidId, 141, 267))) {
            return false;
        }
        Thread.sleep(2000);
        //3.点击右上角搜索按钮，弹出搜索框
        if (!click(new ClickVo(androidId, 1011, 117))) {
            return false;
        }
        Thread.sleep(2000);
        //4.将url复制到搜索框中
        if (!copy(new CopyVo(androidId, contentUrl))) {
            return false;
        }
        Thread.sleep(2000);
        //5.并点击搜索
        if (!click(new ClickVo(androidId, 966, 114))) {
            return false;
        }

        return true;
    }

    @Override
    public boolean support(String androidId, String contentUrl) throws InterruptedException {
        long start = System.currentTimeMillis();

        this.openAppFindVideo(androidId, contentUrl);

        Thread.sleep(5000);
        //6.点赞按钮
        if (!click(new ClickVo(androidId, 999, 1131))) {
            return false;
        }

        Thread.sleep(3000);

        back(androidId);
        System.out.println(androidId + "点赞 花费时间：" + (System.currentTimeMillis() - start));
        return true;
    }

    /**
     * @param androidId  设备id
     * @param contentUrl 要操作的内容的url
     */
    @Override
    public boolean comment(String androidId, String contentUrl, String commentText) throws InterruptedException {
        long start = System.currentTimeMillis();

        this.openAppFindVideo(androidId, contentUrl);

        Thread.sleep(5000);
        //6.点击评论按钮
        if (!click(new ClickVo(androidId, 1002, 1305))) {
            return false;
        }
        Thread.sleep(5000);
        //7.点击评论输入框
        if (!click(new ClickVo(androidId, 156, 2022))) {
            return false;
        }
        Thread.sleep(2000);
        //8.输入评论
        if (!copy(new CopyVo(androidId, commentText))) {
            return false;
        }
        Thread.sleep(5000);
        //9.点击评论-发送按钮
        if (!click(new ClickVo(androidId, 1008, 1785))) {
            return false;
        }
        Thread.sleep(1000);
        //10.关闭评论弹窗
        if (!click(new ClickVo(androidId, 453, 66))) {
            return false;
        }
        Thread.sleep(3000);
        back(androidId);
        System.out.println(androidId + "评论 花费时间：" + (System.currentTimeMillis() - start));

        return true;
    }

    @Override
    public boolean browse(String androidId, String contentUrl, Integer browseTime) throws InterruptedException {
        long start = System.currentTimeMillis();

        this.openAppFindVideo(androidId, contentUrl);

        Thread.sleep(browseTime * 1000);

        //6.播放完成之后点击屏幕任意一处 暂停视频 判断任务完成
        if (!click(new ClickVo(androidId, 453, 66))) {
            return false;
        }

        back(androidId);

        System.out.println(androidId + "浏览 花费时间：" + (System.currentTimeMillis() - start));

        return true;
    }

    @Override
    public boolean forward(String androidId, String contentUrl, String forwardFriendName) throws InterruptedException {
        long start = System.currentTimeMillis();

        this.openAppFindVideo(androidId, contentUrl);
        Thread.sleep(5000);

        //点击转发图标按钮
        if (!click(new ClickVo(androidId, 1002, 1500))) {
            return false;
        }

        Thread.sleep(3000);

        //点击-转发黄色按钮
        if (!click(new ClickVo(androidId, 114, 1665))) {
            return false;
        }

        Thread.sleep(3000);

        //点击-转发日常按钮
        if (!click(new ClickVo(androidId, 632, 2007))) {
            return false;
        }

//        back(androidId);

        System.out.println(androidId + "转发 花费时间：" + (System.currentTimeMillis() - start));

        return true;
    }


    /**
     * 返回
     */
    private boolean back(String androidId) throws InterruptedException {
        if (!click(new ClickVo(androidId, 66, 120))) {
            return false;
        }

        Thread.sleep(3000);

        if (!click(new ClickVo(androidId, 66, 120))) {
            return false;
        }

        Thread.sleep(3000);

        if (!click(new ClickVo(androidId, 66, 120))) {
            return false;
        }

        Thread.sleep(3000);

        return true;
    }
}
