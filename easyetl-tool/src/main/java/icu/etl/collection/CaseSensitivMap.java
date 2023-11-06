package icu.etl.collection;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 在 Map 的 Key 属性忽略英文字符大小写
 *
 * @param <E>
 * @author jeremy8551@qq.com
 */
public class CaseSensitivMap<E> implements Map<String, E> {

    /** 关键字与数值的映射关系 */
    private HashMap<String, E> map;

    /** 关键字集合 */
    private CaseSensitivSet keys;

    /**
     * 初始化
     */
    public CaseSensitivMap() {
        this.map = new HashMap<String, E>();
        this.keys = new CaseSensitivSet();
    }

    public int size() {
        return this.map.size();
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    public boolean containsValue(Object value) {
        return this.map.containsValue(value);
    }

    public boolean containsKey(Object key) {
        return this.keys.contains(key);
    }

    public E get(Object key) {
        if (key instanceof String) {
            return this.map.get(((String) key).toUpperCase());
        } else {
            return null;
        }
    }

    public E put(String key, E value) {
        E old = this.map.put(key.toUpperCase(), value);
        this.keys.add(key);
        return old;
    }

    public E remove(Object key) {
        if (key instanceof String) { // 如果输入参数是字符串
            E old = this.map.remove(((String) key).toUpperCase());
            this.keys.remove(key);
            return old;
        } else {
            return null;
        }
    }

    public void putAll(Map<? extends String, ? extends E> map) {
        if (map != null) {
            Set<? extends String> keys = map.keySet(); // 遍历所有关键字
            for (String key : keys) {
                this.put(key, map.get(key));
            }
        }
    }

    public void clear() {
        this.map.clear();
    }

    public Set<String> keySet() {
        CaseSensitivSet set = new CaseSensitivSet();
        set.addAll(this.keys);
        return set;
    }

    public Collection<E> values() {
        return this.map.values();
    }

    public Set<Entry<String, E>> entrySet() {
        Set<Entry<String, E>> newset = new HashSet<Entry<String, E>>();
        Set<Entry<String, E>> set = this.map.entrySet();
        for (Entry<String, E> entry : set) {
            String key = entry.getKey(); // 大写关键字
            E value = entry.getValue(); // 关键字对应的数值
            String okey = this.keys.get(key); // 原关键字
            newset.add(new SE(okey, value));
        }
        return newset;
    }

    private class SE implements Entry<String, E> {
        private String key;
        private E obj;

        public SE(String key, E obj) {
            super();
            this.key = key;
            this.obj = obj;
        }

        public String getKey() {
            return this.key;
        }

        public E getValue() {
            return this.obj;
        }

        public E setValue(E value) {
            E old = this.obj;
            this.obj = value;
            return old;
        }
    }

}
