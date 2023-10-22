package icu.etl.util;

import org.junit.Test;

public class CharTableTest {

    public final static String SEPARATOR = "分隔符";

    @Test
    public void test1() {
        System.out.println(SEPARATOR);
        CharTable ct = new CharTable();
        ct.addTitle("a");
        ct.addCell("1").addCell("2").addCell("3");
        System.out.println(ct.toStandardShape());
        System.out.println(SEPARATOR);
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
    }

}
