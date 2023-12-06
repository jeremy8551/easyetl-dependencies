package icu.etl.util;

import icu.etl.util.MessageFormatter;
import org.junit.Assert;
import org.junit.Test;

public class MessageFormatterTest {

    @Test
    public void test() {
        MessageFormatter sf = new MessageFormatter();
        Assert.assertEquals("", sf.format("", new Object[]{"1", "2", "3"}));
        Assert.assertEquals("1", sf.format("{}", new Object[]{"1", "2", "3"}));
        Assert.assertEquals("1+2", sf.format("{}+{}", new Object[]{"1", "2", "3"}));
        Assert.assertEquals("\\{}+1", sf.format("\\{}+{}", new Object[]{"1", "2", "3"}));
        Assert.assertEquals("\\{}+13", sf.format("\\{}+{}3", new Object[]{"1", "2", "3"}));
    }
}
