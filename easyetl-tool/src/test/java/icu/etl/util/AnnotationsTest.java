package icu.etl.util;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AnnotationsTest {

    @Test
    public void testgetAnnotations() {
        List<Object> annos = Annotations.getAnnotations(ClassUtilsTest.class, Test.class.getName());
        assertTrue(annos.size() > 1);
    }

    @Test
    public void testgetAnnotations1() {
        AnnotationsTest obj = new AnnotationsTest();
        Method method = ClassUtils.getMethod(obj, "testgetAnnotations1");
        List<Test> list = Annotations.getAnnotations(method, Test.class.getName());
        assertEquals(list.size(), 1);
    }

}
