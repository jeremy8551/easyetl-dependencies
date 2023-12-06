package icu.etl.util;

import java.util.logging.Handler;

import org.junit.Assert;
import org.junit.Test;

public class JULTest {

    @Test
    public void test() {
        JUL.reset(JUL.out);

        JUL.trace("print trace");
        JUL.trace("print trace", new NullPointerException());

        JUL.debug("print debug");
        JUL.debug("print debug", new NullPointerException());

        JUL.info("print info");
        JUL.info("print info", new NullPointerException());

        JUL.warn("print warn");
        JUL.warn("print warn", new NullPointerException());

        JUL.error("print error");
        JUL.error("print error", new NullPointerException());

        JUL.fatal("print fatal");
        JUL.fatal("print fatal", new NullPointerException());

        boolean b = JUL.isFatalEnabled();

        Handler[] handlers = JUL.out.getHandlers();
        for (Handler h : handlers) {
            try {
                h.flush();
                h.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void test1() {
        StackTraceElement e = LogUtils.getStackTrace("1245");
        Assert.assertNotNull(e);
        Assert.assertEquals("?", e.getClassName());
    }
}
