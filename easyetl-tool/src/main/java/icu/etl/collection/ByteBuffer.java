package icu.etl.collection;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Formatter;
import java.util.Locale;

import icu.etl.util.CharsetName;
import icu.etl.util.StringUtils;

/**
 * 字节数组
 *
 * @author jeremy8551@qq.com
 * @createtime 2011-10-07
 */
public class ByteBuffer implements Appendable, CharsetName {

    /** 字符串的默认字符集名称 */
    private String charsetName;

    /** 字节数组 */
    private byte[] value;

    /** 字节数组长度 */
    private int count;

    /** 增长容量 */
    private int incrCapacity;

    /** printf 风格的格式字符串的解释程序 */
    protected Formatter formatter;

    /**
     * 初始化缓冲区大小，初始大小默认为 256，自动增加的空间大小为128
     */
    public ByteBuffer() {
        this.restore(256);
        this.setIncrCapacity(128);
        this.setCharsetName(StringUtils.CHARSET);
    }

    /**
     * 初始化缓冲区，自动增加的空间大小为128
     *
     * @param size 初始大小
     */
    public ByteBuffer(int size) {
        this.restore(size);
        this.setIncrCapacity(128);
        this.setCharsetName(StringUtils.CHARSET);
    }

    /**
     * 初始化缓冲区
     *
     * @param size 缓冲区的初始长度
     * @param incr 缓冲区容量每次增长长度
     */
    public ByteBuffer(int size, int incr) {
        this.restore(size);
        this.setIncrCapacity(incr);
        this.setCharsetName(StringUtils.CHARSET);
    }

    /**
     * 初始化缓冲区
     *
     * @param size        缓冲区的初始长度
     * @param charsetName 字符集
     */
    public ByteBuffer(int size, String charsetName) {
        this.restore(size);
        this.setIncrCapacity(128);
        this.setCharsetName(charsetName);
    }

    /**
     * 初始化缓冲区
     *
     * @param size        缓冲区的初始长度
     * @param incr        缓冲区容量每次增长长度
     * @param charsetName 字符集
     */
    public ByteBuffer(int size, int incr, String charsetName) {
        this.restore(size);
        this.setIncrCapacity(incr);
        this.setCharsetName(charsetName);
    }

    /**
     * 返回缓冲区默认增加的大小
     *
     * @return 增量大小
     */
    public int getIncrCapacity() {
        return this.incrCapacity;
    }

    /**
     * 设置缓冲区默认增加的大小
     *
     * @param size 缓冲区默认增加的大小
     */
    public void setIncrCapacity(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException(String.valueOf(size));
        }

        this.incrCapacity = size;
    }

    /**
     * 清空所有字节并调整字节数组大小
     *
     * @param size 字节数组大小
     */
    public void restore(int size) {
        if (size < 0) {
            throw new IllegalArgumentException(String.valueOf(size));
        }

        this.value = new byte[size];
        this.count = 0;
    }

    /**
     * 返回缓冲区中的字节数组副本
     *
     * @return 字节数组
     */
    public byte[] value() {
        byte[] array = new byte[this.count];
        System.arraycopy(this.value, 0, array, 0, this.count);
        return array;
    }

    /**
     * 返回缓冲区中的字节数组（不是副本，操作数据需要谨慎）
     *
     * @return 字节数组
     */
    protected byte[] getBytes() {
        return this.value;
    }

    /**
     * 扩充字节缓冲区大小
     *
     * @param length 扩充的容量大小
     */
    protected final void expandValueArray(int length) {
        int valueLength = this.value.length; // 当前value数组可用空间大小
        int newCount = this.count + length; // 需要的空间大小
        if (newCount > valueLength) {
            int newValueLength = valueLength + this.incrCapacity; // 扩充value后的可用容量
            if (newCount > newValueLength) {
                newValueLength = newCount;
            }

            byte[] newValue = new byte[newValueLength];
            System.arraycopy(this.value, 0, newValue, 0, this.count);
            this.value = newValue;
        }
    }

    /**
     * 内部使用的函数，限制少，以提高效率
     *
     * @param array  字节数组
     * @param offset 数组起始位置
     * @param length 长度
     * @return 字节缓冲区
     */
    protected final ByteBuffer addByteArrays(byte[] array, int offset, int length) {
        if (length < 0) {
            throw new IllegalArgumentException(String.valueOf(length));
        }
        if (length == 0) {
            return this;
        }

        this.expandValueArray(length);
        System.arraycopy(array, offset, this.value, this.count, length);
        this.count += length;
        return this;
    }

    /**
     * 设置字节数组中指定位置上的值
     *
     * @param index 字节数组的位置
     * @param value 字节
     */
    public void set(int index, byte value) {
        if (index < 0 || index >= this.count) {
            throw new IllegalArgumentException(index + ", " + value);
        }

        this.value[index] = value;
    }

    /**
     * 返回指定位置上的字节
     *
     * @param index 字节数组中的位置，从0开始
     * @return 字节
     */
    public byte byteAt(int index) {
        if (index < 0 || index >= this.count) {
            throw new IllegalArgumentException(String.valueOf(index));
        }

        return this.value[index];
    }

    /**
     * 插入字节数组
     *
     * @param index  插入点（从0开始）
     * @param array  字节数组
     * @param offset 字节数组起始位置（从0开始）
     * @param length 插入长度
     * @return 字节缓冲区
     */
    public ByteBuffer insert(int index, byte[] array, int offset, int length) {
        if (array == null || length == 0) {
            return this;
        }
        if ((index < 0) || (index > this.count)) {
            throw new IllegalArgumentException(String.valueOf(index));
        }
        if ((offset < 0) || (length < 0) || (offset > array.length - length)) {
            throw new IllegalArgumentException(index + ", " + StringUtils.toString(array, " ") + ", " + offset + ", " + length);
        }

        int newCount = this.count + length;
        if (newCount > value.length) {
            expandValueArray(length);
        }

        System.arraycopy(this.value, index, this.value, index + length, this.count - index);
        System.arraycopy(array, offset, this.value, index, length);
        this.count = newCount;
        return this;
    }

    /**
     * 追加字符串
     *
     * @param str 字符串
     * @return 字节缓冲区
     * @throws IOException 访问输入流发生错误
     */
    public ByteBuffer append(String str) throws IOException {
        return str == null ? this : this.append(str, this.charsetName);
    }

    /**
     * 追加字符串
     *
     * @param str         字符串
     * @param charsetName 字符串的字符集
     * @return 字节缓冲区
     * @throws IOException 访问输入流发生错误
     */
    public ByteBuffer append(String str, String charsetName) throws IOException {
        if (str == null || str.length() == 0) {
            return this;
        }
        if (StringUtils.isBlank(charsetName)) {
            throw new IllegalArgumentException(charsetName);
        }

        byte[] array = str.getBytes(charsetName);
        return addByteArrays(array, 0, array.length);
    }

    /**
     * 追加字节缓冲区
     *
     * @param bytes 字节数组
     * @return 字节缓冲区
     */
    public ByteBuffer append(ByteBuffer bytes) {
        if (bytes == null || bytes.length() == 0) {
            return this;
        }
        if (bytes == this) {
            throw new IllegalArgumentException();
        }

        return this.addByteArrays(bytes.value, 0, bytes.count);
    }

    /**
     * 追加字节数组
     *
     * @param array 字节数组
     * @return 字节缓冲区
     */
    public ByteBuffer append(byte[] array) {
        return array == null || array.length == 0 ? this : this.addByteArrays(array, 0, array.length);
    }

    /**
     * 追加字节数组
     *
     * @param array  字节数组
     * @param offset 数组起始位置
     * @param length 长度
     * @return 字节缓冲区
     */
    public ByteBuffer append(byte[] array, int offset, int length) {
        if (array == null) {
            return this;
        }
        if (length < 0 || offset < 0 || (offset + length) > array.length) {
            throw new IllegalArgumentException(StringUtils.toString(array, " ") + ", " + offset + ", " + length);
        }

        return this.addByteArrays(array, offset, length);
    }

    public ByteBuffer append(char c) throws IOException {
        return this.append(c, this.charsetName);
    }

    /**
     * 追加字符
     *
     * @param c           字符
     * @param charsetName 字符的编码集
     * @return 字节缓冲区
     * @throws IOException 访问输入流发生错误
     */
    public ByteBuffer append(char c, String charsetName) throws IOException {
        String str = String.valueOf(c);
        byte[] arrays = str.getBytes(charsetName);
        return this.addByteArrays(arrays, 0, arrays.length);
    }

    /**
     * 追加字节
     *
     * @param b
     * @return 字节缓冲区
     */
    public ByteBuffer append(byte b) {
        this.expandValueArray(4);
        this.value[this.count++] = b;
        return this;
    }

    /**
     * 从 <code> InputStream </code>中读取 <code> length </code>个字节
     *
     * @param in     输入流
     * @param length 读取字节个数
     * @return 字节缓冲区
     * @throws IOException 访问输入流发生错误
     */
    public ByteBuffer append(InputStream in, int length) throws IOException {
        if (length < 0) {
            throw new IllegalArgumentException(String.valueOf(length));
        }

        if (in != null) {
            byte[] array = new byte[length];
            int size = in.read(array, 0, array.length);
            if (size != -1) {
                this.addByteArrays(array, 0, size);
            }
        }
        return this;
    }

    /**
     * 从 <code> InputStream </code> 输入流中读取字节，并将字节数组保存到当前对象中
     *
     * @param in 输入流
     * @return 字节缓冲区
     * @throws IOException 访问输入流发生错误
     */
    public ByteBuffer append(InputStream in) throws IOException {
        if (in != null) {
            byte[] buf = new byte[256];
            for (int s = in.read(buf, 0, buf.length); s != -1; s = in.read(buf, 0, buf.length)) {
                this.addByteArrays(buf, 0, s);
            }
        }
        return this;
    }

    /**
     * 从 <code> InputStream </code>中读取字节流
     *
     * @param in    输入流
     * @param array 字节缓冲区
     * @return 字节缓冲区
     * @throws IOException 访问输入流发生错误
     */
    public ByteBuffer append(InputStream in, byte[] array) throws IOException {
        if (in != null) {
            if (array == null) {
                array = new byte[1024];
            }

            for (int size = in.read(array, 0, array.length); size != -1; size = in.read(array, 0, array.length)) {
                this.addByteArrays(array, 0, size);
            }
        }
        return this;
    }

    public ByteBuffer append(CharSequence cs) throws IOException {
        if (cs != null) {
            this.append(cs.toString());
        }
        return this;
    }

    public ByteBuffer append(CharSequence cs, int start, int end) throws IOException {
        if (cs != null) {
            this.append(cs.subSequence(start, end).toString());
        }
        return this;
    }

    /**
     * 使用指定的语言环境、格式字符串和参数，将一个格式化字符串追加到此 Chars 右端
     *
     * @param format 字符串格式
     * @param args   参数数组
     * @return 字节缓冲区
     */
    public ByteBuffer append(String format, Object... args) {
        if (this.formatter == null || this.formatter.locale() != Locale.getDefault()) {
            this.formatter = new Formatter((Appendable) this);
        }

        this.formatter.format(Locale.getDefault(), format, args);
        return this;
    }

    /**
     * 使用指定的语言环境、格式字符串和参数，将一个格式化字符串追加到此 Chars 右端
     *
     * @param locale 语音环境
     * @param format 字符串格式
     * @param args   参数数组
     * @return 字节缓冲区
     */
    public ByteBuffer append(Locale locale, String format, Object... args) {
        if ((this.formatter == null) || (this.formatter.locale() != locale)) {
            this.formatter = new Formatter(this, locale);
        }

        this.formatter.format(locale, format, args);
        return this;
    }

    /**
     * 清空所有字节
     */
    public void clear() {
        this.count = 0;
    }

    /**
     * 销毁数据
     */
    public void destory() {
        this.restore(0);
    }

    /**
     * 调整字节数组长度为 count
     */
    public void resize() {
        if (this.count > 0 && this.value.length > this.count) {
            this.value = this.value();
        }
    }

    /**
     * 字节数组大小
     *
     * @return 字节数组大小
     */
    public int length() {
        return this.count;
    }

    /**
     * true表示字节数组length为0
     *
     * @return 返回true表示缓冲区容量为空
     */
    public boolean isEmpty() {
        return this.count <= 0;
    }

    /**
     * 将字节数组参数array转为16进制字符串
     *
     * @return 16进制字符串（大写）
     */
    public String toHexString() {
        StringBuilder buf = new StringBuilder(this.count * 2);
        for (int i = 0; i < this.count; i++) {
            String str = Integer.toHexString(this.value[i] & 0xFF);
            if (str.length() < 2) {
                buf.append(0);
            }
            buf.append(str);
        }
        return buf.toString().toUpperCase();
    }

    /**
     * 把字节数组转成字符串
     *
     * @param charsetName 字符集
     * @return 字符串
     */
    public String toString(String charsetName) {
        try {
            return new String(this.value, 0, this.count, charsetName);
        } catch (IOException e) {
            throw new RuntimeException(this.charsetName, e);
        }
    }

    public String toString() {
        return toString(this.charsetName);
    }

    public String getCharsetName() {
        return charsetName;
    }

    public void setCharsetName(String charsetName) {
        this.charsetName = charsetName;
    }

    /**
     * 截取字节数组，返回的新字节数组是全新的对象
     *
     * @param begin 截取起始位置，从0开始
     * @param end   截取终止位置(不包含在内)
     * @return 字节缓冲区
     */
    public ByteBuffer subbytes(int begin, int end) {
        if (begin < 0) {
            throw new StringIndexOutOfBoundsException(begin);
        }
        if (end > this.count) {
            throw new StringIndexOutOfBoundsException(end);
        }
        if (begin > end) {
            throw new StringIndexOutOfBoundsException(end - begin);
        }

        int length = end - begin;
        ByteBuffer buf = new ByteBuffer(length, 5, this.charsetName);
        buf.addByteArrays(this.value, begin, length);
        return buf;
    }

    /**
     * 向输出流中写入全部字节
     *
     * @param out 输出流
     * @throws IOException 输出流发生错误
     */
    public void write(OutputStream out) throws IOException {
        if (out == null) {
            throw new NullPointerException();
        }

        if (this.count != 0) {
            out.write(this.value, 0, this.count);
        }
    }

    /**
     * 向输出流中写入字节
     *
     * @param out    输出流
     * @param offset 起始位置
     * @param length 长度
     * @throws IOException 输出流发生错误
     */
    public void write(OutputStream out, int offset, int length) throws IOException {
        if (out == null) {
            throw new NullPointerException();
        }
        if (offset < 0 || length < 0 || (offset + length) > this.count) {
            throw new IllegalArgumentException(offset + ", " + length);
        }
        
        out.write(this.value, offset, length);
    }

    /**
     * 返回一个输入流
     * 需要特别注意的是: 只能读取当前时点的字节数组副本
     *
     * @return 输入流
     */
    public InputStream getInputStream() {
        return new ByteArrayInputStream(this.value());
    }

    /**
     * 返回一个输出流（将字节信息写入到当前字节集合中）
     *
     * @return 输出流
     */
    public OutputStream getOutputStream() {
        return new OutputStreamImpl(this);
    }

    static class OutputStreamImpl extends OutputStream {
        private ByteBuffer buffer;

        public OutputStreamImpl(ByteBuffer buffer) {
            super();
            this.buffer = buffer;
        }

        public void write(int b) {
            this.buffer.append((byte) b);
        }

        public void write(byte[] b) {
            this.buffer.append(b);
        }

        public void write(byte[] b, int off, int len) {
            this.buffer.append(b, off, len);
        }

        public void flush() {
        }

        public void close() {
        }

        public String toString() {
            return this.buffer.toString(StringUtils.charset(this.buffer.getCharsetName()));
        }

    }
}
