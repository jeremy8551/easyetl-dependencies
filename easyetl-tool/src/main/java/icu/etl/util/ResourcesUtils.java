package icu.etl.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import javax.naming.Context;
import javax.naming.InitialContext;

/**
 * 资源文件工具
 *
 * @author jeremy8551@qq.com
 */
public class ResourcesUtils {

    /** 外部资源配置文件路径 */
    public final static String PROPERTY_RESOURCE = ResourcesUtils.class.getPackage().getName().split("\\.")[0] + "." + ResourcesUtils.class.getPackage().getName().split("\\.")[1] + ".resource";

    /** 资源文件名（不包含扩展名） */
    public static String ResourceName = "Messages";

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
        } else {
            INTERNAL = bundle;
        }
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
            InputStream is = Ensure.notNull(ResourcesUtils.class.getResourceAsStream(uri));
            in = new BufferedReader(new InputStreamReader(is, CharsetName.UTF_8));

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

    /**
     * 在数字 {@code no} 前面加上符号0
     *
     * @param no 数字
     * @return 数字字符串
     */
    private static String toNumber(int no) {
        StringBuilder buf = new StringBuilder(3);
        for (int i = 0, loop = 3 - String.valueOf(no).length(); i < loop; i++) {
            buf.append('0');
        }
        return buf.append(no).toString();
    }

    public static boolean existsScriptMessage(String key) {
        return existsMessage("script.command." + key.trim() + ".name");
    }

    public static String getScriptStderrMessage(int no, Object... args) {
        String name = "script.message.stderr" + toNumber(no);
        return ResourcesUtils.getMessage(name, args);
    }

    public static String getScriptStdoutMessage(int no, Object... args) {
        String name = "script.message.stdout" + toNumber(no);
        return ResourcesUtils.getMessage(name, args);
    }

    public static String getOSMessage(int no, Object... args) {
        String name = "os.standard.output.msg" + toNumber(no);
        return ResourcesUtils.getMessage(name, args);
    }

//    public static String getTelnetMessage(int no, Object... args) {
//        String name = "telnet.standard.output.msg" + toNumber(no, 3);
//        return ResourcesUtils.getMessage(name, args);
//    }

    public static String getSSH2JschMessage(int no, Object... args) {
        String name = "ssh2.jsch.standard.output.msg" + toNumber(no);
        return ResourcesUtils.getMessage(name, args);
    }

//    public static String getSSH2GanymedMessage(int no, Object... args) {
//        String name = "ssh2.ganymed.standard.output.msg" + toNumber(no, 3);
//        return ResourcesUtils.getMessage(name, args);
//    }

    public static String getTimerMessage(int no, Object... args) {
        String name = "timer.standard.output.msg" + toNumber(no);
        return ResourcesUtils.getMessage(name, args);
    }

    public static String getIoxMessage(int no, Object... args) {
        String name = "io.standard.output.msg" + toNumber(no);
        return ResourcesUtils.getMessage(name, args);
    }

    public static String getExpressionMessage(int no, Object... args) {
        String name = "expression.standard.output.msg" + toNumber(no);
        return ResourcesUtils.getMessage(name, args);
    }

    public static String getCryptoMessage(int no, Object... args) {
        String name = "crypto.standard.output.msg" + toNumber(no);
        return ResourcesUtils.getMessage(name, args);
    }

    public static String getXmlMessage(int no, Object... args) {
        String name = "xml.standard.output.msg" + toNumber(no);
        return ResourcesUtils.getMessage(name, args);
    }

    public static String getTaskMessage(int no, Object... args) {
        String name = "task.standard.output.msg" + toNumber(no);
        return ResourcesUtils.getMessage(name, args);
    }

    public static String getDatabaseMessage(int no, Object... args) {
        String name = "database.standard.output.msg" + toNumber(no);
        return ResourcesUtils.getMessage(name, args);
    }

    public static String getDateMessage(int no, Object... args) {
        String name = "date.standard.output.msg" + toNumber(no);
        return ResourcesUtils.getMessage(name, args);
    }

//    public static String getWebSphereMessage(int no, Object... args) {
//        String name = "container.websphere.output.msg" + toNumber(no, 3);
//        return ResourcesUtils.getMessage(name, args);
//    }

    public static String getFtpApacheMessage(int no, Object... args) {
        String name = "ftp.apache.standard.output.msg" + toNumber(no);
        return ResourcesUtils.getMessage(name, args);
    }

    public static String getMailMessage(int no, Object... args) {
        String name = "mail.standard.output.msg" + toNumber(no);
        return ResourcesUtils.getMessage(name, args);
    }

    public static String getClassMessage(int no, Object... args) {
        String name = "class.standard.output.msg" + toNumber(no);
        return ResourcesUtils.getMessage(name, args);
    }

    public static String getIocMessage(int no, Object... args) {
        String name = "ioc.standard.output.msg" + toNumber(no);
        return ResourcesUtils.getMessage(name, args);
    }

    public static String getParamMessage(int no, Object... args) {
        String name = "param.standard.output.msg" + toNumber(no);
        return ResourcesUtils.getMessage(name, args);
    }

    public static String getExtractMessage(int no, Object... args) {
        String name = "extract.standard.output.msg" + toNumber(no);
        return ResourcesUtils.getMessage(name, args);
    }

    public static String getFilesMessage(int no, Object... args) {
        String name = "file.standard.output.msg" + toNumber(no);
        return ResourcesUtils.getMessage(name, args);
    }

    public static String getCommonMessage(int no, Object... args) {
        String name = "commons.standard.output.msg" + toNumber(no);
        return ResourcesUtils.getMessage(name, args);
    }

    public static String getDataSourceMessage(int no, Object... args) {
        String name = "dataSource.standard.output.msg" + toNumber(no);
        return ResourcesUtils.getMessage(name, args);
    }

    public static String getIncrementMessage(int no, Object... args) {
        String name = "increment.standard.output.msg" + toNumber(no);
        return ResourcesUtils.getMessage(name, args);
    }

    public static String getLoadMessage(int no, Object... args) {
        String name = "load.standard.output.msg" + toNumber(no);
        return ResourcesUtils.getMessage(name, args);
    }

//    public static String getScriptMessage(int no, Object... args) {
//        String name = "script.standard.output.msg" + toNumber(no, 3);
//        return ResourcesUtils.getMessage(name, args);
//    }

}