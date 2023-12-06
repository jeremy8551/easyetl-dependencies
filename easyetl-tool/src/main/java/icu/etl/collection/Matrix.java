package icu.etl.collection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import icu.etl.util.CharTable;
import icu.etl.util.StringUtils;

/**
 * 矩阵型数据结构
 *
 * @param <E>
 * @author jeremy8551@qq.com
 */
public class Matrix<E> implements Cloneable {

    /** 行数与列数 */
    protected int row, col;

    /** 矩阵 */
    protected Object[][] matrix;

    /**
     * 初始化
     *
     * @param row 行数
     * @param col 列数
     */
    public Matrix(int row, int col) {
        if (row <= 0) {
            throw new IllegalArgumentException(String.valueOf(row));
        }
        if (col <= 0) {
            throw new IllegalArgumentException(String.valueOf(col));
        }

        this.row = row;
        this.col = col;
        this.matrix = new Object[row][col];
    }

    /**
     * 返回指定位置上的值
     *
     * @param row 行数（从0开始）
     * @param col 列数（从0开始）
     * @return
     */
    @SuppressWarnings("unchecked")
    protected E elementData(int row, int col) {
        return (E) this.matrix[row][col];
    }

    /**
     * 校验位置是否在矩阵内
     *
     * @param row 行号;从0开始
     * @param col 列号;从0开始
     */
    protected void checkRange(int row, int col) {
        if (row < 0 || row >= this.row) {
            throw new IllegalArgumentException(row + ", " + this.row);
        }
        if (col < 0 || col >= this.col) {
            throw new IllegalArgumentException(col + ", " + this.col);
        }
    }

    /**
     * 扩展矩阵容量
     *
     * @param row 行数
     * @param col 列数
     */
    public void expandCapacity(int row, int col) {
        Object[] firstRow = this.matrix[0];
        if (col > firstRow.length) {
            int newCol = (firstRow.length * 3) / 2 + 1;
            if (newCol < col) {
                newCol = col;
            }

            /**
             * 逐行扩容
             */
            for (int i = 0; i < this.matrix.length; i++) {
                Object[] oldRowElement = this.matrix[i];
                Object[] newRowElement = (Object[]) new Object[newCol];
                System.arraycopy(oldRowElement, 0, newRowElement, 0, this.col);
                this.matrix[i] = newRowElement;
            }
        }
        this.col = col;

        if (row > this.matrix.length) {
            Object oldData[][] = this.matrix;
            int newRow = (this.matrix.length * 3) / 2 + 1;
            if (newRow < row) {
                newRow = row;
            }

            this.matrix = (Object[][]) new Object[newRow][col];
            System.arraycopy(oldData, 0, this.matrix, 0, this.row);
        }
        this.row = row;
    }

    /**
     * 矩阵行数
     *
     * @return
     */
    public int getRow() {
        return row;
    }

    /**
     * 矩阵列数
     *
     * @return
     */
    public int getColumn() {
        return col;
    }

    /**
     * 设置坐标处的数值
     *
     * @param row   行号;从0开始
     * @param col   列号;从0开始
     * @param value 数值
     * @return 原值
     */
    public E set(int row, int col, Object value) {
        this.checkRange(row, col);
        E oldObj = this.elementData(row, col);
        this.matrix[row][col] = value;
        return oldObj;
    }

    /**
     * 设置一行的数值
     *
     * @param row    行号;从0开始
     * @param values 列值集合
     * @return
     */
    @SuppressWarnings("unchecked")
    public ArrayList<E> setRow(int row, E... values) {
        this.checkRange(row, values.length - 1);
        ArrayList<E> list = new ArrayList<E>(values.length);
        for (int i = 0; i < values.length; i++) {
            list.add((E) this.matrix[row][i]);
            this.matrix[row][i] = values[i];
        }
        return list;
    }

    /**
     * 返回坐标处的数值
     *
     * @param row 行号;从0开始
     * @param col 列号;从0开始
     * @return
     */
    public E get(int row, int col) {
        this.checkRange(row, col);
        return this.elementData(row, col);
    }

    /**
     * 截取矩阵
     *
     * @param pointRow1 点1的行号, 从0开始
     * @param pointCol1 点1的列号, 从0开始
     * @param pointRow2 点2的行号, 从0开始
     * @param pointCol2 点2的列号, 从0开始
     * @return 返回矩阵本省引用
     */
    public Matrix<E> get(int pointRow1, int pointCol1, int pointRow2, int pointCol2) {
        this.checkRange(pointRow1, pointCol1);
        this.checkRange(pointRow2, pointCol2);

        int row = Math.abs(pointRow2 - pointRow1) + 1;
        int col = Math.abs(pointCol2 - pointCol1) + 1;

        int r = pointRow1 < pointRow2 ? pointRow1 : pointRow2;
        int c = pointCol1 < pointCol2 ? pointCol1 : pointCol2;

        Matrix<E> obj = new Matrix<E>(row, col);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                Object v = this.get(i + r, j + c);
                obj.set(i, j, v);
            }
        }
        return obj;
    }

    /**
     * 对矩阵中的行进行排序
     *
     * @param ascOrDesc true表示从小到大排序
     * @param positions 排序字段所在列数;从0开始
     * @param compare   排序比较类
     */
    @SuppressWarnings("unchecked")
    public void sortRow(boolean ascOrDesc, int[] positions, Comparator<E> compare) {
        if (positions == null || positions.length == 0) {
            throw new IllegalArgumentException(StringUtils.toString(positions));
        }
        for (int position : positions) {
            this.checkRange(0, position);
        }
        if (compare == null) {
            throw new NullPointerException();
        }

        if (ascOrDesc) {
            int l = this.row - 1;
            for (int i = 0; i < l; i++) {
                int ll = (l - i);
                for (int j = 0; j < ll; j++) {
                    Object[] array1 = this.matrix[j];
                    Object[] array2 = this.matrix[j + 1];
                    for (int z = 0; z < positions.length; z++) {
                        int index = positions[z];
                        int r = compare.compare((E) array1[index], (E) array2[index]);
                        if (r > 0) {
                            this.matrix[j + 1] = array1;
                            this.matrix[j] = array2;
                            continue;
                        }
                    }
                }
            }
        } else {
            int l = this.row - 1;
            for (int i = 0; i < l; i++) {
                int ll = (l - i);
                for (int j = 0; j < ll; j++) {
                    Object[] array1 = this.matrix[j];
                    Object[] array2 = this.matrix[j + 1];
                    for (int z = 0; z < positions.length; z++) {
                        int index = positions[z];
                        int r = compare.compare((E) array1[index], (E) array2[index]);
                        if (r < 0) {
                            this.matrix[j + 1] = array1;
                            this.matrix[j] = array2;
                            continue;
                        }
                    }
                }
            }
        }
    }

    /**
     * 对矩阵中的列进行排序
     *
     * @param ascOrDesc true表示从小到大排序
     * @param positions 排序字段所在行号
     * @param compare   排序比较类
     */
    public void sortCol(boolean ascOrDesc, int[] positions, Comparator<E> compare) {
        if (positions == null || positions.length == 0) {
            throw new IllegalArgumentException(StringUtils.toString(positions));
        }
        for (int position : positions) {
            this.checkRange(position, 0);
        }
        if (compare == null) {
            throw new NullPointerException();
        }

        if (ascOrDesc) {
            Object[] array = new Object[this.row];
            int len = this.col - 1;
            for (int i = 0; i < len; i++) {
                int ll = (len - i);
                for (int j = 0; j < ll; j++) {
                    for (int z = 0; z < positions.length; z++) {
                        int index = positions[z];
                        int r = compare.compare(this.elementData(index, j), this.elementData(index, j + 1));
                        if (r > 0) {
                            for (int b = 0; b < this.row; b++) {
                                array[b] = this.matrix[b][j + 1];
                            }
                            for (int b = 0; b < this.row; b++) {
                                this.matrix[b][j + 1] = this.matrix[b][j];
                            }
                            for (int b = 0; b < this.row; b++) {
                                this.matrix[b][j] = array[b];
                            }
                            continue;
                        }
                    }
                }
            }
        } else {
            Object[] array = new Object[this.row];
            int len = this.col - 1;
            for (int i = 0; i < len; i++) {
                int ll = (len - i);
                for (int j = 0; j < ll; j++) {
                    for (int z = 0; z < positions.length; z++) {
                        int index = positions[z];
                        int r = compare.compare(this.elementData(index, j), this.elementData(index, j + 1));
                        if (r < 0) {
                            for (int b = 0; b < this.row; b++) {
                                array[b] = this.matrix[b][j + 1];
                            }
                            for (int b = 0; b < this.row; b++) {
                                this.matrix[b][j + 1] = this.matrix[b][j];
                            }
                            for (int b = 0; b < this.row; b++) {
                                this.matrix[b][j] = array[b];
                            }
                            continue;
                        }
                    }
                }
            }
        }
    }

    /**
     * 顺时针90度旋转矩阵
     */
    public void rotate() {
        Object[][] array = new Object[this.col][this.row];
        for (int j = 0; j < this.col; j++) {
            for (int i = 0, k = this.row - 1; i < this.row; i++, k--) {
                array[j][k] = this.matrix[i][j];
            }
        }
        this.row = array.length;
        this.col = array[0].length;
        this.matrix = array;
    }

    /**
     * 逆时针90度旋转矩阵
     */
    public void reverseRotate() {
        Object[][] array = new Object[this.col][this.row];
        for (int j = 0, k = this.col - 1; j < this.col; j++, k--) {
            for (int i = 0; i < this.row; i++) {
                array[k][i] = this.matrix[i][j];
            }
        }
        this.row = array.length;
        this.col = array[0].length;
        this.matrix = array;
    }

    /**
     * 如果矩阵中是引用型数据项，副本中对象的引用与原值相同
     */
    public Matrix<E> clone() {
        Matrix<E> obj = new Matrix<E>(this.row, this.col);
        for (int i = 0; i < this.row; i++) {
            System.arraycopy(this.matrix[i], 0, obj.matrix[i], 0, this.col);
        }
        return obj;
    }

    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (obj instanceof Matrix) {
            Matrix<E> other = (Matrix<E>) obj;
            if (this.row != other.row || this.col != other.col) {
                return false;
            }

            for (int i = 0; i < this.row; i++) {
                for (int j = 0; j < this.col; j++) {
                    Object thisObj = this.matrix[i][j];
                    Object othObj = other.matrix[i][j];
                    boolean tb = thisObj == null;
                    boolean ob = othObj == null;
                    if (tb && ob) {
                        continue;
                    }
                    if (tb) {
                        return false;
                    }
                    if (ob) {
                        return false;
                    }
                    if (!thisObj.equals(othObj)) {
                        return false;
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public String toString() {
        return this.toString(null, null, null);
    }

    /**
     * 将矩阵列表转为字符图形表格
     *
     * @param titles      表格标题信息(标题个数要与矩阵列数相等), 为null时默认为L1格式的标题
     * @param aligns      表格中每列信息的排列方式(居左LEFT,居中MIDDLE,局右RIGHT), 为null时默认为居中MIDDLE
     * @param charsetName 表格中字符串的字符集, 为null时取jvm默认字符集作为默认值
     * @return 字符图形表格
     */
    public String toString(List<String> titles, List<String> aligns, String charsetName) {
        if (titles != null && titles.size() != this.col) {
            throw new IllegalArgumentException(titles.size() + " != " + this.col);
        }
        if (aligns != null && aligns.size() != this.col) {
            throw new IllegalArgumentException(aligns.size() + " != " + this.col);
        }

        CharTable table = new CharTable(charsetName);

        // 添加标题栏
        for (int i = 0; i < this.col; i++) {
            String colAlign = (aligns == null) ? CharTable.ALIGN_MIDDLE : StringUtils.defaultString(aligns.get(i), CharTable.ALIGN_MIDDLE);
            String colName = (titles == null) ? ("L" + String.valueOf(i + 1)) : titles.get(i);
            table.addTitle(colName, colAlign);
        }

        // 添加单元格
        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < this.col; j++) {
                table.addCell(this.matrix[i][j]);
            }
        }

        return table.toString(CharTable.Style.standard);
    }

}