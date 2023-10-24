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
        System.out.println(ct.toStandardShape());
        System.out.println(SEPARATOR);

        String r = "";
        r += "" + FileUtils.lineSeparator;
        r += "  -" + FileUtils.lineSeparator;
        r += "  a" + FileUtils.lineSeparator;
        r += "  -" + FileUtils.lineSeparator;
        r += "  1" + FileUtils.lineSeparator;
        r += "  2" + FileUtils.lineSeparator;
        r += "  3" + FileUtils.lineSeparator;
        r += "  -";
        Assert.assertEquals(StringUtils.trimBlank(r), StringUtils.trimBlank(ct.toString()));
    }

    @Test
    public void test2() {
        System.out.println(SEPARATOR);
        CharTable ct = new CharTable();
        ct.addTitle("a");
        ct.addTitle("b");
        ct.addCell("1").addCell("2").addCell("3").addCell("4");
        System.out.println(ct.toStandardShape());
        System.out.println(SEPARATOR);

        String r = "";
        r += "  ----" + FileUtils.lineSeparator;
        r += "  a  b" + FileUtils.lineSeparator;
        r += "  -  -" + FileUtils.lineSeparator;
        r += "  1  2" + FileUtils.lineSeparator;
        r += "  3  4" + FileUtils.lineSeparator;
        r += "  ----";
        Assert.assertEquals(StringUtils.trimBlank(r), StringUtils.trimBlank(ct.toString()));
    }

    @Test
    public void test3() {
        System.out.println(SEPARATOR);
        CharTable ct = new CharTable();
        ct.addTitle("列");
        ct.addTitle("b");
        ct.addCell("1").addCell("2").addCell("3").addCell("4");
        System.out.println(ct.toStandardShape());
        System.out.println(SEPARATOR);

        String r = "";
        r += "  -----" + FileUtils.lineSeparator;
        r += "  列  b" + FileUtils.lineSeparator;
        r += "  --  -" + FileUtils.lineSeparator;
        r += "  1   2" + FileUtils.lineSeparator;
        r += "  3   4" + FileUtils.lineSeparator;
        r += "  -----";
        Assert.assertEquals(StringUtils.trimBlank(r), StringUtils.trimBlank(ct.toString()));
    }

    @Test
    public void test4() {
        System.out.println(SEPARATOR);
        CharTable ct = new CharTable();
        ct.addTitle("列");
        ct.addTitle("b");
        ct.addCell("1").addCell("2").addCell("3").addCell("4");
        System.out.println(ct.toStandardShape().ltrim());
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
        ct.toStandardShape().ltrim();
        for (String str : ct) {
            System.out.println(str);
        }
        System.out.println(SEPARATOR);

        String r = "";
        r += "----------------------------------" + FileUtils.lineSeparator;
        r += "列      b                         " + FileUtils.lineSeparator;
        r += "------  --------------------------" + FileUtils.lineSeparator;
        r += "第一列  测试换行列" + FileUtils.lineSeparator;
        r += "        你好啊俄式一下水电费水电费" + FileUtils.lineSeparator;
        r += "        世界" + FileUtils.lineSeparator;
        r += "3       4" + FileUtils.lineSeparator;
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
        ct.toDB2Shape().ltrim();
        for (String str : ct) {
            System.out.println(str);
        }
        System.out.println(SEPARATOR);

        String r = "";
        r += "列      b                         " + FileUtils.lineSeparator;
        r += "------  --------------------------" + FileUtils.lineSeparator;
        r += "第一列  测试换行列" + FileUtils.lineSeparator;
        r += "        你好啊俄式一下水电费水电费" + FileUtils.lineSeparator;
        r += "        世界" + FileUtils.lineSeparator;
        r += "3       4";
        Assert.assertEquals(StringUtils.trimBlank(r), StringUtils.trimBlank(ct.toString()));
    }

    @Test
    public void test7() {
        System.out.println(SEPARATOR);
        CharTable ct = new CharTable();
        ct.addTitle("列");
        ct.addTitle("b");
        ct.addCell("第一列").addCell("测试换行列\n你好啊俄式一下水电费水电费\n世界\n").addCell("3").addCell("4");
        ct.toShellShape().ltrim();
        for (String str : ct) {
            System.out.println(str);
        }
        System.out.println(SEPARATOR);

        String r = "";
        r += "列      b                         " + FileUtils.lineSeparator;
        r += "第一列  测试换行列" + FileUtils.lineSeparator;
        r += "        你好啊俄式一下水电费水电费" + FileUtils.lineSeparator;
        r += "        世界" + FileUtils.lineSeparator;
        r += "3       4";
        Assert.assertEquals(StringUtils.trimBlank(r), StringUtils.trimBlank(ct.toString()));
    }

    @Test
    public void test8() {
        System.out.println(SEPARATOR);
        CharTable ct = new CharTable();
        ct.addTitle("列");
        ct.addTitle("b");
        ct.addCell("第一列").addCell("测试换行列\n你好啊俄式一下水电费水电费\n世界\n").addCell("3").addCell("4");
        ct.toDB2Shape().ltrim();
        for (String str : ct) {
            System.out.println(str);
        }
        System.out.println(SEPARATOR);

        String r = "";
        r += "列      b                         " + FileUtils.lineSeparator;
        r += "------  --------------------------" + FileUtils.lineSeparator;
        r += "第一列  测试换行列" + FileUtils.lineSeparator;
        r += "        你好啊俄式一下水电费水电费" + FileUtils.lineSeparator;
        r += "        世界" + FileUtils.lineSeparator;
        r += "3       4";
        Assert.assertEquals(StringUtils.trimBlank(r), StringUtils.trimBlank(ct.toString()));
    }

}
