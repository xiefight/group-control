package cn.yunshi.groupcontrol.enums;

/**
 * @Description: 群控类型
 * @Author: xietao
 * @Date: 2023-10-18 10:47
 **/
public enum ControlTypeEnum {

    SUPPORT(1,"点赞"),
    COMMENT(2,"评论"),
    BROWSE(3,"浏览"),
    FORWARD(4,"转发"),
    DISLIKE(5,"踩"),
    WXFIRE(6,"小火苗"),

    ;


    private int code;
    private String desc;

    ControlTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
