package icu.etl.util;

/**
 * 字符集管理接口
 *
 * @author jeremy8551@qq.com
 * @createtime 2010-03-05
 */
public interface Charset {

    /**
     * 字符集
     *
     * @return
     */
    String getCharsetName();

    /**
     * 设置字符集
     *
     * @param charsetName 字符集名称
     */
    void setCharsetName(String charsetName);

}