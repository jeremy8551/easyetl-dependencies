package icu.etl.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author jeremy8551@qq.com
 * @createtime 2024/2/19 14:59
 */
public class LocalesTest {

    @Test
    public void testindexOfSqlWords1() {
        Assert.assertNull(Locales.lookup(""));
        Assert.assertNotNull(Locales.lookup("zh_CN"));
    }
}
