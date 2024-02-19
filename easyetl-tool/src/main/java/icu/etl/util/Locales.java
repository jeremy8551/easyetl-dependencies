package icu.etl.util;

import java.util.Locale;

/**
 * Locale 帮助类
 *
 * @author jeremy8551@qq.com
 * @createtime 2024/2/19 14:58
 */
public class Locales {

    /**
     * 根据字符串参数 name 搜索国际化信息
     *
     * @param name 字符串，如：zh_CN
     * @return 返回 null 表示字符串参数 name错误
     */
    public static Locale lookup(String name) {
        try {
            Locale locale = new Locale(name);
            return StringUtils.isBlank(locale.toString()) ? null : locale;
        } catch (Throwable e) {
            return null;
        }
    }

}
