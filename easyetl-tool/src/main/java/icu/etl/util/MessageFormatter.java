package icu.etl.util;

/**
 * 字符串格式化工具
 *
 * @author jeremy8551@qq.com
 * @createtime 2023/11/23
 */
public class MessageFormatter {

    private StringBuilder buf;

    public MessageFormatter() {
        this.buf = new StringBuilder(100);
    }

    /**
     * 将字符串中的占位符 {} 替换为数组元素
     *
     * @param message 字符串
     * @param args    数组
     * @return 字符串
     */
    public String format(CharSequence message, Object[] args) {
        buf.setLength(0);
        int length = message.length();
        for (int i = 0, j = 0; i < length; i++) {
            char c = message.charAt(i);

            // 转义字符
            if (c == '\\') {
                buf.append(c);
                buf.append(message.charAt(++i));
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
     * 将字符串中的占位符 {} 替换为数组元素
     *
     * @param message 字符串
     * @param e       数组
     * @return 字符串
     */
    public String format(CharSequence message, Throwable e) {
        buf.setLength(0);
        buf.append(message);
        buf.append(FileUtils.lineSeparator);
        buf.append(StringUtils.toString(e));
        return buf.toString();
    }

}
