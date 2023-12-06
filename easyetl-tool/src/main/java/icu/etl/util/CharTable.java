package icu.etl.util;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 字符图形表格
 * e.g:
 * {@linkplain CharTable} table = new {@linkplain CharTable}();
 * table.{@linkplain #addTitle(String)};
 * table.{@linkplain #addTitle(String)};
 * <p>
 * table.{@linkplain #addCell(Object)};
 * table.{@linkplain #addCell(Object)};
 * <p>
 * table.{@linkplain #addCell(Object)};
 * table.{@linkplain #addCell(Object)};
 * <p>
 * table.{@linkplain #addCell(Object)};
 * table.{@linkplain #addCell(Object)};
 * <p>
 * table.{@linkplain #toString(Render)};
 *
 * @author jeremy8551@qq.com
 * @createtime 2012-04-11
 */
public class CharTable implements Iterable<String> {

    /** 单元格左对齐 */
    public final static String ALIGN_LEFT = "LEFT";

    /** 单元格右对齐 */
    public final static String ALIGN_RIGHT = "RIGHT";

    /** 单元格居中对齐 */
    public final static String ALIGN_MIDDLE = "MIDDLE";

    /** 每列字段的标题 */
    private List<String> titles;

    /** 字段集合 */
    private List<String> values;

    /** 单元格中字段内容的对齐方式 */
    private List<String> aligns;

    /** 行间分隔符 */
    private String lineSeparator;

    /** 表格中字符串的字符集 */
    private String charsetName;

    /** 字符表格图形字符串 */
    private StringBuilder tableShape;

    /**
     * 初始化
     *
     * @param charsetName 字符集, 为空时默认使用jvm字符集
     */
    public CharTable(String charsetName) {
        this();
        if (StringUtils.isNotBlank(charsetName)) {
            this.setCharsetName(charsetName);
        }
    }

    /**
     * 初始化
     */
    public CharTable() {
        this.titles = new ArrayList<String>();
        this.values = new ArrayList<String>();
        this.aligns = new ArrayList<String>();
        this.lineSeparator = FileUtils.lineSeparator;
        this.charsetName = StringUtils.CHARSET;
        this.clear();
    }

    /**
     * 清空标题信息、单元格信息、单元格对齐方式、单元格长度信息
     * 还原表格字符集、是否显示标题栏、字段间分隔符、行间分隔符
     */
    public void clear() {
        this.aligns.clear();
        this.titles.clear();
        this.values.clear();
    }

    /**
     * 设置表格中字符串的字符集
     *
     * @param charsetName 字符集
     */
    public void setCharsetName(String charsetName) {
        this.charsetName = charsetName;
    }

    /**
     * 表格中字符串的字符集
     *
     * @return 字符集
     */
    public String getCharsetName() {
        return charsetName;
    }

    /**
     * 设置字符表格图形中行之间的分隔符
     *
     * @param lineSeparator 分隔符（回车或换行）
     */
    public void setLineSeparator(String lineSeparator) {
        this.lineSeparator = lineSeparator;
    }

    /**
     * 返回字符表格图形中行之间的分隔符
     *
     * @return 分隔符（回车或换行）
     */
    public String getLineSeparator() {
        return this.lineSeparator;
    }

    /**
     * 返回每列的对齐方式
     *
     * @return 对齐方式集合
     */
    public List<String> getAligns() {
        return this.aligns;
    }

    /**
     * 添加列名
     *
     * @param name  列名
     * @param align 对齐方式
     *              {@linkplain #ALIGN_LEFT} 左对齐
     *              {@linkplain #ALIGN_RIGHT} 右对齐
     *              {@linkplain #ALIGN_MIDDLE} 中间对齐
     * @return 当前字符表格对象
     */
    public CharTable addTitle(String name, String align) {
        this.aligns.add(align);
        this.titles.add(name);
        return this;
    }

    /**
     * 添加列名
     *
     * @param name 列名
     * @return 当前字符表格对象
     */
    public CharTable addTitle(String name) {
        this.addTitle(name, ALIGN_LEFT);
        return this;
    }

    /**
     * 返回每列的标题
     *
     * @return 标题集合
     */
    public List<String> getTitles() {
        return titles;
    }

    /**
     * 添加表格单元格的值，并删除字符串二端的空白字符
     *
     * @param obj 添加一个单元格中的内容
     * @return 当前字符表格对象
     */
    public CharTable addCell(Object obj) {
        this.values.add(obj == null ? "" : StringUtils.trimBlank(obj));
        return this;
    }

    /**
     * 返回表格中的数值
     *
     * @return 数值的集合
     */
    public List<String> getCells() {
        return values;
    }

    /**
     * 转为指定样式的图形表格
     *
     * @param style 样式
     * @return 字符图形
     */
    public String toString(Render style) {
        this.tableShape = style.toString(this);
        return this.toString();
    }

    /**
     * 返回最终绘制的字符图形
     *
     * @return 字符串，内容是最终绘制的字符图形
     */
    public String toString() {
        return this.tableShape == null ? "" : this.tableShape.toString();
    }

    /**
     * 将字符图形表格作为输入源，逐行遍历字符图形字符串中的行
     *
     * @return 字符图形表格的遍历器
     */
    public Iterator<String> iterator() {
        if (this.tableShape == null) {
            throw new UnsupportedOperationException();
        }

        List<CharSequence> list = StringUtils.splitLines(this.tableShape, new ArrayList<CharSequence>());
        List<String> strlist = new ArrayList<String>(list.size());
        for (CharSequence cs : list) {
            strlist.add(cs.toString());
        }
        return strlist.iterator();
    }

    /**
     * 字符图形样式枚举
     */
    public enum Style implements Render {
        markdown(new MarkdownSytle()) //
        , db2(new DB2Sytle()) //
        , shell(new ShellSytle()) //
        , simple(new SimpleSytle()) //
        , standard(new StandardSytle()) //
        ;

        private Render rendor;

        Style(Render rendor) {
            this.rendor = rendor;
        }

        public StringBuilder toString(CharTable charTable) {
            return this.rendor.toString(charTable);
        }
    }

    /**
     * 字符图形渲染接口
     */
    public interface Render {

        /**
         * 将表格中的数值转为字符图形
         *
         * @param charTable 表格
         * @return 字符图形
         */
        StringBuilder toString(CharTable charTable);
    }

    public static class DB2Sytle implements Render {
        public StringBuilder toString(CharTable ct) {
            String prefix = "";
            String last = "";
            String delimiter = "  ";

            StringBuilder buf = new StringBuilder();
            List<Integer> maxlengths = RenderUtils.getWidths(ct);
            RenderUtils.addTitle(buf, ct, maxlengths, prefix, delimiter, last);
            RenderUtils.addBorder(buf, ct, maxlengths, prefix, delimiter, last);
            RenderUtils.addValue(buf, ct, maxlengths, prefix, delimiter, last);
            return buf;
        }
    }

    public static class ShellSytle implements Render {
        public StringBuilder toString(CharTable ct) {
            String prefix = "";
            String last = "";
            String delimiter = "  ";

            StringBuilder buf = new StringBuilder();
            List<Integer> maxlengths = RenderUtils.getWidths(ct);
            RenderUtils.addTitle(buf, ct, maxlengths, prefix, delimiter, last);
            RenderUtils.addValue(buf, ct, maxlengths, prefix, delimiter, last);
            return buf;
        }
    }

    public static class SimpleSytle implements Render {
        public StringBuilder toString(CharTable ct) {
            String prefix = "";
            String last = "";
            String delimiter = "  ";

            StringBuilder buf = new StringBuilder();
            List<Integer> maxlengths = RenderUtils.getWidths(ct);
            RenderUtils.addValue(buf, ct, maxlengths, prefix, delimiter, last);
            return buf;
        }
    }

    public static class StandardSytle implements Render {
        public StringBuilder toString(CharTable ct) {
            char c = '-';
            String prefix = "";
            String last = "";
            String delimiter = "  ";

            StringBuilder buf = new StringBuilder();
            List<Integer> maxlengths = RenderUtils.getWidths(ct);
            RenderUtils.addBorder(buf, ct, maxlengths, c, prefix, delimiter, last);
            RenderUtils.addTitle(buf, ct, maxlengths, prefix, delimiter, last);
            RenderUtils.addBorder(buf, ct, maxlengths, prefix, delimiter, last);
            RenderUtils.addValue(buf, ct, maxlengths, prefix, delimiter, last);
            RenderUtils.addBorder(buf, ct, maxlengths, c, prefix, delimiter, last);
            return buf;
        }
    }

    public static class MarkdownSytle implements Render {
        public StringBuilder toString(CharTable ct) {
            List<String> titles = ct.getTitles();
            List<String> values = ct.getCells();

            // 先转义再计算列宽度
            for (int i = 0; i < titles.size(); i++) {
                titles.set(i, this.escape(titles.get(i)));
            }

            // 先转义再计算列宽度
            for (int i = 0; i < values.size(); i++) {
                values.set(i, this.escape(values.get(i)));
            }

            String prefix = "| ";
            String last = " |";
            String delimiter = " | ";

            StringBuilder buf = new StringBuilder();
            List<Integer> widths = RenderUtils.getWidths(ct);  // 计算列宽度
            RenderUtils.addTitle(buf, ct, widths, prefix, delimiter, last); // 添加标题
            RenderUtils.addBorder(buf, ct, widths, prefix, delimiter, last); // 添加标题栏下面的分隔
            RenderUtils.addValue(buf, ct, widths, prefix, delimiter, last);
            return buf;
        }

        private String escape(String value) {
            value = StringUtils.replaceAll(value, "|", "\\|"); // 对竖线做转义
            value = FileUtils.replaceLineSeparator(value, "<br>"); // 对回车换行符进行转义
            return value;
        }
    }

    private static class RenderUtils {

        /**
         * 计算字符串参数的显示宽度，如果字符串包含多行（即：有回车换行符），则返回显示宽度最长的行
         *
         * @param value       字符串
         * @param charsetName 字符串的字符集
         * @return 返回字符串的显示宽度
         */
        public static int width(String value, String charsetName) {
            if (value == null) {
                return 4; // 4 表示 字符串 null 的长度
            } else if (value.indexOf('\n') != -1 || value.indexOf('\r') != -1) {
                int max = 0;
                BufferedReader in = new BufferedReader(new CharArrayReader(value.toCharArray()));
                try {
                    String line;
                    while ((line = in.readLine()) != null) {
                        int len = StringUtils.width(line, charsetName);
                        if (len > max) {
                            max = len;
                        }
                    }
                    return max;
                } catch (Exception e) {
                    throw new RuntimeException(value, e);
                } finally {
                    IO.close(in);
                }
            } else {
                return StringUtils.width(value, charsetName);
            }
        }

        /**
         * 计算每列的显示宽度
         *
         * @param ct 表格
         * @return 每列的宽度
         */
        public static List<Integer> getWidths(CharTable ct) {
            String charsetName = ct.getCharsetName();
            List<String> titles = ct.getTitles();
            List<String> values = ct.getCells();

            ArrayList<Integer> list = new ArrayList<Integer>();
            int size = titles.size();
            for (int i = 0; i < size; i++) {
                String obj = titles.get(i);
                int length = width(obj, charsetName);
                list.add(length);
            }

            for (int i = 0, column = 0; i < values.size(); i++) {
                String value = values.get(i);
                int length = width(value, charsetName); // 长度4表示 null 的长度
                int oldLength = list.get(column);
                if (length > oldLength) {
                    list.set(column, length);
                }

                if (++column >= size) {
                    column = 0;
                }
            }

            return list;
        }

        /**
         * 写入标题栏
         *
         * @param buf    字符缓冲区，最后得到的字符串图形所在的字符串对象
         * @param ct     表格
         * @param widths 每列的宽度
         */
        public static void addTitle(StringBuilder buf, CharTable ct, List<Integer> widths, String prefix, String columnSeparator, String last) {
            String charsetName = ct.getCharsetName();
            List<String> titles = ct.getTitles();
            List<String> aligns = ct.getAligns();
            String lineSeparator = ct.getLineSeparator();

            int column = titles.size();
            buf.append(prefix);
            for (int i = 0; i < column; i++) {
                String value = titles.get(i);
                Integer width = widths.get(i);
                String align = aligns.get(i);

                addValue(buf, value, charsetName, width, align);

                if ((i + 1) < column) {
                    buf.append(columnSeparator);
                }
            }
            buf.append(last);
            buf.append(lineSeparator);
        }

        /**
         * 生成表格单元格数据
         *
         * @param buf    字符缓冲区，最后得到的字符串图形所在的字符串对象
         * @param ct     表格
         * @param widths 每列的宽度
         */
        public static void addValue(StringBuilder buf, CharTable ct, List<Integer> widths, String prefix, String columnSeparator, String last) {
            String charsetName = ct.getCharsetName();
            List<String> titles = ct.getTitles();
            List<String> values = ct.getCells();
            List<String> aligns = ct.getAligns();
            String lineSeparator = ct.getLineSeparator();

            Map<Integer, List<CharSequence>> map = new HashMap<Integer, List<CharSequence>>(); // 列号与跨行数值的映射关系
            int column = titles.size();
            for (int i = 0; i < values.size(); ) {
                map.clear();

                int moreRows = 0; // 数值跨行的行数
                buf.append(prefix);
                for (int j = 0; j < column; j++) {
                    String value = values.get(i++);
                    Integer width = widths.get(j);
                    String align = aligns.get(j);

                    // 如果字符串中跨行显示，则将跨行部分保存到集合中，从下一行开始显示跨行部分
                    if (value != null && (value.indexOf('\n') != -1 || value.indexOf('\r') != -1)) {
                        List<CharSequence> list = StringUtils.splitLines(value, new ArrayList<CharSequence>());
                        value = list.remove(0).toString(); // 移除第一行

                        // 如果数值中有回车换行符，则计算最大行数
                        if (list.size() > moreRows) {
                            moreRows = list.size();
                        }
                        map.put(Integer.valueOf(j), list);
                    }

                    addValue(buf, value, charsetName, width, align);

                    if (j + 1 < column) {
                        buf.append(columnSeparator);
                    }
                }
                buf.append(last);
                buf.append(lineSeparator);

                /**
                 * 如果单元格值中存在回车换行符，则对单元格中的值按行分割，每行数据单独写入一行到表中
                 */
                if (moreRows > 0) {
                    for (int row = 0; row < moreRows; row++) {
                        buf.append(prefix);
                        for (int j = 0; j < column; j++) {
                            Integer length = widths.get(j);
                            String align = aligns.get(j);

                            List<CharSequence> list = map.get(j);
                            String value = (list != null && row < list.size()) ? list.get(row).toString() : "";

                            addValue(buf, value, charsetName, length, align);

                            if (j + 1 < column) {
                                buf.append(columnSeparator);
                            }
                        }
                        buf.append(last);
                        buf.append(lineSeparator);
                    }
                }
            }
        }

        /**
         * 添加数值
         *
         * @param buf         字符缓冲区，最后得到的字符串图形所在的字符串对象
         * @param value       字符串
         * @param charsetName 字符串编码
         * @param width       显示宽度
         * @param align       对齐方式
         */
        public static void addValue(StringBuilder buf, String value, String charsetName, Integer width, String align) {
            if (ALIGN_LEFT.equalsIgnoreCase(align)) {
                buf.append(StringUtils.left(value, width.intValue(), charsetName, ' '));
            } else if (ALIGN_RIGHT.equalsIgnoreCase(align)) {
                buf.append(StringUtils.right(value, width.intValue(), charsetName, ' '));
            } else {
                buf.append(StringUtils.middle(value, width.intValue(), charsetName, ' '));
            }
        }

        /**
         * 画顶部边框
         *
         * @param buf             字符缓冲区，最后得到的字符串图形所在的字符串对象
         * @param ct              表格
         * @param widths          每列的宽度
         * @param prefix          第一列左侧的字符串
         * @param columnSeparator 每列之间的分隔符
         * @param last            每后一列右侧的字符串
         */
        public static void addBorder(StringBuilder buf, CharTable ct, List<Integer> widths, String prefix, String columnSeparator, String last) {
            String lineSeparator = ct.getLineSeparator();
            List<String> titles = ct.getTitles();
            int column = titles.size();

            buf.append(prefix);
            for (int i = 0; i < column; i++) {
                int width = widths.get(i);
                for (int k = 0; k < width; k++) {
                    buf.append('-');
                }

                if ((i + 1) < column) {
                    buf.append(columnSeparator);
                }
            }
            buf.append(last);
            buf.append(lineSeparator);
        }

        /**
         * 画横向边框
         *
         * @param buf             字符缓冲区，最后得到的字符串图形所在的字符串对象
         * @param ct              表格
         * @param widths          每列的宽度
         * @param delimiter       边框字符
         * @param prefix          第一列左侧的字符串
         * @param columnSeparator 每列之间的分隔符
         * @param last            每后一列右侧的字符串
         */
        public static void addBorder(StringBuilder buf, CharTable ct, List<Integer> widths, char delimiter, String prefix, String columnSeparator, String last) {
            String lineSeparator = ct.getLineSeparator();
            List<String> titles = ct.getTitles();

            int column = titles.size() - 1;
            int length = Numbers.sum(widths) + (columnSeparator.length() * column) + prefix.length() + last.length();
            for (int i = 0; i < length; i++) {
                buf.append(delimiter);
            }
            buf.append(lineSeparator);
        }

    }

}



