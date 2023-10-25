package cn.yunshi.groupcontrol.common;

/**
 * @Description: 群控任务状态
 * @Author: xietao
 * @Date: 2023-10-19 16:46
 **/
public enum GroupTaskStatus {

    INIT(1,"初始化"),
    EXECUTE(2,"执行中"),
    SUCCESS(3,"执行完成"),
    FAIL(4,"执行失败")

    ;


    private int code;
    private String desc;

    GroupTaskStatus(int code, String desc) {
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
