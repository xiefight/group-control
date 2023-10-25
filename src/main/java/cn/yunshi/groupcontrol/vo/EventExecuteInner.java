package cn.yunshi.groupcontrol.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description:
 * @Author: xietao
 * @Date: 2023-10-24 15:33
 **/
@AllArgsConstructor
public class EventExecuteInner {

    //事件类型
    @Setter
    @Getter
    private Integer eventType;
    @Getter
    private Integer totalNum;
    //成功数量
    @Getter
    private Integer sucNum;
    //失败数量
    @Getter
    private Integer failNum;
    //正在执行数量
    @Getter
    private Integer exeNum;

    public void setTotalNum() {
        this.totalNum++;
    }

    public void setSucNum() {
        this.sucNum++;
    }

    public void setFailNum() {
        this.failNum++;
    }

    public void setExeNum() {
        this.exeNum++;
    }

}
