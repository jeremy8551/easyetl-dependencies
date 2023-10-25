package icu.etl.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

/**
 * 集合类工具
 *
 * @author jeremy8551@qq.com
 * @createtime 2011-05-24
 */
public class CollectionUtils {

    public CollectionUtils() {
    }

    /**
     * 判断是否为空 <br>
     * isEmpty(null) == true <br>
     *
     * @param map key, value对象
     * @return true表示为 null 或 array.isEmpty()
     */
    public static <E, F> boolean isEmpty(Map<E, F> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 集合为null或空时返回true <br>
     * isEmpty(null) == true <br>
     * isEmpty({}) == true <br>
     * isEmpty({1,2,3}) == false; <br>
     *
     * @param c 数组
     * @return true表示为 null 或 list.length == 0
     */
    public static <E> boolean isEmpty(Collection<E> c) {
        return c == null || c.isEmpty();
    }

    /**
     * 返回属性名集合
     *
     * @param p
     * @return
     */
    public static Set<String> stringPropertyNames(Properties p) {
        if (p == null) {
            throw new NullPointerException();
        }

        Set<String> set = new HashSet<String>(p.size());
        Set<Entry<Object, Object>> entrys = p.entrySet();
        for (Entry<Object, Object> entry : entrys) {
            Object key = entry.getKey();
            if (key instanceof String) {
                set.add((String) key);
            }
        }
        return set;
    }

    /**
     * 删除集合参数c中的重复数据项
     *
     * @param <E>
     * @param list 集合
     * @param c    判断重复数据的规则
     * @return 一个新创建的集合副本，其中没有重复数据
     */
    public static <E> List<E> removeDuplicate(Collection<E> list, Comparator<E> c) {
        ArrayList<E> newlist = new ArrayList<E>();
        if (list == null || list.size() <= 1) {
            if (list != null) {
                newlist.addAll(list);
            }
            return newlist;
        } else if (c == null) {
            HashSet<E> set = new HashSet<E>();
            set.addAll(list);
            newlist.addAll(set);
            return newlist;
        } else {
            TreeSet<E> set = new TreeSet<E>(c);
            set.addAll(list);
            newlist.addAll(set);
            return newlist;
        }
    }

    /**
     * 把二个数组拼成一个HashMap对象，规则：<br>
     * 参数 args1 数组作为Map 集合的关键字 <br>
     * 参数 args2 数组作为 Map 集合的数值
     *
     * @param <E>
     * @param <F>
     * @param args1 数组1
     * @param args2 数组2
     * @return
     */
    public static <E, F> Map<E, F> toHashMap(E[] args1, F[] args2) {
        if (args1 == null) {
            throw new NullPointerException();
        }
        if (args2 == null) {
            throw new NullPointerException();
        }
        if (args1.length != args2.length) {
            throw new IllegalArgumentException(args1.length + " != " + args2.length);
        }

        HashMap<E, F> map = new HashMap<E, F>();
        for (int i = 0; i < args1.length; i++) {
            if (args1[i] == null) {
                throw new NullPointerException(String.valueOf(args1));
            } else {
                map.put(args1[i], args2[i]);
            }
        }
        return map;
    }

    /**
     * 将字符串集合参数list中的字符串转为字符串数组
     *
     * @param c 字符串集合
     * @return
     */
    public static String[] toArray(Collection<String> c) {
        if (c == null) {
            return null;
        } else {
            String[] array = new String[c.size()];
            c.toArray(array);
            return array;
        }
    }

    /**
     * 把 Object 对象转为 String（自动删除字符串右边的空格）
     *
     * @param list 集合
     * @return 字符串集合副本
     */
    public static ArrayList<String> toList(List<?> list) {
        if (list == null) {
            return new ArrayList<String>();
        } else {
            ArrayList<String> newList = new ArrayList<String>(list.size());
            for (Object str : list) {
                newList.add(StringUtils.rtrimBlank(str));
            }
            return newList;
        }
    }

    /**
     * 集合中只能有一个元素, 并返回该元素 <br>
     * 如果集合中没有元素或超过一个元素就直接抛出异常信息
     *
     * @param <E>
     * @param ite
     * @return
     */
    public static <E> E onlyOne(Iterable<E> ite) {
        if (ite == null) {
            return null;
        } else {
            E obj = null;
            Iterator<E> it = ite.iterator();
            if (it.hasNext()) {
                obj = it.next();
            } else {
                throw new IllegalArgumentException(String.valueOf(ite));
            }

            if (it.hasNext()) { // 判断是否能读取下一个
                throw new IllegalArgumentException(String.valueOf(ite));
            }
            return obj;
        }
    }

    /**
     * 返回集合第一个元素
     *
     * @param <E>
     * @param ite List集合
     * @return
     */
    public static <E> E firstElement(Iterable<E> ite) {
        if (ite == null) {
            return null;
        } else {
            Iterator<E> it = ite.iterator();
            return it.hasNext() ? it.next() : null;
        }
    }

    /**
     * 返回数组最后一个元素
     *
     * @param <E>
     * @param c   集合
     * @return
     */
    public static <E> E lastElement(Collection<E> c) {
        if (c == null || c.size() == 0) {
            return null;
        }

        E object = null;
        Iterator<E> it = c.iterator();
        while (it.hasNext()) {
            object = it.next();
        }
        return object;
    }

    /**
     * 返回属性值不同的属性名（key） <br>
     * 属性名必须是在2个集合中都存在的属性 <br>
     *
     * @param map1 集合1
     * @param map2 集合2
     * @param c    比较规则
     * @return 属性值不同的属姓名集合
     */
    public static ArrayList<String> getDiffAttrVal(Map<String, String> map1, Map<String, String> map2, Comparator<String> c) {
        if (c == null) {
            c = new StringComparator();
        }

        ArrayList<String> list = new ArrayList<String>();
        String[] keys = CollectionUtils.toArray(map1.keySet());
        for (String key : keys) {
            if (map2.containsKey(key)) {
                String v1 = map1.get(key);
                String v2 = map2.get(key);
                if (c.compare(v1, v2) != 0) {
                    list.add(key);
                }
            }
        }
        return list;
    }

    /**
     * 返回集合参数 list 中指定位置上的元素
     *
     * @param <E>
     * @param list  集合列表
     * @param index 位置信息
     * @return 如果位置参数 index 超过了集合 list 的范围则返回 null
     */
    public static <E> E elementAt(List<E> list, int index) {
        return list != null && index >= 0 && index < list.size() ? list.get(index) : null;
    }

    /**
     * 从 src 参数中复制所有属性到 dest 参数中
     *
     * @param src  属性集合
     * @param dest 属性集合
     */
    public static Properties cloneProperties(Properties src, Properties dest) {
        if (src == null || dest == null) {
            return dest;
        } else {
            Set<Object> names = src.keySet();
            for (Object name : names) {
                dest.put(name, src.get(name));
            }
            return dest;
        }
    }

    /**
     * 判断集合参数 ite 中是否含有参数对象 obj
     *
     * @param <E>
     * @param ite 集合
     * @param obj
     * @param c   比对对象
     * @return
     */
    public static <E> boolean contain(Iterable<E> ite, E obj, Comparator<E> c) {
        if (ite == null) {
            throw new NullPointerException();
        }
        if (c == null) {
            throw new NullPointerException();
        }

        for (Iterator<E> it = ite.iterator(); it.hasNext(); ) {
            if (c.compare(obj, it.next()) == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 在忽略字符大小写的情况下判断 map 中是否存在 key
     *
     * @param <E>
     * @param map  属性集合
     * @param name 属性名
     * @return
     */
    public static <E> boolean containsKeyIgnoreCase(Map<String, E> map, String name) {
        if (map == null) {
            throw new NullPointerException();
        }
        if (name == null) {
            throw new NullPointerException();
        }

        for (Iterator<String> it = map.keySet().iterator(); it.hasNext(); ) {
            if (it.next().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

}
