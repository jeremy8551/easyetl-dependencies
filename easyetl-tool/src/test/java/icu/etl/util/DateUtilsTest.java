package icu.etl.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class DateUtilsTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testcalcDate() {
        Date date = Dates.parse("2020-01-01");
        Ensure.isTrue(Dates.parse("2020-01-02").equals(Dates.calcDay(date, Calendar.DAY_OF_MONTH, 1)));
        Ensure.isTrue(Dates.parse("2020-02-01").equals(Dates.calcDay(date, Calendar.MONTH, 1)));
        Ensure.isTrue(Dates.parse("2021-01-01").equals(Dates.calcDay(date, Calendar.YEAR, 1)));
        Ensure.isTrue(Dates.parse("2020-01-01 01:00:00").equals(Dates.calcDay(date, Calendar.HOUR, 1)));
        Ensure.isTrue(Dates.parse("2020-01-01 00:01:00").equals(Dates.calcDay(date, Calendar.MINUTE, 1)));
        Ensure.isTrue(Dates.parse("2020-01-01 00:00:01").equals(Dates.calcDay(date, Calendar.SECOND, 1)));
        Ensure.isTrue(Dates.parse("2020-01-01 00:00:00:001").equals(Dates.calcDay(date, Calendar.MILLISECOND, 1)));
    }

    @Test
    public void testgetRandom() {
        for (int i = 1; i < 1000; i++) {
            Date s = Dates.random(Dates.parse("1960-01-01"), new Date());
            Random r = new Random();
            Date e = Dates.calcDay(s, r.nextInt(1000));

            Date date = Dates.random(s, e);
            Ensure.isTrue(Dates.between(date, s, e));
        }
    }

    @Test
    public void test123() {
        Timestamp t1 = new Timestamp(new Date().getTime());
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Timestamp t2 = new Timestamp(new Date().getTime());
//		System.out.println(StringUtils.toString(t1));
//		System.out.println(StringUtils.toString(t2));
        assertTrue(Dates.compareIgnoreTime(t1, t2) == 0);
    }

    @Test
    public void test1() {
        Date date = Dates.currentDate();
        assertTrue(date.compareTo(Dates.parse(Dates.format21(date))) == 0);
    }

    @Test
    public void testCurDate06() {
        assertTrue(Dates.currentTimeStamp().equals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
    }

    @Test
    public void testFormatStringArray() {
        Date[] array = Dates.parse("20190801", "20190802", "20190831");
        assertTrue(array[0].equals(Dates.parse("20190801")));
        assertTrue(array[1].equals(Dates.parse("20190802")));
        assertTrue(array[2].equals(Dates.parse("20190831")));

        assertTrue(StringUtils.toString(Dates.parse("2017-01-01", "2017-02-01", null)).equals("Date[Sun Jan 01 00:00:00 CST 2017, Wed Feb 01 00:00:00 CST 2017, null]"));
    }

    @Test
    public void testFormatObject() throws ParseException {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String str = "2017-01-01";
        Date date = f.parse(str);

        Date currentDate = new Date();

        assertTrue(Dates.format08(Dates.parse(" 20171130 ")).equals("20171130"));
        assertTrue(Dates.format08(Dates.parse(" 20170228 ")).equals("20170228"));
        assertTrue(Dates.format08(Dates.parse(" 2017-01-30 ")).equals("20170130"));
        assertTrue(Dates.format08(Dates.parse(" 2017-11-30 ")).equals("20171130"));
        assertTrue(Dates.format08(Dates.parse(" 二零一七年 十一月三十号 ")).equals("20171130"));
        assertTrue(Dates.format08(Dates.parse(" 二零一七年 十一月三十日 ")).equals("20171130"));
        assertTrue(Dates.format08(Dates.parse(" 二零一七年 十二月三十号 ")).equals("20171230"));
        assertTrue(Dates.format08(Dates.parse(" 二零一七年 十二月三十一号 ")).equals("20171231"));
        assertTrue(Dates.format08(Dates.parse(" 二零一七年 十月三十一号 ")).equals("20171031"));
        // System.out.println(DT.format02(DT.format(" 二零一七年 十月十一号 ")));
        assertTrue(Dates.format08(Dates.parse(" 二零一七年 十月十一号 ")).equals("20171011"));
        assertTrue(Dates.format08(Dates.parse(" 二零一七年 九月十一号 ")).equals("20170911"));
        assertTrue(Dates.format08(Dates.parse(" 二零一七年十月一号 ")).equals("20171001"));
        assertTrue(Dates.format08(Dates.parse(" 二零一七年十月十号 ")).equals("20171010"));
        assertTrue(Dates.format08(Dates.parse(" 二零一七年一月一号 ")).equals("20170101"));
        assertTrue(Dates.format08(Dates.parse(" 二零一七年一月十号 ")).equals("20170110"));
        assertTrue(Dates.format08(Dates.parse(" 二零一七年一月十二号 ")).equals("20170112"));
        assertTrue(Dates.format08(Dates.parse(" 二零一七年一月二十号 ")).equals("20170120"));
        assertTrue(Dates.format08(Dates.parse(" 二零一七年一月二十一号 ")).equals("20170121"));
        assertTrue(Dates.format08(Dates.parse(" 二零一七年一月三十号 ")).equals("20170130"));
        assertTrue(Dates.format08(Dates.parse(" 二零一七年一月三十一号 ")).equals("20170131"));
        assertTrue(Dates.format08(Dates.parse(" 二零一七年十月三十一号 ")).equals("20171031"));
        assertTrue(Dates.format08(Dates.parse(" 二零一七年十二月三十一号 ")).equals("20171231"));
        assertTrue(Dates.format08(Dates.parse(" 二零一七年十二月十号 ")).equals("20171210"));
        assertTrue(Dates.format08(Dates.parse(" 二零一七年十二月十 ")).equals("20171210"));

        assertTrue(Dates.format08(Dates.parse(" 二零一七年十二月十日")).equals("20171210"));
        assertTrue(Dates.format08(Dates.parse(" 二零一七年一月二日")).equals("20170102"));

        assertTrue(Dates.format08(Dates.parse(" 二零一七年十二月十三 ")).equals("20171213"));
        assertTrue(Dates.format08(Dates.parse(" 二零一七年十二月二十三 ")).equals("20171223"));
        assertTrue(Dates.format08(Dates.parse(" 1九九八年十二月二十三 ")).equals("19981223"));
        assertTrue(Dates.format08(Dates.parse(" 1九九八 年 十二 月 二十三 ")).equals("19981223"));
        assertTrue(Dates.format08(Dates.parse(" 1998年10月11号 ")).equals("19981011"));
        assertTrue(Dates.format08(Dates.parse(" 1998年10月11号13点13分13秒120毫秒 ")).equals("19981011"));
        assertTrue(Dates.format08(Dates.parse(" Sun Oct 11 00:00:00 GMT+08:00 1998")).equals("19981011"));

        try {
            Dates.format08(Dates.parse(" Thu Oct 11 00:00:00 GMT+08:00 1998"));
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }
        assertTrue(Dates.format08(Dates.parse(date)).equals("20170101"));
        assertTrue(Dates.format08(Dates.parse("2017-2-03")).equals("20170203"));
        assertTrue(Dates.format08(Dates.parse("2017-2-3")).equals("20170203"));
        assertTrue(Dates.format08(Dates.parse("2017-12-3")).equals("20171203"));
        assertTrue(Dates.format08(Dates.parse("2017-12-31")).equals("20171231"));
        assertTrue(Dates.format08(Dates.parse("2017-12-2")).equals("20171202"));
        assertTrue(Dates.format08(Dates.parse("2017.12.2")).equals("20171202"));
        assertTrue(Dates.format08(Dates.parse("2017.2.3")).equals("20170203"));
        assertTrue(Dates.format08(Dates.parse(new java.sql.Date(date.getTime()))).equals("20170101"));
        assertTrue(Dates.format21(Dates.parse("2019/04/29 17:07")).equals("2019-04-29 17:07:00:000"));

        assertTrue(Dates.format19(currentDate).equals(Dates.format19(Dates.parse(currentDate.toString()))));
        assertTrue("2017-12-31 13:45:00".equals(Dates.format19(Dates.parse("2017-12-31 13:45"))));
        assertTrue("2017-12-31 13:00:00".equals(Dates.format19(Dates.parse("2017-12-31 13"))));
        assertTrue("2017-12-31 13:45:32".equals(Dates.format19(Dates.parse("2017-12-31 13:45:32"))));
        assertTrue("2017-12-31 13:45:32".equals(Dates.format19(Dates.parse("2017-12-31 13.45.32"))));
        assertTrue("2017-12-31 13:45:32".equals(Dates.format19(Dates.parse("2017/12/31 13.45.32"))));
        assertTrue("2017-12-31 13:45:59".equals(Dates.format19(Dates.parse("2017/12/31 13.45.59"))));
        assertTrue("2017-12-31 23:59:59".equals(Dates.format19(Dates.parse("2017/12/31 23.59.59"))));
        assertTrue("2017-12-31 23:59:59:123".equals(Dates.format21(Dates.parse("2017/12/31 23.59.59:123"))));
        assertTrue("2017-12-31 23:59:59:999".equals(Dates.format21(Dates.parse("2017/12/31 23.59.59:999"))));
        assertTrue("2017-12-31 00:00:00".equals(Dates.format19(Dates.parse("2017/12/31 00.00.00"))));
        assertTrue(Dates.format08(Dates.parse(" 二零一七 年 十 二 月 二 十 三 ")).equals("20171223"));
        assertTrue(Dates.format08(Dates.parse("01/02/2017")).equals("20170102"));
        assertTrue(Dates.format08(Dates.parse("02/02/2017")).equals("20170202"));
//		assertTrue(Dates.format07(Dates.format("31/05/2017")).equals("20170531"));
        assertTrue(Dates.format08(Dates.parse("05/31/2017")).equals("20170531"));

        assertTrue(Dates.format21(Dates.parse("2019-01-23 01")).equals("2019-01-23 01:00:00:000"));
        assertTrue(Dates.format21(Dates.parse("2019-01-23 01:23")).equals("2019-01-23 01:23:00:000"));
        assertTrue(Dates.format21(Dates.parse("2019-01-23 01:23:45")).equals("2019-01-23 01:23:45:000"));
        assertTrue(Dates.format21(Dates.parse("2019-01-23 01:23:45:67")).equals("2019-01-23 01:23:45:067"));
        assertTrue(Dates.format21(Dates.parse("2019-01-23 01:23:45:167")).equals("2019-01-23 01:23:45:167"));

        assertTrue(Dates.format21(Dates.parse("2019012301")).equals("2019-01-23 01:00:00:000"));
        assertTrue(Dates.format21(Dates.parse("201901230123")).equals("2019-01-23 01:23:00:000"));
        assertTrue(Dates.format21(Dates.parse("201904291707")).equals("2019-04-29 17:07:00:000"));
        assertTrue(Dates.format21(Dates.parse("20190429170708")).equals("2019-04-29 17:07:08:000"));
        assertTrue(Dates.format21(Dates.parse("201904291707081")).equals("2019-04-29 17:07:08:001"));
        assertTrue(Dates.format21(Dates.parse("2019042917070812")).equals("2019-04-29 17:07:08:012"));
        assertTrue(Dates.format21(Dates.parse("20190429170708123")).equals("2019-04-29 17:07:08:123"));
        assertTrue(Dates.format21(Dates.parse("20190429170708012")).equals("2019-04-29 17:07:08:012"));
        assertTrue(Dates.format21(Dates.parse("20190429170708002")).equals("2019-04-29 17:07:08:002"));

        assertTrue(Dates.format19(Dates.parse("Thu Mar 21 06:14:26 UTC 2019")).equals("2019-03-21 06:14:26"));
        assertTrue(Dates.format21(Dates.parse("Thu Mar 21 06:14:26:999 UTC 2019")).equals("2019-03-21 06:14:26:999"));
        assertTrue(Dates.format19(Dates.parse("2019年 03月 21日 星期四 06:13:57 UTC")).equals("2019-03-21 06:13:57"));
        assertTrue(Dates.format19(Dates.parse("2019年 03月 21日 星期四 06:13:57 UTC+08:00")).equals("2019-03-21 06:13:57"));

        assertTrue(Dates.format19(Dates.parse(" 31 december 2017 at   08:38")).equals("2017-12-31 08:38:00"));
        assertTrue(Dates.format19(Dates.parse("31 december 2017 at 08:38")).equals("2017-12-31 08:38:00"));
        assertTrue(Dates.format19(Dates.parse("31 december 2017 at 08:38:12")).equals("2017-12-31 08:38:12"));
        assertTrue(Dates.format21(Dates.parse("31 december 2017 at 08:38:12:99")).equals("2017-12-31 08:38:12:099"));
        assertTrue(Dates.format19(Dates.parse("31  december 2017 at 08:38 ")).equals("2017-12-31 08:38:00"));
        assertTrue(Dates.format19(Dates.parse("31 dec 2017")).equals("2017-12-31 00:00:00"));

        assertTrue(Dates.format19(Dates.parse(currentDate.toString())).equals(Dates.format19(currentDate)));
        assertTrue(Dates.format19(Dates.parse("2019年4月29号")).equals("2019-04-29 00:00:00"));
        assertTrue(Dates.format19(Dates.parse("2019年 4月29号")).equals("2019-04-29 00:00:00"));
        assertTrue(Dates.format19(Dates.parse("2019年 4月   29号")).equals("2019-04-29 00:00:00"));

        date = Dates.parse("2014-04-03");
        assertTrue(Dates.getYear(date) == 2014);
        assertTrue(Dates.getMonth(date) == 4);

        assertTrue(Dates.getMonth(Dates.parse("20190101")) == 1);
        assertTrue(Dates.getMonth(Dates.parse("20190201")) == 2);
        assertTrue(Dates.getMonth(Dates.parse("20190301")) == 3);
        assertTrue(Dates.getMonth(Dates.parse("20190401")) == 4);
        assertTrue(Dates.getMonth(Dates.parse("20190501")) == 5);
        assertTrue(Dates.getMonth(Dates.parse("20190601")) == 6);
        assertTrue(Dates.getMonth(Dates.parse("20190701")) == 7);
        assertTrue(Dates.getMonth(Dates.parse("20190801")) == 8);
        assertTrue(Dates.getMonth(Dates.parse("20190901")) == 9);
        assertTrue(Dates.getMonth(Dates.parse("20191001")) == 10);
        assertTrue(Dates.getMonth(Dates.parse("20191101")) == 11);
        assertTrue(Dates.getMonth(Dates.parse("20191201")) == 12);
        try {
            Dates.getMonth(Dates.parse("20191301"));
            throw new RuntimeException();
        } catch (Exception e) {
        }
        assertTrue(Dates.getDayOfMonth(date) == 3);

        assertTrue(Dates.getDayOfWeek(date) == 4);
        assertTrue(Dates.getDayOfWeek(Dates.parse("20190429")) == 1);
        assertTrue(Dates.getDayOfWeek(Dates.parse("20190430")) == 2);
        assertTrue(Dates.getDayOfWeek(Dates.parse("20190501")) == 3);
        assertTrue(Dates.getDayOfWeek(Dates.parse("20190502")) == 4);
        assertTrue(Dates.getDayOfWeek(Dates.parse("20190503")) == 5);
        assertTrue(Dates.getDayOfWeek(Dates.parse("20190504")) == 6);
        assertTrue(Dates.getDayOfWeek(Dates.parse("20190505")) == 7);

        assertTrue(Dates.getDayOfYear(date) == 93);
        assertTrue(Dates.getDayOfYear(Dates.parse("20190101")) == 1);
        assertTrue(Dates.getDayOfYear(Dates.parse("20191231")) == 365);
        assertTrue(Dates.getDayOfYear(Dates.parse("20201231")) == 366);

        assertTrue(Dates.calcMonth(Dates.parse("2017-07-01"), Dates.parse("2017-07-01")) == 0);
        assertTrue(Dates.calcMonth(Dates.parse("2017-07-01"), Dates.parse("2017-08-01")) == 1);
        assertTrue(Dates.calcMonth(Dates.parse("2017-07-01"), Dates.parse("2017-09-01")) == 2);
        assertTrue(Dates.calcMonth(Dates.parse("2017-10-01"), Dates.parse("2017-09-01")) == -1);
    }

    @Test
    public void testxiaolv() {
        // 测试效率
        TimeWatch watch = new TimeWatch();
        for (int i = 1; i <= 100000; i++) { // 循环一百万次
            Dates.parse("2019-01-23");
        }
        assertTrue(watch.useSeconds() < 2);

        watch.start();
        for (int i = 1; i <= 100000; i++) { // 循环一百万次
            Dates.parse("2019-01-23");
        }
        assertTrue(watch.useSeconds() < 10);
    }

    @Test
    public void testFormateCNDate() {
        assertTrue(Dates.formatCN(Dates.parse("2019-01-23")).equals("2019年1月23日"));
        assertTrue(Dates.formatCN(Dates.parse("2019-01-09")).equals("2019年1月9日"));
    }

    @Test
    public void testFormat01Date() {
        assertTrue(Dates.format10(Dates.parse("2019-01-23")).equals("2019-01-23"));
        assertTrue(Dates.format10(Dates.parse("12/01/2019")).equals("2019-12-01"));
        assertTrue(Dates.format10(Dates.parse("2013-12-31")).equals("2013-12-31"));
    }

    @Test
    public void testFormat01String() {
        assertTrue(Dates.parse((String) null) == null); // TODO
        assertTrue(Dates.parse("2019-01-23").equals(Dates.parse("2019-01-23"))); // TODO
    }

    @Test
    public void testFormat02Date() {
        assertTrue(Dates.format08((Date) null) == null); // TODO
        assertTrue(Dates.format08(Dates.parse("2019-01-23")).equals("20190123")); // TODO
    }

    @Test
    public void testFormat02String() {
        assertTrue(Dates.parse((String) null) == null); // TODO
        assertTrue(Dates.parse("20190123").equals(Dates.parse("2019-01-23"))); // TODO
    }

    @Test
    public void testFormat03String() {
        assertTrue(true); // TODO
    }

    @Test
    public void testFormat04Date() {
        assertTrue(true); // TODO
    }

    @Test
    public void testFormat04String() {
        assertTrue(Dates.parse((String) null) == null);
        assertTrue(Dates.parse("2019-01-23 01:23:45:06").equals(Dates.parse("2019-01-23 01:23:45:06")));
    }

    @Test
    public void testFormat05Date() {
        assertTrue(Dates.format16((Date) null) == null);
        assertTrue(Dates.format16(Dates.parse("2019-01-23 01:23:45")).equals("2019-01-23 01:23"));
    }

    @Test
    public void testFormat05String() {
        assertTrue(Dates.parse((String) null) == null);
        assertTrue(Dates.parse("2019-01-23 01:23").equals(Dates.parse("2019-01-23 01:23:00")));
    }

    @Test
    public void testFormat06Date() {
        assertTrue(Dates.format19((Date) null) == null);
        assertTrue(Dates.format19(Dates.parse("2019-01-23 01:23:45")).equals("2019-01-23 01:23:45"));
    }

    @Test
    public void testFormat06String() {
        assertTrue(Dates.parse((String) null) == null);
        assertTrue(Dates.parse("2019-01-23 01:23").equals(Dates.parse("2019-01-23 01:23:00")));
    }

    @Test
    public void testFormat07Date() {
        assertTrue(Dates.format14((Date) null) == null);
        assertTrue(Dates.format14(Dates.parse("2019-01-23 01:23:45")).equals("20190123012345"));
    }

    @Test
    public void testFormatUSADate() {
        assertTrue(Dates.format01((Date) null) == null);
        assertTrue(Dates.format01(Dates.parse("2019-01-23 01:23:45")).equals("01/23/2019"));
    }

    @Test
    public void testFormatUKDate() {
        assertTrue(Dates.format02((Date) null) == null);
        assertTrue(Dates.format02(Dates.parse("2019-01-23 01:23:45")).equals("23/01/2019"));
    }

    @Test
    public void testFormateStringDateString() {
        assertTrue(Dates.format(null, "") == null);
        assertTrue(Dates.format(Dates.parse("2019-01-23"), "yyyy-MM-dd").equals("2019-01-23"));
    }

    @Test
    public void testFormatStringString() {
        assertTrue(Dates.parse((String) null) == null);
        assertTrue(Dates.parse("20190123").equals(Dates.parse("2019-01-23")));
    }

    @Test
    public void testCalcMonth() {
        assertTrue(Dates.calcMonth(Dates.parse("2019-01-23"), 0).equals(Dates.parse("2019-01-23")));
        assertTrue(Dates.calcMonth(Dates.parse("2019-01-23"), 1).equals(Dates.parse("2019-02-23")));
        assertTrue(Dates.calcMonth(Dates.parse("2019-02-28"), 12).equals(Dates.parse("2020-02-28")));
        assertTrue(Dates.calcMonth(Dates.parse("2019-03-23"), -1).equals(Dates.parse("2019-02-23")));
        assertTrue(Dates.calcMonth(Dates.parse("2019-01-23"), -1).equals(Dates.parse("2018-12-23")));
        assertTrue(Dates.calcMonth(Dates.parse("2019-12-23"), -12).equals(Dates.parse("2018-12-23")));
        assertTrue(Dates.calcMonth(Dates.parse("2019-12-23"), -13).equals(Dates.parse("2018-11-23")));
        assertTrue(Dates.calcMonth(Dates.parse("2016-03-30"), -1).equals(Dates.parse("2016-02-29")));
        assertTrue(Dates.calcMonth(Dates.parse("2016-01-29"), 1).equals(Dates.parse("2016-02-29")));

        assertTrue(Dates.format10(Dates.calcMonth(Dates.getBeginOfMonth(Dates.parse("2017-09-12")), -1)).equals("2017-08-01"));
        assertTrue(Dates.format10(Dates.calcMonth(Dates.parse("2019-04-30"), 0)).equals("2019-04-30"));
        assertTrue(Dates.format10(Dates.calcMonth(Dates.parse("2019-04-30"), 1)).equals("2019-05-30"));
        assertTrue(Dates.format10(Dates.calcMonth(Dates.parse("2019-04-30"), 10)).equals("2020-02-29"));
        assertTrue(Dates.format10(Dates.calcMonth(Dates.parse("2019-04-30"), -12)).equals("2018-04-30"));
    }

    @Test
    public void testCalcYear() {
        assertTrue(Dates.calcYear(Dates.parse("2019-01-23"), 0).equals(Dates.parse("2019-01-23")));
        assertTrue(Dates.calcYear(Dates.parse("2016-02-29"), 1).equals(Dates.parse("2017-02-28")));

        assertTrue(Dates.format10(Dates.calcYear(Dates.parse("2019-04-30"), 0)).equals("2019-04-30"));
        assertTrue(Dates.format10(Dates.calcYear(Dates.parse("2019-04-30"), 1)).equals("2020-04-30"));
        assertTrue(Dates.format10(Dates.calcYear(Dates.parse("2019-04-30"), 10)).equals("2029-04-30"));
        assertTrue(Dates.format10(Dates.calcYear(Dates.parse("2019-04-30"), -1)).equals("2018-04-30"));
        assertTrue(Dates.format10(Dates.calcYear(Dates.parse("2019-04-30"), -10)).equals("2009-04-30"));
    }

    @Test
    public void testCalcDateDateInt() {
        assertTrue(Dates.format10(Dates.calcDay(Dates.parse("2019-04-30"), 0)).equals("2019-04-30"));
        assertTrue(Dates.format10(Dates.calcDay(Dates.parse("2019-04-30"), 1)).equals("2019-05-01"));
        assertTrue(Dates.format10(Dates.calcDay(Dates.parse("2019-04-30"), 10)).equals("2019-05-10"));
        assertTrue(Dates.format10(Dates.calcDay(Dates.parse("2019-04-30"), -1)).equals("2019-04-29"));
        assertTrue(Dates.format10(Dates.calcDay(Dates.parse("2019-04-30"), -61)).equals("2019-02-28"));
    }

    @Test
    public void testCalcDays() {
        assertTrue(Dates.calcDay(Dates.parse("2016-05-01"), Dates.parse("2016-10-01")) == 153);
        assertTrue(Dates.calcDay(Dates.parse("2016-05-01"), Dates.parse("2016-05-01")) == 0);
        assertTrue(Dates.calcDay(Dates.parse("2016-05-01"), Dates.parse("2016-05-02")) == 1);
        assertTrue(Dates.calcDay(Dates.parse("2016-05-01"), Dates.parse("2017-05-02")) == 366);
    }

    @Test
    public void testGetBeginOfMonth() {
        assertTrue(Dates.format10(Dates.calcMonth(Dates.getBeginOfMonth(Dates.parse("2017-09-12")), -1)).equals("2017-08-01"));
    }

    @Test
    public void testGetEndOfMonth() {
        assertTrue(Dates.getEndOfMonth(Dates.parse("2019-02-28")).equals(Dates.parse("2019-02-28")));
        assertTrue(Dates.getEndOfMonth(Dates.parse("2019-02-01")).equals(Dates.parse("2019-02-28")));
        assertTrue(Dates.getEndOfMonth(Dates.parse("2020-02-28")).equals(Dates.parse("2020-02-29")));
        assertTrue(Dates.getEndOfMonth(Dates.parse("2020-02-01")).equals(Dates.parse("2020-02-29")));
    }

    @Test
    public void testGetDayOfWeekString() {
        assertTrue(Dates.parseDayOfWeek("星期1") == 1);
        assertTrue(Dates.parseDayOfWeek("星期一") == 1);
        assertTrue(Dates.parseDayOfWeek("星期 7") == 7);
        assertTrue(Dates.parseDayOfWeek("星期 七") == 7);
        assertTrue(Dates.parseDayOfWeek("星期 二") == 2);
        assertTrue(Dates.parseDayOfWeek("星期 2 ") == 2);
        assertTrue(Dates.parseDayOfWeek("mon") == 1);
        assertTrue(Dates.parseDayOfWeek("tue") == 2);
        assertTrue(Dates.parseDayOfWeek("wed") == 3);
        assertTrue(Dates.parseDayOfWeek("thu") == 4);
        assertTrue(Dates.parseDayOfWeek("fri") == 5);
        assertTrue(Dates.parseDayOfWeek("sat") == 6);
        assertTrue(Dates.parseDayOfWeek("sun") == 7);
    }

    @Test
    public void testGetDayOfWeekDate() {
        assertTrue(Dates.getDayOfWeek(Dates.parse("20190829")) == 4);
        assertTrue(Dates.getDayOfWeek(Dates.parse("20190429")) == 1);
        assertTrue(Dates.getDayOfWeek(Dates.parse("20190430")) == 2);
        assertTrue(Dates.getDayOfWeek(Dates.parse("20190501")) == 3);
        assertTrue(Dates.getDayOfWeek(Dates.parse("20190502")) == 4);
        assertTrue(Dates.getDayOfWeek(Dates.parse("20190503")) == 5);
        assertTrue(Dates.getDayOfWeek(Dates.parse("20190504")) == 6);
        assertTrue(Dates.getDayOfWeek(Dates.parse("20190505")) == 7);
    }

    @Test
    public void testIsDate02() {
        assertTrue(Dates.testFormat08("20190228"));
        assertTrue(Dates.testFormat08("20190201"));
    }

    @Test
    public void testIsDateCharCharCharCharCharCharCharChar() {
        assertTrue(Dates.isDate('2', '0', '1', '0', '0', '1', '0', '1'));
        assertTrue(Dates.isDate('2', '0', '1', '0', '1', '2', '3', '1'));
        assertTrue(!Dates.isDate('2', '0', '1', '0', '1', '2', '3', '2'));
        assertTrue(Dates.isDate('2', '0', '1', '6', '0', '2', '2', '9'));
        assertTrue(!Dates.isDate('2', '0', '1', '7', '0', '2', '2', '9'));
    }

    @Test
    public void testIsDateIntIntInt() {
        assertTrue(Dates.isDate(2019, 1, 1));
        assertTrue(Dates.isDate(2019, 12, 31));
    }

//	@Test
//	public void testIs24TimeStringStringStringString() {
//		assertTrue(DT.is24Time("0", "0", "0", "0"));
//		assertTrue(DT.is24Time("23", "59", "59", "999"));
//		assertTrue(!DT.is24Time("24", "0", "0", "0"));
//		assertTrue(!DT.is24Time("23", "60", "59", "0"));
//		assertTrue(!DT.is24Time("23", "59", "60", "0"));
//		assertTrue(!DT.is24Time("23", "59", "59", "1000"));
//	}
//	
//	@Test
//	public void testIs24TimeStringStringString() {
//		assertTrue(DT.is24Time("00", "00", "00", "0"));
//		assertTrue(DT.is24Time("23", "59", "59", "0"));
//		assertTrue(!DT.is24Time("23", "59", "60", "0"));
//	}

    @Test
    public void testIs24TimeIntIntInt() {
        assertTrue(Dates.isTime(0, 0, 0, 0)); // TODO
        assertTrue(Dates.isTime(23, 59, 59, 0)); // TODO
        assertTrue(Dates.isTime(0, 0, 0, 0));
        assertTrue(!Dates.isTime(25, 0, 0, 0));
        assertTrue(!Dates.isTime(0, 61, 0, 0));
        assertTrue(!Dates.isTime(0, 0, 60, 0));
        assertTrue(!Dates.isTime(0, 0, 61, 0));
        assertTrue(Dates.isTime(9, 1, 59, 0));
        assertTrue(Dates.isTime(11, 1, 59, 0));
    }

    @Test
    public void testIs24TimeIntIntIntInt() {
        assertTrue(Dates.isTime(0, 0, 0, 0));
        assertTrue(Dates.isTime(23, 59, 59, 999));
        assertTrue(!Dates.isTime(24, 0, 0, 0));
        assertTrue(!Dates.isTime(23, 60, 59, 0));
        assertTrue(!Dates.isTime(23, 59, 60, 0));
        assertTrue(!Dates.isTime(23, 59, 59, 1000));
    }

    @Test
    public void testGetYear() {

        Date date = Dates.parse("2014-04-03");
        assertTrue(Dates.getYear(date) == 2014);
        assertTrue(Dates.getMonth(date) == 4);
        assertTrue(Dates.getMonth(Dates.parse("20190101")) == 1);
        assertTrue(Dates.getMonth(Dates.parse("20190201")) == 2);
        assertTrue(Dates.getMonth(Dates.parse("20190301")) == 3);
        assertTrue(Dates.getMonth(Dates.parse("20190401")) == 4);
        assertTrue(Dates.getMonth(Dates.parse("20190501")) == 5);
        assertTrue(Dates.getMonth(Dates.parse("20190601")) == 6);
        assertTrue(Dates.getMonth(Dates.parse("20190701")) == 7);
        assertTrue(Dates.getMonth(Dates.parse("20190801")) == 8);
        assertTrue(Dates.getMonth(Dates.parse("20190901")) == 9);
        assertTrue(Dates.getMonth(Dates.parse("20191001")) == 10);
        assertTrue(Dates.getMonth(Dates.parse("20191101")) == 11);
        assertTrue(Dates.getMonth(Dates.parse("20191201")) == 12);
        try {
            Dates.getMonth(Dates.parse("20191301"));
            throw new RuntimeException();
        } catch (Exception e) {
        }
        assertTrue(Dates.getDayOfMonth(date) == 3);

        assertTrue(Dates.getDayOfWeek(date) == 4);
        assertTrue(Dates.getDayOfWeek(Dates.parse("20190429")) == 1);
        assertTrue(Dates.getDayOfWeek(Dates.parse("20190430")) == 2);
        assertTrue(Dates.getDayOfWeek(Dates.parse("20190501")) == 3);
        assertTrue(Dates.getDayOfWeek(Dates.parse("20190502")) == 4);
        assertTrue(Dates.getDayOfWeek(Dates.parse("20190503")) == 5);
        assertTrue(Dates.getDayOfWeek(Dates.parse("20190504")) == 6);
        assertTrue(Dates.getDayOfWeek(Dates.parse("20190505")) == 7);

        assertTrue(Dates.getDayOfYear(date) == 93);
        assertTrue(Dates.getDayOfYear(Dates.parse("20190101")) == 1);
        assertTrue(Dates.getDayOfYear(Dates.parse("20191231")) == 365);
        assertTrue(Dates.getDayOfYear(Dates.parse("20201231")) == 366);
    }

    @Test
    public void testGetMonth() {

        Date date = Dates.parse("2014-04-03");
        assertTrue(Dates.getYear(date) == 2014);
        assertTrue(Dates.getMonth(date) == 4);
        assertTrue(Dates.getMonth(Dates.parse("20190101")) == 1);
        assertTrue(Dates.getMonth(Dates.parse("20190201")) == 2);
        assertTrue(Dates.getMonth(Dates.parse("20190301")) == 3);
        assertTrue(Dates.getMonth(Dates.parse("20190401")) == 4);
        assertTrue(Dates.getMonth(Dates.parse("20190501")) == 5);
        assertTrue(Dates.getMonth(Dates.parse("20190601")) == 6);
        assertTrue(Dates.getMonth(Dates.parse("20190701")) == 7);
        assertTrue(Dates.getMonth(Dates.parse("20190801")) == 8);
        assertTrue(Dates.getMonth(Dates.parse("20190901")) == 9);
        assertTrue(Dates.getMonth(Dates.parse("20191001")) == 10);
        assertTrue(Dates.getMonth(Dates.parse("20191101")) == 11);
        assertTrue(Dates.getMonth(Dates.parse("20191201")) == 12);
        try {
            Dates.getMonth(Dates.parse("20191301"));
            throw new RuntimeException();
        } catch (Exception e) {
        }
        assertTrue(Dates.getDayOfMonth(date) == 3);

        assertTrue(Dates.getDayOfWeek(date) == 4);
        assertTrue(Dates.getDayOfWeek(Dates.parse("20190429")) == 1);
        assertTrue(Dates.getDayOfWeek(Dates.parse("20190430")) == 2);
        assertTrue(Dates.getDayOfWeek(Dates.parse("20190501")) == 3);
        assertTrue(Dates.getDayOfWeek(Dates.parse("20190502")) == 4);
        assertTrue(Dates.getDayOfWeek(Dates.parse("20190503")) == 5);
        assertTrue(Dates.getDayOfWeek(Dates.parse("20190504")) == 6);
        assertTrue(Dates.getDayOfWeek(Dates.parse("20190505")) == 7);

        assertTrue(Dates.getDayOfYear(date) == 93);
        assertTrue(Dates.getDayOfYear(Dates.parse("20190101")) == 1);
        assertTrue(Dates.getDayOfYear(Dates.parse("20191231")) == 365);
        assertTrue(Dates.getDayOfYear(Dates.parse("20201231")) == 366);
    }

    @Test
    public void testGetMonthOfYear() {
        assertTrue(Dates.parseMonth("January") == 1);
        assertTrue(Dates.parseMonth("Jan") == 1);
        assertTrue(Dates.parseMonth("1月") == 1);
        assertTrue(Dates.parseMonth("一月") == 1);
        assertTrue(Dates.parseMonth(" 一 月  ") == 1);

        assertTrue(Dates.parseMonth("February") == 2);
        assertTrue(Dates.parseMonth("Feb") == 2);
        assertTrue(Dates.parseMonth("2月") == 2);
        assertTrue(Dates.parseMonth("二月") == 2);

        assertTrue(Dates.parseMonth("March") == 3);
        assertTrue(Dates.parseMonth("mar") == 3);
        assertTrue(Dates.parseMonth("3月") == 3);
        assertTrue(Dates.parseMonth("三月") == 3);

        assertTrue(Dates.parseMonth("April") == 4);
        assertTrue(Dates.parseMonth("apR") == 4);
        assertTrue(Dates.parseMonth("4月") == 4);
        assertTrue(Dates.parseMonth("四月") == 4);

        assertTrue(Dates.parseMonth("May") == 5);
        assertTrue(Dates.parseMonth("May") == 5);
        assertTrue(Dates.parseMonth("5月") == 5);
        assertTrue(Dates.parseMonth("五月") == 5);

        assertTrue(Dates.parseMonth("June") == 6);
        assertTrue(Dates.parseMonth("Jun") == 6);
        assertTrue(Dates.parseMonth("6月") == 6);
        assertTrue(Dates.parseMonth("六月") == 6);

        assertTrue(Dates.parseMonth("July") == 7);
        assertTrue(Dates.parseMonth("Jul") == 7);
        assertTrue(Dates.parseMonth("7月") == 7);
        assertTrue(Dates.parseMonth("七月") == 7);

        assertTrue(Dates.parseMonth("August") == 8);
        assertTrue(Dates.parseMonth("Aug") == 8);
        assertTrue(Dates.parseMonth("8月") == 8);
        assertTrue(Dates.parseMonth("八月") == 8);

        assertTrue(Dates.parseMonth("September") == 9);
        assertTrue(Dates.parseMonth("Sep") == 9);
        assertTrue(Dates.parseMonth("9月") == 9);
        assertTrue(Dates.parseMonth("九月") == 9);

        assertTrue(Dates.parseMonth("October") == 10);
        assertTrue(Dates.parseMonth("Oct") == 10);
        assertTrue(Dates.parseMonth("10月") == 10);
        assertTrue(Dates.parseMonth("十月") == 10);

        assertTrue(Dates.parseMonth("November") == 11);
        assertTrue(Dates.parseMonth("Nov") == 11);
        assertTrue(Dates.parseMonth("11月") == 11);
        assertTrue(Dates.parseMonth("十一月") == 11);

        assertTrue(Dates.parseMonth("December") == 12);
        assertTrue(Dates.parseMonth("Dec") == 12);
        assertTrue(Dates.parseMonth("12月") == 12);
        assertTrue(Dates.parseMonth("十二月") == 12);
    }

    @Test
    public void testGetMonthBetweenDay() {
        assertTrue(Dates.calcMonth(Dates.parse("2017-07-01"), Dates.parse("2017-07-01")) == 0);
        assertTrue(Dates.calcMonth(Dates.parse("2017-07-01"), Dates.parse("2017-08-01")) == 1);
        assertTrue(Dates.calcMonth(Dates.parse("2017-07-01"), Dates.parse("2017-09-01")) == 2);
        assertTrue(Dates.calcMonth(Dates.parse("2017-10-01"), Dates.parse("2017-09-01")) == -1);
    }

    @Test
    public void testGetDayOfMonth() {
        assertTrue(Dates.getDayOfMonth(Dates.parse("2016-01-01")) == 1);
        assertTrue(Dates.getDayOfMonth(Dates.parse("2016-02-29")) == 29);
        assertTrue(Dates.getDayOfMonth(Dates.parse("2016-01-31")) == 31);
    }

    @Test
    public void testGetHour() {
        assertTrue(Dates.getHour(Dates.parse("2019/04/30 00:00:00")) == 0);
        assertTrue(Dates.getHour(Dates.parse("2019/04/30 01:00:00")) == 1);
        assertTrue(Dates.getHour(Dates.parse("2019/04/30 02:00:00")) == 2);
        assertTrue(Dates.getHour(Dates.parse("2019/04/30 03:00:00")) == 3);
        assertTrue(Dates.getHour(Dates.parse("2019/04/30 04:00:00")) == 4);
        assertTrue(Dates.getHour(Dates.parse("2019/04/30 05:00:00")) == 5);
        assertTrue(Dates.getHour(Dates.parse("2019/04/30 06:00:00")) == 6);
        assertTrue(Dates.getHour(Dates.parse("2019/04/30 07:00:00")) == 7);
        assertTrue(Dates.getHour(Dates.parse("2019/04/30 08:00:00")) == 8);
        assertTrue(Dates.getHour(Dates.parse("2019/04/30 09:00:00")) == 9);
        assertTrue(Dates.getHour(Dates.parse("2019/04/30 10:00:00")) == 10);
        assertTrue(Dates.getHour(Dates.parse("2019/04/30 11:00:00")) == 11);
        assertTrue(Dates.getHour(Dates.parse("2019/04/30 12:00:00")) == 12);
        assertTrue(Dates.getHour(Dates.parse("2019/04/30 13:00:00")) == 13);
        assertTrue(Dates.getHour(Dates.parse("2019/04/30 14:00:00")) == 14);
        assertTrue(Dates.getHour(Dates.parse("2019/04/30 15:00:00")) == 15);
        assertTrue(Dates.getHour(Dates.parse("2019/04/30 16:00:00")) == 16);
        assertTrue(Dates.getHour(Dates.parse("2019/04/30 17:00:00")) == 17);
        assertTrue(Dates.getHour(Dates.parse("2019/04/30 18:00:00")) == 18);
        assertTrue(Dates.getHour(Dates.parse("2019/04/30 19:00:00")) == 19);
        assertTrue(Dates.getHour(Dates.parse("2019/04/30 20:00:00")) == 20);
        assertTrue(Dates.getHour(Dates.parse("2019/04/30 21:00:00")) == 21);
        assertTrue(Dates.getHour(Dates.parse("2019/04/30 22:00:00")) == 22);
        assertTrue(Dates.getHour(Dates.parse("2019/04/30 23:00:00")) == 23);
    }

    @Test
    public void testGetMinute() {
        assertTrue(Dates.getMinute(Dates.parse("2018-05-17 15:58:59")) == 58);
        assertTrue(Dates.getMinute(Dates.parse("2018-05-17 15:59:59")) == 59);
        assertTrue(Dates.getMinute(Dates.parse("2018-05-17 15:00:59")) == 0);
    }

    @Test
    public void testGetSecond() {
        assertTrue(Dates.getSecond(Dates.parse("2018-05-17 15:58:01")) == 1);
        assertTrue(Dates.getSecond(Dates.parse("2018-05-17 15:59:59")) == 59);
        assertTrue(Dates.getSecond(Dates.parse("2018-05-17 15:00:02")) == 2);
    }

    @Test
    public void testGetMillisecond() {
        assertTrue(Dates.getMillisecond(Dates.parse("2018-05-17 15:00:02:999")) == 999);
        assertTrue(Dates.getMillisecond(Dates.parse("2018-05-17 15:00:02:000")) == 0);
    }

    @Test
    public void testGetDayOfYear() {
        Date date = Dates.parse("2014-04-03");
        assertTrue(Dates.getDayOfYear(date) == 93);
        assertTrue(Dates.getDayOfYear(Dates.parse("20190101")) == 1);
        assertTrue(Dates.getDayOfYear(Dates.parse("20191231")) == 365);
        assertTrue(Dates.getDayOfYear(Dates.parse("20201231")) == 366);
    }

    @Test
    public void testBetween() {
        assertTrue(Dates.between(Dates.parse("2019-10-01"), Dates.parse("2019-10-01"), Dates.parse("2019-10-01")));
        assertTrue(Dates.between(Dates.parse("2019-10-01"), Dates.parse("2019-10-01"), Dates.parse("2019-10-02")));
        assertTrue(Dates.between(Dates.parse("2019-10-01"), Dates.parse("2019-10-01"), Dates.parse("2019-10-10")));
    }

    @Test
    public void testIsBeginOfMonth() {
        assertTrue(Dates.isBeginOfMonth(Dates.parse("2020-01-01")));
        assertTrue(Dates.isBeginOfMonth(Dates.parse("2020-02-01")));
        assertTrue(Dates.isBeginOfMonth(Dates.parse("2020-12-01")));
    }

    @Test
    public void testIsEndOfMonth() {
        assertTrue(Dates.isEndOfMonth(Dates.parse("2020-12-31")));
        assertTrue(!Dates.isEndOfMonth(Dates.parse("2020-02-28")));
        assertTrue(Dates.isEndOfMonth(Dates.parse("2020-02-29")));
        assertTrue(Dates.isEndOfMonth(Dates.parse("2016-02-29")));
    }

    @Test
    public void testIsWeekend() {
        assertTrue(Dates.isWeekend(Dates.parse("2019-04-27")));
        assertTrue(Dates.isWeekend(Dates.parse("2019-04-28")));
        assertTrue(!Dates.isWeekend(Dates.parse("2019-04-29")));
    }

    @Test
    public void testIsMonthOfYear() {
        assertTrue(Dates.isMonth("Jan"));
        assertTrue(Dates.isMonth("feb"));
        assertTrue(Dates.isMonth("mar"));
        assertTrue(Dates.isMonth("apr"));
        assertTrue(Dates.isMonth("may"));
        assertTrue(Dates.isMonth("jun"));
        assertTrue(Dates.isMonth("jul"));
        assertTrue(Dates.isMonth("aug"));
        assertTrue(Dates.isMonth("Sep"));
        assertTrue(Dates.isMonth("Oct"));
        assertTrue(Dates.isMonth("Nov"));
        assertTrue(Dates.isMonth("dec"));

        assertTrue(Dates.isMonth("january"));
        assertTrue(Dates.isMonth("february"));
        assertTrue(Dates.isMonth("march"));
        assertTrue(Dates.isMonth("april"));
        assertTrue(Dates.isMonth("may"));
        assertTrue(Dates.isMonth("june"));
        assertTrue(Dates.isMonth("july"));
        assertTrue(Dates.isMonth("august"));
        assertTrue(Dates.isMonth("September"));
        assertTrue(Dates.isMonth("October"));
        assertTrue(Dates.isMonth("November"));
        assertTrue(Dates.isMonth("december"));
    }

    @Test
    public void testIsDayOfWeek() {
        assertTrue(Dates.isDayOfWeek("mon"));
        assertTrue(Dates.isDayOfWeek("Tue"));
        assertTrue(Dates.isDayOfWeek("wed"));
        assertTrue(Dates.isDayOfWeek("thu"));
        assertTrue(Dates.isDayOfWeek("sat"));
        assertTrue(Dates.isDayOfWeek("sun"));
        assertTrue(Dates.isDayOfWeek("fri"));

        assertTrue(Dates.isDayOfWeek("monday"));
        assertTrue(Dates.isDayOfWeek("Tuesday"));
        assertTrue(Dates.isDayOfWeek("wednesday"));
        assertTrue(Dates.isDayOfWeek("thursday"));
        assertTrue(Dates.isDayOfWeek("saturday"));
        assertTrue(Dates.isDayOfWeek("sunday"));
        assertTrue(Dates.isDayOfWeek("friday"));
    }

    @Test
    public void testGreater() {
//		assertTrue(DT.greater(DT.format("2017-01-01"), DT.format("2017-01-01")) == false);
//		assertTrue(DT.greater(DT.format("2017-01-02"), DT.format("2017-01-01")) == true);
//		assertTrue(DT.greater(DT.format("2017-01-02"), null) == true);
//		assertTrue(DT.greater(null, DT.format("2017-01-01")) == false);
//		assertTrue(DT.greater(null, null) == false);
    }

    @Test
    public void testLess() {
//		assertTrue(DT.less(DT.format("2017-01-01"), DT.format("2017-01-01")) == false);
//		assertTrue(DT.less(DT.format("2017-01-01"), DT.format("2017-01-02")) == true);
//		assertTrue(DT.less(DT.format("2017-01-01"), null) == false);
//		assertTrue(DT.less(null, DT.format("2017-01-01")) == true);
//		assertTrue(DT.less(null, null) == false);
    }

    @Test
    public void testCompare() {
        assertTrue(Dates.compare(null, null) == 0);
        assertTrue(Dates.compare(null, Dates.parse("20191010")) == -1);
        assertTrue(Dates.compare(Dates.parse("20191010"), null) == 1);
        assertTrue(Dates.compare(Dates.parse("20191010"), Dates.parse("20191010")) == 0);
        assertTrue(Dates.compare(Dates.parse("20191010"), Dates.parse("20191001")) == 1);
        assertTrue(Dates.compare(Dates.parse("20191010"), Dates.parse("20181201")) == 1);
        assertTrue(Dates.compare(Dates.parse("20171010"), Dates.parse("20181201")) == -1);
        assertTrue(Dates.compare(Dates.parse("20171010"), Dates.parse("20180901")) == -1);
    }

    @Test
    public void testCompareMonth() {
        assertTrue(Dates.compareIgnoreDay(Dates.parse("2019-04-29"), Dates.parse("2019-04-29")) == 0);
        assertTrue(Dates.compareIgnoreDay(Dates.parse("2019-04-29"), Dates.parse("2019-04-28")) == 0);
        assertTrue(Dates.compareIgnoreDay(Dates.parse("2019-04-29"), Dates.parse("2019-03-28")) == 1);
        assertTrue(Dates.compareIgnoreDay(Dates.parse("2019-04-29"), Dates.parse("2019-05-28")) == -1);
        assertTrue(Dates.compareIgnoreDay(Dates.parse("2019-04-29"), Dates.parse("2019-06-28")) == -2);
    }

    @Test
    public void testMin() {
        assertTrue(Dates.min(null, Dates.parse("2019-01-01"), Dates.parse("2019-02-01"), Dates.parse("2019-12-01"), Dates.parse("2018-11-01")).equals(Dates.parse("2018-11-01")));
        assertTrue(Dates.min(Dates.parse("2019-01-01"), Dates.parse("2019-02-01"), Dates.parse("2019-12-01"), Dates.parse("2019-11-01")).equals(Dates.parse("2019-01-01")));
    }

    @Test
    public void testMax() {
        assertTrue(Dates.max(Dates.parse("2019-01-01"), Dates.parse("2019-02-01"), Dates.parse("2019-12-01"), Dates.parse("2019-11-01")).equals(Dates.parse("2019-12-01")));
        assertTrue(Dates.max(null, Dates.parse("2019-01-01"), Dates.parse("2019-02-01"), Dates.parse("2019-12-01"), Dates.parse("2020-01-01")).equals(Dates.parse("2020-01-01")));
    }

    @Test
    public void testIsLeapYearInt() {
        assertTrue(Dates.isLeapYear(1804));
        assertTrue(Dates.isLeapYear(1880));
        assertTrue(!Dates.isLeapYear(1881));
        assertTrue(Dates.isLeapYear(1888));
        assertTrue(Dates.isLeapYear(2000));
        assertTrue(Dates.isLeapYear(2020));
        assertTrue(!Dates.isLeapYear(2021));
    }

    @Test
    public void testIsLeapYearDate() {
//		assertTrue(Dates.isLeapYear(Dates.format("2020-01-01")));
//		assertTrue(!Dates.isLeapYear(Dates.format("2019-01-01")));
    }

    @Test
    public void testSec2Str() {
//		assertTrue(Dates.formatCN(0).equals("0 秒"));
//		assertTrue(Dates.formatCN(1).equals("1 秒"));
//		assertTrue(Dates.formatCN(40).equals("40 秒"));
//		assertTrue(Dates.formatCN(60).equals("1 分"));
//		assertTrue(Dates.formatCN(61).equals("1 分 1 秒"));
//		assertTrue(Dates.formatCN(120).equals("2 分"));
//		assertTrue(Dates.formatCN(300).equals("5 分"));
//		assertTrue(Dates.formatCN(340).equals("5 分 40 秒"));
//		assertTrue(Dates.formatCN(3600).equals("1 小时"));
//		assertTrue(Dates.formatCN(3602).equals("1 小时 2 秒"));
//		assertTrue(Dates.formatCN(3660).equals("1 小时 1 分"));
//		assertTrue(Dates.formatCN(7200).equals("2 小时"));
//		assertTrue(Dates.formatCN(7300).equals("2 小时 1 分 40 秒"));
//		assertTrue(Dates.formatCN(7330).equals("2 小时 2 分 10 秒"));
    }

    @Test
    public void testEqualsDateDate() {
//		assertTrue(DT.equals(DT.format("2020-01-01"), DT.format("2020-01-01")));
//		assertTrue(!DT.equals(DT.format("2020-01-01"), DT.format("2020-01-02")));
    }

    @Test
    public void testEqualsIgnoreDay() {
//		assertTrue(DT.equalsIgnoreDay(null, null));
//		assertTrue(DT.equalsIgnoreDay(DT.format("2017-01-01"), null) == false);
//		assertTrue(DT.equalsIgnoreDay(null, DT.format("2017-01-01")) == false);
//		assertTrue(DT.equalsIgnoreDay(DT.format("2017-01-01"), DT.format("2017-01-01")));
//		assertTrue(DT.equalsIgnoreDay(DT.format("2017-01-01"), DT.format("2017-02-01")) == false);
//		assertTrue(DT.equalsIgnoreDay(DT.format("2017-01-01"), DT.format("2016-01-01")) == false);
//		assertTrue(DT.equalsIgnoreDay(DT.format("2017-01-01"), DT.format("2016-11-01")) == false);
    }

    @Test
    public void testGetCurrentTime01() {
//		assertTrue(Dates.curDate04().equals(Dates.format(new Date(), "yyyy-MM-dd HH:mm:ss:SS")));
    }

    @Test
    public void testGetCurrentTime02() {
//		assertTrue(Dates.curDate05().equals(Dates.format(new Date(), "yyyy-MM-dd HH:mm")));
    }

    @Test
    public void testGetCurrentTime03() {
        assertTrue(Dates.currentTimeStamp().equals(Dates.format(new Date(), "yyyy-MM-dd HH:mm:ss")));
    }

    @Test
    public void testGetCurrentTime04() {
//		assertTrue(Dates.curDate07().equals(Dates.format(new Date(), "yyyyMMddHHmmss")));
    }

    @Test
    public void testTestSimpleDateFormatApply() {
        String[] ds = {"1990-12-08", "1995-12-15", "1998-04-11", "1998-04-29", "1998-09-23", "1999-02-01", "1999-02-17", "1999-03-12", "1999-05-04", "1999-05-20", "2000-03-26", "2000-07-19", "2001-03-28", "2001-07-18", "2001-07-20", "2001-09-06", "2001-09-26", "2001-11-02", "2002-01-28", "2002-06-07", "2002-06-13", "2002-06-14", "2002-06-15", "2002-06-22", "2002-06-25", "2002-06-26", "2002-07-05", "2002-07-06", "2002-07-09", "2002-07-20", "2002-08-06", "2002-09-06", "2002-09-07", "2002-09-10", "2002-09-18", "2002-09-19", "2002-09-24", "2002-09-27", "2002-10-08", "2002-10-26", "2002-10-29", "2002-10-31", "2002-11-07", "2003-01-14", "2003-01-31", "2003-05-20", "2003-05-29", "2003-06-20", "2003-07-20", "2003-07-31", "2003-08-19", "2003-08-20", "2003-09-20", "2003-10-20", "2003-11-20", "2003-12-20", "2004-01-20", "2004-02-20", "2004-03-20", "2004-04-20", "2004-05-20", "2004-06-20", "2004-07-20", "2004-08-20", "2004-09-20", "2004-10-20", "2004-11-20", "2004-12-20", "2005-01-20", "2005-02-20", "2005-03-20", "2005-04-20", "2005-05-20", "2005-06-20", "2005-07-20", "2005-08-20", "2005-09-20", "2005-10-20", "2005-11-20", "2005-12-20", "2006-01-20", "2006-02-20", "2006-03-20", "2006-04-20", "2006-05-20", "2006-06-20", "2006-07-20", "2006-08-20", "2006-09-20", "2006-10-20", "2006-11-07", "2006-11-08", "2006-11-16", "2006-11-20", "2006-11-21", "2006-11-28", "2006-12-05", "2006-12-13", "2006-12-14", "2006-12-15", "2006-12-20", "2006-12-28", "2007-01-05", "2007-01-08", "2007-01-09", "2007-01-12", "2007-01-15", "2007-01-20", "2007-01-31", "2007-02-01", "2007-02-20", "2007-03-01", "2007-03-02", "2007-03-05", "2007-03-08", "2007-03-13", "2007-03-14", "2007-03-20", "2007-04-03", "2007-04-04", "2007-04-05", "2007-04-10", "2007-04-13", "2007-04-14", "2007-04-20", "2007-04-26", "2007-04-27", "2007-05-09", "2007-05-20", "2007-05-23", "2007-06-04", "2007-06-07", "2007-06-11", "2007-06-18", "2007-06-20", "2007-07-16", "2007-07-20", "2007-07-25", "2007-08-06", "2007-08-14", "2007-08-20", "2007-08-23", "2007-09-10", "2007-09-19", "2007-09-20", "2007-10-20", "2007-11-20", "2007-11-28", "2007-11-29", "2007-12-06", "2007-12-08", "2007-12-09", "2007-12-11", "2007-12-12", "2007-12-13", "2007-12-14", "2007-12-15", "2007-12-16", "2007-12-18", "2007-12-19", "2007-12-20", "2007-12-21", "2007-12-22", "2007-12-23", "2007-12-24", "2007-12-26", "2007-12-27", "2007-12-28", "2008-01-04", "2008-01-05", "2008-01-07", "2008-01-08", "2008-01-09", "2008-01-10", "2008-01-11", "2008-01-12", "2008-01-13", "2008-01-14", "2008-01-15", "2008-01-17", "2008-01-18", "2008-01-19", "2008-01-20", "2008-01-21", "2008-01-22", "2008-01-23", "2008-01-24", "2008-01-25", "2008-01-26", "2008-01-27", "2008-01-28", "2008-01-29", "2008-01-30", "2008-02-01", "2008-02-04", "2008-02-05", "2008-02-06", "2008-02-07", "2008-02-11", "2008-02-12", "2008-02-13", "2008-02-20", "2008-02-25", "2008-02-26", "2008-02-29", "2008-03-01", "2008-03-03", "2008-03-08", "2008-03-18", "2008-03-19", "2008-03-20", "2008-03-21", "2008-03-22", "2008-03-23", "2008-03-26", "2008-03-27", "2008-03-28", "2008-03-29", "2008-04-01", "2008-04-03", "2008-04-04", "2008-04-05", "2008-04-06", "2008-04-07", "2008-04-08", "2008-04-09", "2008-04-10", "2008-04-11", "2008-04-12", "2008-04-13", "2008-04-14", "2008-04-15", "2008-04-16", "2008-04-17", "2008-04-18", "2008-04-19", "2008-04-20", "2008-04-21", "2008-04-22", "2008-04-23", "2008-04-24", "2008-04-25", "2008-04-26", "2008-04-27", "2008-04-28", "2008-04-30", "2008-05-01", "2008-05-09", "2008-05-17", "2008-05-20", "2008-06-01", "2008-06-04", "2008-06-06", "2008-06-10", "2008-06-11", "2008-06-13", "2008-06-20", "2008-06-23", "2008-07-02", "2008-07-03", "2008-07-04", "2008-07-05", "2008-07-11", "2008-07-12", "2008-07-14", "2008-07-16", "2008-07-18", "2008-07-19", "2008-07-20", "2008-07-22", "2008-07-23", "2008-07-24", "2008-07-26", "2008-07-30", "2008-08-01", "2008-08-02", "2008-08-06", "2008-08-09", "2008-08-20", "2008-08-21", "2008-08-30", "2008-09-20", "2008-09-29", "2008-09-30", "2008-10-09", "2008-10-11", "2008-10-17", "2008-10-20", "2008-10-21", "2008-10-25", "2008-11-01", "2008-11-05", "2008-11-06", "2008-11-07", "2008-11-08", "2008-11-10", "2008-11-12", "2008-11-14", "2008-11-15", "2008-11-16", "2008-11-17", "2008-11-18", "2008-11-19", "2008-11-20", "2008-11-24", "2008-11-25", "2008-11-26", "2008-11-27", "2008-11-28", "2008-11-29", "2008-12-01", "2008-12-05", "2008-12-06", "2008-12-10", "2008-12-12", "2008-12-15", "2008-12-20", "2008-12-28", "2008-12-30", "2009-01-02", "2009-01-03", "2009-01-04", "2009-01-05", "2009-01-06", "2009-01-07", "2009-01-08", "2009-01-09", "2009-01-10", "2009-01-11", "2009-01-12", "2009-01-13", "2009-01-14", "2009-01-15", "2009-01-16", "2009-01-17", "2009-01-19", "2009-01-20", "2009-01-21", "2009-01-22", "2009-01-23", "2009-01-24", "2009-01-25", "2009-01-26", "2009-01-27", "2009-01-28", "2009-01-30", "2009-01-31", "2009-02-01", "2009-02-06", "2009-02-11", "2009-02-12", "2009-02-13", "2009-02-20", "2009-02-27", "2009-02-28", "2009-03-01", "2009-03-02", "2009-03-03", "2009-03-05", "2009-03-06", "2009-03-07", "2009-03-08", "2009-03-10", "2009-03-11", "2009-03-12", "2009-03-13", "2009-03-15", "2009-03-16", "2009-03-18", "2009-03-19", "2009-03-20", "2009-03-21", "2009-03-22", "2009-03-24", "2009-03-25", "2009-03-26", "2009-03-27", "2009-03-28", "2009-03-31", "2009-04-01", "2009-04-02", "2009-04-03", "2009-04-04", "2009-04-05", "2009-04-06", "2009-04-07", "2009-04-08", "2009-04-10", "2009-04-11", "2009-04-12", "2009-04-13", "2009-04-14", "2009-04-15", "2009-04-16", "2009-04-18", "2009-04-19", "2009-04-20", "2009-04-22", "2009-04-23", "2009-04-24", "2009-04-25", "2009-04-26", "2009-04-28", "2009-04-29", "2009-05-04", "2009-05-05", "2009-05-06", "2009-05-07", "2009-05-08", "2009-05-12", "2009-05-13", "2009-05-20", "2009-05-21", "2009-05-22", "2009-05-27", "2009-05-28", "2009-06-01", "2009-06-04", "2009-06-15", "2009-06-17", "2009-06-20", "2009-07-01", "2009-07-02", "2009-07-04", "2009-07-14", "2009-07-15", "2009-07-16", "2009-07-20", "2009-07-24", "2009-07-25", "2009-07-28", "2009-07-30", "2009-07-31", "2009-08-06", "2009-08-07", "2009-08-11", "2009-08-12", "2009-08-13", "2009-08-20", "2009-08-21", "2009-08-23", "2009-08-30", "2009-09-01", "2009-09-02", "2009-09-09", "2009-09-17", "2009-09-19", "2009-09-20", "2009-09-24", "2009-09-25", "2009-09-28", "2009-09-29", "2009-09-30", "2009-10-09", "2009-10-12", "2009-10-13", "2009-10-14", "2009-10-18", "2009-10-20", "2009-10-21", "2009-10-22", "2009-10-23", "2009-10-26", "2009-11-01", "2009-11-05", "2009-11-07", "2009-11-08", "2009-11-10", "2009-11-14", "2009-11-15", "2009-11-16", "2009-11-17", "2009-11-18", "2009-11-19", "2009-11-20", "2009-11-23", "2009-11-24", "2009-11-25", "2009-11-28", "2009-11-29", "2009-12-01", "2009-12-02", "2009-12-04", "2009-12-06", "2009-12-10", "2009-12-11", "2009-12-15", "2009-12-20", "2009-12-26", "2009-12-30", "2010-01-01", "2010-01-02", "2010-01-03", "2010-01-04", "2010-01-05", "2010-01-06", "2010-01-07", "2010-01-08", "2010-01-09", "2010-01-10", "2010-01-11", "2010-01-12", "2010-01-13", "2010-01-14", "2010-01-15", "2010-01-16", "2010-01-17", "2010-01-18", "2010-01-19", "2010-01-20", "2010-01-21", "2010-01-22", "2010-01-23", "2010-01-24", "2010-01-25", "2010-01-26", "2010-01-27", "2010-01-28", "2010-01-29", "2010-01-30", "2010-01-31", "2010-02-01", "2010-02-03", "2010-02-04", "2010-02-09", "2010-02-10", "2010-02-11", "2010-02-12", "2010-02-13", "2010-02-14", "2010-02-15", "2010-02-16", "2010-02-17", "2010-02-18", "2010-02-19", "2010-02-20", "2010-02-21", "2010-02-22", "2010-02-23", "2010-02-24", "2010-02-25", "2010-02-26", "2010-02-27", "2010-02-28", "2010-03-01", "2010-03-02", "2010-03-03", "2010-03-05", "2010-03-07", "2010-03-08", "2010-03-09", "2010-03-10", "2010-03-11", "2010-03-12", "2010-03-13", "2010-03-14", "2010-03-15", "2010-03-16", "2010-03-18", "2010-03-19", "2010-03-20", "2010-03-21", "2010-03-22", "2010-03-23", "2010-03-24", "2010-03-25", "2010-03-26", "2010-03-27", "2010-03-28", "2010-03-29", "2010-03-30", "2010-04-01", "2010-04-02", "2010-04-03", "2010-04-04", "2010-04-05", "2010-04-07", "2010-04-08", "2010-04-09", "2010-04-10", "2010-04-11", "2010-04-12", "2010-04-13", "2010-04-14", "2010-04-15", "2010-04-16", "2010-04-17", "2010-04-18", "2010-04-19", "2010-04-20", "2010-04-21", "2010-04-22", "2010-04-23", "2010-04-24", "2010-04-25", "2010-04-26", "2010-04-27", "2010-04-28", "2010-04-30", "2010-05-07", "2010-05-09", "2010-05-10", "2010-05-11", "2010-05-12", "2010-05-13", "2010-05-14", "2010-05-16", "2010-05-18", "2010-05-20", "2010-05-30", "2010-06-01", "2010-06-11", "2010-06-12", "2010-06-17", "2010-06-20", "2010-06-22", "2010-06-28", "2010-06-30", "2010-07-01", "2010-07-04", "2010-07-07", "2010-07-08", "2010-07-09", "2010-07-14", "2010-07-15", "2010-07-19", "2010-07-20", "2010-07-21", "2010-07-23", "2010-07-28", "2010-07-29", "2010-08-01", "2010-08-02", "2010-08-06", "2010-08-09", "2010-08-10", "2010-08-13", "2010-08-16", "2010-08-17", "2010-08-18", "2010-08-19", "2010-08-20", "2010-08-21", "2010-08-25", "2010-08-26", "2010-08-27", "2010-08-28", "2010-08-29", "2010-08-30", "2010-08-31", "2010-09-06", "2010-09-08", "2010-09-10", "2010-09-15", "2010-09-17", "2010-09-20", "2010-09-23", "2010-09-24", "2010-09-26", "2010-10-09", "2010-10-11", "2010-10-15", "2010-10-17", "2010-10-18", "2010-10-19", "2010-10-20", "2010-10-21", "2010-10-22", "2010-10-23", "2010-10-26", "2010-10-27", "2010-10-29", "2010-10-30", "2010-11-01", "2010-11-02", "2010-11-03", "2010-11-04", "2010-11-05", "2010-11-06", "2010-11-07", "2010-11-08", "2010-11-09", "2010-11-11", "2010-11-13", "2010-11-14", "2010-11-15", "2010-11-16", "2010-11-17", "2010-11-19", "2010-11-20", "2010-11-24", "2010-11-25", "2010-11-27", "2010-11-28", "2010-11-29", "2010-11-30", "2010-12-01", "2010-12-02", "2010-12-04", "2010-12-06", "2010-12-09", "2010-12-12", "2010-12-15", "2010-12-20", "2010-12-23", "2010-12-24", "2010-12-25", "2010-12-27", "2011-01-01", "2011-01-02", "2011-01-03", "2011-01-04", "2011-01-05", "2011-01-06", "2011-01-07", "2011-01-08", "2011-01-09", "2011-01-10", "2011-01-11", "2011-01-12", "2011-01-13", "2011-01-14", "2011-01-15", "2011-01-16", "2011-01-17", "2011-01-18", "2011-01-19", "2011-01-20", "2011-01-21", "2011-01-22", "2011-01-23", "2011-01-24", "2011-01-25", "2011-01-26", "2011-01-27", "2011-01-28", "2011-01-29", "2011-01-30", "2011-02-01", "2011-02-02", "2011-02-03", "2011-02-04", "2011-02-05", "2011-02-06", "2011-02-07", "2011-02-08", "2011-02-18", "2011-02-19", "2011-02-20", "2011-02-23", "2011-02-24", "2011-02-28", "2011-03-01", "2011-03-02", "2011-03-03", "2011-03-04", "2011-03-05", "2011-03-06", "2011-03-07", "2011-03-08", "2011-03-09", "2011-03-10", "2011-03-11", "2011-03-12", "2011-03-14", "2011-03-15", "2011-03-16", "2011-03-17", "2011-03-18", "2011-03-20", "2011-03-21", "2011-03-22", "2011-03-24", "2011-03-25", "2011-03-26", "2011-03-27", "2011-03-28", "2011-03-29", "2011-03-31", "2011-04-01", "2011-04-02", "2011-04-04", "2011-04-05", "2011-04-06", "2011-04-07", "2011-04-08", "2011-04-09", "2011-04-10", "2011-04-11", "2011-04-12", "2011-04-13", "2011-04-14", "2011-04-15", "2011-04-16", "2011-04-17", "2011-04-18", "2011-04-20", "2011-04-21", "2011-04-22", "2011-04-23", "2011-04-24", "2011-04-25", "2011-04-26", "2011-04-27", "2011-04-28", "2011-04-29", "2011-04-30", "2011-05-01", "2011-05-05", "2011-05-06", "2011-05-09", "2011-05-10", "2011-05-11", "2011-05-12", "2011-05-15", "2011-05-16", "2011-05-18", "2011-05-20", "2011-05-22", "2011-05-23", "2011-05-25", "2011-05-26", "2011-05-28", "2011-05-30", "2011-05-31", "2011-06-01", "2011-06-02", "2011-06-03", "2011-06-06", "2011-06-07", "2011-06-10", "2011-06-11", "2011-06-12", "2011-06-15", "2011-06-16", "2011-06-18", "2011-06-20", "2011-06-24", "2011-06-27", "2011-06-28", "2011-06-29", "2011-06-30", "2011-07-01", "2011-07-04", "2011-07-15", "2011-07-20", "2011-07-28", "2011-08-01", "2011-08-04", "2011-08-08", "2011-08-15", "2011-08-16", "2011-08-19", "2011-08-20", "2011-08-25", "2011-08-27", "2011-08-29", "2011-09-01", "2011-09-05", "2011-09-08", "2011-09-09", "2011-09-12", "2011-09-15", "2011-09-17", "2011-09-18", "2011-09-20", "2011-09-24", "2011-09-28", "2011-10-01", "2011-10-03", "2011-10-04", "2011-10-05", "2011-10-08", "2011-10-09", "2011-10-10", "2011-10-12", "2011-10-15", "2011-10-20", "2011-10-22", "2011-10-23", "2011-10-24", "2011-10-25", "2011-10-26", "2011-10-29", "2011-10-30", "2011-11-01", "2011-11-02", "2011-11-03", "2011-11-04", "2011-11-05", "2011-11-09", "2011-11-10", "2011-11-11", "2011-11-12", "2011-11-13", "2011-11-15", "2011-11-16", "2011-11-18", "2011-11-19", "2011-11-20", "2011-11-21", "2011-11-25", "2011-11-26", "2011-11-27", "2011-11-28", "2011-11-29", "2011-11-30", "2011-12-01", "2011-12-02", "2011-12-05", "2011-12-10", "2011-12-12", "2011-12-15", "2011-12-20", "2011-12-23", "2011-12-24", "2011-12-25", "2011-12-30", "2012-01-01", "2012-01-03", "2012-01-04", "2012-01-05", "2012-01-06", "2012-01-07", "2012-01-08", "2012-01-09", "2012-01-10", "2012-01-11", "2012-01-12", "2012-01-13", "2012-01-14", "2012-01-15", "2012-01-16", "2012-01-17", "2012-01-18", "2012-01-19", "2012-01-20", "2012-01-21", "2012-01-23", "2012-01-24", "2012-01-25", "2012-01-26", "2012-01-27", "2012-01-28", "2012-01-31", "2012-02-01", "2012-02-02", "2012-02-04", "2012-02-05", "2012-02-06", "2012-02-07", "2012-02-08", "2012-02-09", "2012-02-10", "2012-02-12", "2012-02-13", "2012-02-14", "2012-02-15", "2012-02-16", "2012-02-17", "2012-02-18", "2012-02-19", "2012-02-20", "2012-02-21", "2012-02-22", "2012-02-23", "2012-02-24", "2012-02-25", "2012-02-26", "2012-02-27", "2012-02-28", "2012-02-29", "2012-03-01", "2012-03-02", "2012-03-03", "2012-03-04", "2012-03-05", "2012-03-07", "2012-03-08", "2012-03-09", "2012-03-10", "2012-03-11", "2012-03-12", "2012-03-13", "2012-03-14", "2012-03-15", "2012-03-16", "2012-03-17", "2012-03-18", "2012-03-19", "2012-03-20", "2012-03-21", "2012-03-22", "2012-03-23", "2012-03-24", "2012-03-25", "2012-03-26", "2012-03-27", "2012-03-28", "2012-03-29", "2012-03-30", "2012-03-31", "2012-04-01", "2012-04-02", "2012-04-03", "2012-04-05", "2012-04-06", "2012-04-07", "2012-04-08", "2012-04-09", "2012-04-10", "2012-04-11", "2012-04-12", "2012-04-13", "2012-04-14", "2012-04-15", "2012-04-16", "2012-04-17", "2012-04-18", "2012-04-19", "2012-04-20", "2012-04-22", "2012-04-23", "2012-04-24", "2012-04-25", "2012-04-26", "2012-04-27", "2012-04-28", "2012-04-29", "2012-04-30", "2012-05-01", "2012-05-04", "2012-05-05", "2012-05-06", "2012-05-07", "2012-05-08", "2012-05-09", "2012-05-10", "2012-05-11", "2012-05-12", "2012-05-13", "2012-05-14", "2012-05-15", "2012-05-16", "2012-05-17", "2012-05-18", "2012-05-19", "2012-05-20", "2012-05-24", "2012-05-25", "2012-05-26", "2012-05-29", "2012-05-30", "2012-06-01", "2012-06-02", "2012-06-03", "2012-06-07", "2012-06-10", "2012-06-12", "2012-06-14", "2012-06-15", "2012-06-16", "2012-06-17", "2012-06-20", "2012-06-22", "2012-06-23", "2012-06-24", "2012-06-25", "2012-06-28", "2012-06-29", "2012-07-01", "2012-07-02", "2012-07-04", "2012-07-06", "2012-07-07", "2012-07-10", "2012-07-11", "2012-07-13", "2012-07-14", "2012-07-15", "2012-07-16", "2012-07-20", "2012-07-25", "2012-07-28", "2012-08-01", "2012-08-04", "2012-08-08", "2012-08-10", "2012-08-15", "2012-08-16", "2012-08-20", "2012-08-21", "2012-08-22", "2012-08-23", "2012-08-24", "2012-08-25", "2012-08-28", "2012-08-31", "2012-09-01", "2012-09-04", "2012-09-08", "2012-09-09", "2012-09-10", "2012-09-12", "2012-09-13", "2012-09-15", "2012-09-17", "2012-09-19", "2012-09-20", "2012-09-22", "2012-09-24", "2012-09-27", "2012-10-01", "2012-10-10", "2012-10-11", "2012-10-13", "2012-10-15", "2012-10-18", "2012-10-19", "2012-10-20", "2012-10-21", "2012-10-22", "2012-10-23", "2012-10-24", "2012-10-25", "2012-10-26", "2012-10-28", "2012-10-29", "2012-10-30", "2012-11-02", "2012-11-04", "2012-11-05", "2012-11-07", "2012-11-08", "2012-11-09", "2012-11-10", "2012-11-11", "2012-11-13", "2012-11-14", "2012-11-15", "2012-11-16", "2012-11-17", "2012-11-18", "2012-11-19", "2012-11-20", "2012-11-21", "2012-11-22", "2012-11-24", "2012-11-25", "2012-11-26", "2012-11-28", "2012-11-29", "2012-11-30", "2012-12-01", "2012-12-02", "2012-12-03", "2012-12-04", "2012-12-05", "2012-12-06", "2012-12-10", "2012-12-11", "2012-12-12", "2012-12-15", "2012-12-16", "2012-12-17", "2012-12-18", "2012-12-20", "2012-12-21", "2012-12-22", "2012-12-24", "2012-12-25", "2013-01-01", "2013-01-04", "2013-01-05", "2013-01-06", "2013-01-07", "2013-01-08", "2013-01-09", "2013-01-10", "2013-01-11", "2013-01-12", "2013-01-13", "2013-01-14", "2013-01-15", "2013-01-16", "2013-01-17", "2013-01-18", "2013-01-19", "2013-01-20", "2013-01-21", "2013-01-22", "2013-01-23", "2013-01-24", "2013-01-25", "2013-01-26", "2013-01-27", "2013-01-28", "2013-01-29", "2013-01-30", "2013-01-31", "2013-02-01", "2013-02-03", "2013-02-04", "2013-02-05", "2013-02-06", "2013-02-07", "2013-02-08", "2013-02-09", "2013-02-10", "2013-02-11", "2013-02-13", "2013-02-15", "2013-02-16", "2013-02-17", "2013-02-18", "2013-02-20", "2013-02-21", "2013-02-22", "2013-02-23", "2013-02-25", "2013-02-26", "2013-02-27", "2013-02-28", "2013-03-01", "2013-03-04", "2013-03-05", "2013-03-06", "2013-03-07", "2013-03-08", "2013-03-09", "2013-03-10", "2013-03-11", "2013-03-12", "2013-03-13", "2013-03-14", "2013-03-15", "2013-03-16", "2013-03-17", "2013-03-18", "2013-03-19", "2013-03-20", "2013-03-21", "2013-03-22", "2013-03-23", "2013-03-24", "2013-03-25", "2013-03-26", "2013-03-27", "2013-03-28", "2013-03-29", "2013-03-30", "2013-03-31", "2013-04-01", "2013-04-02", "2013-04-03", "2013-04-04", "2013-04-05", "2013-04-06", "2013-04-07", "2013-04-08", "2013-04-09", "2013-04-10", "2013-04-11", "2013-04-12", "2013-04-13", "2013-04-14", "2013-04-15", "2013-04-16", "2013-04-17", "2013-04-18", "2013-04-19", "2013-04-20", "2013-04-21", "2013-04-22", "2013-04-23", "2013-04-24", "2013-04-25", "2013-04-26", "2013-04-27", "2013-04-28", "2013-04-29", "2013-04-30", "2013-05-01", "2013-05-02", "2013-05-04", "2013-05-05", "2013-05-06", "2013-05-07", "2013-05-08", "2013-05-09", "2013-05-10", "2013-05-11", "2013-05-13", "2013-05-14", "2013-05-15", "2013-05-16", "2013-05-20", "2013-05-21", "2013-05-22", "2013-05-24", "2013-05-25", "2013-05-27", "2013-05-28", "2013-05-29", "2013-05-30", "2013-05-31", "2013-06-01", "2013-06-02", "2013-06-03", "2013-06-04", "2013-06-05", "2013-06-06", "2013-06-07", "2013-06-09", "2013-06-10", "2013-06-11", "2013-06-12", "2013-06-15", "2013-06-19", "2013-06-20", "2013-06-22", "2013-06-24", "2013-06-25", "2013-06-26", "2013-06-27", "2013-06-28", "2013-07-01", "2013-07-02", "2013-07-03", "2013-07-04", "2013-07-08", "2013-07-09", "2013-07-10", "2013-07-13", "2013-07-15", "2013-07-16", "2013-07-20", "2013-07-23", "2013-07-24", "2013-07-26", "2013-07-27", "2013-07-28", "2013-08-06", "2013-08-07", "2013-08-09", "2013-08-10", "2013-08-19", "2013-08-20", "2013-08-21", "2013-08-23", "2013-08-26", "2013-08-28", "2013-08-29", "2013-08-31", "2013-09-01", "2013-09-03", "2013-09-04", "2013-09-08", "2013-09-09", "2013-09-10", "2013-09-12", "2013-09-13", "2013-09-16", "2013-09-19", "2013-09-20", "2013-09-21", "2013-09-22", "2013-09-23", "2013-09-26", "2013-09-27", "2013-10-08", "2013-10-09", "2013-10-10", "2013-10-15", "2013-10-17", "2013-10-18", "2013-10-19", "2013-10-20", "2013-10-21", "2013-10-22", "2013-10-23", "2013-10-24", "2013-10-25", "2013-10-26", "2013-10-28", "2013-10-29", "2013-10-30", "2013-11-01", "2013-11-02", "2013-11-03", "2013-11-04", "2013-11-05", "2013-11-06", "2013-11-07", "2013-11-08", "2013-11-10", "2013-11-11", "2013-11-12", "2013-11-13", "2013-11-14", "2013-11-15", "2013-11-17", "2013-11-18", "2013-11-19", "2013-11-20", "2013-11-21", "2013-11-22", "2013-11-25", "2013-11-27", "2013-11-28", "2013-11-29", "2013-11-30", "2013-12-01", "2013-12-02", "2013-12-03", "2013-12-04", "2013-12-05", "2013-12-06", "2013-12-08", "2013-12-10", "2013-12-13", "2013-12-15", "2013-12-16", "2013-12-20", "2013-12-25", "2014-01-01", "2014-01-02", "2014-01-03", "2014-01-06", "2014-01-07", "2014-01-08", "2014-01-09", "2014-01-10", "2014-01-13", "2014-01-14", "2014-01-15", "2014-01-16", "2014-01-17", "2014-01-20", "2014-01-21", "2014-01-22", "2014-01-23", "2014-01-24", "2014-01-26", "2014-01-27", "2014-01-28", "2014-01-29", "2014-01-30", "2014-02-01", "2014-02-05", "2014-02-07", "2014-02-10", "2014-02-12", "2014-02-13", "2014-02-14", "2014-02-16", "2014-02-17", "2014-02-18", "2014-02-20", "2014-02-21", "2014-02-24", "2014-02-25", "2014-02-27", "2014-02-28", "2014-03-01", "2014-03-03", "2014-03-04", "2014-03-05", "2014-03-06", "2014-03-07", "2014-03-10", "2014-03-11", "2014-03-12", "2014-03-13", "2014-03-14", "2014-03-15", "2014-03-17", "2014-03-18", "2014-03-19", "2014-03-20", "2014-03-21", "2014-03-22", "2014-03-23", "2014-03-24", "2014-03-25", "2014-03-26", "2014-03-27", "2014-03-28", "2014-03-29", "2014-03-30", "2014-03-31", "2014-04-01", "2014-04-02", "2014-04-04", "2014-04-05", "2014-04-07", "2014-04-08", "2014-04-09", "2014-04-10", "2014-04-11", "2014-04-14", "2014-04-15", "2014-04-16", "2014-04-17", "2014-04-18", "2014-04-19", "2014-04-20", "2014-04-21", "2014-04-22", "2014-04-23", "2014-04-24", "2014-04-25", "2014-04-26", "2014-04-27", "2014-04-28", "2014-04-29", "2014-04-30", "2014-05-02", "2014-05-04", "2014-05-05", "2014-05-06", "2014-05-07", "2014-05-08", "2014-05-09", "2014-05-10", "2014-05-12", "2014-05-13", "2014-05-14", "2014-05-15", "2014-05-16", "2014-05-19", "2014-05-20", "2014-05-21", "2014-05-22", "2014-05-23", "2014-05-25", "2014-05-26", "2014-05-27", "2014-05-28", "2014-05-30", "2014-05-31", "2014-06-02", "2014-06-03", "2014-06-04", "2014-06-05", "2014-06-06", "2014-06-09", "2014-06-10", "2014-06-11", "2014-06-12", "2014-06-13", "2014-06-14", "2014-06-16", "2014-06-17", "2014-06-18", "2014-06-19", "2014-06-20", "2014-06-21", "2014-06-23", "2014-06-24", "2014-06-25", "2014-06-26", "2014-06-27", "2014-06-30", "2014-07-01", "2014-07-02", "2014-07-04", "2014-07-07", "2014-07-10", "2014-07-11", "2014-07-13", "2014-07-14", "2014-07-15", "2014-07-16", "2014-07-20", "2014-07-21", "2014-07-22", "2014-07-23", "2014-07-24", "2014-07-26", "2014-07-27", "2014-07-28", "2014-07-29", "2014-07-30", "2014-07-31", "2014-08-01", "2014-08-04", "2014-08-05", "2014-08-07", "2014-08-08", "2014-08-09", "2014-08-10", "2014-08-11", "2014-08-12", "2014-08-13", "2014-08-14", "2014-08-15", "2014-08-18", "2014-08-19", "2014-08-20", "2014-08-21", "2014-08-22", "2014-08-25", "2014-08-27", "2014-08-28", "2014-08-29", "2014-09-01", "2014-09-02", "2014-09-03", "2014-09-04", "2014-09-05", "2014-09-07", "2014-09-08", "2014-09-09", "2014-09-10", "2014-09-11", "2014-09-12", "2014-09-13", "2014-09-14", "2014-09-15", "2014-09-16", "2014-09-17", "2014-09-19", "2014-09-20", "2014-09-21", "2014-09-22", "2014-09-23", "2014-09-25", "2014-09-26", "2014-09-27", "2014-09-28", "2014-09-29", "2014-10-01", "2014-10-03", "2014-10-04", "2014-10-05", "2014-10-07", "2014-10-08", "2014-10-09", "2014-10-10", "2014-10-11", "2014-10-12", "2014-10-13", "2014-10-14", "2014-10-15", "2014-10-16", "2014-10-17", "2014-10-18", "2014-10-19", "2014-10-20", "2014-10-21", "2014-10-22", "2014-10-23", "2014-10-24", "2014-10-25", "2014-10-26", "2014-10-27", "2014-10-28", "2014-10-29", "2014-10-30", "2014-10-31", "2014-11-01", "2014-11-02", "2014-11-03", "2014-11-04", "2014-11-05", "2014-11-06", "2014-11-07", "2014-11-08", "2014-11-09", "2014-11-10", "2014-11-11", "2014-11-12", "2014-11-13", "2014-11-14", "2014-11-15", "2014-11-16", "2014-11-17", "2014-11-18", "2014-11-19", "2014-11-20", "2014-11-21", "2014-11-22", "2014-11-23", "2014-11-24", "2014-11-25", "2014-11-26", "2014-11-27", "2014-11-28", "2014-11-29", "2014-11-30", "2014-12-01", "2014-12-02", "2014-12-03", "2014-12-04", "2014-12-05", "2014-12-06", "2014-12-07", "2014-12-08", "2014-12-09", "2014-12-10", "2014-12-11", "2014-12-12", "2014-12-13", "2014-12-14", "2014-12-15", "2014-12-16", "2014-12-17", "2014-12-18", "2014-12-19", "2014-12-20", "2014-12-21", "2014-12-22", "2014-12-23", "2014-12-24", "2014-12-25", "2014-12-26", "2014-12-27", "2014-12-28", "2014-12-29", "2014-12-30", "2014-12-31", "2015-01-01", "2015-01-02", "2015-01-03", "2015-01-04", "2015-01-05", "2015-01-06", "2015-01-07", "2015-01-08", "2015-01-09", "2015-01-10", "2015-01-11", "2015-01-12", "2015-01-13", "2015-01-14", "2015-01-15", "2015-01-16", "2015-01-17", "2015-01-18", "2015-01-19", "2015-01-20", "2015-01-21", "2015-01-22", "2015-01-23", "2015-01-24", "2015-01-25", "2015-01-26", "2015-01-27", "2015-01-28", "2015-01-29", "2015-01-30", "2015-01-31", "2015-02-01", "2015-02-02", "2015-02-03", "2015-02-04", "2015-02-05", "2015-02-06", "2015-02-07", "2015-02-08", "2015-02-09", "2015-02-10", "2015-02-11", "2015-02-12", "2015-02-13", "2015-02-14", "2015-02-15", "2015-02-16", "2015-02-17", "2015-02-18", "2015-02-19", "2015-02-20", "2015-02-21", "2015-02-22", "2015-02-23", "2015-02-24", "2015-02-25", "2015-02-26", "2015-02-27", "2015-02-28", "2015-03-01", "2015-03-02", "2015-03-03", "2015-03-04", "2015-03-05", "2015-03-06", "2015-03-07", "2015-03-08", "2015-03-09", "2015-03-10", "2015-03-11", "2015-03-12", "2015-03-13", "2015-03-14", "2015-03-15", "2015-03-16", "2015-03-17", "2015-03-18", "2015-03-19", "2015-03-20", "2015-03-21", "2015-03-22", "2015-03-23", "2015-03-24", "2015-03-25", "2015-03-26", "2015-03-27", "2015-03-28", "2015-03-29", "2015-03-30", "2015-03-31", "2015-04-01", "2015-04-02", "2015-04-03", "2015-04-04", "2015-04-05", "2015-04-06", "2015-04-07", "2015-04-08", "2015-04-09", "2015-04-10", "2015-04-11", "2015-04-12", "2015-04-13", "2015-04-14", "2015-04-15", "2015-04-16", "2015-04-17", "2015-04-18", "2015-04-19", "2015-04-20", "2015-04-21", "2015-04-22", "2015-04-23", "2015-04-24", "2015-04-25", "2015-04-26", "2015-04-27", "2015-04-28", "2015-04-29", "2015-04-30"};

        for (String d : ds) {
            if (Dates.testParse(d) != null) {
                assertTrue(Dates.match(d, "yyyy-MM-dd"));
            } else {
                assertTrue(!Dates.match(d, "yyyy-MM-dd"));
            }
        }
    }

    @Test
    public void testToStringDate() {
        Date date = Dates.parse("2018-05-17 15:58:59");
        assertTrue(StringUtils.toString(date).equals("2018-05-17 15:58:59"));

        date = Dates.parse("2018-05-17 15:58:59:600");
        assertTrue(StringUtils.toString(date).equals("2018-05-17 15:58:59:600"));

        date = Dates.parse("2018-05-17 15:58");
        assertTrue(Dates.format19(date).equals("2018-05-17 15:58:00"));
    }

    @Test
    public void testformat10() {
        assertTrue(Dates.format17(null) == null);
        assertTrue(Dates.format17(Dates.parse("2019-11-21 01:23:45:6")).equals("20191121012345006"));
        assertTrue(Dates.format17(Dates.parse("2019-11-21 01:23:45:66")).equals("20191121012345066"));
        assertTrue(Dates.format17(Dates.parse("2019-11-21 01:23:45:669")).equals("20191121012345669"));
    }

    @Test
    public void testtestFormatObject() {
        assertTrue(Dates.testParse(null) == null);
        assertTrue(Dates.testParse("") == null);
        assertTrue(Dates.testParse("tst") == null);
        assertTrue(Dates.testParse("2019-11-21 01:23:45:669") != null);
    }

    @Test
    public void testIsTimeCharArray() {
        assertTrue(Dates.isTime("123456".toCharArray()));

        assertTrue(!Dates.isTime("123460".toCharArray()));
        assertTrue(!Dates.isTime("A23456".toCharArray()));
        assertTrue(!Dates.isTime("1A3456".toCharArray()));
        assertTrue(!Dates.isTime("12a456".toCharArray()));
        assertTrue(!Dates.isTime("127456".toCharArray()));
        assertTrue(!Dates.isTime("126056".toCharArray()));

        assertTrue(!Dates.isTime("243456".toCharArray()));
        assertTrue(!Dates.isTime("253456".toCharArray()));
        assertTrue(!Dates.isTime("2A3456".toCharArray()));

        assertTrue(!Dates.isTime("123460".toCharArray()));
        assertTrue(!Dates.isTime("1234-1".toCharArray()));
    }

    @Test
    public void testisStandTimeFormatCharArray() {
        assertTrue(Dates.isTime("12:34:56"));
        assertTrue(Dates.isTime("1:2:3"));
        assertTrue(Dates.isTime("1:20:3"));
        assertTrue(Dates.isTime("1:2:34"));
        assertTrue(Dates.isTime("12:34:56:789"));
        assertTrue(Dates.isTime("12:34:56:7"));
        assertTrue(Dates.isTime("12:34:56:78"));
    }

    @Test
    public void test100() {
        Assert.assertEquals(31, Dates.calcDay("2021-01-01", "2021-02-01"));
    }

    @Test
    public void test101() {
        List<String> list = Dates.getEndOfMonth("2021-01-01", "2022-02-01");
        for (String s : list) {
            System.out.println(s);
        }
        Assert.assertEquals(13, list.size());
        Assert.assertEquals("2021-01-31", list.get(0));
        Assert.assertEquals("2022-01-31", list.get(list.size() - 1));
    }

    @Test
    public void test102() {
        List<String> list = Dates.tolist("2021-01-01", "2021-02-01");
        for (String s : list) {
            System.out.println(s);
        }
        Assert.assertEquals(32, list.size());
        Assert.assertEquals("2021-01-01", list.get(0));
        Assert.assertEquals("2021-02-01", list.get(list.size() - 1));
    }

    @Test
    public void test103() {
        Assert.assertEquals("yyyyMMdd", Dates.pattern("19981225"));
        Assert.assertEquals("yyyy-MM-dd", Dates.pattern("1998-12-25"));
        Assert.assertEquals("MM/dd/yyyy", Dates.pattern("12/25/1998"));
        Assert.assertEquals("yyyyMMddHH", Dates.pattern("1998122500"));
        Assert.assertEquals("yyyyMMddHHmm", Dates.pattern("199812250000"));
        Assert.assertEquals("yyyyMMddHHmmss", Dates.pattern("19981225000000"));
        Assert.assertEquals("yyyyMMddHHmmssSSS", Dates.pattern("19981225000000000"));
        Assert.assertEquals("yyyy-MM-dd HH", Dates.pattern("1998-12-25 12"));
        Assert.assertEquals("yyyy-MM-dd HH:mm", Dates.pattern("1998-12-25 12:12"));
        Assert.assertEquals("yyyy-MM-dd HH:mm:ss", Dates.pattern("1998-12-25 12:12:12"));
        Assert.assertEquals("yyyy-MM-dd HH:mm:ss:SS", Dates.pattern("1998-12-25 12:12:12:99"));
        Assert.assertEquals("yyyy年MM月dd日", Dates.pattern("1998年12月25日"));
        Assert.assertEquals("yyyy年M月dd日", Dates.pattern("1998年1月25日"));
        Assert.assertEquals("dd MMM yyyy", Dates.pattern("31 dec 2017"));
        Assert.assertEquals("dd MMMM yyyy 'at' HH:mm:ss", Dates.pattern("31 december 2017 at 08:38:00"));
        Assert.assertEquals("E MMM dd HH:mm:ss zzz yyyy", Dates.pattern("Thu Mar 21 06:14:26 UTC 2019"));
        Assert.assertEquals("yyyy'年' MM'月' dd'日' E HH:mm:ss zzz", Dates.pattern("2019年 03月 21日 星期四 06:13:57 UTC"));
//        Assert.assertEquals("EEE MMM dd HH:mm:ss 'GMT'XXX yyyy", Dates.pattern("Sun Oct 11 00:00:00 GMT+08:00 1998"));
    }

    @Test
    public void test104() {
        Assert.assertEquals("19981225", Dates.format08("1998-12-25"));
        Assert.assertEquals(Dates.format08(Dates.calcDay(new Date(), -1)), Dates.format08(-1));
        Assert.assertEquals(Dates.format10(Dates.calcDay(new Date(), -1)), Dates.format10(-1));
    }

}
