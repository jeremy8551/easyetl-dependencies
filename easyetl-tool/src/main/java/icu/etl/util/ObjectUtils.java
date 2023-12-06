package icu.etl.util;

/**
 * Object帮助类
 *
 * @author jeremy8551@qq.com
 * @createtime 2011-11-02
 */
public class ObjectUtils {

    /**
     * 如果参数 {@code obj} 不为null，则返回参数本身，否则返回参数 {@code def}
     *
     * @param obj 对象
     * @param def 默认值
     * @param <E> 对象类型
     * @return 对象或默认值
     */
    public static <E> E coalesce(E obj, E def) {
        return obj == null ? def : obj;
    }

    /**
     * 确定对象是否是JAVA的基本类型：
     * String
     * int
     * byte
     * short
     * long
     * float
     * double
     * char
     * boolean
     *
     * @param obj 对象
     * @return 返回true表示参数是JAVA的基本类型
     */
    public static boolean isJavaBasicType(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof String) {
            return true;
        }
        if (obj instanceof Integer) {
            return true;
        }
        if (obj instanceof Byte) {
            return true;
        }
        if (obj instanceof Short) {
            return true;
        }
        if (obj instanceof Long) {
            return true;
        }
        if (obj instanceof Float) {
            return true;
        }
        if (obj instanceof Double) {
            return true;
        }
        if (obj instanceof Character) {
            return true;
        }
        if (obj instanceof Boolean) {
            return true;
        }
        return false;
    }

}
