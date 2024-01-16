package icu.etl.util;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 类信息工具
 *
 * @author jeremy8551@qq.com
 * @createtime 2011-05-24
 */
public class ClassUtils {

    /** 设置脚本引擎默认 classpath 绝对路径 */
    public final static String PROPERTY_CLASSPATH = ClassUtils.class.getPackage().getName().split("\\.")[0] + "." + ClassUtils.class.getPackage().getName().split("\\.")[1] + ".classpath";

    /** 当前JAVA虚拟机的默认类路径 */
    public static String CLASSPATH = System.getProperty(PROPERTY_CLASSPATH);

    public ClassUtils() {
    }

    /**
     * 确定对象是否是JAVA的基本类型：
     * String
     * int
     * byte
     * short
     * long
     * float
     * double
     * char
     * boolean
     *
     * @param obj 对象
     * @return 返回true表示参数是JAVA的基本类型
     */
    public static boolean isPrimitiveType(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof String) {
            return true;
        }
        if (obj instanceof Integer) {
            return true;
        }
        if (obj instanceof Byte) {
            return true;
        }
        if (obj instanceof Short) {
            return true;
        }
        if (obj instanceof Long) {
            return true;
        }
        if (obj instanceof Float) {
            return true;
        }
        if (obj instanceof Double) {
            return true;
        }
        if (obj instanceof Character) {
            return true;
        }
        return obj instanceof Boolean;
    }

    /**
     * 判断类信息参数 cls 是否在类信息集合参数 c 范围内（使用类信息全名是否相等来判断类信息是否相等）
     *
     * @param cls 类信息
     * @param c   类信息集合
     * @return 返回 true 表示类信息在集合范围内
     */
    public static boolean inCollection(Class<?> cls, Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }

        if (cls == null) {
            for (Object o : c) {
                if (o == null) {
                    return true;
                }
            }
        } else {
            for (Object obj : c) {
                if (obj == null) {
                    continue;
                } else {
                    Class<?> cs = (Class<?>) (obj instanceof Class ? obj : obj.getClass());
                    if (cs.getName().equals(cls.getName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断类信息参数 cls 是否在类信息数组参数 array 范围内（使用类信息全名是否相等来判断类信息是否相等）
     *
     * @param cls   类信息
     * @param array 类信息数组
     * @return 返回 true 表示类信息在数组范围内，返回 false 表示类信息不再数组范围内
     */
    public static boolean inArray(Class<?> cls, Class<?>... array) {
        if (cls == null) {
            for (int i = 0; i < array.length; i++) {
                if (array[i] == null) {
                    return true;
                }
            }
        } else {
            for (int i = 0; i < array.length; i++) {
                Class<?> cs = array[i];
                if (cs != null && cs.getName().equals(cls.getName())) { // 判断类信息全名是否相等
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 返回对象或类参数 obj 类信息中的属性名
     *
     * @param obj  类信息
     * @param name 属性名
     * @return 格式：类全名.属性名
     * @throws RuntimeException 如果属性不存在则抛出异常
     */
    public static String toFieldName(Object obj, String name) {
        Field f = null;
        Class<?> cls = (obj instanceof Class) ? ((Class<?>) obj) : obj.getClass();
        try {
            f = cls.getDeclaredField(name);
        } catch (Throwable e) {
            Field[] array = cls.getDeclaredFields();
            for (Field field : array) {
                if (field.getName().equals(name)) {
                    f = field;
                    break;
                }
            }
        }

        if (f == null) {
            throw new RuntimeException(name);
        } else {
            return cls.getName() + "." + f.getName();
        }
    }

    /**
     * 返回对象或类参数 obj 上的方法
     *
     * @param obj   类信息
     * @param name  方法名
     * @param types 方法上所有参数的类型
     * @return 格式：方法名(参数类1，参数类2，参数类3 ..)
     * @throws RuntimeException 如果方法不存在则抛出异常
     */
    public static String toMethodName(Object obj, String name, Class<?>... types) {
        Method method = ClassUtils.getMethod(obj, name, types);
        if (method == null) {
            throw new RuntimeException(name);
        }

        StringBuilder buf = new StringBuilder();
        buf.append(method.getName());
        buf.append("(");
        Class<?>[] array = method.getParameterTypes(); // 返回所有参数类型
        for (int i = 0; i < array.length; ) {
            String className = array[i].getSimpleName();
            buf.append(className);
            if (++i < array.length) {
                buf.append(", ");
            }
        }
        buf.append(")");
        return buf.toString();
    }

    /**
     * 调用java对象 obj 中的方法 name
     *
     * @param obj  JAVA对象
     * @param name 方法名
     * @param args 方法参数值
     * @return 方法返回值
     */
    public static Object executeMethod(Object obj, String name, Object... args) {
        Method method = ClassUtils.getMethod(obj, name);
        try {
            return method.invoke(obj, args);
        } catch (Throwable e) {
            throw new RuntimeException(obj.getClass().getSimpleName() + "." + name, e);
        }
    }

    /**
     * 在java对象中查找方法 name
     *
     * @param obj            JAVA对象
     * @param name           方法名
     * @param parameterTypes 方法的输入参数类型
     * @return 方法对象
     */
    public static Method getMethod(Object obj, String name, Class<?>... parameterTypes) {
        Class<?> cls = (obj instanceof Class) ? ((Class<?>) obj) : obj.getClass();
        try {
            return cls.getMethod(name, parameterTypes);
        } catch (Throwable e) {
            Method[] methods = cls.getDeclaredMethods();
            List<Method> list = new ArrayList<Method>(methods.length);
            for (Method method : methods) {
                if (method.getName().equals(name)) { // 判断方法名是否相等
                    list.add(method);
                }
            }

            if (list.isEmpty()) {
                return null;
            } else if (list.size() == 1) {
                return list.get(0);
            } else {
                for (Method method : list) {
                    Class<?>[] types = method.getParameterTypes();
                    if (types.length == parameterTypes.length) {
                        boolean b = true;
                        for (int i = 0; i < types.length; i++) {
                            if (!types[i].equals(parameterTypes[i])) { // 判断参数类型是否相等
                                b = false;
                                break;
                            }
                        }
                        if (b) {
                            return method;
                        }
                    }
                }
            }
            return null;
        }
    }

    /**
     * 判断 java 类中是否有指定方法
     *
     * @param cls            类信息
     * @param name           方法名
     * @param parameterTypes 方法参数
     * @return 返回true表示方法存在 false表示方法不存在
     */
    public static boolean containsMethod(Class<?> cls, String name, Class<?>... parameterTypes) {
        try {
            Class<?>[] parameter = (parameterTypes == null) ? ((Class<?>[]) null) : parameterTypes;
            return cls.getMethod(name, parameter) != null;
        } catch (NoSuchMethodException e) {
            return false;
        } catch (SecurityException e) {
            return false;
        }
    }

    /**
     * 修改实例对象中的静态字段值
     *
     * @param obj        实例对象
     * @param fieldRegex 匹配字段名的正则表达式
     * @param fieldClass 字段类型
     * @param newValue   新值
     */
    public static <E> void setField(Object obj, String fieldRegex, Class<E> fieldClass, E newValue) {
        Field[] list = obj.getClass().getDeclaredFields();
        for (Field field : list) {
            String name = field.getName();
            if (name.matches(fieldRegex) && field.getType().equals(fieldClass)) {
                try {
                    JUL.debug("change Class: " + obj.getClass().getName() + ", Field: " + name + ", Type: " + field.getType().getName());

                    field.setAccessible(true);
                    Field modifiers = Field.class.getDeclaredField("modifiers");
                    modifiers.setAccessible(true);
                    modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
                    field.set(obj, newValue);
//                    modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL); // 再把final修饰符给加回来
                } catch (Throwable e) {
                    JUL.warn(obj.getClass().getName(), e);
                }
            }
        }
    }

    /**
     * 修改实例对象中的静态字段值
     *
     * @param obj   实例对象
     * @param field 字段类型
     * @param value 新值
     */
    public static void setField(Object obj, Field field, Object value) {
        try {
            field.setAccessible(true);
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(obj, value);
        } catch (Throwable e) {
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 返回对象中某个属性值
     *
     * @param obj   对象
     * @param field 对象中属性信息
     * @param <E>   属性值的类型
     * @return 属性值
     */
    public static <E> Object getField(Object obj, Field field) {
        try {
            field.setAccessible(true);
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            return field.get(obj);
        } catch (Throwable e) {
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 读取并解析 java.class.path 类路径参数
     * <p>
     * /Users/etl/git/repository-atom/atom/target/classes
     * /Users/etl/git/repository-atom/atom/lib/db2java.jar
     * /Users/etl/git/repository-atom/atom/lib/db2jcc_license_cisuz.jar
     * /Users/etl/git/repository-atom/atom/lib/db2jcc_license_cu.jar
     * /Users/etl/git/repository-atom/atom/lib/db2jcc.jar
     * /Users/etl/.m2/repository/javax/servlet/javax.servlet-api/3.1.0/javax.servlet-api-3.1.0.jar
     *
     * @return 类路径数组（数组中没有空值）
     */
    public static String[] getJavaClassPath() {
        String delimiter = System.getProperty("path.separator"); // 路径分隔符
        String classpath = System.getProperty("java.class.path");
        String[] array = StringUtils.removeBlank(StringUtils.split(classpath, delimiter));
        for (int i = 0; i < array.length; i++) {
            array[i] = StringUtils.decodeJvmUtf8HexString(array[i]);
        }
        return array;
    }

    /**
     * 返回 JAVA 类信息所在的 classpath 路径，如果类信息在 jar 文件中则返回 jar 文件绝对路径。
     *
     * @param cls 类信息
     * @return classpath目录绝对路径或jar文件绝对路径
     */
    public static String getClasspath(Class<?> cls) {
        if (cls == null) {
            throw new NullPointerException();
        }

        // 优先检查用户自定义的 CLASSPATH
        if (ClassUtils.CLASSPATH != null) {
            if (JUL.isDebugEnabled()) {
                JUL.debug(ResourcesUtils.getCommonMessage(8, cls.getName(), ClassUtils.CLASSPATH));
            }

            if (ClassUtils.isClasspath0(ClassUtils.CLASSPATH, cls)) {
                return ClassUtils.CLASSPATH;
            }
        }

        // 查询根路径下的 CLASSPATH, 使用场景如: WebContainer
        String classpath0 = StringUtils.decodeJvmUtf8HexString(cls.getResource("/").getFile());
        if (classpath0 != null) {
            if (JUL.isDebugEnabled()) {
                JUL.debug(ResourcesUtils.getCommonMessage(9, cls.getName(), classpath0));
            }

            if (ClassUtils.isClasspath0(classpath0, cls)) {
                return ClassUtils.getClasspath(classpath0);
            }
        }

        // 查询类信息当前路径下的 CLASSPATH, 使用场景如: WebSphere
        String classpath1 = StringUtils.decodeJvmUtf8HexString(cls.getResource("").getPath());
        if (classpath1 != null) {
            if (JUL.isDebugEnabled()) {
                JUL.debug(ResourcesUtils.getCommonMessage(10, cls.getName(), classpath1));
            }

            if (ClassUtils.isClasspath1(classpath1)) {
                // 截取 classpath 中右侧的JAVA包文件路径, 如: D:\...\classes\cn\com\baidu\webs （删右侧的 cn\com\baidu\webs 得到 classpath 路径）
                String classPackName = cls.getPackage().getName().replace('.', File.separatorChar);
                classpath1 = FileUtils.replaceFolderSeparator(StringUtils.rtrim(classpath1, '/', '\\'));
                if (classpath1.endsWith(classPackName)) {
                    classpath1 = classpath1.substring(0, classpath1.length() - classPackName.length());
                }
                return ClassUtils.getClasspath(classpath1);
            }
        }

        // 从环境变量 CLASSPATH 中读取类所在类目录
        List<String> classpaths = new ArrayList<String>();
        String[] array = ClassUtils.getJavaClassPath();
        for (int i = 0; i < array.length; i++) {
            File file = new File(array[i]);
            if (file.exists() && file.isFile() && "jar".equalsIgnoreCase(FileUtils.getFilenameExt(file.getName()))) {
                // 忽略 jar 文件
                continue;
            } else if (file.exists() && file.isDirectory()) {
                classpaths.add(file.getAbsolutePath());
                String classPackageName = cls.getPackage().getName().replace('.', File.separatorChar);
                String classfilepath = FileUtils.joinPath(file.getAbsolutePath(), classPackageName);
                if (JUL.isDebugEnabled()) {
                    JUL.debug(ResourcesUtils.getCommonMessage(12, cls.getName(), classfilepath));
                }

                if (ClassUtils.isClasspath1(classfilepath)) {
                    return file.getAbsolutePath();
                }
            } else {
                if (JUL.isWarnEnabled()) {
                    JUL.warn(ResourcesUtils.getCommonMessage(13, cls.getName(), file.getAbsolutePath())); // 类路径不合法
                }
            }
        }

        // 查询类信息所在 jar 文件的绝对路径
        String jarfilepath = ClassUtils.getJarPath(cls);
        if (jarfilepath != null) {
            if (JUL.isDebugEnabled()) {
                JUL.debug(ResourcesUtils.getCommonMessage(11, cls.getName(), jarfilepath)); // 类路径不合法
            }

            return new File(jarfilepath).getAbsolutePath();
        }

        // 默认从环境变量中选一个类路径目录作为返回值
        if (classpaths.isEmpty()) {
            return null;
        } else {
            Comparator<String> c = new Comparator<String>() {
                public int compare(String o1, String o2) {
                    String[] a1 = StringUtils.split(o1, File.separatorChar);
                    String[] a2 = StringUtils.split(o2, File.separatorChar);

                    // 路径中存在 classes 或 bin 优先级越高
                    if (java.util.Arrays.binarySearch(a1, "classes") != -1 || java.util.Arrays.binarySearch(a1, "bin") != -1) {
                        return 1;
                    } else if (java.util.Arrays.binarySearch(a2, "classes") != -1 || java.util.Arrays.binarySearch(a2, "bin") != -1) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            };

            Collections.sort(classpaths, c);
            return classpaths.get(classpaths.size() - 1); // 默认返回环境变量 CLASSPATH 中最后一个类目录
        }
    }

    /**
     * 判断类路径下是否存在 WEB-INF/classes 目录
     *
     * @param classpath 类路径
     * @return
     */
    private static String getClasspath(String classpath) {
        String prefix = "WEB-INF";
        int index = -1;
        if ((index = classpath.indexOf(prefix)) != -1) {
            String webinf = classpath.substring(0, index + prefix.length()); // /opt/IBM/.../WEB-INF
            String classes = FileUtils.joinPath(webinf, "classes");
            File file = new File(classes);
            if (file.exists()) {
                return classes;
            }

            try {
                return new URL(classes).getPath();
            } catch (MalformedURLException e) {
                return classpath;
            }
        } else {
            return new File(classpath).getAbsolutePath();
        }
    }

    /**
     * 校验 classpath 是否正确
     *
     * @param classpath 类路径
     * @param cls       类信息
     * @return
     */
    private static boolean isClasspath0(String classpath, Class<?> cls) {
        if (classpath == null || classpath.length() == 0 || StringUtils.inArray(classpath, "/", "\\")) {
            return false;
        } else {
            String className = cls.getName().replace('.', File.separatorChar);
            String classfilepath = FileUtils.joinPath(classpath, className) + ".class";
            return FileUtils.isFile(classfilepath);
        }
    }

    /**
     * 校验 classpath 是否正确
     *
     * @param classpath 类路径
     * @return
     */
    private static boolean isClasspath1(String classpath) {
        return classpath != null //
                && classpath.length() > 0 //
                && !StringUtils.inArray(classpath, "/", "\\") //
                && new File(classpath).exists() //
                ;
    }

    /**
     * 返回类文件（*.class）所在jar文件的绝对路径, 如果是多层jar包嵌套，则返回第一层jar所在路径
     *
     * @param cls 类信息
     * @return 如果类信息参数不在 jar 包中时返回 null
     */
    public static String getJarPath(Class<?> cls) {
        if (cls == null) {
            return null;
        }

        ProtectionDomain domain = cls.getProtectionDomain();
        if (domain == null) {
            throw new RuntimeException(cls.getName());
        }

        CodeSource codeSource = domain.getCodeSource();
        if (codeSource == null) {
            throw new RuntimeException(cls.getName());
        }

        URL url = codeSource.getLocation();
        if (url == null) {
            throw new RuntimeException(cls.getName());
        }

        String filepath = StringUtils.decodeJvmUtf8HexString(url.getFile()); // 解压文件路径中的非ascii字符
        if (filepath == null) {
            return null;
        }

        int index = StringUtils.indexOf(filepath, ".jar", 0, true);
        if (index == -1) {
            return null;
        } else {
            return filepath.substring(0, index + 4);
        }
    }

    /**
     * 查找资源
     *
     * @param name  给定资源名称,
     *              例如: /jdbc.properties
     *              /images/show.gif
     * @param array 参数对象数组，最多只能设置一个元素
     * @return
     */
    public static InputStream getResourceAsStream(String name, Object... array) {
        Object obj = Ensure.onlyOne(array);
        if (obj == null) {
            obj = new ClassUtils();
        }

        InputStream in = ClassUtils.getInputStream(obj, name);
        if (in == null) {
            Class<? extends Object> cls = (obj instanceof Class) ? ((Class<?>) obj) : obj.getClass();
            String classpath = ClassUtils.getClasspath(cls);
            String filepath = StringUtils.decodeJvmUtf8HexString(classpath);
            int index = StringUtils.indexOf(filepath, ".jar", 0, true);
            if (index != -1) {
                String str = StringUtils.replaceAll(filepath.substring(index + 4), "!/", "/");
                String uri = NetUtils.joinUri(str, name);
                return ClassUtils.getInputStream(obj, uri);
            } else {
                String all = "";
                String[] parts = StringUtils.split(filepath, "/");
                for (int i = parts.length - 1; i >= 0; i--) {
                    String prefix = NetUtils.joinUri(parts[i], all);
                    String uri = NetUtils.joinUri("", prefix, name);
                    in = ClassUtils.getInputStream(obj, uri);
                    if (in == null) {
                        all = prefix;
                    } else {
                        return in;
                    }
                }
                return null;
            }
        } else {
            return in;
        }
    }

    /**
     * 将资源通过输入流返回
     *
     * @param obj  类信息或对象信息
     * @param name 资源定位符
     * @return 字节输入流
     */
    private static InputStream getInputStream(Object obj, String name) {
        if (obj instanceof Class) {
            return ((Class<?>) obj).getResourceAsStream(name);
        } else {
            return obj.getClass().getResourceAsStream(name);
        }
    }

    /**
     * 返回class信息的包名 <br>
     * getPackageName({@link ClassUtils}, 1) 返回字符串 icu <br>
     * getPackageName({@link ClassUtils}, 2) 返回字符串 icu.etl <br>
     *
     * @param packageName 类名或包名
     * @param level       显示包名的级别
     *                    0表示显示原包名
     *                    -1表示从右向左边显示包名
     *                    1表示从左向右边显示包名
     * @return
     */
    public static String getPackageName(String packageName, int level) {
        if (packageName == null || level == 0) {
            return packageName;
        }

        String[] array = StringUtils.split(packageName, '.');
        StringBuilder buf = new StringBuilder(packageName.length());

        if (level < 0) { // 从右向左边截取
            int i = array.length - (-level);
            if (i < 0) {
                i = 0;
            }

            for (; i >= 0 && i < array.length; ) {
                buf.append(array[i]);
                if (++i < array.length) {
                    buf.append('.');
                }
            }
        } else { // level > 0 从左向右边截取
            for (int i = 0, length = Math.min(level, array.length); i < length; ) {
                buf.append(array[i]);
                if (++i < length) {
                    buf.append('.');
                }
            }
        }

        return buf.toString();
    }

    /**
     * 返回 Class 信息的部分包名
     *
     * @param cls   类信息
     * @param level 显示包名的级别，从1（表示包的根名）开始
     *              比如类信息是 java.lang.String，level参数值是2，返回值就是 java.lang
     * @return 包名
     */
    public static String getPackageName(Class<?> cls, int level) {
        if (level <= 0) {
            throw new IllegalArgumentException(String.valueOf(level));
        }

        if (cls == null) {
            return null;
        } else {
            String packageName = cls.getPackage().getName();
            String[] array = StringUtils.split(packageName, '.');
            StringBuilder buf = new StringBuilder(packageName.length());
            for (int i = 0, length = Math.min(level, array.length); i < length; ) {
                buf.append(array[i]);
                if (++i < length) {
                    buf.append('.');
                }
            }
            return buf.toString();
        }
    }

    /**
     * 判断字符串参数className对应的Java类是否存在
     *
     * @param className java类全名
     * @return 类信息
     */
    @SuppressWarnings("unchecked")
    public static <E> Class<E> forName(String className) {
        try {
            return (Class<E>) Class.forName(className);
        } catch (Throwable e) {
            return null;
        }
    }

    /**
     * 判断字符串参数 className 对应的Java类是否存在
     * 不存在时会返回 null
     *
     * @param <E>        类信息
     * @param className  类名
     * @param initialize 是否初始化
     * @param loader     类加载器
     * @return 类信息
     */
    @SuppressWarnings("unchecked")
    public static <E> Class<E> forName(String className, boolean initialize, ClassLoader loader) {
        try {
            return (Class<E>) Class.forName(className, initialize, loader);
        } catch (Throwable e) {
            return null;
        }
    }

    /**
     * 判断字符串参数className对应的Java类是否存在
     *
     * @param className java类全名
     * @return 类信息
     */
    @SuppressWarnings("unchecked")
    public static <E> Class<E> loadClass(String className) {
        try {
            return (Class<E>) Class.forName(className);
        } catch (Throwable e) {
            throw new RuntimeException(className, e);
        }
    }

    /**
     * 判断字符串参数className对应的Java类是否存在
     *
     * @param className  类名
     * @param initialize 是否初始化
     * @param loader     类加载器
     * @return 类信息
     */
    @SuppressWarnings("unchecked")
    public static <E> Class<E> loadClass(String className, boolean initialize, ClassLoader loader) {
        try {
            return (Class<E>) Class.forName(className, initialize, loader);
        } catch (Throwable e) {
            throw new RuntimeException(className, e);
        }
    }

    /**
     * 生成一个类的实例对象
     *
     * @param cls 类信息
     * @param <E> 类信息
     * @return 实例对象
     */
    @SuppressWarnings("unchecked")
    public static <E> E newInstance(Class<?> cls) {
        if (cls == null) {
            throw new NullPointerException();
        }

        try {
            return (E) cls.newInstance();
        } catch (Throwable e) {
            throw new IllegalArgumentException(ResourcesUtils.getClassMessage(12, cls.getName()), e);
        }
    }

    /**
     * 生成一个类的实例对象
     *
     * @param classname   类名或Class对象
     * @param classLoader 类加载器
     * @param <E>         类信息
     * @return 实例对象
     */
    @SuppressWarnings("unchecked")
    public static <E> E newInstance(String classname, ClassLoader classLoader) {
        if (StringUtils.isBlank(classname)) {
            return null;
        }

        Class<?> cls = ClassUtils.forName(classname, true, classLoader);
        if (cls == null) {
            throw new IllegalArgumentException(ResourcesUtils.getClassMessage(12, classname));
        }

        try {
            return (E) cls.newInstance();
        } catch (Throwable e) {
            throw new IllegalArgumentException(ResourcesUtils.getClassMessage(12, cls.getName()), e);
        }
    }

    /**
     * 返回默认的 ClassLoader 对象
     *
     * @return 类加载器
     */
    public static ClassLoader getDefaultClassLoader() {
        try {
            return Thread.currentThread().getContextClassLoader();
        } catch (Throwable e) {
            ClassLoader loader = ClassUtils.class.getClassLoader();
            if (loader != null) {
                return loader;
            }

            try {
                return ClassLoader.getSystemClassLoader();
            } catch (Throwable e1) {
                return null;
            }
        }
    }

    /**
     * 将对象中的 get 方法和 to 方法名和返回值转为字符串表格，用于调试打印对象内容
     *
     * @param obj          对象
     * @param deep         true 表示打印输出对象中的方法; false表示不打印输出对象的方法
     * @param ignoreCase   true表示忽略英文字母大小写
     * @param methodPrefix 方法名前缀数组
     * @return 字符图形表格
     */
    public static String toString(Object obj, boolean deep, boolean ignoreCase, String... methodPrefix) {
        if (obj == null) {
            return "";
        }

        String prefix = "obj.";
        CharTable table = new CharTable();
        table.addTitle("FUNCTION_NAME", CharTable.ALIGN_LEFT);
        table.addTitle("RETURN", CharTable.ALIGN_LEFT);

        ArrayList<String> list = ArrayUtils.asList(methodPrefix);
        Method[] methods = obj.getClass().getMethods();
        for (Method method : methods) {
            if (Modifier.isAbstract(method.getModifiers())) { // 过滤抽象方法
                continue;
            }

            String functionName = method.getName();
            if (StringUtils.inArray(functionName, "getClass", "hashCode", "clone", "toString")) {
                continue;
            }

            Type[] types = method.getGenericParameterTypes();
            if (types == null || types.length == 0) {
                Class<?> cls = method.getReturnType();
                if (cls.equals(Void.class)) {
                    continue;
                }

                if (StringUtils.startsWith(functionName, list, ignoreCase)) {
                    try {
                        Object value = method.invoke(obj);
                        table.addCell(prefix + functionName);

                        if (value == null || value.getClass().getName().startsWith("java.") || !deep) {
                            table.addCell(value == null ? "" : StringUtils.toString(value));
                        } else {
                            table.addCell(toString(value, false, ignoreCase, methodPrefix));
                        }
                    } catch (Throwable e) {
                        table.addCell(prefix + functionName);
                        table.addCell(StringUtils.toString(e));
                    }
                }
            }
        }

        return table.toString(CharTable.Style.standard);
    }

    /**
     * 将Class数组转为类名集合
     *
     * @param array Class类信息
     * @return 类名集合
     */
    public static List<String> asNameList(Class<?>[] array) {
        if (array == null) {
            return new ArrayList<String>(0);
        }

        List<String> list = new ArrayList<String>(array.length);
        for (Class<?> cls : array) {
            list.add(cls.getName());
        }
        return list;
    }

    /**
     * 返回类实现的所有接口，包括所有子类上的接口，以及接口继承的所有接口
     *
     * @param cls    类信息
     * @param filter 接口的过滤器
     * @return 接口信息集合
     */
    public static List<Class<?>> getAllInterface(Class<?> cls, InterfaceFilter filter) {
        List<Class<?>> list = new ArrayList<Class<?>>();
        if (cls == null) {
            return list;
        }

        loadAllInterface(cls, filter, list);

        // 查询父类上的接口
        Class<?> supcls = cls.getSuperclass();
        while (supcls != null) {
            loadAllInterface(supcls, filter, list);
            supcls = supcls.getSuperclass();
        }
        return list;
    }

    /**
     * 查询类实现的所有接口，包括接口继承的所有接口
     *
     * @param cls    类信息
     * @param filter 接口的过滤器
     * @param list   存储接口的集合
     */
    private static void loadAllInterface(Class<?> cls, InterfaceFilter filter, List<Class<?>> list) {
        Class<?>[] array = cls.getInterfaces();
        if (array != null && array.length > 0) {
            for (Class<?> c : array) { // 判断是否有重复接口
                boolean add = true;
                for (Class<?> cl : list) {
                    if (ClassUtils.equals(cl, c)) { // 判断类名是否重复
                        add = false;
                        break;
                    }
                }

                if (add && (filter == null || filter.accept(c, c.getName()))) {
                    list.add(c);
                }
            }

            for (Class<?> c : array) {
                loadAllInterface(c, filter, list);
            }
        }
    }

    /**
     * 返回类信息 {@code cls} 上指定接口 {@code interfacecls} 的泛型
     *
     * @param cls          类信息
     * @param interfacecls 类 {@code cls} 上实现的接口
     * @return 范型类型
     */
    public static String[] getInterfaceGenerics(Class<?> cls, Class<?> interfacecls) {
        Type[] types = cls.getGenericInterfaces();
        for (Type type : types) {
            if (type instanceof ParameterizedType) {
                ParameterizedType ptype = (ParameterizedType) type;
                // pt.getTypeName().startsWith(interfacecls.getName())
                if (ptype.toString().startsWith(interfacecls.getName())) { // 判断接口名是否匹配
                    Type[] actualTypeArguments = ptype.getActualTypeArguments();
                    String[] array = new String[actualTypeArguments.length];
                    for (int i = 0; i < array.length; i++) {
                        // array[i] = actualTypeArguments[i].getTypeName();
                        String str = actualTypeArguments[i].toString();
                        String[] split = str.split("\\s+");
                        array[i] = split.length >= 2 ? split[1] : str;
                    }
                    return array;
                }
            }
        }
        return new String[0];
    }

    /**
     * 判断2个 Class 参数是否相等
     * <p>
     * 编写这个方法是因为 {@linkplain Class#equals(Object)} 方法不准确
     *
     * @param cls1 类信息
     * @param cls2 类信息
     * @return 返回true表示相等 返回false表示不等
     */
    public static boolean equals(Class<?> cls1, Class<?> cls2) {
        boolean b1 = cls1 == null;
        boolean b2 = cls2 == null;
        if (b1 && b2) {
            return true;
        } else if (b1 || b2) {
            return false;
        } else {
            return cls1.getName().equals(cls2.getName());
        }
    }

    /**
     * 判断集合参数 {@code c}中是否包含参数 {@code cls}
     * <p>
     * 编写这个方法是因为 {@linkplain Class#equals(Object)} 方法不准确
     *
     * @param c   集合
     * @param cls 类信息
     * @return 返回true表示包含 false表示不包含
     */
    public static boolean contains(Collection<Class<?>> c, Class<?> cls) {
        if (c == null) {
            throw new NullPointerException();
        }

        for (Class<?> aClass : c) {
            if (ClassUtils.equals(cls, aClass)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 在类路径classpath下查找包名最短的类（如果有多个，则只返回第一个）
     *
     * @param classLoader 类加载器
     * @param classpath   类路径
     * @return 包名最短的类信息，可能是null
     */
    public static Set<String> findShortPackage(ClassLoader classLoader, String classpath) {
        if (StringUtils.isBlank(classpath)) {
            throw new IllegalArgumentException(classpath);
        }
        if (classLoader == null) {
            throw new NullPointerException();
        }

        File dir = new File(classpath);
        return ClassUtils.findShortPackage(classLoader, classpath, dir);
    }

    /**
     * 在类路径classpath下查找包名最短的类（如果有多个，则只返回第一个）
     *
     * @param classLoader 类加载器
     * @param classpath   类路径
     * @param dir         目录
     * @return 包名最短的类信息，可能是null
     */
    private static Set<String> findShortPackage(ClassLoader classLoader, String classpath, File dir) {
        File[] files = FileUtils.array(dir.listFiles());

        Class<?> cls = findFirstClass(classLoader, classpath, files);
        if (cls != null) {
            Set<String> list = new HashSet<String>();
            list.add(cls.getPackage().getName());
            return list;
        }

        // 遍历目录
        Set<String> list = new HashSet<String>();
        for (File file : files) {
            if (file.isDirectory()) {
                list.addAll(ClassUtils.findShortPackage(classLoader, classpath, file));
            }
        }
        return list;
    }

    /**
     * 在文件数组中查找第一个类信息
     *
     * @param classLoader 类加载器
     * @param classpath   类路径
     * @param files       文件数组
     * @return 类信息
     */
    private static Class<?> findFirstClass(ClassLoader classLoader, String classpath, File[] files) {
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".class")) {
                if (file.getAbsolutePath().startsWith(classpath)) {
                    String lastfix = file.getAbsolutePath().substring(classpath.length());
                    String str = FileUtils.replaceFolderSeparator(StringUtils.ltrim(lastfix, '/', '\\'), '.');
                    String className = str.substring(0, str.length() - ".class".length());
                    Class<?> cls = ClassUtils.forName(className, false, classLoader);
                    if (cls != null) {
                        return cls;
                    }
                }
            }
        }
        return null;
    }

}