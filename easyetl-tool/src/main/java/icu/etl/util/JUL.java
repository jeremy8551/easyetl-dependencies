package icu.etl.util;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * JDK日志系统
 *
 * @author jeremy8551@qq.com
 * @createtime 2023/11/24
 */
public class JUL {
    public static Logger out = Logger.getLogger(JUL.class.getName());

    static {
        JUL.reset(out);
    }

    /**
     * 初始化
     */
    public static void reset(Logger out) {
        Handler[] handlers = out.getHandlers();

        // 移除所有
        for (Handler handler : handlers) {
            out.removeHandler(handler);
        }

        // 将自定义的handler放在第一位
        out.addHandler(new TraceHandler());

        // 重新添加
        for (Handler handler : handlers) {
            out.addHandler(handler);
        }
    }

    public static boolean isTraceEnabled() {
        return out.isLoggable(Level.FINEST);
    }

    public static boolean isDebugEnabled() {
        return out.isLoggable(Level.CONFIG);
    }

    public static boolean isInfoEnabled() {
        return out.isLoggable(Level.INFO);
    }

    public static boolean isWarnEnabled() {
        return out.isLoggable(Level.WARNING);
    }

    public static boolean isErrorEnabled() {
        return out.isLoggable(Level.SEVERE);
    }

    public static boolean isFatalEnabled() {
        return out.isLoggable(Level.SEVERE);
    }

    public static void trace(String msg, Object... args) {
        out.log(Level.FINEST, new MessageFormatter().format(msg, args));
    }

    public static void trace(String msg, Throwable e) {
        out.log(Level.FINEST, new MessageFormatter().format(msg, e));
    }

    public static void debug(String msg, Object... args) {
        out.log(Level.CONFIG, new MessageFormatter().format(msg, args));
    }

    public static void debug(String msg, Throwable e) {
        out.log(Level.CONFIG, new MessageFormatter().format(msg, e));
    }

    public static void info(String msg, Object... args) {
        out.log(Level.INFO, new MessageFormatter().format(msg, args));
    }

    public static void info(String msg, Throwable e) {
        out.log(Level.INFO, new MessageFormatter().format(msg, e));
    }

    public static void warn(String msg, Object... args) {
        out.log(Level.WARNING, new MessageFormatter().format(msg, args));
    }

    public static void warn(String msg, Throwable e) {
        out.log(Level.WARNING, new MessageFormatter().format(msg, e));
    }

    public static void error(String msg, Object... args) {
        out.log(Level.SEVERE, new MessageFormatter().format(msg, args));
    }

    public static void error(String msg, Throwable e) {
        out.log(Level.SEVERE, new MessageFormatter().format(msg, e));
    }

    public static void fatal(String msg, Object... args) {
        out.log(Level.SEVERE, new MessageFormatter().format(msg, args));
    }

    public static void fatal(String msg, Throwable e) {
        out.log(Level.SEVERE, new MessageFormatter().format(msg, e));
    }

    /**
     * 让JUL日志准确输出代码位置
     */
    public static class TraceHandler extends Handler {

        public TraceHandler() {
            super();
        }

        public void publish(LogRecord record) {
            StackTraceElement trace = LogUtils.getStackTrace(JUL.class.getName());
            record.setLoggerName(trace.getClassName());
            record.setSourceClassName(trace.getClassName());
            record.setSourceMethodName(trace.getMethodName());
        }

        public void flush() {
        }

        public void close() {
        }
    }
}
