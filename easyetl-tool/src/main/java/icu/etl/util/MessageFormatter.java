package icu.etl.util;

/**
 * 字符串格式化工具
 *
 * @author jeremy8551@qq.com
 * @createtime 2023/11/23
 */
public class MessageFormatter {

    /**
     * 占位符类型
     */
    public enum Placeholder {

        /** 无参数方式, 如: {} */
        SIMPLE,

        /** 占位符类型是：{1} */
        NORMAL
    }

    /** 占位符类型 */
    private Placeholder type;

    /**
     * 占位符类型是：{}
     */
    public MessageFormatter() {
        this(Placeholder.SIMPLE);
    }

    /**
     * 字符串格式化工具
     *
     * @param type 占位符类型
     */
    public MessageFormatter(Placeholder type) {
        if (type == null) {
            throw new NullPointerException();
        }
        this.type = type;
    }

    /**
     * 将字符串中的占位符替换为数组中的元素
     *
     * @param message 字符串
     * @param args    数组
     * @return 字符串
     */
    public String format(CharSequence message, Object[] args) {
        switch (this.type) {
            case SIMPLE:
                return this.format0(message, args);

            case NORMAL:
                return this.format1(message, args);

            default:
                throw new UnsupportedOperationException(String.valueOf(this.type));
        }
    }

    /**
     * 将字符串中的占位符 {} 替换为数组元素
     *
     * @param message 字符串
     * @param args    数组
     * @return 字符串
     */
    private String format0(CharSequence message, Object[] args) {
        StringBuilder buf = new StringBuilder(message.length());
        int length = message.length();
        boolean escape = false;
        for (int i = 0, j = 0; i < length; i++) {
            char c = message.charAt(i);

            // 转义字符
            if (c == '\\') {
                escape = true;
                continue;
            }

            // 转义字符
            if (escape) {
                buf.append('\\');
                buf.append(c);
                escape = false;
                continue;
            }

            // 替换 {}
            int next = i + 1;
            if (c == '{' && next < length && message.charAt(next) == '}' && j < args.length) {
                buf.append(args[j++]);
                i = next;
            } else {
                buf.append(c);
            }
        }
        return buf.toString();
    }

    /**
     * 将字符串中的占位符 {1} 替换为数组元素
     *
     * @param message 字符串
     * @param args    数组
     * @return 字符串
     */
    private String format1(CharSequence message, Object[] args) {
        System.out.println();
        StringBuilder buf = new StringBuilder(message.length());
        int length = message.length();
        boolean escape = false;
        for (int i = 0; i < length; i++) {
            char c = message.charAt(i);

            // 转义字符
            if (c == '\\') {
                escape = true;
                continue;
            }

            // 转义字符
            if (escape) {
                buf.append(c);
                escape = false;
                continue;
            }

            // 替换 {}
            if (c == '{') {
                int start = i + 1;
                int end = this.indexOfBrace(message, i);
                if (end != -1 && this.isInt(message, start, end)) {
                    CharSequence intExpr = message.subSequence(start, end);
                    int position = Integer.parseInt(intExpr.toString());
                    if (position >= 0 && position < args.length) {
                        buf.append(args[position]);
                        i = end;
                        continue;
                    }
                }
            }

            // 追加字符
            buf.append(c);
        }
        return buf.toString();
    }

    /**
     * 将字符串解析为整数
     *
     * @param str  字符串
     * @param from 整数开始位置，从0开始
     * @param end  整数结束位置，从0开始
     * @return 返回true表示是整数 false表示不是整数
     */
    private boolean isInt(CharSequence str, int from, int end) {
        int size = end - from;

        // 如果字符串为空
        if (size == 0) {
            return false;
        }

        // 排除单数0的情况
        if (size >= 2) {
            boolean first = true;
            for (int i = from; first && i < end; i++) {
                char c = str.charAt(from);
                if (c == '0') {
                    return false; // 如果数字的前缀是0，则返回false
                } else {
                    first = false;
                }
            }
        }

        // 检查每位字符是否是数字
        for (int i = from; i < end; i++) {
            char c = str.charAt(i);
            if (!StringUtils.isNumber(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 搜索大括号结束的位置
     *
     * @param str  字符串
     * @param from 起始位置，从0开始
     * @return 返回打括号结束位置，从0开始，返回-1表示未找到结束位置
     */
    private int indexOfBrace(CharSequence str, int from) {
        for (int i = from + 1; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '}') {
                return i;
            }
        }
        return -1;
    }

    /**
     * 将字符串中的占位符 {} 替换为数组元素
     *
     * @param message 字符串
     * @param e       数组
     * @return 字符串
     */
    public String format(CharSequence message, Throwable e) {
        StringBuilder buf = new StringBuilder(message.length());
        buf.append(message);
        buf.append(FileUtils.lineSeparator);
        buf.append(StringUtils.toString(e));
        return buf.toString();
    }

}
