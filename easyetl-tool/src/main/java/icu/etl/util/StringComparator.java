package icu.etl.util;

import java.util.Comparator;

/**
 * 字符串比较类
 *
 * @author jeremy8551@qq.com
 * @createtime 2017-03-27
 */
public class StringComparator implements Comparator<String>, Cloneable {

    /**
     * 比较字符串大小
     *
     * @param str1 字符串
     * @param str2 字符串
     * @return 返回值小于零时表示第一个参数小于第二个参数 <br>
     * 返回值大于零时表示第一个参数大于第二个参数 <br>
     * 返回值等于零时表示第一个参数等于第二个参数 <br>
     */
    public static int compareTo(String str1, String str2) {
        boolean b1 = (str1 == null);
        boolean b2 = (str2 == null);
        if (b1 && b2) {
            return 0;
        } else if (b1) {
            return -1;
        } else if (b2) {
            return 1;
        } else {
            return str1.compareTo(str2);
        }
    }

    /**
     * 比较字符串大小 <br>
     * 空字符串与空指针 null 相等
     *
     * @param str1 字符串
     * @param str2 字符串
     * @return 返回值小于零时表示第一个参数小于第二个参数 <br>
     * 返回值大于零时表示第一个参数大于第二个参数 <br>
     * 返回值等于零时表示第一个参数等于第二个参数 <br>
     */
    public static int compareIgnoreBlank(String str1, String str2) {
        boolean b1 = StringUtils.isBlank(str1);
        boolean b2 = StringUtils.isBlank(str2);
        if (b1 && b2) {
            return 0;
        } else if (b1) {
            return -1;
        } else if (b2) {
            return 1;
        } else {
            return str1.compareTo(str2);
        }
    }

    /**
     * 初始化
     */
    public StringComparator() {
        super();
    }

    /**
     * 比较字符串大小
     *
     * @param str1 字符串
     * @param str2 字符串
     * @return 返回值小于零时表示第一个参数小于第二个参数 <br>
     * 返回值大于零时表示第一个参数大于第二个参数 <br>
     * 返回值等于零时表示第一个参数等于第二个参数 <br>
     */
    public int compare(String str1, String str2) {
        boolean b1 = (str1 == null);
        boolean b2 = (str2 == null);
        if (b1 && b2) {
            return 0;
        } else if (b1) {
            return -1;
        } else if (b2) {
            return 1;
        } else {
            return str1.compareTo(str2);
        }
    }

    public StringComparator reversed() {
        return new ReversedComparetor();
    }

    public boolean equals(Object obj) {
        return (obj instanceof StringComparator);
    }

    public StringComparator clone() {
        return new StringComparator();
    }

    public String toString() {
        return StringComparator.class.getName();
    }

    class ReversedComparetor extends StringComparator {

        public ReversedComparetor() {
            super();
        }

        public int compare(String str1, String str2) {
            return -super.compare(str1, str2);
        }
    }

}

