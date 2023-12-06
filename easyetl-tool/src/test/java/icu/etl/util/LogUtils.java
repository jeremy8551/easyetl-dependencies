package icu.etl.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 日志辅助类
 *
 * @author jeremy8551@qq.com
 * @createtime 2023/11/22
 */
public class LogUtils {

    /** 文件系统的换行符 */
    public final static String lineSeparator = System.getProperty("line.separator");

    /**
     * 读取文件中的所有内容
     *
     * @param file        文件
     * @param charsetName 文件的字符集
     * @return 文件内容
     * @throws IOException 访问文件错误
     */
    public static String readline(File file, String charsetName) throws IOException {
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            byte[] buf = new byte[(int) file.length()];
            in.read(buf);
            return new String(buf, charsetName);
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    /**
     * 返回堆栈信息
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
            StackTraceElement e = array[i];
            if (name.equals(e.getClassName())) {
                int next = i + 1;
                return next < array.length ? array[next] : e;
            }
        }

        return new StackTraceElement("?", "?", "?", -1);
    }

    /**
     * 判断字符串参数str是否为空白字符串(为null或全是空白字符)
     * isBlank("") == true
     * isBlank("12") == false
     * isBlank(" ") == true
     * isBlank(null) == true
     *
     * @param str 字符串
     * @return
     */
    public static boolean isBlank(CharSequence str) {
        if (str != null) {
            for (int i = 0, size = str.length(); i < size; i++) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 逐个判断字符串 str 中的每个字符，判断是否全部为数字字符
     *
     * @param str 字符串
     * @return
     */
    public static boolean isNumber(CharSequence str) {
        if (str == null || str.length() == 0) {
            return false;
        }

        for (int i = str.charAt(0) == '-' ? 1 : 0; i < str.length(); i++) {
            if ("0123456789".indexOf(str.charAt(i)) == -1) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符参数c是否为26个英文字母之一(包括大写与小写)
     *
     * @param c 字符
     * @return
     */
    public static boolean isLetter(char c) {
        return "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(c) != -1;
    }

    /**
     * 判断字符数组参数array是否全部为数字（0,1,2,3,4,5,6,7,8,9）
     *
     * @param array 字符数组
     * @return
     */
    public static boolean isNumber(char... array) {
        if (array.length == 0) {
            return false;
        }

        for (int i = 0; i < array.length; i++) {
            if ("0123456789".indexOf(array[i]) == -1) {
                return false;
            }
        }
        return true;
    }

    /**
     * 将输入参数 obj 转为字符串
     *
     * @param e 对象信息
     * @return
     */
    public static void append(Throwable e, StringBuilder buf) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(out);
        e.printStackTrace(stream);

        buf.append(out.toString());
        if (e instanceof SQLException) {
            SQLException sql = (SQLException) e;
            while (sql != null) {
                buf.append(FileUtils.lineSeparator);
                buf.append(sql.getClass().getName());
                buf.append("[");
                buf.append("SQLSTATE = ");
                buf.append(sql.getSQLState());
                buf.append(", ERRORCODE = ");
                buf.append(sql.getErrorCode());
                buf.append(", MESSAGE = ");
                buf.append(sql.getMessage());
                buf.append("]");

                sql = sql.getNextException();
            }
        }
    }

    /**
     * 从文件路径中解析文件名(不含扩展名)
     * "mypath/myfile.txt" == "myfile"
     *
     * @param filepath 文件绝对路径
     * @return
     */
    public static String getFilenameNoExt(String filepath) {
        if (filepath == null) {
            return null;
        }

        int lx = filepath.lastIndexOf('/');
        int lf = filepath.lastIndexOf('\\');
        int lp = lx > lf ? lx : lf;
        if (lp < 0) { // 不包含目录
            lp = -1;
        }
        if (lp + 1 == filepath.length()) {
            return "";
        }

        int end = filepath.lastIndexOf('.');
        return (end == -1 || end <= lp) ? filepath.substring(lp + 1) : filepath.substring(lp + 1, end);
    }

    /**
     * 判断是否包含字符数组中的任何一个字符
     *
     * @param str   字符串
     * @param array 字符数组
     * @return 返回true表示存在字符
     */
    public static boolean contains(CharSequence str, char... array) {
        for (int i = 0, length = str.length(); i < length; i++) {
            char c = str.charAt(i);
            for (char ac : array) {
                if (ac == c) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 提取字符串str中行（回车符与换行符作为分隔符），并保存到 {@code list}
     *
     * @param str  字符串
     * @param list 字段集合，用于存储解析后的所有字段，为null时会创建一个 #{@linkplain ArrayList} 作为返回值
     * @return 返回 true 表示字符串参数中没有回车或换行符，返回false表示字符串参数中有回车或换行符
     */
    public static void splitLines(String str, List<CharSequence> list) {
        list.clear();

        int length;
        if (str == null || (length = str.length()) == 0) {
            list.add("");
            return;
        }

        int next = 0;
        for (int i = 0; i < length; i++) {
            char c = str.charAt(i);

            if (c == '\n') {
                list.add(str.substring(next, i));
                next = i + 1;
                continue;
            }

            if (c == '\r') {
                list.add(str.substring(next, i));
                next = i + 1;
                if (next < length && str.charAt(next) == '\n') {
                    i = next;
                    next = i + 1;
                }
                continue;
            }
        }

        if (next < length) {
            list.add(str.substring(next, length));
        }
    }

    /**
     * 加载类，不使用工具包中的方法，防止日志模块与工具包高度耦合
     *
     * @param className java类全名
     * @return 返回类信息
     */
    @SuppressWarnings("unchecked")
    public static <E> Class<E> forName(String className) {
        try {
            return (Class<E>) Class.forName(className);
        } catch (Throwable e) {
            return null;
        }
    }

}
