package icu.etl.util;

import icu.etl.Easyetl;

/**
 * 日志工具类
 *
 * @author jeremy8551@qq.com
 * @createtime 2023/11/24
 */
public class LogUtils {

    /** 属性值设置为true，表示打印日志堆栈 */
    public final static String PROPERTY_LOG_STACKTRACE = Easyetl.class.getPackage().getName() + ".logStackTrace";

    /** 是否打印日志跟踪信息 */
    public static volatile boolean print = Boolean.parseBoolean(System.getProperty(PROPERTY_LOG_STACKTRACE));

    /**
     * 返回堆栈信息 <br>
     * 用于定位输出日志的代码位置 <br>
     * 这个方法只能是单独一个类，不能写在其他日志系统中
     *
     * @param name 类名
     * @return 堆栈信息
     */
    public static StackTraceElement getStackTrace(String name) {
        StackTraceElement[] array = new Throwable().getStackTrace();

        if (print) {
            for (StackTraceElement e : array) {
                System.out.println(e.getClassName() + "." + e.getMethodName() + ":" + e.getLineNumber());
            }
        }

        for (int i = 0; i < array.length; i++) {
            StackTraceElement trace = array[i];
            if (name.equals(trace.getClassName())) {
                int next = i + 1;
                return next < array.length ? array[next] : trace;
            }
        }

        return new StackTraceElement("?", "?", "?", -1);
    }

}
