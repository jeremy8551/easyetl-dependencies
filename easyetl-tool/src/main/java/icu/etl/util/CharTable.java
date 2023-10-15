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
 * table.{@linkplain #addValue(Object)}; <br>
 * table.{@linkplain #addValue(Object)}; <br>
 * <br>
 * table.{@linkplain #addValue(Object)}; <br>
 * table.{@linkplain #addValue(Object)}; <br>
 * <br>
 * table.{@linkplain #addValue(Object)}; <br>
 * table.{@linkplain #addValue(Object)}; <br>
 * <br>
 * table.{@linkplain #toStandardShape()};
 *
 * @author jeremy8551@qq.com
 * @createtime 2012-04-11
 */
public class CharTable {

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

    /** 是否删除每行最左侧的空白字符 */
    private boolean ltrimBlank;

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
     * @param charsetName
     */
    public void setCharsetName(String charsetName) {
        this.charsetName = charsetName;
    }

    /**
     * 表格中字符串的字符集
     *
     * @return
     */
    public String getCharsetName() {
        return charsetName;
    }

    /**
     * 设置字段间分隔符
     *
     * @param columnSplit
     */
    public void setDelimiter(String columnSplit) {
        this.columnSeparator = columnSplit;
    }

    /**
     * 设置行分隔符
     *
     * @param rowSplit
     */
    public void setLineSeparator(String rowSplit) {
        this.lineSeparator = rowSplit;
    }

    /**
     * 添加列名
     *
     * @param length 数据最大长度
     * @param align  对齐方式
     * @param name   列名
     */
    private void addTitle(int length, String align, String name) {
        this.maxLength.add(length);
        this.aligns.add(align);
        this.titles.add(name);
    }

    /**
     * 添加列名
     *
     * @param align 对齐方式
     * @param name  列名
     */
    public void addTitle(String align, String name) {
        this.addTitle(-1, align, name);
    }

    /**
     * 添加列名
     *
     * @param name 列名
     */
    public void addTitle(String name) {
        this.addTitle(-1, ALIGN_LEFT, name);
    }

    /**
     * 添加表格单元格的值，并删除字符串二端的空白字符
     *
     * @param obj
     */
    public void addValue(Object obj) {
        this.values.add(obj == null ? "" : StringUtils.trimBlank(obj));
    }

    /**
     * 设置字符图形表格中是否显示标题栏
     *
     * @param visible true表示列名显示
     */
    public void setTitleVisible(boolean visible) {
        this.displayTitle = visible;
    }

    /**
     * 判断字符图形表格是否显示标题栏
     *
     * @return
     */
    public boolean isTitleVisible() {
        return displayTitle;
    }

    /**
     * 删除表格左侧的空白字符
     */
    public CharTable removeLeftBlank() {
        this.ltrimBlank = true;
        return this;
    }

    /**
     * 计算每列字段的最大长度
     */
    private void calcColumnLength() {
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
     * 计算字符串长度，如果字符串跨行则取最长字符串的长度
     *
     * @param value
     * @return
     */
    private int length(String value) {
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
     * @param str
     */
    private void drawTopBorder(StringBuilder str) {
        int column = this.titles.size();
        str.append(this.lineSeparator);
        if (!this.ltrimBlank) {
            str.append(this.columnSeparator);
        }

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
     * @param str
     */
    private void drawBorder(StringBuilder str) {
        int column = this.titles.size() - 1;
        str.append(this.lineSeparator);
        if (!this.ltrimBlank) {
            str.append(this.columnSeparator);
        }

        int length = Numbers.sum(this.maxLength) + (this.columnSeparator.length() * column);
        for (int i = 0; i < length; i++) {
            str.append('-');
        }
    }

    /**
     * 生成表格单元格数据
     *
     * @param str
     */
    private void addColumnValue(StringBuilder str) {
        Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
        int column = this.titles.size();
        for (int i = 0; i < this.values.size(); ) {
            map.clear();

            str.append(this.lineSeparator);
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

                if (!this.ltrimBlank) {
                    str.append(this.columnSeparator);
                } else if (j > 0) {
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
     * @param str
     * @param column 总列数
     * @param map    插入数据
     */
    private void insertRows(StringBuilder str, int column, Map<Integer, List<String>> map) {
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
                for (int j = 0; j < column; j++) {
                    String value = "";
                    Integer length = this.maxLength.get(j);
                    String align = this.aligns.get(j);

                    List<String> list = map.get(j);
                    if (list != null && i < rows) {
                        value = list.get(i);
                    }

                    if (!this.ltrimBlank) {
                        str.append(this.columnSeparator);
                    } else if (j > 0) {
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
     * @param str
     */
    private void addColumnName(StringBuilder str) {
        int column = this.titles.size();
        str.append(this.lineSeparator);
        if (!this.ltrimBlank) {
            str.append(this.columnSeparator);
        }

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
        this.ltrimBlank = false;
    }

    /**
     * 有标题栏、有边框的字符图形表格
     *
     * @return
     */
    public String toStandardShape() {
        this.calcColumnLength();
        StringBuilder str = new StringBuilder();
        this.drawBorder(str);
        if (this.displayTitle) {
            this.addColumnName(str);
            this.drawTopBorder(str);
        }
        this.addColumnValue(str);
        this.drawBorder(str);
        return str.toString();
    }

    /**
     * 有标题栏、无边框的字符图形表格
     *
     * @return
     */
    public String toShellShape() {
        this.calcColumnLength();
        StringBuilder str = new StringBuilder();
        if (this.displayTitle) {
            this.addColumnName(str);
        }
        this.addColumnValue(str);
        return str.toString();
    }

    /**
     * 有标题栏、无边框的字符图形表格
     *
     * @return
     */
    public String toDB2Shape() {
        this.calcColumnLength();
        StringBuilder str = new StringBuilder();
        if (this.displayTitle) {
            this.addColumnName(str);
            this.drawTopBorder(str);
        }
        this.addColumnValue(str);
        String s = str.toString();
        if (s.startsWith(this.lineSeparator)) { // 删除最前面的换行符
            s = s.substring(this.lineSeparator.length());
        }
        return s;
    }

    /**
     * 无标题栏、无边框的字符图形表格
     *
     * @return
     */
    public String toSimpleShape() {
        this.calcColumnLength();
        StringBuilder buf = new StringBuilder();
        this.addColumnValue(buf);
        String str = buf.toString();
        if (str.startsWith(this.lineSeparator)) { // 删除最前面的换行符
            str = str.substring(this.lineSeparator.length());
        }
        return str;
    }

    public String toString() {
        return this.toStandardShape();
    }

}
