package icu.etl.util;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import icu.etl.collection.ArrayDeque;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ClassUtilsTest {

    @Test
    public void testInArray() {
        assertTrue(!ClassUtils.inArray(null, String.class, Integer.class));
        assertTrue(ClassUtils.inArray(null, String.class, Integer.class, null));
        assertTrue(ClassUtils.inArray(String.class, String.class, Integer.class, null));
        assertTrue(!ClassUtils.inArray(String.class, Integer.class, null));
    }

    @Test
    public void testgetPackageName() {
        String str = ClassUtils.class.getName();
        int b1 = str.indexOf('.', 0);
        int b2 = str.indexOf('.', b1 + 1);

        Ensure.isTrue(str.substring(0, b2).equals(ClassUtils.getPackageName(ClassUtils.class, 2)));
    }

    @Test
    public void testgetClasspath() {
        String classpath = ClassUtils.getClasspath(ClassUtilsTest.class);
        File dir = new File(classpath);
        Assert.assertTrue(dir.exists());
        Assert.assertTrue(dir.isDirectory());
    }

    @Test
    public void testcontainsMethod() {
        assertTrue(ClassUtils.containsMethod(String.class, "toString"));
        assertTrue(ClassUtils.containsMethod(String.class, "split", String.class));
        assertTrue(ClassUtils.containsMethod(String.class, "replaceFirst", String.class, String.class));
    }

    @Test
    public void testGetClasspath() {
        String path = ClassUtils.getClasspath(ClassUtils.class);
        Ensure.isTrue(StringUtils.isNotBlank(path) && FileUtils.isDirectory(path));
        Ensure.isTrue(FileUtils.isDirectory(ClassUtils.getClasspath(StringUtils.class)));
        Ensure.isTrue(FileUtils.isDirectory(ClassUtils.getClasspath(ClassUtils.class)));
    }

    @Test
    public void testgetPackageNameString() {
        Class<?> cls = ClassUtils.class;
        String str = cls.getName();
        int end = str.lastIndexOf('.');
        String classpath = str.substring(0, end);
        assertEquals(classpath, cls.getPackage().getName());

        int n = 0;
        for (int i = 0; i < classpath.length(); i++) {
            char c = classpath.charAt(i);
            if (c == '.') {
                n++;
            }
        }
        assertEquals(classpath, ClassUtils.getPackageName(cls, ++n));
    }

    @Test
    public void testforName() {
        assertNotNull(ClassUtils.forName(String.class.getName()));
        assertNotNull(ClassUtils.forName(String.class.getName(), false, null));
        assertNotNull(ClassUtils.forName(String.class.getName(), true, null));
    }

    @Test
    public void testgetDefaultClassLoader() {
        assertNotNull(ClassUtils.getDefaultClassLoader());
    }

    @Test
    public void testgetJarPath() {
        String jarPath = ClassUtils.getJarPath(Test.class);
        File jarfile = new File(jarPath);
        assertTrue(jarfile.exists() && jarfile.isFile());
        assertNull(ClassUtils.getJarPath(StringUtils.class));
    }

    @Test
    public void testGetJvmJavaClassPath() {
        String[] paths = ClassUtils.getJavaClassPath();
        assertTrue(!StringUtils.isBlank(paths));

        System.out.println("classpath: ");
        for (String s : paths) {
            System.out.println(s);
        }
    }

    @Test
    public void testasClassname() {
        Class[] array = {String.class, Integer.class, StringUtils.class};
        List<String> nameList = ClassUtils.asNameList(array);
        Assert.assertEquals(String.class.getName(), nameList.get(0));
        Assert.assertEquals(Integer.class.getName(), nameList.get(1));
        Assert.assertEquals(StringUtils.class.getName(), nameList.get(2));
    }

    @Test
    public void testinst() {
        try {
            Object o = ClassUtils.newInstance(Object.class.getName());
            System.out.println(o);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

        try {
            Object o = ClassUtils.newInstance(Object.class);
            System.out.println(o);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testloadClass() {
        try {
            ClassUtils.loadClass(String.class.getName());
        } catch (Exception e) {
            Assert.fail();
        }

        try {
            ClassUtils.loadClass("Testlkjsadfljaslkdjf" + String.class.getName());
            Assert.fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void testgetAllInterface() {
        Class<?> cls = ArrayDeque.class;
        List<Class<?>> list = ClassUtils.getAllInterface(cls, new InterfaceFilter() {
            public boolean accept(Class<?> cls, String name) {
                return !name.startsWith("java.");
            }
        });
        Assert.assertTrue(list.isEmpty());

        list = ClassUtils.getAllInterface(cls, null);
        Assert.assertEquals(4, list.size());
        Assert.assertEquals(Cloneable.class, list.get(0));
        Assert.assertEquals(Serializable.class, list.get(1));
        Assert.assertEquals(Collection.class, list.get(2));
        Assert.assertEquals(Iterable.class, list.get(3));
    }

    @Test
    public void testgetInterfaceGenerics() {
        Class<?>[] array = ClassUtils.getInterfaceGenerics(GenericTest.class, G2.class);
        Assert.assertEquals(3, array.length);
        Assert.assertEquals(String.class, array[0]);
        Assert.assertEquals(Long.class, array[1]);
        Assert.assertEquals(Integer.class, array[2]);

        Class<?>[] array1 = ClassUtils.getInterfaceGenerics(GenericSample.class, List.class);
        Assert.assertEquals(1, array1.length);
        Assert.assertEquals(String.class, array1[0]);
    }

    interface G1<E> {
    }

    interface G2<E, F, G> {
    }

    class GenericTest implements G1<String>, G2<String, Long, Integer> {
    }

//    @Test
//    public void test() throws NamingException { TODO 需要测试
//        String key = "java:comp/env/testName1";
//        String value = "test";
//
//        Context cxt = new InitialContext();
//        cxt.bind(key, value);
//
//        Assert.assertEquals(value, ClassUtils.lookup(key));
//    }

}
