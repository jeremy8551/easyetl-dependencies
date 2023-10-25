package icu.etl.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StringUtilsTest {

    @Test
    public void testGetProtocaol() {
        try {
            URL jarUrl = new URL("jar:file:/C:/proj/parser/jar/parser.jar!/test.xml");
            assertEquals("jar", jarUrl.getProtocol());
            assertEquals("file:/C:/proj/parser/jar/parser.jar!/test.xml", jarUrl.getFile());
            URL fileUrl = new URL(jarUrl.getFile());
            assertEquals("file", fileUrl.getProtocol());
            assertEquals("/C:/proj/parser/jar/parser.jar!/test.xml", fileUrl.getFile());
            String[] parts = fileUrl.getFile().split("!");
            assertEquals("/C:/proj/parser/jar/parser.jar", parts[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void tesgtlastIndexOfNotBlank() {
        assertTrue(StringUtils.lastIndexOfNotBlank(null, 0) == -1);
        assertTrue(StringUtils.lastIndexOfNotBlank("", -1) == -1);
        assertTrue(StringUtils.lastIndexOfNotBlank("1", -1) == 0);
        assertTrue(StringUtils.lastIndexOfNotBlank("1", 0) == -1);
        assertTrue(StringUtils.lastIndexOfNotBlank("1 ", -1) == 0);
        assertTrue(StringUtils.lastIndexOfNotBlank("1 1", 0) == 2);
    }

    @Test
    public void tsetsplitByBlank() {
        List<String> a = StringUtils.splitByBlank("1 2      ", 2);
        assertTrue(a.size() == 2 && a.get(1).equals("2      "));

        a = StringUtils.splitByBlank("1", 2);
        assertTrue(a.size() == 1 && a.get(0).equals("1"));

        a = StringUtils.splitByBlank("1 ", 2);
        assertTrue(a.size() == 2 && a.get(0).equals("1"));

        a = StringUtils.splitByBlank("1 2", 2);
        assertTrue(a.size() == 2 && a.get(0).equals("1") && a.get(1).equals("2"));

        a = StringUtils.splitByBlank("1 2   30000000    ", 2);
        assertTrue(a.size() == 2 && a.get(0).equals("1") && a.get(1).equals("2   30000000    "));

        a = StringUtils.splitByBlank("1 2   30000000    ", 1);
        assertTrue(a.size() == 1 && a.get(0).equals("1 2   30000000    "));

        a = StringUtils.splitByBlank("1", 2);
        assertTrue(a.size() == 1 && a.get(0).equals("1"));
    }

    @Test
    public void testsplitByWhitespace() {
        assertTrue(StringUtils.join(StringUtils.splitByBlanks("1 2      "), "").equals("1 2      "));
        assertTrue(StringUtils.join(StringUtils.splitByBlanks("1 2  3    "), "").equals("1 2  3    "));
        assertTrue(StringUtils.join(StringUtils.splitByBlanks("1 2  3    "), "").equals("1 2  3    "));
        assertTrue(StringUtils.join(StringUtils.splitByBlanks(" "), "").equals(" "));
        assertTrue(StringUtils.join(StringUtils.splitByBlanks("1"), "").equals("1"));
    }

    @Test
    public void addLinePrefixTest() {
        StringUtils.testEncoding("测试test1234");

        assertTrue(StringUtils.addLinePrefix("", "").equals(""));
        assertTrue(StringUtils.addLinePrefix("", "1").equals("1"));
        assertTrue(StringUtils.addLinePrefix("1", "").equals("1"));
        assertTrue(StringUtils.addLinePrefix("1", "2").equals("21"));
        assertTrue(StringUtils.addLinePrefix("1\n", "2").equals("21\n"));
        assertTrue(StringUtils.addLinePrefix("1\r2", "3").equals("31\r32"));
        assertTrue(StringUtils.addLinePrefix("1\r2\n3\n4", "").equals("1\r2\n3\n4"));
        assertTrue(StringUtils.addLinePrefix("1\r2\n3\n4", "0").equals("01\r02\n03\n04"));
        assertTrue(StringUtils.addLinePrefix("1\r2\n3\n", "0").equals("01\r02\n03\n"));
        assertTrue(StringUtils.addLinePrefix("1\r2\r\n3\r\n", "0").equals("01\r02\r\n03\r\n"));
        assertTrue(StringUtils.addLinePrefix("", "").equals(""));
        assertTrue(StringUtils.addLinePrefix("", "").equals(""));
    }

    @Test
    public void splitJavaNameTest() {
        String[] array = StringUtils.splitJavaName("JavaConfigIs");
        Ensure.isTrue(array.length == 3 && "Java".equals(array[0]) && "Config".equals(array[1]) && "Is".equals(array[2]), StringUtils.toString(array));

        array = StringUtils.splitJavaName("java");
        Ensure.isTrue(array.length == 1 && "java".equals(array[0]));

        array = StringUtils.splitJavaName("");
        Ensure.isTrue(array.length == 1 && "".equals(array[0]));

        array = StringUtils.splitJavaName("Java");
        Ensure.isTrue(array.length == 1 && "Java".equals(array[0]));
    }

    @Test
    public void removeRightLineSeparator() {
        assertTrue(StringUtils.removeEOL("\n").equals(""));
        assertTrue(StringUtils.removeEOL("\r\n").equals(""));
        assertTrue(StringUtils.removeEOL("\r").equals(""));
        assertTrue(StringUtils.removeEOL("1\n").equals("1"));
        assertTrue(StringUtils.removeEOL("2\r\n").equals("2"));
        assertTrue(StringUtils.removeEOL("3\r").equals("3"));
        assertTrue(StringUtils.removeEOL("1234\n").equals("1234"));
        assertTrue(StringUtils.removeEOL("1234\r\n").equals("1234"));
        assertTrue(StringUtils.removeEOL("1234\r").equals("1234"));
        assertTrue(StringUtils.removeEOL("1").equals("1"));
        assertTrue(StringUtils.removeEOL("2").equals("2"));
        assertTrue(StringUtils.removeEOL("3").equals("3"));

    }

    @Test
    public void testindexOfSqlWords1() {
        assertTrue(StringUtils.lookupLocale("") == null);
        assertTrue(StringUtils.lookupLocale("zh_CN") != null);
    }

    @Test
    public void test13() {
        assertTrue(StringUtils.getValue(new String[]{"key", "value"}, "key").equals("value"));
        assertTrue(StringUtils.getValue(new String[]{"key", "value", "key1"}, "key1") == null);
    }

    @Test
    public void test12() {
        Properties p = new Properties();
        p.setProperty("v1", "vn1");
        p.setProperty("v2", "vn2");

        assertTrue(StringUtils.replaceProperties("", p).equals(""));
        assertTrue(StringUtils.replaceProperties("${v1}", p).equals("vn1"));
        assertTrue(StringUtils.replaceProperties("${v1}+$", p).equals("vn1+$"));
        assertTrue(StringUtils.replaceProperties("${v1}+${v2}", p).equals("vn1+vn2"));
    }

    @Test
    public void test11() {
        List<String> list = new ArrayList<String>();
        StringUtils.splitVariable("", list);
        assertTrue(list.size() == 0);

        list.clear();
        StringUtils.splitVariable("${}", list);
        assertTrue(list.size() == 1 && list.get(0).equals(""));

        list.clear();
        StringUtils.splitVariable("${n}", list);
        assertTrue(list.size() == 1 && list.get(0).equals("n"));

        list.clear();
        StringUtils.splitVariable("${name1}", list);
        assertTrue(list.size() == 1 && list.get(0).equals("name1"));

        list.clear();
        StringUtils.splitVariable("${name}＋", list);
        assertTrue(list.size() == 1 && list.get(0).equals("name"));

        list.clear();
        StringUtils.splitVariable("${name}+${code}", list);
        assertTrue(list.size() == 2 && list.get(0).equals("name") && list.get(1).equals("code"));

        list.clear();
        StringUtils.splitVariable("${name}+${code}+}+$+${", list);
        assertTrue(list.size() == 2 && list.get(0).equals("name") && list.get(1).equals("code"));
    }

    @Test
    public void test112() {
        List<String> list = new ArrayList<String>();
        StringUtils.splitParameters("", list);
        assertTrue(list.size() == 0);

        list.clear();
        StringUtils.splitParameters("a ", list);
        assertTrue(list.size() == 1 && list.get(0).equals("a"));

        list.clear();
        StringUtils.splitParameters("a  ", list);
        assertTrue(list.size() == 1 && list.get(0).equals("a"));

        list.clear();
        StringUtils.splitParameters("a b", list);
        assertTrue(list.size() == 2 && list.get(0).equals("a") && list.get(1).equals("b"));

        list.clear();
        StringUtils.splitParameters("a ' ' \"\" b", list);
        assertTrue(list.size() == 4 && list.get(0).equals("a") && list.get(1).equals("' '") && list.get(2).equals("\"\"") && list.get(3).equals("b"));

        list.clear();
        StringUtils.splitParameters("a ' ' \"''\" b", list);
        assertTrue(list.size() == 4 && list.get(0).equals("a") && list.get(1).equals("' '") && list.get(2).equals("\"''\"") && list.get(3).equals("b"));
    }

    @Test
    public void test1() {
        assertTrue(StringUtils.removePrefix(null, null) == null);
        assertTrue(StringUtils.removePrefix(null, "") == null);
        assertTrue(StringUtils.removePrefix("", "").equals(""));
        assertTrue(StringUtils.removePrefix(" ", " ").equals(""));
        assertTrue(StringUtils.removePrefix(" ", "").equals(" "));
        assertTrue(StringUtils.removePrefix("1", "1").equals(""));
        assertTrue(StringUtils.removePrefix("123", "1").equals("23"));
        assertTrue(StringUtils.removePrefix("123", "12").equals("3"));
        assertTrue(StringUtils.removePrefix("123", "3").equals("123"));
    }

    @Test
    public void testTransUtf8HexString() {
        String str = StringUtils.encodeJvmUtf8HexString("中文/home/udsf/英文/名字d中文/daksdfjk0090美国/");
        assertTrue(StringUtils.decodeJvmUtf8HexString(str).equals("中文/home/udsf/英文/名字d中文/daksdfjk0090美国/"));
    }

    @Test
    public void testDecodeJvmUtf8HexString() {
        String str = StringUtils.encodeJvmUtf8HexString("中文/home/udsf/英文/名字d中文/daksdfjk0090美国/");
        assertTrue(StringUtils.decodeJvmUtf8HexString(StringUtils.encodeJvmUtf8HexString(str)).equals("中文/home/udsf/英文/名字d中文/daksdfjk0090美国/"));

        assertTrue(StringUtils.decodeJvmUtf8HexString("").equals(""));
        assertTrue(StringUtils.decodeJvmUtf8HexString("abcdefughilmnopqrstuvwxyz").equals("abcdefughilmnopqrstuvwxyz"));
        assertTrue(StringUtils.decodeJvmUtf8HexString("0123456789").equals("0123456789"));
        assertTrue(StringUtils.decodeJvmUtf8HexString(" ").equals(" "));
        assertTrue(StringUtils.decodeJvmUtf8HexString(null) == null);
    }

    @Test
    public void testDefaultString() {
        assertTrue(StringUtils.defaultString(null, null) == null);
        assertTrue(StringUtils.defaultString("", null) == null);
        assertTrue(StringUtils.defaultString("1", "2").equals("1"));
        assertTrue(StringUtils.defaultString("", "2").equals("2"));
        assertTrue(StringUtils.defaultString(null, "").equals(""));
    }

    @Test
    public void testEqualsCharCharBoolean() {
        assertTrue(StringUtils.equals('a', 'A', true));
        assertFalse(StringUtils.equals('a', 'A', false));
        assertFalse(StringUtils.equals('a', 'b', true));
        assertFalse(StringUtils.equals('a', 'B', false));
        assertTrue(StringUtils.equals('中', '中', true));
    }

    @Test
    public void testIsEmptyString() {
        String str = null;
        assertTrue(StringUtils.isEmpty(str));
        assertTrue(StringUtils.isEmpty(""));
        assertFalse(StringUtils.isEmpty(" "));
        assertFalse(StringUtils.isEmpty(StringUtils.FULLWIDTH_BLANK));
        assertFalse(StringUtils.isEmpty("1"));
        assertFalse(StringUtils.isEmpty("12"));
        assertFalse(StringUtils.isEmpty("1234567890"));
    }

    @Test
    public void testIsBlankString() {
        String str = null;
        assertTrue(StringUtils.isBlank(str));
        assertTrue(StringUtils.isBlank(""));
        assertTrue(StringUtils.isBlank(" "));
        assertTrue(StringUtils.isBlank(StringUtils.FULLWIDTH_BLANK));
        assertTrue(StringUtils.isBlank(" " + StringUtils.FULLWIDTH_BLANK));
        assertTrue(StringUtils.isBlank(" " + StringUtils.FULLWIDTH_BLANK + " " + StringUtils.FULLWIDTH_BLANK));
    }

    @Test
    public void testIsBlankStringArray() {
        String[] a = new String[0];
        assertTrue(StringUtils.isBlank(a));

        a = null;
        assertTrue(StringUtils.isBlank(a));
        assertFalse(StringUtils.isBlank(new String[]{"1"}));
        assertFalse(StringUtils.isBlank(new String[]{" ", "1"}));
        assertTrue(StringUtils.isBlank(new String[]{" ", StringUtils.FULLWIDTH_BLANK, null,}));
        assertTrue(StringUtils.isBlank(new String[]{}));
        assertFalse(StringUtils.isBlank(new String[]{" ", StringUtils.FULLWIDTH_BLANK, " 1"}));
    }

    @Test
    public void testIsEmptyStringArray() {
        String[] a = new String[0];
        assertTrue(ArrayUtils.isEmpty(a));

        a = null;
        assertTrue(ArrayUtils.isEmpty(a));
        assertFalse(ArrayUtils.isEmpty(new String[]{"1"}));
        assertFalse(ArrayUtils.isEmpty(new String[]{" ", "1"}));
        assertFalse(ArrayUtils.isEmpty(new String[]{" ", StringUtils.FULLWIDTH_BLANK, null,}));
        assertTrue(ArrayUtils.isEmpty(new String[]{}));
        assertFalse(ArrayUtils.isEmpty(new String[]{" ", StringUtils.FULLWIDTH_BLANK, " 1"}));
    }

    @Test
    public void testIsNotBlank() {
        assertFalse(StringUtils.isNotBlank(null));
        assertFalse(StringUtils.isNotBlank(""));
        assertFalse(StringUtils.isNotBlank(" "));
        assertFalse(StringUtils.isNotBlank(StringUtils.FULLWIDTH_BLANK));
        assertFalse(StringUtils.isNotBlank(StringUtils.FULLWIDTH_BLANK + StringUtils.FULLWIDTH_BLANK));
        assertTrue(StringUtils.isNotBlank("1"));
        assertTrue(StringUtils.isNotBlank("12"));
        assertTrue(StringUtils.isNotBlank("1234567890"));
    }

    @Test
    public void testLastNotBlankString() {
        assertTrue(StringUtils.lastIndexOfNotBlank(new String[]{}) == -1);
        assertTrue(StringUtils.lastIndexOfNotBlank(new String[]{""}) == -1);
        assertTrue(StringUtils.lastIndexOfNotBlank(new String[]{"", StringUtils.FULLWIDTH_BLANK, null}) == -1);
        assertTrue(StringUtils.lastIndexOfNotBlank(new String[]{"", StringUtils.FULLWIDTH_BLANK, null}) == -1);
        assertTrue(StringUtils.lastIndexOfNotBlank(new String[]{"", StringUtils.FULLWIDTH_BLANK, " ", null}) == -1);
        assertTrue(StringUtils.lastIndexOfNotBlank(new String[]{"1", "", StringUtils.FULLWIDTH_BLANK, " ", null}) == 0);
//		assertEquals(ST.lastNotBlankString(new String[] { "1", "", ST.FULLWIDTH_BLANK, "2", " ", null }), "2");
        assertTrue(StringUtils.lastIndexOfNotBlank(new String[]{"1", "", StringUtils.FULLWIDTH_BLANK, "2", " ", null, "3"}) == 6);
        assertTrue(StringUtils.lastIndexOfNotBlank(new String[]{"1", "", StringUtils.FULLWIDTH_BLANK, "2", " ", null}) == 3);
    }

    @Test
    public void testLastIndexBlank() {
        assertTrue(StringUtils.lastIndexOfBlank("0123456789", 0) == -1);
        assertTrue(StringUtils.lastIndexOfBlank(" 123456789", 0) == 0);
        assertTrue(StringUtils.lastIndexOfBlank(" 1234567" + StringUtils.FULLWIDTH_BLANK + "9", 0) == 0);
        assertTrue(StringUtils.lastIndexOfBlank(" 1234567" + StringUtils.FULLWIDTH_BLANK + "9", -1) == 8);
        assertTrue(StringUtils.lastIndexOfBlank(" 12345678" + StringUtils.FULLWIDTH_BLANK, -1) == 9);
        assertTrue(StringUtils.lastIndexOfBlank(" 12345678" + StringUtils.FULLWIDTH_BLANK, 9) == 9);
        assertTrue(StringUtils.lastIndexOfBlank(" 12345678" + StringUtils.FULLWIDTH_BLANK, 1) == 0);
    }

    @Test
    public void testLastIndex() {
        assertTrue(StringUtils.lastIndexOfStr("0123456789", "tset", 0, 9, true) == -1);
        assertTrue(StringUtils.lastIndexOfStr("0123456789", "9", 0, 9, true) == 9);
        assertTrue(StringUtils.lastIndexOfStr("0123456789", "0", 0, 9, true) == 0);
        assertTrue(StringUtils.lastIndexOfStr("0123456789", "4", 0, 9, true) == 4);
        assertTrue(StringUtils.lastIndexOfStr("0123456789", "0123456789", 0, 9, true) == 0);
        assertTrue(StringUtils.lastIndexOfStr("012345678901234567890123456789", "012", 0, 9, true) == 0);
        assertTrue(StringUtils.lastIndexOfStr("01234567890123456789", "890", 0, 19, true) == 8);
        assertTrue(StringUtils.lastIndexOfStr("012345678901234567890123456789", "0123456789", 0, 29, true) == 20);
    }

    @Test
    public void testTrimBlank() {
        // System.out.print(ST.trim("sdf \n ") + "]");
        String[] str = {null, "1 ", "2 ", "3", " 4 ", "5  ", ""};
        assertTrue(StringUtils.toString(StringUtils.removeBlank(str)).equals("String[1, 2, 3, 4, 5]"));
        assertTrue(StringUtils.trimBlank(" 　\n sdf 　\n ").equals("sdf"));
        assertTrue(StringUtils.trimBlank(" 　\n sdf 　\n ", 's', 'f').equals("d"));
        assertTrue(StringUtils.trimBlank(" 　\n sfdsf 　\n ", 's', 'f').equals("d"));
        assertTrue(StringUtils.trimBlank("1").equals("1"));
        // Exception e = new Exception();
        // check(ST.toString(e));
    }

    @Test
    public void testTrimStringCharArray() {
        assertTrue(StringUtils.trim("1", 'a').equals("1"));
        assertTrue(StringUtils.trim("a1", 'a').equals("1"));
        assertTrue(StringUtils.trim("a1a", 'a').equals("1"));
        assertTrue(StringUtils.trim("aaa1aaa", 'a').equals("1"));
        assertTrue(StringUtils.trim("aaa1a aa", 'a').equals("1a "));
    }

    @Test
    public void testTrimString() {
        assertTrue(StringUtils.trim((String) null) == null);
        assertTrue(StringUtils.trim(" 1 ").equals("1"));
        assertTrue(StringUtils.trim("  1  ").equals("1"));

        assertTrue(StringUtils.trim((String) null) == null);
        assertTrue(StringUtils.trim("").equals(""));
        assertTrue(StringUtils.trim(" ").equals(""));
        assertTrue(StringUtils.trim(" 1 ").equals("1"));
        assertTrue(StringUtils.trim(" 12 ").equals("12"));
        assertTrue(StringUtils.trim(" 12").equals("12"));
    }

    @Test
    public void testTrimQuotes() {
        assertTrue(StringUtils.unquote(null) == null);
        assertTrue(StringUtils.unquote("").equals(""));
        assertTrue(StringUtils.unquote("'").equals("'"));
        assertTrue(StringUtils.unquote("''").equals(""));
        assertTrue(StringUtils.unquote("' '").equals(" "));
        assertTrue(StringUtils.unquote("'1'").equals("1"));
        assertTrue(StringUtils.unquote("'12'").equals("12"));
    }

    @Test
    public void testTrim2Quotes() {
        assertTrue(StringUtils.unquotes(null) == null);
        assertTrue(StringUtils.unquotes("1").equals("1"));
        assertTrue(StringUtils.unquotes("\"1").equals("\"1"));
        assertTrue(StringUtils.unquotes("\"1\"").equals("1"));
        assertTrue(StringUtils.unquotes("\"1\" ").equals("\"1\" "));
        assertTrue(StringUtils.unquotes(" \"1\"").equals(" \"1\""));
    }

    @Test
    public void testTrimQuotationMark() {
        assertTrue(StringUtils.unquotation((String) null) == null);
        assertTrue(StringUtils.unquotation("\"1\"").equals("1"));
        assertTrue(StringUtils.unquotation("'1'").equals("1"));
        assertTrue(StringUtils.unquotation("'1' ").equals("'1' "));
        assertTrue(StringUtils.unquotation(" '1' ").equals(" '1' "));
    }

    @Test
    public void testTrimBlankAndBrace() {
        assertTrue(StringUtils.trimParenthes("1").equals("1"));
        assertTrue(StringUtils.trimParenthes("12").equals("12"));
        assertTrue(StringUtils.trimParenthes(" ( 12 ) ").equals("12"));
        assertTrue(StringUtils.trimParenthes(" ( 12  ").equals("( 12"));
        assertTrue(StringUtils.trimParenthes("  12  ").equals("12"));
        assertTrue(StringUtils.trimParenthes("  ((12))  ").equals("12"));
        assertTrue(StringUtils.trimParenthes("  ( ( 12 )  )  ").equals("12"));
        assertTrue(StringUtils.trimParenthes("  ( ( (12 )  )  ").equals("(12"));
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
        assertTrue(lc.get(0).equals("1"));
        assertTrue(lc.get(1).equals("2"));
        assertTrue(lc.get(2).equals("3"));
        assertTrue(lc.get(3).equals("4 　"));
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
        assertTrue(lc.get(0).equals("1"));
        assertTrue(lc.get(1).equals("2"));
        assertTrue(lc.get(2).equals("3"));
        assertTrue(lc.get(3).equals("4"));
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
        assertTrue(lc.get(0).equals("1"));
        assertTrue(lc.get(1).equals("2"));
        assertTrue(lc.get(2).equals("3"));
        assertTrue(lc.get(3).equals("4 　"));

        lc = new ArrayList<String>(l1);
        String[] a4 = CollectionUtils.toArray(lc);
        StringUtils.trim(a4);
        assertTrue(a4[0].equals("1"));
        assertTrue(a4[1].equals("2"));
        assertTrue(a4[2].equals("3"));
        assertTrue(a4[3].equals("4 　"));
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
        assertTrue(lc.get(0).equals("1"));
        assertTrue(lc.get(1).equals("2"));
        assertTrue(lc.get(2).equals("3"));
        assertTrue(lc.get(3).equals("4"));

        String[] a5 = CollectionUtils.toArray(lc);
        StringUtils.trimBlank(a5);
        assertTrue(a5[0].equals("1"));
        assertTrue(a5[1].equals("2"));
        assertTrue(a5[2].equals("3"));
        assertTrue(a5[3].equals("4"));
    }

    @Test
    public void testTrimQuotationMarkInArray() {
        assertTrue(StringUtils.trimParenthes("1").equals("1"));
        assertTrue(StringUtils.trimParenthes("12").equals("12"));
        assertTrue(StringUtils.trimParenthes(" ( 12 ) ").equals("12"));
        assertTrue(StringUtils.trimParenthes(" ( 12  ").equals("( 12"));
        assertTrue(StringUtils.trimParenthes("  12  ").equals("12"));
        assertTrue(StringUtils.trimParenthes("  ((12))  ").equals("12"));
        assertTrue(StringUtils.trimParenthes("  ( ( 12 )  )  ").equals("12"));
        assertTrue(StringUtils.trimParenthes("  ( ( (12 )  )  ").equals("(12"));
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
        assertTrue(m1.get("1").equals("1"));
        assertTrue(m1.get("2").equals("2"));
        assertTrue(m1.get("3").equals("3"));
        assertTrue(m1.get("4").equals("4"));
        assertTrue(m1.get("41").equals("　 41 　　"));
    }

    @Test
    public void testRtrimString() {
        assertTrue(StringUtils.rtrim(null) == null);
        assertTrue(StringUtils.rtrim("1").equals("1"));
        assertTrue(StringUtils.rtrim("1 ").equals("1"));
        assertTrue(StringUtils.rtrim("1  ").equals("1"));
        assertTrue(StringUtils.rtrim("1  1").equals("1  1"));
        assertTrue(StringUtils.rtrim("1  1 　").equals("1  1 　"));
    }

    @Test
    public void testRtrimStringCharArray() {
        assertTrue(StringUtils.rtrim("1234", '1').equals("1234"));
        assertTrue(StringUtils.rtrim("1234", '4').equals("123"));

        assertTrue(StringUtils.rtrim(null, ' ') == null);
        assertTrue(StringUtils.rtrim("1", ' ').equals("1"));
        assertTrue(StringUtils.rtrim("1 ", ' ').equals("1"));
        assertTrue(StringUtils.rtrim("1  ", ' ').equals("1"));
        assertTrue(StringUtils.rtrim("1  1", ' ').equals("1  1"));
        assertTrue(StringUtils.rtrim("1  1 　", ' ').equals("1  1 　"));

        assertTrue(StringUtils.rtrim("123", ' ', '2', '3').equals("1"));
        assertTrue(StringUtils.rtrim("1aa ", ' ', 'a', '3').equals("1"));

        assertTrue(StringUtils.rtrim("1", 'a').equals("1"));
        assertTrue(StringUtils.rtrim("1a", 'a').equals("1"));
        assertTrue(StringUtils.rtrim("1aa", 'a').equals("1"));
        assertTrue(StringUtils.rtrim("1aa1", 'a').equals("1aa1"));
        assertTrue(StringUtils.rtrim("1  1aaa", 'a').equals("1  1"));
    }

    @Test
    public void testRtrimBlank() {
        assertTrue(StringUtils.rtrimBlank(null) == null);
        assertTrue(StringUtils.rtrimBlank("1").equals("1"));
        assertTrue(StringUtils.rtrimBlank("1 ").equals("1"));
        assertTrue(StringUtils.rtrimBlank("1  ").equals("1"));
        assertTrue(StringUtils.rtrimBlank("1  1").equals("1  1"));
        assertTrue(StringUtils.rtrimBlank("1  1 　").equals("1  1"));
        assertTrue(StringUtils.rtrimBlank(" 1  1 　").equals(" 1  1"));
        assertTrue(StringUtils.rtrimBlank("sdf 　\n ").equals("sdf"));
        assertTrue(StringUtils.rtrimBlank("sdf 　\n ", 'f').equals("sd"));
    }

    @Test
    public void testLtrimString() {
        assertTrue(StringUtils.ltrim(null) == null);
        assertTrue(StringUtils.ltrim("1").equals("1"));
        assertTrue(StringUtils.ltrim(" 1 ").equals("1 "));
        assertTrue(StringUtils.ltrim("  1  ").equals("1  "));
        assertTrue(StringUtils.ltrim("  1  1").equals("1  1"));
        assertTrue(StringUtils.ltrim(" 　1  1 　").equals("　1  1 　"));
    }

    @Test
    public void testLtrimStringCharArray() {
        assertTrue(StringUtils.ltrim(null, ' ') == null);
        assertTrue(StringUtils.ltrim("1", 'a').equals("1"));
        assertTrue(StringUtils.ltrim("a1 ", 'a').equals("1 "));
        assertTrue(StringUtils.ltrim("aa1  ", 'a').equals("1  "));
        assertTrue(StringUtils.ltrim("aaa1  1", 'a').equals("1  1"));
        assertTrue(StringUtils.ltrim(" aa1  1 　", 'a').equals(" aa1  1 　"));

        assertTrue(StringUtils.ltrim("1  1aaa", '1', 'a').equals("  1aaa"));
        assertTrue(StringUtils.ltrim("1aa  1aaa", '1', 'a').equals("  1aaa"));
        assertTrue(StringUtils.ltrim("1234", '1').equals("234"));
    }

    @Test
    public void testLtrimBlank() {
        assertTrue(StringUtils.ltrimBlank(null) == null);
        assertTrue(StringUtils.ltrimBlank("1").equals("1"));
        assertTrue(StringUtils.ltrimBlank(" 1 ").equals("1 "));
        assertTrue(StringUtils.ltrimBlank("  1  ").equals("1  "));
        assertTrue(StringUtils.ltrimBlank("  1  1").equals("1  1"));
        assertTrue(StringUtils.ltrimBlank(" 　1  1 　").equals("1  1 　"));
        assertTrue(StringUtils.ltrimBlank(" 　\n sdf 　\n ").equals("sdf 　\n "));
        assertTrue(StringUtils.ltrimBlank(" 　\n sdf 　\n ", 's').equals("df 　\n "));
    }

    @Test
    public void testObjToStrObject() {
        assertTrue(StringUtils.objToStr(null).equals(""));
        assertTrue(StringUtils.objToStr("1 ").equals("1"));
    }

    @Test
    public void testObjToStrObjectString() {
//		assertEquals(ST.objToStr(null, null), null);
//		assertEquals(ST.objToStr(null, ""), "");
//		assertEquals(ST.objToStr("1", ""), "1");
    }

    @Test
    public void testReplaceFirst() {
        assertTrue(StringUtils.replace("12345678901234567890", "890", "A").equals("1234567A1234567890"));
        assertTrue(StringUtils.replace("12345678901234567890", "1", "A").equals("A2345678901234567890"));
    }

    @Test
    public void testReplaceLast() {
        assertTrue(StringUtils.replaceLast("12345678901234567890", "890", "").equals("12345678901234567"));
    }

    @Test
    public void testReplaceAll() {
        assertTrue(StringUtils.replaceAll("12345678901234567890", "890", "A").equals("1234567A1234567A"));
        assertTrue(StringUtils.replaceAll("", "890", "A").equals(""));
        assertTrue(StringUtils.replaceAll("12345678901234567890", "1", "A").equals("A234567890A234567890"));
        assertTrue(StringUtils.replaceAll("12345678901234567890", "1", "1").equals("12345678901234567890"));
    }

    @Test
    public void testReplaceChars() {
        assertTrue(StringUtils.replace("12345678901234567890", 0, 1, "").equals("2345678901234567890"));
        assertTrue(StringUtils.replace("12345678901234567890", 0, 10, "").equals("1234567890"));
        assertTrue(StringUtils.replace("12345678901234567890", 0, 20, "").equals(""));
        assertTrue(StringUtils.replace("12345678901234567890", 19, 1, "").equals("1234567890123456789"));
    }

    @Test
    public void testReplaceChineseAscii() {
        assertTrue(StringUtils.replaceHalfWidthChar("１2345678901234567890").equals("12345678901234567890"));
        assertTrue(StringUtils.replaceHalfWidthChar("１234567890123456789０").equals("12345678901234567890"));
        assertTrue(StringUtils.replaceHalfWidthChar("１234567890123456789０　").equals("12345678901234567890 "));
        assertTrue(StringUtils.replaceHalfWidthChar("（１234567890123456789０　）").equals("(12345678901234567890 )"));
        assertTrue(StringUtils.replaceHalfWidthChar("（１测试０　）").equals("(1测试0 )"));
    }

    @Test
    public void testReplaceVariableAndEnvironment() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("HOME", "test");
        map.put("T", "test");
        assertTrue(StringUtils.replaceEnvironment("１234567890${HOME}12345${T}6789０", map).equals("１234567890test12345test6789０"));
        assertTrue(StringUtils.replaceEnvironment("１234567890${HOME}123456789０", map).equals("１234567890test123456789０"));
        assertTrue(StringUtils.replaceEnvironment("１234567890${HOME}12345${PUBLIC}6789０", map).equals("１234567890test12345${PUBLIC}6789０"));
        map.put("PUBLIC", "PUBLIC");
        assertTrue(StringUtils.replaceEnvironment("１234567890${HOME}12345${PUBLIC}6789０", map).equals("１234567890test12345PUBLIC6789０"));
    }

    @Test
    public void testReplaceEnvironmentVariable() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("HOME", "test");
        map.put("T", "test");
        assertTrue(StringUtils.replaceEnvironment("１234567890${HOME}12345${T}6789０", map).equals("１234567890test12345test6789０"));
        assertTrue(StringUtils.replaceEnvironment("１234567890${HOME}123456789０", map).equals("１234567890test123456789０"));
        assertTrue(StringUtils.replaceEnvironment("１234567890${HOME}12345${PUBLIC}6789０", map).equals("１234567890test12345${PUBLIC}6789０"));

        map.put("PUBLIC", "PUBLIC");
        assertTrue(StringUtils.replaceEnvironment("１234567890${HOME}12345${PUBLIC}6789０", map).equals("１234567890test12345PUBLIC6789０"));
        assertTrue(StringUtils.replaceEnvironment("１234567890${HOME}12345${PUBLIC}6789０").equals("１234567890" + Settings.getUserHome() + "12345${PUBLIC}6789０"));
    }

    @Test
    public void testReplaceVariableStringMapOfStringString() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("HOME", "test");
        map.put("T", "test");
        map.put("PUBLIC", "PUBLIC");

        assertTrue(StringUtils.replaceVariable("12345678901234567890", map).equals("12345678901234567890"));
        assertTrue(StringUtils.replaceVariable("１234567890${HOME}12345${PUBLIC}6789０${HOME}", map).equals("１234567890test12345PUBLIC6789０test"));
        assertTrue(StringUtils.replaceVariable("${HOME}", map).equals("test"));
        assertTrue(StringUtils.replaceVariable("${HOME}${T}", map).equals("testtest"));
        assertTrue(StringUtils.replaceVariable("", map).equals(""));
        assertTrue(StringUtils.replaceVariable("${HOME}+${T}", map).equals("test+test"));
    }

    @Test
    public void testReplaceVariableStringMapOfObjectString() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("HOME", "test");
        map.put("T", "test");
        map.put("PUBLIC", "PUBLIC");

        assertTrue(StringUtils.replaceVariable("12345678901234567890", map, null).equals("12345678901234567890"));
        assertTrue(StringUtils.replaceVariable("１234567890${HOME}12345${PUBLIC}6789０${HOME}", map, null).equals("１234567890test12345PUBLIC6789０test"));
        assertTrue(StringUtils.replaceVariable("${HOME}", map, null).equals("test"));
        assertTrue(StringUtils.replaceVariable("${HOME}${T}", map, null).equals("testtest"));
        assertTrue(StringUtils.replaceVariable("${HOME}+${T}", map, null).equals("test+test"));
        assertTrue(StringUtils.replaceVariable("${HOME}+${T1}", map, null).equals("test+${T1}"));
    }

    @Test
    public void testReplaceVariableStringStringString() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("HOME", "test");
        map.put("T", "test");
        map.put("PUBLIC", "PUBLIC");

        assertTrue(StringUtils.replaceVariable("12345678901234567890", "HOME", "test").equals("12345678901234567890"));
        assertTrue(StringUtils.replaceVariable("${HOME}", "HOME", "test").equals("test"));
    }

    @Test
    public void testSubstrStringIntInt() {
        assertTrue(StringUtils.substring((String) null, 0, 0, StandardCharsets.UTF_8.name()) == null);
        assertTrue(StringUtils.substring("", 0, 0, StandardCharsets.UTF_8.name()).equals(""));

        boolean val = false;
        try {
            assertTrue(StringUtils.substring("", 0, 1, StandardCharsets.UTF_8.name()).equals(""));
            val = true;
        } catch (Exception e) {
        }
        if (val) {
            throw new RuntimeException();
        }

        val = false;
        try {
            assertTrue(StringUtils.substring("12345678901234567890", 0, 21, StandardCharsets.UTF_8.name()).equals(""));
            val = true;
        } catch (Exception e) {
        }
        if (val) {
            throw new RuntimeException();
        }

        assertTrue(StringUtils.substring("12345678901234567890", 0, 0, StandardCharsets.UTF_8.name()).equals(""));
        assertTrue(StringUtils.substring("12345678901234567890", 0, 10, StandardCharsets.UTF_8.name()).equals("1234567890"));
        assertTrue(StringUtils.substring("12345678901234567890", 0, 20, StandardCharsets.UTF_8.name()).equals("12345678901234567890"));
    }

    @Test
    public void testSubstrStringIntIntString() {
        assertTrue(StringUtils.substring("12345678901234567890", 0, 0, "UTF-8").equals(""));
        assertTrue(StringUtils.substring("12345678901234567890", 0, 10, "UTF-8").equals("1234567890"));
        assertTrue(StringUtils.substring("12345678901234567890", 0, 20, "UTF-8").equals("12345678901234567890"));

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
        assertTrue(StringUtils.substring("12345678901234567890".getBytes("UTF-8"), 0, 10, "UTF-8").equals("1234567890"));
        assertTrue(StringUtils.substring("12345678901234567890".getBytes("UTF-8"), 0, 20, "UTF-8").equals("12345678901234567890"));
        assertTrue(StringUtils.substring("12345678901234567890".getBytes("GBK"), 0, 19, "gbk").equals("1234567890123456789"));
        assertTrue(StringUtils.substring("一二三四五六七八九十".getBytes("GBK"), 0, 10, "gbk").equals("一二三四五"));
        assertTrue(!StringUtils.substring("一二三四五六七八九十".getBytes("GBK"), 0, 11, "gbk").equals("一二三四五"));
    }

    @Test
    public void testSubstrStringIntIntInt() {
        assertTrue(StringUtils.substring("0123456789", 1, 1, 1).equals("012"));
        assertTrue(StringUtils.substring("0123456789", 0, 1, 1).equals("01"));
        assertTrue(StringUtils.substring("0123456789", 0, 1, 0).equals("0"));
        assertTrue(StringUtils.substring("0123456789", 5, 2, 2).equals("34567"));
        assertTrue(StringUtils.substring("0123456789", 5, 5, 4).equals("0123456789"));
        assertTrue(StringUtils.substring("0123456789", 5, 6, 5).equals("0123456789"));
        assertTrue(StringUtils.substring("0123456789", 9, 0, 10).equals("9"));
    }

    @Test
    public void testSubstrTrimBlank() {
        assertTrue(StringUtils.substr("0123456789", 1, 0, 0).equals("1"));
        assertTrue(StringUtils.substr("0123 5 789", 5, 1, 1).equals("5"));
        assertTrue(StringUtils.substr("     5    ", 5, 5, 4).equals("5"));
        assertTrue(StringUtils.substr("     5    ", 5, 6, 5).equals("5"));
        assertTrue(StringUtils.substr("5", 0, 6, 5).equals("5"));
    }

    @Test
    public void testLeft() {
        assertTrue(StringUtils.left(null, 10) == null);
        assertTrue(StringUtils.left("", 10).equals(""));
        assertTrue(StringUtils.left("1", 10).equals("1"));
        assertTrue(StringUtils.left("12", 10).equals("12"));
        assertTrue(StringUtils.left("12345678901", 10).equals("1234567890"));
        assertTrue(StringUtils.left("123456789012", 10).equals("1234567890"));
        assertTrue(StringUtils.left("", 10).equals(""));
    }

    @Test
    public void testLeftIgnoreChinese() {
        assertTrue(StringUtils.left(null, 10, null) == null);
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
        Assert.assertEquals("1234567890", StringUtils.left("1234567890", 10, null, ' ')); // 判断字符串是否相等
        Assert.assertEquals("1234567890 ", StringUtils.left("1234567890", 11, null, ' ')); // 判断字符串是否相等
        Assert.assertEquals("1234567890  ", StringUtils.left("1234567890", 12, null, ' ')); // 判断字符串是否相等
        Assert.assertEquals("1234567截取", StringUtils.left("1234567截取", 11, null, ' '));
        Assert.assertEquals(null, StringUtils.left(null, 11, null, ' '));
        Assert.assertEquals("", StringUtils.left("中", 0, null, ' '));
        Assert.assertEquals("", StringUtils.left("a", 0, null, ' '));
    }

    @Test
    public void testLeftFormatObjectInt() {
        assertTrue(StringUtils.left(null, 1, ' ') == null);
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
        assertTrue(StringUtils.right(null, 10) == null);
        Assert.assertEquals("", StringUtils.right("", 10));
        Assert.assertEquals("1234567890", StringUtils.right("12345678901234567890", 10));
        Assert.assertEquals("12345678901234567890", StringUtils.right("12345678901234567890", 20));
        Assert.assertEquals("12345678901234567890", StringUtils.right("A12345678901234567890", 20));
    }

    @Test
    public void testRightFormatObjectInt() {
        assertTrue(StringUtils.right(null, 10, ' ') == null);
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
        Assert.assertEquals(null, StringUtils.right(null, 1, StringUtils.CHARSET));
        Assert.assertEquals("", StringUtils.right("", 0, StringUtils.CHARSET));
        Assert.assertEquals("", StringUtils.right("", 1, StringUtils.CHARSET));
        Assert.assertEquals("7", StringUtils.right("01234567", 1, StringUtils.CHARSET));
        Assert.assertEquals("01234567", StringUtils.right("01234567", 8, StringUtils.CHARSET));
        Assert.assertEquals("234567中文", StringUtils.right("01234567中文", 10, StringUtils.CHARSET));
        Assert.assertEquals("文567", StringUtils.right("中文567", 6, StringUtils.CHARSET));
    }

    @Test
    public void testRightFormatObjectStringIntByte() {
        Assert.assertTrue(StringUtils.right(null, 10, null, ' ') == null);
        Assert.assertEquals(" 1", StringUtils.right("1", 2, null, ' '));
        Assert.assertEquals(" 123", StringUtils.right("123", 4, null, ' '));
        Assert.assertEquals("          1234567890", StringUtils.right("1234567890", 20, null, ' '));
        Assert.assertEquals("1234567890", StringUtils.right("12345678901234567890", 10, null, ' '));
        Assert.assertEquals("1234567890", StringUtils.right("A12345678901234567890", 10, null, ' '));
        Assert.assertEquals("12345678901234567890", StringUtils.right("A12345678901234567890", 20, null, ' '));
        Assert.assertEquals("12345678901234567890", StringUtils.right("12345678901234567890", 20, null, ' '));

        Assert.assertTrue(StringUtils.right(null, 10, null, 'a') == null);
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
//		Assert.assertEquals("[  吕钊军  ]", ("[" + StringUtils.middleFormat("吕钊军", 10) + "]"));
//		Assert.assertEquals("[    吕    ]", ("[" + StringUtils.middleFormat("吕", 10) + "]"));
    }

    @Test
    public void testMiddleFormatObjectIntByte() {
//		Assert.assertEquals("[    1     ]", ("[" + StringUtils.middleFormat("1", 10, (byte) ' ') + "]"));
//		Assert.assertEquals("[     1     ]", ("[" + StringUtils.middleFormat("1", 11, (byte) ' ') + "]"));
//		Assert.assertEquals("[  吕钊军  ]", ("[" + StringUtils.middleFormat("吕钊军", 10, (byte) ' ') + "]"));
//		Assert.assertEquals("[    吕    ]", ("[" + StringUtils.middleFormat("吕", 10, (byte) ' ') + "]"));
    }

    @Test
    public void testMiddleFormatObjectStringIntByte() {
        Assert.assertEquals("[吕]", ("[" + StringUtils.middle("吕", 2, null, ' ') + "]"));
        Assert.assertEquals("[    吕    ]", ("[" + StringUtils.middle("吕", 10, null, ' ') + "]"));
        Assert.assertEquals("[    吕     ]", ("[" + StringUtils.middle("吕", 11, null, ' ') + "]"));
        Assert.assertEquals("测", StringUtils.middle("测试", 2, null, ' '));
        Assert.assertEquals("", StringUtils.middle("测试", 0, null, ' '));
        Assert.assertEquals(null, StringUtils.middle(null, 0, null, ' '));
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
        Assert.assertTrue(StringUtils.indexOf(new String[]{}, "") == -1);
        Assert.assertTrue(StringUtils.indexOf(new String[]{""}, "") == 0);
        Assert.assertTrue(StringUtils.indexOf(new String[]{null, "", ""}, "") == 1);
    }

    @Test
    public void testIndexStrIgnoreCase() {
        Assert.assertTrue(StringUtils.indexOfIgnoreCase(new String[]{}, "") == -1);
        Assert.assertTrue(StringUtils.indexOfIgnoreCase(new String[]{null, "", ""}, "") == 1);
    }

    @Test
    public void testIndexStrStringStringIntBoolean() {
        Assert.assertTrue(StringUtils.indexOf("A", "a", 0, true) == 0);
        Assert.assertTrue(StringUtils.indexOf("A", "a", 0, true) == 0);
        Assert.assertTrue(StringUtils.indexOf("bA", "a", 0, true) == 1);
        Assert.assertTrue(StringUtils.indexOf("bA", "a", 1, true) == 1);
        Assert.assertTrue(StringUtils.indexOf("abAbc", "abc", 0, true) == 2);
        Assert.assertTrue(StringUtils.indexOf("abc", "A", 0, true) == 0);
        Assert.assertTrue(StringUtils.indexOf("abc", "Ab", 0, true) == 0);
        Assert.assertTrue(StringUtils.indexOf("abc", "Abc", 0, true) == 0);
        Assert.assertTrue(StringUtils.indexOf("abc", "Abcd", 0, true) == -1);
        Assert.assertTrue(StringUtils.indexOf("abcdefABCDEF", "A", 1, true) == 6);
        Assert.assertTrue(StringUtils.indexOf("abcdefABCDEF", "Ab", 1, true) == 6);
        Assert.assertTrue(StringUtils.indexOf("abcdefABCDEF", "ef", 1, true) == 4);
        Assert.assertTrue(StringUtils.indexOf("abcdefABCDEF", "EF", 5, true) == 10);
    }

    @Test
    public void testIndexStrStringStringArrayIntIntBoolean() {
        Assert.assertTrue(StringUtils.indexOf(new String[]{"a", "b", "c", "d"}, "a", 0, 4, true) == 0);
        Assert.assertTrue(StringUtils.indexOf(new String[]{"a", "b", "c", "d"}, "A", 0, 4, true) == 0);
        Assert.assertTrue(StringUtils.indexOf(new String[]{"a", "b", "c", "d"}, "D", 0, 4, true) == 3);
        Assert.assertTrue(StringUtils.indexOf(new String[]{"a"}, "a", 0, 1, true) == 0);
        Assert.assertTrue(StringUtils.indexOf(new String[]{"a", "b", "c", "d"}, "e", 0, 4, true) == -1);
        Assert.assertTrue(StringUtils.indexOf(new String[]{}, "a", 0, 1, true) == -1);
        Assert.assertTrue(StringUtils.indexOf(new String[]{"a", "b", "c", "d"}, "B", 0, 1, true) == 1);

        Assert.assertTrue(StringUtils.indexOf(new String[]{"a", "b", "c", "d"}, "a", 0, 1, true) == 0);
        Assert.assertTrue(StringUtils.indexOf(new String[]{"a", "b", "c", "d"}, "a", 0, 0, true) == 0);
        Assert.assertTrue(StringUtils.indexOf(new String[]{"a", "b", "c", "d"}, "B", 2, 3, true) == -1);
    }

    @Test
    public void testIndexBlank() {
        Assert.assertTrue(StringUtils.indexOfBlank(" ", 0, 10) == 0);
        Assert.assertTrue(StringUtils.indexOfBlank("123 456 789  ", 0, 10) == 3);

        Assert.assertTrue(StringUtils.indexOfBlank(" 123456789", 0, -1) == 0);
        Assert.assertTrue(StringUtils.indexOfBlank("0 23456789", 0, -1) == 1);
        Assert.assertTrue(StringUtils.indexOfBlank("012345678 ", 0, -1) == 9);
        Assert.assertTrue(StringUtils.indexOfBlank("0123 5678 ", 0, 4) == 4);
        Assert.assertTrue(StringUtils.indexOfBlank("012345678 ", 0, 9) == 9);
        Assert.assertTrue(StringUtils.indexOfBlank("0123 5678 ", 0, 3) == -1);
    }

    @Test
    public void testIndexCharCharCharArray() {
        Assert.assertTrue(StringUtils.indexOf(new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'}, '0') == 0);
        Assert.assertTrue(StringUtils.indexOf(new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'}, '9') == 9);
    }

    @Test
    public void testIndexCharCharCharArrayInt() {
        Assert.assertTrue(StringUtils.indexOf("0123456789".toCharArray(), '0', 0) == 0);
        Assert.assertTrue(StringUtils.indexOf("0123456789".toCharArray(), '1', 0) == 1);
        Assert.assertTrue(StringUtils.indexOf("0123456789".toCharArray(), '9', 0) == 9);
    }

    @Test
    public void testIndexSqlQuotationMarkEndPos() {
        Assert.assertTrue(StringUtils.indexOfQuotation("0123456789", 0, true) == -1);
        Assert.assertTrue(StringUtils.indexOfQuotation("0'23456789", 0, true) == 1);
        Assert.assertTrue(StringUtils.indexOfQuotation("012345678'", 0, true) == 9);
        Assert.assertTrue(StringUtils.indexOfQuotation("0\\'23456789", 0, true) == -1);
    }

    @Test
    public void testJoinStringArrayString() {
//		SQLScriptSyntaxAnalysis p = new SQLScriptSyntaxAnalysis();
        Assert.assertEquals("11|2|3|4|5|吕钊军", StringUtils.join(StringUtils.splitByBlank(" 11 2 3 4 5 吕钊军 "), "|"));
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
        Assert.assertTrue(StringUtils.join(a, ",") == null);
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

        list.clear();
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
        Assert.assertTrue(Arrays.equals(StringUtils.splitProperty("key=value"), new String[]{"key", "value"}));
        Assert.assertTrue(Arrays.equals(StringUtils.splitProperty("key="), new String[]{"key", ""}));
        Assert.assertTrue(Arrays.equals(StringUtils.splitProperty("="), new String[]{"", ""}));
        Assert.assertTrue(StringUtils.splitProperty("") == null);
    }

    @Test
    public void testSplitKeyValueForceString() {
        Assert.assertTrue(Arrays.equals(StringUtils.splitPropertyForce("key=value"), new String[]{"key", "value"}));
        Assert.assertTrue(Arrays.equals(StringUtils.splitPropertyForce("key="), new String[]{"key", ""}));
        Assert.assertTrue(Arrays.equals(StringUtils.splitPropertyForce("="), new String[]{"", ""}));

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
        Assert.assertTrue(list.size() == 3);
        Assert.assertEquals("key", list.get(0)[0]);
        Assert.assertEquals("value", list.get(0)[1]);
        Assert.assertEquals("key2", list.get(2)[0]);
        Assert.assertEquals("value2", list.get(2)[1]);
    }

    @Test
    public void testSplitBlank() {
        Assert.assertEquals("11|2|3|4|5|吕钊军", StringUtils.join(StringUtils.splitByBlank(" 11 2 3 4 5 吕钊军 "), "|"));
        Assert.assertEquals("11|2|3|4|5|吕钊军", StringUtils.join(StringUtils.splitByBlank(" 11   2     3 4     5 吕钊军 "), "|"));
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
        Assert.assertTrue(!StringUtils.inArray('e', new char[]{'a', 'A', 'c'}));
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
        Assert.assertTrue(!StringUtils.inCollection("A", c, false));
    }

    @Test
    public void testCloneStringArray() {
        String[] array = Arrays.copyOf(new String[]{"0", "1", "2"}, 3);
        Assert.assertEquals(new String[]{"0", "1", "2"}, array);
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
        Assert.assertTrue(!StringUtils.startsWith("abCdefghijk", list, false));
        Assert.assertTrue(StringUtils.startsWith("abCdefghijk", list, true));
    }

    @Test
    public void testStartsWithIgnoreCase() {
        Assert.assertTrue(StringUtils.startsWithIgnoreCase("0123456789", "0"));
        Assert.assertTrue(StringUtils.startsWithIgnoreCase("a", "A"));
        Assert.assertTrue(StringUtils.startsWithIgnoreCase("012", "012"));
        Assert.assertTrue(StringUtils.startsWithIgnoreCase("abcd", "Abc"));
        Assert.assertTrue(!StringUtils.startsWithIgnoreCase("abcd", " Abc"));
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
        Assert.assertTrue(StringUtils.startsWith("   a abc", "a ", 5, true, true) == false);
        Assert.assertTrue(StringUtils.startsWith("   ra abc", "a", 6, true, true) == true);
        Assert.assertTrue(StringUtils.startsWith("   ra abc", "a", 7, true, true) == false);
    }

    @Test
    public void testFirstCharToUpper() {
        Assert.assertTrue(StringUtils.firstCharToUpper(null) == null);
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
        Assert.assertTrue(!Character.isLowerCase('A'));
    }

    @Test
    public void testIsUpper() {
        Assert.assertTrue(!Character.isUpperCase('a'));
        Assert.assertTrue(Character.isUpperCase('A'));
        Assert.assertTrue(Character.isUpperCase('Z'));
    }

    @Test
    public void testHexStringToBytes() throws UnsupportedEncodingException {
        Assert.assertTrue("30313233343536373839".equals(StringUtils.toHexString("0123456789".getBytes("GBK"))));
    }

    @Test
    public void testByteToHexStringStringString() throws IOException {
        Assert.assertEquals("3031323334", StringUtils.toHexString("01234", "GBK"));
        try {
            Assert.assertEquals("3031323334", StringUtils.toRadixString("01234".getBytes("gbk"), 16));
        } catch (UnsupportedEncodingException e) {
            Assert.assertTrue(false);
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
        Assert.assertTrue(StringUtils.length("中文是个ab", "gbk") == 10);
    }

    @Test
    public void testisascii() {
        Assert.assertTrue(StringUtils.isAscii('0'));
        Assert.assertTrue(StringUtils.isAscii('a'));
        Assert.assertTrue(StringUtils.isAscii('Z'));
        Assert.assertTrue(StringUtils.isAscii('+'));
        Assert.assertTrue(StringUtils.isAscii(';'));
        Assert.assertTrue(!StringUtils.isAscii('中'));
        Assert.assertTrue(!StringUtils.isAscii('美'));
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
        assertTrue(StringUtils.isNotBlank(Settings.getFileEncoding()));
    }

    @Test
    public void testGetJvmVmVersion() {
        assertTrue(StringUtils.isNotBlank(Settings.getJavaVmVersion()));
    }

    @Test
    public void testGetJvmVmVendor() {
        assertTrue(StringUtils.isNotBlank(Settings.getJavaVmVendor()));
    }

    @Test
    public void testGetJvmVmName() {
        assertTrue(StringUtils.isNotBlank(Settings.getJavaVmName()));
    }

    @Test
    public void testGetJvmUserCountry() {
        assertTrue(StringUtils.isNotBlank(Settings.getUserCountry()));
    }

    @Test
    public void testGetJvmUserLanguage() {
        assertTrue(StringUtils.isNotBlank(Settings.getUserLanguage()));
    }

    @Test
    public void testGetJvmLineSeparator() {
        assertTrue(StringUtils.inArray(FileUtils.lineSeparator, "\r", "\n", "\r\n"));
    }

    @Test
    public void testGetJvmOsName() {
        assertTrue(StringUtils.isNotBlank(OSUtils.getName()));
    }

    @Test
    public void testGetJvmUserTimezone() {
        Settings.getUserTimezone();
    }

    @Test
    public void testToArrayStringArray() {
        String[] array = new String[]{"", "", ""};
        assertTrue(Arrays.equals(array, new String[]{"", "", ""}));
    }

    @Test
    public void testToArrayCollectionOfString() {
        List<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        list.add("3");
        assertTrue(Arrays.equals(CollectionUtils.toArray(list), new String[]{"1", "2", "3"}));
    }

    @Test
    public void testToHalfBlank() {
        Assert.assertEquals(" ", StringUtils.replaceHalfWidthBlank(StringUtils.FULLWIDTH_BLANK));
        Assert.assertEquals(" a ", StringUtils.replaceHalfWidthBlank(" a" + StringUtils.FULLWIDTH_BLANK));
    }

    @Test
    public void testremove() {
        assertTrue(StringUtils.remove(null, 0, 0) == null);
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
        assertTrue(a1.length == 1 && a1[0].equals("a"));
    }

    @Test
    public void testRemoveRightEndChar() {
        assertTrue(StringUtils.removeSuffix(null) == null);
        Assert.assertEquals("", StringUtils.removeSuffix(""));
        Assert.assertEquals("", StringUtils.removeSuffix("1"));
        Assert.assertEquals("012345678", StringUtils.removeSuffix("0123456789"));
        Assert.assertEquals("0123456789", StringUtils.removeSuffix("0123456789号"));
    }

    @Test
    public void testRemoveLeftSideChar() {
        assertTrue(true);
    }

    @Test
    public void testGetMapKeyArray() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("a", "A");
        map.put("b", "A");
        map.put("c", "A");
        String[] keys = CollectionUtils.toArray(map.keySet());
        assertTrue(Arrays.equals(keys, new String[]{"a", "b", "c"}));
    }

    @Test
    public void testIsNumberChar() {
        assertTrue(StringUtils.isNumber('0'));
        assertTrue(StringUtils.isNumber('1'));
        assertTrue(StringUtils.isNumber('9'));
        assertTrue(!StringUtils.isNumber('\\'));
    }

    @Test
    public void testIsEnglishCharacter() {
        assertTrue(StringUtils.isLetter('a'));
        assertTrue(StringUtils.isLetter('z'));
        assertTrue(StringUtils.isLetter('A'));
        assertTrue(StringUtils.isLetter('Z'));
        assertTrue(!StringUtils.isLetter('1'));
        assertTrue(!StringUtils.isLetter('了'));
    }

    @Test
    public void testIsSpecialCharacter() {
        assertTrue(StringUtils.isSymbol('~'));
        assertTrue(StringUtils.isSymbol('!'));
        assertTrue(StringUtils.isSymbol('/'));
        assertTrue(!StringUtils.isSymbol('l'));
    }

    @Test
    public void testIsNumberCharArray() {
        assertTrue(!StringUtils.isNumber((char[]) null));
        assertTrue(!StringUtils.isNumber("".toCharArray()));
        assertTrue(StringUtils.isNumber("0123456789".toCharArray()));
        assertTrue(!StringUtils.isNumber("0123456789|".toCharArray()));
    }

    @Test
    public void testIsNumberString() {
        assertTrue(!StringUtils.isNumber(""));
        assertTrue(!StringUtils.isNumber((String) null));

        assertTrue(!StringUtils.isNumber("q0123456789"));
        assertTrue(StringUtils.isNumber("0123456789"));
        assertTrue(!StringUtils.isNumber("0123456789|"));
    }

    @Test
    public void testContainQuotes() {
        assertTrue(StringUtils.containsQuotation("' '"));
        assertTrue(StringUtils.containsQuotation("''"));
        assertTrue(StringUtils.containsQuotation("' '"));
        assertTrue(!StringUtils.containsQuotation("'"));
        assertTrue(!StringUtils.containsQuotation(""));
    }

    @Test
    public void testContain2Quotes() {
        assertTrue(StringUtils.containsDoubleQuotation("\"\""));
        assertTrue(StringUtils.containsDoubleQuotation("\"1\""));
        assertTrue(!StringUtils.containsDoubleQuotation(""));
        assertTrue(!StringUtils.containsDoubleQuotation(" "));
        assertTrue(!StringUtils.containsDoubleQuotation("\""));
        assertTrue(!StringUtils.containsDoubleQuotation("\"1"));
    }

    @Test
    public void testTestParseInt() {
        assertTrue(StringUtils.isInt("0"));
        assertTrue(StringUtils.isInt("1000"));
        assertTrue(!StringUtils.isInt("0v"));
    }

    @Test
    public void testTestParseDouble() {
        assertTrue(StringUtils.isDouble("0"));
        assertTrue(StringUtils.isDouble("1000"));
        assertTrue(!StringUtils.isDouble("0v"));
    }

    @Test
    public void testTestParseLong() {
        assertTrue(StringUtils.isLong("0"));
        assertTrue(StringUtils.isLong("1000"));
        assertTrue(!StringUtils.isLong("0v"));
    }

    @Test
    public void testTestParseBigDecimal() {
        assertTrue(StringUtils.isDecimal("0"));
        assertTrue(StringUtils.isDecimal("1000"));
        assertTrue(!StringUtils.isDecimal("0v"));
    }

    @Test
    public void testParseIntStringInt() {
        assertTrue(StringUtils.parseInt("0", 1) == 0);
        assertTrue(StringUtils.parseInt("", 1) == 1);
    }

    @Test
    public void testIndexEndOfLinePosition() {
        assertTrue(StringUtils.indexOfEOL("", 0) == 0);
        assertTrue(StringUtils.indexOfEOL("1", 0) == 0);
        assertTrue(StringUtils.indexOfEOL("01", 0) == 1);
        assertTrue(StringUtils.indexOfEOL("\r", 0) == 0);
        assertTrue(StringUtils.indexOfEOL("\n", 0) == 0);
        assertTrue(StringUtils.indexOfEOL("\r\n", 0) == 1);
        assertTrue(StringUtils.indexOfEOL("\r\n\r", 0) == 1);
        assertTrue(StringUtils.indexOfEOL("\r\n\n", 0) == 1);
        assertTrue(StringUtils.indexOfEOL("0\r\n\n", 0) == 2);
        assertTrue(StringUtils.indexOfEOL("01\n\n", 0) == 2);
        assertTrue(StringUtils.indexOfEOL("01\n\r", 0) == 2);
        assertTrue(StringUtils.indexOfEOL("01\r\n567890", 0) == 3);
        assertTrue(StringUtils.indexOfEOL("01\r\n567890\r", 0) == 3);
        assertTrue(StringUtils.indexOfEOL("01\r\n567890\n", 0) == 3);
        assertTrue(StringUtils.indexOfEOL("01\r\n4567890\n", 4) == 11);
        assertTrue(StringUtils.indexOfEOL("01\r\n4567890\n23456789\n", 4) == 11);
        assertTrue(StringUtils.indexOfEOL("01\r\n4567890\n23456789\n", 12) == 20);
    }

    @Test
    public void testTrimBlankMap() {
        Map<String, String> map = new HashMap<String, String>();
        assertTrue(StringUtils.trimBlank(map).isEmpty());

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
        assertTrue(map.get("key6") == null);
    }

    @Test
    public void testSplitXmlPropertys() {
        List<Property> list = XMLUtils.splitXmlProperties(" value='' v1=\"1\" v2=  v3= 3 v4 = 4 v5 =5 ");
        assertTrue(list.get(0).getValue().equals("") && list.get(0).getKey().equals("value"));
        assertTrue(list.get(1).getValue().equals("1") && list.get(1).getKey().equals("v1"));
        assertTrue(list.get(2).getValue() == null && list.get(2).getKey().equals("v2"));
        assertTrue(list.get(3).getValue().equals(" 3") && list.get(3).getKey().equals("v3"));
        assertTrue(list.get(4).getValue().equals(" 4") && list.get(4).getKey().equals("v4"));
        assertTrue(list.get(5).getValue().equals("5") && list.get(5).getKey().equals("v5"));
        assertTrue(XMLUtils.splitXmlProperties(" value='' ").get(0).getValue().equals(""));
        assertTrue(XMLUtils.splitXmlProperties(" value='' v1 = \"test\" ").get(1).getKey().equals("v1"));
        assertTrue(XMLUtils.splitXmlProperties(" value='' v1 = \"test\" ").get(1).getValue().equals("test"));

        String s1 = " value='' v1 = \"test\" v2 v3 ";
        Assert.assertEquals("test", XMLUtils.splitXmlProperties(s1).get(1).getValue());
        Assert.assertEquals("v2", XMLUtils.splitXmlProperties(s1).get(2).getKey());
        Assert.assertEquals("v3", XMLUtils.splitXmlProperties(s1).get(3).getKey());

//		System.out.println(ST.toString(ST.splitXmlProperties(s1), " "));
    }

    @Test
    public void testremoveStringIntegerInteger() {
        assertTrue(StringUtils.remove(null, 0, 0) == null);
        assertTrue("".equals(StringUtils.remove("", 0, 0)));
        assertTrue("".equals(StringUtils.remove("0", 0, 1)));
        assertTrue("".equals(StringUtils.remove("0123456789", 0, 10)));
    }

    @Test
    public void testindexNotBlank() {
        assertTrue(StringUtils.indexOfNotBlank("", 0, -1) == -1);
        assertTrue(StringUtils.indexOfNotBlank(" 123456789", 0, -1) == 1);
        assertTrue(StringUtils.indexOfNotBlank("        8 ", 0, -1) == 8);
        assertTrue(StringUtils.indexOfNotBlank("         9 ", 0, -1) == 9);
    }

    @Test
    public void testparseContentTypeCharset() {
        Assert.assertEquals("gbk", NetUtils.parseContentTypeCharset("application/soap+xml; charset=gbk"));
        Assert.assertEquals("gbk", NetUtils.parseContentTypeCharset("application/soap+xml; charset= gbk"));
        Assert.assertEquals("gbk", NetUtils.parseContentTypeCharset("application/soap+xml; charset = gbk"));
        Assert.assertEquals("gbk", NetUtils.parseContentTypeCharset("application/soap+xml; charset = gbk "));
        Assert.assertEquals("gbk", NetUtils.parseContentTypeCharset("application/soap+xml; charset =  gbk "));
        Assert.assertEquals("gbk", NetUtils.parseContentTypeCharset("application/soap+xml; charset =  gbk"));
        assertTrue(NetUtils.parseContentTypeCharset("application/soap+xml; charset =   ") == null);
        assertTrue(NetUtils.parseContentTypeCharset("application/soap+xml;  =   ") == null);
        assertTrue(NetUtils.parseContentTypeCharset("application/soap+xml;  charset  ") == null);
        assertTrue(NetUtils.parseContentTypeCharset("application/soap+xml;  charset gbk ") == null);
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

            assertTrue(val == v);
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
        assertTrue(StringUtils.maxlength("123", "1234", "1") == 4);
    }

}
