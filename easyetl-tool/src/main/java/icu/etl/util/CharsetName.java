package icu.etl.util;

/**
 * 字符集管理接口
 *
 * @author jeremy8551@qq.com
 * @createtime 2010-03-05
 */
public interface CharsetName {

    String GBK = "GBK";

    String UTF_8 = "UTF-8";

    String ISO_8859_1 = "ISO-8859-1";

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