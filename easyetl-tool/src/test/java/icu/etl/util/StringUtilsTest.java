package icu.etl.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StringUtilsTest {

    @Test
    public void testGetProtocaol() {
        try {
            URL jarUrl = new URL("jar:file:/C:/proj/parser/jar/parser.jar!/test.xml");
            Assert.assertEquals("jar", jarUrl.getProtocol());
            Assert.assertEquals("file:/C:/proj/parser/jar/parser.jar!/test.xml", jarUrl.getFile());
            URL fileUrl = new URL(jarUrl.getFile());
            Assert.assertEquals("file", fileUrl.getProtocol());
            Assert.assertEquals("/C:/proj/parser/jar/parser.jar!/test.xml", fileUrl.getFile());
            String[] parts = fileUrl.getFile().split("!");
            Assert.assertEquals("/C:/proj/parser/jar/parser.jar", parts[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testleft2() {
        Assert.assertEquals("1234567890", StringUtils.left("1234567890", 10, null, ' ')); // 判断字符串是否相等
        Assert.assertEquals("1234567890 ", StringUtils.left("1234567890", 11, null, ' ')); // 判断字符串是否相等
        Assert.assertEquals("1234567890  ", StringUtils.left("1234567890", 12, null, ' ')); // 判断字符串是否相等
    }

    @Test
    public void tesgtlastIndexOfNotBlank() {
        Assert.assertEquals(-1, StringUtils.lastIndexOfNotBlank(null, 0));
        Assert.assertEquals(0, StringUtils.lastIndexOfNotBlank("1", -1));
        Assert.assertEquals(-1, StringUtils.lastIndexOfNotBlank("1", 0));
        Assert.assertEquals(0, StringUtils.lastIndexOfNotBlank("1 ", -1));
        Assert.assertEquals(2, StringUtils.lastIndexOfNotBlank("1 1", 0));
    }

    @Test
    public void tsetsplitByBlank() {
        List<String> list = StringUtils.splitByBlank("1 2      ", 2);
        Assert.assertEquals(2, list.size());
        Assert.assertEquals("2      ", list.get(1));

        list = StringUtils.splitByBlank("1", 2);
        Assert.assertTrue(list.size() == 1 && list.get(0).equals("1"));

        list = StringUtils.splitByBlank("1 ", 2);
        Assert.assertTrue(list.size() == 2 && list.get(0).equals("1"));

        list = StringUtils.splitByBlank("1 2", 2);
        Assert.assertTrue(list.size() == 2 && list.get(0).equals("1") && list.get(1).equals("2"));

        list = StringUtils.splitByBlank("1 2   30000000    ", 2);
        Assert.assertTrue(list.size() == 2 && list.get(0).equals("1") && list.get(1).equals("2   30000000    "));

        list = StringUtils.splitByBlank("1 2   30000000    ", 1);
        Assert.assertTrue(list.size() == 1 && list.get(0).equals("1 2   30000000    "));

        list = StringUtils.splitByBlank("1", 2);
        Assert.assertTrue(list.size() == 1 && list.get(0).equals("1"));

        list = StringUtils.splitByBlank(" 1 ", 3);
        Assert.assertEquals(3, list.size());
        Assert.assertEquals("", list.get(0));
        Assert.assertEquals("1", list.get(1));
        Assert.assertEquals("", list.get(2));

        list = StringUtils.splitByBlank(" 1 ", 2);
        Assert.assertEquals(2, list.size());
        Assert.assertEquals("", list.get(0));
        Assert.assertEquals("1 ", list.get(1));

        list = StringUtils.splitByBlank(" 1 2 3 ", 2);
        Assert.assertEquals(2, list.size());
        Assert.assertEquals("", list.get(0));
        Assert.assertEquals("1 2 3 ", list.get(1));

        list = StringUtils.splitByBlank(" 1 2 3 ", 1);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals(" 1 2 3 ", list.get(0));

        list = StringUtils.splitByBlank(" 1 2 3 ", 2);
        Assert.assertEquals(2, list.size());
        Assert.assertEquals("", list.get(0));
        Assert.assertEquals("1 2 3 ", list.get(1));

        list = StringUtils.splitByBlank(" ", 3);
        Assert.assertEquals(2, list.size());
        Assert.assertEquals("", list.get(0));
        Assert.assertEquals("", list.get(1));
    }

    @Test
    public void testsplitByWhitespace() {
        Assert.assertEquals("1 2      ", StringUtils.join(StringUtils.splitByBlanks("1 2      "), ""));
        Assert.assertEquals("1 2  3    ", StringUtils.join(StringUtils.splitByBlanks("1 2  3    "), ""));
        Assert.assertEquals("1 2  3    ", StringUtils.join(StringUtils.splitByBlanks("1 2  3    "), ""));
        Assert.assertEquals(" ", StringUtils.join(StringUtils.splitByBlanks(" "), ""));
        Assert.assertEquals("1", StringUtils.join(StringUtils.splitByBlanks("1"), ""));

        List<String> list = StringUtils.splitByBlanks("1");
        Assert.assertEquals(1, list.size());
        Assert.assertEquals("1", list.get(0));

        list = StringUtils.splitByBlanks(" 1 ");
        Assert.assertEquals(5, list.size());
        Assert.assertEquals("", list.get(0));
        Assert.assertEquals(" ", list.get(1));
        Assert.assertEquals("1", list.get(2));
        Assert.assertEquals(" ", list.get(3));
        Assert.assertEquals("", list.get(4));

        list = StringUtils.splitByBlanks("0  1  2");
        Assert.assertEquals(5, list.size());
        Assert.assertEquals("0", list.get(0));
        Assert.assertEquals("  ", list.get(1));
        Assert.assertEquals("1", list.get(2));
        Assert.assertEquals("  ", list.get(3));
        Assert.assertEquals("2", list.get(4));

        list = StringUtils.splitByBlanks("  1  ");
        Assert.assertEquals(5, list.size());
        Assert.assertEquals("", list.get(0));
        Assert.assertEquals("  ", list.get(1));
        Assert.assertEquals("1", list.get(2));
        Assert.assertEquals("  ", list.get(3));
        Assert.assertEquals("", list.get(4));
    }

    @Test
    public void addLinePrefixTest() {
        StringUtils.testEncoding("测试test1234");

        Assert.assertEquals("", StringUtils.addLinePrefix("", ""));
        Assert.assertEquals("1", StringUtils.addLinePrefix("", "1"));
        Assert.assertEquals("1", StringUtils.addLinePrefix("1", ""));
        Assert.assertEquals("21", StringUtils.addLinePrefix("1", "2"));
        Assert.assertEquals("21\n", StringUtils.addLinePrefix("1\n", "2"));
        Assert.assertEquals("31\r32", StringUtils.addLinePrefix("1\r2", "3"));
        Assert.assertEquals("1\r2\n3\n4", StringUtils.addLinePrefix("1\r2\n3\n4", ""));
        Assert.assertEquals("01\r02\n03\n04", StringUtils.addLinePrefix("1\r2\n3\n4", "0"));
        Assert.assertEquals("01\r02\n03\n", StringUtils.addLinePrefix("1\r2\n3\n", "0"));
        Assert.assertEquals("01\r02\r\n03\r\n", StringUtils.addLinePrefix("1\r2\r\n3\r\n", "0"));
        Assert.assertEquals("", StringUtils.addLinePrefix("", ""));
        Assert.assertEquals("", StringUtils.addLinePrefix("", ""));
    }

    @Test
    public void splitJavaNameTest() {
        String[] array = StringUtils.splitJavaName("JavaConfigIs");
        Assert.assertTrue(array.length == 3 && "Java".equals(array[0]) && "Config".equals(array[1]) && "Is".equals(array[2]));

        array = StringUtils.splitJavaName("java");
        Assert.assertTrue(array.length == 1 && "java".equals(array[0]));

        array = StringUtils.splitJavaName("");
        Assert.assertTrue(array.length == 1 && "".equals(array[0]));

        array = StringUtils.splitJavaName("Java");
        Assert.assertTrue(array.length == 1 && "Java".equals(array[0]));
    }

    @Test
    public void removeRightLineSeparator() {
        Assert.assertEquals("", StringUtils.removeEOL("\n"));
        Assert.assertEquals("", StringUtils.removeEOL("\r\n"));
        Assert.assertEquals("", StringUtils.removeEOL("\r"));
        Assert.assertEquals("1", StringUtils.removeEOL("1\n"));
        Assert.assertEquals("2", StringUtils.removeEOL("2\r\n"));
        Assert.assertEquals("3", StringUtils.removeEOL("3\r"));
        Assert.assertEquals("1234", StringUtils.removeEOL("1234\n"));
        Assert.assertEquals("1234", StringUtils.removeEOL("1234\r\n"));
        Assert.assertEquals("1234", StringUtils.removeEOL("1234\r"));
        Assert.assertEquals("1", StringUtils.removeEOL("1"));
        Assert.assertEquals("2", StringUtils.removeEOL("2"));
        Assert.assertEquals("3", StringUtils.removeEOL("3"));
    }

    @Test
    public void testindexOfSqlWords1() {
        Assert.assertNull(StringUtils.lookupLocale(""));
        Assert.assertNotNull(StringUtils.lookupLocale("zh_CN"));
    }

    @Test
    public void test13() {
        Assert.assertEquals("value", StringUtils.getValue(new String[]{"key", "value"}, "key"));
        Assert.assertNull(StringUtils.getValue(new String[]{"key", "value", "key1"}, "key1"));
    }

    @Test
    public void test12() {
        Properties p = new Properties();
        p.setProperty("v1", "vn1");
        p.setProperty("v2", "vn2");

        Assert.assertEquals("", StringUtils.replaceProperties("", p));
        Assert.assertEquals("vn1", StringUtils.replaceProperties("${v1}", p));
        Assert.assertEquals("vn1+$", StringUtils.replaceProperties("${v1}+$", p));
        Assert.assertEquals("vn1+vn2", StringUtils.replaceProperties("${v1}+${v2}", p));
    }

    @Test
    public void test11() {
        List<String> list = new ArrayList<String>();
        StringUtils.splitVariable("", list);
        Assert.assertEquals(0, list.size());

        list.clear();
        StringUtils.splitVariable("${}", list);
        Assert.assertTrue(list.size() == 1 && list.get(0).equals(""));

        list.clear();
        StringUtils.splitVariable("${n}", list);
        Assert.assertTrue(list.size() == 1 && list.get(0).equals("n"));

        list.clear();
        StringUtils.splitVariable("${name1}", list);
        Assert.assertTrue(list.size() == 1 && list.get(0).equals("name1"));

        list.clear();
        StringUtils.splitVariable("${name}＋", list);
        Assert.assertTrue(list.size() == 1 && list.get(0).equals("name"));

        list.clear();
        StringUtils.splitVariable("${name}+${code}", list);
        Assert.assertTrue(list.size() == 2 && list.get(0).equals("name") && list.get(1).equals("code"));

        list.clear();
        StringUtils.splitVariable("${name}+${code}+}+$+${", list);
        Assert.assertTrue(list.size() == 2 && list.get(0).equals("name") && list.get(1).equals("code"));
    }

    @Test
    public void test112() {
        List<String> list = new ArrayList<String>();
        StringUtils.splitParameters("", list);
        Assert.assertEquals(0, list.size());

        list.clear();
        StringUtils.splitParameters("a ", list);
        Assert.assertTrue(list.size() == 1 && list.get(0).equals("a"));

        list.clear();
        StringUtils.splitParameters("a  ", list);
        Assert.assertTrue(list.size() == 1 && list.get(0).equals("a"));

        list.clear();
        StringUtils.splitParameters("a b", list);
        Assert.assertTrue(list.size() == 2 && list.get(0).equals("a") && list.get(1).equals("b"));

        list.clear();
        StringUtils.splitParameters("a ' ' \"\" b", list);
        Assert.assertTrue(list.size() == 4 && list.get(0).equals("a") && list.get(1).equals("' '") && list.get(2).equals("\"\"") && list.get(3).equals("b"));

        list.clear();
        StringUtils.splitParameters("a ' ' \"''\" b", list);
        Assert.assertTrue(list.size() == 4 && list.get(0).equals("a") && list.get(1).equals("' '") && list.get(2).equals("\"''\"") && list.get(3).equals("b"));
    }

    @Test
    public void test1() {
        assertNull(StringUtils.removePrefix(null, null));
        assertNull(StringUtils.removePrefix(null, ""));
        Assert.assertEquals("", StringUtils.removePrefix("", ""));
        Assert.assertEquals("", StringUtils.removePrefix(" ", " "));
        Assert.assertEquals(" ", StringUtils.removePrefix(" ", ""));
        Assert.assertEquals("", StringUtils.removePrefix("1", "1"));
        Assert.assertEquals("23", StringUtils.removePrefix("123", "1"));
        Assert.assertEquals("3", StringUtils.removePrefix("123", "12"));
        Assert.assertEquals("123", StringUtils.removePrefix("123", "3"));
    }

    @Test
    public void testTransUtf8HexString() {
        String str = StringUtils.encodeJvmUtf8HexString("中文/home/udsf/英文/名字d中文/daksdfjk0090美国/");
        Assert.assertEquals("中文/home/udsf/英文/名字d中文/daksdfjk0090美国/", StringUtils.decodeJvmUtf8HexString(str));
    }

    @Test
    public void testDecodeJvmUtf8HexString() {
        String str = StringUtils.encodeJvmUtf8HexString("中文/home/udsf/英文/名字d中文/daksdfjk0090美国/");
        Assert.assertEquals("中文/home/udsf/英文/名字d中文/daksdfjk0090美国/", StringUtils.decodeJvmUtf8HexString(StringUtils.encodeJvmUtf8HexString(str)));

        Assert.assertEquals("", StringUtils.decodeJvmUtf8HexString(""));
        Assert.assertEquals("abcdefughilmnopqrstuvwxyz", StringUtils.decodeJvmUtf8HexString("abcdefughilmnopqrstuvwxyz"));
        Assert.assertEquals("0123456789", StringUtils.decodeJvmUtf8HexString("0123456789"));
        Assert.assertEquals(" ", StringUtils.decodeJvmUtf8HexString(" "));
        Assert.assertNull(StringUtils.decodeJvmUtf8HexString(null));
    }

    @Test
    public void testDefaultString() {
        Assert.assertNull(StringUtils.defaultString(null, null));
        Assert.assertNull(StringUtils.defaultString("", null));
        Assert.assertEquals("1", StringUtils.defaultString("1", "2"));
        Assert.assertEquals("2", StringUtils.defaultString("", "2"));
        Assert.assertEquals("", StringUtils.defaultString(null, ""));
    }

    @Test
    public void testEqualsCharCharBoolean() {
        Assert.assertTrue(StringUtils.equals('a', 'A', true));
        Assert.assertFalse(StringUtils.equals('a', 'A', false));
        Assert.assertFalse(StringUtils.equals('a', 'b', true));
        Assert.assertFalse(StringUtils.equals('a', 'B', false));
        Assert.assertTrue(StringUtils.equals('中', '中', true));
    }

    @Test
    public void testIsEmptyString() {
        String str = null;
        Assert.assertTrue(StringUtils.isEmpty(str));
        Assert.assertTrue(StringUtils.isEmpty(""));
        Assert.assertFalse(StringUtils.isEmpty(" "));
        Assert.assertFalse(StringUtils.isEmpty(StringUtils.FULLWIDTH_BLANK));
        Assert.assertFalse(StringUtils.isEmpty("1"));
        Assert.assertFalse(StringUtils.isEmpty("12"));
        Assert.assertFalse(StringUtils.isEmpty("1234567890"));
    }

    @Test
    public void testIsBlankString() {
        String str = null;
        Assert.assertTrue(StringUtils.isBlank(str));
        Assert.assertTrue(StringUtils.isBlank(""));
        Assert.assertTrue(StringUtils.isBlank(" "));
        Assert.assertTrue(StringUtils.isBlank(StringUtils.FULLWIDTH_BLANK));
        Assert.assertTrue(StringUtils.isBlank(" " + StringUtils.FULLWIDTH_BLANK));
        Assert.assertTrue(StringUtils.isBlank(" " + StringUtils.FULLWIDTH_BLANK + " " + StringUtils.FULLWIDTH_BLANK));
    }

    @Test
    public void testIsBlankStringArray() {
        String[] a = new String[0];
        Assert.assertTrue(StringUtils.isBlank(a));

        a = null;
        Assert.assertTrue(StringUtils.isBlank(a));
        Assert.assertFalse(StringUtils.isBlank(new String[]{"1"}));
        Assert.assertFalse(StringUtils.isBlank(new String[]{" ", "1"}));
        Assert.assertTrue(StringUtils.isBlank(new String[]{" ", StringUtils.FULLWIDTH_BLANK, null,}));
        Assert.assertTrue(StringUtils.isBlank(new String[]{}));
        Assert.assertFalse(StringUtils.isBlank(new String[]{" ", StringUtils.FULLWIDTH_BLANK, " 1"}));
    }

    @Test
    public void testIsEmptyStringArray() {
        String[] a = new String[0];
        Assert.assertTrue(ArrayUtils.isEmpty(a));

        a = null;
        Assert.assertTrue(ArrayUtils.isEmpty(a));
        Assert.assertFalse(ArrayUtils.isEmpty(new String[]{"1"}));
        Assert.assertFalse(ArrayUtils.isEmpty(new String[]{" ", "1"}));
        Assert.assertFalse(ArrayUtils.isEmpty(new String[]{" ", StringUtils.FULLWIDTH_BLANK, null,}));
        Assert.assertTrue(ArrayUtils.isEmpty(new String[]{}));
        Assert.assertFalse(ArrayUtils.isEmpty(new String[]{" ", StringUtils.FULLWIDTH_BLANK, " 1"}));
    }

    @Test
    public void testIsNotBlank() {
        Assert.assertFalse(StringUtils.isNotBlank(null));
        Assert.assertFalse(StringUtils.isNotBlank(""));
        Assert.assertFalse(StringUtils.isNotBlank(" "));
        Assert.assertFalse(StringUtils.isNotBlank(StringUtils.FULLWIDTH_BLANK));
        Assert.assertFalse(StringUtils.isNotBlank(StringUtils.FULLWIDTH_BLANK + StringUtils.FULLWIDTH_BLANK));
        Assert.assertTrue(StringUtils.isNotBlank("1"));
        Assert.assertTrue(StringUtils.isNotBlank("12"));
        Assert.assertTrue(StringUtils.isNotBlank("1234567890"));
    }

    @Test
    public void testLastNotBlankString() {
        Assert.assertEquals(-1, StringUtils.lastIndexOfNotBlank(new String[]{}));
        Assert.assertEquals(-1, StringUtils.lastIndexOfNotBlank(new String[]{""}));
        Assert.assertEquals(-1, StringUtils.lastIndexOfNotBlank(new String[]{"", StringUtils.FULLWIDTH_BLANK, null}));
        Assert.assertEquals(-1, StringUtils.lastIndexOfNotBlank(new String[]{"", StringUtils.FULLWIDTH_BLANK, null}));
        Assert.assertEquals(-1, StringUtils.lastIndexOfNotBlank(new String[]{"", StringUtils.FULLWIDTH_BLANK, " ", null}));
        Assert.assertEquals(0, StringUtils.lastIndexOfNotBlank(new String[]{"1", "", StringUtils.FULLWIDTH_BLANK, " ", null}));
//	Assert.	assertEquals(ST.lastNotBlankString(new String[] { "1", "", ST.FULLWIDTH_BLANK, "2", " ", null }), "2");
        Assert.assertEquals(6, StringUtils.lastIndexOfNotBlank(new String[]{"1", "", StringUtils.FULLWIDTH_BLANK, "2", " ", null, "3"}));
        Assert.assertEquals(3, StringUtils.lastIndexOfNotBlank(new String[]{"1", "", StringUtils.FULLWIDTH_BLANK, "2", " ", null}));
    }

    @Test
    public void testLastIndexBlank() {
        Assert.assertEquals(-1, StringUtils.lastIndexOfBlank("0123456789", 0));
        Assert.assertEquals(0, StringUtils.lastIndexOfBlank(" 123456789", 0));
        Assert.assertEquals(0, StringUtils.lastIndexOfBlank(" 1234567" + StringUtils.FULLWIDTH_BLANK + "9", 0));
        Assert.assertEquals(8, StringUtils.lastIndexOfBlank(" 1234567" + StringUtils.FULLWIDTH_BLANK + "9", -1));
        Assert.assertEquals(9, StringUtils.lastIndexOfBlank(" 12345678" + StringUtils.FULLWIDTH_BLANK, -1));
        Assert.assertEquals(9, StringUtils.lastIndexOfBlank(" 12345678" + StringUtils.FULLWIDTH_BLANK, 9));
        Assert.assertEquals(0, StringUtils.lastIndexOfBlank(" 12345678" + StringUtils.FULLWIDTH_BLANK, 1));
    }

    @Test
    public void testLastIndex() {
        Assert.assertEquals(-1, StringUtils.lastIndexOfStr("0123456789", "tset", 0, 9, true));
        Assert.assertEquals(9, StringUtils.lastIndexOfStr("0123456789", "9", 0, 9, true));
        Assert.assertEquals(0, StringUtils.lastIndexOfStr("0123456789", "0", 0, 9, true));
        Assert.assertEquals(4, StringUtils.lastIndexOfStr("0123456789", "4", 0, 9, true));
        Assert.assertEquals(0, StringUtils.lastIndexOfStr("0123456789", "0123456789", 0, 9, true));
        Assert.assertEquals(0, StringUtils.lastIndexOfStr("012345678901234567890123456789", "012", 0, 9, true));
        Assert.assertEquals(8, StringUtils.lastIndexOfStr("01234567890123456789", "890", 0, 19, true));
        Assert.assertEquals(20, StringUtils.lastIndexOfStr("012345678901234567890123456789", "0123456789", 0, 29, true));
    }

    @Test
    public void testTrimBlank() {
        // System.out.print(ST.trim("sdf \n ") + "]");
        String[] str = {null, "1 ", "2 ", "3", " 4 ", "5  ", ""};
        Assert.assertEquals("String[1, 2, 3, 4, 5]", StringUtils.toString(StringUtils.removeBlank(str)));
        Assert.assertEquals("sdf", StringUtils.trimBlank(" 　\n sdf 　\n "));
        Assert.assertEquals("d", StringUtils.trimBlank(" 　\n sdf 　\n ", 's', 'f'));
        Assert.assertEquals("d", StringUtils.trimBlank(" 　\n sfdsf 　\n ", 's', 'f'));
        Assert.assertEquals("1", StringUtils.trimBlank("1"));
        // Exception e = new Exception();
        // check(ST.toString(e));
    }

    @Test
    public void testTrimStringCharArray() {
        Assert.assertEquals("1", StringUtils.trim("1", 'a'));
        Assert.assertEquals("1", StringUtils.trim("a1", 'a'));
        Assert.assertEquals("1", StringUtils.trim("a1a", 'a'));
        Assert.assertEquals("1", StringUtils.trim("aaa1aaa", 'a'));
        Assert.assertEquals("1a ", StringUtils.trim("aaa1a aa", 'a'));
    }

    @Test
    public void testTrimString() {
        Assert.assertNull(StringUtils.trim((String) null));
        Assert.assertEquals("1", StringUtils.trim(" 1 "));
        Assert.assertEquals("1", StringUtils.trim("  1  "));

        Assert.assertNull(StringUtils.trim((String) null));
        Assert.assertEquals("", StringUtils.trim(""));
        Assert.assertEquals("", StringUtils.trim(" "));
        Assert.assertEquals("1", StringUtils.trim(" 1 "));
        Assert.assertEquals("12", StringUtils.trim(" 12 "));
        Assert.assertEquals("12", StringUtils.trim(" 12"));
    }

    @Test
    public void testTrimQuotes() {
        Assert.assertNull(StringUtils.unquote(null));
        Assert.assertEquals("", StringUtils.unquote(""));
        Assert.assertEquals("'", StringUtils.unquote("'"));
        Assert.assertEquals("", StringUtils.unquote("''"));
        Assert.assertEquals(" ", StringUtils.unquote("' '"));
        Assert.assertEquals("1", StringUtils.unquote("'1'"));
        Assert.assertEquals("12", StringUtils.unquote("'12'"));
    }

    @Test
    public void testTrim2Quotes() {
        Assert.assertNull(StringUtils.unquotes(null));
        Assert.assertEquals("1", StringUtils.unquotes("1"));
        Assert.assertEquals("\"1", StringUtils.unquotes("\"1"));
        Assert.assertEquals("1", StringUtils.unquotes("\"1\""));
        Assert.assertEquals("\"1\" ", StringUtils.unquotes("\"1\" "));
        Assert.assertEquals(" \"1\"", StringUtils.unquotes(" \"1\""));
    }

    @Test
    public void testTrimQuotationMark() {
        Assert.assertNull(StringUtils.unquotation((String) null));
        Assert.assertEquals("1", StringUtils.unquotation("\"1\""));
        Assert.assertEquals("1", StringUtils.unquotation("'1'"));
        Assert.assertEquals("'1' ", StringUtils.unquotation("'1' "));
        Assert.assertEquals(" '1' ", StringUtils.unquotation(" '1' "));
    }

    @Test
    public void testTrimBlankAndBrace() {
        Assert.assertEquals("1", StringUtils.trimParenthes("1"));
        Assert.assertEquals("12", StringUtils.trimParenthes("12"));
        Assert.assertEquals("12", StringUtils.trimParenthes(" ( 12 ) "));
        Assert.assertEquals("( 12", StringUtils.trimParenthes(" ( 12  "));
        Assert.assertEquals("12", StringUtils.trimParenthes("  12  "));
        Assert.assertEquals("12", StringUtils.trimParenthes("  ((12))  "));
        Assert.assertEquals("12", StringUtils.trimParenthes("  ( ( 12 )  )  "));
        Assert.assertEquals("(12", StringUtils.trimParenthes("  ( ( (12 )  )  "));
    }

    @Test
    public void testTrimStrInList() {
        List<String> l1 = new ArrayList<String>();
        l1.add("1 ");
        l1.add(" 2 ");
        l1.add("3");
        l1.add("4 　");

        ArrayList<String> lc = new ArrayList<String>(l1);
        StringUtils.trim(lc);
        Assert.assertEquals("1", lc.get(0));
        Assert.assertEquals("2", lc.get(1));
        Assert.assertEquals("3", lc.get(2));
        Assert.assertEquals("4 　", lc.get(3));
    }

    @Test
    public void testTrimBlankInList() {
        List<String> l1 = new ArrayList<String>();
        l1.add("1 ");
        l1.add(" 2 ");
        l1.add("3");
        l1.add("4 　");

        ArrayList<String> lc = new ArrayList<String>(l1);
        StringUtils.trim(lc);

        lc = new ArrayList<String>(l1);
        String[] a4 = CollectionUtils.toArray(lc);
        StringUtils.trim(a4);

        lc = new ArrayList<String>(l1);
        StringUtils.trimBlank(lc);
        Assert.assertEquals("1", lc.get(0));
        Assert.assertEquals("2", lc.get(1));
        Assert.assertEquals("3", lc.get(2));
        Assert.assertEquals("4", lc.get(3));
    }

    @Test
    public void testTrimStrInArray() {
        List<String> l1 = new ArrayList<String>();
        l1.add("1 ");
        l1.add(" 2 ");
        l1.add("3");
        l1.add("4 　");

        ArrayList<String> lc = new ArrayList<String>(l1);
        StringUtils.trim(lc);
        Assert.assertEquals("1", lc.get(0));
        Assert.assertEquals("2", lc.get(1));
        Assert.assertEquals("3", lc.get(2));
        Assert.assertEquals("4 　", lc.get(3));

        lc = new ArrayList<String>(l1);
        String[] a4 = CollectionUtils.toArray(lc);
        StringUtils.trim(a4);
        Assert.assertEquals("1", a4[0]);
        Assert.assertEquals("2", a4[1]);
        Assert.assertEquals("3", a4[2]);
        Assert.assertEquals("4 　", a4[3]);
    }

    @Test
    public void testTrimBlankInArray() {
        List<String> l1 = new ArrayList<String>();
        l1.add("1 ");
        l1.add(" 2 ");
        l1.add("3");
        l1.add("4 　");

        List<String> lc = new ArrayList<String>(l1);
        StringUtils.trimBlank(lc);
        Assert.assertEquals("1", lc.get(0));
        Assert.assertEquals("2", lc.get(1));
        Assert.assertEquals("3", lc.get(2));
        Assert.assertEquals("4", lc.get(3));

        String[] a5 = CollectionUtils.toArray(lc);
        StringUtils.trimBlank(a5);
        Assert.assertEquals("1", a5[0]);
        Assert.assertEquals("2", a5[1]);
        Assert.assertEquals("3", a5[2]);
        Assert.assertEquals("4", a5[3]);
    }

    @Test
    public void testTrimQuotationMarkInArray() {
        Assert.assertEquals("1", StringUtils.trimParenthes("1"));
        Assert.assertEquals("12", StringUtils.trimParenthes("12"));
        Assert.assertEquals("12", StringUtils.trimParenthes(" ( 12 ) "));
        Assert.assertEquals("( 12", StringUtils.trimParenthes(" ( 12  "));
        Assert.assertEquals("12", StringUtils.trimParenthes("  12  "));
        Assert.assertEquals("12", StringUtils.trimParenthes("  ((12))  "));
        Assert.assertEquals("12", StringUtils.trimParenthes("  ( ( 12 )  )  "));
        Assert.assertEquals("(12", StringUtils.trimParenthes("  ( ( (12 )  )  "));
    }

    @Test
    public void testTrimStrInMapValue() {
        Map<String, String> m1 = new HashMap<String, String>();
        m1.put("1", "1");
        m1.put("2", "2 ");
        m1.put("3", " 3");
        m1.put("4", "  4 ");
        m1.put("41", "　 41 　　");
        StringUtils.trim(m1);
        assertEquals("1", m1.get("1"));
        assertEquals("2", m1.get("2"));
        assertEquals("3", m1.get("3"));
        assertEquals("4", m1.get("4"));
        assertEquals("　 41 　　", m1.get("41"));
    }

    @Test
    public void testRtrimString() {
        assertNull(StringUtils.rtrim(null));
        assertEquals("1", StringUtils.rtrim("1"));
        assertEquals("1", StringUtils.rtrim("1 "));
        assertEquals("1", StringUtils.rtrim("1  "));
        assertEquals("1  1", StringUtils.rtrim("1  1"));
        assertEquals("1  1 　", StringUtils.rtrim("1  1 　"));
    }

    @Test
    public void testRtrimStringCharArray() {
        assertEquals("1234", StringUtils.rtrim("1234", '1'));
        assertEquals("123", StringUtils.rtrim("1234", '4'));

        assertNull(StringUtils.rtrim(null, ' '));
        assertEquals("1", StringUtils.rtrim("1", ' '));
        assertEquals("1", StringUtils.rtrim("1 ", ' '));
        assertEquals("1", StringUtils.rtrim("1  ", ' '));
        assertEquals("1  1", StringUtils.rtrim("1  1", ' '));
        assertEquals("1  1 　", StringUtils.rtrim("1  1 　", ' '));

        assertEquals("1", StringUtils.rtrim("123", ' ', '2', '3'));
        assertEquals("1", StringUtils.rtrim("1aa ", ' ', 'a', '3'));

        assertEquals("1", StringUtils.rtrim("1", 'a'));
        assertEquals("1", StringUtils.rtrim("1a", 'a'));
        assertEquals("1", StringUtils.rtrim("1aa", 'a'));
        assertEquals("1aa1", StringUtils.rtrim("1aa1", 'a'));
        assertEquals("1  1", StringUtils.rtrim("1  1aaa", 'a'));
    }

    @Test
    public void testRtrimBlank() {
        Assert.assertNull(StringUtils.rtrimBlank(null));
        Assert.assertEquals("1", StringUtils.rtrimBlank("1"));
        Assert.assertEquals("1", StringUtils.rtrimBlank("1 "));
        Assert.assertEquals("1", StringUtils.rtrimBlank("1  "));
        Assert.assertEquals("1  1", StringUtils.rtrimBlank("1  1"));
        Assert.assertEquals("1  1", StringUtils.rtrimBlank("1  1 　"));
        Assert.assertEquals(" 1  1", StringUtils.rtrimBlank(" 1  1 　"));
        Assert.assertEquals("sdf", StringUtils.rtrimBlank("sdf 　\n "));
        Assert.assertEquals("sd", StringUtils.rtrimBlank("sdf 　\n ", 'f'));
    }

    @Test
    public void testLtrimString() {
        Assert.assertNull(StringUtils.ltrim(null));
        Assert.assertEquals("1", StringUtils.ltrim("1"));
        Assert.assertEquals("1 ", StringUtils.ltrim(" 1 "));
        Assert.assertEquals("1  ", StringUtils.ltrim("  1  "));
        Assert.assertEquals("1  1", StringUtils.ltrim("  1  1"));
        Assert.assertEquals("　1  1 　", StringUtils.ltrim(" 　1  1 　"));
    }

    @Test
    public void testLtrimStringCharArray() {
        Assert.assertNull(StringUtils.ltrim(null, ' '));
        Assert.assertEquals("1", StringUtils.ltrim("1", 'a'));
        Assert.assertEquals("1 ", StringUtils.ltrim("a1 ", 'a'));
        Assert.assertEquals("1  ", StringUtils.ltrim("aa1  ", 'a'));
        Assert.assertEquals("1  1", StringUtils.ltrim("aaa1  1", 'a'));
        Assert.assertEquals(" aa1  1 　", StringUtils.ltrim(" aa1  1 　", 'a'));

        Assert.assertEquals("  1aaa", StringUtils.ltrim("1  1aaa", '1', 'a'));
        Assert.assertEquals("  1aaa", StringUtils.ltrim("1aa  1aaa", '1', 'a'));
        Assert.assertEquals("234", StringUtils.ltrim("1234", '1'));
    }

    @Test
    public void testLtrimBlank() {
        Assert.assertNull(StringUtils.ltrimBlank(null));
        Assert.assertEquals("1", StringUtils.ltrimBlank("1"));
        Assert.assertEquals("1 ", StringUtils.ltrimBlank(" 1 "));
        Assert.assertEquals("1  ", StringUtils.ltrimBlank("  1  "));
        Assert.assertEquals("1  1", StringUtils.ltrimBlank("  1  1"));
        Assert.assertEquals("1  1 　", StringUtils.ltrimBlank(" 　1  1 　"));
        Assert.assertEquals("sdf 　\n ", StringUtils.ltrimBlank(" 　\n sdf 　\n "));
        Assert.assertEquals("df 　\n ", StringUtils.ltrimBlank(" 　\n sdf 　\n ", 's'));
    }

    @Test
    public void testObjToStrObject() {
        Assert.assertEquals("", StringUtils.objToStr(null));
        Assert.assertEquals("1", StringUtils.objToStr("1 "));
    }

    @Test
    public void testObjToStrObjectString() {
//		assertEquals(ST.objToStr(null, null), null);
//		assertEquals(ST.objToStr(null, ""), "");
//		assertEquals(ST.objToStr("1", ""), "1");
    }

    @Test
    public void testReplaceFirst() {
        Assert.assertEquals("1234567A1234567890", StringUtils.replace("12345678901234567890", "890", "A"));
        Assert.assertEquals("A2345678901234567890", StringUtils.replace("12345678901234567890", "1", "A"));
    }

    @Test
    public void testReplaceLast() {
        Assert.assertEquals("12345678901234567", StringUtils.replaceLast("12345678901234567890", "890", ""));
    }

    @Test
    public void testReplaceAll() {
        Assert.assertEquals("1234567A1234567A", StringUtils.replaceAll("12345678901234567890", "890", "A"));
        Assert.assertEquals("", StringUtils.replaceAll("", "890", "A"));
        Assert.assertEquals("A234567890A234567890", StringUtils.replaceAll("12345678901234567890", "1", "A"));
        Assert.assertEquals("12345678901234567890", StringUtils.replaceAll("12345678901234567890", "1", "1"));
    }

    @Test
    public void testReplaceChars() {
        Assert.assertEquals("2345678901234567890", StringUtils.replace("12345678901234567890", 0, 1, ""));
        Assert.assertEquals("1234567890", StringUtils.replace("12345678901234567890", 0, 10, ""));
        Assert.assertEquals("", StringUtils.replace("12345678901234567890", 0, 20, ""));
        Assert.assertEquals("1234567890123456789", StringUtils.replace("12345678901234567890", 19, 1, ""));
    }

    @Test
    public void testReplaceChineseAscii() {
        Assert.assertEquals("12345678901234567890", StringUtils.replaceHalfWidthChar("１2345678901234567890"));
        Assert.assertEquals("12345678901234567890", StringUtils.replaceHalfWidthChar("１234567890123456789０"));
        Assert.assertEquals("12345678901234567890 ", StringUtils.replaceHalfWidthChar("１234567890123456789０　"));
        Assert.assertEquals("(12345678901234567890 )", StringUtils.replaceHalfWidthChar("（１234567890123456789０　）"));
        Assert.assertEquals("(1测试0 )", StringUtils.replaceHalfWidthChar("（１测试０　）"));
    }

    @Test
    public void testReplaceVariableAndEnvironment() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("HOME", "test");
        map.put("T", "test");
        Assert.assertEquals("１234567890test12345test6789０", StringUtils.replaceEnvironment("１234567890${HOME}12345${T}6789０", map));
        Assert.assertEquals("１234567890test123456789０", StringUtils.replaceEnvironment("１234567890${HOME}123456789０", map));
        Assert.assertEquals("１234567890test12345${PUBLIC}6789０", StringUtils.replaceEnvironment("１234567890${HOME}12345${PUBLIC}6789０", map));

        map.put("PUBLIC", "PUBLIC");
        Assert.assertEquals("１234567890test12345PUBLIC6789０", StringUtils.replaceEnvironment("１234567890${HOME}12345${PUBLIC}6789０", map));
    }

    @Test
    public void testReplaceEnvironmentVariable() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("HOME", "test");
        map.put("T", "test");
        Assert.assertEquals("１234567890test12345test6789０", StringUtils.replaceEnvironment("１234567890${HOME}12345${T}6789０", map));
        Assert.assertEquals("１234567890test123456789０", StringUtils.replaceEnvironment("１234567890${HOME}123456789０", map));
        Assert.assertEquals("１234567890test12345${PUBLIC}6789０", StringUtils.replaceEnvironment("１234567890${HOME}12345${PUBLIC}6789０", map));

        map.put("PUBLIC", "PUBLIC");
        Assert.assertEquals("１234567890test12345PUBLIC6789０", StringUtils.replaceEnvironment("１234567890${HOME}12345${PUBLIC}6789０", map));
        Assert.assertEquals(StringUtils.replaceEnvironment("１234567890${HOME}12345${PUBLIC}6789０"), "１234567890" + Settings.getUserHome() + "12345${PUBLIC}6789０");
    }

    @Test
    public void testReplaceVariableStringMapOfStringString() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("HOME", "test");
        map.put("T", "test");
        map.put("PUBLIC", "PUBLIC");

        Assert.assertEquals("12345678901234567890", StringUtils.replaceVariable("12345678901234567890", map));
        Assert.assertEquals("１234567890test12345PUBLIC6789０test", StringUtils.replaceVariable("１234567890${HOME}12345${PUBLIC}6789０${HOME}", map));
        Assert.assertEquals("test", StringUtils.replaceVariable("${HOME}", map));
        Assert.assertEquals("testtest", StringUtils.replaceVariable("${HOME}${T}", map));
        Assert.assertEquals("", StringUtils.replaceVariable("", map));
        Assert.assertEquals("test+test", StringUtils.replaceVariable("${HOME}+${T}", map));
    }

    @Test
    public void testReplaceVariableStringMapOfObjectString() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("HOME", "test");
        map.put("T", "test");
        map.put("PUBLIC", "PUBLIC");

        Assert.assertEquals("12345678901234567890", StringUtils.replaceVariable("12345678901234567890", map, null));
        Assert.assertEquals("１234567890test12345PUBLIC6789０test", StringUtils.replaceVariable("１234567890${HOME}12345${PUBLIC}6789０${HOME}", map, null));
        Assert.assertEquals("test", StringUtils.replaceVariable("${HOME}", map, null));
        Assert.assertEquals("testtest", StringUtils.replaceVariable("${HOME}${T}", map, null));
        Assert.assertEquals("test+test", StringUtils.replaceVariable("${HOME}+${T}", map, null));
        Assert.assertEquals("test+${T1}", StringUtils.replaceVariable("${HOME}+${T1}", map, null));
    }

    @Test
    public void testReplaceVariableStringStringString() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("HOME", "test");
        map.put("T", "test");
        map.put("PUBLIC", "PUBLIC");

        Assert.assertEquals("12345678901234567890", StringUtils.replaceVariable("12345678901234567890", "HOME", "test"));
        Assert.assertEquals("test", StringUtils.replaceVariable("${HOME}", "HOME", "test"));
    }

    @Test
    public void testSubstrStringIntInt() {
        Assert.assertNull(StringUtils.substring((String) null, 0, 0, CharsetName.UTF_8));
        Assert.assertEquals("", StringUtils.substring("", 0, 0, CharsetName.UTF_8));

        boolean val = false;
        try {
            Assert.assertEquals("", StringUtils.substring("", 0, 1, CharsetName.UTF_8));
            val = true;
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        if (val) {
            throw new RuntimeException();
        }

        val = false;
        try {
            Assert.assertEquals("", StringUtils.substring("12345678901234567890", 0, 21, CharsetName.UTF_8));
            val = true;
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        if (val) {
            throw new RuntimeException();
        }

        Assert.assertEquals("", StringUtils.substring("12345678901234567890", 0, 0, CharsetName.UTF_8));
        Assert.assertEquals("1234567890", StringUtils.substring("12345678901234567890", 0, 10, CharsetName.UTF_8));
        Assert.assertEquals("12345678901234567890", StringUtils.substring("12345678901234567890", 0, 20, CharsetName.UTF_8));
    }

    @Test
    public void testSubstrStringIntIntString() {
        Assert.assertEquals("", StringUtils.substring("12345678901234567890", 0, 0, "UTF-8"));
        Assert.assertEquals("1234567890", StringUtils.substring("12345678901234567890", 0, 10, "UTF-8"));
        Assert.assertEquals("12345678901234567890", StringUtils.substring("12345678901234567890", 0, 20, "UTF-8"));

        Assert.assertEquals("一二三四五", StringUtils.substring("一二三四五六七八九十", 0, 15, "UTF-8"));
        Assert.assertNotEquals("一二三四五", StringUtils.substring("一二三四五六七八九十", 0, 16, "UTF-8"));
    }

    @Test
    public void testSubstrByteArrayIntInt() throws UnsupportedEncodingException {
//		assertTrue(StringUtils.substr("12345678901234567890".getBytes("UTF-8"), 0, 10).equals("1234567890"));
//		assertTrue(StringUtils.substr("12345678901234567890".getBytes("UTF-8"), 0, 20).equals("12345678901234567890"));
//		assertTrue(StringUtils.substr("12345678901234567890".getBytes("UTF-8"), 0, 19).equals("1234567890123456789"));
//		assertTrue(StringUtils.substr("一二三四五六七八九十".getBytes("UTF-8"), 0, 10).equals("一二三四五"));
//		assertTrue(!StringUtils.substr("一二三四五六七八九十".getBytes("UTF-8"), 0, 11).equals("一二三四五"));
    }

    @Test
    public void testSubstrByteArrayIntIntString() throws UnsupportedEncodingException {
        Assert.assertEquals("1234567890", StringUtils.substring("12345678901234567890".getBytes("UTF-8"), 0, 10, "UTF-8"));
        Assert.assertEquals("12345678901234567890", StringUtils.substring("12345678901234567890".getBytes("UTF-8"), 0, 20, "UTF-8"));
        Assert.assertEquals("1234567890123456789", StringUtils.substring("12345678901234567890".getBytes("GBK"), 0, 19, "gbk"));
        Assert.assertEquals("一二三四五", StringUtils.substring("一二三四五六七八九十".getBytes("GBK"), 0, 10, "gbk"));
        Assert.assertNotEquals("一二三四五", StringUtils.substring("一二三四五六七八九十".getBytes("GBK"), 0, 11, "gbk"));
    }

    @Test
    public void testSubstrStringIntIntInt() {
        Assert.assertEquals("012", StringUtils.substring("0123456789", 1, 1, 1));
        Assert.assertEquals("01", StringUtils.substring("0123456789", 0, 1, 1));
        Assert.assertEquals("0", StringUtils.substring("0123456789", 0, 1, 0));
        Assert.assertEquals("34567", StringUtils.substring("0123456789", 5, 2, 2));
        Assert.assertEquals("0123456789", StringUtils.substring("0123456789", 5, 5, 4));
        Assert.assertEquals("0123456789", StringUtils.substring("0123456789", 5, 6, 5));
        Assert.assertEquals("9", StringUtils.substring("0123456789", 9, 0, 10));
    }

    @Test
    public void testSubstrTrimBlank() {
        Assert.assertEquals("1", StringUtils.substr("0123456789", 1, 0, 0));
        Assert.assertEquals("5", StringUtils.substr("0123 5 789", 5, 1, 1));
        Assert.assertEquals("5", StringUtils.substr("     5    ", 5, 5, 4));
        Assert.assertEquals("5", StringUtils.substr("     5    ", 5, 6, 5));
        Assert.assertEquals("5", StringUtils.substr("5", 0, 6, 5));
    }

    @Test
    public void testLeft() {
        Assert.assertNull(StringUtils.left(null, 10));
        Assert.assertEquals("", StringUtils.left("", 10));
        Assert.assertEquals("1", StringUtils.left("1", 10));
        Assert.assertEquals("12", StringUtils.left("12", 10));
        Assert.assertEquals("1234567890", StringUtils.left("12345678901", 10));
        Assert.assertEquals("1234567890", StringUtils.left("123456789012", 10));
        Assert.assertEquals("", StringUtils.left("", 10));
    }

    @Test
    public void testLeftIgnoreChinese() {
        Assert.assertNull(StringUtils.left(null, 10, null));
        Assert.assertEquals("", StringUtils.left("", 10, null));
        Assert.assertEquals("1", StringUtils.left("1", 10, null));
        Assert.assertEquals("", StringUtils.left("1", 0, null));
        Assert.assertEquals("1234567890", StringUtils.left("1234567890", 10, null));
        Assert.assertEquals("12345678截", StringUtils.left("12345678截", 10, "GBK")); // 从字符串中截取字符串并对比结果集

        Assert.assertEquals("1234567截", StringUtils.left("1234567截取", 10, null));
        Assert.assertEquals("1234567截取", StringUtils.left("1234567截取", 11, null));
        Assert.assertEquals("中国测试", StringUtils.left("中国测试阿斯顿发", 8, null));
    }

    @Test
    public void testLeftFormatIgnoreChinese() {
        Assert.assertNull(StringUtils.left(null, 11, null, ' '));
        Assert.assertEquals("1234567890", StringUtils.left("1234567890", 10, null, ' ')); // 判断字符串是否相等
        Assert.assertEquals("1234567890 ", StringUtils.left("1234567890", 11, null, ' ')); // 判断字符串是否相等
        Assert.assertEquals("1234567890  ", StringUtils.left("1234567890", 12, null, ' ')); // 判断字符串是否相等
        Assert.assertEquals("1234567截取", StringUtils.left("1234567截取", 11, null, ' '));
        Assert.assertEquals("", StringUtils.left("中", 0, null, ' '));
        Assert.assertEquals("", StringUtils.left("a", 0, null, ' '));
    }

    @Test
    public void testLeftFormatObjectInt() {
        Assert.assertTrue(StringUtils.left(null, 1, ' ') == null);
        Assert.assertEquals("1", StringUtils.left("1234567890", 1, ' '));
        Assert.assertEquals("12", StringUtils.left("1234567890", 2, ' '));
        Assert.assertEquals("1234567890", StringUtils.left("1234567890", 10, ' '));
        Assert.assertEquals("1234567890 ", StringUtils.left("1234567890", 11, ' '));
    }

    @Test
    public void testLeftFormatObjectStringIntByte() {
//		assertTrue(StringUtils.leftFormat(null, 5, "gbk", (byte) 'x') == null);
//		Assert.assertEquals("xxxxx", StringUtils.leftFormat("", 5, "gbk", (byte) 'x'));
//		Assert.assertEquals("1xxxx", StringUtils.leftFormat("1", 5, "gbk", (byte) 'x'));
//		Assert.assertEquals("12345", StringUtils.leftFormat("1234567890", 5, "gbk", (byte) 'x'));
    }

    @Test
    public void testRight() {
        Assert.assertTrue(StringUtils.right(null, 10) == null);
        Assert.assertEquals("", StringUtils.right("", 10));
        Assert.assertEquals("1234567890", StringUtils.right("12345678901234567890", 10));
        Assert.assertEquals("12345678901234567890", StringUtils.right("12345678901234567890", 20));
        Assert.assertEquals("12345678901234567890", StringUtils.right("A12345678901234567890", 20));
    }

    @Test
    public void testRightFormatObjectInt() {
        Assert.assertNull(StringUtils.right(null, 10, ' '));
        Assert.assertEquals(" 1", StringUtils.right("1", 2, ' '));
        Assert.assertEquals(" 123", StringUtils.right("123", 4, ' '));
        Assert.assertEquals("          1234567890", StringUtils.right("1234567890", 20, ' '));
        Assert.assertEquals("1234567890", StringUtils.right("12345678901234567890", 10, ' '));
        Assert.assertEquals("1234567890", StringUtils.right("A12345678901234567890", 10, ' '));
        Assert.assertEquals("12345678901234567890", StringUtils.right("A12345678901234567890", 20, ' '));
        Assert.assertEquals("12345678901234567890", StringUtils.right("12345678901234567890", 20, ' '));
    }

    @Test
    public void testright() {
        Assert.assertNull(StringUtils.right(null, 1, StringUtils.CHARSET));
        Assert.assertEquals("", StringUtils.right("", 0, StringUtils.CHARSET));
        Assert.assertEquals("", StringUtils.right("", 1, StringUtils.CHARSET));
        Assert.assertEquals("7", StringUtils.right("01234567", 1, StringUtils.CHARSET));
        Assert.assertEquals("01234567", StringUtils.right("01234567", 8, StringUtils.CHARSET));
        Assert.assertEquals("234567中文", StringUtils.right("01234567中文", 10, StringUtils.CHARSET));
        Assert.assertEquals("文567", StringUtils.right("中文567", 6, StringUtils.CHARSET));
    }

    @Test
    public void testRightFormatObjectStringIntByte() {
        Assert.assertNull(StringUtils.right(null, 10, null, ' '));
        Assert.assertEquals(" 1", StringUtils.right("1", 2, null, ' '));
        Assert.assertEquals(" 123", StringUtils.right("123", 4, null, ' '));
        Assert.assertEquals("          1234567890", StringUtils.right("1234567890", 20, null, ' '));
        Assert.assertEquals("1234567890", StringUtils.right("12345678901234567890", 10, null, ' '));
        Assert.assertEquals("1234567890", StringUtils.right("A12345678901234567890", 10, null, ' '));
        Assert.assertEquals("12345678901234567890", StringUtils.right("A12345678901234567890", 20, null, ' '));
        Assert.assertEquals("12345678901234567890", StringUtils.right("12345678901234567890", 20, null, ' '));

        Assert.assertNull(StringUtils.right(null, 10, null, 'a'));
        Assert.assertEquals("a1", StringUtils.right("1", 2, null, 'a'));
        Assert.assertEquals("a123", StringUtils.right("123", 4, null, 'a'));
        Assert.assertEquals("aaaaaaaaaa1234567890", StringUtils.right("1234567890", 20, null, 'a'));
        Assert.assertEquals("1234567890", StringUtils.right("12345678901234567890", 10, null, 'a'));
        Assert.assertEquals("1234567890", StringUtils.right("A12345678901234567890", 10, null, 'a'));
        Assert.assertEquals("12345678901234567890", StringUtils.right("A12345678901234567890", 20, null, 'a'));
        Assert.assertEquals("12345678901234567890", StringUtils.right("12345678901234567890", 20, null, 'a'));

        Assert.assertEquals("aa中文测试", StringUtils.right("中文测试", 10, null, 'a'));
        Assert.assertEquals("测试", StringUtils.right("中文测试", 4, null, 'a'));
        Assert.assertEquals("a测试", StringUtils.right("中文测试", 5, null, 'a'));
    }

    @Test
    public void testMiddleFormatObjectInt() {
//		Assert.assertEquals("[    1     ]", ("[" + StringUtils.middleFormat("1", 10) + "]"));
//		Assert.assertEquals("[     1     ]", ("[" + StringUtils.middleFormat("1", 11) + "]"));
//		Assert.assertEquals("[  测试  ]", ("[" + StringUtils.middleFormat("测试", 10) + "]"));
//		Assert.assertEquals("[    吕    ]", ("[" + StringUtils.middleFormat("吕", 10) + "]"));
    }

    @Test
    public void testMiddleFormatObjectIntByte() {
//		Assert.assertEquals("[    1     ]", ("[" + StringUtils.middleFormat("1", 10, (byte) ' ') + "]"));
//		Assert.assertEquals("[     1     ]", ("[" + StringUtils.middleFormat("1", 11, (byte) ' ') + "]"));
//		Assert.assertEquals("[  测试  ]", ("[" + StringUtils.middleFormat("测试", 10, (byte) ' ') + "]"));
//		Assert.assertEquals("[    吕    ]", ("[" + StringUtils.middleFormat("吕", 10, (byte) ' ') + "]"));
    }

    @Test
    public void testMiddleFormatObjectStringIntByte() {
        Assert.assertEquals("[吕]", ("[" + StringUtils.middle("吕", 2, null, ' ') + "]"));
        Assert.assertEquals("[    吕    ]", ("[" + StringUtils.middle("吕", 10, null, ' ') + "]"));
        Assert.assertEquals("[    吕     ]", ("[" + StringUtils.middle("吕", 11, null, ' ') + "]"));
        Assert.assertEquals("测", StringUtils.middle("测试", 2, null, ' '));
        Assert.assertEquals("", StringUtils.middle("测试", 0, null, ' '));
        Assert.assertNull(StringUtils.middle(null, 0, null, ' '));
    }

    @Test
    public void testTranslateEscape() {
        Assert.assertEquals("0123456789\t", StringUtils.unescape("0123456789\\t"));
    }

    @Test
    public void testTranslateLineSeperator() {
        Assert.assertEquals("sdfdsf\\rdfsdf\\n", StringUtils.escapeLineSeparator("sdfdsf\rdfsdf\n"));
        Assert.assertEquals("1\\r\\n", StringUtils.escapeLineSeparator("1\r\n"));
    }

    @Test
    public void testIndexStrFromArr() {
        Assert.assertEquals(-1, StringUtils.indexOf(new String[]{}, ""));
        Assert.assertEquals(0, StringUtils.indexOf(new String[]{""}, ""));
        Assert.assertEquals(1, StringUtils.indexOf(new String[]{null, "", ""}, ""));
    }

    @Test
    public void testIndexStrIgnoreCase() {
        Assert.assertEquals(-1, StringUtils.indexOfIgnoreCase(new String[]{}, ""));
        Assert.assertEquals(1, StringUtils.indexOfIgnoreCase(new String[]{null, "", ""}, ""));
    }

    @Test
    public void testIndexStrStringStringIntBoolean() {
        Assert.assertEquals(0, StringUtils.indexOf("A", "a", 0, true));
        Assert.assertEquals(0, StringUtils.indexOf("A", "a", 0, true));
        Assert.assertEquals(1, StringUtils.indexOf("bA", "a", 0, true));
        Assert.assertEquals(1, StringUtils.indexOf("bA", "a", 1, true));
        Assert.assertEquals(2, StringUtils.indexOf("abAbc", "abc", 0, true));
        Assert.assertEquals(0, StringUtils.indexOf("abc", "A", 0, true));
        Assert.assertEquals(0, StringUtils.indexOf("abc", "Ab", 0, true));
        Assert.assertEquals(0, StringUtils.indexOf("abc", "Abc", 0, true));
        Assert.assertEquals(-1, StringUtils.indexOf("abc", "Abcd", 0, true));
        Assert.assertEquals(6, StringUtils.indexOf("abcdefABCDEF", "A", 1, true));
        Assert.assertEquals(6, StringUtils.indexOf("abcdefABCDEF", "Ab", 1, true));
        Assert.assertEquals(4, StringUtils.indexOf("abcdefABCDEF", "ef", 1, true));
        Assert.assertEquals(10, StringUtils.indexOf("abcdefABCDEF", "EF", 5, true));
    }

    @Test
    public void testIndexStrStringStringArrayIntIntBoolean() {
        Assert.assertEquals(0, StringUtils.indexOf(new String[]{"a", "b", "c", "d"}, "a", 0, 4, true));
        Assert.assertEquals(0, StringUtils.indexOf(new String[]{"a", "b", "c", "d"}, "A", 0, 4, true));
        Assert.assertEquals(3, StringUtils.indexOf(new String[]{"a", "b", "c", "d"}, "D", 0, 4, true));
        Assert.assertEquals(0, StringUtils.indexOf(new String[]{"a"}, "a", 0, 1, true));
        Assert.assertEquals(-1, StringUtils.indexOf(new String[]{"a", "b", "c", "d"}, "e", 0, 4, true));
        Assert.assertEquals(-1, StringUtils.indexOf(new String[]{}, "a", 0, 1, true));
        Assert.assertEquals(1, StringUtils.indexOf(new String[]{"a", "b", "c", "d"}, "B", 0, 1, true));

        Assert.assertEquals(0, StringUtils.indexOf(new String[]{"a", "b", "c", "d"}, "a", 0, 1, true));
        Assert.assertEquals(0, StringUtils.indexOf(new String[]{"a", "b", "c", "d"}, "a", 0, 0, true));
        Assert.assertEquals(-1, StringUtils.indexOf(new String[]{"a", "b", "c", "d"}, "B", 2, 3, true));
    }

    @Test
    public void testIndexBlank() {
        Assert.assertEquals(0, StringUtils.indexOfBlank(" ", 0, 10));
        Assert.assertEquals(3, StringUtils.indexOfBlank("123 456 789  ", 0, 10));

        Assert.assertEquals(0, StringUtils.indexOfBlank(" 123456789", 0, -1));
        Assert.assertEquals(1, StringUtils.indexOfBlank("0 23456789", 0, -1));
        Assert.assertEquals(9, StringUtils.indexOfBlank("012345678 ", 0, -1));
        Assert.assertEquals(4, StringUtils.indexOfBlank("0123 5678 ", 0, 4));
        Assert.assertEquals(9, StringUtils.indexOfBlank("012345678 ", 0, 9));
        Assert.assertEquals(-1, StringUtils.indexOfBlank("0123 5678 ", 0, 3));
    }

    @Test
    public void testIndexCharCharCharArray() {
        Assert.assertEquals(0, StringUtils.indexOf(new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'}, '0'));
        Assert.assertEquals(9, StringUtils.indexOf(new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'}, '9'));
    }

    @Test
    public void testIndexCharCharCharArrayInt() {
        Assert.assertEquals(0, StringUtils.indexOf("0123456789".toCharArray(), '0', 0));
        Assert.assertEquals(1, StringUtils.indexOf("0123456789".toCharArray(), '1', 0));
        Assert.assertEquals(9, StringUtils.indexOf("0123456789".toCharArray(), '9', 0));
    }

    @Test
    public void testIndexSqlQuotationMarkEndPos() {
        Assert.assertEquals(-1, StringUtils.indexOfQuotation("0123456789", 0, true));
        Assert.assertEquals(1, StringUtils.indexOfQuotation("0'23456789", 0, true));
        Assert.assertEquals(9, StringUtils.indexOfQuotation("012345678'", 0, true));
        Assert.assertEquals(-1, StringUtils.indexOfQuotation("0\\'23456789", 0, true));
    }

    @Test
    public void testJoinStringArrayString() {
//		SQLScriptSyntaxAnalysis p = new SQLScriptSyntaxAnalysis();
        Assert.assertEquals("|11|2|3|4|5|测试|", StringUtils.join(StringUtils.splitByBlank(" 11 2 3 4 5 测试 "), "|"));
//		Assert.assertEquals("ip 22 user passwd", StringUtils.join(p.resolveSSHLoginCmd("user@ip -p 22 -w passwd"), " "));
//		Assert.assertEquals("ip 22 user passwd", StringUtils.join(p.resolveSSHLoginCmd("-p 22 user@ip  -w passwd"), " "));
//		Assert.assertEquals("ip 22 user passwd", StringUtils.join(p.resolveSSHLoginCmd("-p 22  -w passwd  user@ip  "), " "));
//		Assert.assertEquals("ip 22 user passwd", StringUtils.join(p.resolveSSHLoginCmd(" -w passwd  user@ip  "), " "));
        Assert.assertEquals("ssh ", StringUtils.join(StringUtils.split("ssh ", " && "), " "));
        Assert.assertEquals("1 2 ", StringUtils.join(StringUtils.split("1,2,", ','), " "));
    }

    @Test
    public void testJoinObjectArrayString() {
        String[] a = null;
        Assert.assertNull(StringUtils.join(a, ","));
        Assert.assertEquals("", StringUtils.join(new String[]{}, ", "));
        Assert.assertEquals("1", StringUtils.join(new String[]{"1"}, ", "));
        Assert.assertEquals("1, 2, 3", StringUtils.join(new String[]{"1", "2", "3"}, ", "));
    }

    @Test
    public void testJoinCollectionOfQString() {
        List<String> l = new ArrayList<String>();
        Assert.assertEquals("", StringUtils.join(l, ","));

        l.add("a");
        Assert.assertEquals("a", StringUtils.join(l, ","));

        l.add("b");
        l.add("c");
        Assert.assertEquals("a,b,c", StringUtils.join(l, ","));
    }

    @Test
    public void testJoinListOfStringStringChar() {
        List<String> list = new ArrayList<String>();
        list.add("1,1");
        list.add("2");
        list.add("3");
        Assert.assertEquals("1\\,1,2,3", StringUtils.join(list, ",", '\\'));

        list = new ArrayList<String>();
        list.add("1||1");
        list.add("2");
        list.add("3");
        Assert.assertEquals("1\\||1||2||3", StringUtils.join(list, "||", '\\'));
    }

    @Test
    public void testJoinListOfStringString() {
        List<String> l = new ArrayList<String>();
        StringUtils.split("1,2,", ',', l);
        Assert.assertEquals("1 2 ", StringUtils.join(l, " "));

        l.clear();
        StringUtils.split("1,2,\\3", ',', '\\', l);
        Assert.assertEquals("1 2 3", StringUtils.join(l, " "));
    }

    @Test
    public void testJoinUseQuoteComma() {
        Assert.assertEquals("", StringUtils.joinUseQuoteComma(new String[]{}));
        Assert.assertEquals("'1'", StringUtils.joinUseQuoteComma(new String[]{"1"}));
        Assert.assertEquals("'1','2','3','4'", StringUtils.joinUseQuoteComma(new String[]{"1", "2", "3", "4"}));
    }

    @Test
    public void testSplitStringString() {
        Assert.assertEquals("ssh ", StringUtils.join(StringUtils.split("ssh ", " && "), " "));
        Assert.assertEquals("String[]", StringUtils.toString(StringUtils.split("", "||")));
        Assert.assertEquals("String[, ]", StringUtils.toString(StringUtils.split("||", "||")));
        Assert.assertEquals("String[ ,  ]", StringUtils.toString(StringUtils.split(" || ", "||")));
        Assert.assertEquals("String[1, ]", StringUtils.toString(StringUtils.split("1||", "||")));
        Assert.assertEquals("String[, 2]", StringUtils.toString(StringUtils.split("||2", "||")));
        Assert.assertEquals("String[1, 2]", StringUtils.toString(StringUtils.split("1||2", "||")));
        Assert.assertEquals("String[11, 2]", StringUtils.toString(StringUtils.split("11||2", "||")));
        Assert.assertEquals("String[1, 22]", StringUtils.toString(StringUtils.split("1||22", "||")));
        Assert.assertEquals("String[11, 22]", StringUtils.toString(StringUtils.split("11||22", "||")));
        Assert.assertEquals("String[11111, 2222]", StringUtils.toString(StringUtils.split("11111||2222", "||")));
        Assert.assertEquals("String[11111, 2222, ]", StringUtils.toString(StringUtils.split("11111||2222||", "||")));
        Assert.assertEquals("String[11111, 2222,  ]", StringUtils.toString(StringUtils.split("11111||2222|| ", "||")));
        Assert.assertEquals("String[11111, 2222, 3]", StringUtils.toString(StringUtils.split("11111||2222||3", "||")));
        Assert.assertEquals("String[11111, 3]", StringUtils.toString(StringUtils.split("11111||3", "||")));
        Assert.assertEquals("String[11111, ]", StringUtils.toString(StringUtils.split("11111||", "||")));
        Assert.assertEquals("String[, ]", StringUtils.toString(StringUtils.split("||", "||")));
        Assert.assertEquals("String[, |]", StringUtils.toString(StringUtils.split("|||", "||")));
        Assert.assertEquals("String[, , ]", StringUtils.toString(StringUtils.split("||||", "||")));
        Assert.assertEquals("String[, , , ]", StringUtils.toString(StringUtils.split("||||||", "||")));
    }

    @Test
    public void testSplitStringStringListOfString() {
        List<String> list = new ArrayList<String>();
        StringUtils.split(",1,,2,3,4\\,5,", ",", list);
        Assert.assertEquals("ArrayList[, 1, , 2, 3, 4\\, 5, ]", StringUtils.toString(list));

        list.clear();
        StringUtils.split("", ",", list);
        Assert.assertEquals("ArrayList[]", StringUtils.toString(list));

        list.clear();
        StringUtils.split("1", ",", list);
        Assert.assertEquals("ArrayList[1]", StringUtils.toString(list));

        list.clear();
        StringUtils.split("12", ",", list);
        Assert.assertEquals("ArrayList[12]", StringUtils.toString(list));

        list.clear();
        StringUtils.split("123", ",", list);
        Assert.assertEquals("ArrayList[123]", StringUtils.toString(list));

        list.clear();
        StringUtils.split("1,", ",", list);
        Assert.assertEquals("ArrayList[1, ]", StringUtils.toString(list));

        list.clear();
        StringUtils.split("1,2", ",", list);
        Assert.assertEquals("ArrayList[1, 2]", StringUtils.toString(list));

        list.clear();
        StringUtils.split("1,2,3,45,6", ",", list);
        Assert.assertEquals("ArrayList[1, 2, 3, 45, 6]", StringUtils.toString(list));

        list.clear();
        StringUtils.split("1,2,", ",", list);
        Assert.assertEquals("ArrayList[1, 2, ]", StringUtils.toString(list));
    }

    @Test
    public void testSplitStringStringBoolean() {
        String[] a1 = StringUtils.split("1and2And3 and4 and 5 and 6 ", "and", true);
        Assert.assertEquals("String[1, 2, 3 , 4 ,  5 ,  6 ]", StringUtils.toString(a1));

        String[] a2 = StringUtils.split("1||2||3||this is word", "||", true);
        Assert.assertEquals("String[1, 2, 3, this is word]", StringUtils.toString(a2));
    }

    @Test
    public void testSplitStringStringListOfStringBoolean() {
        List<String> list = new ArrayList<String>();
        StringUtils.split("1,2,3,4,5", ",", true, list);
        Assert.assertEquals("ArrayList[1, 2, 3, 4, 5]", StringUtils.toString(list));

        list.clear();
        StringUtils.split("1a2A3a4A5", ",", true, list);
        Assert.assertEquals("ArrayList[1a2A3a4A5]", StringUtils.toString(list));

        list.clear();
        StringUtils.split("a", ",", true, list);
        Assert.assertEquals("ArrayList[a]", StringUtils.toString(list));

        list.clear();
        StringUtils.split("1a2A3a4A5", "a", true, list);
        Assert.assertEquals("ArrayList[1, 2, 3, 4, 5]", StringUtils.toString(list));

        list.clear();
        StringUtils.split("1ab2Ab3aB4Ab5", "ab", true, list);
        Assert.assertEquals("ArrayList[1, 2, 3, 4, 5]", StringUtils.toString(list));
    }

    @Test
    public void testSplitStringListOfStringBoolean() {
        String[] array = StringUtils.split("table1 a left join table2 b on a.id=b.id and a.cd = b.cd inner join table3 c on a.id =c.id ", ArrayUtils.asList("JOIN", "ON"), true);
        Assert.assertEquals("String[table1 a left ,  table2 b ,  a.id=b.id and a.cd = b.cd inner ,  table3 c ,  a.id =c.id ]", StringUtils.toString(array));
    }

    @Test
    public void testSplitStringCollectionOfStringListOfStringBooleanSpliter() {
        List<String> list = new ArrayList<String>();
        StringUtils.split("", ArrayUtils.asList("||", "|", "**", "++", "&&"), true, list);
        Assert.assertTrue(list.size() == 1 && list.get(0).equals(""));

        list.clear();
        StringUtils.split("ab||cd|ef**ghi++jlm&&nopqrst", ArrayUtils.asList("||", "|", "**", "++", "&&"), true, list);

        Assert.assertEquals("ab", list.get(0));
        Assert.assertEquals("cd", list.get(1));
        Assert.assertEquals("ef", list.get(2));
        Assert.assertEquals("ghi", list.get(3));
        Assert.assertEquals("jlm", list.get(4));
        Assert.assertEquals("nopqrst", list.get(5));
    }

    @Test
    public void testSplitStringChar() {
        String[] array = StringUtils.split("|0|1|2|3|4|5|", '|');
        Assert.assertTrue(StringUtils.isEmpty(array[0]) && StringUtils.isEmpty(ArrayUtils.lastElement(array)) && array[1].equals("0") && array[2].equals("1"));
    }

    @Test
    public void testSplitStringCharListOfString() {
        List<String> list = new ArrayList<String>();
        StringUtils.split("|0|1|2|3|4|5|", '|', list);
        Assert.assertTrue(StringUtils.isEmpty(list.get(0)) && StringUtils.isEmpty(CollectionUtils.lastElement(list)) && list.get(1).equals("0") && list.get(2).equals("1"));
    }

    @Test
    public void testSplitStringCharChar() {
        String[] array = StringUtils.split("|0|1|2|3|4|5|\\|", '|', '\\');
        Assert.assertTrue(StringUtils.isEmpty(array[0]) && ArrayUtils.lastElement(array).equals("|") && array[1].equals("0") && array[2].equals("1"));
    }

    @Test
    public void testSplitStringCharCharListOfString() {
        List<String> list = new ArrayList<String>();
        StringUtils.split("|0|1|2|3|4|5|\\|", '|', '\\', list);
        Assert.assertTrue(StringUtils.isEmpty(list.get(0)) && "|".equals(CollectionUtils.lastElement(list)) && list.get(1).equals("0") && list.get(2).equals("1"));
    }

    @Test
    public void testSplitStringStringCharListOfString() {
        List<String> list = new ArrayList<String>();
        StringUtils.split("||0||1||2||3||4||5||\\||", "||", '\\', list);
        Assert.assertTrue(StringUtils.isEmpty(list.get(0)) && "||".equals(CollectionUtils.lastElement(list)) && list.get(1).equals("0") && list.get(2).equals("1"));
    }

    @Test
    public void testSplitKeyValue() {
        Assert.assertArrayEquals(StringUtils.splitProperty("key=value"), new String[]{"key", "value"});
        Assert.assertArrayEquals(StringUtils.splitProperty("key="), new String[]{"key", ""});
        Assert.assertArrayEquals(StringUtils.splitProperty("="), new String[]{"", ""});
        Assert.assertNull(StringUtils.splitProperty(""));
    }

    @Test
    public void testSplitKeyValueForceString() {
        Assert.assertArrayEquals(StringUtils.splitPropertyForce("key=value"), new String[]{"key", "value"});
        Assert.assertArrayEquals(StringUtils.splitPropertyForce("key="), new String[]{"key", ""});
        Assert.assertArrayEquals(StringUtils.splitPropertyForce("="), new String[]{"", ""});

        try {
            Arrays.equals(StringUtils.splitPropertyForce(""), new String[]{"", ""});
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testSplitKeyValueForceStringString() {
        String str = "key=value; key1=value1; key2=value2;";
        List<String[]> list = StringUtils.splitPropertyForce(str, ";");
        Assert.assertEquals(3, list.size());
        Assert.assertEquals("key", list.get(0)[0]);
        Assert.assertEquals("value", list.get(0)[1]);
        Assert.assertEquals("key2", list.get(2)[0]);
        Assert.assertEquals("value2", list.get(2)[1]);
    }

    @Test
    public void testSplitBlank() {
        Assert.assertEquals("|11|2|3|4|5|测试|", StringUtils.join(StringUtils.splitByBlank(" 11 2 3 4 5 测试 "), "|"));
        Assert.assertEquals("|11|2|3|4|5|测试|", StringUtils.join(StringUtils.splitByBlank(" 11   2     3 4     5 测试 "), "|"));
        Assert.assertEquals("11|2|3|4|5", StringUtils.join(StringUtils.splitByBlank("11   2     3 4     5"), "|"));

        String[] array = StringUtils.splitByBlank("   1   2     3 4     5 6 7   ");
        System.out.println(Arrays.toString(array));
        Assert.assertEquals(9, array.length);

    }

    @Test
    public void testToByteArray() throws UnsupportedEncodingException {
        Assert.assertEquals("中文是个语言a", new String(StringUtils.toBytes("中文是个语言a", "GBK"), "gbk"));
    }

    @Test
    public void testToUpperCaseStringArray() {
//		 Assert.assertTrue(ST.toUpperCase((String[]) null) == null);

        Assert.assertEquals("ABCABC", StringUtils.toCase("abcABC", false, null));
        Assert.assertEquals("ArrayList[A, B, C]", StringUtils.toString(StringUtils.toCase(ArrayUtils.asList("a", "b", "c"), false, null)));
        Assert.assertEquals("String[A, B, CD]", StringUtils.toString(StringUtils.toCase(new String[]{"a", "b", "cd"}, false, null)));
        Assert.assertEquals("Integer[0, 1, 2]", StringUtils.toString(new Integer[]{0, 1, 2}));
        Assert.assertEquals("int[1, 2, 3]", StringUtils.toString(new int[]{1, 2, 3}));
        Assert.assertEquals("char[A, B, C, 1]", StringUtils.toString(StringUtils.toCase(new char[]{'a', 'b', 'c', '1'}, false, null)));
    }

    @Test
    public void testToStringObject() {
//		Assert.assertEquals("", ST.toString(""));
//		Assert.assertEquals("a", ST.toString(" a "));
        Assert.assertEquals("null", StringUtils.toString(null));
        Assert.assertEquals("", StringUtils.toString(""));
        Assert.assertEquals("String[1, 2, 3]", StringUtils.toString(CollectionUtils.toArray(ArrayUtils.asList("1", "2", "3"))));

        Assert.assertEquals("2017-01-23", StringUtils.toString(Dates.parse("2017-01-23")));
        Assert.assertEquals("2017-01-23 12:34:56", StringUtils.toString(Dates.parse("2017-01-23 12:34:56")));

        Assert.assertEquals("2017-01-23 12:34", StringUtils.toString("2017-01-23 12:34"));

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("key1", "val1");
        map.put("key2", "val2");
        map.put("key3", "val3");
        Assert.assertEquals("HashMap[key1=val1, key2=val2, key3=val3]", StringUtils.toString(map));
    }

    @Test
    public void testToStringException() {
        String msg = StringUtils.toString(new RuntimeException("this"));
        Assert.assertTrue(StringUtils.isNotBlank(msg));
    }

    @Test
    public void testToStringThrowable() {
        String msg = StringUtils.toString(new RuntimeException("this").getCause());
        Assert.assertTrue(StringUtils.isNotBlank(msg));
    }

    @Test
    public void testToStringMapOfQQString() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("k1", "v1");
        map.put("k2", "v2");
        map.put("k3", "v3");
        map.put("k4", "v4");

        Assert.assertEquals("HashMap[k1=v1, k2=v2, k3=v3, k4=v4]", StringUtils.toString(map));
    }

    @Test
    public void testInArrayCharCharArray() {
        Assert.assertTrue(StringUtils.inArray('a', new char[]{'a', 'A'}));
        Assert.assertTrue(StringUtils.inArray('a', new char[]{'a', 'A', 'c'}));
        Assert.assertFalse(StringUtils.inArray('e', new char[]{'a', 'A', 'c'}));
    }

    @Test
    public void testInCollection() {
        List<String> c = new ArrayList<String>();
        c.add("a");
        c.add("b");
        c.add("c");

        Assert.assertTrue(StringUtils.inCollection("a", c, true));
        Assert.assertTrue(StringUtils.inCollection("A", c, true));
        Assert.assertTrue(StringUtils.inCollection("C", c, true));
        Assert.assertFalse(StringUtils.inCollection("A", c, false));
    }

    @Test
    public void testStartsWtih() {
        Assert.assertTrue(StringUtils.startsWith("abcdefghijk", "Cd", 2, true, true));
        Assert.assertTrue(StringUtils.startsWith("ab cdefghijk", "Cd", 2, true, true));
        Assert.assertTrue(StringUtils.startsWith("ab" + StringUtils.FULLWIDTH_BLANK + "cdefghijk", "Cd", 2, true, true));
    }

    @Test
    public void testStartsWtih3() {
        Assert.assertTrue(StringUtils.startsWith("abcdefghijk", Arrays.asList("Cd"), 2, true, true));
        Assert.assertTrue(StringUtils.startsWith("ab cdefghijk", Arrays.asList("Cd"), 2, true, true));
        Assert.assertTrue(StringUtils.startsWith("ab" + StringUtils.FULLWIDTH_BLANK + "cdefghijk", Arrays.asList("Cd"), 2, true, true));
        Assert.assertTrue(StringUtils.startsWith("ab" + StringUtils.FULLWIDTH_BLANK + "cdefghijk", Arrays.asList("e", "a", "Cd"), 2, true, true));
    }

    @Test
    public void testStartsWtih1() {
        List<String> list = Arrays.asList("abc", "ABC", "eft");
        Assert.assertTrue(StringUtils.startsWith("abcdefghijk", list, false));
        Assert.assertFalse(StringUtils.startsWith("abCdefghijk", list, false));
        Assert.assertTrue(StringUtils.startsWith("abCdefghijk", list, true));
    }

    @Test
    public void testStartsWithIgnoreCase() {
        Assert.assertTrue(StringUtils.startsWithIgnoreCase("0123456789", "0"));
        Assert.assertTrue(StringUtils.startsWithIgnoreCase("a", "A"));
        Assert.assertTrue(StringUtils.startsWithIgnoreCase("012", "012"));
        Assert.assertTrue(StringUtils.startsWithIgnoreCase("abcd", "Abc"));
        Assert.assertFalse(StringUtils.startsWithIgnoreCase("abcd", " Abc"));
    }

    @Test
    public void testStartWith() {
        Assert.assertTrue(StringUtils.startsWith("abc", "a", 0, true, true));
        Assert.assertTrue(StringUtils.startsWith("abc", "ab", 0, true, true));
        Assert.assertTrue(StringUtils.startsWith(" abc", "a", 0, true, true));
        Assert.assertTrue(StringUtils.startsWith(" \tabc", "a", 0, true, true));
        Assert.assertTrue(StringUtils.startsWith("\tabc", "a", 0, true, true));
        Assert.assertTrue(StringUtils.startsWith("\t\n\rabc", "a", 0, true, true));
        Assert.assertTrue(StringUtils.startsWith("\t\n\ra bc", "a ", 0, true, true));
        Assert.assertFalse(StringUtils.startsWith("   a abc", "a ", 5, true, true));
        Assert.assertTrue(StringUtils.startsWith("   ra abc", "a", 6, true, true));
        Assert.assertFalse(StringUtils.startsWith("   ra abc", "a", 7, true, true));
    }

    @Test
    public void testFirstCharToUpper() {
        Assert.assertNull(StringUtils.firstCharToUpper(null));
        Assert.assertEquals("TestFileIsRead", StringUtils.firstCharToUpper("testFileIsRead"));
        Assert.assertEquals("T", StringUtils.firstCharToUpper("t"));
        Assert.assertEquals("A", StringUtils.firstCharToUpper("a"));
        Assert.assertEquals("T", StringUtils.firstCharToUpper("t"));
    }

    @Test
    public void testFirstCharToLower() {
        Assert.assertEquals("t", StringUtils.firstCharToLower("T"));
        Assert.assertEquals("a", StringUtils.firstCharToLower("A"));
        Assert.assertEquals("testFileIsRead", StringUtils.firstCharToLower("TestFileIsRead"));
    }

    @Test
    public void testIsLower() {
        Assert.assertTrue(Character.isLowerCase('a'));
        Assert.assertFalse(Character.isLowerCase('A'));
    }

    @Test
    public void testIsUpper() {
        Assert.assertFalse(Character.isUpperCase('a'));
        Assert.assertTrue(Character.isUpperCase('A'));
        Assert.assertTrue(Character.isUpperCase('Z'));
    }

    @Test
    public void testHexStringToBytes() throws UnsupportedEncodingException {
        Assert.assertEquals("30313233343536373839", StringUtils.toHexString("0123456789".getBytes("GBK")));
    }

    @Test
    public void testByteToHexStringStringString() throws IOException {
        Assert.assertEquals("3031323334", StringUtils.toHexString("01234", "GBK"));
        try {
            Assert.assertEquals("3031323334", StringUtils.toRadixString("01234".getBytes("gbk"), 16));
        } catch (UnsupportedEncodingException e) {
            Assert.fail();
        }
    }

    @Test
    public void testByteToRadixString() throws UnsupportedEncodingException {
        Assert.assertEquals("1100000011000100110010001100110011010000110101", StringUtils.toRadixString("012345".getBytes("gbk"), 2));
        Assert.assertEquals("52987853747253", StringUtils.toRadixString("012345".getBytes("gbk"), 10));
    }

    @Test
    public void testByteToBinaryStringByteArray() throws UnsupportedEncodingException {
        Assert.assertEquals("1100000011000100110010001100110011010000110101", StringUtils.toBinaryString("012345".getBytes("GBK")));
    }

    @Test
    public void testByteSize() {
        Assert.assertEquals(10, StringUtils.length("中文是个ab", "gbk"));
    }

    @Test
    public void testisascii() {
        Assert.assertTrue(StringUtils.isAscii('0'));
        Assert.assertTrue(StringUtils.isAscii('a'));
        Assert.assertTrue(StringUtils.isAscii('Z'));
        Assert.assertTrue(StringUtils.isAscii('+'));
        Assert.assertTrue(StringUtils.isAscii(';'));
        Assert.assertFalse(StringUtils.isAscii('中'));
        Assert.assertFalse(StringUtils.isAscii('美'));
    }

    @Test
    public void testwidth() {
        Assert.assertEquals(0, StringUtils.width("", "UTF-8"));
        Assert.assertEquals(1, StringUtils.width("0", "UTF-8"));
        Assert.assertEquals(2, StringUtils.width("01", "UTF-8"));
        Assert.assertEquals(4, StringUtils.width("01中", "UTF-8"));
        Assert.assertEquals(4, StringUtils.width("0a中", "UTF-8"));
    }

    @Test
    public void testGetJvmFileEncoding() {
        Assert.assertTrue(StringUtils.isNotBlank(Settings.getFileEncoding()));
    }

    @Test
    public void testGetJvmVmVersion() {
        Assert.assertTrue(StringUtils.isNotBlank(Settings.getJavaVmVersion()));
    }

    @Test
    public void testGetJvmVmVendor() {
        Assert.assertTrue(StringUtils.isNotBlank(Settings.getJavaVmVendor()));
    }

    @Test
    public void testGetJvmVmName() {
        Assert.assertTrue(StringUtils.isNotBlank(Settings.getJavaVmName()));
    }

    @Test
    public void testGetJvmUserCountry() {
        Assert.assertTrue(StringUtils.isNotBlank(Settings.getUserCountry()));
    }

    @Test
    public void testGetJvmUserLanguage() {
        Assert.assertTrue(StringUtils.isNotBlank(Settings.getUserLanguage()));
    }

    @Test
    public void testGetJvmLineSeparator() {
        Assert.assertTrue(StringUtils.inArray(FileUtils.lineSeparator, "\r", "\n", "\r\n"));
    }

    @Test
    public void testGetJvmOsName() {
        Assert.assertTrue(StringUtils.isNotBlank(OSUtils.getName()));
    }

    @Test
    public void testGetJvmUserTimezone() {
        System.out.println(Settings.getUserTimezone());
//        Ensure.notBlank(Settings.getUserTimezone());
    }

    @Test
    public void testToArrayStringArray() {
        String[] array = new String[]{"", "", ""};
        Assert.assertArrayEquals(array, new String[]{"", "", ""});
    }

    @Test
    public void testToArrayCollectionOfString() {
        List<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        list.add("3");
        Assert.assertArrayEquals(CollectionUtils.toArray(list), new String[]{"1", "2", "3"});
    }

    @Test
    public void testToHalfBlank() {
        Assert.assertEquals(" ", StringUtils.replaceHalfWidthBlank(StringUtils.FULLWIDTH_BLANK));
        Assert.assertEquals(" a ", StringUtils.replaceHalfWidthBlank(" a" + StringUtils.FULLWIDTH_BLANK));
    }

    @Test
    public void testremove() {
        Assert.assertNull(StringUtils.remove(null, 0, 0));
        Assert.assertEquals("", StringUtils.remove("", 0, 1));
        Assert.assertEquals("", StringUtils.remove("0", 0, 1));
        Assert.assertEquals("1", StringUtils.remove("01", 0, 1));
        Assert.assertEquals("", StringUtils.remove("01", 0, 2));
        Assert.assertEquals("1", StringUtils.remove("01", 0, 1));
        Assert.assertEquals("0", StringUtils.remove("01", 1, 2));
        Assert.assertEquals("123456789", StringUtils.remove("0123456789", 0, 1));
        Assert.assertEquals("", StringUtils.remove("0123456789", 0, 10));

    }

    @Test
    public void testRemoveBlank() {
        Assert.assertEquals("", StringUtils.removeBlank(""));
        Assert.assertEquals("a", StringUtils.removeBlank(" a"));
        Assert.assertEquals("a", StringUtils.removeBlank(" a" + StringUtils.FULLWIDTH_BLANK));
    }

    @Test
    public void testRemoveBlankAndTrimStrInArray() {
        String[] a1 = StringUtils.removeBlank(new String[]{" ", StringUtils.FULLWIDTH_BLANK, " a " + StringUtils.FULLWIDTH_BLANK});
        Assert.assertTrue(a1.length == 1 && a1[0].equals("a"));
    }

    @Test
    public void testRemoveRightEndChar() {
        Assert.assertNull(StringUtils.removeSuffix(null));
        Assert.assertEquals("", StringUtils.removeSuffix(""));
        Assert.assertEquals("", StringUtils.removeSuffix("1"));
        Assert.assertEquals("012345678", StringUtils.removeSuffix("0123456789"));
        Assert.assertEquals("0123456789", StringUtils.removeSuffix("0123456789号"));
    }

    @Test
    public void testRemoveLeftSideChar() {
        Assert.assertTrue(true);
    }

    @Test
    public void testGetMapKeyArray() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("a", "A");
        map.put("b", "A");
        map.put("c", "A");
        String[] keys = CollectionUtils.toArray(map.keySet());
        Assert.assertArrayEquals(keys, new String[]{"a", "b", "c"});
    }

    @Test
    public void testIsNumberChar() {
        Assert.assertTrue(StringUtils.isNumber('0'));
        Assert.assertTrue(StringUtils.isNumber('1'));
        Assert.assertTrue(StringUtils.isNumber('9'));
        Assert.assertFalse(StringUtils.isNumber('\\'));
    }

    @Test
    public void testIsEnglishCharacter() {
        Assert.assertTrue(StringUtils.isLetter('a'));
        Assert.assertTrue(StringUtils.isLetter('z'));
        Assert.assertTrue(StringUtils.isLetter('A'));
        Assert.assertTrue(StringUtils.isLetter('Z'));
        Assert.assertFalse(StringUtils.isLetter('1'));
        Assert.assertFalse(StringUtils.isLetter('了'));
    }

    @Test
    public void testIsSpecialCharacter() {
        Assert.assertTrue(StringUtils.isSymbol('~'));
        Assert.assertTrue(StringUtils.isSymbol('!'));
        Assert.assertTrue(StringUtils.isSymbol('/'));
        Assert.assertFalse(StringUtils.isSymbol('l'));
    }

    @Test
    public void testIsNumberCharArray() {
        Assert.assertFalse(StringUtils.isNumber((char[]) null));
        Assert.assertFalse(StringUtils.isNumber("".toCharArray()));
        Assert.assertTrue(StringUtils.isNumber("0123456789".toCharArray()));
        Assert.assertFalse(StringUtils.isNumber("0123456789|".toCharArray()));
    }

    @Test
    public void testIsNumberString() {
        Assert.assertFalse(StringUtils.isNumber(""));
        Assert.assertFalse(StringUtils.isNumber((String) null));

        Assert.assertFalse(StringUtils.isNumber("q0123456789"));
        Assert.assertTrue(StringUtils.isNumber("0123456789"));
        Assert.assertFalse(StringUtils.isNumber("0123456789|"));
    }

    @Test
    public void testContainQuotes() {
        Assert.assertTrue(StringUtils.containsQuotation("' '"));
        Assert.assertTrue(StringUtils.containsQuotation("''"));
        Assert.assertTrue(StringUtils.containsQuotation("' '"));
        Assert.assertFalse(StringUtils.containsQuotation("'"));
        Assert.assertFalse(StringUtils.containsQuotation(""));
    }

    @Test
    public void testContain2Quotes() {
        Assert.assertTrue(StringUtils.containsDoubleQuotation("\"\""));
        Assert.assertTrue(StringUtils.containsDoubleQuotation("\"1\""));
        Assert.assertFalse(StringUtils.containsDoubleQuotation(""));
        Assert.assertFalse(StringUtils.containsDoubleQuotation(" "));
        Assert.assertFalse(StringUtils.containsDoubleQuotation("\""));
        Assert.assertFalse(StringUtils.containsDoubleQuotation("\"1"));
    }

    @Test
    public void testTestParseInt() {
        Assert.assertTrue(StringUtils.isInt("0"));
        Assert.assertTrue(StringUtils.isInt("1000"));
        Assert.assertFalse(StringUtils.isInt("0v"));
    }

    @Test
    public void testTestParseDouble() {
        Assert.assertTrue(StringUtils.isDouble("0"));
        Assert.assertTrue(StringUtils.isDouble("1000"));
        Assert.assertFalse(StringUtils.isDouble("0v"));
    }

    @Test
    public void testTestParseLong() {
        Assert.assertTrue(StringUtils.isLong("0"));
        Assert.assertTrue(StringUtils.isLong("1000"));
        Assert.assertFalse(StringUtils.isLong("0v"));
    }

    @Test
    public void testTestParseBigDecimal() {
        Assert.assertTrue(StringUtils.isDecimal("0"));
        Assert.assertTrue(StringUtils.isDecimal("1000"));
        Assert.assertFalse(StringUtils.isDecimal("0v"));
    }

    @Test
    public void testParseIntStringInt() {
        Assert.assertEquals(0, StringUtils.parseInt("0", 1));
        Assert.assertEquals(1, StringUtils.parseInt("", 1));
    }

    @Test
    public void testIndexEndOfLinePosition() {
        Assert.assertEquals(0, StringUtils.indexOfEOL("", 0));
        Assert.assertEquals(0, StringUtils.indexOfEOL("1", 0));
        Assert.assertEquals(1, StringUtils.indexOfEOL("01", 0));
        Assert.assertEquals(0, StringUtils.indexOfEOL("\r", 0));
        Assert.assertEquals(0, StringUtils.indexOfEOL("\n", 0));
        Assert.assertEquals(1, StringUtils.indexOfEOL("\r\n", 0));
        Assert.assertEquals(1, StringUtils.indexOfEOL("\r\n\r", 0));
        Assert.assertEquals(1, StringUtils.indexOfEOL("\r\n\n", 0));
        Assert.assertEquals(2, StringUtils.indexOfEOL("0\r\n\n", 0));
        Assert.assertEquals(2, StringUtils.indexOfEOL("01\n\n", 0));
        Assert.assertEquals(2, StringUtils.indexOfEOL("01\n\r", 0));
        Assert.assertEquals(3, StringUtils.indexOfEOL("01\r\n567890", 0));
        Assert.assertEquals(3, StringUtils.indexOfEOL("01\r\n567890\r", 0));
        Assert.assertEquals(3, StringUtils.indexOfEOL("01\r\n567890\n", 0));
        Assert.assertEquals(11, StringUtils.indexOfEOL("01\r\n4567890\n", 4));
        Assert.assertEquals(11, StringUtils.indexOfEOL("01\r\n4567890\n23456789\n", 4));
        Assert.assertEquals(20, StringUtils.indexOfEOL("01\r\n4567890\n23456789\n", 12));
    }

    @Test
    public void testTrimBlankMap() {
        Map<String, String> map = new HashMap<String, String>();
        Assert.assertTrue(StringUtils.trimBlank(map).isEmpty());

        map.put("key1", "vlaue1");
        map.put("key2", " vlaue1 ");
        map.put("key3", "   vlaue1   ");
        map.put("key4", StringUtils.FULLWIDTH_BLANK + "vlaue1" + StringUtils.FULLWIDTH_BLANK);
        map.put("key5", StringUtils.FULLWIDTH_BLANK + StringUtils.FULLWIDTH_BLANK + "  vlaue1  " + StringUtils.FULLWIDTH_BLANK + StringUtils.FULLWIDTH_BLANK);
        map.put("key6", null);
        StringUtils.trimBlank(map);
        Assert.assertEquals("vlaue1", map.get("key1"));
        Assert.assertEquals("vlaue1", map.get("key2"));
        Assert.assertEquals("vlaue1", map.get("key3"));
        Assert.assertEquals("vlaue1", map.get("key4"));
        Assert.assertEquals("vlaue1", map.get("key5"));
        Assert.assertNull(map.get("key6"));
    }

    @Test
    public void testSplitXmlPropertys() {
        List<Property> list = XMLUtils.splitXmlProperties(" value='' v1=\"1\" v2=  v3= 3 v4 = 4 v5 =5 ");
        Assert.assertTrue(list.get(0).getValue().equals("") && list.get(0).getKey().equals("value"));
        Assert.assertTrue(list.get(1).getValue().equals("1") && list.get(1).getKey().equals("v1"));
        Assert.assertTrue(list.get(2).getValue() == null && list.get(2).getKey().equals("v2"));
        Assert.assertTrue(list.get(3).getValue().equals(" 3") && list.get(3).getKey().equals("v3"));
        Assert.assertTrue(list.get(4).getValue().equals(" 4") && list.get(4).getKey().equals("v4"));
        Assert.assertTrue(list.get(5).getValue().equals("5") && list.get(5).getKey().equals("v5"));
        Assert.assertEquals("", XMLUtils.splitXmlProperties(" value='' ").get(0).getValue());
        Assert.assertEquals("v1", XMLUtils.splitXmlProperties(" value='' v1 = \"test\" ").get(1).getKey());
        Assert.assertEquals("test", XMLUtils.splitXmlProperties(" value='' v1 = \"test\" ").get(1).getValue());

        String s1 = " value='' v1 = \"test\" v2 v3 ";
        Assert.assertEquals("test", XMLUtils.splitXmlProperties(s1).get(1).getValue());
        Assert.assertEquals("v2", XMLUtils.splitXmlProperties(s1).get(2).getKey());
        Assert.assertEquals("v3", XMLUtils.splitXmlProperties(s1).get(3).getKey());

//		System.out.println(ST.toString(ST.splitXmlProperties(s1), " "));
    }

    @Test
    public void testremoveStringIntegerInteger() {
        Assert.assertNull(StringUtils.remove(null, 0, 0));
        Assert.assertEquals("", StringUtils.remove("", 0, 0));
        Assert.assertEquals("", StringUtils.remove("0", 0, 1));
        Assert.assertEquals("", StringUtils.remove("0123456789", 0, 10));
    }

    @Test
    public void testindexNotBlank() {
        Assert.assertEquals(-1, StringUtils.indexOfNotBlank("", 0, -1));
        Assert.assertEquals(1, StringUtils.indexOfNotBlank(" 123456789", 0, -1));
        Assert.assertEquals(8, StringUtils.indexOfNotBlank("        8 ", 0, -1));
        Assert.assertEquals(9, StringUtils.indexOfNotBlank("         9 ", 0, -1));
    }

    @Test
    public void testparseContentTypeCharset() {
        Assert.assertEquals("gbk", NetUtils.parseContentTypeCharset("application/soap+xml; charset=gbk"));
        Assert.assertEquals("gbk", NetUtils.parseContentTypeCharset("application/soap+xml; charset= gbk"));
        Assert.assertEquals("gbk", NetUtils.parseContentTypeCharset("application/soap+xml; charset = gbk"));
        Assert.assertEquals("gbk", NetUtils.parseContentTypeCharset("application/soap+xml; charset = gbk "));
        Assert.assertEquals("gbk", NetUtils.parseContentTypeCharset("application/soap+xml; charset =  gbk "));
        Assert.assertEquals("gbk", NetUtils.parseContentTypeCharset("application/soap+xml; charset =  gbk"));
        Assert.assertNull(NetUtils.parseContentTypeCharset("application/soap+xml; charset =   "));
        Assert.assertNull(NetUtils.parseContentTypeCharset("application/soap+xml;  =   "));
        Assert.assertNull(NetUtils.parseContentTypeCharset("application/soap+xml;  charset  "));
        Assert.assertNull(NetUtils.parseContentTypeCharset("application/soap+xml;  charset gbk "));
    }

    /**
     * 测试36进制文件序号生成程序
     */
    @Test
    public void testtoBatchNo() {
        Assert.assertEquals("000", StringUtils.toHexadecimalString(0, 3));
        Assert.assertEquals("001", StringUtils.toHexadecimalString(1, 3));
        Assert.assertEquals("999", StringUtils.toHexadecimalString(999, 3));
        Assert.assertEquals("99A", StringUtils.toHexadecimalString(1000, 3));
        Assert.assertEquals("9BZ", StringUtils.toHexadecimalString(1097, 3));
        Assert.assertEquals("9AZ", StringUtils.toHexadecimalString(1061, 3));
        Assert.assertEquals("A00", StringUtils.toHexadecimalString(1962, 3));
        Assert.assertEquals("99Z", StringUtils.toHexadecimalString(1025, 3));
        Assert.assertEquals("9C0", StringUtils.toHexadecimalString(1098, 3));
        Assert.assertEquals("ZZZ", StringUtils.toHexadecimalString(35657, 3));
        Assert.assertEquals("AAA", StringUtils.toHexadecimalString(2332, 3));
        Assert.assertEquals("B00", StringUtils.toHexadecimalString(3258, 3));

        for (int val = 0; val <= 100000; val++) {
            String str = StringUtils.toHexadecimalString(val, 4);
            int v = StringUtils.parseHexadecimal(str);

            Assert.assertEquals(val, v);
        }
    }

    @Test
    public void testSplitLines() {
        List<CharSequence> l1 = StringUtils.splitLines("", null);
        Assert.assertEquals(1, l1.size());
        Assert.assertEquals("", l1.get(0));

        l1 = StringUtils.splitLines(" ", null);
        Assert.assertEquals(1, l1.size());
        Assert.assertEquals(" ", l1.get(0));

        l1 = StringUtils.splitLines("12345", null);
        Assert.assertEquals(1, l1.size());
        Assert.assertEquals("12345", l1.get(0));

        l1 = StringUtils.splitLines("1\n2\r3\r\n4\n5", null);
        Assert.assertEquals(5, l1.size());
        Assert.assertEquals("1", l1.get(0));
        Assert.assertEquals("2", l1.get(1));
        Assert.assertEquals("3", l1.get(2));
        Assert.assertEquals("4", l1.get(3));
        Assert.assertEquals("5", l1.get(4));
    }

    @Test
    public void test100() {
        Assert.assertEquals(32, StringUtils.toRandomUUID().length());
    }

    @Test
    public void testGetLongestString() {
        Assert.assertEquals(4, StringUtils.maxlength("123", "1234", "1"));
    }

    @Test
    public void testtoStringArray() {
        char[] chars = "1234".toCharArray();
        String[] sa = StringUtils.toStringArray(chars);
        Assert.assertEquals("1", sa[0]);
        Assert.assertEquals("4", sa[3]);
    }

    @Test
    public void testappend() {
        String[] a = {};
        String[] sa = StringUtils.append(a, "a", "bc");
        Assert.assertEquals("a", sa[0]);
        Assert.assertEquals("bc", sa[1]);

        String[] b = {"1"};
        sa = StringUtils.append(b, "a", "bc");
        Assert.assertEquals("1", sa[0]);
        Assert.assertEquals("a", sa[1]);
        Assert.assertEquals("bc", sa[2]);
    }

}
