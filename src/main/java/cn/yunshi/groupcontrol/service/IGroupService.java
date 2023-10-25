package cn.yunshi.groupcontrol.service;

import cn.yunshi.groupcontrol.common.Response;
import cn.yunshi.groupcontrol.exception.InputErrorException;
import cn.yunshi.groupcontrol.vo.GroupTaskReqVo;

import java.util.Map;

/**
 * @Description:
 * @Author: xietao
 * @Date: 2023-10-18 15:34
 **/
public interface IGroupService {
    Response reportTask(Map<String, Object> param) throws InterruptedException;

    Response listTask(GroupTaskReqVo groupTaskReqVo) throws InputErrorException;
}
