package icu.etl.util;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TimeWatchTest {

    @Before
    public void setUp() {
        File dir = FileUtils.getTempDir(TimeWatchTest.class);
        if (dir != null) {
            FileUtils.clearDirectory(dir);
        }
    }

    @Test
    public void test() {
        TimeWatch watch = new TimeWatch();

        // 测试暂停
        assertEquals(watch.useSeconds(), 0);
        watch.pauseOrKeep();
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(watch.useSeconds(), 0);

        // 测试暂停后启动
        watch.pauseOrKeep();
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(watch.useSeconds(), 2);

        // 测试用时
        watch.start();
        assertEquals(watch.useSeconds(), 0);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(watch.useSeconds(), 1);
    }
}
