package icu.etl.util;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 字符图形表格 <br>
 * e.g: <br>
 * {@linkplain CharTable} table = new {@linkplain CharTable}(); <br>
 * table.{@linkplain #addTitle(String)}; <br>
 * table.{@linkplain #addTitle(String)}; <br>
 * <br>
 * table.{@linkplain #addCell(Object)}; <br>
 * table.{@linkplain #addCell(Object)}; <br>
 * <br>
 * table.{@linkplain #addCell(Object)}; <br>
 * table.{@linkplain #addCell(Object)}; <br>
 * <br>
 * table.{@linkplain #addCell(Object)}; <br>
 * table.{@linkplain #addCell(Object)}; <br>
 * <br>
 * table.{@linkplain #toStandardShape()};
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

    /** 每列字段的最大长度，单位字节 */
    private List<Integer> maxLength;

    /** 字段间分隔符 */
    private String columnSeparator;

    /** 行间分隔符 */
    private String lineSeparator;

    /** 表格中字符串的字符集 */
    private String charsetName;

    /** true表示显示列名 */
    private boolean displayTitle;

    /** 字符表格图形字符串 */
    private StringBuilder tableShape;

    /**
     * 初始化
     */
    public CharTable() {
        this.titles = new ArrayList<String>();
        this.values = new ArrayList<String>();
        this.aligns = new ArrayList<String>();
        this.maxLength = new ArrayList<Integer>();
        this.clear();
    }

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
     * 设置字段间分隔符
     *
     * @param columnSeparator 列分隔符
     */
    public void setDelimiter(String columnSeparator) {
        this.columnSeparator = columnSeparator;
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
     * 设置字符图形表格中是否显示标题栏
     *
     * @param visible true表示列名显示
     * @return 当前字符表格对象
     */
    public CharTable setTitleVisible(boolean visible) {
        this.displayTitle = visible;
        return this;
    }

    /**
     * 判断字符图形表格是否显示标题栏
     *
     * @return 返回 true 表示会在字符图形中显示标题信息
     */
    public boolean isTitleVisible() {
        return displayTitle;
    }

    /**
     * 添加列名
     *
     * @param name   列名
     * @param align  列单元格中数据的对齐方式
     *               {@linkplain #ALIGN_LEFT} 左对齐
     *               {@linkplain #ALIGN_RIGHT} 右对齐
     *               {@linkplain #ALIGN_MIDDLE} 中间对齐
     * @param length 在列中所有单元格的最大显示宽度（一个英文字符表示一个显示宽度，一个汉字表示2个显示宽度，也就是占2个英文字符的显示宽度）
     * @return 当前字符表格对象
     */
    private CharTable addTitle(String name, String align, int length) {
        this.maxLength.add(length);
        this.aligns.add(align);
        this.titles.add(name);
        return this;
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
        this.addTitle(name, align, -1);
        return this;
    }

    /**
     * 添加列名
     *
     * @param name 列名
     * @return 当前字符表格对象
     */
    public CharTable addTitle(String name) {
        this.addTitle(name, ALIGN_LEFT, -1);
        return this;
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
     * 计算每列字段的最大长度
     */
    protected void calcColumnLength() {
        int col = this.titles.size();
        for (int i = 0; i < col; i++) {
            String obj = this.titles.get(i);
            int len = obj == null ? 0 : this.length(obj);
            this.maxLength.set(i, len);
        }

        int column = this.values.size();
        for (int i = 0, c = 0; i < column; i++) {
            String obj = this.values.get(i);
            int len = obj == null ? 4 : this.length(obj);
            // int len = this.tool.getByteSize(this.columnValues.get(i));
            int ln = this.maxLength.get(c);
            if (len > ln) {
                this.maxLength.set(c, len);
            }
            c++;
            if (c >= col) {
                c = 0;
            }
        }
    }

    /**
     * 计算字符串参数的显示宽度，如果字符串包含多行（即：有回车换行符），则返回显示宽度最长的行
     *
     * @param value 字符串
     * @return 返回字符串的显示宽度
     */
    protected int length(String value) {
        if (value.indexOf('\n') != -1 || value.indexOf('\r') != -1) {
            int max = 0;
            BufferedReader in = new BufferedReader(new CharArrayReader(value.toCharArray()));
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    int len = StringUtils.width(line, this.charsetName);
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
            return StringUtils.width(value, this.charsetName);
        }
    }

    /**
     * 画顶部边框
     *
     * @param str 字符缓冲区，最后得到的字符串图形所在的字符串对象
     */
    private void drawTopBorder(StringBuilder str) {
        int column = this.titles.size();
        str.append(this.lineSeparator);
        str.append(this.columnSeparator);

        for (int j = 0; j < column; j++) {
            int lenth = this.maxLength.get(j);
            for (int k = 0; k < lenth; k++) {
                str.append('-');
            }

            if ((j + 1) < column) {
                str.append(this.columnSeparator);
            }
        }
    }

    /**
     * 画横向边框
     *
     * @param str 字符缓冲区，最后得到的字符串图形所在的字符串对象
     */
    private void drawBorder(StringBuilder str) {
        int column = this.titles.size() - 1;
        str.append(this.lineSeparator);
        str.append(this.columnSeparator);

        int length = Numbers.sum(this.maxLength) + (this.columnSeparator.length() * column);
        for (int i = 0; i < length; i++) {
            str.append('-');
        }
    }

    /**
     * 生成表格单元格数据
     *
     * @param str 字符缓冲区，最后得到的字符串图形所在的字符串对象
     */
    private void addColumnValue(StringBuilder str) {
        Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
        int column = this.titles.size();
        for (int i = 0; i < this.values.size(); ) {
            map.clear();

            str.append(this.lineSeparator);
            str.append(this.columnSeparator);
            for (int j = 0; j < column; j++) {
                String value = this.values.get(i++);
                Integer length = this.maxLength.get(j);
                String align = this.aligns.get(j);

                if (value != null && (value.indexOf('\n') != -1 || value.indexOf('\r') != -1)) {
                    BufferedReader in = new BufferedReader(new CharArrayReader(value.toCharArray()));
                    try {
                        String line;
                        if ((line = in.readLine()) != null) {
                            value = line;
                        }

                        List<String> list = new ArrayList<String>();
                        while ((line = in.readLine()) != null) {
                            list.add(line);
                        }
                        map.put(j, list);
                    } catch (IOException e) {
                        throw new RuntimeException(value, e);
                    } finally {
                        IO.close(in);
                    }
                }

                if (j > 0) {
                    str.append(this.columnSeparator);
                }

                if (ALIGN_LEFT.equalsIgnoreCase(align)) {
                    if (j + 1 == column) {
                        str.append(value);
                    } else {
                        str.append(StringUtils.left(value, length, this.charsetName, ' '));
                    }
                } else if (ALIGN_RIGHT.equalsIgnoreCase(align)) {
                    str.append(StringUtils.right(value, length, this.charsetName, ' '));
                } else {
                    str.append(StringUtils.middle(value, length, this.charsetName, ' '));
                }
            }

            this.insertRows(str, column, map);
        }
    }

    /**
     * 对于跨行的列信息，对列信息按行分割，每行数据单独写入一行到表中
     *
     * @param str    字符缓冲区，最后得到的字符串图形所在的字符串对象
     * @param column 总列数
     * @param map    插入数据
     */
    protected void insertRows(StringBuilder str, int column, Map<Integer, List<String>> map) {
        if (map.size() > 0) {
            int rows = 0;
            for (Iterator<List<String>> it = map.values().iterator(); it.hasNext(); ) {
                List<String> list = it.next();
                if (list.size() > rows) {
                    rows = list.size();
                }
            }

            for (int i = 0; i < rows; i++) {
                str.append(this.lineSeparator);
                str.append(this.columnSeparator);
                for (int j = 0; j < column; j++) {
                    String value = "";
                    Integer length = this.maxLength.get(j);
                    String align = this.aligns.get(j);

                    List<String> list = map.get(j);
                    if (list != null && i < rows) {
                        value = list.get(i);
                    }

                    if (j > 0) {
                        str.append(this.columnSeparator);
                    }

                    if (ALIGN_LEFT.equalsIgnoreCase(align)) {
                        if (j + 1 == column) {
                            str.append(value);
                        } else {
                            str.append(StringUtils.left(value, length, this.charsetName, ' '));
                        }
                    } else if (ALIGN_RIGHT.equalsIgnoreCase(align)) {
                        str.append(StringUtils.right(value, length, this.charsetName, ' '));
                    } else {
                        str.append(StringUtils.middle(value, length, this.charsetName, ' '));
                    }
                }
            }
        }
    }

    /**
     * 写入标题栏
     *
     * @param str 字符缓冲区，最后得到的字符串图形所在的字符串对象
     */
    protected void addColumnName(StringBuilder str) {
        int column = this.titles.size();
        str.append(this.lineSeparator);
        str.append(this.columnSeparator);

        for (int i = 0; i < column; i++) {
            String name = this.titles.get(i);
            Integer lenth = this.maxLength.get(i);
            String align = this.aligns.get(i);

            if (ALIGN_LEFT.equalsIgnoreCase(align)) {
                str.append(StringUtils.left(name, lenth, this.charsetName, ' '));
            } else if (ALIGN_RIGHT.equalsIgnoreCase(align)) {
                str.append(StringUtils.right(name, lenth, this.charsetName, ' '));
            } else {
                str.append(StringUtils.middle(name, lenth, this.charsetName, ' '));
            }

            if ((i + 1) < column) {
                str.append(this.columnSeparator);
            }
        }
    }

    /**
     * 清空标题信息、单元格信息、单元格对齐方式、单元格长度信息 <br>
     * 还原表格字符集、是否显示标题栏、字段间分隔符、行间分隔符
     */
    public void clear() {
        this.aligns.clear();
        this.maxLength.clear();
        this.titles.clear();
        this.values.clear();
        this.columnSeparator = "  ";
        this.lineSeparator = FileUtils.lineSeparator;
        this.charsetName = StringUtils.CHARSET;
        this.displayTitle = true;
    }

    /**
     * 绘制有标题栏、有边框的字符图形表格
     *
     * @return 当前字符表格对象
     */
    public CharTable toStandardShape() {
        this.calcColumnLength();
        StringBuilder buf = new StringBuilder();
        this.drawBorder(buf);
        if (this.displayTitle) {
            this.addColumnName(buf);
            this.drawTopBorder(buf);
        }
        this.addColumnValue(buf);
        this.drawBorder(buf);
        this.tableShape = buf;
        return this;
    }

    /**
     * 绘制有标题栏、无边框的字符图形表格
     *
     * @return 当前字符表格对象
     */
    public CharTable toShellShape() {
        this.calcColumnLength();
        StringBuilder buf = new StringBuilder();
        if (this.displayTitle) {
            this.addColumnName(buf);
        }
        this.addColumnValue(buf);
        this.tableShape = buf;
        return this;
    }

    /**
     * 绘制有标题栏、无边框的字符图形表格
     *
     * @return 当前字符表格对象
     */
    public CharTable toDB2Shape() {
        this.calcColumnLength();
        StringBuilder buf = new StringBuilder();
        if (this.displayTitle) {
            this.addColumnName(buf);
            this.drawTopBorder(buf);
        }
        this.addColumnValue(buf);
        if (StringUtils.startsWith(buf, this.lineSeparator, 0, false, false)) { // 删除最前面的换行符
            buf.delete(0, this.lineSeparator.length());
        }
        this.tableShape = buf;
        return this;
    }

    /**
     * 绘制无标题栏、无边框的字符图形表格
     *
     * @return 当前字符表格对象
     */
    public CharTable toSimpleShape() {
        this.calcColumnLength();
        StringBuilder buf = new StringBuilder();
        this.addColumnValue(buf);
        if (StringUtils.startsWith(buf, this.lineSeparator, 0, false, false)) { // 删除最前面的换行符
            buf.delete(0, this.lineSeparator.length());
        }
        this.tableShape = buf;
        return this;
    }

    /**
     * 删除表格左侧的空白字符
     *
     * @return 当前字符表格对象
     */
    public CharTable ltrim() {
        if (this.tableShape == null) {
            throw new UnsupportedOperationException();
        }

        List<CharSequence> list = StringUtils.splitLines(this.tableShape, new ArrayList<CharSequence>());
        int prefixLength = this.columnSeparator.length();
        StringBuilder buf = new StringBuilder(this.tableShape.length());
        for (CharSequence cs : list) {
            if (StringUtils.startsWith(cs, this.columnSeparator, 0, false, false)) {
                buf.append(cs.subSequence(prefixLength, cs.length()));
                buf.append(this.lineSeparator);
            }
        }
        this.tableShape = buf;
        return this;
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

}
