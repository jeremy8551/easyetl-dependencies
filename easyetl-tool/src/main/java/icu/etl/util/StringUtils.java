package icu.etl.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具
 * <p>
 * 后续开始时需要注意基础工具类中不能依赖其他工具类，只基于JDK API 编写方法
 *
 * @author jeremy8551@qq.com
 * @createtime 2010-01-08 2:27:49
 */
public class StringUtils {

    /** JDK日志输出接口 */
    private final static Logger log = Logger.getLogger(StringUtils.class.getName());

    /** 全角空白字符 */
    public final static String FULLWIDTH_BLANK = "　";

    /** 系统默认字符集 */
    public final static String PROPERTY_CHARSET = StringUtils.class.getPackage().getName().split("\\.")[0] + "." + StringUtils.class.getPackage().getName().split("\\.")[1] + ".charset";

    /** 默认字符集 */
    public static String CHARSET = getCharset();

    public StringUtils() {
    }

    /**
     * 返回默认字符集
     *
     * @return
     */
    public static String getCharset() {
        if (CHARSET == null) {
            String charset = System.getProperty(PROPERTY_CHARSET);
            if (charset == null || charset.length() == 0) {
                CHARSET = System.getProperty("file.encoding");
            } else {
                CHARSET = charset;
            }
        }
        return CHARSET;
    }

    /**
     * 根据字符串参数 charsetName 搜索字符集信息
     *
     * @param name 字符集名(如: UTF-8)
     * @return 返回 null 表示字符串参数 name错误
     */
    public static java.nio.charset.Charset lookupCharset(String name) {
        try {
            return java.nio.charset.Charset.forName(name);
        } catch (Throwable x) {
            return null;
        }
    }

    /**
     * 根据字符串参数 name 搜索国际化信息
     *
     * @param name 字符串，如：zh_CN
     * @return 返回 null 表示字符串参数 name错误
     */
    public static Locale lookupLocale(String name) {
        try {
            Locale locale = new Locale(name);
            return StringUtils.isBlank(locale.toString()) ? null : locale;
        } catch (Throwable e) {
            return null;
        }
    }

    /**
     * 返回字符串参数 str 的字节长度
     *
     * @param str         字符串
     * @param charsetName 字符集编码
     * @return
     */
    public static int length(String str, String charsetName) {
        try {
            return str == null ? 0 : str.getBytes(charsetName).length;
        } catch (Throwable e) {
            throw new UnsupportedOperationException(str + ", " + charsetName, e);
        }
    }

    /**
     * 字符串数组中最长字符串的长度（按char计算）
     *
     * @param array 字符串数组
     * @return
     */
    public static int maxlength(String... array) {
        if (array == null || array.length == 0) {
            return 0;
        }

        int max = 0;
        for (int i = 0; i < array.length; i++) {
            String str = array[i];
            if (str != null && str.length() > max) {
                max = str.length();
            }
        }
        return max;
    }

    /**
     * 计算字符串在显示器上的宽度，基础单位是一个英文字符，一个中文字符算2个英文字符宽度
     *
     * @param str         字符串
     * @param charsetName 字符集
     * @return 显示宽度
     */
    public static int width(CharSequence str, String charsetName) {
        try {
            int length = 0;
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (StringUtils.isAscii(c) || String.valueOf(c).getBytes(charsetName).length == 1) {
                    length++;
                } else {
                    length += 2;
                }
            }
            return length;
        } catch (Exception e) {
            throw new RuntimeException(str + ", " + charsetName, e);
        }
    }

    /**
     * 计算字符串在显示器上的宽度，基础单位是一个英文字符，一个中文字符算2个英文字符宽度
     *
     * @param c           字符
     * @param charsetName 字符集
     * @return 显示宽度
     */
    public static int width(char c, String charsetName) {
        try {
            if (StringUtils.isAscii(c) || String.valueOf(c).getBytes(charsetName).length == 1) {
                return 1;
            } else {
                return 2;
            }
        } catch (Exception e) {
            throw new RuntimeException(c + ", " + charsetName, e);
        }
    }

    /**
     * 字符串数组中最长字符串的长度（按字节计算）
     *
     * @param charsetName 字符串字符集
     * @param array       字符串数组
     * @return
     */
    public static int width(String charsetName, String[] array) {
        if (array == null || array.length == 0) {
            return 0;
        }

        int max = 0;
        for (int i = 0; i < array.length; i++) {
            String str = array[i];
            int length = StringUtils.width(str, charsetName);
            if (str != null && length > max) {
                max = length;
            }
        }
        return max;
    }

    /**
     * 判断字符是否相等
     *
     * @param c1         字符1
     * @param c2         字符2
     * @param ignoreCase true表示忽略字符的英文字母大小写
     * @return true表示相等
     */
    public static boolean equals(char c1, char c2, boolean ignoreCase) {
        if (ignoreCase) {
            if (c1 == c2) {
                return true;
            } else if (StringUtils.isLetter(c1) && StringUtils.isLetter(c2)) { // 如果是英文字符，需要忽略大小写
                return Character.toLowerCase(c1) == Character.toLowerCase(c2);
            } else {
                return false;
            }
        } else {
            return c1 == c2;
        }
    }

    /**
     * 判断字符串参数是否相等
     *
     * @param str1       字符串
     * @param str2       字符串
     * @param ignoreCase true表示忽略字符串中英文字母大小写
     * @return true表示相等
     */
    public static boolean equals(String str1, String str2, boolean ignoreCase) {
        boolean v1 = str1 == null;
        boolean v2 = str2 == null;
        if (v1 && v2) {
            return true;
        } else if (v1 || v2) {
            return false;
        } else if (ignoreCase) {
            return str1.equalsIgnoreCase(str2);
        } else {
            return str1.equals(str2);
        }
    }

    /**
     * 判断字符串是否是空字符串
     *
     * @param str 字符串
     * @return true表示字符是 null 或长度是 0
     */
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    /**
     * 判断字符串参数str是否为空白字符串(为null或全是空白字符)<br>
     * isBlank("") == true <br>
     * isBlank("12") == false <br>
     * isBlank(" ") == true <br>
     * isBlank(null) == true <br>
     *
     * @param str 字符串
     * @return
     */
    public static boolean isBlank(CharSequence str) {
        if (str != null) {
            for (int i = 0, size = str.length(); i < size; i++) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 判断字符串数组参数array中是否全部为空白字符串 <br>
     * ST.isBlank(null) == true <br>
     *
     * @param array 字符串数组
     * @return
     */
    public static boolean isBlank(CharSequence[] array) {
        if (array != null && array.length > 0) {
            for (int i = 0; i < array.length; i++) {
                if (!StringUtils.isBlank(array[i])) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 判断字符串参数str是否为非空白字符串<br>
     * isNotBlank("") == false <br>
     * isNotBlank("12") == true <br>
     * isNotBlank(" ") == false <br>
     * isNotBlank(null) == false <br>
     *
     * @param str 字符串
     * @return
     */
    public static boolean isNotBlank(CharSequence str) {
        return !StringUtils.isBlank(str);
    }

    /**
     * 判断字符是否是 ASCII 码表字符
     *
     * @param c 字符
     * @return true是ASCII码
     */
    public static boolean isAscii(char c) {
        return c >= 0 && c <= 127;
    }

    /**
     * 删除字符串参数str左右二端的半角空格字符 <br>
     * ST.trim(null) == null <br>
     * ST.trim(" 123 ") == "123" <br>
     * ST.trim(" 123") == "123" <br>
     *
     * @param str 字符串
     * @return
     */
    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    /**
     * 删除字符串数组参数array中每个字符串的左右二端的半角空格字符
     *
     * @param array 字符串数组
     * @return 返回 array 参数本身（不是副本）
     */
    public static String[] trim(String[] array) {
        if (array != null && array.length > 0) {
            for (int i = 0; i < array.length; i++) {
                array[i] = StringUtils.trim(array[i]);
            }
        }
        return array;
    }

    /**
     * 删除字符串集合参数list中每个字符串的左右二端的半角空格字符
     *
     * @param list 字符串集合
     */
    public static void trim(List<String> list) {
        if (list != null && list.size() > 0) {
            for (int i = 0, size = list.size(); i < size; i++) {
                list.set(i, StringUtils.trim(list.get(i)));
            }
        }
    }

    /**
     * 删除Map集合中Value字符串数组中字符串左右二端的半角空格字符
     *
     * @param map 集合对象
     * @return 返回参数map
     */
    public static Map<String, String> trim(Map<String, String> map) {
        if (map != null && map.size() > 0) {
            Set<String> keys = map.keySet();
            for (Iterator<String> it = keys.iterator(); it.hasNext(); ) {
                String key = it.next();
                map.put(key, StringUtils.trim(map.get(key)));
            }
        }
        return map;
    }

    /**
     * 删除字符串参数str左右二端的字符参数 array
     *
     * @param str   字符串
     * @param array 字符数组
     * @return
     */
    public static String trim(CharSequence str, char... array) {
        if (str == null) {
            return null;
        }
        if (array == null || array.length == 0) {
            return str.toString();
        }

        int left = 0, length = str.length(), right = length - 1;
        while (left < length && StringUtils.inArray(str.charAt(left), array)) {
            left++;
        }
        while (left <= right && right >= 0 && StringUtils.inArray(str.charAt(right), array)) {
            right--;
        }
        return str.subSequence(left, right + 1).toString();
    }

    /**
     * 将参数obj转为字符串并删除字符串左右端的空白字符（半角空格,全角空格,\r,\n,\t等）
     *
     * @param obj   字符串
     * @param array 待删除字符
     * @return
     */
    public static String trimBlank(Object obj, char... array) {
        if (obj == null) {
            return null;
        }

        String str = obj.toString();
        int sp = 0, len = str.length(), ep = len - 1;
        while (sp < len && (Character.isWhitespace(str.charAt(sp)) || StringUtils.inArray(str.charAt(sp), array))) {
            sp++;
        }
        while (sp <= ep && ep >= 0 && (Character.isWhitespace(str.charAt(ep)) || StringUtils.inArray(str.charAt(ep), array))) {
            ep--;
        }
        return str.substring(sp, ep + 1);
    }

    /**
     * 删除字符串数组参数array中每个字符串的左右二端的空白字符
     *
     * @param array 字符串数组
     * @return array参数本身（不是副本）
     */
    public static String[] trimBlank(String[] array) {
        if (array != null && array.length > 0) {
            for (int i = 0; i < array.length; i++) {
                array[i] = StringUtils.trimBlank(array[i]);
            }
        }
        return array;
    }

    /**
     * 删除字符串集合参数list中每个字符串的左右二端的空白字符
     *
     * @param list 字符串集合
     * @return 返回集合参数list
     */
    public static List<String> trimBlank(List<String> list) {
        if (list != null && list.size() > 0) {
            for (int i = 0, size = list.size(); i < size; i++) {
                list.set(i, StringUtils.trimBlank(list.get(i)));
            }
        }
        return list;
    }

    /**
     * 删除Map集合中Value字符串数组中字符串左右二端的空白字符
     *
     * @param map 集合对象
     * @return 返回参数map
     */
    public static Map<String, String> trimBlank(Map<String, String> map) {
        if (map != null && map.size() > 0) {
            Set<String> keys = map.keySet();
            for (Iterator<String> it = keys.iterator(); it.hasNext(); ) {
                String key = it.next();
                map.put(key, StringUtils.trimBlank(map.get(key)));
            }
        }
        return map;
    }

    /**
     * 删除字符串参数str左右二端的小括号()与空白字符
     *
     * @param str 字符串
     * @return
     */
    public static String trimParenthes(CharSequence str) {
        if (str == null) {
            return null;
        }
        if (str.length() <= 1) {
            return str.toString();
        }

        str = StringUtils.trimBlank(str);
        if (str.charAt(0) == '(' && str.charAt(str.length() - 1) == ')') {
            str = StringUtils.trimBlank(str.subSequence(1, str.length() - 1));
            return str.length() > 0 && str.charAt(0) == '(' ? StringUtils.trimParenthes(str) : str.toString();
        } else {
            return str.toString();
        }
    }

    /**
     * 删除字符串参数str右端的半角空格字符<br>
     * <br>
     * rTrim(" 1234 ") == " 1234" <br>
     * rTrim(" ") == "" <br>
     * rTrim(null) == null <br>
     *
     * @param str 字符串
     * @return
     */
    public static String rtrim(CharSequence str) {
        if (str == null) {
            return null;
        }

        int index = str.length() - 1;
        while (index >= 0 && str.charAt(index) == ' ') {
            index--;
        }
        return ++index == str.length() ? str.toString() : str.subSequence(0, index).toString();
    }

    /**
     * 删除字符串参数str最右端的字符数组参数array中的字符
     *
     * @param str   字符串
     * @param array 字符数组
     * @return
     */
    public static String rtrim(CharSequence str, char... array) {
        if (str == null) {
            return null;
        }
        if (array == null || array.length == 0) {
            return str.toString();
        }

        int index = str.length() - 1;
        while (index >= 0 && StringUtils.inArray(str.charAt(index), array)) {
            index--;
        }
        return ++index == str.length() ? str.toString() : str.subSequence(0, index).toString();
    }

    /**
     * 将参数obj转为字符串并删除字符串右端的空白字符与 {@code  array} 数组中的字符 <br>
     * rtrimBlank(" 1234 ") == " 1234" <br>
     * rtrimBlank(" ") == "" <br>
     * rtrimBlank(null) == null <br>
     *
     * @param obj   字符串
     * @param array 待删除字符
     * @return
     */
    public static String rtrimBlank(Object obj, char... array) {
        if (obj == null) {
            return null;
        }

        String str = obj.toString();
        int index = str.length() - 1;
        while (index >= 0) {
            char c = str.charAt(index);
            if (Character.isWhitespace(c) || StringUtils.inArray(c, array)) {
                index--;
            } else {
                break;
            }
        }
        return ++index == str.length() ? str : str.substring(0, index);
    }

    /**
     * 删除字符串参数str左端的半角空白字符 <br>
     * ltrim(null) == null <br>
     * ltrim(" ") == "" <br>
     * ltrim(" 12345") == "12345" <br>
     * ltrim(" 12345 ") == "12345 " <br>
     * ltrim("12345") == "12345" <br>
     *
     * @param str 字符串
     * @return
     */
    public static String ltrim(CharSequence str) {
        if (str == null) {
            return null;
        }

        int index = 0;
        int length = str.length();
        while (index < length && str.charAt(index) == ' ') {
            index++;
        }
        return index == 0 ? str.toString() : str.subSequence(index, str.length()).toString();
    }

    /**
     * 删除字符串参数str左端的字符c
     *
     * @param str   字符串
     * @param array 字符数组
     * @return
     */
    public static String ltrim(CharSequence str, char... array) {
        if (str == null) {
            return null;
        }
        if (array == null || array.length == 0) {
            return str.toString();
        }

        int index = 0;
        int length = str.length();
        while (index < length && StringUtils.inArray(str.charAt(index), array)) {
            index++;
        }
        return index == 0 ? str.toString() : str.subSequence(index, str.length()).toString();
    }

    /**
     * 将参数obj转为字符串并删除字符串左端的空白字符 <br>
     * ltrim(null) == null <br>
     * ltrim(" ") == "" <br>
     * ltrim(" 12345") == "12345" <br>
     * ltrim(" 12345 ") == "12345 " <br>
     * ltrim("12345") == "12345" <br>
     *
     * @param obj   字符串
     * @param array 待删除字符
     * @return
     */
    public static String ltrimBlank(Object obj, char... array) {
        if (obj == null) {
            return null;
        }

        int index = 0;
        String str = obj.toString();
        int length = str.length();
        while (index < length) {
            char c = str.charAt(index);
            if (Character.isWhitespace(c) || StringUtils.inArray(c, array)) {
                index++;
            } else {
                break;
            }
        }
        return index == 0 ? str : str.substring(index);
    }

    /**
     * 把一个参数对象obj转成字符串（字符串的内容跟toString()方法有关）,并且删除右端空格 <br>
     * 如：<br>
     * objToStr(" 12345 ") = " 12345"<br>
     * objToStr(43000) = "43000"<br>
     * objToStr(null) = ""<br>
     *
     * @param obj 参数对象
     * @return
     */
    public static String objToStr(Object obj) {
        if (obj == null) {
            return "";
        } else {
            return StringUtils.rtrim(obj.toString());
        }
    }

    /**
     * 使用参数replacement替换字符串参数str中从begin开始length长的内容
     *
     * @param str         字符串
     * @param begin       字符串开始位置（从0开始）
     * @param length      替换长度（从0开始）
     * @param replacement 替换后的内容
     * @return
     */
    public static String replace(CharSequence str, int begin, int length, CharSequence replacement) {
        if (begin < 0) {
            throw new IllegalArgumentException(String.valueOf(begin));
        }
        if (length < 0) {
            throw new IllegalArgumentException(String.valueOf(length));
        }
        if (str == null || length == 0) {
            return str.toString();
        }
        if (begin >= str.length()) {
            return str.toString() + replacement;
        }

        StringBuilder buf = new StringBuilder(str.length() - length + replacement.length());
        buf.append(str.subSequence(0, begin));
        buf.append(replacement);
        buf.append(str.subSequence(begin + length, str.length()));
        return buf.toString();
    }

    /**
     * 使用字符串参数newStr替换字符串参数str中从左端开始搜索到的第一个字符串参数oldStr <br>
     * <br>
     * replaceFirst("asasas","s","t") = "atasas"
     *
     * @param str    字符串
     * @param oldStr 字符串
     * @param newStr 字符串
     * @return
     */
    public static String replace(String str, String oldStr, String newStr) {
        if (str == null) {
            return null;
        }
        if (oldStr == null) {
            throw new NullPointerException();
        }
        if (newStr == null) {
            throw new NullPointerException();
        }

        int index = str.indexOf(oldStr);
        return index == -1 ? str : new StringBuilder(str).replace(index, index + oldStr.length(), newStr).toString();
    }

    /**
     * 使用字符串参数newStr替换字符串参数str中从左端开始搜索到所有的字符串参数oldStr
     *
     * @param str    字符串
     * @param oldStr 字符串
     * @param newStr 字符串
     * @return
     */
    public static String replaceAll(CharSequence str, String oldStr, String newStr) {
        if (str == null) {
            return null;
        }
        if (oldStr == null) {
            throw new NullPointerException();
        }
        if (newStr == null) {
            throw new NullPointerException();
        }

        StringBuilder buf = new StringBuilder(str);
        for (int index = 0; ; ) {
            if ((index = buf.indexOf(oldStr, index)) == -1) {
                break;
            }
            buf.replace(index, index + oldStr.length(), newStr);
            index += newStr.length();
        }
        return buf.toString();
    }

    /**
     * 使用字符串参数newStr替换字符串参数str中从右端向左搜索到的第一个字符串参数oldStr<br>
     * <br>
     * replaceLast("asasas","s","t") = "asasat"
     *
     * @param str    字符串
     * @param oldStr 字符串
     * @param newStr 字符串
     * @return
     */
    public static String replaceLast(String str, String oldStr, String newStr) {
        if (str == null) {
            return null;
        }
        if (oldStr == null) {
            throw new NullPointerException();
        }
        if (newStr == null) {
            throw new NullPointerException();
        }

        int index = str.lastIndexOf(oldStr);
        return index == -1 ? str : new StringBuilder(str).replace(index, index + oldStr.length(), newStr).toString();
    }

    /**
     * 使用Map中的变量替换字符串参数str中的shell型变量, 再使用环境变量替换字符串参数str中的shell型变量
     *
     * @param str 字符串
     * @param map 变量集合
     * @return
     */
    public static String replaceEnvironment(String str, Map<String, String> map) {
        if (str == null || map == null) {
            return str;
        } else {
            return StringUtils.replaceVariable(StringUtils.replaceVariable(str, map, -1), System.getenv(), -1);
        }
    }

    /**
     * 使用环境变量替换字符串参数str中的shell型变量 <br>
     * replaceEnvironmentVariable("mkdir ${HOME}/f/k/adb", ) == "mkdir /home/xxx/f/k/adb"
     *
     * @param str 字符串
     * @return
     */
    public static String replaceEnvironment(String str) {
        if (str == null) {
            return null;
        } else {
            return StringUtils.replaceVariable(str, System.getenv(), -1);
        }
    }

    /**
     * 使用系统属性信息替换字符串参数str中的shell型变量
     *
     * @param str 字符串
     * @return
     */
    public static String replaceProperties(String str) {
        if (str == null) {
            return str;
        } else {
            return StringUtils.replaceVariable(str, System.getProperties(), -1);
        }
    }

    /**
     * 使用属性信息替换字符串参数str中的shell型变量
     *
     * @param str 字符串
     * @param p   属性集合
     * @return
     */
    public static String replaceProperties(String str, Properties p) {
        if (str == null || p == null) {
            return str;
        } else {
            return StringUtils.replaceVariable(str, p, -1);
        }
    }

    /**
     * 替换字符串中的变量 <br>
     * Map == first=99 second=2 <br>
     * replaceVariable("${first} + ${second}", map) == "99 + 2" <br>
     *
     * @param str     字符串
     * @param map     变量名变量值映射的集合
     * @param convert 把 map 对象中 Object 转为 String
     * @return
     */
    public static String replaceVariable(String str, Map<String, Object> map, Format convert) {
        if (str == null || map == null) {
            return str;
        } else {
            return StringUtils.replaceVariable(str, map, -1, convert);
        }
    }

    /**
     * 替换字符 str 中的变量占位符 ${xxx} 或 $xxx
     *
     * @param str     字符串
     * @param map     属性名与属性值的映射集合
     * @param index   开始替换位置
     * @param convert 类型转换器
     * @return
     */
    protected static String replaceVariable(String str, Map<String, Object> map, int index, Format convert) {
        if (index == -1) {
            index = str.indexOf("${");
        }

        int from = 0; // 下一次搜索开始的位置
        while (index != -1) {
            int end = StringUtils.indexOfUnixVariable(str, index + 1, str.length()) + 1; // 搜索变量名结尾的下一个字符
            if (end == 0) {
                return str;
            }

            String var = str.substring(index, end); // 格式: ${name}
            String key = var.substring(2, var.length() - 1); // 格式: name

            // 解析 key 中的嵌套变量
            int start = key.indexOf("${");
            if (start != -1) {
                key = StringUtils.replaceVariable(key, map, start, convert); // 替换嵌套变量名
            }

            Object obj = map.get(key);
            String value = null;
            if (obj != null) {
                value = (convert == null ? StringUtils.toString(obj) : convert.format(obj));
            }

            if (value == null) {
                from = end;
            } else {
                str = StringUtils.replace(str, var, value);
            }

            index = str.indexOf("${", from);
        }
        return str;
    }

    /**
     * 使用Map中的变量替换字符串参数str中的shell型变量 <br>
     * Map == first=99 second=2 <br>
     * replaceVariable("${first} + ${second}", map) == "99 + 2" <br>
     *
     * @param str 字符串
     * @param map 变量集合
     * @return
     */
    public static String replaceVariable(String str, Map<String, String> map) {
        if (str == null || map == null) {
            return str;
        } else {
            return StringUtils.replaceVariable(str, map, -1);
        }
    }

    /**
     * 使用字符串value替换字符串参数str中的字符串参数name <br>
     * Map == first=99 second=2 <br>
     * <p>
     * replaceVariable("${first} + 2", "first", "99") == "99 + 2"
     *
     * @param str   字符串
     * @param array 变量名和变量值映射数组, 如: new String[] {变量名1, 变量值1, 变量名2, 变量值2}
     * @return
     */
    public static String replaceVariable(String str, CharSequence... array) {
        if (str == null || array == null || array.length <= 1) {
            return str;
        } else {
            return StringUtils.replaceVariable(str, array, -1);
        }
    }

    /**
     * 替换字符串中 ${name} 变量 <br>
     * 替换嵌套变量, 如: ${${name}}
     *
     * @param str   字符串
     * @param map   变量名与变量值的映射
     * @param index 占位符 ${ 起始位置
     * @return
     */
    protected static String replaceVariable(String str, Map<String, String> map, int index) {
        if (index == -1) {
            index = str.indexOf("${");
        }

        int from = 0; // 下一次搜索开始的位置
        while (index != -1) {
            int end = StringUtils.indexOfUnixVariable(str, index + 1, str.length()) + 1; // 搜索变量名结尾的下一个字符
            if (end == 0) {
                return str;
            }

            String var = str.substring(index, end); // 格式: ${name}
            String key = var.substring(2, var.length() - 1); // 格式: name

            // 解析 key 中的嵌套变量
            int start = key.indexOf("${");
            if (start != -1) {
                key = StringUtils.replaceVariable(key, map, start); // 替换嵌套变量名
            }

            String value = map.get(key);
            if (value == null) {
                from = end;
            } else {
                str = StringUtils.replace(str, var, value);
            }

            index = str.indexOf("${", from);
        }
        return str;
    }

    /**
     * 替换字符串中 ${name} 变量 <br>
     * 替换嵌套变量, 如: ${${name}}
     *
     * @param str   字符串
     * @param array 变量名与变量值的映射
     * @param index 占位符 ${ 起始位置
     * @return
     */
    protected static String replaceVariable(String str, CharSequence[] array, int index) {
        if (index == -1) {
            index = str.indexOf("${");
        }

        int from = 0; // 下一次搜索开始的位置
        while (index != -1) {
            int end = StringUtils.indexOfUnixVariable(str, index + 1, str.length()) + 1; // 搜索变量名结尾的下一个字符
            if (end == 0) {
                return str;
            }

            String var = str.substring(index, end); // 格式: ${name}
            String key = var.substring(2, var.length() - 1); // 格式: name

            // 解析 key 中的嵌套变量
            int start = key.indexOf("${");
            if (start != -1) {
                key = StringUtils.replaceVariable(key, array, start); // 替换嵌套变量名
            }

            String value = getValue(array, key); // array.get(key);
            if (value == null) {
                from = end;
            } else {
                str = StringUtils.replace(str, var, value);
            }

            index = str.indexOf("${", from);
        }
        return str;
    }

    /**
     * 查询属性名对应的属性值
     *
     * @param array 属性数组
     * @param key   属性名
     * @return
     */
    protected static String getValue(CharSequence[] array, String key) {
        for (int i = 0; i < array.length; i += 2) {
            int next = i + 1;
            if (array[i].equals(key) && next < array.length) {
                return array[next].toString();
            }
        }
        return null;
    }

    /**
     * 替换字符串中 ${name} 变量 <br>
     * 替换嵌套变量, 如: ${${name}}
     *
     * @param str   字符串
     * @param p     变量名与变量值的映射
     * @param index 占位符 ${ 起始位置
     * @return
     */
    private static String replaceVariable(String str, Properties p, int index) {
        if (index == -1) {
            index = str.indexOf("${");
        }

        int from = 0; // 下一次搜索开始的位置
        while (index != -1) {
            int end = StringUtils.indexOfUnixVariable(str, index + 1, str.length()) + 1; // 搜索变量名结尾的下一个字符
            if (end == 0) {
                return str;
            }

            String var = str.substring(index, end); // 格式: ${name}
            String name = var.substring(2, var.length() - 1); // 格式: name

            // 解析 key 中的嵌套变量
            int start = name.indexOf("${");
            if (start != -1) {
                name = StringUtils.replaceVariable(name, p, start); // 替换嵌套变量名
            }

            String value = p.getProperty(name);
            if (value == null) {
                from = end;
            } else {
                str = StringUtils.replace(str, var, value);
            }

            index = str.indexOf("${", from);
        }
        return str;
    }

    /**
     * 替换字符串参数 str 中所有全角字符为半角字符
     *
     * @param str 字符串
     * @return
     */
    public static String replaceHalfWidthChar(String str) {
        if (str == null) {
            return null;
        }

        char[] array = str.toCharArray();
        for (int i = 0; i < array.length; i++) {
            char c = array[i];
            if (c >= 65281 && c <= 65374) { // 全角字符
                array[i] = (char) (c - 65248);
            } else if (c == 12288) { // 全角空格
                array[i] = (char) 32;
            }
        }
        return new String(array);
    }

    /**
     * 替换字符串参数 str 中所有空白字符为半角空格
     *
     * @param str 字符串
     * @return
     */
    public static String replaceHalfWidthBlank(String str) {
        if (str == null) {
            return null;
        }

        char[] array = str.toCharArray();
        for (int i = 0; i < str.length(); i++) {
            if (Character.isWhitespace(array[i])) {
                array[i] = ' ';
            }
        }
        return new String(array);
    }

    /**
     * 按字节截取字符串
     *
     * @param str         字符串
     * @param begin       开始截取为止(包含), 从0开始，单位：字节
     * @param length      截取的长度, 单位字节
     * @param charsetName 字符集, 为空时使用默认值
     * @return
     */
    public static String substring(String str, int begin, int length, String charsetName) {
        if (StringUtils.isBlank(charsetName)) {
            charsetName = StringUtils.CHARSET;
        }

        byte[] array = StringUtils.toBytes(str, charsetName);
        return StringUtils.substring(array, begin, length, charsetName);
    }

    /**
     * 从字节数组参数 array 指定 begin 位置开始截取 length 字节长度的字节数组
     *
     * @param array       字节数组
     * @param begin       开始截取位(包含本身), 从0开始
     * @param length      截取的长度,单位字节
     * @param charsetName 字符集
     * @return
     */
    protected static String substring(byte[] array, int begin, int length, String charsetName) {
        if (array == null) {
            return null;
        }
        if (begin < 0 || length < 0 || (begin + length > array.length)) {
            throw new IndexOutOfBoundsException(StringUtils.toBinaryString(array) + ", " + begin + ", " + length + ", " + charsetName);
        }

        try {
            byte[] newarray = new byte[length];
            System.arraycopy(array, begin, newarray, 0, length);
            return new String(newarray, charsetName);
        } catch (Throwable e) {
            throw new RuntimeException(StringUtils.toBinaryString(array) + ", " + begin + ", " + length + ", " + charsetName);
        }
    }

    /**
     * 从字符串参数str 的index位置开始向左left个字符向右right字符截取字符串
     *
     * @param str   字符串
     * @param index 位置(单位: 字符)
     * @param left  左截的位数(单位: 字符)
     * @param right 右截的位数(单位: 字符)
     * @return
     */
    public static String substring(CharSequence str, int index, int left, int right) {
        if (str == null) {
            return null;
        }
        if (index < 0 || index >= str.length() || left < 0 || right < 0) {
            throw new IllegalArgumentException(str + ", " + index + ", " + left + ", " + right);
        }

        int lp = index - left;
        if (lp < 0) {
            lp = 0;
        }

        int pos = index + right + 1;
        return pos > str.length() ? str.subSequence(lp, str.length()).toString() : str.subSequence(lp, pos).toString();
    }

    /**
     * 从字符串中的 index 位置开始向左和向右截取字符串, 并删除截取后字符串二端的空白字符
     *
     * @param str   字符串
     * @param index 位置(单位: 字符)
     * @param left  左截的位数(单位: 字符)
     * @param right 右截的位数(单位: 字符)
     * @return
     */
    public static String substr(CharSequence str, int index, int left, int right) {
        if (str == null) {
            return null;
        }

        int length = str.length();
        if (length == 0) {
            return "";
        }
        if (index < 0 || index >= length || left < 0 || right < 0) {
            throw new IllegalArgumentException(str + ", " + index + ", " + left + ", " + right);
        }

        int lp = index - left; // 左坐标
        if (lp < 0) {
            lp = 0;
        }
        while (lp < length && Character.isWhitespace(str.charAt(lp))) {
            ++lp;
        }
        if (lp == length) { // 删除左端空白
            return ""; // 已到字符串末端位置
        }

        int rp = index + right; // 右坐标
        if (rp >= length) {
            rp = length - 1;
        }

        while (lp < rp && Character.isWhitespace(str.charAt(rp))) { // 便利右侧字符中的空白字符
            if (--rp == 0) { // 删除右端空白
                return "";
            }
        }
        if (rp < lp) {
            rp = lp;
        }
        return str.subSequence(lp, rp + 1).toString();
    }

    /**
     * 从字符串str的左面截取length长度的字符串,如： <br>
     * <br>
     * left("12345", 4) = "1234"<br>
     * left("12", 4) = "12"<br>
     * left(null, 4) = null<br>
     *
     * @param obj    字符串
     * @param length 截取长度,单位字符
     * @return
     */
    public static String left(Object obj, int length) {
        if (obj == null) {
            return null;
        }
        if (length < 0) {
            throw new IllegalArgumentException(String.valueOf(length));
        }
        if (length == 0) {
            return "";
        }

        String str = obj.toString();
        return str.length() > length ? str.substring(0, length) : str;
    }

    /**
     * 从参数 obj 左边开始向右截取 length 个字符长度, 如果不够 length 个字符长度默认用半角空格填充最右端<br>
     * <br>
     * leftFormat("123", 5) = "123__"<br>
     * leftFormat("1234567", 5) = "12345"<br>
     * leftFormat(null, 5) = null <br>
     *
     * @param obj    参数对象
     * @param length 截取长度,单位字符
     * @param c      默认填充字符
     * @return
     */
    public static String left(Object obj, int length, char c) {
        if (obj == null) {
            return null;
        }
        if (length < 0) {
            throw new IllegalArgumentException(String.valueOf(length));
        }
        if (length == 0) {
            return "";
        }

        String str = obj.toString();
        int size = str.length();
        if (size < length) {
            StringBuilder buf = new StringBuilder(str);
            for (int i = length - size; i > 0; i--) {
                buf.append(c);
            }
            return buf.toString();
        } else if (size == length) {
            return str;
        } else {
            return str.substring(0, length);
        }
    }

    /**
     * 从左面截取字符串, 截取字符串时如果超长则自动忽略字符串最后的半个双字节字符
     *
     * @param obj         字符串
     * @param length      显示宽度，单位是英文字符在显示器中显示的宽度，一个中文字符作为2个显示宽度
     * @param charsetName 字符集
     * @return
     */
    public static String left(Object obj, int length, String charsetName) {
        if (obj == null) {
            return null;
        }
        if (length < 0) {
            throw new IllegalArgumentException(String.valueOf(length));
        }
        if (length == 0) {
            return "";
        }
        if (StringUtils.isBlank(charsetName)) {
            charsetName = StringUtils.CHARSET;
        }

        String str = obj.toString();
        int bytes = StringUtils.width(str, charsetName); // 显示宽度
        if (bytes <= length) {
            return str;
        } else {
            StringBuilder buf = new StringBuilder(str.length());
            for (int i = 0, width = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                width += StringUtils.width(c, charsetName);
                if (width == length) {
                    buf.append(c);
                    return buf.toString();
                } else if (width > length) {
                    return buf.toString();
                } else {
                    buf.append(c);
                }
            }
            return buf.toString();
        }
    }

    /**
     * 从字符串左侧截取length长度的字节如果不足长度自动在右侧补齐半角空白字符
     *
     * @param obj         字符串
     * @param length      显示宽度，单位是英文字符在显示器中显示的宽度，一个中文字符作为2个显示宽度
     * @param charsetName 字符集
     * @param d           默认填充字符
     * @return
     */
    public static String left(Object obj, int length, String charsetName, char d) {
        if (obj == null) {
            return null;
        }
        if (length < 0) {
            throw new IllegalArgumentException(String.valueOf(length));
        }
        if (length == 0) {
            return "";
        }
        if (StringUtils.isBlank(charsetName)) {
            charsetName = StringUtils.CHARSET;
        }

        String str = obj.toString();
        int size = StringUtils.width(str, charsetName); // 显示宽度
        if (size == length) {
            return str;
        } else if (size < length) {
            StringBuilder buf = new StringBuilder(str.length() + length - size).append(str);
            do {
                buf.append(d);
            } while ((size = StringUtils.width(buf, charsetName)) < length);

            if (size == length) {
                return buf.toString();
            } else {
                throw new IllegalArgumentException(String.valueOf(d));
            }
        } else {
            StringBuilder buf = new StringBuilder(str.length());
            for (int i = 0, width = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                width += StringUtils.width(c, charsetName);
                if (width == length) {
                    buf.append(c);
                    return buf.toString();
                } else if (width > length) {
                    return buf.append(d).toString();
                } else {
                    buf.append(c);
                }
            }
            return buf.toString();
        }
    }

    /**
     * 将 obj 转为字符串并从右端开始向左截取length个字符长度
     *
     * @param obj    字符串
     * @param length 截取长度,单位字符
     * @return
     */
    public static String right(Object obj, int length) {
        if (obj == null) {
            return null;
        }
        if (length < 0) {
            throw new IllegalArgumentException(String.valueOf(length));
        }
        if (length == 0) {
            return "";
        }

        String str = obj.toString();
        return str.length() > length ? str.substring(str.length() - length) : str;
    }

    /**
     * 将 obj 转为字符串并从右端开始向左截取length个字符长度，如果长度不够用字符参数 c 填充 <br>
     *
     * @param obj    参数对象
     * @param length 截取长度,单位字符
     * @param c      填充字符
     * @return
     */
    public static String right(Object obj, int length, char c) {
        if (obj == null) {
            return null;
        }
        if (length < 0) {
            throw new IllegalArgumentException(String.valueOf(length));
        }
        if (length == 0) {
            return "";
        }

        String str = obj.toString();
        int size = str.length();
        if (size >= length) {
            return str.substring(size - length);
        } else {
            StringBuilder buf = new StringBuilder();
            for (int i = length - size; i > 0; i--) {
                buf.append(c);
            }
            buf.append(str);
            return buf.toString();
        }
    }

    /**
     * 将 obj 转为字符串并从右端开始向左截取length个字节长度 <br>
     *
     * @param obj         参数对象
     * @param length      显示宽度，单位是英文字符在显示器中显示的宽度，一个中文字符作为2个显示宽度
     * @param charsetName 字符集（为空自动使用默认值）
     * @return
     */
    public static String right(Object obj, int length, String charsetName) {
        if (obj == null) {
            return null;
        }
        if (length < 0) {
            throw new IllegalArgumentException(String.valueOf(length));
        }
        if (length == 0) {
            return "";
        }
        if (StringUtils.isBlank(charsetName)) {
            charsetName = StringUtils.CHARSET;
        }

        String str = obj.toString();
        int width = StringUtils.width(str, charsetName);
        if (width <= length) {
            return str;
        } else {
            width = 0;
            StringBuilder buf = new StringBuilder(str.length());
            for (int i = str.length() - 1; i >= 0; i--) {
                char c = str.charAt(i);
                width += StringUtils.width(c, charsetName);
                if (width == length) {
                    buf.append(c);
                    return buf.reverse().toString();
                } else if (width > length) {
                    return buf.reverse().toString();
                } else {
                    buf.append(c);
                }
            }
            return buf.reverse().toString();
        }
    }

    /**
     * 将 obj 转为字符串并从右端开始向左截取length个显示长度，如果长度不够用字符参数 d 填充 <br>
     *
     * @param obj         参数对象
     * @param length      显示宽度，单位是英文字符在显示器中显示的宽度，一个中文字符作为2个显示宽度
     * @param charsetName 字符集（为空自动使用默认值）
     * @param d           填充字符
     * @return
     */
    public static String right(Object obj, int length, String charsetName, char d) {
        if (obj == null) {
            return null;
        }
        if (length < 0) {
            throw new IllegalArgumentException(String.valueOf(length));
        }
        if (length == 0) {
            return "";
        }
        if (StringUtils.isBlank(charsetName)) {
            charsetName = StringUtils.CHARSET;
        }

        String str = obj.toString();
        int size = StringUtils.width(str, charsetName); // 显示宽度
        if (size == length) {
            return str;
        } else if (size < length) {
            StringBuilder buf = new StringBuilder(str.length() + length - size).append(str);
            do {
                buf.insert(0, d);
            } while ((size = StringUtils.width(buf, charsetName)) < length);

            if (size == length) {
                return buf.toString();
            } else {
                throw new IllegalArgumentException(String.valueOf(d));
            }
        } else {
            StringBuilder buf = new StringBuilder(str.length());
            for (int i = str.length() - 1, width = 0; i >= 0; i--) {
                char c = str.charAt(i);
                width += StringUtils.width(c, charsetName);
                if (width == length) {
                    buf.append(c);
                    return buf.reverse().toString();
                } else if (width > length) {
                    return buf.append(d).reverse().toString();
                } else {
                    buf.append(c);
                }
            }
            return buf.reverse().toString();
        }
    }

    /**
     * 对象居中对齐
     *
     * @param obj         参数对象
     * @param length      显示宽度，单位是英文字符在显示器中显示的宽度，一个中文字符作为2个显示宽度
     * @param charsetName 字符集（为空自动使用默认值）
     * @param d           做补齐时使用的字节
     * @return 字符串
     */
    public static String middle(Object obj, int length, String charsetName, char d) {
        if (obj == null) {
            return null;
        }
        if (length < 0) {
            throw new IllegalArgumentException(String.valueOf(length));
        }
        if (length == 0) {
            return "";
        }
        if (StringUtils.isBlank(charsetName)) {
            charsetName = StringUtils.CHARSET;
        }

        String str = obj.toString();
        int width = StringUtils.width(str, charsetName);
        if (width == length) {
            return str;
        } else if (width > length) {
            return StringUtils.left(str, length, charsetName, d);
        } else {
            StringBuilder buf = new StringBuilder(str.length()).append(str);
            for (int i = 0, cz = length - width; i < cz; i++) {
                if (i % 2 == 0) { // 在右侧填充字符
                    buf.append(d);
                } else { // 在左侧填充字符
                    buf.insert(0, d);
                }

                width = StringUtils.width(buf, charsetName);
                if (width == length) {
                    return buf.toString();
                } else if (width > length) {
                    throw new IllegalArgumentException(String.valueOf(d)); // 填充字符错误
                }
            }
            return buf.toString();
        }
    }

    /**
     * 对 str 参数中 ‘\’ 字符进行转义（填写2个 ‘\’ 符号）
     *
     * @param str 字符串，为null时返回null
     * @return
     */
    public static String escape(CharSequence str) {
        return escape(str, '\\');
    }

    /**
     * 对 str 参数中转义字符进行转义（填写2个转义字符）
     *
     * @param str    字符串，为null时返回null
     * @param escape 转义字符
     * @return
     */
    public static String escape(CharSequence str, char escape) {
        if (str == null) {
            return null;
        }

        int length = str.length();
        StringBuilder buf = new StringBuilder(length + 5);
        for (int i = 0; i < length; i++) {
            char c = str.charAt(i);
            if (c == escape) {
                buf.append(escape);
            }
            buf.append(c);
        }
        return buf.length() == length ? str.toString() : buf.toString();
    }

    /**
     * 对正则表达式中保留字符及元字符进行转义（在特殊字符前加 ‘\’ 符号）
     *
     * @param regex 正则表达式
     * @return 转义后的正则表达式
     */
    public static String escapeRegex(String regex) {
        if (regex == null || regex.length() == 0) {
            return null;
        }

        StringBuilder buf = new StringBuilder(regex.length() + 5);
        for (int i = 0; i < regex.length(); i++) {
            char c = regex.charAt(i);
            if (StringUtils.inArray(c, '$', '(', ')', '*', '+', '.', '[', ']', '?', '\\', '^', '{', '}', '|')) {
                buf.append('\\');
            }
            buf.append(c);
        }
        return buf.toString();
    }

    /**
     * 替换字符串参数str中的回车符\r与换行符\n为 \\r \\n
     *
     * @param str 字符串
     * @return
     */
    public static String escapeLineSeparator(CharSequence str) {
        if (str == null) {
            return null;
        }

        int length = str.length();
        StringBuilder buf = new StringBuilder(length + 10);
        for (int i = 0; i < length; i++) {
            char c = str.charAt(i);
            switch (c) {
                case '\r':
                    buf.append('\\');
                    buf.append('r');
                    break;

                case '\n':
                    buf.append('\\');
                    buf.append('n');
                    break;

                default:
                    buf.append(c);
                    break;
            }
        }
        return buf.toString();
    }

    /**
     * 转换字符串<b>str</b>中的转义字符（Escape Sequence）<br>
     * 把字符串：<br>
     * \t \r \n \b \f \\ \' \" <br>
     * 转成对应的转义字符 <br>
     * <br>
     * 把\\uXXXX格式的字符串转成unicode码 <br>
     * 如: <br>
     * \\u002E 转成 点'.'
     *
     * @param str 字符串
     * @return
     */
    public static String unescape(CharSequence str) {
        if (str == null) {
            return null;
        }
        int length = str.length();
        if (length <= 1) {
            return str.toString();
        }

        char[] array = new char[length];
        int newLen = 0;
        boolean isEscape = false;
        for (int index = 0; index < length; ) {
            char c = str.charAt(index);

            if (isEscape) {
                switch (c) {
                    case '\\':
                        array[newLen++] = '\\';
                        break;
                    case '\'':
                        array[newLen++] = '\'';
                        break;
                    case '\"':
                        array[newLen++] = '"';
                        break;
                    case 'r':
                        array[newLen++] = '\r';
                        break;
                    case 'n':
                        array[newLen++] = '\n';
                        break;
                    case 'b':
                        array[newLen++] = '\b';
                        break;
                    case 'f':
                        array[newLen++] = '\f';
                        break;
                    case 't':
                        array[newLen++] = '\t';
                        break;
                    case 'u':
                        int ep = index + 5;
                        if (ep <= length) {
                            String number = str.subSequence(index + 1, ep).toString();
                            try {
                                array[newLen++] = (char) Integer.parseInt(number, 16);
                            } catch (Throwable e) {
                                throw new NumberFormatException("Unicode=[\\u" + number + "] parse to Integer error!");
                            }
                            isEscape = false;
                            index += 5;
                            continue;
                        }
                    default:
                        array[newLen++] = '\\';
                        array[newLen++] = c;
                        break;
                }

                isEscape = false;
                index++;
                continue;
            }

            if (++index != length && c == '\\') {
                isEscape = true;
            } else {
                array[newLen++] = c;
            }
        }

        return new String(array, 0, newLen);
    }

    /**
     * 在字符串二端加单引号<br>
     * quote("adb") = "'abc'"<br>
     * quote(" bcd ") = "' bcd '"<br>
     * quote(null) = null<br>
     *
     * @param str 字符串
     * @return
     */
    public static String quote(CharSequence str) {
        return str == null ? null : new StringBuilder(str.length() + 2).append('\'').append(str).append('\'').toString();
    }

    /**
     * 在字符串二端加双引号
     *
     * @param str
     * @return
     */
    public static String quotes(CharSequence str) {
        return str == null ? null : new StringBuilder(str.length() + 2).append('\"').append(str).append('\"').toString();
    }

    /**
     * 删除一次字符串参数str左右二端的单引号
     *
     * @param str 字符串
     * @return
     */
    public static String unquote(CharSequence str) {
        if (str == null) {
            return null;
        } else if (str.length() <= 1) {
            return str.toString();
        } else {
            int end = str.length() - 1;
            return str.charAt(0) == '\'' && str.charAt(end) == '\'' ? str.subSequence(1, end).toString() : str.toString();
        }
    }

    /**
     * 删除一次字符串参数str左右二端的双引号
     *
     * @param str 字符串
     * @return
     */
    public static String unquotes(CharSequence str) {
        if (str == null) {
            return null;
        } else if (str.length() <= 1) {
            return str.toString();
        } else {
            int end = str.length() - 1;
            return str.charAt(0) == '"' && str.charAt(end) == '"' ? str.subSequence(1, end).toString() : str.toString();
        }
    }

    /**
     * 删除一次字符串参数str左右二端的单引号或双引号
     *
     * @param str 字符串
     * @return
     */
    public static String unquotation(CharSequence str) {
        if (str == null) {
            return null;
        } else if (str.length() <= 1) {
            return str.toString();
        } else {
            int end = str.length() - 1;// 最后一个字符串位置
            return (str.charAt(0) == '\'' && str.charAt(end) == '\'') || (str.charAt(0) == '"' && str.charAt(end) == '"') ? str.subSequence(1, end).toString() : str.toString();
        }
    }

    /**
     * 返回字符串数组参数 array 中第一次出现字符串参数 dest 的位置
     *
     * @param array 字符串数组
     * @param dest  搜索字符串
     * @return -1表示字符串参数 str 没有出现
     */
    public static int indexOf(CharSequence[] array, CharSequence dest) {
        if (array == null) {
            throw new NullPointerException();
        }
        if (dest == null) {
            throw new NullPointerException();
        }

        for (int i = 0; i < array.length; i++) {
            if (dest.equals(array[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 返回字符串数组参数 array 中第一次出现字符串参数 dest 的位置（忽略大小写）
     *
     * @param array 字符串数组
     * @param dest  字符串
     * @return -1表示字符串参数 str 没有出现
     */
    public static int indexOfIgnoreCase(String[] array, String dest) {
        if (array == null) {
            throw new NullPointerException();
        }
        if (dest == null) {
            throw new NullPointerException();
        }

        for (int i = 0; i < array.length; i++) {
            if (dest.equalsIgnoreCase(array[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 从指定位置参数 from 处开始到搜索字符串参数 str，返回字符串参数 str 中第一次出现字符串参数 dest 的索引位置
     *
     * @param str        字符串
     * @param dest       搜索字符串
     * @param from       搜索的起始位置
     * @param ignoreCase true表示忽略大小写
     * @return -1表示字符串参数 key 没有出现
     */
    public static int indexOf(CharSequence str, CharSequence dest, int from, boolean ignoreCase) {
        if (StringUtils.isBlank(dest)) {
            throw new IllegalArgumentException(String.valueOf(dest));
        }
        if (from < 0) {
            throw new IllegalArgumentException(String.valueOf(from));
        }
        if (str == null || from >= str.length()) {
            return -1;
        }

        if (ignoreCase) {
            for (int i = from; i < str.length(); i++) {
                if (Character.toLowerCase(str.charAt(i)) == Character.toLowerCase(dest.charAt(0)) && StringUtils.startsWith(str, dest, i, ignoreCase, false)) {
                    return i;
                }
            }
            return -1;
        } else {
            for (int i = from; i < str.length(); i++) {
                if (str.charAt(i) == dest.charAt(0) && StringUtils.startsWith(str, dest, i, ignoreCase, false)) {
                    return i;
                }
            }
            return -1;
        }
    }

    /**
     * 从指定位置 from 开始到 end 索引位置搜索字符串数组 array，返回字符串数组 array 中第一次出现字符串参数 dest 的位置（忽略大小写）
     *
     * @param array      字符串数组
     * @param dest       字符串
     * @param from       搜索数组array的起始位置，从0开始
     * @param end        搜索数组array的终止位置(搜索范围包含 end 位置)，从0开始
     * @param ignoreCase true表示忽略大小写
     * @return -1表示字符串参数 str 没有出现
     */
    public static int indexOf(String[] array, String dest, int from, int end, boolean ignoreCase) {
        if (dest == null || array == null || from < 0 || from > array.length || end < from) {
            throw new IllegalArgumentException(StringUtils.toString(array) + ", " + dest + ", " + from + ", " + end + ", " + ignoreCase);
        }

        for (int i = from; i < array.length && i <= end; i++) {
            if (ignoreCase) {
                if (dest.equalsIgnoreCase(array[i])) {
                    return i;
                }
            } else {
                if (dest.equals(array[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 在字符序列参数 str 中从左开始搜索第一次出现空白字符的位置
     *
     * @param str  字符串
     * @param from 搜索起始位置
     * @param end  搜索结束位置（包含位置上的字符）, 等于 -1 表示字符串最右端
     * @return -1表示空白字符没有出现
     */
    public static int indexOfBlank(CharSequence str, int from, int end) {
        if (str == null) {
            throw new NullPointerException();
        }
        if ((from < 0 && from != -1) || from >= str.length()) {
            throw new IllegalArgumentException(str + ", " + from + ", " + end);
        }
        if ((end < 0 && end != -1) || (end >= 0 && end < from)) {
            throw new IllegalArgumentException(str + ", " + from + ", " + end);
        }
        if (from == -1) {
            from = 0;
        }
        if (end == -1) {
            end = str.length();
        }

        for (int i = from; i < str.length() && i <= end; i++) {
            if (Character.isWhitespace(str.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 在字符序列参数 str 中从左开始搜索第一次出现非空白字符的位置
     *
     * @param str   字符串
     * @param from  搜索起始位置, 等于 -1 时表示从0开始搜索
     * @param end   搜索终止位置, 等于 -1 时表示没有限制
     * @param array 忽略字符数组
     * @return -1表示非空白字符没有出现
     */
    public static int indexOfNotBlank(CharSequence str, int from, int end, char... array) {
        if (str == null) {
            throw new NullPointerException();
        }
        if ((from < 0 && from != -1) || from > str.length()) {
            throw new IndexOutOfBoundsException(str + ", " + from + ", " + end);
        }
        if ((end < 0 && end != -1) || (end >= 0 && end < from)) {
            throw new IndexOutOfBoundsException(str + ", " + from + ", " + end);
        }
        if (from == -1) {
            from = 0;
        }
        if (end == -1) {
            end = str.length();
        }

        for (int i = from; i < str.length() && i <= end; i++) {
            char c = str.charAt(i);
            if (!(Character.isWhitespace(c) || StringUtils.inArray(c, array))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 从字符序列参数 str 的起始位置开始搜索第一次出现字符参数 c 的位置
     *
     * @param str 字符序列
     * @param c   字符
     * @return -1表示字符参数 c 没有出现
     */
    public static int indexOf(CharSequence str, char c) {
        if (str == null) {
            return -1;
        }

        for (int i = 0, len = str.length(); i < len; i++) {
            if (str.charAt(i) == c) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 在字符数组参数 array 起始位置开始搜索第一次出现字符参数 c 的位置
     *
     * @param array 字符数组
     * @param c     字符
     * @return -1表示字符参数 c 没有出现
     */
    public static int indexOf(char[] array, char c) {
        return StringUtils.indexOf(array, c, 0);
    }

    /**
     * 在字符数组参数 array 左端开始搜索第一次出现字符参数 c 的位置
     *
     * @param array 字符数组
     * @param c     字符
     * @param from  搜索起始位置
     * @return -1表示字符参数 c 没有出现
     */
    public static int indexOf(char[] array, char c, int from) {
        if (array == null || from < 0 || from >= array.length) {
            throw new IllegalArgumentException(StringUtils.toString(array) + ", " + c + ", " + from);
        }

        for (int i = from; i < array.length; i++) {
            if (array[i] == c) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 从指定索引位置 from 开始搜索，返回字符串 str 行末最后一个字符的位置字符（回车符或换行符第一次出现的位置，如果没有回车符和换行符就是行末最后一个字符位置）
     *
     * @param str  字符串
     * @param from 搜索起始位置
     * @return 行末最后一个字符所在的位置，不可能等于-1
     */
    public static int indexOfEOL(CharSequence str, int from) {
        if (str == null || from < 0 || (from > 0 && from >= str.length())) {
            throw new IllegalArgumentException(str + ", " + from);
        }
        if (str.length() == 0) {
            return 0;
        }

        for (int i = from; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '\r') {
                int next = i + 1;
                if (next < str.length()) {
                    return str.charAt(next) == '\n' ? next : i;
                } else {
                    return i;
                }
            } else if (c == '\n') {
                return i;
            }
        }
        return str.length() - 1;
    }

    /**
     * 在字符串参数 str 中搜索单引号的结束位置（忽略转义字符右侧的字符）
     *
     * @param str    字符串
     * @param from   单引号的起始位置
     * @param escape true表示忽略转义字符
     * @return -1表示单引号没有出现
     */
    public static int indexOfQuotation(CharSequence str, int from, boolean escape) {
        if (str == null || from < 0 || from >= str.length()) {
            throw new IllegalArgumentException(str + ", " + from + ", " + escape);
        }

        for (int i = from + 1; i < str.length(); i++) {
            char c = str.charAt(i);
            if (escape && c == '\\') { // escape
                i++;
            } else if (c == '\'') {
                return i;
            }
        }
        return -1;
    }

    /**
     * 在字符串参数 str 中搜索双引号结束位置（忽略转义字符右侧的字符）
     *
     * @param str    字符串
     * @param from   双引号的起始位置
     * @param escape true表示忽略转义字符
     * @return -1表示双引号没有出现
     */
    public static int indexOfDoubleQuotation(CharSequence str, int from, boolean escape) {
        if (str == null || from < 0 || from >= str.length()) {
            throw new IllegalArgumentException(str + ", " + from + ", " + escape);
        }

        for (int i = from + 1; i < str.length(); i++) {
            char c = str.charAt(i);
            if (escape && c == '\\') { // escape
                i++;
            } else if (c == '\"') {
                return i;
            }
        }
        return -1;
    }

    /**
     * 搜索小括号的终止位置
     *
     * @param str  字符串
     * @param from 小括号的起始位置
     * @return -1表示小括号没有出现
     */
    public static int indexOfParenthes(CharSequence str, int from) {
        if (str == null || from < 0 || from >= str.length()) {
            throw new IllegalArgumentException(str + ", " + from);
        }

        for (int i = from + 1, count = 1; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '\'') {
                int end = StringUtils.indexOfQuotation(str, i, true);
                if (end == -1) {
                    return -1;
                } else {
                    i = end;
                    continue;
                }
            } else if (c == '(') {
                count++;
            } else if (c == ')') {
                count--;
                if (count == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 在字符串参数 str 中搜索大括号的结束位置
     *
     * @param str    字符串
     * @param from   大括号的起始位置
     * @param length 搜索长度
     * @return -1表示大括号没有出现
     */
    public static int indexOfUnixVariable(CharSequence str, int from, int length) {
        for (int i = from + 1; i < length; i++) {
            char c = str.charAt(i);
            if (c == '{') {
                i = StringUtils.indexOfUnixVariable(str, i + 1, length);
            } else if (c == '}') {
                return i;
            }
        }
        return -1;
    }

    /**
     * 返回字符串数组 array 最后一次出现空白字符串的位置信息
     *
     * @param array 字符串数组
     * @return -1表示空白字符串没有出现
     */
    public static int lastIndexOfNotBlank(CharSequence... array) {
        if (array != null && array.length > 0) {
            for (int i = array.length - 1; i >= 0; i--) {
                if (StringUtils.isNotBlank(array[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 从字符串右端开始向左端搜索第一个不是空白字符的位置
     *
     * @param str  字符串
     * @param from 搜索字符的起始位置
     * @return -1表示非空白字符没有出现
     */
    public static int lastIndexOfNotBlank(CharSequence str, int from) {
        if (str == null) {
            return -1;
        }
        if (from < -1) {
            from = -1;
        }

        for (int i = str.length() - 1; i > from; i--) {
            char c = str.charAt(i);
            if (!Character.isWhitespace(c)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 从指定位置 from 开始从右向左搜索字符串 str，返回第一次出现空白字符的位置
     *
     * @param str  字符串
     * @param from 开始搜索位置
     * @return -1表示空白字符没有出现
     */
    public static int lastIndexOfBlank(CharSequence str, int from) {
        if (str == null || (from < 0 && from != -1) || from >= str.length()) {
            throw new IllegalArgumentException(str + ", " + from);
        }

        for (int i = (from == -1) ? (str.length() - 1) : from; i >= 0; i--) {
            if (Character.isWhitespace(str.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 从字符串参数 str 中指定位置 from 开始到 end 位置为止开始搜索字符串参数 dest，返回字符串参数 dest 在字符串参数 str 中最后一次出现所在的位置
     *
     * @param str        字符串
     * @param dest       搜索字符串
     * @param from       搜索起始位置(包含该点)
     * @param end        搜索终止位置(包含该点)
     * @param ignoreCase true表示忽略大小写
     * @return -1表示字符串 dest 没有出现
     */
    public static int lastIndexOfStr(String str, String dest, int from, int end, boolean ignoreCase) {
        if (str == null || from < 0 || from > str.length() || end < from || end >= str.length()) {
            throw new IllegalArgumentException(dest + ", " + str + ", " + from + ", " + end + ", " + ignoreCase);
        }
        if ((end - from + 1) < dest.length()) {
            return -1;
        }

        for (; (end - from + 1) >= dest.length(); end--) {
            int start = end - dest.length() + 1;
            String sub = str.substring(start, end + 1);
            if (ignoreCase) {
                if (sub.equalsIgnoreCase(dest)) {
                    return start;
                }
            } else {
                if (sub.equals(dest)) {
                    return start;
                }
            }
        }
        return -1;
    }

    /**
     * 把数组拼接为一行字符串
     *
     * @param ite       遍历器
     * @param delimiter 分隔符
     * @return
     */
    public static String join(Iterable<?> ite, String delimiter) {
        if (ite == null) {
            return null;
        }

        StringBuilder buf = new StringBuilder();
        for (Iterator<?> it = ite.iterator(); it.hasNext(); ) {
            buf.append(StringUtils.toString(it.next()));
            if (it.hasNext()) {
                buf.append(delimiter);
            }
        }
        return buf.toString();
    }

    /**
     * 把字符串迭代器 iterator 中字符串拼接为一行字符串
     *
     * @param iterable  集合
     * @param delimiter 分隔符
     * @param escape    转义字符
     * @return
     */
    public static String join(Iterable<String> iterable, String delimiter, char escape) {
        if (iterable == null) {
            return null;
        }

        String del = escape + delimiter; // 对字符串中的分隔符前增加转义字符
        StringBuilder buf = new StringBuilder(delimiter.length() * 10);

        for (Iterator<String> it = iterable.iterator(); it.hasNext(); ) {
            String str = it.next();
            buf.append(StringUtils.replaceAll(str, delimiter, del));
            if (it.hasNext()) {
                buf.append(delimiter);
            }
        }
        return buf.toString();
    }

    /**
     * 在字符串数组参数array中的每个字符串的左右端添加单引号，按顺序逐个遍历数组中的字符串并在每个元素后面添加一个半角逗号分分隔符 <br>
     *
     * @param array 字符串数组
     * @return
     */
    public static String joinUseQuoteComma(CharSequence... array) {
        if (array == null) {
            return null;
        }

        StringBuilder buf = new StringBuilder(array.length * 10);
        for (int i = 0; i < array.length; ) {
            buf.append('\'').append(array[i]).append('\'');
            if (++i < array.length) {
                buf.append(',');
            }
        }
        return buf.toString();
    }

    /**
     * 把字符串src数组用间隔符sep连接起来<br>
     * join({this,is,my,program}, " ") == this is my program
     *
     * @param array     数组
     * @param delimiter 分隔符
     * @return
     */
    public static String join(Object[] array, String delimiter) {
        if (array == null) {
            return null;
        }

        StringBuilder buf = new StringBuilder(array.length * 5);
        for (int i = 0; i < array.length; ) {
            buf.append(array[i]);
            if (++i != array.length) {
                buf.append(delimiter);
            }
        }
        return buf.toString();
    }

    /**
     * 使用字段分隔符参数解析提取字符串参数str中的字段数值, 返回字段数组
     *
     * @param str       字符串
     * @param delimiter 分隔符
     * @return 字段数组
     */
    public static String[] split(CharSequence str, String delimiter) {
        List<String> list = new ArrayList<String>();
        StringUtils.split(str, delimiter, list);
        return list.toArray(new String[list.size()]);
    }

    /**
     * 使用给定的分隔符delimiter 解析提取字符串参数str中的字段, 保存字段到字符串集合参数list
     *
     * @param str       字符串
     * @param delimiter 分隔符
     * @param list      字段集合，用于存储解析后的所有字段
     */
    public static void split(CharSequence str, String delimiter, Collection<String> list) {
        if (str == null) {
            return;
        }
        if (delimiter == null || delimiter.length() == 0) {
            throw new IllegalArgumentException(str + ", " + delimiter + ", " + StringUtils.toString(list));
        }

        int begin = 0;
        int end = StringUtils.indexOf(str, delimiter, 0, false);
        if (end == -1) {
            list.add(str.toString());
            return;
        }

        while (end >= 0) {
            list.add(str.subSequence(begin, end).toString());
            begin = end + delimiter.length();
            end = StringUtils.indexOf(str, delimiter, begin, false);
        }

        list.add(begin < str.length() ? str.subSequence(begin, str.length()).toString() : "");
    }

    /**
     * 使用给定的字段分隔符delimiter 解析提起字符串参数str中的字段, 返回字段数组
     *
     * @param str        字符串
     * @param delimiter  分隔符
     * @param ignoreCase true表示忽略大小写
     * @return
     */
    public static String[] split(CharSequence str, String delimiter, boolean ignoreCase) {
        List<String> list = new ArrayList<String>();
        StringUtils.split(str, delimiter, ignoreCase, list);
        return list.toArray(new String[list.size()]);
    }

    /**
     * 使用给定的字段分隔符集合delimiter 解析提取字符串参数str中的字段, 保存字段到字符串集合参数list
     *
     * @param str        字符串
     * @param delimiter  分隔符
     * @param ignoreCase true表示忽略大小写
     * @param list       字段集合，用于存储解析后的所有字段
     */
    public static void split(CharSequence str, String delimiter, boolean ignoreCase, Collection<String> list) {
        if (str == null) {
            return;
        }
        if (delimiter == null || delimiter.length() == 0) {
            throw new IllegalArgumentException(str + ", " + delimiter + ", " + ignoreCase + ", " + StringUtils.toString(list));
        }

        int begin = 0;
        int end = StringUtils.indexOf(str, delimiter, 0, ignoreCase);
        if (end == -1) {
            list.add(str.toString());
            return;
        }

        while (end >= 0) {
            list.add(str.subSequence(begin, end).toString());
            begin = end + delimiter.length();
            end = StringUtils.indexOf(str, delimiter, begin, ignoreCase);
        }

        list.add(begin < str.length() ? str.subSequence(begin, str.length()).toString() : "");
    }

    /**
     * 使用给定的字段分隔符集合delimiter 解析提取字符串参数str中的字段, 返回字段数组
     *
     * @param str        字符串
     * @param delimiter  分隔符集合
     * @param ignoreCase true表示忽略大小写
     * @return
     */
    public static String[] split(CharSequence str, Collection<String> delimiter, boolean ignoreCase) {
        List<String> list = new ArrayList<String>();
        StringUtils.split(str, delimiter, ignoreCase, list);
        return list.toArray(new String[list.size()]);
    }

    /**
     * 使用给定的字段分隔符集合delimiter 解析提取字符串参数str中的字段，保存字段数值到字符串集合参数list
     *
     * @param str        字符串
     * @param delimiter  分隔符集合
     * @param ignoreCase true表示忽略大小写
     * @param list       字段集合，用于存储解析后的所有字段
     */
    public static void split(CharSequence str, Collection<String> delimiter, boolean ignoreCase, Collection<String> list) {
        if (str == null) {
            return;
        }
        if (delimiter == null || delimiter.isEmpty() || delimiter.contains("") || delimiter.contains(null)) {
            throw new IllegalArgumentException(str + ", " + StringUtils.toString(delimiter) + ", " + ignoreCase + ", " + StringUtils.toString(list));
        }

        int begin = 0; // 字段开始的位置
        for (int i = 0; i < str.length(); i++) {
            for (String del : delimiter) { // 遍历分隔符
                if (ignoreCase ? //
                        (Character.toLowerCase(del.charAt(0)) == Character.toLowerCase(str.charAt(i)) && StringUtils.startsWith(str, del, i, true, false)) //
                        : (del.charAt(0) == str.charAt(i) && StringUtils.startsWith(str, del, i, true, false)) //
                ) {
                    list.add(str.subSequence(begin, i).toString());
                    begin = i + del.length();
                    i = begin;
                    break;
                }
            }
        }
        list.add(str.subSequence(begin, str.length()).toString());
    }

    /**
     * 使用给定的字段分隔符集合delimiter 解析提取字符串参数str中的字段，保存字段数值到字符串集合参数list
     *
     * @param str        字符串
     * @param delimiter  分隔符的集合
     * @param ignoreCase true表示忽略大小写
     * @param list       分隔后字段存储的集合
     * @param delimiters 用来存储字符串中实际的分隔符，与 list 参数对应
     */
    public static void split(CharSequence str, Collection<String> delimiter, boolean ignoreCase, Collection<String> list, Collection<String> delimiters) {
        if (str == null) {
            return;
        }
        if (delimiter == null || delimiter.isEmpty() || delimiter.contains("") || delimiter.contains(null)) {
            throw new IllegalArgumentException(str + ", " + StringUtils.toString(delimiter) + ", " + ignoreCase + ", " + StringUtils.toString(list));
        }

        int index = 0; // 字段开始的位置
        for (int i = 0; i < str.length(); i++) {
            for (String del : delimiter) { // 遍历分隔符
                if (ignoreCase ? //
                        (Character.toLowerCase(del.charAt(0)) == Character.toLowerCase(str.charAt(i)) && StringUtils.startsWith(str, del, i, true, false)) //
                        : (del.charAt(0) == str.charAt(i) && StringUtils.startsWith(str, del, i, true, false)) //
                ) {
                    delimiters.add(del);
                    list.add(str.subSequence(index, i).toString());
                    index = i + del.length();
                    i = index;
                    break;
                }
            }
        }
        list.add(str.subSequence(index, str.length()).toString());
    }

    /**
     * 使用给定的字段分隔符delimiter 解析提取字符串参数str中的字段, 返回字段数组
     *
     * @param str       字符串
     * @param delimiter 字段分隔符
     * @return 字段数组
     */
    public static String[] split(CharSequence str, char delimiter) {
        List<String> list = new ArrayList<String>(10);
        StringUtils.split(str, delimiter, list);
        return list.toArray(new String[list.size()]);
    }

    /**
     * 使用给定的字段分隔符delimiter 解析提取字符串参数str中的字段, 保存字段数值到字符串集合参数list
     *
     * @param str       字符串
     * @param delimiter 字段分隔符
     * @param list      字段集合，用于存储解析后的所有字段
     */
    public static void split(CharSequence str, char delimiter, Collection<String> list) {
        if (str == null) {
            return;
        }

        int begin = 0;
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (str.charAt(i) == delimiter) {
                list.add(str.subSequence(begin, i).toString());
                begin = i + 1;
            }
        }
        list.add(str.subSequence(begin, str.length()).toString());
    }

    /**
     * 使用给定的字段分隔符delimiter 转义字符 escape 解析提取字符串参数str中的字段, 返回字段数组
     *
     * @param str       字符串
     * @param delimiter 字段分隔符
     * @param escape    转义字符
     * @return 字段数组
     */
    public static String[] split(CharSequence str, char delimiter, char escape) {
        List<String> list = new ArrayList<String>(10);
        StringUtils.split(str, delimiter, escape, list);
        return list.toArray(new String[list.size()]);
    }

    /**
     * 使用给定的字段分隔符delimiter 转义字符 escape 解析提取字符串参数str中的字段, 保存字段数值到字符串集合参数list
     *
     * @param str       字符串
     * @param delimiter 字段分隔符
     * @param escape    转义字符
     * @param list      字段集合，用于存储解析后的所有字段
     */
    public static void split(CharSequence str, char delimiter, char escape, Collection<String> list) {
        if (str == null) {
            return;
        }

        int length = str.length();
        StringBuilder buf = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char c = str.charAt(i);
            if (c == escape) {
                int n = i + 1;
                if (n < length) {
                    buf.append(str.charAt(n));
                    i = n;
                }
            } else if (c == delimiter) {
                list.add(buf.toString());
                buf.setLength(0);
            } else {
                buf.append(c);
            }
        }
        list.add(buf.toString());
    }

    /**
     * 使用给定的字段分隔符delimiter 转义字符 escape 解析提取字符串参数str中的字段, 返回字段数组
     *
     * @param str       字符串
     * @param delimiter 分隔符
     * @param escape    转义字符
     * @return 字段数组
     */
    public static String[] split(CharSequence str, String delimiter, char escape) {
        List<String> list = new ArrayList<String>(10);
        StringUtils.split(str, delimiter, escape, list);
        return list.toArray(new String[list.size()]);
    }

    /**
     * 使用给定的字段分隔符delimiter 转义字符 escape 解析提取字符串参数str中的字段, 保存字段数值到字符串集合参数list
     *
     * @param str       字符串
     * @param delimiter 分隔符
     * @param escape    转义字符
     * @param list      字段集合，用于存储解析后的所有字段
     */
    public static void split(CharSequence str, String delimiter, char escape, Collection<String> list) {
        if (str == null) {
            return;
        }
        if (delimiter == null || delimiter.length() == 0) {
            throw new IllegalArgumentException(str + ", " + delimiter + ", " + escape + ", " + StringUtils.toString(list));
        }

        char fc = delimiter.charAt(0);
        int length = str.length();
        StringBuilder buf = new StringBuilder(str.length());
        for (int i = 0; i < length; i++) {
            char c = str.charAt(i);
            if (c == escape) {
                int next = i + 1;
                if (next < length) {
                    buf.append(str.charAt(next)); // 追加转移字符后的内容
                }
                i++;
                continue;
            } else if (c == fc && StringUtils.startsWith(str, delimiter, i, false, false)) {
                list.add(buf.toString());
                buf.setLength(0);

                i += delimiter.length() - 1;
                continue;
            } else {
                buf.append(c);
                continue;
            }
        }
        list.add(buf.toString());
    }

    /**
     * 解析字符串为 key == value
     *
     * @param str 字符串; 格式： key=value
     * @return 字符串数组; 第一位array[0]等于key 第二位array[1]等于value; null表示不符合 key=value 映射规范
     */
    public static String[] splitProperty(CharSequence str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }

        int index = StringUtils.indexOf(str, '=');
        return index == -1 ? null : new String[]{StringUtils.ltrim(str.subSequence(0, index)), str.subSequence(index + 1, str.length()).toString()};
    }

    /**
     * 解析字符串为 key 分隔符 value
     *
     * @param str       字符串, 格式: key分隔符value
     * @param delimiter 分隔符（分隔属性名与属性值）
     * @return 字符串数组, 返回null表示格式错误（在字符串中找不到分隔符）
     */
    public static String[] splitProperty(CharSequence str, char delimiter) {
        if (StringUtils.isBlank(str)) {
            return null;
        }

        int index = StringUtils.indexOf(str, delimiter);
        return index == -1 ? null : new String[]{StringUtils.trimBlank(str.subSequence(0, index)), str.subSequence(index + 1, str.length()).toString()};
    }

    /**
     * 解析字符串为 key == value
     *
     * @param str 字符串; 格式： key=value
     * @return 字符串数组
     * @throws IllegalArgumentException 表达式格式错误
     */
    public static String[] splitPropertyForce(CharSequence str) {
        if (StringUtils.isBlank(str)) {
            throw new IllegalArgumentException(String.valueOf(str));
        }

        int index = StringUtils.indexOf(str, '=');
        if (index == -1) {
            throw new IllegalArgumentException(String.valueOf(str));
        } else {
            return new String[]{StringUtils.ltrim(str.subSequence(0, index)), str.subSequence(index + 1, str.length()).toString()};
        }
    }

    /**
     * 提取字符串中的 key == value 集合
     *
     * @param str       字符串, 格式: key=value; key2=value2;key3=value3; ;
     * @param delimiter key=value 表达式间的分隔符
     * @return key==value 的集合
     */
    public static List<String[]> splitPropertyForce(CharSequence str, String delimiter) {
        String[] array = StringUtils.removeBlank(StringUtils.split(str, delimiter));
        List<String[]> list = new ArrayList<String[]>(array.length);
        for (String express : array) {
            list.add(StringUtils.splitPropertyForce(express));
        }
        return list;
    }

    /**
     * 将字符串str中变量名提取到list
     *
     * @param str  字符串
     * @param list 字段集合，用于存储解析后的所有字段
     * @return 返回 {@code list} 对象
     */
    public static List<String> splitVariable(CharSequence str, List<String> list) {
        if (StringUtils.isBlank(str)) {
            return list;
        }

        int begin = 0;
        while ((begin = StringUtils.indexOf(str, "${", begin, false)) != -1) {
            int end = StringUtils.indexOfUnixVariable(str, begin + 1, str.length());
            if (end == -1) {
                return list;
            }

            String key = str.subSequence(begin + 2, end).toString();
            list.add(key);
            begin = end + 1;
        }
        return list;
    }

    /**
     * 提取字符串str中行（回车符与换行符作为分隔符），并保存到 {@code list}
     *
     * @param str  字符串
     * @param list 字段集合，用于存储解析后的所有字段，为null时会创建一个 #{@linkplain ArrayList} 作为返回值
     * @return 返回 {@code list} 对象的引用
     */
    public static List<CharSequence> splitLines(CharSequence str, List<CharSequence> list) {
        if (list == null) {
            list = new ArrayList<CharSequence>();
        }

        int length = 0;
        if (str == null || (length = str.length()) == 0) {
            list.add("");
            return list;
        }

        int next = 0;
        for (int i = 0; i < length; i++) {
            char c = str.charAt(i);
            switch (c) {
                case '\n':
                    list.add(str.subSequence(next, i));
                    next = i + 1;
                    break;

                case '\r':
                    list.add(str.subSequence(next, i));
                    next = i + 1;
                    if (next < length && str.charAt(next) == '\n') {
                        i++;
                        next = i + 1;
                    }
                    break;
            }
        }

        if (next < length) {
            list.add(str.subSequence(next, length));
        }

        return list;
    }

    /**
     * 解析 java类名
     *
     * @param str Java类名
     * @return
     */
    public static String[] splitJavaName(CharSequence str) {
        List<String> list = new ArrayList<String>();
        StringUtils.splitJavaName(str, list);
        return list.toArray(new String[list.size()]);
    }

    /**
     * 解析 java类名
     *
     * @param str
     * @param list
     */
    public static void splitJavaName(CharSequence str, Collection<String> list) {
        if (str == null) {
            return;
        }
        if (str.length() == 0) {
            list.add(str.toString());
            return;
        }

        int begin = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0) {
                    list.add(str.subSequence(begin, i).toString());
                    begin = i;
                }
                continue;
            }
        }

        if (begin >= 0 && begin < str.length()) {
            list.add(str.subSequence(begin, str.length()).toString());
        }
    }

    /**
     * 解析参数字符串 str，如：commandName 'param1' "param2" (value3)
     *
     * @param str 字符串
     * @return
     */
    public static String[] splitParameters(CharSequence str) {
        ArrayList<String> list = new ArrayList<String>();
        StringUtils.splitParameters(str, list);
        return list.toArray(new String[list.size()]);
    }

    /**
     * 解析参数字符串 str，如：commandName 'param1' "param2" (value3)
     *
     * @param str  字符串
     * @param list 字段存储的集合
     */
    public static void splitParameters(CharSequence str, Collection<String> list) {
        if (str == null) {
            return;
        }

        str = StringUtils.trimBlank(str);
        int begin = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '(') {
                i = StringUtils.indexOfParenthes(str, i);
                if (i == -1) {
                    throw new IllegalArgumentException(String.valueOf(str));
                }
                continue;
            }

            if (c == '\"') {
                i = StringUtils.indexOfDoubleQuotation(str, i, true);
                if (i == -1) {
                    throw new IllegalArgumentException(String.valueOf(str));
                }
                continue;
            }

            if (c == '\'') {
                i = StringUtils.indexOfQuotation(str, i, true);
                if (i == -1) {
                    throw new IllegalArgumentException(String.valueOf(str));
                }
                continue;
            }

            if (Character.isWhitespace(c)) {
                list.add(str.subSequence(begin, i).toString());
                for (int j = i + 1; j < str.length(); j++) {
                    if (Character.isWhitespace(str.charAt(j))) {
                        i++;
                    } else {
                        break;
                    }
                }
                begin = i + 1;
            }
        }

        if (begin < str.length()) {
            list.add(str.subSequence(begin, str.length()).toString());
        }
    }

    /**
     * 使用空白字符作为分隔符，将字符串分隔为多个字段，并将字段保存到 {@code list} 中
     *
     * @param str  字符串
     * @param list 字段集合，用于存储解析后的所有字段
     */
    public static void splitByBlank(CharSequence str, Collection<String> list) {
        if (str == null) {
            return;
        }

        StringBuilder buf = new StringBuilder(str.length());
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            if (Character.isWhitespace(c)) {
                if (buf.length() > 0) {
                    list.add(buf.toString());
                    buf.setLength(0);
                }
                continue;
            } else {
                buf.append(c);
            }
        }

        if (buf.length() > 0) {
            list.add(buf.toString());
            buf.setLength(0);
        }
    }

    /**
     * 使用空白字符作为分隔符，将字符串分隔为多个字段，并将字段保存到 {@code list} 中
     *
     * @param str    字符串
     * @param column 分隔后的字段
     * @return
     */
    public static List<String> splitByBlank(CharSequence str, int column) {
        List<String> list = new ArrayList<String>();
        if (str == null) {
            return list;
        }
        if (column < 1) {
            throw new IllegalArgumentException(String.valueOf(column));
        }

        if (column == 1) {
            list.add(str.toString());
            return list;
        }

        boolean lastCharIsBlank = false;
        int count = column - 1; // 需要分隔的字段个数
        StringBuilder buf = new StringBuilder(str.length());
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            lastCharIsBlank = false;

            if (Character.isWhitespace(c)) {
                if (buf.length() > 0) {
                    if (list.size() == count) {
                        list.add(buf.append(str.subSequence(i, str.length())).toString());
                        return list;
                    } else {
                        list.add(buf.toString());
                        buf.setLength(0);
                    }
                }
                lastCharIsBlank = true;
                continue;
            } else {
                buf.append(c);
            }
        }

        if (buf.length() > 0) {
            list.add(buf.toString());
            buf.setLength(0);
        } else if (lastCharIsBlank) {
            list.add("");
        }
        return list;
    }

    /**
     * 提取字符串中的所有字段数值,包括空白
     *
     * @param str 字符串
     * @return 字段集合，用于存储解析后的所有字段
     */
    public static List<String> splitByBlanks(CharSequence str) {
        List<String> list = new ArrayList<String>();
        if (str == null) {
            return list;
        }

        StringBuilder blank = new StringBuilder(str.length());
        StringBuilder field = new StringBuilder(str.length());
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            if (Character.isWhitespace(c)) {
                blank.append(c);
                if (field.length() > 0) {
                    list.add(field.toString());
                    field.setLength(0);
                }
                continue;
            } else {
                field.append(c);
                if (blank.length() > 0) {
                    list.add(blank.toString());
                    blank.setLength(0);
                }
            }
        }

        if (field.length() > 0) {
            list.add(field.toString());
            field.setLength(0);
        } else {
            if (blank.length() > 0) {
                list.add(blank.toString());
                blank.setLength(0);
            }
        }

        return list;
    }

    /**
     * 使用空白作为字段分隔符解析提取字符串参数str中的字段, 返回字段数组 <br>
     *
     * @param str 字符串
     * @return
     */
    public static String[] splitByBlank(CharSequence str) {
        List<String> list = new ArrayList<String>();
        StringUtils.splitByBlank(str, list);
        return list.toArray(new String[list.size()]);
    }

    /**
     * 将字符串参数str转为字节数组
     *
     * @param str         字符串
     * @param charsetName 字符集
     * @return
     */
    public static byte[] toBytes(CharSequence str, String charsetName) {
        try {
            return str == null ? null : str.toString().getBytes(charsetName);
        } catch (Throwable e) {
            throw new RuntimeException(str + ", " + charsetName, e);
        }
    }

    /**
     * 将输入参数 obj 或参数中的字符串进行大小写转换 <br>
     * 如果输入参数 obj 是字符串则将字符串大小写转换 <br>
     * 如果输入参数 obj 是集合且集合中元素为String类型，将集合中所有字符串进行大小写转行
     *
     * @param <E>
     * @param obj    字符串或集合 <br>
     *               支持的类及数据结构: String, List&lt;String&gt;, Set&lt;String&gt;, Map&lt;String&gt;, char[]
     * @param lower  true表示转为小写字母; false表示转为大写字母
     * @param locale 环境信息
     * @return 字符串或集合参数对象的引用
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <E> E toCase(E obj, boolean lower, Locale locale) {
        if (obj == null) {
            return obj;
        }
        if (locale == null) {
            locale = Locale.getDefault();
        }

        if (String.class.getName().equals(obj.getClass().getName())) {
            return (E) StringUtils.toCaseStr(obj, lower, locale);
        } else if (obj instanceof List) {
            List list = (List) obj;
            for (int i = 0; i < list.size(); i++) {
                if (String.class.getName().equals(list.get(i).getClass().getName())) {
                    list.set(i, StringUtils.toCaseStr(list.get(i), lower, locale));
                }
            }
        } else if (obj instanceof Set) {
            Set set = (Set) obj;
            for (Object value : set) { // 逐个遍历集合中的元素并删除重新添加
                if ((value instanceof String) && set.remove(value)) {
                    set.add(StringUtils.toCaseStr(value, lower, locale));
                }
            }
        } else if (obj instanceof Map) {
            Map map = (Map) obj;
            Set keySet = map.keySet();
            for (Object key : keySet) {
                Object value = map.get(key);
                String newKey = null;
                String newValue = null;

                if (String.class.getName().equals(key.getClass().getName())) {
                    newKey = StringUtils.toCaseStr(key.toString(), lower, locale);
                }
                if (String.class.getName().equals(value.getClass().getName())) {
                    newValue = StringUtils.toCaseStr(value.toString(), lower, locale);
                }

                if (!newKey.equals(key)) {
                    map.remove(key);
                }
                map.put(newKey, newValue);
            }
        } else if (obj.getClass().isArray()) { // 参数对象为数组类型
            Class<?> clazz = obj.getClass().getComponentType();
            if (String.class.getName().equals(clazz.getName())) {
                String[] array = (String[]) obj;
                for (int i = 0; i < array.length; i++) {
                    if (String.class.getName().equals(array[i].getClass().getName())) {
                        array[i] = StringUtils.toCaseStr(array[i], lower, locale);
                    }
                }
                return obj;
            }

            // 基本数据类型
            if ("int".equals(clazz.getName())) {
            } else if ("long".equals(clazz.getName())) {
            } else if ("byte".equals(clazz.getName())) {
            } else if ("short".equals(clazz.getName())) {
            } else if ("boolean".equals(clazz.getName())) {
            } else if ("double".equals(clazz.getName())) {
            } else if ("float".equals(clazz.getName())) {
            } else if ("char".equals(clazz.getName())) {
                char[] array = (char[]) obj;
                for (int i = 0; i < array.length; i++) {
                    array[i] = lower ? Character.toLowerCase(array[i]) : Character.toUpperCase(array[i]);
                }
            }
            return obj;
        }

        return obj;
    }

    /**
     * 将Object 对象强制转换为字符串并执行大小写转换
     *
     * @param obj 字符串
     * @return
     */
    protected static String toCaseStr(Object obj, boolean lower, Locale locale) {
        return obj == null ? null : (lower ? obj.toString().toLowerCase(locale) : obj.toString().toUpperCase(locale));
    }

    /**
     * 将半角字符转为全角字符
     *
     * @param c 半角字符
     * @return
     */
    public static char toFullWidthChar(char c) {
        if (c < 32 || c > 126) {
            throw new IllegalArgumentException(String.valueOf(c));
        } else if (c == 32) {
            return (char) 12288;
        } else {
            return (char) (c + 65248);
        }
    }

    /**
     * 字节数组参数bytes按字符集charsetName转为字符串
     *
     * @param array       字节数组
     * @param charsetName 字符集
     * @return
     */
    public static String toString(byte[] array, String charsetName) {
        try {
            return array == null ? null : new String(array, charsetName);
        } catch (Throwable e) {
            throw new RuntimeException(StringUtils.toBinaryString(array) + ", " + charsetName, e);
        }
    }

    /**
     * 将输入参数 obj 转为字符串
     *
     * @param obj 对象信息
     * @return
     */
    public static String toString(Object obj) {
        if (obj == null) {
            return "null";
        }

        /**
         * yyyy-MM-dd <br>
         * yyyy-MM-dd hh:mm <br>
         * yyyy-MM-dd hh:mm:ss <br>
         * yyyy-MM-dd hh:mm:ss:SSS <br>
         */
        if (obj instanceof Date) {
            Date date = (Date) obj;
            Calendar cr = Calendar.getInstance();
            cr.setTime(date);
            int hour = cr.get(Calendar.HOUR_OF_DAY);
            int minute = cr.get(Calendar.MINUTE);
            int second = cr.get(Calendar.SECOND);
            int mills = cr.get(Calendar.MILLISECOND);
            if (mills > 0) {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(date);
            } else if (second > 0) {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
            } else if (minute > 0 || hour > 0) {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
            } else {
                return new SimpleDateFormat("yyyy-MM-dd").format(date);
            }
        } else if (obj instanceof Throwable) {
            Throwable e = (Throwable) obj;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintStream stream = new PrintStream(out);
            try {
                e.printStackTrace(stream);
            } finally {
                try {
                    stream.close();
                } catch (Exception ex) {
                    log.log(Level.CONFIG, e.getLocalizedMessage(), e);
                }
            }

            StringBuilder buf = new StringBuilder(out.toString());
            if (e instanceof SQLException) {
                SQLException sql = (SQLException) e;
                while (sql != null) {
                    buf.append(System.getProperty("line.separator"));
                    buf.append(sql.getClass().getName());
                    buf.append("[");
                    buf.append("SQLSTATE = ");
                    buf.append(sql.getSQLState());
                    buf.append(", ERRORCODE = ");
                    buf.append(sql.getErrorCode());
                    buf.append(", MESSAGE = ");
                    buf.append(sql.getMessage());
                    buf.append("]");

                    sql = sql.getNextException();
                }
            }
            return buf.toString();
        }

        /**
         * ArrayList[1, 2, ...]
         */
        else if (obj instanceof Iterable<?>) {
            StringBuilder buf = new StringBuilder(20);
            buf.append(obj.getClass().getSimpleName());
            buf.append('[');
            for (Iterator<?> it = ((Iterable<?>) obj).iterator(); it.hasNext(); ) {
                Object next = it.next();
                if (obj == next || obj.equals(next)) {
                    buf.append(next);
                    break;
                } else {
                    buf.append(StringUtils.toString(next));
                }

                if (it.hasNext()) {
                    buf.append(", ");
                }
            }
            return buf.append(']').toString();
        }

        /**
         * HashMap[k1=v1, k2=v2, ...]
         */
        else if (obj instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) obj;
            StringBuilder buf = new StringBuilder(map.size() * 5);
            buf.append(map.getClass().getSimpleName());
            buf.append("[");

            for (Iterator<?> it = map.keySet().iterator(); it.hasNext(); ) {
                Object key = it.next();
                Object val = map.get(key);

                buf.append(key);
                buf.append("=");
                buf.append(obj == val ? val : StringUtils.toString(val));

                if (it.hasNext()) {
                    buf.append(", ");
                }
            }
            buf.append("]");
            return buf.toString();
        }

        /**
         * int[1, 2, ...] <br>
         * String[1, 2, ...]
         */
        else if (obj.getClass().isArray()) {
            try {
                Class<?> clazz = obj.getClass().getComponentType();

                // 基本数据类型数组
                if ("int".equals(clazz.getName())) {
                    return "int" + Arrays.toString((int[]) obj);
                } else if ("long".equals(clazz.getName())) {
                    return "long" + Arrays.toString((long[]) obj);
                } else if ("byte".equals(clazz.getName())) {
                    return "byte" + Arrays.toString((byte[]) obj);
                } else if ("short".equals(clazz.getName())) {
                    return "short" + Arrays.toString((short[]) obj);
                } else if ("boolean".equals(clazz.getName())) {
                    return "boolean" + Arrays.toString((boolean[]) obj);
                } else if ("double".equals(clazz.getName())) {
                    return "double" + Arrays.toString((double[]) obj);
                } else if ("float".equals(clazz.getName())) {
                    return "float" + Arrays.toString((float[]) obj);
                } else if ("char".equals(clazz.getName())) {
                    return "char" + Arrays.toString((char[]) obj);
                }

                // 对象数组
                Object[] array = (Object[]) obj;
                StringBuilder buf = new StringBuilder(array.length * 5);
                buf.append(obj.getClass().getSimpleName().replaceAll("[\\[|\\]|\\;]", ""));
                buf.append("[");
                buf.append(StringUtils.join(array, ", "));
                buf.append("]");
                return buf.toString();
            } catch (ClassCastException e) {
                return obj.toString();
            }
        } else if (obj instanceof Method) {
            Method method = (Method) obj;
            StringBuilder buf = new StringBuilder();
            buf.append(method.getName());
            buf.append("(");

            Class<?>[] clazz = method.getParameterTypes(); // 返回所有参数类型
            for (int j = 0; j < clazz.length; ) {
                buf.append(clazz[j].getSimpleName());
                if (++j < clazz.length) {
                    buf.append(", ");
                }
            }
            buf.append(")");
            return buf.toString();
        } else if (obj instanceof Clob) {
            try {
                Clob clob = (Clob) obj;
                return clob.getSubString((long) 1, (int) clob.length());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return obj.toString();
    }

    /**
     * 将字节数组转为参数radix进制字符串
     *
     * @param bytes 字节数组
     * @param radix 基数可以转换进制的范围，从 Character.MIN_RADIX 到 Character.MAX_RADIX, 超出范围后变为10进制
     * @return
     */
    public static String toRadixString(byte[] bytes, int radix) {
        return new BigInteger(1, bytes).toString(radix);
    }

    /**
     * 将字节数组参数array转为16进制字符串
     *
     * @param array 二进制字节数组
     * @return 16进制字符串（大写）
     */
    public static String toHexString(byte[] array) {
        if (array == null) {
            return null;
        }

        StringBuilder buf = new StringBuilder(array.length * 2);
        for (int i = 0; i < array.length; i++) {
            String str = Integer.toHexString(array[i] & 0xFF);
            if (str.length() < 2) {
                buf.append(0);
            }
            buf.append(str);
        }
        return buf.toString().toUpperCase();
    }

    /**
     * 将字符串参数str转为16进制字符串
     *
     * @param str         字符串
     * @param charsetName 字符集
     * @return
     * @throws IOException
     */
    public static String toHexString(String str, String charsetName) throws IOException {
        if (str == null) {
            return null;
        } else if (StringUtils.isBlank(charsetName)) {
            return StringUtils.toHexString(str.getBytes());
        } else {
            return StringUtils.toHexString(str.getBytes(charsetName));
        }
    }

    /**
     * 将字节数组转为二进制字符串
     *
     * @param bytes 字节数组
     * @return
     */
    public static String toBinaryString(byte[] bytes) {
        return new BigInteger(1, bytes).toString(2);
    }

    /**
     * 将十进制整数转为36进制字符串 <br>
     * 36进制字符串：0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ <br>
     * 如果length等于2，返回从0到99之间的数值，超出从9A开始到ZZ结束 <br>
     *
     * <br>
     * 将36进制字符串转为整数详见方法：{@linkplain #parseHexadecimal(CharSequence)}
     *
     * @param value  大于等于零的十进制整数
     * @param length 结果字符串的长度
     * @return 三十六进制字符串
     */
    public static String toHexadecimalString(int value, int length) {
        if (value < 0 || length <= 0) {
            throw new IllegalArgumentException(value + ", " + length);
        }

        int number = Integer.parseInt(StringUtils.left("", length, '9'));
        if (value <= number) {
            return StringUtils.right(value, length, StandardCharsets.ISO_8859_1.name(), '0');
        } else {
            int val = value - number; // 差额
            int zs = 0; // 整数
            int ys = 0; // 余数
            String result = "";
            for (int i = 1; i <= length; i++) {
                zs = val / 36;
                ys = val % 36;

                int index = ys + 9;
                if (index >= 36) {
                    zs += index / 36;
                    ys = index % 36;
                    index = ys;
                }

                char c = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(index);
                result = c + result;

                if (zs == 0) {
                    break;
                } else {
                    val = zs;
                }
            }

            if (zs > 0) {
                throw new IllegalArgumentException(value + ", " + length);
            }

            return StringUtils.right(result, length, StandardCharsets.ISO_8859_1.name(), '9');
        }
    }

    /**
     * 32-bit UUID
     *
     * @return
     */
    public static String toRandomUUID() {
        String uuid = UUID.randomUUID().toString(); // 0f38fc7a-782d-4747-b6de-ebd0ee782748
        StringBuilder buf = new StringBuilder(32);
        buf.append(uuid, 0, 8);
        buf.append(uuid, 9, 13);
        buf.append(uuid, 14, 18);
        buf.append(uuid, 19, 23);
        buf.append(uuid, 24, 36);
        return buf.toString();
    }

    /**
     * 判断字符数组参数array中是否含有指定字符参数c
     *
     * @param c     字符
     * @param array 字符数组
     * @return
     */
    public static boolean inArray(char c, char... array) {
        if (array == null || array.length == 0) {
            return false;
        }

        for (int i = 0; i < array.length; i++) {
            if (array[i] == c) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断字符数组参数array中是否含有指定字符参数c（忽略大小写）
     *
     * @param c
     * @param array
     * @param ignoreCase
     * @return
     */
    public static boolean inArray(char c, char[] array, boolean ignoreCase) {
        if (array == null) {
            return false;
        }

        if (ignoreCase) {
            c = Character.toLowerCase(c);
        }

        for (int i = 0; i < array.length; i++) {
            if (ignoreCase) {
                if (Character.toLowerCase(array[i]) == c) {
                    return true;
                }
            } else {
                if (array[i] == c) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断字符串数组参数array中是否含有字符串参数str
     *
     * @param str   字符串
     * @param array 字符串数组
     * @return
     */
    public static boolean inArray(CharSequence str, CharSequence... array) {
        if (array == null || str == null) {
            return false;
        }

        for (int i = 0; i < array.length; i++) {
            if (str.equals(array[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断字符串数组参数array中是否含有字符串参数str
     *
     * @param str   字符串
     * @param array 字符串数组
     * @return
     */
    public static boolean inArrayIgnoreCase(String str, String... array) {
        if (array == null || str == null) {
            return false;
        }

        for (int i = 0; i < array.length; i++) {
            if (str.equalsIgnoreCase(array[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断字符串参数 str 是否在集合参数 c 中
     *
     * @param str        字符串
     * @param c          字符串集合
     * @param ignoreCase true表示忽略大小写
     * @return
     */
    public static boolean inCollection(String str, Collection<String> c, boolean ignoreCase) {
        if (c == null) {
            throw new IllegalArgumentException(str + ", " + StringUtils.toString(c) + ", " + ignoreCase);
        }

        if (str == null) {
            for (String s : c) {
                if (s == null) {
                    return true;
                }
            }
        } else {
            for (String s : c) {
                if (ignoreCase) {
                    if (str.equalsIgnoreCase(s)) {
                        return true;
                    }
                } else {
                    if (str.equals(s)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断字符串前缀与字符串prefix相符
     *
     * @param str    字符串
     * @param prefix 字符串
     * @return
     */
    public static boolean startsWithIgnoreCase(CharSequence str, String prefix) {
        if (prefix == null || prefix.length() == 0) {
            throw new IllegalArgumentException(prefix);
        }

        if (str == null || str.length() == 0 || str.length() < prefix.length()) {
            return false;
        } else {
            for (int i = 0; i < prefix.length(); i++) {
                if (Character.toLowerCase(str.charAt(i)) != Character.toLowerCase(prefix.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * 判断从字符串参数 str 指定位置 from 开始的内容是否与参数 prefix 匹配
     *
     * @param str         字符串
     * @param prefix      字符串
     * @param from        搜索字符串str的起始位置（包含起始位置）从0开始
     * @param ignoreCase  true表示忽略字符大小写
     * @param ignoreBlank true表示忽略字符串参数str开始位置begin的空白字符,从非空白字符开始匹配
     * @return true表示匹配
     */
    public static boolean startsWith(CharSequence str, CharSequence prefix, int from, boolean ignoreCase, boolean ignoreBlank) {
        if (str == null || prefix == null || prefix.length() == 0 || from < 0) {
            throw new IllegalArgumentException(str + ", " + prefix + ", " + from + ", " + ignoreCase + ", " + ignoreBlank);
        }

        for (int i = from; i < str.length(); i++) {
            char c = str.charAt(i);

            if (ignoreBlank && Character.isWhitespace(c)) {
                continue;
            }

            int len = str.length() - i;
            if (len < prefix.length()) {
                return false;
            }

            for (int j = 0; j < prefix.length() && i < str.length(); j++, i++) {
                if (ignoreCase) {
                    if (Character.toLowerCase(prefix.charAt(j)) != Character.toLowerCase(str.charAt(i))) {
                        return false;
                    }
                } else {
                    if (prefix.charAt(j) != str.charAt(i)) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 判断字符串参数 str 是否以字符串集合 array 中字符串开头
     *
     * @param str        字符串
     * @param prefix     字符串集合
     * @param ignoreCase true表示忽略大小写
     * @return true表示匹配
     */
    public static boolean startsWith(CharSequence str, Collection<? extends CharSequence> prefix, boolean ignoreCase) {
        if (str == null) {
            return prefix == null;
        }
        if (prefix == null || prefix.isEmpty()) {
            throw new IllegalArgumentException(StringUtils.toString(prefix));
        }

        for (CharSequence cs : prefix) {
            if (StringUtils.startsWith(str, cs, 0, ignoreCase, false)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断从字符串参数 str 指定位置 from 开始的内容是否与参数 prefix 匹配
     *
     * @param str         字符串
     * @param prefix      字符串集合
     * @param from        搜索字符串str的起始位置（包含起始位置）从0开始
     * @param ignoreCase  true表示忽略字符大小写
     * @param ignoreBlank true表示忽略字符串参数str开始位置begin的空白字符,从非空白字符开始匹配
     * @return true表示匹配
     */
    public static boolean startsWith(CharSequence str, Collection<? extends CharSequence> prefix, int from, boolean ignoreCase, boolean ignoreBlank) {
        if (str == null) {
            return prefix == null;
        }
        if (prefix == null || prefix.isEmpty()) {
            throw new IllegalArgumentException(StringUtils.toString(prefix));
        }

        for (CharSequence cs : prefix) {
            if (StringUtils.startsWith(str, cs, from, ignoreCase, ignoreBlank)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 数组a 是否与数组b中指定 bBegin 位置开始匹配
     *
     * @param array      数组a
     * @param from1      数组a匹配的起始位置, 从0开始
     * @param end1       数组a匹配的终止位置（包含当前位置）, 从0开始
     * @param array2     数组b
     * @param from2      数组b匹配的起始位置, 从0开始
     * @param ignoreCase true表示忽略大小写
     * @return true表示匹配
     */
    public static boolean startsWith(String[] array, int from1, int end1, String[] array2, int from2, boolean ignoreCase) {
        if (array == null || array.length == 0 || array2 == null) {
            throw new IllegalArgumentException(StringUtils.toString(array) + ", " + from1 + ", " + end1 + ", " + StringUtils.toString(array2) + ", " + from2 + ", " + ignoreCase);
        }
        if (from1 < 0 || end1 < from1 || from1 > (array.length - 1) || end1 >= array.length || from2 < 0 || from2 >= array2.length) {
            throw new IllegalArgumentException(StringUtils.toString(array) + ", " + from1 + ", " + end1 + ", " + StringUtils.toString(array2) + ", " + from2 + ", " + ignoreCase);
        }
        if (array2.length == 0) {
            return false;
        }

        int len = end1 - from1 + 1;
        if (array2.length - from2 < len) {
            return false;
        }

        for (int i = from1; i <= end1; i++) {
            String str1 = array[i];
            String str2 = array2[from2++];
            if (ignoreCase) {
                if (!str1.equalsIgnoreCase(str2)) {
                    return false;
                }
            } else {
                if (!str1.equals(str2)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 把字符串中第一个字符变成大写字母
     *
     * @param str 字符串
     * @return
     */
    public static String firstCharToUpper(CharSequence str) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return str.toString();
        }

        char c = str.charAt(0);
        if (Character.isLowerCase(c)) {
            c = (char) (c - 32);
        }
        return c + str.subSequence(1, str.length()).toString();
    }

    /**
     * 把字符串中第一个字符变成小写字母
     *
     * @param str 字符串
     * @return
     */
    public static String firstCharToLower(CharSequence str) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return str.toString();
        }

        char c = str.charAt(0);
        if (Character.isUpperCase(c)) {
            c = (char) (c + 32);
        }
        return c + str.subSequence(1, str.length()).toString();
    }

    /**
     * 将字符串从 charsetName1 编码转为 charsetName2 编码
     *
     * @param cs           字符型变量
     * @param charsetName1 字符集1
     * @param charsetName2 字符集2
     * @return
     */
    public static String encodeCharset(CharSequence cs, String charsetName1, String charsetName2) {
        if (StringUtils.isBlank(charsetName1)) {
            throw new IllegalArgumentException(charsetName1);
        }
        if (StringUtils.isBlank(charsetName2)) {
            throw new IllegalArgumentException(charsetName2);
        }
        return cs == null ? null : StringUtils.toString(StringUtils.toBytes(cs.toString(), charsetName1), charsetName2);
    }

    /**
     * 将字符串从 GBK 转为 UTF-8
     *
     * @param cs 字符型变量
     * @return
     */
    public static String encodeGBKtoUTF8(CharSequence cs) {
        return StringUtils.encodeCharset(cs, "GBK", StandardCharsets.UTF_8.name());
    }

    /**
     * 将字符串从 UTF-8 转为 GBK
     *
     * @param cs 字符型变量
     * @return
     */
    public static String encodeUTF8toGBK(CharSequence cs) {
        return StringUtils.encodeCharset(cs, StandardCharsets.UTF_8.name(), "GBK");
    }

    /**
     * 将url字符串中的非ascii码转为16进制字符串
     *
     * @param str
     * @return
     */
    public static String encodeJvmUtf8HexString(CharSequence str) {
        if (str == null) {
            return null;
        }

        StringBuilder encodeStr = new StringBuilder();
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c >= 0 && c <= 255) { // ascii
                if (buf.length() > 0) {
                    encodeStr.append(StringUtils.encodeJvmUtf8HexWord(buf.toString()));
                    buf.setLength(0);
                }

                encodeStr.append(c);
            } else {
                buf.append(c);
            }
        }

        if (buf.length() > 0) {
            encodeStr.append(StringUtils.encodeJvmUtf8HexWord(buf.toString()));
            buf.setLength(0);
        }
        return encodeStr.toString();
    }

    protected static String encodeJvmUtf8HexWord(String str) {
        byte[] bytes = StringUtils.toBytes(str, StandardCharsets.UTF_8.name());
        String hexStr = new BigInteger(1, bytes).toString(16).toLowerCase(); // 将字节数组转为16进制字符串
        if (hexStr.length() % 2 != 0) {
            throw new RuntimeException(str);
        }

        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < hexStr.length(); i++) {
            if ((i + 1) % 2 != 0) {
                buf.append('%');
            }
            buf.append(hexStr.charAt(i));
        }
        return buf.toString();
    }

    /**
     * JVM把非ASCII字符转为十六进制字符串, 对十六进制字符串反向解析生成可读字符串
     *
     * @param str 十六进制字符串
     * @return
     */
    public static String decodeJvmUtf8HexString(CharSequence str) {
        if (str == null) {
            return null;
        }

        StringBuilder buf = new StringBuilder(str.length());
        StringBuilder cb = new StringBuilder();
        for (int i = 0, m = 1, n = 2; i < str.length(); i++, m++, n++) {
            char c = str.charAt(i);

            if (c == '%' //
                    && n < str.length() //
                    && "0123456789abcdef".indexOf(Character.toLowerCase(str.charAt(m))) != -1 //
                    && "0123456789abcdef".indexOf(Character.toLowerCase(str.charAt(n))) != -1 //
            ) {
                cb.append(str.charAt(m));
                cb.append(str.charAt(n));

                i += 2;
                m += 2;
                n += 2;

                continue;
            } else {
                if (cb.length() > 0) {
                    byte[] array = StringUtils.parseHexString(cb.toString());
                    buf.append(StringUtils.toString(array, StandardCharsets.UTF_8.name()));
                    cb.setLength(0);
                }
                buf.append(c);
            }
        }

        if (cb.length() > 0) {
            byte[] array = StringUtils.parseHexString(cb.toString());
            buf.append(StringUtils.toString(array, StandardCharsets.UTF_8.name()));
            cb.setLength(0);
        }

        return buf.toString();
    }

    /**
     * 在字符串参数 str 每行的起始位置添加字符串 prefix
     *
     * @param str    字符串
     * @param prefix 字符串前缀
     * @return
     */
    public static String addLinePrefix(CharSequence str, CharSequence prefix) {
        StringBuilder buf = new StringBuilder(str.length() + prefix.length() * 10);
        buf.append(prefix);

        char[] array = str.toString().toCharArray();
        for (int i = 0; i < array.length; i++) {
            char c = array[i];

            if (c == '\r') {
                buf.append('\r');

                int next = i + 1;
                if (next < array.length) {
                    if (array[next] == '\n') {
                        buf.append('\n');
                        i = next;

                        int nextIndx = next + 1;
                        if (nextIndx < array.length) {
                            buf.append(prefix);
                        }
                    } else {
                        buf.append(prefix);
                    }
                }
            } else if (c == '\n') {
                buf.append('\n');
                int next = i + 1;
                if (next < array.length) {
                    buf.append(prefix);
                }
            } else {
                buf.append(c);
            }
        }
        return buf.toString();
    }

    /**
     * 生成一个字符串
     *
     * @param c    字符
     * @param size 字符的个数
     * @return 字符串
     */
    public static String repeat(char c, int size) {
        char[] array = new char[size];
        for (int i = size - 1; i >= 0; --i) {
            array[i] = c;
        }
        return new String(array);
    }

    /**
     * 移除字符串参数 str 中从 begin 开始到 end 结束的之间的内容
     *
     * @param str   字符串
     * @param begin 删除起始位置
     * @param end   删除终止位置(不包含)
     * @return 移除指定内容后的字符串
     */
    public static String remove(CharSequence str, int begin, int end) {
        if (str == null) {
            return null;
        }
        if (begin == 0 && str.length() == 0) {
            return str.toString();
        }
        if (begin < 0 || begin >= str.length() || end <= begin || end > str.length()) {
            throw new IllegalArgumentException(str + ", " + begin + ", " + end);
        }

        StringBuilder buf = new StringBuilder();
        buf.append(str.subSequence(0, begin));
        if (end < str.length()) {
            buf.append(str.subSequence(end, str.length()));
        }
        return buf.toString();
    }

    /**
     * 删除字符串参数str中的空白字符
     *
     * @param str 字符串
     * @return
     */
    public static String removeBlank(CharSequence str) {
        if (str == null) {
            return null;
        }

        int size = 0;
        int length = str.length();
        char[] array = new char[length];
        for (int i = 0; i < length; i++) {
            char c = str.charAt(i);
            if (!Character.isWhitespace(c)) {
                array[size++] = c;
            }
        }
        return size == length ? str.toString() : new String(array, 0, size);
    }

    /**
     * 删除字符串数组 array 中空白字符串，并删除字符串二端的空白
     *
     * @param array 字符串数组
     * @return 字符串数组副本
     */
    public static String[] removeBlank(CharSequence... array) {
        if (array == null || array.length == 0) {
            return new String[0];
        }

        int size = 0;
        String[] buffer = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            CharSequence str = array[i];
            if (StringUtils.isNotBlank(str)) {
                buffer[size++] = StringUtils.trimBlank(str);
            }
        }

        String[] copy = new String[size];
        System.arraycopy(buffer, 0, copy, 0, size);
        return copy;
    }

    /**
     * 删除字符串参数str左端的一个字符 <br>
     * null或空字符返回输入字符串本身
     *
     * @param str 字符串
     * @return
     */
    public static String removePrefix(CharSequence str) {
        if (str == null) {
            return null;
        } else {
            return str.length() <= 1 ? "" : str.subSequence(1, str.length()).toString();
        }
    }

    /**
     * 从字符串 str 的最左段移除字符串 prefix
     *
     * @param str    字符串
     * @param prefix 字符串
     * @return 移除后的字符串
     */
    public static String removePrefix(CharSequence str, CharSequence prefix) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0 || prefix == null || prefix.length() == 0 || str.length() < prefix.length()) {
            return str.toString();
        }

        int index = prefix.length() - 1;
        for (; index >= 0; index--) {
            if (str.charAt(index) != prefix.charAt(index)) {
                break;
            }
        }
        return index == -1 ? str.subSequence(prefix.length(), str.length()).toString() : str.toString();
    }

    /**
     * 删除字符串参数str右端的一个字符 <br>
     * 输入参数为null时返回null, 输入参数为空字符串时返回空字符
     *
     * @param str 字符串
     * @return
     */
    public static String removeSuffix(CharSequence str) {
        if (str == null) {
            return null;
        } else {
            return str.length() == 0 ? str.toString() : str.subSequence(0, str.length() - 1).toString();
        }
    }

    /**
     * 删除字符串右侧的回车或回车换行符
     *
     * @param str
     * @return
     */
    public static String removeEOL(CharSequence str) {
        if (str == null) {
            return null;
        }

        int last = str.length() - 1;
        if (last >= 0) {
            char c = str.charAt(last);
            if (c == '\n') {
                int before = last - 1;
                if (before >= 0 && str.charAt(before) == '\r') {
                    return str.subSequence(0, before).toString();
                } else {
                    return str.subSequence(0, last).toString();
                }
            } else if (c == '\r') {
                return str.subSequence(0, last).toString();
            }
        }

        return str.toString();
    }

    /**
     * 删除字符串参数 str 中的回车符与换行符
     *
     * @param str 字符串
     * @return
     */
    public static StringBuilder removeLineSeparator(CharSequence str) {
        if (str == null) {
            return null;
        }

        StringBuilder buf = new StringBuilder(str.length());
        for (int j = 0; j < str.length(); j++) {
            char c = str.charAt(j);
            if (c != '\r' && c != '\n') {
                buf.append(c);
            }
        }
        return buf;
    }

    /**
     * 判断字符参数c是否为26个英文字母之一(包括大写与小写)
     *
     * @param c 字符
     * @return
     */
    public static boolean isLetter(char c) {
        return "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(c) != -1;
    }

    /**
     * 判断字符参数c是否为26个英文字母之一(包括大写与小写) <br>
     * 判断字符数组参数array是否全部为数字（0,1,2,3,4,5,6,7,8,9）
     *
     * @param str 字符
     * @return
     */
    public static boolean isLetterOrNumber(CharSequence str) {
        if (str == null) {
            return false;
        }

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if ("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(c) == -1) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断输入字符是否为ASCII中除数据，英文字母，控制字符外的其他字符如： -
     *
     * @param c 字符
     * @return
     */
    public static boolean isSymbol(char c) {
        return ".,;:'\"<>/\\|~`!@#$%^&*()_+={}[]?- ".indexOf(c) != -1;
    }

    /**
     * 判断字符数组参数array是否全部为数字（0,1,2,3,4,5,6,7,8,9）
     *
     * @param array 字符数组
     * @return
     */
    public static boolean isNumber(char... array) {
        if (array == null || array.length == 0) {
            return false;
        }

        for (int i = 0; i < array.length; i++) {
            if ("0123456789".indexOf(array[i]) == -1) {
                return false;
            }
        }
        return true;
    }

    /**
     * 逐个判断字符串 str 中的每个字符，判断是否全部为数字字符
     *
     * @param str 字符串
     * @return
     */
    public static boolean isNumber(CharSequence str) {
        if (str == null || str.length() == 0) {
            return false;
        }

        for (int i = 0; i < str.length(); i++) {
            if ("0123456789".indexOf(str.charAt(i)) == -1) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串参数 str 二端是否有小括号
     *
     * @param str 字符串
     * @return
     */
    public static boolean containsParenthes(CharSequence str) {
        if (str == null || str.length() <= 1) {
            return false;
        } else {
            return str.charAt(0) == '(' && str.charAt(str.length() - 1) == ')';
        }
    }

    /**
     * 判断字符串参数 str 二端是否有单引号
     *
     * @param str 字符串
     * @return
     */
    public static boolean containsQuotation(CharSequence str) {
        if (str == null || str.length() <= 1) {
            return false;
        } else {
            return str.charAt(0) == '\'' && str.charAt(str.length() - 1) == '\'';
        }
    }

    /**
     * 判断字符串参数 str 二端是否有双引号
     *
     * @param str 字符串
     * @return
     */
    public static boolean containsDoubleQuotation(CharSequence str) {
        if (str == null || str.length() <= 1) {
            return false;
        } else {
            return str.charAt(0) == '"' && str.charAt(str.length() - 1) == '"';
        }
    }

    /**
     * 如果字符串 str 为空字符串, 则返回一个默认值 defaultStr
     *
     * @param str        字符串
     * @param defaultStr 默认值
     * @return str 或 defaultStr
     */
    public static String defaultString(CharSequence str, CharSequence defaultStr) {
        if (StringUtils.isBlank(str)) {
            return defaultStr == null ? null : defaultStr.toString();
        } else {
            return str.toString();
        }
    }

    /**
     * 判断字符串参数str转为int对象是否会发生异常
     *
     * @param str 字符串
     * @return
     */
    public static boolean isInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * 判断字符串参数str转为 Double 对象是否会发生异常
     *
     * @param str 字符串
     * @return
     */
    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * 判断字符串参数str转为 Long 对象是否会发生异常
     *
     * @param str 字符串
     * @return
     */
    public static boolean isLong(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * 判断字符串参数str转为 BigDecimal 对象是否会发生异常
     *
     * @param str 字符串
     * @return
     */
    public static boolean isDecimal(String str) {
        try {
            new BigDecimal(str);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * 尝试使用 utf-8，GBK，ISO8859-1 字符集解析显示字符序列集合参数 cs
     *
     * @param chars 字符序列
     */
    public static void testEncoding(CharSequence chars) {
        if (chars == null) {
            log.log(Level.INFO, "null");
            return;
        }

        String str = chars.toString();

        try {
            log.log(Level.INFO, StringUtils.left("original value ", 20, ' ') + " = " + str);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            log.log(Level.INFO, StringUtils.left("GBK -> UTF8 ", 20, ' ') + " = " + StringUtils.encodeCharset(str, "GBK", StandardCharsets.UTF_8.name()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            log.log(Level.INFO, StringUtils.left("UTF8 -> GBK = ", 20, ' ') + " = " + StringUtils.encodeCharset(str, StandardCharsets.UTF_8.name(), "GBK"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            log.log(Level.INFO, StringUtils.left("ISO8859-1 -> GBK ", 20, ' ') + " = " + StringUtils.encodeCharset(str, StandardCharsets.ISO_8859_1.name(), "GBK"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            log.log(Level.INFO, StringUtils.left("GBK -> ISO8859-1 ", 20, ' ') + " = " + StringUtils.encodeCharset(str, "GBK", StandardCharsets.ISO_8859_1.name()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            log.log(Level.INFO, StringUtils.left("ISO8859-1 -> UTF8 ", 20, ' ') + " = " + StringUtils.encodeCharset(str, StandardCharsets.ISO_8859_1.name(), StandardCharsets.UTF_8.name()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            log.log(Level.INFO, StringUtils.left("UTF8 -> ISO8859-1 ", 20, ' ') + " = " + StringUtils.encodeCharset(str, StandardCharsets.UTF_8.name(), StandardCharsets.ISO_8859_1.name()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 把字符串参数str转为整数，如果转换失败返回默认值 defaultVal
     *
     * @param str        字符串
     * @param defaultVal 默认值
     * @return
     */
    public static int parseInt(String str, int defaultVal) {
        try {
            return Integer.parseInt(str);
        } catch (Throwable e) {
            return defaultVal;
        }
    }

    /**
     * 把字符串数组中的所有元素转为整数，如果转换失败则抛出异常信息
     *
     * @param array 字符串数组
     * @return
     */
    public static int[] parseInt(String[] array) {
        if (array == null) {
            throw new NullPointerException();
        }

        int[] numbers = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            numbers[i] = Integer.parseInt(array[i]);
        }
        return numbers;
    }

    /**
     * 将36进制字符串转为整数 <br>
     * <br>
     * 将整数转为36进制字符串详见方法：{@linkplain #toHexadecimalString(int, int)}
     *
     * @param str 字符串
     * @return
     */
    public static int parseHexadecimal(CharSequence str) {
        if (StringUtils.isBlank(str)) {
            throw new IllegalArgumentException("parseHexadecimal(\"" + str + "\")");
        }
        if (StringUtils.isNumber(str)) {
            return Integer.parseInt(str.toString());
        }

        int number = Integer.parseInt(StringUtils.left("", str.length(), '9'));
        for (int index = str.length() - 1, j = 0; index >= 0; index--, j++) {
            char c = str.charAt(index);
            int pos = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(c, 0);
            if (pos == -1) {
                throw new IllegalArgumentException(str + ", character '" + c + "' illegal!");
            }

            number += (pos + 1 - 10) * Math.pow(36, j);
        }
        return number;
    }

    /**
     * 十六进制字符串转为字节数组
     *
     * @param str 十六进制字符串
     * @return
     */
    public static byte[] parseHexString(String str) {
        if (str == null) {
            return null;
        }

        str = str.toUpperCase();
        int length = str.length() / 2;
        char[] array = str.toCharArray();
        byte[] newarray = new byte[length];
        for (int i = 0; i < length; i++) {
            int index = i * 2;
            newarray[i] = (byte) ("0123456789ABCDEF".indexOf(array[index]) << 4 | "0123456789ABCDEF".indexOf(array[index + 1]));
        }
        return newarray;
    }

    /**
     * 使用正则表达式参数 regex 编译字符串参数 str，返回编译后的 Matcher 对象
     *
     * @param str   字符串
     * @param regex 正则表达式
     * @return
     */
    public static Matcher compile(String str, String regex) {
        Pattern compile = Pattern.compile(regex);
        Matcher matcher = compile.matcher(str);
        return matcher.find() ? matcher : null;
    }

}