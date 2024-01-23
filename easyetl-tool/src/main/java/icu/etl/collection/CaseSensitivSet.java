package icu.etl.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 英文字母大小写不敏感集合
 * 集合内部统一使用英文字母大写作为集合元素
 *
 * @author jeremy8551@qq.com
 */
public class CaseSensitivSet implements Set<String> {

    private LinkedHashSet<String> set;

    /**
     * 初始化
     */
    public CaseSensitivSet() {
        this.set = new LinkedHashSet<String>();
    }

    /**
     * 初始化
     *
     * @param c 集合
     */
    public CaseSensitivSet(Collection<String> c) {
        this();
        if (c != null) {
            for (Iterator<String> it = c.iterator(); it.hasNext(); ) {
                this.add(it.next());
            }
        }
    }

    public synchronized boolean add(String str) {
        if (str == null) {
            return !this.containsNull() && this.set.add(null);
        } else {
            String key = this.get(str);
            return key == null && this.set.add(str);
        }
    }

    public synchronized boolean addAll(Collection<? extends String> c) {
        if (c == null) {
            return false;
        }

        boolean modified = false;
        for (String str : c) {
            if (this.add(str)) {
                modified = true;
            }
        }
        return modified;
    }

    public boolean contains(Object obj) {
        if (obj == null) {
            return this.containsNull();
        } else if (obj instanceof String) {
            return this.get((String) obj) != null;
        } else {
            return false;
        }
    }

    /**
     * 判断集合元素中是否存在空指针数据
     *
     * @return 返回true表示存在空指针数据
     */
    public boolean containsNull() {
        for (Iterator<String> it = this.set.iterator(); it.hasNext(); ) {
            if (it.next() == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断集合元素中查找指定字符串参数 str
     *
     * @param str 字符串，不能是空指针
     * @return 返回找到相同的字符串
     */
    public String get(String str) {
        if (str == null) {
            throw new NullPointerException();
        }

        for (Iterator<String> it = this.set.iterator(); it.hasNext(); ) {
            String obj = it.next();
            if (obj != null && obj.equalsIgnoreCase(str)) {
                return obj;
            }
        }
        return null;
    }

    public synchronized boolean remove(Object obj) {
        if (obj == null) {
            return this.containsNull() && this.set.remove(null);
        } else if (obj instanceof String) {
            String key = this.get((String) obj);
            return key != null && this.set.remove(key);
        } else {
            return false;
        }
    }

    public synchronized boolean removeAll(Collection<?> c) {
        if (c == null) {
            return false;
        }

        boolean modified = false;
        for (Iterator<?> it = c.iterator(); it.hasNext(); ) {
            modified |= this.remove(it.next());
        }
        return modified;
    }

    public int size() {
        return this.set.size();
    }

    public boolean isEmpty() {
        return this.set.isEmpty();
    }

    public Iterator<String> iterator() {
        return this.set.iterator();
    }

    public Object[] toArray() {
        return this.set.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return this.set.toArray(a);
    }

    public boolean containsAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        } else {
            for (Object obj : c) {
                if (!this.contains(obj)) {
                    return false;
                }
            }
            return true;
        }
    }

    public synchronized boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        } else {
            boolean modified = false;
            for (Iterator<String> it = this.iterator(); it.hasNext(); ) {
                String str = it.next();
                if (!this.contains(c, str)) {
                    this.remove(str);
                    modified = true;
                }
            }
            return modified;
        }
    }

    /**
     * 判断在集合参数 c 中是否存在字符串 str
     *
     * @param c   集合
     * @param str 元素
     * @return 返回true表示集合中存在字符串参数
     */
    private boolean contains(Collection<?> c, String str) {
        if (str == null) {
            for (Iterator<?> it = c.iterator(); it.hasNext(); ) {
                Object obj = it.next();
                if (obj == null) {
                    return true;
                }
            }
            return false;
        } else {
            for (Iterator<?> it = c.iterator(); it.hasNext(); ) {
                Object obj = it.next();
                if (obj instanceof String && str.equalsIgnoreCase((String) obj)) {
                    return true;
                }
            }
            return false;
        }
    }

    public void clear() {
        this.set.clear();
    }

}
