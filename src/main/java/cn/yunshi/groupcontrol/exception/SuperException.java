package cn.yunshi.groupcontrol.exception;

import java.util.regex.Matcher;

/**
 * @Description:
 * @Author: xietao
 * @Date: 2023-10-24 14:46
 **/
public class SuperException extends Exception {

    private static final String place = "\\{\\}";

    public SuperException() {}

    public SuperException(String msg) {
        super(msg);
    }







    public SuperException(String msg, Object... args) {
        super(format(msg, args));
    }

    protected static String format(String msg, Object... args) {
        if (null == msg || msg.trim().length() == 0) {
            return "";
        }
        if (null == args || args.length == 0) {
            return msg;
        }

        for (Object arg : args) {
            String strArg = String.valueOf(arg);
            strArg = Matcher.quoteReplacement(strArg);
            msg = msg.replaceFirst("\\{\\}", strArg);
        }

        return msg;
    }

}
