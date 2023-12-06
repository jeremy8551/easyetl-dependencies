package icu.etl.collection;

import icu.etl.util.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CharBufferTest {

    @Test
    public void test() {
        CharBuffer cb = new CharBuffer(20, 10);
        cb.append('0');
        cb.append('1');
        cb.append('2');
        cb.append('3');
        cb.append('4');
        cb.append('5');

//		assertTrue(cb.substring(0, 3).equals("0123"));
        assertTrue(cb.subSequence(0, 3).equals("012"));

        assertTrue(cb.substring(1, 1).equals(""));
        assertTrue(cb.substring(1, 2).equals("1"));
        assertTrue(cb.substring(0, 2).equals("01"));
        assertTrue(cb.substring(0, 0).equals(""));

        CharBuffer cb1 = new CharBuffer(20, 10).append("a").append(cb);
        assertTrue(cb1.toString().equals("a012345"));

        assertTrue(cb.append("012345", 0, 0).toString().equals("012345"));
        assertTrue(cb.append("012345", 4, 0).toString().equals("012345"));
        assertTrue(cb.append("012345", 5, 0).toString().equals("012345"));
        assertTrue(cb.append("012345", 6, 0).toString().equals("012345"));
        assertTrue(cb.append("012345", 0, 1).toString().equals("0123450"));
        assertTrue(cb.append("012345", 0, 2).toString().equals("012345001"));
        assertTrue(cb.append("012345", 0, 6).toString().equals("012345001012345"));
        assertEquals(cb.append("012345", 0, 7).toString(), "012345001012345012345");
        assertEquals(cb.append("012345", 0, 10).toString(), "012345001012345012345012345");

        Assert.assertTrue(cb.contains('5'));
        Assert.assertFalse(cb.contains('l'));

//        assertEquals(cb.rtrim('5').toString(), "01234500101234501234501234");
//        assertEquals(cb.rtrim('3', '4').toString(), "012345001012345012345012");
//        assertEquals(cb.rtrim('1').toString(), "012345001012345012345012");

        cb.clear();
//        assertEquals(cb.rtrim('1').toString(), "");
//        assertEquals(cb.rtrim().toString(), "");

        cb.append(' ').append(StringUtils.FULLWIDTH_BLANK);
//        assertEquals(cb.rtrim().toString(), "");

        cb.clear();
        cb.append("ab").append(' ').append(StringUtils.FULLWIDTH_BLANK);
//        assertEquals(cb.rtrim().toString(), "ab");

        cb.clear();
//        assertEquals(cb.append("a 2345   b").trim('a', 'b'), "2345");

    }

}
