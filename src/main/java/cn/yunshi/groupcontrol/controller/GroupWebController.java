package cn.yunshi.groupcontrol.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.yunshi.groupcontrol.bo.UserBo;
import cn.yunshi.groupcontrol.common.Response;
import cn.yunshi.groupcontrol.service.IControlScriptService;
import cn.yunshi.groupcontrol.service.IGroupWebService;
import cn.yunshi.groupcontrol.util.JsonParameterUtil;
import cn.yunshi.groupcontrol.vo.GroupTaskReqVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Description: 接受前端的群控请求
 * @Author: xietao
 * @Date: 2023-10-18 14:43
 **/
@RestController
@RequestMapping("group")
public class GroupWebController {

    @Autowired
    private IGroupWebService groupService;

    @Autowired
    private IControlScriptService scriptService;

    @RequestMapping("task/report")
    public Response reportTask(@RequestBody String reqBody) {
        try {
            Map<String, Object> param = JsonParameterUtil.jsonToMap(reqBody, Exception.class);
            return groupService.reportTask(param);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @RequestMapping("task/list")
    public Response listTask(@RequestBody GroupTaskReqVo groupTaskReqVo) {
        try {
//            Map<String, Object> param = JsonParameterUtil.jsonToMap(reqBody, Exception.class);
            return groupService.listTask(groupTaskReqVo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }



    /*@RequestMapping("task/getAndroidIds")
    public void getAndroidIds() {
        try {
            scriptService.getAndroidIds();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


    @RequestMapping("getUser")
    public void testMapping(@RequestBody String reqBody) throws Exception {
        Map<String, Object> param = JsonParameterUtil.jsonToMap(reqBody, Exception.class);
        UserBo userBo = new UserBo();
        BeanUtil.fillBeanWithMap(param, userBo, true);
        System.out.println("userBo==> " + userBo);
    }

}
