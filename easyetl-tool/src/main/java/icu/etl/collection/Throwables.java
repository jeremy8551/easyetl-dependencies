package icu.etl.collection;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 存储多个异常
 *
 * @author jeremy8551@qq.com
 * @createtime 2023/11/26
 */
public class Throwables extends RuntimeException {

    /** 异常集合 */
    private final List<String> messages;

    /** 异常错误信息 */
    private final List<Throwable> list;

    /**
     * 异常集合
     */
    public Throwables() {
        this("");
    }

    /**
     * 异常集合
     *
     * @param message 异常信息
     */
    public Throwables(String message) {
        super(message);
        this.messages = new ArrayList<String>();
        this.list = new ArrayList<Throwable>();
    }

    /**
     * 添加异常信息
     *
     * @param message 信息
     * @param e       异常信息
     */
    public void add(String message, Throwable e) {
        this.list.add(e);
        this.messages.add(message);
    }

    /**
     * 添加异常信息
     *
     * @param e 异常信息
     */
    public void add(Throwable e) {
        this.add(e.getLocalizedMessage(), e);
    }

    /**
     * 返回集合中异常个数
     *
     * @return 集合元素个数
     */
    public int size() {
        return this.messages.size();
    }

    /**
     * 返回true表示不为空
     *
     * @return 返回true表示集合不是空的 返回false表示集合是空的
     */
    public boolean notEmpty() {
        return this.messages.size() > 0;
    }

    public void printStackTrace() {
        super.printStackTrace();
    }

    public void printStackTrace(PrintStream s) {
        int size = this.messages.size();
        for (int i = 0; i < size; i++) {
            String msg = this.messages.get(i);
            s.println(msg);

            Throwable e = this.list.get(i);
            e.printStackTrace(s);
        }
    }

    public void printStackTrace(PrintWriter s) {
        int size = this.messages.size();
        for (int i = 0; i < size; i++) {
            String msg = this.messages.get(i);
            s.println(msg);

            Throwable e = this.list.get(i);
            e.printStackTrace(s);
        }
    }

    public String toString() {
        StringBuilder buf = new StringBuilder(100);
        int size = this.messages.size();
        for (int i = 0; i < size; i++) {
            String msg = this.messages.get(i);
            buf.append(msg);
            buf.append(System.getProperty("line.separator"));

            Throwable e = this.list.get(i);
            buf.append(e.toString());
            buf.append(System.getProperty("line.separator"));
        }
        return buf.toString();
    }

}
