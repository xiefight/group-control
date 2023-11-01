package cn.yunshi.groupcontrol.vo;

import lombok.Data;

/**
 * @Description: 微信视频号-视频号名字全称-搜索视频 额外参数
 * @Author: xietao
 * @Date: 2023-11-01 16:06
 **/
@Data
public class WeixinExtraVo {
    //视频号的名称
    private String weixinVideoName;
    //视频序列号--不填默认第一条视频
    private Integer weixinVideoSort;
}
