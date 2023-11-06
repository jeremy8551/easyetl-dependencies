package icu.etl.util;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class NetUtilsTest {

    @Test
    public void test() {
        assertTrue(NetUtils.joinUri("/", "/").equals("/"));
        assertTrue(NetUtils.joinUri("/1", "/").equals("/1/"));
        assertTrue(NetUtils.joinUri("/", "/1").equals("/1"));
        assertTrue(NetUtils.joinUri("/a", "/b").equals("/a/b"));
        assertTrue(NetUtils.joinUri("/a", "/b").equals("/a/b"));
        assertTrue(NetUtils.joinUri("a", "b").equals("a/b"));
        assertTrue(NetUtils.joinUri("a/", "/b").equals("a/b"));
        assertTrue(NetUtils.joinUri(null, null).equals(""));
        assertTrue(NetUtils.joinUri("/", null).equals("/"));
        assertTrue(NetUtils.joinUri(null, "/").equals("/"));
    }

}
