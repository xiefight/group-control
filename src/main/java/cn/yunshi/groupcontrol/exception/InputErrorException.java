package cn.yunshi.groupcontrol.exception;

/**
 * @Description:
 * @Author: xietao
 * @Date: 2023-10-24 14:45
 **/
public class InputErrorException extends SuperException {

    public InputErrorException() {}

    public InputErrorException(String msg) {
        super(msg);
    }







    public InputErrorException(String msg, Object... args) {
        super(format(msg, args));
    }

}
