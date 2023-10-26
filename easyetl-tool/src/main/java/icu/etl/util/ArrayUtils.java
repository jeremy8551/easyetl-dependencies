package icu.etl.util;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * 数组工具
 *
 * @author jeremy8551@qq.com
 * @createtime 2011-05-24
 */
public class ArrayUtils {

    public ArrayUtils() {
    }

    /**
     * 在参数数组 array 中搜索 cls 类首次出现的数组元素
     *
     * @param <E>    元素类型
     * @param array  数组
     * @param cls    目标类信息（可以是接口或抽象类）
     * @param offset 搜索数组的起始位置, 从 0 开始 <br>
     *               如果参数值 cls 是一个具体的类，则返回数组中第一个类信息相等的对象 <br>
     *               如果参数值 cls 是一个接口或抽象类，则返回接口或抽象类的子类
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <E> E indexOf(Object[] array, Class<E> cls, int offset) {
        if (array == null) {
            throw new NullPointerException(String.valueOf(array));
        }
        if (cls == null) {
            throw new NullPointerException(String.valueOf(cls));
        }
        if (offset < 0) {
            throw new IllegalArgumentException(String.valueOf(offset));
        }

        for (int i = offset; i < array.length; i++) {
            Object obj = array[i];
            if (obj != null) {
                if (ClassUtils.equals(obj.getClass(), cls)) { // 类信息相同
                    return (E) obj;
                }
                if ((cls.isInterface() || Modifier.isAbstract(cls.getModifiers())) && cls.isAssignableFrom(obj.getClass())) { // 接口或抽象类的子类
                    return (E) obj;
                }
            }
        }
        return null;
    }

    /**
     * 截取字符串数组
     *
     * @param array  数组
     * @param begin  截取开始位置（从0开始）
     * @param length 截取长度
     * @return 字符串数组副本
     */
    public static String[] subarray(String[] array, int begin, int length) {
        if (begin < 0) {
            throw new IllegalArgumentException(String.valueOf(begin));
        } else if (length < 0) {
            throw new IllegalArgumentException(String.valueOf(length));
        } else if (array == null) {
            return null;
        } else if (length == 0) {
            return new String[0];
        } else {
            int newlen = Math.min(length, array.length);
            String[] newarray = new String[newlen];
            System.arraycopy(array, begin, newarray, 0, newlen);
            return newarray;
        }
    }

    /**
     * 从字符串数组参数array中的删除字符串参数str
     *
     * @param array 字符串数组
     * @param str   字符串
     * @return 字符串数组副本
     */
    public static String[] remove(String[] array, String str) {
        if (array == null || array.length == 0) {
            return array;
        }

        int length = 0;
        String[] newarray = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            String element = array[i];
            if (str != null && str.equals(element)) {
                continue;
            }

            if (str == null && element == null) {
                continue;
            } else {
                newarray[length++] = element;
            }
        }
        return ArrayUtils.subarray(newarray, 0, length);
    }

    /**
     * 从数据参数array中删除重复数据项
     *
     * @param <E>      元素类型
     * @param array    数组
     * @param compator 对比对象(可以为null)
     * @return ArrayList集合
     */
    public static <E> List<E> removeDuplicat(E[] array, Comparator<E> compator) {
        ArrayList<E> list = new ArrayList<E>();
        if (array == null) {
            return list;
        }

        if (compator == null) {
            for (int i = 0; i < array.length; i++) {
                E obj = array[i];
                if (!list.contains(obj)) {
                    list.add(obj);
                }
            }
        } else {
            for (int i = 0; i < array.length; i++) {
                E obj = array[i];
                if (!contain(list, obj, compator)) {
                    list.add(obj);
                }
            }
        }
        return list;
    }

    /**
     * 判断集合参数 ite 中是否含有参数对象 obj
     *
     * @param <E> 元素类型
     * @param ite 集合
     * @param obj
     * @param c   比对对象
     * @return
     */
    private static <E> boolean contain(Iterable<E> ite, E obj, Comparator<E> c) {
        for (Iterator<E> it = ite.iterator(); it.hasNext(); ) {
            if (c.compare(obj, it.next()) == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 删除数组中的重复数据
     *
     * @param array 数组
     * @return
     */
    public static int[] removeDuplicat(int... array) {
        int length = 0;
        int[] newarray = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            int val = array[i];
            boolean exists = false;
            for (int j = 0; j < length; j++) {
                if (val == newarray[j]) {
                    exists = true;
                    break;
                }
            }

            if (exists) {
                continue;
            } else {
                newarray[length++] = val;
            }
        }

        int[] result = new int[length];
        System.arraycopy(newarray, 0, result, 0, length);
        return result;
    }

    /**
     * 判断数组是否为空 <br>
     * isEmpty(null) == true <br>
     * isEmpty({}) == true <br>
     * isEmpty({1,2,3}) == false; <br>
     *
     * @param array 数组
     * @return true表示为 null 或 array.length == 0
     */
    public static <E> boolean isEmpty(E[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断数组是否为空 <br>
     * isEmpty(null) == true <br>
     * isEmpty({}) == true <br>
     * isEmpty({1,2,3}) == false; <br>
     *
     * @param array 数组
     * @return true表示为 null 或 array.length == 0
     */
    public static boolean isEmpty(int[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断数组是否为空 <br>
     * isEmpty(null) == true <br>
     * isEmpty({}) == true <br>
     * isEmpty({1,2,3}) == false; <br>
     *
     * @param array 数组
     * @return true表示为 null 或 array.length == 0
     */
    public static boolean isEmpty(char[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断数组是否为空 <br>
     * isEmpty(null) == true <br>
     * isEmpty({}) == true <br>
     * isEmpty({1,2,3}) == false; <br>
     *
     * @param array 数组
     * @return true表示为 null 或 array.length == 0
     */
    public static boolean isEmpty(byte[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断数组是否为空 <br>
     * isEmpty(null) == true <br>
     * isEmpty({}) == true <br>
     * isEmpty({1,2,3}) == false; <br>
     *
     * @param array 数组
     * @return true表示为 null 或 array.length == 0
     */
    public static boolean isEmpty(double[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断数组是否为空 <br>
     * isEmpty(null) == true <br>
     * isEmpty({}) == true <br>
     * isEmpty({1,2,3}) == false; <br>
     *
     * @param array 数组
     * @return true表示为 null 或 array.length == 0
     */
    public static boolean isEmpty(float[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断数组是否为空 <br>
     * isEmpty(null) == true <br>
     * isEmpty({}) == true <br>
     * isEmpty({1,2,3}) == false; <br>
     *
     * @param array 数组
     * @return true表示为 null 或 array.length == 0
     */
    public static boolean isEmpty(long[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断数组是否为空 <br>
     * isEmpty(null) == true <br>
     * isEmpty({}) == true <br>
     * isEmpty({1,2,3}) == false; <br>
     *
     * @param array 数组
     * @return true表示为 null 或 array.length == 0
     */
    public static boolean isEmpty(short[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 把数组转为List集合，与{@linkplain java.util.Arrays#asList(Object[])} 方法的区别是返回值是一个 ArrayList
     *
     * @param <E>   元素类型
     * @param array 数组
     * @return ArrayList集合
     */
    @SuppressWarnings("unchecked")
    public static <E> ArrayList<E> asList(E... array) {
        if (array == null) {
            return null;
        }

        ArrayList<E> list = new ArrayList<E>(array.length);
        for (int i = 0; i < array.length; i++) {
            list.add(array[i]);
        }
        return list;
    }

    /**
     * 将参数数组转为数组
     *
     * @param array 数组
     * @param <E>   元素类型
     * @return 数组
     */
    public static <E> E[] as(E... array) {
        return array;
    }

    /**
     * 返回数组第一个元素
     *
     * @param <E>   元素类型
     * @param array 数组
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <E> E firstElement(E... array) {
        return array == null || array.length == 0 ? null : array[0];
    }

    /**
     * 返回数组最后一个元素
     *
     * @param <E>   元素类型
     * @param array 数组
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <E> E lastElement(E... array) {
        return array == null || array.length == 0 ? null : array[array.length - 1];
    }

    /**
     * 设置数组第一个元素
     *
     * @param array 数组
     * @param val   参数值
     * @param <E>   元素类型
     */
    public static <E> void setFirstElement(E[] array, E val) {
        if (array != null && array.length > 0) {
            array[0] = val;
        }
    }

    /**
     * 设置数组最后一个元素
     *
     * @param array 数组
     * @param val   参数值
     * @param <E>   元素类型
     */
    public static <E> void setLastElement(E[] array, E val) {
        if (array != null && array.length > 0) {
            array[array.length - 1] = val;
        }
    }

    /**
     * 返回指定位置的元素
     *
     * @param <E>   元素类型
     * @param array 数组
     * @param index 位置,从0开始
     * @return
     */
    public static <E> E elementAt(E[] array, int index) {
        return array != null && index >= 0 && index < array.length ? array[index] : null;
    }

    /**
     * 合并多个数组
     *
     * @param <E>   元素类型
     * @param array 数组
     * @return ArrayList集合
     */
    @SuppressWarnings("unchecked")
    public static <E> List<E> join(E[]... array) {
        ArrayList<E> list = new ArrayList<E>();
        for (int i = 0; i < array.length; i++) {
            E[] a = array[i];
            if (a != null && a.length != 0) {
                for (E obj : a) {
                    list.add(obj);
                }
            }
        }
        return list;
    }

    /**
     * 在数组中插入新数组
     *
     * @param <E>      元素类型
     * @param array1   数组
     * @param position 在 array 数组中的插入点，大于等于零，小于等于 array 数组长度
     * @param array2   插入数组
     * @return 插入合并后的ArrayList集合
     */
    public static <E> List<E> join(E[] array1, int position, E[] array2) {
        boolean e1 = (array1 == null || array1.length == 0);
        boolean e2 = (array2 == null || array2.length == 0);
        if (e1 && e2) {
            return new ArrayList<E>();
        } else if (e1) {
            List<E> list = new ArrayList<E>(array2.length);
            for (E obj : array2) {
                list.add(obj);
            }
            return list;
        } else if (e2) {
            List<E> list = new ArrayList<E>(array1.length);
            for (E obj : array1) {
                list.add(obj);
            }
            return list;
        } else {
            // 从开始位置添加
            boolean add = false;
            int size = array1.length + array2.length;
            ArrayList<E> list = new ArrayList<E>(size);
            if (position < 0) {
                for (E obj : array2) {
                    list.add(obj);
                }
                add = true;
            }

            // 从指定位置开始添加
            for (int i = 0; i < array1.length; i++) {
                if (!add && i == position) { // 在指定位置插入数组
                    for (E obj : array2) {
                        list.add(obj);
                    }
                    add = true;
                }
                list.add(array1[i]);
            }

            // 在数组右侧添加
            if (!add) {
                for (E obj : array2) {
                    list.add(obj);
                }
            }
            return list;
        }
    }

    /**
     * 复制字符串数组
     *
     * @param array  字符串数组
     * @param length 新数组长度
     * @return 字符串数组副本
     */
    public static String[] copyOf(String[] array, int length) {
        if (array == null) {
            return array;
        } else {
            String[] newarray = new String[length];
            System.arraycopy(array, 0, newarray, 0, Math.min(array.length, length));
            return newarray;
        }
    }

    /**
     * 判断数组元素是否逐位相等(equals 函数) <br>
     * equalsElement(null, {}) = true <br>
     * equalsElement(null, null) = true <br>
     * equalsElement({}, {}) = true <br>
     *
     * @param <E> 元素类型
     * @param a1  数组1
     * @param a2  数组2
     * @param c   数组元素比较规则，如果为 null 则使用 {@linkplain Object#equals(Object)} 方法比较
     * @return true表示数组中元素相等
     */
    public static <E> boolean equals(E[] a1, E[] a2, Comparator<E> c) {
        boolean e1 = (a1 == null || a1.length == 0);
        boolean e2 = (a2 == null || a2.length == 0);
        if (e1 && e2) {
            return true;
        } else if (e1 || e2) {
            return false;
        } else if (a1.length != a2.length) {
            return true;
        } else {
            for (int i = 0; i < a1.length; i++) {
                E o1 = a1[i];
                E o2 = a2[i];
                boolean p1 = (o1 == null);
                boolean p2 = (o2 == null);
                if (p1 && p2) { // 全为null
                    return true;
                } else if (p1 || p2) { // 部分为null
                    return false;
                } else if (c == null) { // 未设置规则
                    if (!o1.equals(o2)) {
                        return false;
                    }
                } else { // 使用规则
                    if (c.compare(o1, o2) != 0) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    /**
     * 将数组中的第一个元素开始，整体向右移动一位
     *
     * @param positions 数组
     * @return 移动位置后的新数组
     */
    public static int[] shift(int[] positions) {
        int[] newarray = new int[positions.length + 1];
        System.arraycopy(positions, 0, newarray, 1, positions.length);
        return newarray;
    }

}
