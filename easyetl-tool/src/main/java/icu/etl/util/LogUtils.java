package icu.etl.util;

/**
 * 日志工具类
 *
 * @author jeremy8551@qq.com
 * @createtime 2023/11/24
 */
public class LogUtils {

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

//        for (StackTraceElement e : array) {
//            System.out.println(e.getClassName() + "." + e.getMethodName() + ":" + e.getLineNumber());
//        }

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
