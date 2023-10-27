package cn.yunshi.groupcontrol.service.impl;

import cn.yunshi.groupcontrol.entity.GroupEventEntity;
import cn.yunshi.groupcontrol.service.BaseScriptService;
import cn.yunshi.groupcontrol.vo.ClickVo;
import cn.yunshi.groupcontrol.vo.CopyVo;
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
    public boolean support(GroupEventEntity groupEventEntity, String contentUrl) throws InterruptedException {
        long start = System.currentTimeMillis();

        //1.点击home按钮到桌面
        if (!click(new ClickVo(groupEventEntity.getAndroidId(), 543, 2157))) {
            return false;
        }

        Thread.sleep(2000);

        //2.点击抖音坐标
        if (!click(new ClickVo(groupEventEntity.getAndroidId(), 141, 267))) {
            return false;
        }
        Thread.sleep(2000);
        //3.点击右上角搜索按钮，弹出搜索框
        if (!click(new ClickVo(groupEventEntity.getAndroidId(), 1011, 117))) {
            return false;
        }
        Thread.sleep(2000);
        //4.将url复制到搜索框中
        if (!copy(new CopyVo(groupEventEntity.getAndroidId(), contentUrl))) {
            return false;
        }
        Thread.sleep(2000);
        //5.并点击搜索
        if (!click(new ClickVo(groupEventEntity.getAndroidId(), 966, 114))) {
            return false;
        }
        Thread.sleep(5000);

        //6.点赞按钮
        if (!click(new ClickVo(groupEventEntity.getAndroidId(), 999, 1131))) {
            return false;
        }

        System.out.println(groupEventEntity.getAndroidId() + " 花费时间：" + (System.currentTimeMillis() - start));
        return true;
    }

    /**
     * @param groupEventEntity 事件实体
     * @param contentUrl       要操作的内容的url
     */
    @Override
    public boolean comment(GroupEventEntity groupEventEntity, String contentUrl) throws InterruptedException {
        long start = System.currentTimeMillis();

        //1.点击home按钮到桌面
        if (!click(new ClickVo(groupEventEntity.getAndroidId(), 543, 2157))) {
            return false;
        }

        Thread.sleep(2000);

        //2.点击抖音坐标
        if (!click(new ClickVo(groupEventEntity.getAndroidId(), 141, 267))) {
            return false;
        }
        Thread.sleep(2000);
        //3.点击右上角搜索按钮，弹出搜索框
        if (!click(new ClickVo(groupEventEntity.getAndroidId(), 1011, 117))) {
            return false;
        }
        Thread.sleep(2000);
        //4.将url复制到搜索框中
        if (!copy(new CopyVo(groupEventEntity.getAndroidId(), contentUrl))) {
            return false;
        }
        Thread.sleep(2000);
        //5.并点击搜索
        if (!click(new ClickVo(groupEventEntity.getAndroidId(), 966, 114))) {
            return false;
        }
        Thread.sleep(5000);
        //6.点击评论按钮
        if (!click(new ClickVo(groupEventEntity.getAndroidId(), 1002, 1305))) {
            return false;
        }
        Thread.sleep(5000);
        //7.点击评论输入框
        if (!click(new ClickVo(groupEventEntity.getAndroidId(), 156, 2022))) {
            return false;
        }
        Thread.sleep(2000);
        //8.输入评论
        if (!copy(new CopyVo(groupEventEntity.getAndroidId(), groupEventEntity.getCommentText()))) {
            return false;
        }
        Thread.sleep(5000);
        //9.点击评论-发送按钮
        if (!click(new ClickVo(groupEventEntity.getAndroidId(), 1008, 1785))) {
            return false;
        }
        Thread.sleep(1000);
        //10.关闭评论弹窗
        if (!click(new ClickVo(groupEventEntity.getAndroidId(), 453, 66))) {
            return false;
        }
        System.out.println(groupEventEntity.getAndroidId() + " 花费时间：" + (System.currentTimeMillis() - start));
        return true;
    }

    @Override
    public boolean browse(GroupEventEntity groupEventEntity, String contentUrl) throws InterruptedException {
        long start = System.currentTimeMillis();

        //1.点击home按钮到桌面
        if (!click(new ClickVo(groupEventEntity.getAndroidId(), 543, 2157))) {
            return false;
        }

        Thread.sleep(2000);

        //2.点击抖音坐标
        if (!click(new ClickVo(groupEventEntity.getAndroidId(), 141, 267))) {
            return false;
        }
        Thread.sleep(2000);
        //3.点击右上角搜索按钮，弹出搜索框
        if (!click(new ClickVo(groupEventEntity.getAndroidId(), 1011, 117))) {
            return false;
        }
        Thread.sleep(2000);
        //4.将url复制到搜索框中
        if (!copy(new CopyVo(groupEventEntity.getAndroidId(), contentUrl))) {
            return false;
        }
        Thread.sleep(2000);
        //5.并点击搜索
        if (!click(new ClickVo(groupEventEntity.getAndroidId(), 966, 114))) {
            return false;
        }
        Thread.sleep(groupEventEntity.getBrowseTime() * 1000);


        //6.播放完成之后点击屏幕任意一处 暂停视频 判断任务完成
        if (!click(new ClickVo(groupEventEntity.getAndroidId(), 453, 66))) {
            return false;
        }

        System.out.println(groupEventEntity.getAndroidId() + " 花费时间：" + (System.currentTimeMillis() - start));
        return true;
    }
}
