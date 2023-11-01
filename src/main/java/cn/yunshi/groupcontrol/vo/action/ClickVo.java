package cn.yunshi.groupcontrol.vo.action;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Description: 点击事件vo
 * @Author: xietao
 * @Date: 2023-10-23 16:00
 **/
@Data
@AllArgsConstructor
public class ClickVo {

    //安卓设备id
    private String id;
    //x坐标
    private Integer x;
    //y坐标
    private Integer y;

}
