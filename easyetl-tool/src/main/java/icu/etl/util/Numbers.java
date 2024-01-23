package icu.etl.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;

/**
 * 数值工具
 *
 * @author jeremy8551@qq.com
 * @createtime 2010-01-08
 */
public class Numbers {

    public Numbers() {
    }

    /**
     * 对 BigDecimal 进行四舍五入
     *
     * @param value 参数值
     * @return 四舍五入后的整数
     */
    public static Integer floorDecimal2int(BigDecimal value) {
        return value == null ? null : value.setScale(0, RoundingMode.HALF_UP).intValue();
    }

    /**
     * 求最大值
     *
     * @param array 整数数组
     * @return 最大值
     */
    public static int max(int... array) {
        if (array.length == 0) {
            throw new IllegalArgumentException(String.valueOf(array));
        }

        int max = array[0];
        for (int value : array) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    /**
     * 求和
     *
     * @param array 整数数组
     * @return 数组中元素之和
     */
    public static int sum(int... array) {
        int sum = 0;
        for (int i = 0; i < array.length; i++) {
            sum += array[i];
        }
        return sum;
    }

    /**
     * 求和
     *
     * @param list 整数集合
     * @return 集合中元素之和
     */
    public static int sum(List<Integer> list) {
        int sum = 0;
        for (int i = 0; i < list.size(); i++) {
            Integer number = list.get(i);
            if (number != null) {
                sum += number;
            }
        }
        return sum;
    }

    /**
     * 取得数字位数（不含符号位）
     * 输入0 返回1
     * 输入100 返回3
     * 输入-100 返回3
     *
     * @param number 数字
     * @return 参数的位数
     */
    public static int digit(Integer number) {
        if (number == null) {
            return 0;
        } else {
            int val = number.intValue();
            return Integer.toString(val < 0 ? 0 - val : val).length();
        }
    }

    /**
     * 生成随机数 1到10之间
     *
     * @return 1到10之间
     */
    public static int getRandom() {
        int s = 5;
        double r = Math.random() * 10;
        if (r <= 0) {
            return s;
        }

        if (r > 0 && r < 10) {
            s = Double.valueOf(r).intValue();
            if (s == 0) {
                s = 3;
            }
            return s;
        }

        if (r >= 10) {
            s = 10;
            return s;
        }

        return s;
    }

    /**
     * 判断整数是否在数组中存在
     *
     * @param number 整数
     * @param array  数组
     * @return 返回true表示整数包含在数组中 false表示不存在
     */
    public static boolean inArray(int number, int... array) {
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                if (number == array[i]) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 对整形数组中所有元素都加一个参数number
     *
     * @param array  整数数组
     * @param number 整数
     * @return 返回 array 数组本身
     */
    public static int[] plus(int[] array, int number) {
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                array[i] = array[i] + number;
            }
        }
        return array;
    }

    /**
     * 返回 i1 + i2 结果
     *
     * @param i1 数值
     * @param i2 数值
     * @return 参数之和
     */
    public static Integer plus(Integer i1, Integer i2) {
        if (i1 == null) {
            return i2;
        }
        if (i2 == null) {
            return i1;
        }

        return Integer.valueOf(i1.intValue() + i2.intValue());
    }

    /**
     * 返回 i1 + i2 结果
     *
     * @param i1 数值
     * @param i2 数值
     * @return 参数之和
     */
    public static BigInteger plus(BigInteger i1, BigInteger i2) {
        if (i1 == null) {
            return i2;
        }
        if (i2 == null) {
            return i1;
        }

        return i1.add(i2);
    }

    /**
     * 返回 i1 + i2 结果
     *
     * @param i1 数值
     * @param i2 数值
     * @return 参数之和
     */
    public static BigDecimal plus(BigDecimal i1, BigDecimal i2) {
        if (i1 == null) {
            return i2;
        }
        if (i2 == null) {
            return i1;
        }

        return i1.add(i2);
    }

    /**
     * 返回 i1 + i2 结果
     *
     * @param i1 数值
     * @param i2 数值
     * @return 参数之和
     */
    public static Double plus(Double i1, Double i2) {
        if (i1 == null) {
            return i2;
        }
        if (i2 == null) {
            return i1;
        }

        return BigDecimal.valueOf(i1).add(BigDecimal.valueOf(i2)).doubleValue();
    }

    /**
     * 返回 i1 + i2 结果
     *
     * @param i1 数值
     * @param i2 数值
     * @return 参数之和
     */
    public static Long plus(Long i1, Long i2) {
        if (i1 == null) {
            return i2;
        }
        if (i2 == null) {
            return i1;
        }

        return Long.valueOf(i1.longValue() + i2.longValue());
    }

    /**
     * 返回 i1 + i2 结果
     *
     * @param i1 数值
     * @param i2 数值
     * @return 参数之和
     */
    public static Short plus(Short i1, Short i2) {
        if (i1 == null) {
            return i2;
        }
        if (i2 == null) {
            return i1;
        }

        return Short.valueOf((short) (i1.shortValue() + i2.shortValue()));
    }

    /**
     * 返回 i1 + i2 结果
     *
     * @param i1 数值
     * @param i2 数值
     * @return 参数之和
     */
    public static Float plus(Float i1, Float i2) {
        if (i1 == null) {
            return i2;
        }
        if (i2 == null) {
            return i1;
        }
        return Float.valueOf(i1.floatValue() + i2.floatValue());
    }

    /**
     * 返回 i1 - i2 结果
     *
     * @param i1 数值
     * @param i2 数值
     * @return 参数之差
     */
    public static Integer subtract(Integer i1, Integer i2) {
        if (i1 == null) {
            return i2;
        }
        if (i2 == null) {
            return i1;
        }

        return Integer.valueOf(i1.intValue() - i2.intValue());
    }

    /**
     * 返回 i1 - i2 结果
     *
     * @param i1 数值
     * @param i2 数值
     * @return 参数之差
     */
    public static BigInteger subtract(BigInteger i1, BigInteger i2) {
        if (i1 == null) {
            return i2;
        }
        if (i2 == null) {
            return i1;
        }

        return i1.subtract(i2);
    }

    /**
     * 返回 i1 - i2 结果
     *
     * @param i1 数值
     * @param i2 数值
     * @return 参数之差
     */
    public static BigDecimal subtract(BigDecimal i1, BigDecimal i2) {
        if (i1 == null) {
            return i2;
        }
        if (i2 == null) {
            return i1;
        }

        return i1.subtract(i2);
    }

    /**
     * 返回 i1 - i2 结果
     *
     * @param i1 数值
     * @param i2 数值
     * @return 参数之差
     */
    public static Double subtract(Double i1, Double i2) {
        if (i1 == null) {
            return i2;
        }
        if (i2 == null) {
            return i1;
        }

        return BigDecimal.valueOf(i1).subtract(BigDecimal.valueOf(i2)).doubleValue();
    }

    /**
     * 返回 i1 - i2 结果
     *
     * @param i1 数值
     * @param i2 数值
     * @return 参数之差
     */
    public static Long subtract(Long i1, Long i2) {
        if (i1 == null) {
            return i2;
        }
        if (i2 == null) {
            return i1;
        }

        return Long.valueOf(i1.longValue() - i2.longValue());
    }

    /**
     * 返回 i1 - i2 结果
     *
     * @param i1 数值
     * @param i2 数值
     * @return 参数之差
     */
    public static Short subtract(Short i1, Short i2) {
        if (i1 == null) {
            return i2;
        }
        if (i2 == null) {
            return i1;
        }

        return Short.valueOf((short) (i1.shortValue() - i2.shortValue()));
    }

    /**
     * 返回 i1 - i2 结果
     *
     * @param i1 数值
     * @param i2 数值
     * @return 参数之差
     */
    public static Float subtract(Float i1, Float i2) {
        if (i1 == null) {
            return i2;
        }
        if (i2 == null) {
            return i1;
        }
        return Float.valueOf(i1.floatValue() - i2.floatValue());
    }

    /**
     * 返回 i1 乘 i2 结果
     *
     * @param i1 数值
     * @param i2 数值
     * @return 乘基
     */
    public static Integer multiply(Integer i1, Integer i2) {
        if (i1 == null) {
            return i2;
        }
        if (i2 == null) {
            return i1;
        }

        return Integer.valueOf(i1.intValue() * i2.intValue());
    }

    /**
     * 返回 i1 乘 i2 结果
     *
     * @param i1 数值
     * @param i2 数值
     * @return 乘积
     */
    public static BigInteger multiply(BigInteger i1, BigInteger i2) {
        if (i1 == null) {
            return i2;
        }
        if (i2 == null) {
            return i1;
        }

        return i1.multiply(i2);
    }

    /**
     * 返回 i1 乘 i2 结果
     *
     * @param i1 数值
     * @param i2 数值
     * @return 乘积
     */
    public static BigDecimal multiply(BigDecimal i1, BigDecimal i2) {
        if (i1 == null) {
            return i2;
        }
        if (i2 == null) {
            return i1;
        }

        return i1.multiply(i2);
    }

    /**
     * 返回 i1 乘 i2 结果
     *
     * @param i1 数值
     * @param i2 数值
     * @return 乘积
     */
    public static Double multiply(Double i1, Double i2) {
        if (i1 == null) {
            return i2;
        }
        if (i2 == null) {
            return i1;
        }

        return BigDecimal.valueOf(i1).multiply(BigDecimal.valueOf(i2)).doubleValue();
    }

    /**
     * 返回 i1 乘 i2 结果
     *
     * @param i1 数值
     * @param i2 数值
     * @return 乘积
     */
    public static Long multiply(Long i1, Long i2) {
        if (i1 == null) {
            return i2;
        }
        if (i2 == null) {
            return i1;
        }

        return Long.valueOf(i1.longValue() * i2.longValue());
    }

    /**
     * 返回 i1 乘 i2 结果
     *
     * @param i1 数值
     * @param i2 数值
     * @return 乘积
     */
    public static Short multiply(Short i1, Short i2) {
        if (i1 == null) {
            return i2;
        }
        if (i2 == null) {
            return i1;
        }

        return Short.valueOf((short) (i1.shortValue() * i2.shortValue()));
    }

    /**
     * 返回 i1 乘 i2 结果
     *
     * @param i1 数值
     * @param i2 数值
     * @return 乘积
     */
    public static Float multiply(Float i1, Float i2) {
        if (i1 == null) {
            return i2;
        }
        if (i2 == null) {
            return i1;
        }
        return Float.valueOf(i1.floatValue() * i2.floatValue());
    }

    /**
     * 返回 i1 除 i2 结果
     *
     * @param i1 数值
     * @param i2 数值
     * @return 余数
     */
    public static Integer divide(Integer i1, Integer i2) {
        if (i1 == null) {
            return i2;
        }
        if (i2 == null) {
            return i1;
        }

        return Integer.valueOf(i1.intValue() / i2.intValue());
    }

    /**
     * 返回 i1 除 i2 结果
     *
     * @param i1 数值
     * @param i2 数值
     * @return 余数
     */
    public static BigInteger divide(BigInteger i1, BigInteger i2) {
        if (i1 == null) {
            return i2;
        }
        if (i2 == null) {
            return i1;
        }

        return i1.divide(i2);
    }

    /**
     * 返回 i1 除 i2 结果
     *
     * @param i1 数值
     * @param i2 数值
     * @return 余数
     */
    public static BigDecimal divide(BigDecimal i1, BigDecimal i2) {
        if (i1 == null) {
            return i2;
        }
        if (i2 == null) {
            return i1;
        }

        return i1.divide(i2);
    }

    /**
     * 返回 i1 除 i2 结果
     *
     * @param i1 数值
     * @param i2 数值
     * @return 余数
     */
    public static Double divide(Double i1, Double i2) {
        if (i1 == null) {
            return i2;
        }
        if (i2 == null) {
            return i1;
        }

        return BigDecimal.valueOf(i1).divide(BigDecimal.valueOf(i2)).doubleValue();
    }

    /**
     * 返回 i1 除 i2 结果
     *
     * @param i1 数值
     * @param i2 数值
     * @return 余数
     */
    public static Long divide(Long i1, Long i2) {
        if (i1 == null) {
            return i2;
        }
        if (i2 == null) {
            return i1;
        }

        return Long.valueOf(i1.longValue() / i2.longValue());
    }

    /**
     * 返回 i1 除 i2 结果
     *
     * @param i1 数值
     * @param i2 数值
     * @return 余数
     */
    public static Short divide(Short i1, Short i2) {
        if (i1 == null) {
            return i2;
        }
        if (i2 == null) {
            return i1;
        }

        return Short.valueOf((short) (i1.shortValue() / i2.shortValue()));
    }

    /**
     * 返回 i1 除 i2 结果
     *
     * @param i1 数值
     * @param i2 数值
     * @return 余数
     */
    public static Float divide(Float i1, Float i2) {
        if (i1 == null) {
            return i2;
        }
        if (i2 == null) {
            return i1;
        }
        return Float.valueOf(i1.floatValue() / i2.floatValue());
    }

    /**
     * 返回 i1 取余 i2 结果
     *
     * @param i1 数值
     * @param i2 数值
     * @return 余数
     */
    public static Integer mod(Integer i1, Integer i2) {
        if (i1 == null) {
            return i2;
        }
        if (i2 == null) {
            return i1;
        }

        return Integer.valueOf(i1.intValue() % i2.intValue());
    }

    /**
     * 返回 i1 取余 i2 结果
     *
     * @param i1 数值
     * @param i2 数值
     * @return 余数
     */
    public static BigInteger mod(BigInteger i1, BigInteger i2) {
        if (i1 == null) {
            return i2;
        }
        if (i2 == null) {
            return i1;
        }

        return i1.mod(i2);
    }

    /**
     * 返回 i1 取余 i2 结果
     *
     * @param i1 数值
     * @param i2 数值
     * @return 余数
     */
    public static Double mod(Double i1, Double i2) {
        if (i1 == null) {
            return i2;
        }
        if (i2 == null) {
            return i1;
        }

        return BigDecimal.valueOf(i1).divideAndRemainder(BigDecimal.valueOf(i2))[1].doubleValue();
    }

    /**
     * 返回 i1 取余 i2 结果
     *
     * @param i1 数值
     * @param i2 数值
     * @return 余数
     */
    public static Long mod(Long i1, Long i2) {
        if (i1 == null) {
            return i2;
        }
        if (i2 == null) {
            return i1;
        }

        return Long.valueOf(i1.longValue() % i2.longValue());
    }

    /**
     * 返回 i1 取余 i2 结果
     *
     * @param i1 数值
     * @param i2 数值
     * @return 余数
     */
    public static Short mod(Short i1, Short i2) {
        if (i1 == null) {
            return i2;
        }
        if (i2 == null) {
            return i1;
        }

        return Short.valueOf((short) (i1.shortValue() % i2.shortValue()));
    }

    /**
     * 返回 i1 取余 i2 结果
     *
     * @param i1 数值
     * @param i2 数值
     * @return 余数
     */
    public static Float mod(Float i1, Float i2) {
        if (i1 == null) {
            return i2;
        }
        if (i2 == null) {
            return i1;
        }
        return Float.valueOf(i1.floatValue() % i2.floatValue());
    }

    /**
     * i1 是否大于 i2
     *
     * @param i1 数值
     * @param i2 数值
     * @return true表示 i1 大于 i2
     */
    public static boolean greater(Integer i1, Integer i2) {
        if (i1 == null && i2 == null) {
            return false;
        }
        if (i1 == null && i2 != null) {
            return false;
        }
        if (i1 != null && i2 == null) {
            return true;
        }

        return i1.intValue() > i2.intValue();
    }

    /**
     * i1 是否大于 i2
     *
     * @param i1 数值
     * @param i2 数值
     * @return true表示 i1 大于 i2
     */
    public static boolean greater(BigInteger i1, BigInteger i2) {
        if (i1 == null && i2 == null) {
            return false;
        }
        if (i1 == null && i2 != null) {
            return false;
        }
        if (i1 != null && i2 == null) {
            return true;
        }

        return i1.compareTo(i2) > 0;
    }

    /**
     * i1 是否大于 i2
     *
     * @param i1 数值
     * @param i2 数值
     * @return true表示 i1 大于 i2
     */
    public static boolean greater(BigDecimal i1, BigDecimal i2) {
        if (i1 == null && i2 == null) {
            return false;
        }
        if (i1 == null && i2 != null) {
            return false;
        }
        if (i1 != null && i2 == null) {
            return true;
        }

        return i1.compareTo(i2) > 0;
    }

    /**
     * i1 是否大于 i2
     *
     * @param i1 数值
     * @param i2 数值
     * @return true表示 i1 大于 i2
     */
    public static boolean greater(Double i1, Double i2) {
        if (i1 == null && i2 == null) {
            return false;
        }
        if (i1 == null && i2 != null) {
            return false;
        }
        if (i1 != null && i2 == null) {
            return true;
        }

        return i1.doubleValue() > i2.doubleValue();
    }

    /**
     * i1 是否大于 i2
     *
     * @param i1 数值
     * @param i2 数值
     * @return true表示 i1 大于 i2
     */
    public static boolean greater(Long i1, Long i2) {
        if (i1 == null && i2 == null) {
            return false;
        }
        if (i1 == null && i2 != null) {
            return false;
        }
        if (i1 != null && i2 == null) {
            return true;
        }

        return i1.longValue() > i2.longValue();
    }

    /**
     * i1 是否大于 i2
     *
     * @param i1 数值
     * @param i2 数值
     * @return true表示 i1 大于 i2
     */
    public static boolean greater(Short i1, Short i2) {
        if (i1 == null && i2 == null) {
            return false;
        }
        if (i1 == null && i2 != null) {
            return false;
        }
        if (i1 != null && i2 == null) {
            return true;
        }

        return i1.shortValue() > i2.shortValue();
    }

    /**
     * i1 是否大于 i2
     *
     * @param i1 数值
     * @param i2 数值
     * @return true表示 i1 大于 i2
     */
    public static boolean greater(Float i1, Float i2) {
        if (i1 == null && i2 == null) {
            return false;
        }
        if (i1 == null && i2 != null) {
            return false;
        }
        if (i1 != null && i2 == null) {
            return true;
        }
        return i1.floatValue() > i2.floatValue();
    }

    /**
     * i1 是否小于 i2
     *
     * @param i1 数值
     * @param i2 数值
     * @return true表示 i1 小于 i2
     */
    public static boolean less(Integer i1, Integer i2) {
        if (i1 == null && i2 == null) {
            return false;
        }
        if (i1 == null && i2 != null) {
            return true;
        }
        if (i1 != null && i2 == null) {
            return false;
        }

        return i1.intValue() < i2.intValue();
    }

    /**
     * i1 是否小于 i2
     *
     * @param i1 数值
     * @param i2 数值
     * @return true表示 i1 小于 i2
     */
    public static boolean less(BigInteger i1, BigInteger i2) {
        if (i1 == null && i2 == null) {
            return false;
        }
        if (i1 == null && i2 != null) {
            return true;
        }
        if (i1 != null && i2 == null) {
            return false;
        }

        return i1.compareTo(i2) < 0;
    }

    /**
     * i1 是否小于 i2
     *
     * @param i1 数值
     * @param i2 数值
     * @return true表示 i1 小于 i2
     */
    public static boolean less(BigDecimal i1, BigDecimal i2) {
        if (i1 == null && i2 == null) {
            return false;
        }
        if (i1 == null && i2 != null) {
            return true;
        }
        if (i1 != null && i2 == null) {
            return false;
        }

        return i1.compareTo(i2) < 0;
    }

    /**
     * i1 是否小于 i2
     *
     * @param i1 数值
     * @param i2 数值
     * @return true表示 i1 小于 i2
     */
    public static boolean less(Double i1, Double i2) {
        if (i1 == null && i2 == null) {
            return false;
        }
        if (i1 == null && i2 != null) {
            return true;
        }
        if (i1 != null && i2 == null) {
            return false;
        }

        return i1.doubleValue() < i2.doubleValue();
    }

    /**
     * i1 是否小于 i2
     *
     * @param i1 数值
     * @param i2 数值
     * @return true表示 i1 小于 i2
     */
    public static boolean less(Long i1, Long i2) {
        if (i1 == null && i2 == null) {
            return false;
        }
        if (i1 == null && i2 != null) {
            return true;
        }
        if (i1 != null && i2 == null) {
            return false;
        }

        return i1.longValue() < i2.longValue();
    }

    /**
     * i1 是否小于 i2
     *
     * @param i1 数值
     * @param i2 数值
     * @return true表示 i1 小于 i2
     */
    public static boolean less(Short i1, Short i2) {
        if (i1 == null && i2 == null) {
            return false;
        }
        if (i1 == null && i2 != null) {
            return true;
        }
        if (i1 != null && i2 == null) {
            return false;
        }

        return i1.shortValue() < i2.shortValue();
    }

    /**
     * i1 是否小于 i2
     *
     * @param i1 数值
     * @param i2 数值
     * @return true表示 i1 小于 i2
     */
    public static boolean less(Float i1, Float i2) {
        if (i1 == null && i2 == null) {
            return false;
        }
        if (i1 == null && i2 != null) {
            return true;
        }
        if (i1 != null && i2 == null) {
            return false;
        }
        return i1.floatValue() < i2.floatValue();
    }

    /**
     * i1 是否大于等于 i2
     *
     * @param i1 数值
     * @param i2 数值
     * @return true表示 i1 大于等于 i2
     */
    public static boolean greaterEquals(Integer i1, Integer i2) {
        if (i1 == null && i2 == null) {
            return true;
        }
        if (i1 == null && i2 != null) {
            return false;
        }
        if (i1 != null && i2 == null) {
            return true;
        }

        return i1.intValue() >= i2.intValue();
    }

    /**
     * i1 是否大于等于 i2
     *
     * @param i1 数值
     * @param i2 数值
     * @return true表示 i1 大于等于 i2
     */
    public static boolean greaterEquals(BigInteger i1, BigInteger i2) {
        if (i1 == null && i2 == null) {
            return true;
        }
        if (i1 == null && i2 != null) {
            return false;
        }
        if (i1 != null && i2 == null) {
            return true;
        }
        return i1.compareTo(i2) >= 0;
    }

    /**
     * i1 是否大于等于 i2
     *
     * @param i1 数值
     * @param i2 数值
     * @return true表示 i1 大于等于 i2
     */
    public static boolean greaterEquals(BigDecimal i1, BigDecimal i2) {
        if (i1 == null && i2 == null) {
            return true;
        }
        if (i1 == null && i2 != null) {
            return false;
        }
        if (i1 != null && i2 == null) {
            return true;
        }

        return i1.compareTo(i2) >= 0;
    }

    /**
     * i1 是否大于等于 i2
     *
     * @param i1 数值
     * @param i2 数值
     * @return true表示 i1 大于等于 i2
     */
    public static boolean greaterEquals(Double i1, Double i2) {
        if (i1 == null && i2 == null) {
            return true;
        }
        if (i1 == null && i2 != null) {
            return false;
        }
        if (i1 != null && i2 == null) {
            return true;
        }

        return i1.doubleValue() >= i2.doubleValue();
    }

    /**
     * i1 是否大于等于 i2
     *
     * @param i1 数值
     * @param i2 数值
     * @return true表示 i1 大于等于 i2
     */
    public static boolean greaterEquals(Long i1, Long i2) {
        if (i1 == null && i2 == null) {
            return true;
        }
        if (i1 == null && i2 != null) {
            return false;
        }
        if (i1 != null && i2 == null) {
            return true;
        }

        return i1.longValue() >= i2.longValue();
    }

    /**
     * i1 是否大于等于 i2
     *
     * @param i1 数值
     * @param i2 数值
     * @return true表示 i1 大于等于 i2
     */
    public static boolean greaterEquals(Short i1, Short i2) {
        if (i1 == null && i2 == null) {
            return true;
        }
        if (i1 == null && i2 != null) {
            return false;
        }
        if (i1 != null && i2 == null) {
            return true;
        }

        return i1.shortValue() >= i2.shortValue();
    }

    /**
     * i1 是否大于等于 i2
     *
     * @param i1 数值
     * @param i2 数值
     * @return true表示 i1 大于等于 i2
     */
    public static boolean greaterEquals(Float i1, Float i2) {
        if (i1 == null && i2 == null) {
            return true;
        }
        if (i1 == null && i2 != null) {
            return false;
        }
        if (i1 != null && i2 == null) {
            return true;
        }
        return i1.floatValue() >= i2.floatValue();
    }

    /**
     * i1 是否小于等于 i2
     *
     * @param i1 数值
     * @param i2 数值
     * @return true表示 i1 小于等于 i2
     */
    public static boolean lessEquals(Integer i1, Integer i2) {
        if (i1 == null && i2 == null) {
            return true;
        }
        if (i1 == null && i2 != null) {
            return true;
        }
        if (i1 != null && i2 == null) {
            return false;
        }

        return i1.intValue() <= i2.intValue();
    }

    /**
     * i1 是否小于等于 i2
     *
     * @param i1 数值
     * @param i2 数值
     * @return true表示 i1 小于等于 i2
     */
    public static boolean lessEquals(BigInteger i1, BigInteger i2) {
        if (i1 == null && i2 == null) {
            return true;
        }
        if (i1 == null && i2 != null) {
            return true;
        }
        if (i1 != null && i2 == null) {
            return false;
        }

        return i1.compareTo(i2) <= 0;
    }

    /**
     * i1 是否小于等于 i2
     *
     * @param i1 数值
     * @param i2 数值
     * @return true表示 i1 小于等于 i2
     */
    public static boolean lessEquals(BigDecimal i1, BigDecimal i2) {
        if (i1 == null && i2 == null) {
            return true;
        }
        if (i1 == null && i2 != null) {
            return true;
        }
        if (i1 != null && i2 == null) {
            return false;
        }

        return i1.compareTo(i2) <= 0;
    }

    /**
     * i1 是否小于等于 i2
     *
     * @param i1 数值
     * @param i2 数值
     * @return true表示 i1 小于等于 i2
     */
    public static boolean lessEquals(Double i1, Double i2) {
        if (i1 == null && i2 == null) {
            return true;
        }
        if (i1 == null && i2 != null) {
            return true;
        }
        if (i1 != null && i2 == null) {
            return false;
        }

        return i1.doubleValue() <= i2.doubleValue();
    }

    /**
     * i1 是否小于等于 i2
     *
     * @param i1 数值
     * @param i2 数值
     * @return true表示 i1 小于等于 i2
     */
    public static boolean lessEquals(Long i1, Long i2) {
        if (i1 == null && i2 == null) {
            return true;
        }
        if (i1 == null && i2 != null) {
            return true;
        }
        if (i1 != null && i2 == null) {
            return false;
        }

        return i1.longValue() <= i2.longValue();
    }

    /**
     * i1 是否小于等于 i2
     *
     * @param i1 数值
     * @param i2 数值
     * @return true表示 i1 小于等于 i2
     */
    public static boolean lessEquals(Short i1, Short i2) {
        if (i1 == null && i2 == null) {
            return true;
        }
        if (i1 == null && i2 != null) {
            return true;
        }
        if (i1 != null && i2 == null) {
            return false;
        }

        return i1.shortValue() <= i2.shortValue();
    }

    /**
     * i1 是否小于等于 i2
     *
     * @param i1 数值
     * @param i2 数值
     * @return true表示 i1 小于等于 i2
     */
    public static boolean lessEquals(Float i1, Float i2) {
        if (i1 == null && i2 == null) {
            return true;
        }
        if (i1 == null && i2 != null) {
            return true;
        }
        if (i1 != null && i2 == null) {
            return false;
        }
        return i1.floatValue() <= i2.floatValue();
    }

    /**
     * i1 是否等于 i2
     *
     * @param i1 数值
     * @param i2 数值
     * @return true表示 i1 等于 i2
     */
    public static boolean equals(Integer i1, Integer i2) {
        if (i1 == null && i2 == null) {
            return true;
        }
        if (i1 == null && i2 != null) {
            return false;
        }
        if (i1 != null && i2 == null) {
            return false;
        }

        return i1.intValue() == i2.intValue();
    }

    /**
     * i1 是否等于 i2
     *
     * @param i1 数值
     * @param i2 数值
     * @return true表示 i1 等于 i2
     */
    public static boolean equals(BigInteger i1, BigInteger i2) {
        if (i1 == null && i2 == null) {
            return true;
        }
        if (i1 == null && i2 != null) {
            return false;
        }
        if (i1 != null && i2 == null) {
            return false;
        }

        return i1.compareTo(i2) == 0;
    }

    /**
     * i1 是否等于 i2
     *
     * @param i1 数值
     * @param i2 数值
     * @return true表示 i1 等于 i2
     */
    public static boolean equals(BigDecimal i1, BigDecimal i2) {
        if (i1 == null && i2 == null) {
            return true;
        }
        if (i1 == null && i2 != null) {
            return false;
        }
        if (i1 != null && i2 == null) {
            return false;
        }

        return i1.compareTo(i2) == 0;
    }

    /**
     * i1 是否等于 i2
     *
     * @param i1 数值
     * @param i2 数值
     * @return true表示 i1 等于 i2
     */
    public static boolean equals(Double i1, Double i2) {
        if (i1 == null && i2 == null) {
            return true;
        }
        if (i1 == null && i2 != null) {
            return false;
        }
        if (i1 != null && i2 == null) {
            return false;
        }

        return i1.doubleValue() == i2.doubleValue();
    }

    /**
     * i1 是否等于 i2
     *
     * @param i1 数值
     * @param i2 数值
     * @return true表示 i1 等于 i2
     */
    public static boolean equals(Long i1, Long i2) {
        if (i1 == null && i2 == null) {
            return true;
        }
        if (i1 == null && i2 != null) {
            return false;
        }
        if (i1 != null && i2 == null) {
            return false;
        }

        return i1.longValue() == i2.longValue();
    }

    /**
     * i1 是否等于 i2
     *
     * @param i1 数值
     * @param i2 数值
     * @return true表示 i1 等于 i2
     */
    public static boolean equals(Short i1, Short i2) {
        if (i1 == null && i2 == null) {
            return true;
        }
        if (i1 == null && i2 != null) {
            return false;
        }
        if (i1 != null && i2 == null) {
            return false;
        }

        return i1.shortValue() == i2.shortValue();
    }

    /**
     * i1 是否等于 i2
     *
     * @param i1 数值
     * @param i2 数值
     * @return true表示 i1 等于 i2
     */
    public static boolean equals(Float i1, Float i2) {
        if (i1 == null && i2 == null) {
            return true;
        }
        if (i1 == null && i2 != null) {
            return false;
        }
        if (i1 != null && i2 == null) {
            return false;
        }
        return i1.floatValue() == i2.floatValue();
    }

    /**
     * 判断是否为零
     *
     * @param val 数值
     * @return null返回false；
     */
    public static boolean isZero(BigDecimal val) {
        return val != null && val.compareTo(BigDecimal.ZERO) == 0;
    }

}