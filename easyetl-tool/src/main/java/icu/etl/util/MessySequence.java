package icu.etl.util;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Arrays;

/**
 * 字符串乱码处理类 <br>
 * <br>
 * 乱码: 在字符串中不符合字符集编码规则的字符 <br>
 * <br>
 * 1）支持处理单个字符串中的乱码 <br>
 * 2）支持批量处理大量字符串中的乱码 <br>
 *
 * @author jeremy8551@qq.com
 * @createtime 2017-02-28
 */
public class MessySequence implements CharSequence {

    /** 字符集编码器 */
    protected CharsetEncoder encoder;

    /** 输入的字符串 */
    protected String source;

    /** 字符集编码 */
    protected String charsetName;

    /** 乱码字符个数 */
    protected int length;

    /** 乱码字符的数组 */
    protected char[] array;

    /** 乱码字符所在位置的数组，从0开始 */
    protected int[] position;

    /** 删除乱码后的字符串 */
    protected String result;

    /**
     * 初始化
     *
     * @param charsetName 字符集
     */
    public MessySequence(String charsetName) {
        Charset charset = Charset.forName(charsetName);
        CharsetEncoder encoder = charset.newEncoder();
        if (encoder == null) {
            throw new NullPointerException();
        } else {
            this.encoder = encoder;
            this.charsetName = charsetName;
        }
    }

    /**
     * 初始化并解析非字符集的字符
     *
     * @param str         字符串
     * @param charsetName 字符集
     */
    public MessySequence(String str, String charsetName) {
        this(charsetName);
        this.result = this.remove(str);
    }

    /**
     * 初始化
     *
     * @param encoder 字符集的编码器
     */
    public MessySequence(CharsetEncoder encoder) {
        if (encoder == null) {
            throw new NullPointerException();
        } else {
            this.encoder = encoder;
            this.charsetName = encoder.charset().name();
        }
    }

    /**
     * 初始化并解析非字符集的字符
     *
     * @param str     字符串
     * @param encoder 字符集的编码器
     */
    public MessySequence(String str, CharsetEncoder encoder) {
        this(encoder);
        this.result = this.remove(str);
    }

    /**
     * 删除字符串中的乱码字符
     *
     * @param str 字符串
     * @return 删除乱码字符后的字符串
     */
    public String remove(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }

        this.length = 0;
        this.source = str;
        int length = str.length();

        this.position = new int[length];
        Arrays.fill(this.position, -1);

        this.array = new char[length];
        Arrays.fill(this.array, ' ');

        int newlength = 0; // 正确字符的个数
        char[] array = new char[length]; // 正确字符数组
        for (int i = 0; i < length; i++) {
            char c = str.charAt(i);

            if (this.encoder.canEncode(c)) {
                array[newlength++] = c;
            } else {
                this.array[this.length] = c;
                this.position[this.length] = i;
                this.length++;
            }
        }

        if (newlength == length) {
            return str;
        } else {
            return new String(array, 0, newlength);
        }
    }

    /**
     * 返回true表示可以编码字符参数
     *
     * @param c 字符
     * @return true表示字符参数在字符集内
     */
    public boolean canEncode(char c) {
        return this.encoder.canEncode(c);
    }

    /**
     * 乱码字符个数
     *
     * @return
     */
    public int length() {
        return this.length;
    }

    /**
     * 返回指定位置上的乱码字符
     */
    public char charAt(int index) {
        if (index >= this.length) {
            throw new IllegalArgumentException(String.valueOf(index));
        }
        return this.array[index];
    }

    /**
     * 截取乱码字符
     */
    public String subSequence(int start, int end) {
        if (start >= this.length || end > this.length || start > end) {
            throw new IllegalArgumentException(start + ", " + end);
        }
        return new String(this.array, start, end - start);
    }

    /**
     * 使用字符数组 array 替换字符串中的乱码字符
     *
     * @param array 字符数组
     * @return
     */
    public String replace(char... array) {
        String replace = array.length == 0 ? "■" : new String(array); // 乱码替换字符
        int length = this.source.length();
        StringBuilder cb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            if (Numbers.inArray(i, this.position)) {
                cb.append(replace);
            } else {
                cb.append(this.source.charAt(i));
            }
        }
        return cb.toString();
    }

    /**
     * 返回 true 表示字符串中存在乱码字符
     *
     * @return
     */
    public boolean contains() {
        return this.length != 0;
    }

    /**
     * 乱码字符的数组
     *
     * @return
     */
    public char[] toArray() {
        if (this.length == 0) {
            return new char[0];
        } else {
            char[] array = new char[this.length];
            System.arraycopy(this.array, 0, array, 0, this.length);
            return array;
        }
    }

    /**
     * 乱码字符所在位置的数组（从0开始）
     *
     * @return
     */
    public int[] getPosition() {
        if (this.length == 0) {
            return new int[0];
        } else {
            int[] array = new int[this.length];
            System.arraycopy(this.position, 0, array, 0, this.length);
            return array;
        }
    }

    /**
     * 返回字符集编码器
     *
     * @return
     */
    public CharsetEncoder getEncoder() {
        return this.encoder;
    }

    /**
     * 返回字符集编码
     *
     * @return
     */
    public String getCharsetName() {
        return this.charsetName;
    }

    /**
     * 返回在删除乱码之前的字符串
     *
     * @return
     */
    public String getSource() {
        return this.source;
    }

    /**
     * 高亮显示乱码: <br>
     * 1) 非乱码字符替换为半角空白字符 <br>
     * 2) 乱码字符替换为半角字符 tip <br>
     *
     * @param tip 半角字符
     * @return
     */
    public String highlights(char tip) {
        if (tip < 32 || tip > 126) { // 只能用半角字符
            throw new IllegalArgumentException(String.valueOf(tip));
        }

        int length = this.source.length();
        StringBuilder cb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char c = this.source.charAt(i);
            String word = String.valueOf(c);

            int charSize = StringUtils.length(word, this.charsetName);
            if (charSize == 1) { // 半角字符
                if (Numbers.inArray(i, this.position)) {
                    cb.append(tip);
                } else {
                    cb.append(' ');
                }
            } else { // 全角字符
                if (Numbers.inArray(i, this.position)) {
                    cb.append(StringUtils.toFullWidthChar(tip));
                } else {
                    cb.append(StringUtils.FULLWIDTH_BLANK);
                }
            }
        }
        return cb.toString();
    }

    /**
     * 显示乱码详细信息 <br>
     * 字符串{ xxxx } 中第 x 个字符, 第 y 个字符是非字符集中的字符!
     *
     * @return
     */
    public String toMessyString() {
        if (this.length == 0) {
            return "";
        }

        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < this.length; ) {
            int number = this.position[i] + 1;
            buf.append(ResourcesUtils.getCommonMessage(4, number));
            if (++i < this.length) {
                buf.append(", ");
            }
        }
        return ResourcesUtils.getCommonMessage(5, this.source, buf, StringUtils.toCase(this.charsetName, false, null));
    }

    public String toString() {
        return this.result;
    }

}