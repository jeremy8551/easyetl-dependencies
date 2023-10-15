package icu.etl.collection;

import java.io.IOException;
import java.io.Reader;
import java.text.Format;

import icu.etl.util.StringUtils;

/**
 * 字符缓冲
 *
 * @author jeremy8551@qq.com
 * @createtime 2012-04-08
 */
public class CharBuffer implements Appendable, CharSequence {

    /** 数据值 */
    protected char[] value;

    /** 当前数据长度 */
    protected int count;

    /** 增长容量 */
    protected int incrCapacity;

    /** 初始容量 */
    protected int initCapacity;

    /** 类型转换器 */
    protected Format converter;

    /**
     * 初始化
     *
     * @param size 初始容量, 单位字符
     * @param incr 增长容量, 单位字符
     */
    public CharBuffer(int size, int incr) {
        if (size < 0) {
            throw new IllegalArgumentException(String.valueOf(size));
        }
        if (incr <= 0) {
            throw new IllegalArgumentException(String.valueOf(incr));
        }

        this.initCapacity = size;
        this.incrCapacity = incr;
        this.restore();
    }

    /**
     * 设置对象格式化接口
     *
     * @param converter 转换接口
     */
    public void setConverter(Format converter) {
        this.converter = converter;
    }

    /**
     * 恢复初始状态和容量
     */
    public void restore() {
        if (this.value == null || this.value.length != this.initCapacity) {
            this.value = new char[this.initCapacity];
        }
        this.count = 0;
    }

    /**
     * 追加对象, 使用对象的 toString() 方法将对象转为的字符串，并添加到缓冲区
     *
     * @param obj 对象
     * @return
     */
    public CharBuffer append(Object obj) {
        if (obj == null) {
            return this;
        } else if (obj instanceof String) {
            return this.append((String) obj);
        } else if (obj instanceof StringBuilder) {
            return this.append((StringBuilder) obj);
        } else if (obj instanceof StringBuffer) {
            return this.append((StringBuffer) obj);
        } else if (obj instanceof CharBuffer) {
            return this.append((CharBuffer) obj);
        } else {
            String str = (this.converter == null) ? obj.toString() : this.converter.format(obj);
            int length = str.length();
            if (length != 0) {
                this.expandCapacity(length);
                str.getChars(0, length, this.value, this.count);
                this.count += length;
            }
            return this;
        }
    }

    /**
     * 追加异常信息
     *
     * @param e 异常信息
     * @return
     */
    public CharBuffer append(Throwable e) {
        if (e != null) {
            this.append(StringUtils.toString(e));
        }
        return this;
    }

    /**
     * 追加字符串
     *
     * @param str 字符串
     * @return
     */
    public CharBuffer append(String str) {
        if (str == null) {
            return this;
        }

        int length = str.length();
        if (length == 0) {
            return this;
        }

        this.expandCapacity(length);
        str.getChars(0, length, this.value, this.count);
        this.count += length;
        return this;
    }

    /**
     * 追加字符串
     *
     * @param str    字符串
     * @param offset 字符串起始位置, 从0开始
     * @param length 追加长度, 从0开始
     * @return
     */
    public CharBuffer append(String str, int offset, int length) {
        if (offset < 0) {
            throw new IllegalArgumentException(String.valueOf(offset));
        } else if (length < 0) {
            throw new IllegalArgumentException(String.valueOf(length));
        } else if (str == null) {
            return this;
        } else {
            int end = offset + length;
            int size = str.length();
            if (length == 0 || offset >= size) {
                return this;
            } else if (end > size) {
                length = size - offset;
                end = size;
            }

            this.expandCapacity(length);
            str.getChars(offset, end, this.value, this.count);
            this.count += length;
            return this;
        }
    }

    /**
     * 追加字符串
     *
     * @param buf 字符串
     * @return
     */
    public CharBuffer append(CharBuffer buf) {
        if (buf == null) {
            return this;
        }

        int length = buf.length();
        if (length == 0) {
            return this;
        }

        this.expandCapacity(length);
        System.arraycopy(buf.value, 0, this.value, this.count, length);
        this.count += length;
        return this;
    }

    /**
     * 追加字符数组
     *
     * @param array  字符数组
     * @param offset 起始位置
     * @param length 长度
     * @return
     */
    public CharBuffer append(char[] array, int offset, int length) {
        if (offset < 0) {
            throw new IllegalArgumentException(String.valueOf(offset));
        } else if (length < 0) {
            throw new IllegalArgumentException(String.valueOf(offset));
        } else if (array == null || array.length == 0 || length == 0) {
            return this;
        } else if (array.length < (offset + length)) {
            throw new IllegalArgumentException(array.length + " < " + offset + " + " + length);
        } else {
            this.expandCapacity(length);
            System.arraycopy(array, offset, this.value, this.count, length);
            this.count += length;
            return this;
        }
    }

    public CharBuffer append(char c) {
        this.expandCapacity(1);
        this.value[this.count++] = c;
        return this;
    }

    /**
     * 追加 StringBuffer 对象
     *
     * @param buf StringBuffer对象
     * @return
     */
    public CharBuffer append(StringBuffer buf) {
        return this.append(buf, 0, buf.length());
    }

    /**
     * 追加 StringBuffer 对象
     *
     * @param buf    StringBuffer对象
     * @param offset 起始位置
     * @param length 长度
     * @return
     */
    public CharBuffer append(StringBuffer buf, int offset, int length) {
        if (offset < 0) {
            throw new IllegalArgumentException(String.valueOf(offset));
        } else if (length < 0) {
            throw new IllegalArgumentException(String.valueOf(offset));
        } else if (buf == null) {
            return this;
        } else if (buf.length() < (offset + length)) {
            throw new IllegalArgumentException(buf.length() + " < " + offset + " + " + length);
        } else {
            this.expandCapacity(length);
            buf.getChars(offset, length - offset, this.value, this.count);
            this.count += length;
            return this;
        }
    }

    /**
     * 从 Reader 对象中读取字符流
     *
     * @param in Read 读取类
     * @return
     * @throws IOException
     */
    public CharBuffer append(Reader in) throws IOException {
        if (in != null) {
            char[] array = new char[128];
            for (int len = 0; (len = in.read(array)) != -1; ) {
                this.append(array, 0, len);
            }
        }
        return this;
    }

    /**
     * 从 Reader 对象中读取字符流
     *
     * @param in     Read 读取类
     * @param buf    缓冲区长度
     * @param length 长度
     * @return
     * @throws IOException
     */
    public CharBuffer append(Reader in, char[] buf, int length) throws IOException {
        if (in != null) {
            int len = in.read(buf, 0, length);
            if (len > 0) {
                this.append(buf, 0, len);
            }
        }
        return this;
    }

    /**
     * 追加 StringBuilder 对象
     *
     * @param buf StringBuilder对象
     * @return
     */
    public CharBuffer append(StringBuilder buf) {
        if (buf != null) {
            this.append(buf, 0, buf.length());
        }
        return this;
    }

    /**
     * 追加 StringBuilder 对象
     *
     * @param buf    StringBuilder对象
     * @param offset 起始位置
     * @param length 长度
     * @return
     */
    public CharBuffer append(StringBuilder buf, int offset, int length) {
        if (buf == null) {
            throw new IllegalArgumentException();
        } else if (offset < 0 || offset > buf.length()) {
            throw new IllegalArgumentException(String.valueOf(offset));
        } else if (length < 0 || buf.length() < (offset + length)) {
            throw new IllegalArgumentException(buf.length() + " " + offset + " " + length);
        } else {
            this.expandCapacity(length);
            buf.getChars(offset, length - offset, this.value, this.count);
            this.count += length;
            return this;
        }
    }

    public CharBuffer append(CharSequence cs) {
        if (cs != null) {
            this.append(cs.toString());
        }
        return this;
    }

    public CharBuffer append(CharSequence cs, int start, int end) {
        if (cs != null) {
            this.append(cs.subSequence(start, end).toString());
        }
        return this;
    }

    /**
     * 扩充value数组的容量
     *
     * @param length 扩容大小
     */
    public int expandCapacity(int length) {
        int valueLength = this.value.length; // 当前value数组可用空间大小
        int newCount = this.count + length; // 需要的空间大小

        if (newCount > valueLength) {
            int newValueLength = valueLength + this.incrCapacity; // 扩充value后的可用容量
            if (newCount > newValueLength) {
                newValueLength = newCount;
            }

            char[] array = new char[newValueLength];
            System.arraycopy(this.value, 0, array, 0, this.count);
            this.value = array;
        }
        return this.value.length;
    }

    public char charAt(int index) {
        if (index < 0 || index >= this.count) {
            throw new IllegalArgumentException(String.valueOf(index));
        } else {
            return this.value[index];
        }
    }

    /**
     * 设置字符
     *
     * @param index 位置,从0开始
     * @param c     字符
     * @return
     */
    public CharBuffer set(int index, char c) {
        if (index < 0 || index >= this.count) {
            throw new IllegalArgumentException(index + ", " + c);
        } else {
            this.value[index] = c;
            return this;
        }
    }

    /**
     * 字符数组是否为空
     *
     * @return
     */
    public boolean isEmpty() {
        return this.count == 0;
    }

    /**
     * 清空缓冲区
     */
    public void clear() {
        this.count = 0;
    }

    public int length() {
        return this.count;
    }

    /**
     * 返回缓冲区中的字符数组（不是副本，操作数据需要谨慎）
     *
     * @return
     */
    public char[] value() {
        return this.value;
    }

    /**
     * 截取缓冲区中的字符串
     *
     * @param begin 截取起始位置
     * @param end   截取终止位置（截取字符串不包含该位置上的字符）
     * @return 截取的字符串
     */
    public String substring(int begin, int end) {
        if (begin < 0 || begin >= this.count || begin > end) {
            throw new IllegalArgumentException(begin + " " + end + " " + this.count);
        } else {
            return new String(this.value, begin, end - begin);
        }
    }

    public CharSequence subSequence(int start, int end) {
        return this.substring(start, end);
    }

    public String toString() {
        return new String(this.value, 0, this.count);
    }

}