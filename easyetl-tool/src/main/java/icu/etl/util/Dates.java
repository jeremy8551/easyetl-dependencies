package icu.etl.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * 日期工具类
 *
 * @author jeremy8551@qq.com
 * @createtime 2010-01-08
 */
public final class Dates {

    /** 频率是天 */
    public final static String FREQ_DAY = "day";

    /** 月初第一天 */
    public final static String FREQ_beginOfMonth = "beginOfMonth";

    /** 月末最后一天 */
    public final static String FREQ_endOfMonth = "endOfMonth";

    /** 年初第一天 */
    public final static String FREQ_beginOfYear = "beginOfYear";

    /** 年末最后一天 */
    public final static String FREQ_endOfYear = "endOfYear";

    /** 季初第一天 */
    public final static String FREQ_beginOfQuarter = "beginOfQuarter";

    /** 季末最后一天 */
    public final static String FREQ_endOfQuarter = "endOfQuarter";

    /** 一月 */
    public final static String January = "January";

    /** 二月 */
    public final static String February = "February";

    /** 三月 */
    public final static String March = "March";

    /** 四月 */
    public final static String April = "April";

    /** 五月 */
    public final static String May = "May";

    /** 六月 */
    public final static String June = "June";

    /** 七月 */
    public final static String July = "July";

    /** 八月 */
    public final static String August = "August";

    /** 九月 */
    public final static String September = "September";

    /** 十月 */
    public final static String October = "October";

    /** 十一月 */
    public final static String November = "November";

    /** 十二月 */
    public final static String December = "December";

    /** 周一 */
    public final static String Monday = "Monday";

    /** 周二 */
    public final static String Tuesday = "Tuesday";

    /** 周三 */
    public final static String Wednesday = "Wednesday";

    /** 周四 */
    public final static String Thursday = "Thursday";

    /** 周五 */
    public final static String Friday = "Friday";

    /** 周六 */
    public final static String Saturday = "Saturday";

    /** 周末 */
    public final static String Sunday = "Sunday";

    /** 时间表达式的分隔符：小时和分钟之间的分隔符，分和秒之间的分隔符 */
    public static List<String> TIME_DELIMITER = ArrayUtils.asList(":", "：", ".", "。", "-", "_");

    /** 日期中的分隔符，例如：年与月之间的分隔符、月与日之间的分隔符 */
    public static char[] DATE_DELIMITER = {'-', '/', '|', '\\', '_', ':', '：', '.', '。'};

    public Dates() {
    }

    /**
     * 使当前线程进入休眠
     *
     * @param timeout 休眠的时间
     * @param unit    时间单位
     * @return 返回异常信息, 返回null表示没有发生错误
     */
    public static boolean sleep(long timeout, TimeUnit unit) {
        try {
            unit.sleep(timeout);
            return true;
        } catch (Throwable e) {
            if (JUL.isWarnEnabled()) {
                JUL.warn(e.getLocalizedMessage(), e);
            }
            return false;
        }
    }

    /**
     * 使当前线程进入休眠
     *
     * @param millis 毫秒数
     * @return 返回异常信息, 返回null表示没有发生错误
     */
    public static Throwable sleep(long millis) {
        long start = System.currentTimeMillis();
        try {
            Thread.sleep(millis);
            return null;
        } catch (Throwable e) {
            if (JUL.isDebugEnabled()) {
                JUL.debug(e.getLocalizedMessage(), e);
            }

            do {
                if ((System.currentTimeMillis() - start) >= millis) {
                    break;
                }
            } while (true);
            return e;
        }
    }

    /**
     * 返回时间戳 yyyy-MM-dd HH:mm:ss
     *
     * @return 字符串
     */
    public static String currentTimeStamp() {
        return Dates.format19(new Date());
    }

    /**
     * 返回当前日期
     *
     * @return 日期
     */
    public static Date currentDate() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * 尝试用 {@linkplain #parse(Object)} 方法解析日期
     *
     * @param obj 日期信息
     * @return 返回 null 表示输入参数格式错误
     */
    public static Date testParse(Object obj) {
        try {
            return Dates.parse(obj);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 尝试用 'yyyy-MM-dd' 格式解析日期
     *
     * @param str 日期字符串
     * @return true表示解析成功 false格式错误
     */
    public static boolean testFormat10(String str) {
        return str != null //
                && str.length() == 10 //
                && StringUtils.inArray(str.charAt(4), Dates.DATE_DELIMITER) //
                && StringUtils.inArray(str.charAt(7), Dates.DATE_DELIMITER) //
                && Dates.isDate(str.charAt(0), str.charAt(1), str.charAt(2), str.charAt(3), str.charAt(5), str.charAt(6), str.charAt(8), str.charAt(9));
    }

    /**
     * 尝试用 yyyy-MM-dd HH:mm 格式解析日期
     *
     * @param str 日期字符串
     * @return true表示解析成功 false格式错误
     */
    public static boolean testFormat16(String str) {
        return str != null //
                && str.length() == 16 //
                && StringUtils.inArray(str.charAt(4), Dates.DATE_DELIMITER) //
                && StringUtils.inArray(str.charAt(7), Dates.DATE_DELIMITER) //
                && Dates.isDate(str.charAt(0), str.charAt(1), str.charAt(2), str.charAt(3), str.charAt(5), str.charAt(6), str.charAt(8), str.charAt(9)) //
                && Character.isWhitespace(str.charAt(10)) //
                && StringUtils.inArray(str.charAt(13), Dates.DATE_DELIMITER) //
                && Dates.isTime(str.charAt(11), str.charAt(12), str.charAt(14), str.charAt(15), '0', '0');
    }

    /**
     * 尝试用 'yyyyMMdd' 格式解析日期
     *
     * @param str 日期字符串
     * @return 返回true表示字符串与格式相符 false表示字符串与格式不符
     */
    public static boolean testFormat08(String str) {
        return str != null //
                && str.length() == 8 //
                && Dates.isDate(str.charAt(0), str.charAt(1), str.charAt(2), str.charAt(3), str.charAt(4), str.charAt(5), str.charAt(6), str.charAt(7)) //
                ;
    }

    /**
     * 分析日期字符串的格式
     *
     * @param str 日期字符串
     * @return 日期时间格式，详见: {@linkplain SimpleDateFormat}
     */
    public static String pattern(String str) {
        if (str == null) {
            return null;
        }

        str = StringUtils.trimBlank(str);
        int length = str.length();

        // yyyyMMdd
        if (length == 8 && StringUtils.isNumber(str.charAt(0), str.charAt(1), str.charAt(2), str.charAt(3), str.charAt(4), str.charAt(5), str.charAt(6), str.charAt(7))) {
            return "yyyyMMdd";
        }

        // yyyy-MM-dd
        else if (length == 10 //
                && StringUtils.inArray(str.charAt(4), Dates.DATE_DELIMITER) // separator
                && StringUtils.inArray(str.charAt(7), Dates.DATE_DELIMITER) // separator
                && StringUtils.isNumber(str.charAt(0), str.charAt(1), str.charAt(2), str.charAt(3), str.charAt(5), str.charAt(6), str.charAt(8), str.charAt(9)) //
        ) {
            return "yyyy-MM-dd";
        }

        // MM/dd/yyyy
        else if (length == 10 //
                && StringUtils.inArray(str.charAt(2), Dates.DATE_DELIMITER) // separator
                && StringUtils.inArray(str.charAt(5), Dates.DATE_DELIMITER) // separator
                && Dates.isDate(str.charAt(6), str.charAt(7), str.charAt(8), str.charAt(9), str.charAt(0), str.charAt(1), str.charAt(3), str.charAt(4)) //
        ) {
            return "MM/dd/yyyy";
        }

        // yyyyMMddHH
        else if (length == 10 //
                && Dates.isDate(str.charAt(0), str.charAt(1), str.charAt(2), str.charAt(3), str.charAt(4), str.charAt(5), str.charAt(6), str.charAt(7)) //
                && Dates.isTime(str.charAt(8), str.charAt(9), '0', '0', '0', '0') //
        ) {
            return "yyyyMMddHH";
        }

        // yyyyMMddHHmm
        else if (length == 12 //
                && Dates.isDate(str.charAt(0), str.charAt(1), str.charAt(2), str.charAt(3), str.charAt(4), str.charAt(5), str.charAt(6), str.charAt(7)) //
                && Dates.isTime(str.charAt(8), str.charAt(9), str.charAt(10), str.charAt(11), '0', '0') //
        ) {
            return "yyyyMMddHHmm";
        }

        // yyyyMMddHHmmss 或 yyyyMMddHHmmssSSS
        else if ((length >= 14 && length <= 17) //
                && Dates.isDate(str.charAt(0), str.charAt(1), str.charAt(2), str.charAt(3), str.charAt(4), str.charAt(5), str.charAt(6), str.charAt(7)) //
                && Dates.isTime(str.substring(8).toCharArray()) //
        ) {
            switch (length) {
                case 14:
                    return "yyyyMMddHHmmss";
                case 15:
                    return "yyyyMMddHHmmssS";
                case 16:
                    return "yyyyMMddHHmmssSS";
                case 17:
                    return "yyyyMMddHHmmssSSS";
            }
        }

        // yyyy-MM-dd HH || yyyy-MM-dd HH:mm || yyyy-MM-dd HH:mm:ss || yyyy-MM-dd HH:mm:ss:SS
        else if (length >= 12 // min length of time expression
                && StringUtils.inArray(str.charAt(4), Dates.DATE_DELIMITER) // separator
                && StringUtils.inArray(str.charAt(7), Dates.DATE_DELIMITER) // separator
                && StringUtils.isNumber(str.charAt(0), str.charAt(1), str.charAt(2), str.charAt(3), str.charAt(5), str.charAt(6), str.charAt(8), str.charAt(9)) // year
                && Character.isWhitespace(str.charAt(10)) // blank
                && Dates.isTime(str.substring(11)) // time
        ) {
            switch (length) {
                case 13:
                    return "yyyy-MM-dd HH";
                case 16:
                    return "yyyy-MM-dd HH:mm";
                case 19:
                    return "yyyy-MM-dd HH:mm:ss";
                case 22:
                    return "yyyy-MM-dd HH:mm:ss:SS";
            }
        }

        String[] array = StringUtils.splitByBlank(str);

        // yyyy年MM月dd日
        int b, m;
        if ((b = str.indexOf('年')) != -1 && (m = str.indexOf('月', b)) != -1 && str.endsWith("日")) {
            String monthstr = str.substring(b + 1, m);
            return monthstr.length() == 1 ? "yyyy年M月dd日" : "yyyy年MM月dd日";
        }

        // 31 dec 2017
        if (array.length == 3 //
                && StringUtils.isNumber(array[0]) // day of month
                && Dates.isMonth(array[1]) // month
                && StringUtils.isNumber(array[2]) // year
        ) {
            return "dd MMM yyyy";
        }

        // 31 december 2017 at 08:38:00
        else if (array.length == 5 //
                && StringUtils.isNumber(array[0]) // day of month
                && Dates.isMonth(array[1]) // month
                && StringUtils.isNumber(array[2]) // year
                && "at".equalsIgnoreCase(array[3]) //
                && Dates.isTime(array[4]) // time expression
        ) {
            return "dd MMMM yyyy 'at' HH:mm:ss";
        }

        // Thu Mar 21 06:14:26 UTC 2019
        else if (array.length == 6 //
                && Dates.isDayOfWeek(array[0]) // day of week
                && Dates.isMonth(array[1]) // month
                && StringUtils.isNumber(array[2]) // day of month
                && Dates.isTime(array[3]) // time
                && Dates.isTimeZone(array[4]) // timezone
                && StringUtils.isNumber(array[5]) // year
        ) {
            return "E MMM dd HH:mm:ss zzz yyyy";
        }

        // 2019年 03月 21日 星期四 06:13:57 UTC
        else if (array.length == 6 //
                && array[0].endsWith("年") // year
                && array[1].endsWith("月") // month
                && array[2].endsWith("日") // day of month
                && Dates.isDayOfWeek(array[3]) // day of week
                && Dates.isTime(array[4]) // time
                && Dates.isTimeZone(array[5]) // timezone
        ) {
            return "yyyy'年' MM'月' dd'日' E HH:mm:ss zzz";
        }

//        // Sun Oct 11 00:00:00 GMT+08:00 1998
//        else if (array.length == 6 //
//                && Dates.isDayOfWeek(array[0]) // day of week
//                && Dates.isMonth(array[1]) // month
//                && StringUtils.isNumber(array[2]) // day of month
//                && Dates.isTime(array[3]) // time
//                && Dates.isTimeZone(array[4]) // timezone
//                && StringUtils.isNumber(array[5]) // year
//        ) {
//            return "EEE MMM dd HH:mm:ss 'GMT'XXX yyyy";
//        }

        // 其他格式
        else {
            throw new UnsupportedOperationException(str);
        }
    }

    /**
     * 将数组中的元素转为日期
     *
     * @param array 数组, 支持的格式详见: {@linkplain #parse(Object)}
     * @return 日期数组
     */
    public static Date[] parse(Object... array) {
        if (array == null) {
            return null;
        }

        Date[] newarray = new Date[array.length];
        for (int index = 0; index < array.length; index++) {
            newarray[index] = Dates.parse(array[index]);
        }
        return newarray;
    }

    /**
     * 将输入参数转为 {@link Date}
     * <p>
     * Support date format:
     * y+.*MM.*dd
     * <p>
     * yyyy-MM-dd, e.g: 2017-01-01 || 2017/01/01 || 2017.01.01, the separator between year and month can be "- / | \\ _ : ： . 。"
     * <p>
     * MM/dd/yyyy
     * <p>
     * yyyy-M-d, e.g: 2017-1-1
     * <p>
     * yyyyMMdd
     * yyyyMMddHH
     * yyyyMMddHHmm
     * yyyyMMddHHmmss
     * yyyyMMddHHmmssSSS
     * <p>
     * yyyy-MM-dd hh
     * yyyy-MM-dd hh:mm
     * yyyy-MM-dd hh:mm:ss
     * yyyy-MM-dd hh:mm:ss:SSS
     * <p>
     * Sun Oct 11 00:00:00 GMT+08:00 1998
     * Sun Oct 11 00:00:00:000 GMT+08:00 1998
     * <p>
     * 二零一七年十二月二十三
     * 1998年10月11日
     * <p>
     * 31 december 2017 at 08:38
     * 31 dec 2017
     *
     * @param obj 日期信息
     * @return 日期
     */
    public static Date parse(Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof Date) {
            return (Date) obj;
        } else if (obj instanceof Calendar) {
            Date time = ((Calendar) obj).getTime();
            return new Date(time.getTime());
        }

        String str = StringUtils.trimBlank(obj.toString());
        int length = str.length();

        // 0 mean year, 1 mean month, 2 mean day of month, 3 mean hour, 4 mean minute, 5 mean second, 6 mean millisecond, 7 mean day of week
        int[] datetime = new int[8];

        // yyyyMMdd
        if (length == 8 && StringUtils.isNumber(str.charAt(0), str.charAt(1), str.charAt(2), str.charAt(3), str.charAt(4), str.charAt(5), str.charAt(6), str.charAt(7))) {
            datetime[0] = Integer.parseInt(str.substring(0, 4));
            datetime[1] = Integer.parseInt(str.substring(4, 6));
            datetime[2] = Integer.parseInt(str.substring(6));
        }

        // yyyy-MM-dd
        else if (length == 10 //
                && StringUtils.inArray(str.charAt(4), Dates.DATE_DELIMITER) // separator
                && StringUtils.inArray(str.charAt(7), Dates.DATE_DELIMITER) // separator
                && StringUtils.isNumber(str.charAt(0), str.charAt(1), str.charAt(2), str.charAt(3), str.charAt(5), str.charAt(6), str.charAt(8), str.charAt(9)) //
        ) {
            datetime[0] = Integer.parseInt(str.substring(0, 4)); // year
            datetime[1] = Integer.parseInt(str.substring(5, 7)); // month
            datetime[2] = Integer.parseInt(str.substring(8)); // day of month
        }

        // MM/dd/yyyy
        else if (length == 10 //
                && StringUtils.inArray(str.charAt(2), Dates.DATE_DELIMITER) // separator
                && StringUtils.inArray(str.charAt(5), Dates.DATE_DELIMITER) // separator
                && Dates.isDate(str.charAt(6), str.charAt(7), str.charAt(8), str.charAt(9), str.charAt(0), str.charAt(1), str.charAt(3), str.charAt(4)) //
        ) {
            datetime[0] = Integer.parseInt(str.substring(6, 10)); // year
            datetime[1] = Integer.parseInt(str.substring(0, 2)); // month
            datetime[2] = Integer.parseInt(str.substring(3, 5)); // day of month
        }

        // yyyyMMddHH
        else if (length == 10 //
                && Dates.isDate(str.charAt(0), str.charAt(1), str.charAt(2), str.charAt(3), str.charAt(4), str.charAt(5), str.charAt(6), str.charAt(7)) //
                && Dates.isTime(str.charAt(8), str.charAt(9), '0', '0', '0', '0') //
        ) {
            datetime[0] = Integer.parseInt(str.substring(0, 4)); // year
            datetime[1] = Integer.parseInt(str.substring(4, 6)); // month
            datetime[2] = Integer.parseInt(str.substring(6, 8)); // day of month
            datetime[3] = Integer.parseInt(str.substring(8)); // hour
        }

        // yyyyMMddHHmm
        else if (length == 12 //
                && Dates.isDate(str.charAt(0), str.charAt(1), str.charAt(2), str.charAt(3), str.charAt(4), str.charAt(5), str.charAt(6), str.charAt(7)) //
                && Dates.isTime(str.charAt(8), str.charAt(9), str.charAt(10), str.charAt(11), '0', '0') //
        ) {
            datetime[0] = Integer.parseInt(str.substring(0, 4)); // year
            datetime[1] = Integer.parseInt(str.substring(4, 6)); // month
            datetime[2] = Integer.parseInt(str.substring(6, 8)); // day of month
            datetime[3] = Integer.parseInt(str.substring(8, 10)); // hour
            datetime[4] = Integer.parseInt(str.substring(10)); // minute
        }

        // yyyyMMddHHmmss 或 yyyyMMddHHmmssSSS
        else if ((length >= 14 && length <= 17) //
                && Dates.isDate(str.charAt(0), str.charAt(1), str.charAt(2), str.charAt(3), str.charAt(4), str.charAt(5), str.charAt(6), str.charAt(7)) //
                && Dates.isTime(str.substring(8).toCharArray()) //
        ) {
            datetime[0] = Integer.parseInt(str.substring(0, 4)); // year
            datetime[1] = Integer.parseInt(str.substring(4, 6)); // month
            datetime[2] = Integer.parseInt(str.substring(6, 8)); // day of month
            datetime[3] = Integer.parseInt(str.substring(8, 10)); // hour
            datetime[4] = Integer.parseInt(str.substring(10, 12)); // minute
            datetime[5] = Integer.parseInt(str.substring(12, 14)); // second
            if (length != 14) {
                datetime[6] = Integer.parseInt(str.substring(14)); // millisecond
            }
        }

        // yyyy-MM-dd hh || yyyy-MM-dd hh:mm || yyyy-MM-dd hh:mm:ss || yyyy-MM-dd hh:mm:ss:SS
        else if (length >= 12 // min length of time expression
                && StringUtils.inArray(str.charAt(4), Dates.DATE_DELIMITER) // separator
                && StringUtils.inArray(str.charAt(7), Dates.DATE_DELIMITER) // separator
                && StringUtils.isNumber(str.charAt(0), str.charAt(1), str.charAt(2), str.charAt(3), str.charAt(5), str.charAt(6), str.charAt(8), str.charAt(9)) // year
                && Character.isWhitespace(str.charAt(10)) // blank
                && Dates.isTime(str.substring(11)) // time
        ) {
            datetime[0] = Integer.parseInt(str.substring(0, 4));
            datetime[1] = Integer.parseInt(str.substring(5, 7));
            datetime[2] = Integer.parseInt(str.substring(8, 10));
            Dates.parseTime(str.substring(11), datetime);
        } else {
            String[] array = StringUtils.splitByBlank(str);

            // 31 dec 2017
            if (array.length == 3 //
                    && StringUtils.isNumber(array[0]) // day of month
                    && Dates.isMonth(array[1]) // month
                    && StringUtils.isNumber(array[2]) // year
            ) {
                datetime[0] = Integer.parseInt(array[2]);
                datetime[1] = Dates.parseMonth(array[1]);
                datetime[2] = Integer.parseInt(array[0]);
            }

            // 31 december 2017 at 08:38:00
            else if (array.length == 5 //
                    && StringUtils.isNumber(array[0]) // day of month
                    && Dates.isMonth(array[1]) // month
                    && StringUtils.isNumber(array[2]) // year
                    && "at".equalsIgnoreCase(array[3]) //
                    && Dates.isTime(array[4]) // time expression
            ) {
                datetime[0] = Integer.parseInt(array[2]);
                datetime[1] = Dates.parseMonth(array[1]);
                datetime[2] = Integer.parseInt(array[0]);
                Dates.parseTime(array[4], datetime);
            }

            // Thu Mar 21 06:14:26 UTC 2019
            else if (array.length == 6 //
                    && Dates.isDayOfWeek(array[0]) // day of week
                    && Dates.isMonth(array[1]) // month
                    && StringUtils.isNumber(array[2]) // day of month
                    && Dates.isTime(array[3]) // time
                    && Dates.isTimeZone(array[4]) // timezone
                    && StringUtils.isNumber(array[5]) // year
            ) {
                datetime[0] = Integer.parseInt(array[5]);
                datetime[1] = Dates.parseMonth(array[1]);
                datetime[2] = Integer.parseInt(array[2]);
                Dates.parseTime(array[3], datetime);
                datetime[7] = Dates.parseDayOfWeek(array[0]);
            }

            // 2019年 03月 21日 星期四 06:13:57 UTC
            else if (array.length == 6 //
                    && array[0].endsWith("年") // year
                    && array[1].endsWith("月") // month
                    && array[2].endsWith("日") // day of month
                    && Dates.isDayOfWeek(array[3]) // day of week
                    && Dates.isTime(array[4]) // time
                    && Dates.isTimeZone(array[5]) // timezone
            ) {
                datetime[0] = Integer.parseInt(StringUtils.removeSuffix(array[0]));
                datetime[1] = Integer.parseInt(StringUtils.removeSuffix(array[1]));
                datetime[2] = Integer.parseInt(StringUtils.removeSuffix(array[2]));
                Dates.parseTime(array[4], datetime);
                datetime[7] = Dates.parseDayOfWeek(array[3]);
            }

            // Sun Oct 11 00:00:00 GMT+08:00 1998
            else if (array.length == 6 //
                    && Dates.isDayOfWeek(array[0]) // day of week
                    && Dates.isMonth(array[1]) // month
                    && StringUtils.isNumber(array[2]) // day of month
                    && Dates.isTime(array[3]) // time
                    && Dates.isTimeZone(array[4]) // timezone
                    && StringUtils.isNumber(array[5]) // year
            ) {
                datetime[0] = Integer.parseInt(array[5]);
                datetime[1] = Dates.parseMonth(array[1]);
                datetime[2] = Integer.parseInt(array[2]);
                Dates.parseTime(array[3], datetime);
                datetime[7] = Dates.parseDayOfWeek(array[0]);
            }

            // 一九九九年1月13日
            else {
                Dates.parse(str, datetime);
            }
        }

        if (!Dates.isDate(datetime[0], datetime[1], datetime[2])) {
            throw new RuntimeException("format(" + str + ") Illegal date: " + datetime[0] + " year " + datetime[1] + " month " + datetime[2] + " day ");
        }
        if (!Dates.isTime(datetime[3], datetime[4], datetime[5], datetime[6])) {
            throw new RuntimeException("format(" + str + ") Illegal time: " + datetime[3] + " hour " + datetime[4] + " minitue " + datetime[5] + " second " + datetime[6] + " millisecond");
        }

        Calendar cr = Calendar.getInstance();
        cr.set(Calendar.YEAR, datetime[0]);
        cr.set(Calendar.MONTH, datetime[1] - 1);
        cr.set(Calendar.DATE, datetime[2]);
        cr.set(Calendar.HOUR_OF_DAY, datetime[3]);
        cr.set(Calendar.MINUTE, datetime[4]);
        cr.set(Calendar.SECOND, datetime[5]);
        cr.set(Calendar.MILLISECOND, datetime[6]);
        Date date = new Date(cr.getTimeInMillis());

        if (datetime[7] == 0) {
            return date;
        }

        int dayOfWeek = cr.get(Calendar.DAY_OF_WEEK) - 1;
        int dayOfWeek1 = datetime[7]; // day of week
        if ((dayOfWeek1 == 7 && dayOfWeek == 0) || (dayOfWeek1 == dayOfWeek)) {
            return date;
        } else {
            throw new RuntimeException("format(" + str + ") -> " + StringUtils.toString(date) + ", But not " + "零一二三四五六七八九".charAt(dayOfWeek1));
        }
    }

    /**
     * 将中文日期信息转为日期
     * e.g: 二零一七年一月十二日
     *
     * @param str      中文日期信息
     * @param datetime 数组第一位表示年份, 数组第二位表示月份, 数组第三位表示月份中的日, 数组第四位表示小时, 数组第五位表示分钟, 数组第六位表示秒, 数组第七位表示毫秒, 数组第八位表示星期几
     */
    private static void parse(String str, int[] datetime) {
        char[] array = new char[8];

        int size = 0, index = 0;
        int length = str.length();
        for (; index < length; index++) {
            char c = str.charAt(index);
            if (Character.isWhitespace(c)) {
                continue;
            }
            if (StringUtils.isNumber(c)) {
                array[size++] = c;
            } else {
                c = replaceChineseNumber(c);
                if (StringUtils.isNumber(c)) {
                    array[size++] = c;
                } else {
                    index++;
                    break;
                }
            }
        }

        if (size != 4) {
            throw new RuntimeException(str + " parse " + StringUtils.toString(array));
        }

        boolean flag = false;
        for (; index < length; index++) {
            char c = str.charAt(index);
            if (Character.isWhitespace(c)) {
                continue;
            }
            if (StringUtils.isNumber(c)) {
                array[size++] = c;
                flag = false;
            } else {
                c = replaceChineseNumber(c);
                if (StringUtils.isNumber(c)) {
                    array[size++] = c;
                    flag = false;
                } else if (c == '十' || c == '拾') {
                    array[size++] = '1';
                    flag = true;
                } else {
                    if (flag) {
                        array[size++] = '0';
                    }
                    index++;
                    break;
                }
            }
        }

        if (size == 5) {
            array[5] = array[4];
            array[4] = '0';
            size++;
        } else if (size != 6) {
            throw new RuntimeException(str + " parse " + StringUtils.toString(array));
        }

        // date
        flag = false;
        while (index < length) {
            char c = str.charAt(index);
            if (Character.isWhitespace(c)) {
                index++;
                continue;
            }
            if (StringUtils.isNumber(c)) {
                if (size == 8) {
                    array[size - 1] = c;
                    flag = false;
                } else {
                    array[size++] = c;
                }
            } else {
                c = replaceChineseNumber(c);
                if (StringUtils.isNumber(c)) {
                    if (size == 8) {
                        array[size - 1] = c;
                        flag = false;
                    } else {
                        array[size++] = c;
                    }
                } else if (c == '十' || c == '拾') {
                    if (flag) {
                        throw new RuntimeException(str + " parse " + StringUtils.toString(array));
                    }

                    if (size == 6) {
                        array[size++] = '1';
                    } else if (size == 7) {
                        array[size++] = '0';
                    } else {
                        throw new RuntimeException(str + " parse " + StringUtils.toString(array));
                    }
                    flag = true;
                } else {
                    if (flag && size == 7) {
                        array[size++] = '0';
                        flag = false;
                    }
                    index++;
                    break;
                }
            }
            index++;
        }

        if (size == 7) {
            if (flag) {
                array[7] = '0';
            } else {
                array[7] = array[6];
                array[6] = '0';
            }
        }

        datetime[0] = Integer.parseInt(new String(new char[]{array[0], array[1], array[2], array[3]}));
        datetime[1] = Integer.parseInt(new String(new char[]{array[4], array[5]}));
        datetime[2] = Integer.parseInt(new String(new char[]{array[6], array[7]}));
    }

    /**
     * 将中文数字替换为阿拉伯数字
     * 中文字符范围包括： 零壹贰叁肆伍陆柒捌玖 零一二三四五六七八九
     *
     * @param c 字符
     * @return 字符
     */
    private static char replaceChineseNumber(char c) {
        for (int i = 0; i <= 9; i++) {
            if (c == "零壹贰叁肆伍陆柒捌玖".charAt(i) || c == "零一二三四五六七八九".charAt(i)) {
                return "0123456789".charAt(i);
            }
        }
        return c;
    }

    /**
     * 解析周信息
     * <p>
     * 支持周格式:
     * Mon
     * Monday
     * Tuesday
     * Wednesday
     * 星期一
     * 星期1
     *
     * @param str 字符串，格式：周一、或 monday 或 星期一 或 星期1
     * @return 数字: 1-7
     */
    static int parseDayOfWeek(String str) {
        if (str == null) {
            throw new NullPointerException(str);
        }

        str = StringUtils.removeBlank(str);
        if ("Mon".equalsIgnoreCase(str) || Monday.equalsIgnoreCase(str) || str.equals("星期一") || str.equals("星期1")) {
            return 1;
        }
        if ("Tue".equalsIgnoreCase(str) || Tuesday.equalsIgnoreCase(str) || str.equals("星期二") || str.equals("星期2")) {
            return 2;
        }
        if ("Wed".equalsIgnoreCase(str) || Wednesday.equalsIgnoreCase(str) || str.equals("星期三") || str.equals("星期3")) {
            return 3;
        }
        if ("Thu".equalsIgnoreCase(str) || Thursday.equalsIgnoreCase(str) || str.equals("星期四") || str.equals("星期4")) {
            return 4;
        }
        if ("Fri".equalsIgnoreCase(str) || Friday.equalsIgnoreCase(str) || str.equals("星期五") || str.equals("星期5")) {
            return 5;
        }
        if ("Sat".equalsIgnoreCase(str) || Saturday.equalsIgnoreCase(str) || str.equals("星期六") || str.equals("星期6")) {
            return 6;
        }
        if ("Sun".equalsIgnoreCase(str) || Sunday.equalsIgnoreCase(str) || str.equals("星期天") || str.equals("星期七") || str.equals("星期7")) {
            return 7;
        }

        throw new UnsupportedOperationException("parseWeek(\"" + str + "\")");
    }

    /**
     * 解析月份
     * 支持的月份格式:
     * January
     * 1月
     * 一月
     * Jan
     *
     * @param str 字符串：格式：一月 或 Jan 或 January 或 1月
     * @return 月份: 1-12
     */
    static int parseMonth(String str) {
        if (str == null) {
            throw new NullPointerException(str);
        }

        str = StringUtils.removeBlank(str);
        if ("Jan".equalsIgnoreCase(str) || Dates.January.equalsIgnoreCase(str) || str.equals("一月") || str.equals("1月")) {
            return 1;
        }
        if ("Feb".equalsIgnoreCase(str) || Dates.February.equalsIgnoreCase(str) || str.equals("二月") || str.equals("2月")) {
            return 2;
        }
        if ("Mar".equalsIgnoreCase(str) || Dates.March.equalsIgnoreCase(str) || str.equals("三月") || str.equals("3月")) {
            return 3;
        }
        if ("Apr".equalsIgnoreCase(str) || Dates.April.equalsIgnoreCase(str) || str.equals("四月") || str.equals("4月")) {
            return 4;
        }
        if ("May".equalsIgnoreCase(str) || Dates.May.equalsIgnoreCase(str) || str.equals("五月") || str.equals("5月")) {
            return 5;
        }
        if ("Jun".equalsIgnoreCase(str) || Dates.June.equalsIgnoreCase(str) || str.equals("六月") || str.equals("6月")) {
            return 6;
        }
        if ("Jul".equalsIgnoreCase(str) || Dates.July.equalsIgnoreCase(str) || str.equals("七月") || str.equals("7月")) {
            return 7;
        }
        if ("Aug".equalsIgnoreCase(str) || Dates.August.equalsIgnoreCase(str) || str.equals("八月") || str.equals("8月")) {
            return 8;
        }
        if ("Sep".equalsIgnoreCase(str) || Dates.September.equalsIgnoreCase(str) || str.equals("九月") || str.equals("9月")) {
            return 9;
        }
        if ("Oct".equalsIgnoreCase(str) || Dates.October.equalsIgnoreCase(str) || str.equals("十月") || str.equals("10月")) {
            return 10;
        }
        if ("Nov".equalsIgnoreCase(str) || Dates.November.equalsIgnoreCase(str) || str.equals("十一月") || str.equals("11月")) {
            return 11;
        }
        if ("Dec".equalsIgnoreCase(str) || Dates.December.equalsIgnoreCase(str) || str.equals("十二月") || str.equals("12月")) {
            return 12;
        }

        throw new UnsupportedOperationException("parseMonth(\"" + str + "\")");
    }

    /**
     * 解析时间表达式
     * <p>
     * 支持的时间表达式格式:
     * hh
     * hh:mm
     * hh:mm:ss
     * hh:mm:ss:SS
     * 时间表达中的分隔符详见 {@linkplain #isTime(String)}
     *
     * @param str      字符串
     * @param datetime 数组用于存储日期与时间
     *                 第一个位置是年份 （2015）
     *                 第二个位置是月份（1-12）
     *                 第三个位置是月份中的日期（1-31）
     *                 第四个位置上的是小时（0-23）
     *                 第五个位置上的是分钟 （0-59）
     *                 第六个位置上的是秒钟 （0-59）
     *                 第七个位置上的是毫秒 （0-999）
     *                 第八个位置上的是周中的日期（1-7）
     */
    private static void parseTime(String str, int[] datetime) {
        List<String> list = new ArrayList<String>();
        StringUtils.split(str, Dates.TIME_DELIMITER, true, list); // 分隔参数要与 isStandTimeFormat 函数保持一致
        if (list.size() == 1) {
            datetime[3] = Integer.parseInt(list.get(0)); // 解析小时
        } else if (list.size() == 2) {
            datetime[3] = Integer.parseInt(list.get(0)); // 解析小时
            datetime[4] = Integer.parseInt(list.get(1)); // 解析分钟
        } else if (list.size() == 3) {
            datetime[3] = Integer.parseInt(list.get(0)); // 解析小时
            datetime[4] = Integer.parseInt(list.get(1)); // 解析分钟
            datetime[5] = Integer.parseInt(list.get(2)); // 解析秒钟
        } else if (list.size() == 4) {
            datetime[3] = Integer.parseInt(list.get(0)); // 解析小时
            datetime[4] = Integer.parseInt(list.get(1)); // 解析分钟
            datetime[5] = Integer.parseInt(list.get(2)); // 解析秒钟
            datetime[6] = Integer.parseInt(list.get(3)); // 解析毫秒
        } else {
            throw new UnsupportedOperationException("parseTime(\"" + str + "\", " + StringUtils.toString(datetime) + ")");
        }
    }

    /**
     * 将日期转为指定格式的字符串
     * formateString(null, "yyyyMMddHHmmss") == null
     * formateString(date, "yyyyMMddHHmmss") == 20120301121212
     *
     * @param date    日期信息
     * @param pattern 日期时间格式，详见: {@link SimpleDateFormat}
     * @return 字符串
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            return null;
        } else if (pattern == null) {
            throw new NullPointerException();
        } else {
            SimpleDateFormat obj = new SimpleDateFormat();
            obj.applyPattern(pattern);
            return obj.format(date);
        }
    }

    /**
     * 将日期转为指定格式的字符串
     * formateString(null, "yyyyMMddHHmmss") == null
     * formateString(date, "yyyyMMddHHmmss") == 20120301121212
     *
     * @param date    日期信息
     * @param pattern 日期时间格式，详见: {@link SimpleDateFormat}
     * @param locale  本地信息
     * @return 字符串
     */
    public static String format(Date date, String pattern, Locale locale) {
        if (date == null) {
            return null;
        } else if (pattern == null) {
            throw new IllegalArgumentException(date + ", " + pattern + ", " + locale);
        } else {
            return new SimpleDateFormat(pattern, locale).format(date);
        }
    }

    /**
     * MM/DD/YYYY
     *
     * @param date 日期信息
     * @return 字符串
     */
    public static String format01(Date date) {
        if (date == null) {
            return null;
        }

        Calendar cr = Calendar.getInstance();
        cr.setTime(date);
        int year = cr.get(Calendar.YEAR);
        int month = cr.get(Calendar.MONTH) + 1;
        int dayOfMonth = cr.get(Calendar.DAY_OF_MONTH);

        StringBuilder buf = new StringBuilder(14);
        if (month <= 9) {
            buf.append('0');
        }
        buf.append(month);
        buf.append('/');
        if (dayOfMonth <= 9) {
            buf.append('0');
        }
        buf.append(dayOfMonth);
        buf.append('/');
        buf.append(year);
        return buf.toString();
    }

    /**
     * dd/MM/yyyy
     *
     * @param date 日期信息
     * @return 字符串
     */
    public static String format02(Date date) {
        if (date == null) {
            return null;
        }

        Calendar cr = Calendar.getInstance();
        cr.setTime(date);
        int year = cr.get(Calendar.YEAR);
        int month = cr.get(Calendar.MONTH) + 1;
        int dayOfMonth = cr.get(Calendar.DAY_OF_MONTH);

        StringBuilder buf = new StringBuilder(14);
        if (dayOfMonth <= 9) {
            buf.append('0');
        }
        buf.append(dayOfMonth);
        buf.append('/');
        if (month <= 9) {
            buf.append('0');
        }
        buf.append(month);
        buf.append('/');
        buf.append(year);
        return buf.toString();
    }

    /**
     * yyyyMMdd
     *
     * @param date 日期信息
     * @return 字符串
     */
    public static String format08(Date date) {
        if (date == null) {
            return null;
        }

        Calendar cr = Calendar.getInstance();
        cr.setTime(date);
        int year = cr.get(Calendar.YEAR);
        int month = cr.get(Calendar.MONTH) + 1;
        int dayOfMonth = cr.get(Calendar.DAY_OF_MONTH);

        StringBuilder buf = new StringBuilder(8);
        buf.append(year);
        if (month <= 9) {
            buf.append('0');
        }
        buf.append(month);
        if (dayOfMonth <= 9) {
            buf.append('0');
        }
        buf.append(dayOfMonth);
        return buf.toString();
    }

    /**
     * 计算日期，并返回 yyyyMMdd 格式的日期
     *
     * @param days -1表示昨天 0表示当天 1表示明天
     * @return 日期字符串
     */
    public static String format08(int days) {
        Date date = calcDay(new Date(), days);
        return format08(date);
    }

    /**
     * 将日期字符串转为 yyyyMMdd 格式的日期字符串
     *
     * @param date 日期字符串，格式详见: {#{@linkplain #parse(Object)}}
     * @return 日期字符串
     */
    public static String format08(String date) {
        Date d = parse(date);
        return format08(d);
    }

    /**
     * 将日期格式化为 yyyy-MM-dd
     *
     * @param date 日期信息
     * @return 日期字符串
     */
    public static String format10(Date date) {
        if (date == null) {
            return null;
        }

        Calendar cr = Calendar.getInstance();
        cr.setTime(date);
        int year = cr.get(Calendar.YEAR);
        int month = cr.get(Calendar.MONTH) + 1;
        int dayOfMonth = cr.get(Calendar.DAY_OF_MONTH);

        StringBuilder buf = new StringBuilder(10);
        buf.append(year);
        buf.append('-');
        if (month <= 9) {
            buf.append('0');
        }
        buf.append(month);
        buf.append('-');
        if (dayOfMonth <= 9) {
            buf.append('0');
        }
        buf.append(dayOfMonth);
        return buf.toString();
    }

    /**
     * 计算日期，并返回 yyyy-MM-dd 格式的日期
     *
     * @param days -1表示昨天 0表示当天 1表示明天
     * @return 日期字符串
     */
    public static String format10(int days) {
        Date date = calcDay(new Date(), days);
        return format10(date);
    }

    /**
     * HH:mm:ss:SSS
     *
     * @param date 日期信息
     * @return 字符串
     */
    public static String format12(Date date) {
        if (date == null) {
            return null;
        }

        Calendar cr = Calendar.getInstance();
        cr.setTime(date);
        int hour = cr.get(Calendar.HOUR_OF_DAY);
        int minute = cr.get(Calendar.MINUTE);
        int second = cr.get(Calendar.SECOND);
        int mills = cr.get(Calendar.MILLISECOND);

        StringBuilder buf = new StringBuilder(12);
        if (hour <= 9) {
            buf.append('0');
        }
        buf.append(hour);
        buf.append(':');

        if (minute <= 9) {
            buf.append('0');
        }
        buf.append(minute);
        buf.append(':');

        if (second <= 9) {
            buf.append('0');
        }
        buf.append(second);
        buf.append(':');

        if (mills <= 9) {
            buf.append("00");
        } else if (mills <= 99) {
            buf.append('0');
        }
        buf.append(mills);
        return buf.toString();
    }

    /**
     * yyyyMMddHHmmss
     *
     * @param date 日期信息
     * @return 字符串
     */
    public static String format14(Date date) {
        if (date == null) {
            return null;
        }

        Calendar cr = Calendar.getInstance();
        cr.setTime(date);
        int year = cr.get(Calendar.YEAR);
        int month = cr.get(Calendar.MONTH) + 1;
        int dayOfMonth = cr.get(Calendar.DAY_OF_MONTH);
        int hour = cr.get(Calendar.HOUR_OF_DAY);
        int minute = cr.get(Calendar.MINUTE);
        int second = cr.get(Calendar.SECOND);

        StringBuilder buf = new StringBuilder(14);
        buf.append(year);

        if (month <= 9) {
            buf.append('0');
        }
        buf.append(month);

        if (dayOfMonth <= 9) {
            buf.append('0');
        }
        buf.append(dayOfMonth);

        if (hour <= 9) {
            buf.append('0');
        }
        buf.append(hour);

        if (minute <= 9) {
            buf.append('0');
        }
        buf.append(minute);

        if (second <= 9) {
            buf.append('0');
        }
        buf.append(second);
        return buf.toString();
    }

    /**
     * yyyy-MM-dd HH:mm
     *
     * @param date 日期信息
     * @return 字符串
     */
    public static String format16(Date date) {
        if (date == null) {
            return null;
        }

        Calendar cr = Calendar.getInstance();
        cr.setTime(date);
        int year = cr.get(Calendar.YEAR);
        int month = cr.get(Calendar.MONTH) + 1;
        int dayOfMonth = cr.get(Calendar.DAY_OF_MONTH);
        int hour = cr.get(Calendar.HOUR_OF_DAY);
        int minute = cr.get(Calendar.MINUTE);

        StringBuilder buf = new StringBuilder(16);
        buf.append(year);
        buf.append('-');
        if (month <= 9) {
            buf.append('0');
        }
        buf.append(month);
        buf.append('-');
        if (dayOfMonth <= 9) {
            buf.append('0');
        }
        buf.append(dayOfMonth);
        buf.append(' ');
        if (hour <= 9) {
            buf.append('0');
        }
        buf.append(hour);
        buf.append(':');
        if (minute <= 9) {
            buf.append('0');
        }
        buf.append(minute);
        return buf.toString();
    }

    /**
     * 当前时间的 yyyyMMddHHmmssSSS
     *
     * @return 字符串
     */
    public static String format17() {
        return Dates.format17(new Date());
    }

    /**
     * yyyyMMddHHmmssSSS
     *
     * @param date 日期信息
     * @return 字符串
     */
    public static String format17(Date date) {
        if (date == null) {
            return null;
        }

        Calendar cr = Calendar.getInstance();
        cr.setTime(date);
        int year = cr.get(Calendar.YEAR);
        int month = cr.get(Calendar.MONTH) + 1;
        int dayOfMonth = cr.get(Calendar.DAY_OF_MONTH);
        int hour = cr.get(Calendar.HOUR_OF_DAY);
        int minute = cr.get(Calendar.MINUTE);
        int second = cr.get(Calendar.SECOND);
        int millse = cr.get(Calendar.MILLISECOND);

        StringBuilder buf = new StringBuilder(17);
        buf.append(year);
        if (month <= 9) {
            buf.append('0');
        }
        buf.append(month);

        if (dayOfMonth <= 9) {
            buf.append('0');
        }
        buf.append(dayOfMonth);

        if (hour <= 9) {
            buf.append('0');
        }
        buf.append(hour);

        if (minute <= 9) {
            buf.append('0');
        }
        buf.append(minute);

        if (second <= 9) {
            buf.append('0');
        }
        buf.append(second);

        if (millse <= 9) {
            buf.append("00");
        } else if (millse <= 99) {
            buf.append('0');
        }
        buf.append(millse);
        return buf.toString();
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     *
     * @param date 日期信息
     * @return 字符串
     */
    public static String format19(Date date) {
        if (date == null) {
            return null;
        }

        Calendar cr = Calendar.getInstance();
        cr.setTime(date);
        int year = cr.get(Calendar.YEAR);
        int month = cr.get(Calendar.MONTH) + 1;
        int dayOfMonth = cr.get(Calendar.DAY_OF_MONTH);
        int hour = cr.get(Calendar.HOUR_OF_DAY);
        int minute = cr.get(Calendar.MINUTE);
        int second = cr.get(Calendar.SECOND);

        StringBuilder buf = new StringBuilder(19);
        buf.append(year);
        buf.append('-');
        if (month <= 9) {
            buf.append('0');
        }
        buf.append(month);
        buf.append('-');
        if (dayOfMonth <= 9) {
            buf.append('0');
        }
        buf.append(dayOfMonth);
        buf.append(' ');
        if (hour <= 9) {
            buf.append('0');
        }
        buf.append(hour);
        buf.append(':');
        if (minute <= 9) {
            buf.append('0');
        }
        buf.append(minute);
        buf.append(':');
        if (second <= 9) {
            buf.append('0');
        }
        buf.append(second);
        return buf.toString();
    }

    /**
     * yyyy-MM-dd HH:mm:ss:SSS
     *
     * @param date 日期信息
     * @return 字符串
     */
    public static String format21(Date date) {
        if (date == null) {
            return null;
        }

        Calendar cr = Calendar.getInstance();
        cr.setTime(date);
        int year = cr.get(Calendar.YEAR);
        int month = cr.get(Calendar.MONTH) + 1;
        int dayOfMonth = cr.get(Calendar.DAY_OF_MONTH);
        int hour = cr.get(Calendar.HOUR_OF_DAY);
        int minute = cr.get(Calendar.MINUTE);
        int second = cr.get(Calendar.SECOND);
        int millse = cr.get(Calendar.MILLISECOND);

        StringBuilder buf = new StringBuilder(21);
        buf.append(year);
        buf.append('-');

        if (month <= 9) {
            buf.append('0');
        }
        buf.append(month);

        buf.append('-');

        if (dayOfMonth <= 9) {
            buf.append('0');
        }
        buf.append(dayOfMonth);

        buf.append(' ');

        if (hour <= 9) {
            buf.append('0');
        }
        buf.append(hour);

        buf.append(':');

        if (minute <= 9) {
            buf.append('0');
        }
        buf.append(minute);

        buf.append(':');

        if (second <= 9) {
            buf.append('0');
        }
        buf.append(second);

        buf.append(':');

        if (millse <= 9) {
            buf.append('0');
            buf.append('0');
        } else if (millse <= 99) {
            buf.append('0');
        }
        buf.append(millse);
        return buf.toString();
    }

    /**
     * yyyy年MM月dd日
     *
     * @param date 日期信息
     * @return 字符串
     */
    public static String formatCN(Date date) {
        if (date == null) {
            return null;
        }

        Calendar cr = Calendar.getInstance();
        cr.setTime(date);
        int year = cr.get(Calendar.YEAR);
        int month = cr.get(Calendar.MONTH) + 1;
        int day = cr.get(Calendar.DAY_OF_MONTH);
        return new StringBuilder(12).append(year).append('年').append(month).append('月').append(day).append('日').toString();
    }

    /**
     * 把秒转为中文常见时间说明, 格式: xx小时 xx 分 xx 秒
     * (1000000) == 277 小时 46 分 40 秒
     * (61) == 1 分 1 秒
     * (3660) == 1 小时 1 分
     *
     * @param value         时间
     * @param unit          时间单位
     * @param displaySecond true表示返回值中显示秒数
     *                      false表示返回值中不显示秒数
     * @return 字符串, 格式: xx 小时 xx 分 xx 秒 或 xx 小时 xx 分
     */
    public static StringBuilder format(long value, TimeUnit unit, boolean displaySecond) {
        if (value < 0) {
            throw new IllegalArgumentException(String.valueOf(value));
        }
        if (unit == null) {
            throw new NullPointerException(String.valueOf(unit));
        }

        StringBuilder buf = new StringBuilder(20);
        long seconds = unit.toSeconds(value);
        long hour = seconds / 3600;
        if (hour > 0) {
            buf.append(hour);
            buf.append(ResourcesUtils.getDateMessage(1));

            long i = (seconds % 3600);
            long minute = i / 60;
            if (minute > 0) {
                buf.append(' ');
                buf.append(minute);
                buf.append(ResourcesUtils.getDateMessage(2));
            }

            long second = i % 60;
            if (second > 0 && displaySecond) {
                buf.append(' ');
                buf.append(second);
                buf.append(ResourcesUtils.getDateMessage(3));
            }
            return buf;
        }

        long minute = seconds / 60;
        if (minute > 0) {
            buf.append(minute);
            buf.append(ResourcesUtils.getDateMessage(2));

            long second = seconds % 60;
            if (second > 0 && displaySecond) {
                buf.append(' ');
                buf.append(second);
                buf.append(ResourcesUtils.getDateMessage(3));
            }
            return buf;
        }

        if (displaySecond || seconds < 60) {
            buf.append(seconds);
            buf.append(ResourcesUtils.getDateMessage(3));
        }
        return buf;
    }

    /**
     * 以月为单位计算日期
     *
     * @param date  日期
     * @param month 月数，等于0表示不变，大于0表示加月，小于0表示减月
     * @return 日期
     */
    public static Date calcMonth(Date date, int month) {
        if (date == null) {
            throw new NullPointerException();
        }

        Calendar cr = Calendar.getInstance();
        cr.setTime(date);
        cr.add(Calendar.MONTH, month);
        return cr.getTime();
    }

    /**
     * 计算2个日期间的月数; 如果2个日期的年月相同则返回0;
     * calcMonth(Dates.parse("2017-07-01"), Dates.parse("2017-07-01")) == 0
     * calcMonth(Dates.parse("2017-07-01"), Dates.parse("2017-08-01")) == 1
     *
     * @param start 起始日
     * @param end   结算日
     * @return 2个日期间的间隔月数
     */
    public static int calcMonth(Date start, Date end) {
        if (start == null) {
            throw new NullPointerException();
        }
        if (end == null) {
            throw new NullPointerException();
        }

        Calendar cr = Calendar.getInstance();
        cr.setTime(start);
        int sy = cr.get(Calendar.YEAR);
        int sm = cr.get(Calendar.MONTH) + 1;

        cr.setTime(end);
        int ey = cr.get(Calendar.YEAR);
        int em = cr.get(Calendar.MONTH) + 1;
        return (ey - sy) * 12 + em - sm;
    }

    /**
     * 以年为单位计算日期
     *
     * @param date 日期
     * @param year 年数，等于0表示不变，大于0表示加年份，小于0表示减年份
     * @return 日期
     */
    public static Date calcYear(Date date, int year) {
        if (date == null) {
            return null;
        }

        Calendar cr = Calendar.getInstance();
        cr.setTime(date);
        cr.add(Calendar.YEAR, year);
        return cr.getTime();
    }

    /**
     * 以天为单位计算日期
     *
     * @param date 日期
     * @param days 等于0：日期+0 小于0：日期-天数 大于0：日期+天数
     * @return 日期
     */
    public static Date calcDay(Date date, int days) {
        if (date == null) {
            throw new NullPointerException();
        }

        Calendar cr = Calendar.getInstance();
        cr.setTime(date);
        cr.add(Calendar.DATE, days);
        return cr.getTime();
    }

    /**
     * 计算 start 到 end 之间天数 （但不包括d1天）
     *
     * @param start 起始日，格式参考：{@linkplain #parse(Object)}
     * @param end   终止日，格式参考：{@linkplain #parse(Object)}
     * @return 天数
     */
    public static long calcDay(String start, String end) {
        return calcDay(Dates.parse(start), Dates.parse(end));
    }

    /**
     * 计算 start 到 end 之间天数 （但不包括d1天）
     *
     * @param start 起始日
     * @param end   终止日
     * @return 天数
     */
    public static long calcDay(Date start, Date end) {
        if (start == null) {
            throw new NullPointerException();
        }
        if (end == null) {
            throw new NullPointerException();
        }

        Calendar cr = Calendar.getInstance();
        cr.setTime(start);
        cr.set(Calendar.HOUR_OF_DAY, 0);
        cr.set(Calendar.MINUTE, 0);
        cr.set(Calendar.SECOND, 0);
        cr.set(Calendar.MILLISECOND, 0);
        Date d1 = cr.getTime();

        cr.setTime(end);
        cr.set(Calendar.HOUR_OF_DAY, 0);
        cr.set(Calendar.MINUTE, 0);
        cr.set(Calendar.SECOND, 0);
        cr.set(Calendar.MILLISECOND, 0);
        Date d2 = cr.getTime();

        return ((d2.getTime() - d1.getTime()) / 86400000);
    }

    /**
     * 计算时间
     *
     * @param date  日期
     * @param type  操作类型
     *              {@linkplain Calendar#HOUR}
     *              {@linkplain Calendar#MINUTE}
     *              {@linkplain Calendar#SECOND}
     *              {@linkplain Calendar#MILLISECOND}
     *              {@linkplain Calendar#DAY_OF_MONTH}
     *              {@linkplain Calendar#MONTH}
     *              {@linkplain Calendar#YEAR}
     * @param value 操作数, 负数表示减法，整数表示加法
     * @return 日期
     */
    public static Date calcDay(Date date, int type, int value) {
        if (date == null || value == 0) {
            return date;
        }

        Calendar cr = Calendar.getInstance();
        cr.setTime(date);
        switch (type) { // calculate by type
            case Calendar.HOUR:
                cr.add(Calendar.HOUR, value);
                break;
            case Calendar.MINUTE:
                cr.add(Calendar.MINUTE, value);
                break;
            case Calendar.SECOND:
                cr.add(Calendar.SECOND, value);
                break;
            case Calendar.MILLISECOND:
                cr.add(Calendar.MILLISECOND, value);
                break;
            case Calendar.DAY_OF_MONTH:
                cr.add(Calendar.DAY_OF_MONTH, value);
                break;
            case Calendar.MONTH:
                cr.add(Calendar.MONTH, value);
                break;
            case Calendar.YEAR:
                cr.add(Calendar.YEAR, value);
                break;
            default:
                throw new IllegalArgumentException(String.valueOf(type));
        }
        return cr.getTime();
    }

    /**
     * 返回日期参数date所在月的月初1号
     *
     * @param date 日期
     * @return 日期
     */
    public static Date getBeginOfMonth(Date date) {
        if (date == null) {
            return null;
        }

        Calendar cr = Calendar.getInstance();
        cr.setTime(date);
        cr.set(Calendar.DATE, 1);
        return cr.getTime();
    }

    /**
     * 返回日期参数date所在月的月末最后一天
     *
     * @param date 日期
     * @return 日期
     */
    public static Date getEndOfMonth(Date date) {
        if (date == null) {
            return null;
        }

        Calendar cr = Calendar.getInstance();
        cr.setTime(date);
        cr.set(Calendar.DATE, 1); // 月初第一天
        cr.add(Calendar.MONTH, 1); // 下月第一天
        cr.add(Calendar.DATE, -1); // 上月最后一天
        return cr.getTime();
    }

    /**
     * 从日期参数start 开始到 end 为止，计算每个月的最后一天
     *
     * @param start 起始日，格式参考：{@linkplain #parse(Object)}
     * @param end   终止日，格式参考：{@linkplain #parse(Object)}
     * @return 月末的集合
     */
    public static List<String> getEndOfMonth(String start, String end) {
        Ensure.notBlank(start);
        Ensure.notBlank(end);
        Date b = Dates.parse(start), e = Dates.parse(end);
        List<String> list = new ArrayList<String>(31);
        String pattern = Dates.pattern(start);
        while (b.compareTo(e) <= 0) {
            Date endOfMonth = Dates.getEndOfMonth(b);
            if (endOfMonth.compareTo(e) <= 0) {
                list.add(Dates.format(endOfMonth, pattern));
            }
            b = Dates.calcMonth(b, 1);
        }
        return list;
    }

    /**
     * 返回日期参数date是星期几
     *
     * @param date 日期
     * @return 1, 2, 3, 4, 5, 6, 7
     */
    public static int getDayOfWeek(Date date) {
        if (date == null) {
            throw new NullPointerException();
        }

        Calendar cr = Calendar.getInstance();
        cr.setTime(date);
        return cr.get(Calendar.DAY_OF_WEEK) == 1 ? 7 : (cr.get(Calendar.DAY_OF_WEEK) - 1);
    }

    /**
     * 返回日期参数date的日期（月份中的日期）
     *
     * @param date 日期
     * @return 1, 2, 3 .. 28, 29, 30, 31
     */
    public static int getDayOfMonth(Date date) {
        if (date == null) {
            throw new NullPointerException();
        }

        Calendar cr = Calendar.getInstance();
        cr.setTime(date);
        return cr.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 返回日期参数date的是一年的第几天
     *
     * @param date 日期
     * @return 1, 2, 3 .. 366
     */
    public static int getDayOfYear(Date date) {
        if (date == null) {
            throw new NullPointerException();
        }

        Calendar cr = Calendar.getInstance();
        cr.setTime(date);
        return cr.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 检查日期字符是否正确
     *
     * @param y1 年份第一位
     * @param y2 年份第二位
     * @param y3 年份第三位
     * @param y4 年份第四位
     * @param m1 月份第一位
     * @param m2 月份第二位
     * @param d1 日期第一位
     * @param d2 日期第二位
     * @return true表示日期正确
     */
    static boolean isDate(char y1, char y2, char y3, char y4, char m1, char m2, char d1, char d2) {
        if (y1 == '0' || !StringUtils.isNumber(y1, y2, y3, y4)) {
            return false;
        }

        // true-表示31天 false表示30天
        boolean isBigMonth = false;

        // 校验月
        if (m1 == '0') { // 一月份
            if (m2 == '0') {
                return false;
            }

            if (m2 == '1' || m2 == '3' || m2 == '5' || m2 == '7' || m2 == '8') {
                isBigMonth = true;
            } else if (m2 == '2') { // 二月份
                if (d1 == '2' && d2 == '9') { // 2月29号
                    int year = Integer.parseInt(new String(new char[]{y1, y2, y3, y4}));
                    return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
                } else if (d1 == '3') {
                    return false;
                }
            } else if (m2 == '4' || m2 == '6' || m2 == '9') {
            }
        } else if (m1 == '1' && m2 == '0') {
            isBigMonth = true;
        } else if (m1 == '1' && m2 == '1') {
        } else if (m1 == '1' && m2 == '2') {
            isBigMonth = true;
        } else {
            return false;
        }

        // 校验日
        if (d1 == '0') {
            return d2 != '0' && StringUtils.isNumber(d2);
        } else if (d1 == '1' || d1 == '2') {
            return StringUtils.isNumber(d2);
        } else if (d1 == '3') {
            return isBigMonth ? (d2 == '0' || d2 == '1') : (d2 == '0');
        }

        return false;
    }

    /**
     * 返回日期范围内的一个随机日期
     *
     * @param start 随机起始日期（包含在内）
     * @param end   随机终止日期（包含在内）
     * @return 返回 start 与 end 日期之间的随机日期
     */
    public static Date random(Date start, Date end) {
        if (start == null) {
            throw new NullPointerException();
        }
        if (end == null) {
            throw new NullPointerException();
        }
        if (start.compareTo(end) > 0) {
            throw new IllegalArgumentException(Dates.format19(start) + " > " + Dates.format19(end));
        }

        long days = Dates.calcDay(start, end) + 1;
        int count = new Random().nextInt((int) days);
        return Dates.calcDay(start, count);
    }

    /**
     * 判断年月日是否是合法日期
     *
     * @param year       年份
     * @param month      月份（从1到12）
     * @param dayOfMonth 月份中的日（从1到31）
     * @return true表示合法
     */
    static boolean isDate(int year, int month, int dayOfMonth) {
        if (year < 1000 || year > 9999) {
            return false;
        } else if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) { // 大月
            return dayOfMonth >= 1 && dayOfMonth <= 31;
        } else if (month == 2) {
            if (dayOfMonth >= 1 && dayOfMonth <= 28) {
                return true;
            } else if (dayOfMonth == 29) {
                return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
            } else {
                return false;
            }
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            return dayOfMonth >= 1 && dayOfMonth <= 30;
        } else {
            return false;
        }
    }

    /**
     * 判断时间是否正确
     *
     * @param hour        小时（从0到23）
     * @param minute      分钟（从0到59）
     * @param second      秒数（从0到59）
     * @param millisecond 秒数（从0到999）
     * @return 返回true表示正确 false表示错误
     */
    static boolean isTime(int hour, int minute, int second, int millisecond) {
        return hour >= 0 && hour <= 23 && minute >= 0 && minute <= 59 && second >= 0 && second <= 59 && millisecond >= 0 && millisecond <= 999;
    }

    /**
     * 支持字符数组需要按如下时间格式排列
     * <p>
     * HHmmss
     * HHmmssSS
     * HHmmssSSS
     * <p>
     *
     * @param array 每个字符含义：
     *              h1          小时的第一位
     *              h2          小时的第二位
     *              h3          分钟的第一位
     *              h4          分钟的第二位
     *              h5          秒钟的第一位
     *              h6          秒钟的第二位
     *              millisecond 毫秒字符数组，为null或空数组时表示不需要校验毫秒
     * @return 返回true表示格式正确 false表示格式错误
     */
    static boolean isTime(char... array) {
        if (array.length < 6 || array.length > 9) { // 判断时间字符串长度
            return false;
        }

        if (array[0] == '0' || array[0] == '1') {
            if (!StringUtils.isNumber(array[1])) {
                return false;
            }
        } else if (array[0] == '2') {
            if (array[1] != '0' && array[1] != '1' && array[1] != '2' && array[1] != '3') {
                return false;
            }
        } else {
            return false;
        }

        if ((array[2] != '0' && array[2] != '1' && array[2] != '2' && array[2] != '3' && array[2] != '4' && array[2] != '5') || !StringUtils.isNumber(array[3])) {
            return false;
        }

        if ((array[4] != '0' && array[4] != '1' && array[4] != '2' && array[4] != '3' && array[4] != '4' && array[4] != '5') || !StringUtils.isNumber(array[5])) {
            return false;
        }

        if (array.length == 6) {
            return true;
        } else {
            for (int i = 6; i < array.length; i++) {
                if (!StringUtils.isNumber(array[i])) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * 判断字符串是否为标准时间字符串（支持的格式: hh 或 hh:mm 或 hh:mm:ss 或 hh:mm:ss:SS）
     * 时间的分隔符要与方法 {@linkplain #parseTime(String, int[])} 中保持一致
     *
     * @param str 字符串
     * @return 返回true表示格式正确 false表示格式错误
     */
    static boolean isTime(String str) {
        if (StringUtils.isBlank(str) || StringUtils.indexOfBlank(str, 0, -1) != -1) {
            return false;
        }

        String[] array = StringUtils.split(str, Dates.TIME_DELIMITER, true);
        if (array.length == 0 || array.length > 4) {
            return false;
        }

        char h1 = '0', h2 = '0', m1 = '0', m2 = '0', s1 = '0', s2 = '0', l1 = '0', l2 = '0', l3 = '0'; // 小时 分钟 秒钟 毫秒
        if (array[0].length() == 1) {
            h2 = array[0].charAt(0);
        } else if (array[0].length() == 2) {
            h1 = array[0].charAt(0);
            h2 = array[0].charAt(1);
        } else {
            return false;
        }

        if (array.length >= 2) {
            if (array[1].length() == 1) {
                m2 = array[1].charAt(0);
            } else if (array[1].length() == 2) {
                m1 = array[1].charAt(0);
                m2 = array[1].charAt(1);
            } else {
                return false;
            }
        }

        if (array.length >= 3) {
            if (array[2].length() == 1) {
                s2 = array[2].charAt(0);
            } else if (array[2].length() == 2) {
                s1 = array[2].charAt(0);
                s2 = array[2].charAt(1);
            } else {
                return false;
            }
        }

        if (array.length == 4) {
            if (array[3].length() == 1) {
                l3 = array[3].charAt(0);
            } else if (array[3].length() == 2) {
                l2 = array[3].charAt(0);
                l3 = array[3].charAt(1);
            } else if (array[3].length() == 3) {
                l1 = array[3].charAt(0);
                l2 = array[3].charAt(1);
                l3 = array[3].charAt(2);
            } else {
                return false;
            }
        }

        return Dates.isTime(h1, h2, m1, m2, s1, s2, l1, l2, l3);
    }

    /**
     * 判断字符串是否是时区信息
     *
     * @param str 时区字符串: UTC, 不区分大小写
     * @return 返回true表示格式正确 false表示格式错误
     */
    private static boolean isTimeZone(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }

        // 解析时区信息中加减号前的内容
        str = StringUtils.trimBlank(str);
        int zf = str.indexOf('+');
        if (zf == -1) {
            zf = str.length();
        }

        int jf = str.indexOf('-'); // 解析减号前的符号
        if (jf == -1) {
            jf = str.length();
        }
        str = StringUtils.trimBlank(str.substring(0, Math.min(zf, jf))); // 截取时区中加号和减号前的时区信息

        String[] array = TimeZone.getAvailableIDs(); // 返回环境中的所有时区信息
        for (String zone : array) { // 便利时区
            // 时区信息等长
            if (zone.equalsIgnoreCase(str)) {
                return true;
            }

            // 输入字符串大于标准时区信息
            if (str.length() > zone.length()) {
                continue;
            }

            // 输入字符串长度小于标准时区信息
            int i = 0;
            while ((i = StringUtils.indexOf(zone, str, i, true)) != -1) { // 在标准时区信息中搜索输入字符串
                String substr = StringUtils.trimBlank(StringUtils.substring(zone, i, 1, str.length() + 1)); // 截取时区信息

                // 时区相等
                if (substr.equalsIgnoreCase(str)) {
                    return true;
                }

                if (substr.length() < str.length()) {
                    continue;
                }

                if ((substr.length() == str.length() + 2) && !StringUtils.isLetter(substr.charAt(0)) && !StringUtils.isLetter(substr.charAt(substr.length() - 1))) {
                    return true;
                }

                if (substr.length() == str.length() + 1) {
                    int es = StringUtils.indexOf(substr, str, 0, true); // 从0开始搜索输入的时区信息
                    if (es != -1) {
                        if (es == 0) {
                            if (!StringUtils.isLetter(substr.charAt(substr.length() - 1))) {
                                return true;
                            } else {
                                continue;
                            }
                        } else if (es == 1) {
                            if (!StringUtils.isLetter(substr.charAt(0))) {
                                return true;
                            } else {
                                continue;
                            }
                        } else {
                            throw new IllegalArgumentException("The string " + str + " is neither the starting position nor the second character in the string " + substr + "!");
                        }
                    }
                    return true;
                }

                i += str.length(); // 位置加一
            }
        }

        return false;
    }

    /**
     * 返回日期的年份
     *
     * @param date 日期
     * @return 年份
     */
    public static int getYear(Date date) {
        if (date == null) {
            throw new NullPointerException();
        }

        Calendar cr = Calendar.getInstance();
        cr.setTime(date);
        return cr.get(Calendar.YEAR);
    }

    /**
     * 返回日期的月份
     *
     * @param date 日期
     * @return 1-12
     */
    public static int getMonth(Date date) {
        if (date == null) {
            throw new NullPointerException();
        }

        Calendar cr = Calendar.getInstance();
        cr.setTime(date);
        return cr.get(Calendar.MONTH) + 1;
    }

    /**
     * 返回时间的小时
     *
     * @param date 日期
     * @return 1-24
     */
    public static int getHour(Date date) {
        if (date == null) {
            throw new NullPointerException();
        }

        Calendar cr = Calendar.getInstance();
        cr.setTime(date);
        return cr.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 返回时间的分钟数
     *
     * @param date 日期
     * @return 0-59
     */
    public static int getMinute(Date date) {
        if (date == null) {
            throw new NullPointerException();
        }

        Calendar cr = Calendar.getInstance();
        cr.setTime(date);
        return cr.get(Calendar.MINUTE);
    }

    /**
     * 返回时间的秒数
     *
     * @param date 日期
     * @return 0-59
     */
    public static int getSecond(Date date) {
        if (date == null) {
            throw new NullPointerException();
        }

        Calendar cr = Calendar.getInstance();
        cr.setTime(date);
        return cr.get(Calendar.SECOND);
    }

    /**
     * 返回时间的毫秒数
     *
     * @param date 日期
     * @return 0-999
     */
    public static int getMillisecond(Date date) {
        if (date == null) {
            throw new NullPointerException();
        }

        Calendar cr = Calendar.getInstance();
        cr.setTime(date);
        return cr.get(Calendar.MILLISECOND);
    }

    /**
     * 判断 {@code date} 是在 {@code start} 与 {@code end} 之间的日期（包含 start 与 end）
     *
     * @param date  日期
     * @param start 起始日
     * @param end   终止日
     * @return 返回true表示日期在范围之内 false表示不在范围之内
     */
    public static boolean between(Date date, Date start, Date end) {
        return date.compareTo(start) >= 0 && date.compareTo(end) <= 0;
    }

    /**
     * 判断日期参数date是否是月初一号
     *
     * @param date 日期
     * @return 返回true表示日期是月初第一天 false表示日期不是月初第一天
     */
    public static boolean isBeginOfMonth(Date date) {
        return Dates.getDayOfMonth(date) == 1;
    }

    /**
     * 判断日期参数date是否是月末最后一天
     *
     * @param date 日期
     * @return 返回true表示日期是月末最后一天 false表示日期不是月末最后一天
     */
    public static boolean isEndOfMonth(Date date) {
        return date.equals(Dates.getEndOfMonth(date));
    }

    /**
     * 判断字符串是否是月份英文全称或三位缩写
     *
     * @param str 字符串, 如: January 或 Jan
     * @return true表示字符串为月份字符串
     */
    public static boolean isMonth(String str) {
        if (str == null) {
            return false;
        }

        str = StringUtils.removeBlank(str);
        if ("Jan".equalsIgnoreCase(str) || Dates.January.equalsIgnoreCase(str) || str.equals("一月") || str.equals("1月")) {
            return true;
        }
        if ("Feb".equalsIgnoreCase(str) || Dates.February.equalsIgnoreCase(str) || str.equals("二月") || str.equals("2月")) {
            return true;
        }
        if ("Mar".equalsIgnoreCase(str) || Dates.March.equalsIgnoreCase(str) || str.equals("三月") || str.equals("3月")) {
            return true;
        }
        if ("Apr".equalsIgnoreCase(str) || Dates.April.equalsIgnoreCase(str) || str.equals("四月") || str.equals("4月")) {
            return true;
        }
        if ("May".equalsIgnoreCase(str) || Dates.May.equalsIgnoreCase(str) || str.equals("五月") || str.equals("5月")) {
            return true;
        }
        if ("Jun".equalsIgnoreCase(str) || Dates.June.equalsIgnoreCase(str) || str.equals("六月") || str.equals("6月")) {
            return true;
        }
        if ("Jul".equalsIgnoreCase(str) || Dates.July.equalsIgnoreCase(str) || str.equals("七月") || str.equals("7月")) {
            return true;
        }
        if ("Aug".equalsIgnoreCase(str) || Dates.August.equalsIgnoreCase(str) || str.equals("八月") || str.equals("8月")) {
            return true;
        }
        if ("Sep".equalsIgnoreCase(str) || Dates.September.equalsIgnoreCase(str) || str.equals("九月") || str.equals("9月")) {
            return true;
        }
        if ("Oct".equalsIgnoreCase(str) || Dates.October.equalsIgnoreCase(str) || str.equals("十月") || str.equals("10月")) {
            return true;
        }
        if ("Nov".equalsIgnoreCase(str) || Dates.November.equalsIgnoreCase(str) || str.equals("十一月") || str.equals("11月")) {
            return true;
        }
        if ("Dec".equalsIgnoreCase(str) || Dates.December.equalsIgnoreCase(str) || str.equals("十二月") || str.equals("12月")) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串(如: Mon, Monday, Tuesday, Wednesday, 星期一, 星期1)是星期几
     *
     * @param str 字符串
     * @return 返回true表示字符串是周日 false表示字符串不是周日
     */
    public static boolean isDayOfWeek(String str) {
        if (str == null) {
            return false;
        }

        str = StringUtils.removeBlank(str);
        if ("Mon".equalsIgnoreCase(str) || Monday.equalsIgnoreCase(str) || str.equals("星期一") || str.equals("星期1")) {
            return true;
        }
        if ("Tue".equalsIgnoreCase(str) || Tuesday.equalsIgnoreCase(str) || str.equals("星期二") || str.equals("星期2")) {
            return true;
        }
        if ("Wed".equalsIgnoreCase(str) || Wednesday.equalsIgnoreCase(str) || str.equals("星期三") || str.equals("星期3")) {
            return true;
        }
        if ("Thu".equalsIgnoreCase(str) || Thursday.equalsIgnoreCase(str) || str.equals("星期四") || str.equals("星期4")) {
            return true;
        }
        if ("Fri".equalsIgnoreCase(str) || Friday.equalsIgnoreCase(str) || str.equals("星期五") || str.equals("星期5")) {
            return true;
        }
        if ("Sat".equalsIgnoreCase(str) || Saturday.equalsIgnoreCase(str) || str.equals("星期六") || str.equals("星期6")) {
            return true;
        }
        if ("Sun".equalsIgnoreCase(str) || Sunday.equalsIgnoreCase(str) || str.equals("星期天") || str.equals("星期七") || str.equals("星期7")) {
            return true;
        }
        return false;
    }

    /**
     * 判断日期参数date是否是双休日（周六、周日）
     * 忽略法定补休日
     *
     * @param date 日期
     * @return 返回true表示日期是双休日 false表示日期不是双休日
     */
    public static boolean isWeekend(Date date) {
        if (date == null) {
            return false;
        }

        int t = Dates.getDayOfWeek(date);
        return t == 6 || t == 7;
    }

    /**
     * 比较2个日期对象大小
     *
     * @param d1 日期1, 可以为null
     * @param d2 日期2, 可以为null
     * @return 0表示日期相等(二个null值相等或二个日期值相等) 大于0表示日期d1大于日期d2 小于零表示日期d1小于日期d2
     */
    public static int compare(Date d1, Date d2) {
        boolean e1 = d1 == null;
        boolean e2 = d2 == null;
        if (e1 && e2) {
            return 0;
        } else if (e1) {
            return -1;
        } else if (e2) {
            return 1;
        } else {
            return d1.compareTo(d2);
        }
    }

    /**
     * 比较年月日, 忽略小时分钟秒毫秒
     *
     * @param d1 日期1, 可以为null
     * @param d2 日期2, 可以为null
     * @return 0表示日期相等(二个null值相等或二个日期值相等) 大于0表示日期d1大于日期d2 小于零表示日期d1小于日期d2
     */
    public static int compareIgnoreTime(Date d1, Date d2) {
        boolean e1 = d1 == null;
        boolean e2 = d2 == null;
        if (e1 && e2) {
            return 0;
        } else if (e1) {
            return -1;
        } else if (e2) {
            return 1;
        } else {
            Calendar cr = Calendar.getInstance();
            cr.setTime(d1);
            cr.set(Calendar.HOUR_OF_DAY, 0);
            cr.set(Calendar.MINUTE, 0);
            cr.set(Calendar.SECOND, 0);
            cr.set(Calendar.MILLISECOND, 0);
            long ml1 = cr.getTimeInMillis();

            cr.setTime(d2);
            cr.set(Calendar.HOUR_OF_DAY, 0);
            cr.set(Calendar.MINUTE, 0);
            cr.set(Calendar.SECOND, 0);
            cr.set(Calendar.MILLISECOND, 0);
            long ml2 = cr.getTimeInMillis();

            long c = ml1 - ml2;
            if (c > 0) {
                return 1;
            } else if (c < 0) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    /**
     * 比较2个日期的年月
     *
     * @param d1 日期
     * @param d2 日期
     * @return 返回0表示2个日期相等 大于0表示d1大于d2 小于0表示d1小于d2
     */
    public static int compareIgnoreDay(Date d1, Date d2) {
        if (d1 == null || d2 == null) {
            throw new NullPointerException(format10(d1) + ", " + format10(d2));
        }

        Calendar cr = Calendar.getInstance();
        cr.setTime(d1);
        int y1 = cr.get(Calendar.YEAR);
        int m1 = cr.get(Calendar.MONTH);

        cr.setTime(d2);
        int y2 = cr.get(Calendar.YEAR);
        int m2 = cr.get(Calendar.MONTH);
        int y = y1 - y2;
        return y == 0 ? m1 - m2 : y;
    }

    /**
     * 使用参数pattern 解析字符串str
     *
     * @param str     字符串
     * @param pattern 日期格式: yyyy-MM-dd 或 yyyyMMdd
     * @return 返回true表示字符串与模式匹配 false表示不匹配
     */
    public static boolean match(String str, String pattern) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern(pattern);
            sdf.parse(str);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * 返回日期数组中最小值
     *
     * @param array 日期数组
     * @return 日期
     */
    public static Date min(Date... array) {
        if (array == null || array.length == 0) {
            return null;
        }

        Date min = array[0];
        for (int i = 1; i < array.length; i++) {
            Date date = array[i];
            if (date != null) {
                if (min == null) {
                    min = date;
                    continue;
                }

                if (date.compareTo(min) < 0) {
                    min = date;
                }
            }
        }
        return min;
    }

    /**
     * 返回日期数组中最大的日期数值
     *
     * @param array 日期数组
     * @return 日期
     */
    public static Date max(Date... array) {
        if (array == null || array.length == 0) {
            return null;
        }

        Date max = array[0];
        for (int i = 1; i < array.length; i++) {
            Date date = array[i];
            if (date != null) {
                if (max == null) {
                    max = date;
                    continue;
                }

                if (date.compareTo(max) > 0) {
                    max = date;
                }
            }
        }
        return max;
    }

    /**
     * 判断年份参数year是否为闰年
     *
     * @param year 年, 如： 2015 或 2016 等
     * @return true表示闰年
     */
    public static boolean isLeapYear(int year) {
        return year > 0 && ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0));
    }

    /**
     * 从起始日期 start 开始到 end 为止，计算2个日期之间的所有日期
     *
     * @param start 起始日（包含），格式参考：{@linkplain #parse(Object)}
     * @param end   终止日（包含），格式参考：{@linkplain #parse(Object)}
     * @return 日期字符串集合
     */
    public static List<String> tolist(String start, String end) {
        Ensure.notBlank(start);
        Ensure.notBlank(end);
        Date date = Dates.parse(start), e = Dates.parse(end);
        List<String> list = new ArrayList<String>(31);
        String pattern = Dates.pattern(start);
        while (date.compareTo(e) <= 0) {
            list.add(Dates.format(date, pattern));
            date = Dates.calcDay(date, 1);
        }
        return list;
    }

}