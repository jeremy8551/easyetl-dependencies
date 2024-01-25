package icu.etl.util;

import org.junit.Assert;
import org.junit.Test;

public class ResourcesUtilsTest {

    @Test
    public void test() {
        // 设置外部资源文件
        System.setProperty(ResourcesUtils.PROPERTY_RESOURCE, FileUtils.joinPath(ClassUtils.getClasspath(this.getClass()), "script_res.properties"));

        // 测试外部资源文件中的属性是否取到了
        Assert.assertEquals("filepath", ResourcesUtils.getMessage("script.engine.usage.msg888"));

        Assert.assertFalse(ResourcesUtils.existsMessage("test.msg.stdout"));
    }

    @Test
    public void test1() {
        Assert.assertEquals("test", ResourcesUtils.getMessage("test.standard.output.msg001"));
    }
}
