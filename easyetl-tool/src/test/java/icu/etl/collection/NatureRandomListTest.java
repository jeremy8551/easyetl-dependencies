package icu.etl.collection;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import icu.etl.util.StringUtils;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class NatureRandomListTest {

    @Test
    public void test() {
        RandomAccessList<String> list = new RandomAccessList<String>();
        assertTrue(list.size() == 0);

        list.add("1");
        assertTrue(list.size() == 1);
        assertTrue(list.get(0).equals("1"));

        list.add("2");
        assertTrue(list.get(1).equals("2"));

        list.add("3");
        list.add("吕钊军");

        assertTrue(list.size() == 4);
        RandomAccessList<String> clone = (RandomAccessList<String>) list.clone();
        assertTrue(StringUtils.toString(clone).equals("RandomAccessList[1, 2, 3, 吕钊军]"));

        list.add(0, "0");
        assertTrue(list.get(0).equals("0"));
        assertTrue(list.get(1).equals("1"));

        list.clear();
        assertTrue(list.isEmpty());
        assertTrue(list.size() == 0);

        list.addAll(clone);
        assertTrue(StringUtils.toString(list).equals("RandomAccessList[1, 2, 3, 吕钊军]"));

        list.addAll(0, clone);
        assertTrue(StringUtils.toString(list).equals("RandomAccessList[1, 2, 3, 吕钊军, 1, 2, 3, 吕钊军]"));

        list.clear();
        list.addAll(clone);
        list.addAll(4, clone);
        assertTrue(StringUtils.toString(list).equals("RandomAccessList[1, 2, 3, 吕钊军, 1, 2, 3, 吕钊军]"));

        list.clear();
        list.addAll(clone);
        list.addAll(2, clone);
        assertTrue(StringUtils.toString(list).equals("RandomAccessList[1, 2, 1, 2, 3, 吕钊军, 3, 吕钊军]"));

        list.clear();
        list.addAll(clone);
        assertTrue(list.equals(clone));

        assertTrue(list.indexOf("1") == 0);
        assertTrue(list.indexOf("吕钊军") == 3);

        list.clear();
        list.addAll(clone);
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String str = it.next();
            if (str.equals("吕钊军")) {
                it.remove();
            }
            if (str.equals("2")) {
                it.remove();
            }
        }
        assertTrue(StringUtils.toString(list).equals("RandomAccessList[1, 3]"));

        list.clear();
        list.addAll(clone);
        it = list.iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
        assertTrue(list.isEmpty());

        list.clear();
        list.addAll(clone);
        assertTrue(list.lastIndexOf("1") == 0);
        assertTrue(list.lastIndexOf("吕钊军") == 3);

        list.clear();
        list.addAll(clone);
        list.remove("1");
        assertTrue(StringUtils.toString(list).equals("RandomAccessList[2, 3, 吕钊军]"));
        list.remove("吕钊军");
        assertTrue(StringUtils.toString(list).equals("RandomAccessList[2, 3]"));

        list.clear();
        list.addAll(clone);
        list.remove(0);
        assertTrue(StringUtils.toString(list).equals("RandomAccessList[2, 3, 吕钊军]"));
        list.remove(2);
        assertTrue(StringUtils.toString(list).equals("RandomAccessList[2, 3]"));

        list.clear();
        list.addAll(clone);
        list.set(0, "4");
        assertTrue(StringUtils.toString(list).equals("RandomAccessList[4, 2, 3, 吕钊军]"));
        list.set(3, "4");
        assertTrue(StringUtils.toString(list).equals("RandomAccessList[4, 2, 3, 4]"));

        list.clear();
        list.addAll(clone);
        List<String> subList = list.subList(0, 1);
        assertTrue(StringUtils.toString(subList).equals("RandomAccessSubList[1]"));
        subList = list.subList(0, 0);
        assertTrue(StringUtils.toString(subList).equals("RandomAccessSubList[]"));
        subList = list.subList(0, 4);
        assertTrue(StringUtils.toString(subList).equals("RandomAccessSubList[1, 2, 3, 吕钊军]"));

        String[] array = new String[list.size()];
        list.toArray(array);
        assertTrue(StringUtils.toString(array).equals("String[1, 2, 3, 吕钊军]"));

        Object[] array1 = list.toArray();
        assertTrue(StringUtils.toString(array1).equals("Object[1, 2, 3, 吕钊军]"));

        list.clear();
        list.addAll(clone);
        list.removeAll(clone);
        assertTrue(list.isEmpty());

        list.clear();
        list.addAll(clone);
        list.retainAll(clone);
        assertTrue(StringUtils.toString(list).equals("RandomAccessList[1, 2, 3, 吕钊军]"));

        list.clear();
        list.addAll(clone);
        // clone.remove("吕钊军");
        // list.retainAll(clone);
        // assertTrue(ST.toString(list, " ").equals("RandomAccessList[1, 2, 3]"));
        assertTrue(list.containsAll(clone));

        list.expandCapacity(10);
        assertTrue(list.size() == 4);

        StringBuilder buf = new StringBuilder();
        list.clear();
        list.addAll(clone);
        ListIterator<String> lt = list.listIterator();
        while (lt.hasNext()) {
            buf.append(lt.next());
        }
        assertTrue(buf.toString().equals("123"));

        buf.setLength(0);
        list.clear();
        list.addAll(clone);
        lt = list.listIterator(1);
        while (lt.hasNext()) {
            buf.append(lt.next());
        }
        assertTrue(buf.toString().equals("23"));

        list.clear();
        list.addAll(clone);
//		assertTrue(list.toString());
    }
}
