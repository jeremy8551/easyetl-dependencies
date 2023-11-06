package icu.etl.util;

import java.io.File;

/**
 * 输出当前JVM的所有配置信息 包括: JVM参数，环境变量
 *
 * @author jeremy8551@qq.com
 * @createtime 2011-08-20
 */
public final class Settings {

    public Settings() {
    }

    /**
     * 返回java虚拟机当前的文件字符集 <br>
     * <br>
     * return System.getProperty("file.encoding"); <br>
     *
     * @return
     */
    public static String getFileEncoding() {
        return System.getProperty("file.encoding");
    }

    /**
     * 影响文件名字符集
     *
     * @return
     */
    public static String getFilenameEncoding() {
        return System.getProperty("sun.jnu.encoding");
    }

    /**
     * jvm版本 <br>
     * <br>
     * return System.getProperty("java.vm.version"); <br>
     * 1.5.0_22-b03
     *
     * @return
     */
    public static String getJavaVmVersion() {
        return System.getProperty("java.vm.version");
    }

    /**
     * jvm供应商 <br>
     * <br>
     * return System.getProperty("java.vm.vendor"); <br>
     * Sun Microsystems Inc.
     *
     * @return
     */
    public static String getJavaVmVendor() {
        return System.getProperty("java.vm.vendor");
    }

    /**
     * jvm名 <br>
     * <br>
     * return System.getProperty("java.vm.name"); <br>
     * Java HotSpot(TM) Client VM
     *
     * @return
     */
    public static String getJavaVmName() {
        return System.getProperty("java.vm.name");
    }

    /**
     * jvm默认使用的字符集的类包 <br>
     * <br>
     * return System.getProperty("file.encoding.pkg"); <br>
     *
     * @return 返回字符串 sun.io
     */
    public static String getFileEncodingPkg() {
        return System.getProperty("file.encoding.pkg");
    }

    /**
     * 国家代码 <br>
     * <br>
     * return System.getProperty("user.country"); <br>
     * CN
     *
     * @return
     */
    public static String getUserCountry() {
        return System.getProperty("user.country");
    }

    /**
     * 语言代码 <br>
     * <br>
     * return System.getProperty("user.language"); <br>
     * zh
     *
     * @return
     */
    public static String getUserLanguage() {
        return System.getProperty("user.language");
    }

    /**
     * 时区 <br>
     * <br>
     * return System.getProperty("user.timezone"); <br>
     * / Asia/Shanghai
     *
     * @return
     */
    public static String getUserTimezone() {
        return System.getProperty("user.timezone");
    }

    /**
     * JAVA_HOME 参数值 <br>
     * <br>
     * return System.getProperty("java.home"); <br>
     * C:\Program Files (x86)\Java\jdk1.5.0_22\jre
     *
     * @return
     */
    public static File getJavaHome() {
        return new File(System.getProperty("java.home"));
    }

    /**
     * 返回运行 java 命令的目录
     *
     * @return
     */
    public static File getUserDir() {
        return new File(System.getProperty("user.dir"));
    }

    /**
     * user.home 参数值 <br>
     * <br>
     * return System.getProperty("user.home"); <br>
     * C:\Users\etl
     *
     * @return
     */
    public static File getUserHome() {
        return new File(System.getProperty("user.home"));
    }

    /**
     * user.name 参数值 <br>
     * JVM虚拟机所在操作系统用户名
     *
     * @return
     */
    public static String getUserName() {
        return System.getProperty("user.name");
    }

    /**
     * zh_CN
     *
     * @return
     */
    public static String getLang() {
        StringBuilder buf = new StringBuilder(10);
        buf.append(System.getProperty("user.language")); // zh
        String country = System.getProperty("user.country"); // CN
        if (country != null && country.length() > 0) {
            buf.append('_');
            buf.append(country);
        }
        return buf.toString();
    }

    /**
     * 返回当前Java虚拟机启动命令 <br>
     * com.ibm.wsspi.bootstrap.WSPreLauncher -nosplash -application com.ibm.ws.bootstrap.WSLauncher com.ibm.ws.runtime.WsServer /was/IBM/WebSphere/AppServer/profiles/AppSrv01/config LocalhostNode01Cell LocalhostNode01 server1
     *
     * @return
     */
    public static String getJavaCommand() {
        String value = System.getProperty("sun.java.command");
        return value == null ? null : value.trim();
    }

    /**
     * 返回工程的 groupId
     *
     * @return 包名
     */
    public static String getGroupID() {
        String packageName = Settings.class.getPackage().getName();
        String[] array = packageName.split("\\.");
        StringBuilder buf = new StringBuilder(packageName.length());
        for (int i = 0, length = Math.min(2, array.length); i < length; ) {
            buf.append(array[i]);
            if (++i < length) {
                buf.append('.');
            }
        }
        return buf.toString();
    }

}
