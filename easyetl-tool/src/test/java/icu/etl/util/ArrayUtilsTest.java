package icu.etl.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ArrayUtilsTest {

    @Test
    public void testindexOf() {
        Comparator<String> c = new Comparator<String>() {
            public int compare(String o1, String o2) {
                return 0;
            }
        };

        CharTable jdbc = new CharTable();
        Object[] array = new Object[]{0, 12.12, "test", jdbc, c};
        assertEquals(ArrayUtils.indexOf(array, Integer.class, 0), new Integer(0));
        assertEquals(ArrayUtils.indexOf(array, String.class, 0), "test");
        assertEquals(ArrayUtils.indexOf(array, Comparator.class, 0), c);
        assertEquals(ArrayUtils.indexOf(array, CharTable.class, 0), jdbc);
    }

    @Test
    public void testRemoveDuplicatDataListOfTComparatorOfT() {
        Assert.assertTrue(Arrays.equals(new int[]{}, ArrayUtils.removeDuplicat(new int[]{})));
        Assert.assertTrue(Arrays.equals(new int[]{0}, ArrayUtils.removeDuplicat(new int[]{0})));
        Assert.assertTrue(Arrays.equals(new int[]{0}, ArrayUtils.removeDuplicat(new int[]{0, 0, 0})));
        Assert.assertTrue(Arrays.equals(new int[]{0, 1, 2}, ArrayUtils.removeDuplicat(new int[]{0, 1, 2})));
        Assert.assertTrue(Arrays.equals(new int[]{0, 1, 2}, ArrayUtils.removeDuplicat(new int[]{0, 1, 2, 1, 0, 2})));

        Assert.assertFalse(Arrays.equals(new int[]{0}, ArrayUtils.removeDuplicat(new int[]{1})));
        Assert.assertFalse(Arrays.equals(new int[]{0, 1, 2}, ArrayUtils.removeDuplicat(new int[]{0, 1, 2, 1, 0, 4})));
        Assert.assertFalse(Arrays.equals(new int[]{0, 1, 2}, ArrayUtils.removeDuplicat(new int[]{0, 1, 3})));
    }

    @Test
    public void testRemoveDuplicatDataEArrayComparatorOfEClassOfE() {
        assertEquals(ArrayUtils.removeDuplicat((String[]) null, null), ArrayUtils.asList());
        assertEquals(ArrayUtils.removeDuplicat(new String[]{"1"}, null), ArrayUtils.asList("1"));
        assertEquals(ArrayUtils.removeDuplicat(new String[]{"1", "1"}, null), ArrayUtils.asList("1"));
        assertEquals(ArrayUtils.removeDuplicat(new String[]{"1", "1"}, new StringComparator()), ArrayUtils.asList("1"));
        assertEquals(ArrayUtils.removeDuplicat(new String[]{"1", "2", "3"}, new StringComparator()), ArrayUtils.asList("1", "2", "3"));
        assertEquals(ArrayUtils.removeDuplicat(new String[]{"1", "2", "3", "1", "1", "3", "3", "2", "2"}, new StringComparator()), ArrayUtils.asList("1", "2", "3"));
    }

    @Test
    public void testRemoveStrFromArray() {
        String[] array = ArrayUtils.remove(new String[]{"", "1", "2"}, "1");
        assertTrue(array.length == 2 && array[0].equals("") && array[1].equals("2"));

        array = ArrayUtils.remove(new String[]{"", "1", "2"}, "3");
        assertTrue(array.length == 3 && array[0].equals("") && array[1].equals("1") && array[2].equals("2"));

        Assert.assertEquals(new String[]{"1"}, ArrayUtils.remove(new String[]{null, "1", null}, null));

        array = ArrayUtils.remove(new String[]{"", "1", "2"}, "");
        assertTrue(array.length == 2 && array[0].equals("1") && array[1].equals("2"));

        String[] newArray = ArrayUtils.remove(new String[]{"a", "b", "abc"}, "abc");
        assertEquals(-1, StringUtils.indexOf(newArray, "abc", 0, newArray.length, true));

        String[] a1 = ArrayUtils.remove(new String[]{"a"}, "a");
        assertEquals(-1, StringUtils.indexOf(a1, "a", 0, a1.length, true));

        assertNull(ArrayUtils.remove(null, ""));

        assertEquals(0, ArrayUtils.remove(new String[]{}, "").length);
    }

    @Test
    public void testSubArray() {
        assertNull(ArrayUtils.subarray(null, 0, 0));
        assertArrayEquals(ArrayUtils.subarray(new String[]{}, 0, 0), new String[0]);

        String[] strarray = {null, "1", "2", "3", "4", "5"};
        assertArrayEquals(ArrayUtils.subarray(strarray, 0, 0), new String[0]);
        assertArrayEquals(ArrayUtils.subarray(strarray, 0, 1), new String[]{(String) null});
        assertArrayEquals(ArrayUtils.subarray(strarray, 0, 2), new String[]{null, "1"});
        assertArrayEquals(ArrayUtils.subarray(strarray, 0, 3), new String[]{null, "1", "2"});
        assertArrayEquals(ArrayUtils.subarray(strarray, 0, 4), new String[]{null, "1", "2", "3"});
        assertArrayEquals(ArrayUtils.subarray(strarray, 0, 5), new String[]{null, "1", "2", "3", "4"});

        String[] array = ArrayUtils.subarray(new String[]{"0", "1", "2"}, 0, 1);
        assertTrue(array.length == 1 && array[0].equals("0"));
    }

    @Test
    public void testToHashMapProperties() {
        assertTrue(true);
    }

    @Test
    public void testCloneStringArray() {
        String[] array = ArrayUtils.copyOf(new String[]{"0", "1", "2"}, 3);
        Assert.assertEquals(new String[]{"0", "1", "2"}, array);
    }

    @Test
    public void testIsEmptyTArray() {
        String[] array = null;
        assertTrue(ArrayUtils.isEmpty(array));

        array = new String[]{};
        assertTrue(ArrayUtils.isEmpty(array));

        array = new String[]{""};
        assertFalse(ArrayUtils.isEmpty(array));
    }

    @Test
    public void testasList() {
        ArrayList<String> list = ArrayUtils.asList();
        assertTrue(list.isEmpty());

        String[] array = new String[0];
        if (array != null) {
            array = null;
        }
        list = ArrayUtils.asList(array);
        assertNull(list);
    }

    @Test
    public void testIsEmptyMapOfKV() {
        assertTrue(true);
    }

    @Test
    public void testIsEmptyCollectionOfT() {
        assertTrue(true);
    }

    @Test
    public void testIsEmptyIntArray() {
        assertTrue(true);
    }

    @Test
    public void testIsEmptyCharArray() {
        assertTrue(true);
    }

    @Test
    public void testIsEmptyByteArray() {
        assertTrue(true);
    }

    @Test
    public void testIsEmptyDoubleArray() {
        assertTrue(true);
    }

    @Test
    public void testIsEmptyFloatArray() {
        assertTrue(true);
    }

    @Test
    public void testIsEmptyLongArray() {
        assertTrue(true);
    }

    @Test
    public void testIsEmptyShortArray() {
        assertTrue(true);
    }

    @Test
    public void testToArrayTArray() {
//		String[] a = null;
//		assertTrue(H.toArray(a) == null);
//		assertTrue(true);
    }

    @Test
    public void testToArrayCollectionOfTClassOfT() {
        assertTrue(true);
    }

    @Test
    public void testToArrayIntArray() {
        assertTrue(true);
    }

    @Test
    public void testToArrayDateArray() {
        assertTrue(true);
    }

    @Test
    public void testToArrayCharArray() {
        assertTrue(true);
    }

    @Test
    public void testToListTArray() {
        assertTrue(true);
    }

    @Test
    public void testToListListOfQ() {
        Assert.assertEquals(0, ArrayUtils.as().length);
        Assert.assertEquals(1, ArrayUtils.as("").length);
        Assert.assertEquals(2, ArrayUtils.as("1", "2").length);
        Assert.assertEquals("2", ArrayUtils.as("1", "2")[1]);
    }

    @Test
    public void testFirstElementListOfT() {
        String[] array = null;
        assertNull(ArrayUtils.firstElement(array));

        array = new String[0];
        assertNull(ArrayUtils.firstElement(array));

        array = new String[]{"1"};
        assertEquals(ArrayUtils.firstElement(array), "1");

        array = new String[]{"1", "2"};
        assertEquals(ArrayUtils.firstElement(array), "1");
    }

    @Test
    public void testFirstElementTArray() {
        assertTrue(true);
    }

    @Test
    public void testLastElementTArray() {
        String[] array = null;
        assertNull(ArrayUtils.lastElement(array));

        array = new String[0];
        assertNull(ArrayUtils.lastElement(array));

        array = new String[]{"1"};
        assertEquals(ArrayUtils.lastElement(array), "1");

        array = new String[]{"1", "2"};
        assertEquals(ArrayUtils.lastElement(array), "2");
    }

    @Test
    public void testLastElementCollectionOfT() {
        assertTrue(true);
    }

    @Test
    public void testLastElementListOfT() {
        assertTrue(true);
    }

    @Test
    public void testSetFirstElement() {
        String[] array = null;
        ArrayUtils.setFirstElement(array, "1");
        assertNull(ArrayUtils.firstElement(array));

        array = new String[]{""};
        ArrayUtils.setFirstElement(array, "1");
        assertEquals(ArrayUtils.firstElement(array), "1");

        array = new String[]{"", "2"};
        ArrayUtils.setFirstElement(array, "1");
        assertEquals(ArrayUtils.firstElement(array), "1");
    }

    @Test
    public void testSetLastElement() {

    }

    @Test
    public void testGetDiffAttrVal() {
        assertTrue(true);
    }

    @Test
    public void testElementAtListOfTInt() {
        assertTrue(true);
    }

    @Test
    public void testElementAtTArrayInt() {
        assertTrue(true);
    }

    @Test
    public void testUnmodifiableProperties() {
        assertTrue(true);
    }

    @Test
    public void testJoin2Array() {
        assertTrue(true);
    }

    @Test
    public void testJoinTArrayArray() {
        assertEquals(ArrayUtils.join(new String[]{"0", "1", "2"}, 0, null), ArrayUtils.asList("0", "1", "2"));
        assertEquals(ArrayUtils.join(new String[]{"0", "1", "2"}, 0, new String[]{}), ArrayUtils.asList("0", "1", "2"));
        assertEquals(ArrayUtils.join(new String[]{"0", "1", "2"}, 0, new String[]{"-1"}), ArrayUtils.asList(new String[]{"-1", "0", "1", "2"}));
        assertEquals(ArrayUtils.join(new String[]{"0", "1", "2"}, 0, new String[]{"test"}), ArrayUtils.asList(new String[]{"test", "0", "1", "2"}));
        assertEquals(ArrayUtils.join(new String[]{"0"}, 0, new String[]{"test"}), ArrayUtils.asList(new String[]{"test", "0"}));
        assertEquals(ArrayUtils.join(new String[]{"0", "1"}, 1, new String[]{"test"}), ArrayUtils.asList(new String[]{"0", "test", "1"}));
        assertEquals(ArrayUtils.join(new String[]{"0", "1"}, 1, new String[]{"test"}), ArrayUtils.asList(new String[]{"0", "test", "1"}));

        assertEquals(ArrayUtils.join(new String[]{"0", "1"}, 1, new String[]{"test", "3"}), ArrayUtils.asList(new String[]{"0", "test", "3", "1"}));
        assertEquals(ArrayUtils.join(new String[]{"0", "1"}, 2, new String[]{"test", "3"}), ArrayUtils.asList(new String[]{"0", "1", "test", "3"}));
        assertEquals(ArrayUtils.join(new String[]{"0", "1"}, 2, new String[]{"2"}), ArrayUtils.asList(new String[]{"0", "1", "2"}));
        assertEquals(ArrayUtils.join(new String[]{"0", "1"}, 2, new String[]{}), ArrayUtils.asList(new String[]{"0", "1"}));
        assertEquals(ArrayUtils.join(new String[]{"0", "1"}, 2, null), ArrayUtils.asList(new String[]{"0", "1"}));
    }

    @Test
    public void testcopyOf() {
        String[] array = null;
        assertNull(ArrayUtils.copyOf(array, 1));

        array = new String[0];
        assertTrue(ArrayUtils.equals(ArrayUtils.copyOf(array, 0), new String[0], null));

        array = new String[]{"0"};
        assertTrue(ArrayUtils.equals(ArrayUtils.copyOf(array, 0), new String[0], null));

        array = new String[]{"0"};
        assertTrue(ArrayUtils.equals(ArrayUtils.copyOf(array, 1), new String[]{"0"}, null));

        array = new String[]{"0", "1", null};
        assertTrue(ArrayUtils.equals(ArrayUtils.copyOf(array, 2), new String[]{"0", "1"}, null));

        array = new String[]{"0", "1", null};
        assertTrue(ArrayUtils.equals(ArrayUtils.copyOf(array, 3), new String[]{"0", "1", null}, null));

    }

    @Test
    public void testJoinCollectionOfTTInt() {
        assertTrue(true);
    }

    @Test
    public void testLength() {
        assertTrue(true);
    }

    @Test
    public void testContain() {
        assertTrue(true);
    }

    @Test
    public void testContainKeyIgnoreCase() {
        assertTrue(true);
    }

    @Test
    public void testEqualsElements() {
        assertTrue(true);
    }

    @Test
    public void testtoListObject() {
//		List<String> list = H.toList((Object) H.toArray("1", "2", "3"));
//		System.out.println(ST.toString(list));
    }

    @Test
    public void testtoArrayCollectionClass() {
//		List<String> list = H.toList("1", "2", "3");
//		String[] array = H.toArray(list, String.class);
//		for (String s : array) {
//			System.out.println(s);
//		}
    }

    @Test
    public void testtoindexOf() {
        Object[] array = new Object[]{1, "str", 123.12, null, true, null};
        Assert.assertEquals(3, ArrayUtils.indexOf(array, 0, null));
        Assert.assertEquals(5, ArrayUtils.indexOf(array, 4, null));
        Assert.assertEquals(1, ArrayUtils.indexOf(array, 0, "str"));
        Assert.assertEquals(-1, ArrayUtils.indexOf(array, 0, "obj"));
    }

    @Test
    public void test100() {
        int[] array = new int[0];
        Assert.assertEquals(1, ArrayUtils.shift(array).length);
        Assert.assertEquals(0, ArrayUtils.shift(array)[0]);

        array = new int[]{1};
        Assert.assertEquals(2, ArrayUtils.shift(array).length);
        Assert.assertEquals(1, ArrayUtils.shift(array)[1]);

        array = new int[]{1, 2};
        Assert.assertEquals(3, ArrayUtils.shift(array).length);
        Assert.assertEquals(2, ArrayUtils.shift(array)[2]);
    }
}
