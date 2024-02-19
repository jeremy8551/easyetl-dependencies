package icu.etl.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import icu.etl.Easyetl;

/**
 * 资源文件工具
 *
 * @author jeremy8551@qq.com
 */
public class ResourcesUtils {

    /** 外部资源配置文件路径 */
    public final static String PROPERTY_RESOURCE = Easyetl.class.getPackage().getName() + ".resource";

    /** 资源文件名（不包含扩展名） */
    public static String ResourceName = Easyetl.class.getPackage().getName().replace('.', '_') + "_Messages";

    /** 资源文件 */
    private static ResourceBundle INTERNAL = ResourceBundle.getBundle(ResourcesUtils.ResourceName, Locale.getDefault());

    /** 外部输入的资源配置文件 */
    private static ResourceBundle EXTERNAL = readExternalBundle();

    /**
     * 初始化
     */
    public ResourcesUtils() {
    }

    /**
     * 查询 JNDI 资源
     *
     * @param <E>      资源类型
     * @param jndiName 资源定位符
     * @return 资源对象
     */
    @SuppressWarnings("unchecked")
    public static <E> E lookup(String jndiName) {
        try {
            Context context;
            if (StringUtils.startsWith(jndiName, "java:", 0, true, true)) {
                context = new InitialContext();
            } else {
                context = (Context) new InitialContext().lookup("java:comp/env");
            }
            return (E) context.lookup(jndiName);
        } catch (Throwable e) {
            throw new RuntimeException(jndiName, e);
        }
    }

    /**
     * 设置内部资源配置信息
     *
     * @param bundle 国际化信息集合
     */
    public static void setInternalBundle(ResourceBundle bundle) {
        if (bundle == null) {
            throw new NullPointerException();
        }
        INTERNAL = bundle;
    }

    /**
     * 设置外部资源配置信息
     *
     * @param bundle 国际化信息集合
     */
    public static void setExternalBundle(ResourceBundle bundle) {
        EXTERNAL = bundle;
    }

    /**
     * 返回内部资源配置信息
     *
     * @return 国际化资源
     */
    public static ResourceBundle getInternalBundle() {
        return INTERNAL;
    }

    /**
     * 返回外部资源配置信息
     *
     * @return 国际化资源
     */
    public static ResourceBundle getExternalBundle() {
        return EXTERNAL;
    }

    /**
     * 读取执行前缀的属性名集合
     *
     * @param prefix 属性前缀, 如: script.variable.method 或 script.command
     * @return 属性名集合
     */
    public static List<String> getPropertyMiddleName(String prefix) {
        if (prefix == null || prefix.length() == 0) {
            throw new IllegalArgumentException(prefix);
        }

        int size = prefix.split("\\.").length; // 返回属性名所在位置
        String uri = "/" + ResourcesUtils.ResourceName + ".properties"; // 资源文件的路径

        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(ResourcesUtils.class.getResourceAsStream(uri), CharsetName.UTF_8));

            List<String> list = new ArrayList<String>();
            String line;
            while ((line = in.readLine()) != null) { // 遍历属性文件中的内容
                String str = line.trim();
                if (str.startsWith(prefix)) { // 属性名前缀相等
                    String[] names = str.split("\\."); // StringUtils.split(str, '.'); // 返回所有属性名
                    if (names.length <= size) {
                        continue;
                    }

                    String name = names[size]; // 属性名
                    if (!list.contains(name)) {
                        list.add(name);
                    }
                }
            }
            return list;
        } catch (Throwable e) {
            throw new RuntimeException(uri, e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    if (JUL.isErrorEnabled()) {
                        JUL.error(prefix, e);
                    }
                }
            }
        }
    }

    /**
     * 返回资源文件中属性值
     *
     * @param key  属性名
     * @param args 属性值中占位符对应的参数
     * @return 属性值
     */
    public static String getMessage(String key, Object... args) {
        // 检查是否已设置了外部资源配置文件
        if (EXTERNAL == null) {
            String value = System.getProperty(PROPERTY_RESOURCE);
            if (value != null && value.length() != 0) {
                EXTERNAL = readExternalBundle();
            }
        }

        // 优先读取外部资源信息
        String message = null;
        if (EXTERNAL != null) {
            try {
                message = EXTERNAL.getString(key);
            } catch (Throwable e) {
                if (JUL.isDebugEnabled()) {
                    JUL.debug(key, e);
                }
            }
        }

        // 读取内部资源配置信息
        if (message == null) {
            try {
                message = INTERNAL.getString(key);
            } catch (Throwable e) {
                if (JUL.isDebugEnabled()) {
                    JUL.debug(key, e);
                }
            }
        }

        // 将参数带入资源配置信息中
        if (message == null) {
            return "";
        } else if (args.length == 0) {
            return message;
        } else {
            return MessageFormat.format(message, args);
        }
    }

    /**
     * 加载外部资源配置文件
     *
     * @return 国际化资源
     */
    private static synchronized ResourceBundle readExternalBundle() {
        File file = getExternalResourceFile();
        if (file == null) {
            return null;
        }

        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            return new PropertyResourceBundle(in);
        } catch (Throwable e) {
            if (JUL.isErrorEnabled()) {
                JUL.error(PROPERTY_RESOURCE + "=" + file.getAbsolutePath(), e);
            }
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    if (JUL.isErrorEnabled()) {
                        JUL.error(file.getAbsolutePath(), e);
                    }
                }
            }
        }
    }

    /**
     * 返回外部设置的国际化资源文件
     *
     * @return 外部资源文件
     */
    public static File getExternalResourceFile() {
        String filepath = System.getProperty(PROPERTY_RESOURCE);
        if (filepath == null || filepath.length() == 0) {
            return null;
        }

        File file = new File(filepath);
        if (!file.exists()) {
            if (JUL.isErrorEnabled()) {
                JUL.error(PROPERTY_RESOURCE + " bundle resource file " + filepath + " not found!");
            }
            return null;
        } else if (!file.isFile()) {
            if (JUL.isErrorEnabled()) {
                JUL.error(PROPERTY_RESOURCE + " bundle resource file " + filepath + " is not a file!");
            }
            return null;
        } else {
            return file;
        }
    }

    /**
     * 返回 true 表示存在国际化信息
     *
     * @param key 资源标签
     * @return 返回true表示存在国际化信息
     */
    public static boolean existsMessage(String key) {
        try {
            String message = INTERNAL.getString(key);
            if (message.length() > 0) {
                return true;
            }
        } catch (Throwable e) {
            if (JUL.isDebugEnabled()) {
                JUL.debug(key, e);
            }
        }

        // 读取外部资源文件中的国际化信息
        if (EXTERNAL != null) {
            try {
                String message = EXTERNAL.getString(key);
                if (message.length() > 0) {
                    return true;
                }
            } catch (Throwable e) {
                if (JUL.isDebugEnabled()) {
                    JUL.debug(key, e);
                }
            }
        }

        return false;
    }

    public static boolean existsScriptMessage(String key) {
        return existsMessage("script.command." + key.trim() + ".name");
    }

}