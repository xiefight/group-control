package cn.yunshi.groupcontrol.util;

import cn.yunshi.groupcontrol.exception.InputErrorException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {

    private static final String SDF_DATE_ZH_STR = "yyyy年MM月dd日 HH:mm";
    public static final String SDF_DATETIME_STR = "yyyy-MM-dd HH:mm:ss";
    private static final String SDF_DATE_STR = "yyyy-MM-dd";
    private static final String SDF_DATE_STR_FILENAME = "yyyyMMdd";
    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

    private static final Object LOCK_OBJ = new Object();
    private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<String, ThreadLocal<SimpleDateFormat>>();

    private static SimpleDateFormat getSdf(final String pattern) {
        ThreadLocal<SimpleDateFormat> tl = sdfMap.get(pattern);

        // 此处的双重判断和同步是为了防止sdfMap这个单例被多次put重复的sdf
        if (tl != null) {
            return tl.get();
        }
        synchronized (LOCK_OBJ) {
            tl = sdfMap.get(pattern);
            if (tl == null) {
                tl = new ThreadLocal<SimpleDateFormat>() {
                    @Override
                    protected SimpleDateFormat initialValue() {
                        return new SimpleDateFormat(pattern, Locale.CHINA);
                    }
                };
                sdfMap.put(pattern, tl);
            }
        }
        return tl.get();
    }

    /**
     * 获取今天最后一分钟 , 23:59:59
     *
     * @param date
     * @author Xiaodai
     * @date 2018年12月14日 下午5:25:26
     */
    public static Date getDay235959(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        return cal.getTime();
    }

    /**
     * 获取今天凌晨 0点整的时间
     *
     * @param date
     * @author Xiaodai
     * @date 2018年12月14日 下午5:24:57
     */
    public static Date getDayZero(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取本周周一的时间类型
     *
     * @param date
     * @param setZero 是否将时分秒设置为 0
     * @author Xiaodai
     * @date 2018年12月14日 下午5:24:13
     */
    public static Date getThisWeekMonday(Date date, boolean setZero) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        if (setZero) {
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
        }
        return cal.getTime();
    }

    /**
     * 获取字符串解析时间(格式:yyyy-MM-dd HH:mm:ss),若是传入的字符串为空或解析异常, 则会返回当前时间
     *
     * @param timeStr
     * @return
     * @author Xiaodai
     * @date 2018年12月4日 下午1:51:57
     */
    public static Date getTimestampFromString(String timeStr) {
        if (StringUtils.isEmpty(timeStr)) {
            return new Date();
        }
        try {
            SimpleDateFormat sdf = getSdf(SDF_DATETIME_STR);
            Date parse = sdf.parse(timeStr);
            return parse;
        } catch (Exception e) {
            return new Date();
        }
    }

    /**
     * 获取字符串解析时间(格式:yyyy-MM-dd),若是传入的字符串为空或解析异常, 则会返回当前时间
     */
    public static Date getDateFromDateDayString(String timeStr) {
        if (StringUtils.isEmpty(timeStr)) {
            return new Date();
        }
        try {
            SimpleDateFormat sdf = getSdf(SDF_DATE_STR);
            Date parse = sdf.parse(timeStr);
            return parse;
        } catch (Exception e) {
            return new Date();
        }
    }

    /**
     * 由指定时间获取格式化字符串 yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     * @author Xiaodai
     * @date 2019年4月28日 下午4:46:29
     */
    public static String getFormatStringFromDate(Date date) {
        if (null == date) {
            date = new Date();
        }
        SimpleDateFormat sdf = getSdf(SDF_DATETIME_STR);
        return sdf.format(date);
    }

    /**
     * 由指定时间获取格式化字符串 yyyyMMdd, 不指定时间, 默认为当前系统时间
     */
    public static String getFormatNameStringFromDate(Date date) {
        if (null == date) {
            date = new Date();
        }
        SimpleDateFormat sdf = getSdf(SDF_DATE_STR_FILENAME);
        String format = sdf.format(date);
        return format;
    }

    /**
     * 由指定时间获取格式化字符串 yyyy年MM月dd日 HH:mm
     *
     * @author Xiaodai
     * @date 2020年7月29日 下午3:05:43
     */
    public static String getFormatZhDateStringFromDate(Date date) {
        if (null == date) {
            date = new Date();
        }
        SimpleDateFormat sdf = getSdf(SDF_DATE_ZH_STR);
        String format = sdf.format(date);
        return format;
    }

    /**
     * 由指定时间获取格式化字符串 yyyy-MM-dd
     *
     * @param date
     * @return
     * @author
     */
    public static String getFormatDateStringFromDate(Date date) {
        if (null == date) {
            date = new Date();
        }
        SimpleDateFormat sdf = getSdf(SDF_DATE_STR);
        String format = sdf.format(date);
        return format;
    }

    /**
     * 获取字符串解析时间(格式:yyyy-MM-dd HH:mm:ss)
     *
     * @param timeStr
     * @param canNull 解析失败: true表示后返回 null, false表示返回当前时间
     * @author Xiaodai
     * @date 2018年12月4日 下午1:51:39
     */
    public static Date getTimestampFromString(String timeStr, boolean canNull) {
        if (StringUtils.isEmpty(timeStr)) {
            if (canNull) {
                return null;
            }
            return new Date();
        }
        try {
            SimpleDateFormat sdf = getSdf(SDF_DATETIME_STR);
            Date parse = sdf.parse(timeStr);
            return parse;
        } catch (Exception e) {
            if (canNull) {
                return null;
            }
            return new Date();
        }
    }


    /**
     * 获取文件时长 入参单位 秒
     *
     * @author Xiaodai
     * @date 2018年9月20日 下午3:31:26
     */
    public static String secondToTime(Integer time) {
        if (time == null) {
            return "00:00:00";
        }
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0) {
            return "00:00";
        } else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99) {
                    return "99:59:59";
                }
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    private static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10) {
            retStr = "0" + Integer.toString(i);
        } else {
            retStr = "" + i;
        }
        return retStr;
    }

    /**
     * 计算多少秒，分，小时，天之前的时间
     *
     * @param endTime
     * @param n       数字
     * @param unit    单位 S：秒,M:天，H：小时,D:天
     * @return
     * @throws ParseException
     */
    public static String getTimeBefore(String endTime, int n, String unit) throws ParseException {
        if (StringUtils.isEmpty(endTime) || StringUtils.isEmpty(unit)) {
            return "";
        }
        Date date = getSdf(SDF_DATETIME_STR).parse(endTime);// 解析成日期格式
        long mills = 0L;
        if ("S".equals(unit)) {
            mills = date.getTime() - n * 1000;
        } else if ("M".equals(unit)) {
            mills = date.getTime() - n * 60 * 1000;
        } else if ("H".equals(unit)) {
            mills = date.getTime() - n * 60 * 60 * 1000;
        } else if ("D".equals(unit)) {
            mills = date.getTime() - n * 24 * 60 * 60 * 1000;
        }
        date.setTime(mills);
        return getSdf(SDF_DATETIME_STR).format(date);
    }

    /**
     * 计算多少秒，分，小时，天之前的时间
     *
     * @param date
     * @param n    数字
     * @param unit 单位 S：秒,M:天，H：小时,D:天
     * @return
     * @throws ParseException
     */
    public static Date getTimeBefore(Date date, int n, String unit) throws ParseException {
        if (null == date || StringUtils.isEmpty(unit)) {
            return null;
        }
        date = new Date(date.getTime());
        long mills = 0L;
        if ("S".equals(unit)) {
            mills = date.getTime() - n * 1000;
        } else if ("M".equals(unit)) {
            mills = date.getTime() - n * 60 * 1000;
        } else if ("H".equals(unit)) {
            mills = date.getTime() - n * 60 * 60 * 1000;
        } else if ("D".equals(unit)) {
            mills = date.getTime() - n * 24 * 60 * 60 * 1000;
        }
        date.setTime(mills);
        return date;
    }

    /**
     * 获取两个日期字符串之间的日期集合
     *
     * @param startTime:String
     * @param endTime:String
     * @return list:yyyyMMdd
     */
    public static List<String> getBetweenDate(String startTime, String endTime, String dateFormat, int calendarType, int interval) {
        if (null == dateFormat) {
            dateFormat = "yyyy-MM-dd";
        }
        if (0 == calendarType) {
            calendarType = Calendar.DATE;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.CHINA);
        // 转化成日期类型
        Date startDate = null, endDate = null;
        try {
            startDate = sdf.parse(startTime);
            endDate = sdf.parse(endTime);
        } catch (ParseException e1) {
            logger.error("getBetweenDate.error:", e1);
            return null;
        }
        List<String> list = getBetweenDateFromDate(startDate, endDate, sdf, calendarType, interval, "".intern());
        return list;
    }

    /**
     * 获取两个日期字符串之间的日期集合
     *
     * @param startDate
     * @param endDate
     * @param dateFormat   格式化
     * @param calendarType 日历类型
     * @param interval     间隔
     * @param t            目标类, 仅支持数字和字符串, 当时数字类型时候, 需要 dateFormat 可以通过解析获取到Long类型的数据
     * @return
     * @author Xiaodai
     * @date 2019年6月20日 下午6:59:52
     */
    public static <T> List<T> getBetweenDate(Date startDate, Date endDate, String dateFormat, int calendarType, int interval, T t) {
        if (null == dateFormat) {
            dateFormat = "yyyy-MM-dd";
        }
        if (0 == calendarType) {
            calendarType = Calendar.DATE;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.CHINA);
        List<T> list = getBetweenDateFromDate(startDate, endDate, sdf, calendarType, interval, t);
        return list;
    }

    @SuppressWarnings("unchecked")
    private static <T> List<T> getBetweenDateFromDate(Date startDate, Date endDate, SimpleDateFormat sdf,
                                                      int calendarType, int interval, T t) {
        List<T> list = new ArrayList<T>();
        if (startDate.getTime() > endDate.getTime()) {
            logger.error("getBetweenDateFromDate 开始时间比结束时间大!");
            return list;
        }
        try {
            //用Calendar 进行日期比较判断
            Calendar calendar = Calendar.getInstance();
            while (startDate.getTime() <= endDate.getTime()) {
                // 把日期添加到集合
                String format = sdf.format(startDate);
                if (t instanceof Number) {
                    Object parseLong = Long.parseLong(format);
                    list.add((T) parseLong);
                } else {
                    list.add((T) format);
                }
                // 设置日期
                calendar.setTime(startDate);
                //把日期增加一天
                calendar.add(calendarType, interval);
                // 获取增加后的日期
                startDate = calendar.getTime();
            }
        } catch (Exception e) {
            logger.error("getBetweenDateFromDate.error:", e);
        }
        return list;
    }

    //转换日期时间格式
    public static String dateChange(String date, String inputFormat, String outputFormat) {
        if (StringUtils.isEmpty(date) || StringUtils.isEmpty(inputFormat) || StringUtils.isEmpty(outputFormat)) {
            return "";
        }
        String returndate = "";
        SimpleDateFormat inputdateFormat = new SimpleDateFormat(inputFormat, Locale.CHINA);
        SimpleDateFormat outputdateFormat = new SimpleDateFormat(outputFormat, Locale.CHINA);
        try {
            returndate = outputdateFormat.format(inputdateFormat.parse(date));
        } catch (ParseException e) {
            logger.error("dateChange.error:", e);
        }
        return returndate;
    }

    //获取前num月第一天，如上月，传1，默认查询上月
    public static String getBeforeMonthStart(Integer num) {
        Calendar c = Calendar.getInstance();
        if (num != null) {
            c.add(Calendar.MONTH, 0 - num);
        } else {
            c.add(Calendar.MONTH, -1);
        }
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
        SimpleDateFormat startSdf = getSdf("yyyy-MM-01 00:00:00");
        return startSdf.format(c.getTime());
    }

    //获取前num月第一天，如上月，传1，默认查询上月
    public static Date getBeforeMonthStart(Date date, Integer num) {
        Calendar c = Calendar.getInstance();
        if (null != date) {
            c.setTime(date);
        }
        if (num != null) {
            c.add(Calendar.MONTH, 0 - num);
        } else {
            c.add(Calendar.MONTH, -1);
        }
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1, 0, 0, 1);
        return c.getTime();
    }

    //获取前num月最后一天，如上月，传1，默认查询上月
    public static String getBeforeMonthEnd(Integer num) {
        Calendar c = Calendar.getInstance();
        if (num != null) {
            c.add(Calendar.MONTH, 0 - num);
        } else {
            c.add(Calendar.MONTH, -1);
        }
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
        SimpleDateFormat endSdf = getSdf("yyyy-MM-dd HH:mm:ss");
        return endSdf.format(c.getTime());
    }

    //获取前num月最后一天，如上月，传1，默认查询上月
    public static Date getBeforeMonthEnd(Date date, Integer num, boolean relativeRatio) {
        Calendar c = Calendar.getInstance();
        if (null != date) {
            c.setTime(date);
        }
        Integer day = null;
        day = c.get(Calendar.DAY_OF_MONTH);
        if (num != null) {
            c.add(Calendar.MONTH, 0 - num);
        } else {
            c.add(Calendar.MONTH, -1);
        }
        int actualMaxDayOfMontthAftter = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (relativeRatio) {
            if (day >= actualMaxDayOfMontthAftter) {
                day = actualMaxDayOfMontthAftter;
            }
        } else {
            day = actualMaxDayOfMontthAftter;
        }
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), day, 23, 59, 59);
        return c.getTime();
    }

    /**
     * 获取月首
     *
     * @author Xiaodai
     * @date 2020年8月3日 下午9:03:24
     */
    public static Date getThisMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (null != date) {
            calendar.setTime(date);
        }
        calendar.set(Calendar.DATE, 1);
        return calendar.getTime();
    }

    /**
     * @param date                时间, 可为null, 默认当前时间
     * @param num                 回退的时间天数
     * @param zeroOrEndHourMinute 是否将分钟 等都置位: true: 00:00:00 或者false: 23:59:59, 为null, 则不改变时间数值
     * @author Xiaodai
     * @date 2019年12月25日 下午4:28:16
     */
    public static Date getBeforeDayZeroOrEnd(Date date, int num, Boolean zeroOrEndHourMinute) {
        if (null == date) {
            date = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) - num);
        if (null == zeroOrEndHourMinute) {
            return c.getTime();
        } else if (zeroOrEndHourMinute.booleanValue() == true) {
            c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        } else {
            c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        }
        return c.getTime();
    }

    /**
     * 获取两个时间之间间隔天数
     *
     * @param before 前时间
     * @param after  后时间
     * @return int 天数
     */
    public static int getDaysBetweenTwoDates(Date before, Date after) {
        long oneDay = 24 * 60 * 60 * 1000L;
        Calendar cal = Calendar.getInstance();
        cal.setTime(before);
        long beforeTime = cal.getTimeInMillis();
        cal.setTime(after);
        long afterTime = cal.getTimeInMillis();
        long days = (afterTime - beforeTime) / oneDay;
        return Integer.parseInt(String.valueOf(days)) + 1;
    }

    /**
     * 时间戳转换为时间格式yyyy-MM-dd HH:mm:ss的字符串
     */
    public static String timeStampToDateString(Long timeStr) {
        SimpleDateFormat sdf = getSdf(SDF_DATETIME_STR);
        if (StringUtils.isEmpty(String.valueOf(timeStr))) {
            return sdf.format(new Date());
        }
        try {
            return sdf.format(new Date(Long.parseLong(String.valueOf(timeStr))));
        } catch (Exception e) {
            return sdf.format(new Date());
        }
    }


    /**
     * 匹配时间戳的格式, 返回开始或者结束时间
     *
     * @author Xiaodai
     * @date 2021/4/26 9:59
     */
    public static String getStartOrEndTimeStr(Map<String, Object> parameter, String key, boolean start) throws InputErrorException {
        String timeStr = String.valueOf(parameter.get(key));
        if (StringUtils.isEmpty(timeStr)) {
            return null;
        }
        if (timeStr.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
            return timeStr;
        } else if (timeStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
            if (start) {
                timeStr += " 00:00:00";
            } else {
                timeStr += " 23:59:59";
            }
        } else {
            throw new InputErrorException("{}: 时间戳格式异常: {}", key, timeStr);
        }
        return timeStr;
    }

    /**
     * 获取当前时间所在年份的1月1号
     *
     * @return
     * @Author: xietao
     * @Date: 2021/10/9 16:17
     */
    public static Date getFirstDayOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(cal.get(Calendar.YEAR), 0, 1, 0, 0, 0);
        return cal.getTime();
    }

    /**
     * 获取当前时间前推一年
     *
     * @return
     * @Author: xietao
     * @Date: 2021/10/9 16:17
     */
    public static Date getLastYearOfToday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, -1);
        return cal.getTime();
    }

    /**
     * 获取两个时间之间相隔的月份
     *
     * @param before 前时间
     * @param after  后时间
     * @return int 月份数量
     */
    public static int getMonthsBetweenTwoDates(String before, String after) {
        Date beforeDate = getDateFromDateDayString(before);
        Date afterDate = getDateFromDateDayString(after);
        return getMonthsBetweenTwoDates(beforeDate, afterDate);
    }

    public static int getMonthsBetweenTwoDates(Date before, Date after) {
        int beforeMonth = cn.hutool.core.date.DateUtil.month(before) + 1;
        int afterMonth = cn.hutool.core.date.DateUtil.month(after) + 1;
        if (beforeMonth > afterMonth) return 1;
        return afterMonth - beforeMonth + 1;
    }


    /**
     * <pre>
     * 从参数中获取指定的key的时间字符串并转化为时间对象
     * 会将时间字符串自动补全为  yyyy-MM-dd HH:mm:ss 的格式然后格式化为date对象
     * </pre>
     *
     * @param isEnd 是否结束时间, 结束时间时,且时间格式为 yyyy-MM-dd 会自动补全 23:59:59
     * @throws Exception 解析异常会抛出相关的异常
     */
    public static Date getStartOrEndTime(String timeStr, String key, boolean isEnd) throws InputErrorException {
        if (StringUtils.isEmpty(timeStr)) {
            throw new InputErrorException("参数 " + key + " 指定的时间格式不能为空");
        }
        try {
            if (timeStr.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}-\\d{2}-\\d{2}")) {
                return DateUtil.getTimestampFromString(timeStr);
            }
            if (timeStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
                timeStr += (isEnd ? " 23:59:59" : " 00:00:00");
                return DateUtil.getTimestampFromString(timeStr);
            }
            throw new InputErrorException("参数 " + key + " 指定的时间格式解析异常");
        } catch (Exception e) {
            throw new InputErrorException("参数 " + key + " 指定的时间解析异常");
        }
    }

}
