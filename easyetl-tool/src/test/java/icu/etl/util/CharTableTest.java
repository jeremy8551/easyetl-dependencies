package icu.etl.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class CharTableTest {

    public final static String SEPARATOR = "\n\n";

    public void print(CharTable ct) {
        String str = ct.toString();
        List<CharSequence> list = StringUtils.splitLines(str, new ArrayList<CharSequence>());

        System.out.println("String r = \"\";");
        for (Iterator<CharSequence> it = list.iterator(); it.hasNext(); ) {
            String s = "r += \"" + it.next() + "\"";
            if (it.hasNext()) {
                s += " + FileUtils.lineSeparator;";
            } else {
                s += ";";
            }
            System.out.println(s);
        }
        System.out.println("Assert.assertEquals(StringUtils.trimBlank(r), StringUtils.trimBlank(ct.toString()));");
    }

    @Test
    public void test1() {
        System.out.println(SEPARATOR);
        CharTable ct = new CharTable();
        ct.addTitle("a");
        ct.addCell("1").addCell("2").addCell("3");
        System.out.println(ct.toString(CharTable.Style.standard));
        System.out.println(SEPARATOR);

        String r = "";
        r += "" + FileUtils.lineSeparator;
        r += "-" + FileUtils.lineSeparator;
        r += "a" + FileUtils.lineSeparator;
        r += "-" + FileUtils.lineSeparator;
        r += "1" + FileUtils.lineSeparator;
        r += "2" + FileUtils.lineSeparator;
        r += "3" + FileUtils.lineSeparator;
        r += "-";
        Assert.assertEquals(StringUtils.trimBlank(r), StringUtils.trimBlank(ct.toString()));
    }

    @Test
    public void test2() {
        System.out.println(SEPARATOR);
        CharTable ct = new CharTable();
        ct.addTitle("a");
        ct.addTitle("b");
        ct.addCell("1").addCell("2").addCell("3").addCell("4");
        System.out.println(ct.toString(CharTable.Style.standard));
        System.out.println(SEPARATOR);

        String r = "";
        r += "----" + FileUtils.lineSeparator;
        r += "a  b" + FileUtils.lineSeparator;
        r += "-  -" + FileUtils.lineSeparator;
        r += "1  2" + FileUtils.lineSeparator;
        r += "3  4" + FileUtils.lineSeparator;
        r += "----";
        Assert.assertEquals(StringUtils.trimBlank(r), StringUtils.trimBlank(ct.toString()));
    }

    @Test
    public void test3() {
        System.out.println(SEPARATOR);
        CharTable ct = new CharTable();
        ct.addTitle("列");
        ct.addTitle("b");
        ct.addCell("1").addCell("2").addCell("3").addCell("4");
        System.out.println(ct.toString(CharTable.Style.standard));
        System.out.println(SEPARATOR);

        String r = "";
        r += "-----" + FileUtils.lineSeparator;
        r += "列  b" + FileUtils.lineSeparator;
        r += "--  -" + FileUtils.lineSeparator;
        r += "1   2" + FileUtils.lineSeparator;
        r += "3   4" + FileUtils.lineSeparator;
        r += "-----";
        Assert.assertEquals(StringUtils.trimBlank(r), StringUtils.trimBlank(ct.toString()));
    }

    @Test
    public void test4() {
        System.out.println(SEPARATOR);
        CharTable ct = new CharTable();
        ct.addTitle("列");
        ct.addTitle("b");
        ct.addCell("1").addCell("2").addCell("3").addCell("4");
        System.out.println(ct.toString(CharTable.Style.standard));
        System.out.println(SEPARATOR);

        String r = "";
        r += "-----" + FileUtils.lineSeparator;
        r += "列  b" + FileUtils.lineSeparator;
        r += "--  -" + FileUtils.lineSeparator;
        r += "1   2" + FileUtils.lineSeparator;
        r += "3   4" + FileUtils.lineSeparator;
        r += "-----";
        Assert.assertEquals(StringUtils.trimBlank(r), StringUtils.trimBlank(ct.toString()));
    }

    @Test
    public void test5() {
        System.out.println(SEPARATOR);
        CharTable ct = new CharTable();
        ct.addTitle("列");
        ct.addTitle("b");
        ct.addCell("第一列").addCell("测试换行列\n你好啊俄式一下水电费水电费\n世界\n").addCell("3").addCell("4");
        ct.toString(CharTable.Style.standard);
        for (String str : ct) {
            System.out.println(str);
        }
        System.out.println(SEPARATOR);

        String r = "";
        r += "----------------------------------" + FileUtils.lineSeparator;
        r += "列      b                         " + FileUtils.lineSeparator;
        r += "------  --------------------------" + FileUtils.lineSeparator;
        r += "第一列  测试换行列                " + FileUtils.lineSeparator;
        r += "        你好啊俄式一下水电费水电费" + FileUtils.lineSeparator;
        r += "        世界                      " + FileUtils.lineSeparator;
        r += "3       4                         " + FileUtils.lineSeparator;
        r += "----------------------------------";
        Assert.assertEquals(StringUtils.trimBlank(r), StringUtils.trimBlank(ct.toString()));
    }

    @Test
    public void test6() {
        System.out.println(SEPARATOR);
        CharTable ct = new CharTable();
        ct.addTitle("列");
        ct.addTitle("b");
        ct.addCell("第一列").addCell("测试换行列\n你好啊俄式一下水电费水电费\n世界\n").addCell("3").addCell("4");
        ct.toString(CharTable.Style.db2);
        for (String str : ct) {
            System.out.println(str);
        }
        System.out.println(SEPARATOR);

        String r = "";
        r += "列      b                         " + FileUtils.lineSeparator;
        r += "------  --------------------------" + FileUtils.lineSeparator;
        r += "第一列  测试换行列                " + FileUtils.lineSeparator;
        r += "        你好啊俄式一下水电费水电费" + FileUtils.lineSeparator;
        r += "        世界                      " + FileUtils.lineSeparator;
        r += "3       4                         ";
        Assert.assertEquals(StringUtils.trimBlank(r), StringUtils.trimBlank(ct.toString()));
    }

    @Test
    public void test7() {
        System.out.println(SEPARATOR);
        CharTable ct = new CharTable();
        ct.addTitle("列");
        ct.addTitle("b");
        ct.addCell("第一列").addCell("测试换行列\n你好啊俄式一下水电费水电费\n世界\n").addCell("3").addCell("4");
        ct.toString(CharTable.Style.shell);
        for (String str : ct) {
            System.out.println(str);
        }
        System.out.println(SEPARATOR);

        String r = "";
        r += "列      b                         " + FileUtils.lineSeparator;
        r += "第一列  测试换行列                " + FileUtils.lineSeparator;
        r += "        你好啊俄式一下水电费水电费" + FileUtils.lineSeparator;
        r += "        世界                      " + FileUtils.lineSeparator;
        r += "3       4                         ";
        Assert.assertEquals(StringUtils.trimBlank(r), StringUtils.trimBlank(ct.toString()));
    }

    @Test
    public void test8() {
        System.out.println(SEPARATOR);
        CharTable ct = new CharTable();
        ct.addTitle("列");
        ct.addTitle("b");
        ct.addCell("第一列").addCell("测试换行列\n你好啊俄式一下水电费水电费\n世界\n").addCell("3").addCell("4");
        ct.toString(CharTable.Style.db2);
        for (String str : ct) {
            System.out.println(str);
        }
        System.out.println(SEPARATOR);

        String r = "";
        r += "列      b                         " + FileUtils.lineSeparator;
        r += "------  --------------------------" + FileUtils.lineSeparator;
        r += "第一列  测试换行列                " + FileUtils.lineSeparator;
        r += "        你好啊俄式一下水电费水电费" + FileUtils.lineSeparator;
        r += "        世界                      " + FileUtils.lineSeparator;
        r += "3       4";
        Assert.assertEquals(StringUtils.trimBlank(r), StringUtils.trimBlank(ct.toString()));
    }

    @Test
    public void test9() {
        System.out.println(SEPARATOR);
        CharTable ct = new CharTable();
        ct.addTitle("测试1-");
        ct.addTitle("测试2");
        ct.addTitle("测试3");
        ct.addCell("1");
        ct.addCell("测试\nksdf");
        ct.addCell("");
        ct.addCell("s");
        ct.addCell("|");
        ct.addCell("_");
        ct.addCell("----");
        ct.addCell("");
        ct.addCell("");
        ct.toString(CharTable.Style.markdown);
        for (String str : ct) {
            System.out.println(str);
        }

        String r = "";
        r += "| 测试1- | 测试2        | 测试3 |" + FileUtils.lineSeparator;
        r += "| ------ | ------------ | ----- |" + FileUtils.lineSeparator;
        r += "| 1      | 测试<br>ksdf |       |" + FileUtils.lineSeparator;
        r += "| s      | \\|           | _     |" + FileUtils.lineSeparator;
        r += "| ----   |              |       |";
        Assert.assertEquals(StringUtils.trimBlank(r), StringUtils.trimBlank(ct.toString()));
    }

    @Test
    public void test10() {
        System.out.println(SEPARATOR);
        CharTable ct = new CharTable();
        ct.addTitle("测试1-");
        ct.addTitle("测试2");
        ct.addTitle("测试3");
        ct.addCell("1");
        ct.addCell("2\n3\n5");
        ct.addCell("3\n4");
        ct.addCell("s");
        ct.addCell("k");
        ct.addCell("m");
        ct.addCell("o");
        ct.addCell("p\r\nq\ns\ni");
        ct.addCell("p");
        ct.toString(CharTable.Style.simple);
        for (String str : ct) {
            System.out.println(str);
        }

        String r = "";
        r += "1       2      3    " + FileUtils.lineSeparator;
        r += "        3      4    " + FileUtils.lineSeparator;
        r += "        5           " + FileUtils.lineSeparator;
        r += "s       k      m    " + FileUtils.lineSeparator;
        r += "o       p      p    " + FileUtils.lineSeparator;
        r += "        q           " + FileUtils.lineSeparator;
        r += "        s           " + FileUtils.lineSeparator;
        r += "        i           ";
        Assert.assertEquals(StringUtils.trimBlank(r), StringUtils.trimBlank(ct.toString()));
    }

}
