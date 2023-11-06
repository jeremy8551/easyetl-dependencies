package icu.etl.util;

/**
 * 断言工具类
 *
 * @author jeremy8551@qq.com
 * @createtime 2023-09-15
 */
public class Ensure {

    /**
     * 断言参数 {@code cs} 不能为空白
     *
     * @param cs 字符串
     */
    @SuppressWarnings("unchecked")
    public static <T> T notBlank(CharSequence cs) {
        if (StringUtils.isBlank(cs)) {
            throw new UnsupportedOperationException(String.valueOf(cs));
        } else {
            return (T) cs;
        }
    }

    /**
     * 检查参数是否是 true，此方法主要用于在方法和构造函数中进行参数验证
     *
     * @param value 布尔参数
     * @param array 异常信息
     * @return 返回true
     */
    public static boolean isTrue(boolean value, Object... array) {
        if (value) {
            return value;
        } else {
            throw new IllegalArgumentException(array.length == 0 ? String.valueOf(value) : StringUtils.toString(array));
        }
    }

    /**
     * 检查参数是否是整数，此方法主要用于在方法和构造函数中进行参数验证
     *
     * @param str 字符串
     * @return 字符串参数转为整数
     */
    public static int isInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Throwable e) {
            throw new IllegalArgumentException(str, e);
        }
    }

    /**
     * 检查参数是否相等（都为null或 equals 方法返回true 表示相等）
     *
     * @param value1 参数
     * @param value2 参数
     */
    public static void equals(Object value1, Object value2) {
        boolean b1 = value1 == null;
        boolean b2 = value2 == null;
        if (b1 && b2) {
            return;
        } else if (b1 || b2) {
            throw new RuntimeException(StringUtils.toString(value1) + " != " + StringUtils.toString(value2));
        } else if (value1 == value2 || value1.equals(value2)) {
            return;
        } else {
            throw new RuntimeException(StringUtils.toString(value1) + " != " + StringUtils.toString(value2));
        }
    }

    /**
     * 检查参数是否相等
     *
     * @param value 字符串
     * @param array 字符串数组
     */
    public static void exists(String value, String... array) {
        if (value == null || array == null || array.length == 0 || !StringUtils.inArrayIgnoreCase(value, array)) {
            throw new RuntimeException(StringUtils.toString(value) + " != " + StringUtils.toString(array));
        }
    }

    /**
     * 检查参数是否为大于等于零的正整数
     *
     * @param value 整数
     */
    public static long isPosition(long value) {
        if (value < 0) {
            throw new IllegalArgumentException(String.valueOf(value));
        } else {
            return value;
        }
    }

    /**
     * 检查参数是否为大于零的正整数
     *
     * @param value
     * @return
     */
    public static long isIndex(long value) {
        if (value <= 0) {
            throw new IllegalArgumentException(String.valueOf(value));
        } else {
            return value;
        }
    }

    /**
     * 检查参数是否为null
     *
     * @param <E>
     * @param obj
     * @param array 异常信息
     * @return
     */
    public static <E> E notnull(E obj, Object... array) {
        if (obj == null) {
            throw new NullPointerException(StringUtils.toString(array.length == 0 ? obj : array));
        } else {
            return obj;
        }
    }

    /**
     * 断言数组中元素
     *
     * @param array 数组
     * @param <E>   元素类型
     * @return 数组
     */
    public static <E> E[] notempty(E[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException(String.valueOf(array));
        } else {
            return array;
        }
    }

    /**
     * 断言数组中最多只能有一个元素
     *
     * @param array 数组
     * @param <E>   元素类型
     * @return 第一个元素
     */
    public static <E> E onlyone(E[] array) {
        if (array == null) {
            throw new NullPointerException();
        }

        switch (array.length) {
            case 0:
                return null;
            case 1:
                return array[0];
            default:
                throw new IllegalArgumentException(String.valueOf(array.length));
        }
    }

}
