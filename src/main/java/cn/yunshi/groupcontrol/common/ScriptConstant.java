package cn.yunshi.groupcontrol.common;

/**
 * @Description: 群控脚本常量
 * @Author: xietao
 * @Date: 2023-10-18 17:35
 **/
public class ScriptConstant {

    /**
     * 群控脚本服务器地址
     * 放在数据库或配置文件中，不同客户动态配置
     */
    public static final String NODE_SCRIPT_PATH = "http://192.168.3.200:9000/";

    /**
     * 获取在线设备列表uri
     */
    public static final String ANDROID_ID_URI = "getIds";

    /**
     * 点击事件click
     */
    public static final String CLICK = "click";

    /**
     * 粘贴文件路径到搜索框
     */
    public static final String COPY_FILE_URL = "setText";



}
