package cn.yunshi.groupcontrol.service.impl;

import cn.yunshi.groupcontrol.service.BaseScriptService;
import cn.yunshi.groupcontrol.vo.action.ClickVo;
import cn.yunshi.groupcontrol.vo.action.CopyVo;
import org.springframework.stereotype.Service;

/**
 * 微信视频号群控逻辑封装
 */
@Service("weixinScriptService")
public class WeixinVideoScriptServiceImpl extends BaseScriptService {


    @Override
    public boolean openAppFindVideo(String androidId, String contentUrl) throws InterruptedException {

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
        //3.点击搜索放大镜
        if (!click(new ClickVo(androidId, 906, 117))) {
            return false;
        }
        System.out.println("3.点击搜索放大镜");
        Thread.sleep(2000);

        //4.搜索框输入文件传输工具
        if (!copy(new CopyVo(androidId, "文件传输"))) {
            return false;
        }
        System.out.println("4.搜索框输入文件传输工具");
        Thread.sleep(2000);

        //5.点击-文件传输工具快捷入口
        if (!click(new ClickVo(androidId, 102, 441))) {
            return false;
        }
        System.out.println("5.点击-文件传输工具快捷入口");
        Thread.sleep(5000);

        //点击输入框
        if (!click(new ClickVo(androidId, 171, 2013))) {
            return false;
        }
        System.out.println("5.点击输入框");
        Thread.sleep(2000);

        //6.搜索框输入视频链接文案
        if (!copy(new CopyVo(androidId, ""))) {
            return false;
        }
        if (!copy(new CopyVo(androidId, "\\\n\\\n\\\n\\\n\\\n\\\n\\\n\\\n\\\n\\\n\\\n\\\n\\\n\\\n\\\n\\\n\\\n\\\n\\\n\\\n\\\n\\\n\\\n\\\n\\\n\\\n\\\n\\\n\\\n" + contentUrl))) {
            return false;
        }
        System.out.println("6.搜索框输入视频链接文案");
        Thread.sleep(2000);

        //7.点击发送链接
        if (!click(new ClickVo(androidId, 987, 1962))) {
            return false;
        }
        System.out.println("7.点击发送链接");
        Thread.sleep(2000);

        //8.打开链接
        if (!click(new ClickVo(androidId, 357, 1824))) {
            return false;
        }
        System.out.println("8.打开链接");

        return true;
    }

    @Override
    public boolean support(String androidId, String contentUrl) throws InterruptedException {
        long start = System.currentTimeMillis();

        this.openAppFindVideo(androidId, contentUrl);

        Thread.sleep(5000);

        //9.点赞
        if (!click(new ClickVo(androidId, 636, 1947))) {
            return false;
        }
        System.out.println("9.点赞");
        Thread.sleep(4000);

        //14.返回
        if (!click(new ClickVo(androidId, 51, 123))) {
            return false;
        }

        clear(androidId);

        System.out.println("14.返回");
        Thread.sleep(4000);
        //15.返回
        if (!click(new ClickVo(androidId, 51, 123))) {
            return false;
        }
        Thread.sleep(2000);
        //16.点取消-返回
        if (!click(new ClickVo(androidId, 1011, 123))) {
            return false;
        }

        System.out.println(androidId + " 花费时间：" + (System.currentTimeMillis() - start));
        return true;
    }

    /**
     * @param contentUrl 要操作的内容的url
     */
    @Override
    public boolean comment(String androidId, String contentUrl, String commentText) throws InterruptedException {
        long start = System.currentTimeMillis();

        this.openAppFindVideo(androidId, contentUrl);

        Thread.sleep(5000);

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

        //14.返回
        if (!click(new ClickVo(androidId, 51, 123))) {
            return false;
        }
        System.out.println("14.返回");
        Thread.sleep(4000);
        //15.返回
        if (!click(new ClickVo(androidId, 51, 123))) {
            return false;
        }
        Thread.sleep(2000);
        //16.点取消-返回
        if (!click(new ClickVo(androidId, 1011, 123))) {
            return false;
        }

        System.out.println(androidId + " 花费时间：" + (System.currentTimeMillis() - start));
        return true;
    }

    @Override
    public boolean browse(String androidId, String contentUrl, Integer browseTime) throws InterruptedException {
        long start = System.currentTimeMillis();

        this.openAppFindVideo(androidId, contentUrl);

        Thread.sleep(browseTime * 1000);

        clear(androidId);

        //14.返回
        if (!click(new ClickVo(androidId, 51, 123))) {
            return false;
        }
        System.out.println("14.返回");
        Thread.sleep(4000);
        //15.返回
        if (!click(new ClickVo(androidId, 51, 123))) {
            return false;
        }
        Thread.sleep(2000);
        //16.点取消-返回
        if (!click(new ClickVo(androidId, 1011, 123))) {
            return false;
        }

        System.out.println(androidId + " 花费时间：" + (System.currentTimeMillis() - start));
        return true;
    }

    @Override
    public boolean forward(String androidId, String contentUrl) throws InterruptedException {
        return false;
    }

    private void clear(String androidId) {
        //1.点击文件传输助手对话框右上角...
        click(new ClickVo(androidId, 1005, 126));

        //2.点击清空聊天记录
        click(new ClickVo(androidId, 525, 1188));

        //3.点击-弹窗清空选项
        click(new ClickVo(androidId, 738, 1167));
    }
}
