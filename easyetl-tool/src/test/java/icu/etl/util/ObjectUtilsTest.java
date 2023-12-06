package icu.etl.util;

import org.junit.Assert;
import org.junit.Test;

public class ObjectUtilsTest {

    @Test
    public void test() {
        String str = null;
        Assert.assertEquals("a", ObjectUtils.coalesce(str, "a"));

        str = "b";
        Assert.assertEquals("b", ObjectUtils.coalesce(str, "a"));
    }
}
