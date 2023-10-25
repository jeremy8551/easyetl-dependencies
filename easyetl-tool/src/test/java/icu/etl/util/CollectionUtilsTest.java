package icu.etl.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CollectionUtilsTest {

    @Test
    public void testIsEmptyMapOfEF() {
        Map<String, String> map = null;
        assertTrue(CollectionUtils.isEmpty(map));

        map = new HashMap<String, String>();
        assertTrue(CollectionUtils.isEmpty(map));

        map.put("", "");
        assertTrue(!CollectionUtils.isEmpty(map));
    }

    @Test
    public void testIsEmptyCollectionOfE() {
        List<String> map = null;
        assertTrue(CollectionUtils.isEmpty(map));

        map = new ArrayList<String>();
        assertTrue(CollectionUtils.isEmpty(map));

        map.add("");
        assertTrue(!CollectionUtils.isEmpty(map));
    }

    @Test
    public void testRemoveDuplicatData() {
        List<String> list = new ArrayList<String>();
        list.add("");
        list.add("");
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("2");
        list.add("2");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("9");

        List<String> newlist = CollectionUtils.removeDuplicate(list, null);
        assertEquals(newlist.size(), 8);

        newlist = CollectionUtils.removeDuplicate(list, new StringComparator());
        assertEquals(newlist.size(), 8);
    }

    @Test
    public void testToHashMap() {
        String[] keys = {"key1", "key2"};
        String[] vals = {"val1", "val2"};
        Map<String, String> map = CollectionUtils.toHashMap(keys, vals);
        assertEquals(map.get("key1"), "val1");
        assertEquals(map.get("key2"), "val2");
    }

    @Test
    public void testToArray() {
        List<String> list = new ArrayList<String>();
        list.add("");
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");

        assertTrue(ArrayUtils.equals(CollectionUtils.toArray(list), new String[]{"", "1", "2", "3", "4", "5", "6"}, null));
    }

    @Test
    public void testToList() {
    }

    @Test
    public void testOnlyOne() {
        List<String> list = new ArrayList<String>();
        try {
            CollectionUtils.onlyOne(list);
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }

        list.add("");
        assertEquals(CollectionUtils.onlyOne(list), "");

        list.add("1");
        try {
            CollectionUtils.onlyOne(list);
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testFirstElement() {
        List<String> list = null;
        assertEquals(CollectionUtils.firstElement(list), null);

        list = new ArrayList<String>();
        assertEquals(CollectionUtils.firstElement(list), null);

        list.add("1");
        assertEquals(CollectionUtils.firstElement(list), "1");

        list.add("1");
        list.add("2");
        assertEquals(CollectionUtils.firstElement(list), "1");
    }

    @Test
    public void testLastElement() {
        List<String> list = null;
        assertEquals(CollectionUtils.lastElement(list), null);

        list = new ArrayList<String>();
        assertEquals(CollectionUtils.lastElement(list), null);

        list.add("1");
        assertEquals(CollectionUtils.lastElement(list), "1");

        list.add("1");
        list.add("2");
        assertEquals(CollectionUtils.lastElement(list), "2");

    }

    @Test
    public void testGetDiffAttrVal() {
        HashMap<String, String> m1 = new HashMap<String, String>();
        m1.put("a", "a");
        m1.put("b", "b");
        m1.put("c", "c");
        m1.put("d", "d");
        m1.put("e", "e");

        HashMap<String, String> m2 = new HashMap<String, String>();
        m2.put("a", "a1");
        m2.put("c", "c");
        m2.put("d", "d1");
        m2.put("e", "e");

        assertEquals(CollectionUtils.getDiffAttrVal(m1, m2, null), ArrayUtils.asList("a", "d"));
        assertEquals(CollectionUtils.getDiffAttrVal(m1, m2, new StringComparator()), ArrayUtils.asList("a", "d"));
    }

    @Test
    public void testElementAt() {
        List<String> list = new ArrayList<String>();
        list.add("0");
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");

        assertNull(CollectionUtils.elementAt(list, -1));
        assertNull(CollectionUtils.elementAt(list, list.size()));
        assertNull(CollectionUtils.elementAt(list, list.size() + 1));
        assertEquals(CollectionUtils.elementAt(list, 0), "0");
        assertEquals(CollectionUtils.elementAt(list, 1), "1");
    }

    @Test
    public void testCloneProperties() {
        Properties p = new Properties();
        p.setProperty("name", "value1");
        p.put(new Integer(1), "value2");

        Properties np = new Properties();
        CollectionUtils.cloneProperties(p, np);
        assertEquals(np.get("name"), "value1");
        assertEquals(np.get(new Integer(1)), "value2");
    }

    @Test
    public void testContain() {
        List<String> list = new ArrayList<String>();
        list.add(null);
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");

        assertTrue(CollectionUtils.contain(list, null, new StringComparator()));
        assertTrue(CollectionUtils.contain(list, "6", new StringComparator()));
    }

    @Test
    public void testContainsKeyIgnoreCase() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("t", "");
        map.put("a", "");
        map.put("d", "");
        map.put("c", "");

        assertTrue(CollectionUtils.containsKeyIgnoreCase(map, "T"));
        assertTrue(CollectionUtils.containsKeyIgnoreCase(map, "C"));
        assertTrue(CollectionUtils.containsKeyIgnoreCase(map, "d"));
    }

}
