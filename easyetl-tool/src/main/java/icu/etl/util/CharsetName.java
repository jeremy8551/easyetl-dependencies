package icu.etl.util;

/**
 * 字符集管理接口
 *
 * @author jeremy8551@qq.com
 * @createtime 2010-03-05
 */
public interface CharsetName {

    public final static String GBK = "GBK";

    public final static String UTF_8 = "UTF-8";

    public final static String UTF_16 = "UTF-16";

    public final static String UTF_16BE = "UTF-16BE";

    public final static String UTF_16LE = "UTF-16LE";

    public final static String ISO_8859_1 = "ISO-8859-1";

    public final static String US_ASCII = "US-ASCII";

    /**
     * 字符集
     *
     * @return 字符集名称
     */
    String getCharsetName();

    /**
     * 设置字符集
     *
     * @param charsetName 字符集名称
     */
    void setCharsetName(String charsetName);

}