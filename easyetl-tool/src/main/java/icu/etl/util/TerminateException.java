package icu.etl.util;

/**
 * 执行中断操作发生异常
 *
 * @author jeremy8551@qq.com
 */
public class TerminateException extends Exception {
    private final static long serialVersionUID = 1L;

    public TerminateException() {
        super();
    }

//	public TerminateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
//		super(message, cause, enableSuppression, writableStackTrace);
//	}

    public TerminateException(String message, Throwable cause) {
        super(message, cause);
    }

    public TerminateException(String message) {
        super(message);
    }

    public TerminateException(Throwable cause) {
        super(cause);
    }

}
