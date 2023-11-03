package cn.yunshi.groupcontrol.middle.impl;

import cn.yunshi.groupcontrol.vo.action.ClickVo;
import cn.yunshi.groupcontrol.vo.action.CopyVo;
import org.springframework.stereotype.Service;

/**
 * 微信视频号--通过在视频号中搜索公众号的名字，然后滑动查找视频的方式，进行群控操作
 */
@Service("weixinNameService")
public class WeixinVideoNameScriptServiceImpl extends BaseWeixinVideoScriptService {

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

    public boolean back(String androidId) throws InterruptedException {
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
        return true;
    }


    /**
     * 一些清除操作
     *
     * @param androidId
     * @throws InterruptedException
     */
    public void clear(String androidId) throws InterruptedException {
        //1.点击文件传输助手对话框右上角...
        click(new ClickVo(androidId, 1005, 126));
        Thread.sleep(2000);

        //2.点击清空聊天记录
        click(new ClickVo(androidId, 525, 1188));
        Thread.sleep(2000);

        //3.点击-弹窗清空选项
        click(new ClickVo(androidId, 738, 1167));
        Thread.sleep(2000);
    }


}
