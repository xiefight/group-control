package cn.yunshi.groupcontrol.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.yunshi.groupcontrol.common.ScriptConstant;
import cn.yunshi.groupcontrol.vo.action.ClickVo;
import cn.yunshi.groupcontrol.vo.action.CopyVo;
import cn.yunshi.groupcontrol.vo.action.SwipeVo;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Description: 请求脚本的基本方法
 * @Author: xietao
 * @Date: 2023-10-23 14:59
 **/
public abstract class BaseScriptService implements IControlScriptService {

    /**
     * 获取已连接的设备id集合
     */
    public List<String> getAndroidIds() {
        List<String> androidBotIdsArr = new ArrayList<>();
        String url = ScriptConstant.NODE_SCRIPT_PATH + ScriptConstant.ANDROID_ID_URI;
        //http请求
        HttpResponse response = HttpRequest.post(url).execute();
        if (HttpStatus.HTTP_OK == response.getStatus()) {
            String body = response.body();
            HashMap androidIds = JSONObject.parseObject(body, HashMap.class);
            androidBotIdsArr = (List<String>) androidIds.get("androidBotIdsArr");
            System.out.println(androidBotIdsArr);
        } else {
            System.out.println("获取ids失败");
        }
        return CollUtil.isEmpty(androidBotIdsArr) ? new ArrayList<>() : androidBotIdsArr;
    }

    /**
     * 点击事件
     */
    public boolean click(ClickVo clickVo) {
        String url = ScriptConstant.NODE_SCRIPT_PATH + ScriptConstant.CLICK;
        //http请求
        try {
            JSONObject json = new JSONObject();
            json.put("id", clickVo.getId());
            json.put("x", clickVo.getX());
            json.put("y", clickVo.getY());
            HttpResponse response = HttpRequest.post(url).body(json.toJSONString()).execute();
            if (HttpStatus.HTTP_OK != response.getStatus()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 复制内容到输入框事件
     */
    public boolean copy(CopyVo copyVo) {
        String url = ScriptConstant.NODE_SCRIPT_PATH + ScriptConstant.COPY_FILE_URL;
        //http请求
        try {
            JSONObject json = new JSONObject();
            json.put("id", copyVo.getId());
            json.put("text", copyVo.getText());
            HttpResponse response = HttpRequest.post(url).body(json.toJSONString()).execute();
            if (HttpStatus.HTTP_OK != response.getStatus()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    /**
     * 向上滑动方法
     */
    public boolean swipeUp(SwipeVo swipeVo) {
        String url = ScriptConstant.NODE_SCRIPT_PATH + ScriptConstant.SWIPE_UP;
        //http请求
        try {
            JSONObject json = new JSONObject();
            json.put("id", swipeVo.getId());
            HttpResponse response = HttpRequest.post(url).body(json.toJSONString()).execute();
            if (HttpStatus.HTTP_OK != response.getStatus()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }


}
