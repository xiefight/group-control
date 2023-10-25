package cn.yunshi.groupcontrol.vo;

import cn.yunshi.groupcontrol.common.GroupTaskStatus;
import lombok.Data;

/**
 * @Description:
 * @Author: xietao
 * @Date: 2023-10-24 10:18
 **/
@Data
public class GroupTaskReqVo {

    private String platform;

    /**
     * 状态
     * {@link GroupTaskStatus}
     */
    private Integer status;

    private String startTime;
    private String endTime;

    private String page;
    private String limit;

}
