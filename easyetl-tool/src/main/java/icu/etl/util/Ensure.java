package icu.etl.util;

import java.util.Arrays;

/**
 * 断言工具
 *
 * @author jeremy8551@qq.com
 * @createtime 2023-09-15
 */
public class Ensure {

    /**
     * 参数不能是空白的字符序列
     *
     * @param value 字符串
     */
    public static <T> T notBlank(T value) {
        if (!(value instanceof CharSequence)) {
            throw new IllegalArgumentException(value.getClass().getName());
        }
        if (StringUtils.isBlank((CharSequence) value)) {
            throw new IllegalArgumentException(String.valueOf(value));
        }
        return value;
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
            throw new IllegalArgumentException(array.length == 0 ? "" : Arrays.toString(array));
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
            throw new IllegalArgumentException(value1 + " != " + value2);
        } else if (value1 == value2 || value1.equals(value2)) {
            return;
        } else {
            throw new IllegalArgumentException(value1 + " != " + value2);
        }
    }

    /**
     * 检查参数 {@code value} 是否在数组 {@code array} 中
     *
     * @param value 字符串
     * @param array 字符串数组
     */
    public static void exists(String value, String... array) {
        if (value == null || array == null || array.length == 0 || !StringUtils.inArrayIgnoreCase(value, array)) {
            throw new IllegalArgumentException(value + " != " + Arrays.toString(array));
        }
    }

    /**
     * 检查参数是否为大于等于零的正整数
     *
     * @param value 整数
     */
    public static long isFromZero(long value) {
        if (value < 0) {
            throw new IllegalArgumentException(String.valueOf(value));
        }
        return value;
    }

    /**
     * 检查参数是否为大于等于零的正整数
     *
     * @param value 整数
     */
    public static int isFromZero(int value) {
        if (value < 0) {
            throw new IllegalArgumentException(String.valueOf(value));
        }
        return value;
    }

    /**
     * 检查参数是否为大于零的正整数
     *
     * @param value 数值
     * @return 数值
     */
    public static long isFromOne(long value) {
        if (value <= 0) {
            throw new IllegalArgumentException(String.valueOf(value));
        }
        return value;
    }

    /**
     * 检查参数是否为大于零的正整数
     *
     * @param value 数值
     * @return 数值
     */
    public static int isFromOne(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException(String.valueOf(value));
        }
        return value;
    }

    /**
     * 检查参数是否为null
     *
     * @param <E> 类型
     * @param obj 对象
     * @return 对象
     */
    public static <E> E notNull(E obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return obj;
    }

    /**
     * 断言数组中元素
     *
     * @param array 数组
     * @param <E>   元素类型
     * @return 数组
     */
    public static <E> E[] notEmpty(E[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException(Arrays.toString(array));
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
    public static <E> E onlyOne(E[] array) {
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
