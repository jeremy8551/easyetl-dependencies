package icu.etl.util;

import org.junit.Assert;
import org.junit.Test;

public class MessageFormatterTest {

    @Test
    public void test1() {
        MessageFormatter mf = new MessageFormatter();
        Assert.assertEquals("", mf.format("", new Object[]{"1", "2", "3"}));
        Assert.assertEquals("1", mf.format("{}", new Object[]{"1", "2", "3"}));
        Assert.assertEquals("1+2", mf.format("{}+{}", new Object[]{"1", "2", "3"}));
        Assert.assertEquals("\\{}+1", mf.format("\\{}+{}", new Object[]{"1", "2", "3"}));
        Assert.assertEquals("\\{}+13", mf.format("\\{}+{}3", new Object[]{"1", "2", "3"}));
    }

    @Test
    public void test2() {
        MessageFormatter mf = new MessageFormatter(MessageFormatter.Placeholder.NORMAL);
        Assert.assertEquals("", mf.format("", new Object[]{"1", "2", "3"}));
        Assert.assertEquals("1", mf.format("{0}", new Object[]{"1", "2", "3"}));
        Assert.assertEquals("1+2", mf.format("{0}+{1}", new Object[]{"1", "2", "3"}));
        Assert.assertEquals("{0}", mf.format("\\{0}", new Object[]{"1", "2", "3"}));
        Assert.assertEquals("{0}+1", mf.format("\\{0}+{0}", new Object[]{"1", "2", "3"}));
        Assert.assertEquals("{}+13", mf.format("\\{}+{0}3", new Object[]{"1", "2", "3"}));
        Assert.assertEquals("{}+{01}3", mf.format("\\{}+{01}3", new Object[]{"1", "2", "3"}));
        Assert.assertEquals("{}+{01}3", mf.format("\\{}+\\{01}3", new Object[]{"1", "2", "3"}));
        Assert.assertEquals("{}+{01}3", mf.format("{}+{01}3", new Object[]{"1", "2", "3"}));
        Assert.assertEquals("{a}+{b}3", mf.format("{a}+{b}3", new Object[]{"1", "2", "3"}));
        Assert.assertEquals("11", mf.format("{10}", new Object[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"}));
    }

}
