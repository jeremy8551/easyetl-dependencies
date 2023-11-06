package icu.etl.collection;

import java.util.HashSet;
import java.util.Iterator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CaseSensitveSetTest {

    @Test
    public void test() {
        CaseSensitivSet set = new CaseSensitivSet();
        assertTrue(set.isEmpty());

        set.add(null);
        set.add("");
        set.add("a");
        set.add("A");
        assertEquals(set.size(), 3);

        set.clear();
        set.add(null);
        set.add("");
        set.add("a");
        set.add("A");
        set.add("Abc");
        set.add("abc");
        assertEquals(set.size(), 4);

        Iterator<String> it = set.iterator();
        assertTrue(it.hasNext());
        assertEquals(it.next(), null);
        assertTrue(it.hasNext());
        assertEquals(it.next(), "");
        assertTrue(it.hasNext());
        assertEquals(it.next(), "a");
        assertTrue(it.hasNext());
        assertEquals(it.next(), "Abc");
        assertTrue(!it.hasNext());

        Object[] array = set.toArray();
        assertEquals(array[0], null);
        assertEquals(array[1], "");
        assertEquals(array[2], "a");
        assertEquals(array[3], "Abc");

        array = set.toArray(new String[4]);
        assertEquals(array[0], null);
        assertEquals(array[1], "");
        assertEquals(array[2], "a");
        assertEquals(array[3], "Abc");

        // 测试删除全部数据
        for (it = set.iterator(); it.hasNext(); ) {
            it.next();
            it.remove();
        }
        assertTrue(set.isEmpty());

        // 测试删除全部数据
        set.add(null);
        set.add("");
        set.add("a");
        set.add("A");
        set.add("Abc");
        set.add("abc");
        assertTrue(set.contains(null));
        assertTrue(set.contains(""));
        assertTrue(set.contains("A"));
        assertTrue(set.contains("aBc"));
        HashSet<String> test = new HashSet<String>();
        test.add("A");
        test.add("aBc");
        test.add("");
        test.add(null);
        assertTrue(set.containsAll(test));
        assertTrue(set.removeAll(test) && set.isEmpty());

        // 测试添加集合元素
        set.clear();
        set.add(null);
        set.add("");
        set.add("a");
        set.add("A");
        set.add("Abc");
        set.add("abc");
        int old = set.size();
        assertTrue(!set.addAll(test) && old == set.size());

        set.clear();
        set.add(null);
        set.add("");
        set.add("a");
        set.add("A");
        set.add("Abc");
        set.add("abc");

        // 测试顺序
        it = set.iterator();
        assertEquals(it.next(), null);
        assertEquals(it.next(), "");
        assertEquals(it.next(), "a");
        assertEquals(it.next(), "Abc");

        old = set.size();
        assertTrue(!set.retainAll(test) && old == set.size());
    }

}
