package cn.yunshi.groupcontrol.vo.action;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Description: 文本复制vo
 * @Author: xietao
 * @Date: 2023-10-23 16:56
 **/
@Data
@AllArgsConstructor
public class CopyVo {

    //安卓设备id
    private String id;

    //要复制的文本
    private String text;

}
