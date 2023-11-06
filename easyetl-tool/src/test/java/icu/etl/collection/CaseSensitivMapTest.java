package icu.etl.collection;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CaseSensitivMapTest {

    @Test
    public void test() {
        CaseSensitivMap<String> map = new CaseSensitivMap();
        assertTrue(map.isEmpty());

        map.put("1", "");
        map.put("1", "2");
        map.put("a", "");
        map.put("A", "2");
        map.put("aB", "");
        map.put("Ab", "2");
        map.put("abc", "");
        assertEquals(map.size(), 4);

        assertEquals(map.get("1"), "2");
        assertEquals(map.get("a"), "2");
        assertEquals(map.get("Ab"), "2");
        assertEquals(map.get("ABC"), "");

        assertTrue(!map.containsKey(""));
        assertTrue(map.containsKey("1"));
        assertTrue(map.containsKey("a"));
        assertTrue(map.containsKey("A"));
        assertTrue(map.containsKey("Ab"));
        assertTrue(map.containsKey("aB"));
        assertTrue(map.containsKey("abc"));
        assertTrue(map.containsValue(""));
        assertTrue(map.containsValue("2"));
        assertTrue(!map.containsValue("3"));

        map.clear();
        assertTrue(map.isEmpty());

        map.put("1", "");
        map.put("1", "2");
        map.put("a", "");
        map.put("A", "2");
        map.put("aB", "");
        map.put("Ab", "2");
        map.put("abc", "");

        Collection<String> c = map.values();
        assertTrue(c.contains("2"));
        assertTrue(c.contains(""));
        assertTrue(!c.contains("1"));

        Set<String> keys = map.keySet();
        assertTrue(keys.contains("1"));
        assertTrue(keys.contains("a"));
        assertTrue(keys.contains("ab"));
        assertTrue(keys.contains("abc"));
        assertTrue(!keys.contains(""));

        Set<String> set = new CaseSensitivSet();
        Set<Entry<String, String>> entrySet = map.entrySet();
        for (Entry<String, String> e : entrySet) {
            assertTrue(!set.contains(e.getKey()));
            set.add(e.getKey());
        }

        map.remove("1");
        assertEquals(map.size(), 3);
        map.remove("a");
        assertEquals(map.size(), 2);
        map.remove("AB");
        assertEquals(map.size(), 1);
        map.remove("ABc");
        assertEquals(map.size(), 0);

    }

}
