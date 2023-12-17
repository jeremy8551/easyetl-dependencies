package icu.etl.util;

import java.io.File;

/**
 * JVM参数工具
 *
 * @author jeremy8551@qq.com
 * @createtime 2011-08-20
 */
public final class Settings {

    public Settings() {
    }

    /**
     * 返回java虚拟机当前的文件字符集
     * <p>
     * return System.getProperty("file.encoding");
     *
     * @return 属性值
     */
    public static String getFileEncoding() {
        return System.getProperty("file.encoding");
    }

    /**
     * 影响文件名字符集
     *
     * @return 属性值
     */
    public static String getFilenameEncoding() {
        return System.getProperty("sun.jnu.encoding");
    }

    /**
     * jvm版本
     * <p>
     * return System.getProperty("java.vm.version");
     * 1.5.0_22-b03
     *
     * @return 属性值
     */
    public static String getJavaVmVersion() {
        return System.getProperty("java.vm.version");
    }

    /**
     * jvm供应商
     * <p>
     * return System.getProperty("java.vm.vendor");
     * Sun Microsystems Inc.
     *
     * @return 属性值
     */
    public static String getJavaVmVendor() {
        return System.getProperty("java.vm.vendor");
    }

    /**
     * jvm名
     * <p>
     * return System.getProperty("java.vm.name");
     * Java HotSpot(TM) Client VM
     *
     * @return 属性值
     */
    public static String getJavaVmName() {
        return System.getProperty("java.vm.name");
    }

    /**
     * jvm默认使用的字符集的类包
     * <p>
     * return System.getProperty("file.encoding.pkg");
     *
     * @return 返回字符串 sun.io
     */
    public static String getFileEncodingPkg() {
        return System.getProperty("file.encoding.pkg");
    }

    /**
     * 国家代码
     * <p>
     * return System.getProperty("user.country");
     * CN
     *
     * @return 属性值
     */
    public static String getUserCountry() {
        return System.getProperty("user.country");
    }

    /**
     * 语言代码
     * <p>
     * return System.getProperty("user.language");
     * zh
     *
     * @return 属性值
     */
    public static String getUserLanguage() {
        return System.getProperty("user.language");
    }

    /**
     * 时区
     * <p>
     * return System.getProperty("user.timezone");
     * / Asia/Shanghai
     *
     * @return 属性值
     */
    public static String getUserTimezone() {
        return System.getProperty("user.timezone");
    }

    /**
     * JAVA_HOME 参数值
     * <p>
     * return System.getProperty("java.home");
     * C:\Program Files (x86)\Java\jdk1.5.0_22\jre
     *
     * @return 属性值
     */
    public static File getJavaHome() {
        return new File(System.getProperty("java.home"));
    }

    /**
     * 返回运行 java 命令的目录
     *
     * @return 属性值
     */
    public static File getUserDir() {
        return new File(System.getProperty("user.dir"));
    }

    /**
     * user.home 参数值
     * <p>
     * return System.getProperty("user.home");
     * C:\Users\etl
     *
     * @return 属性值
     */
    public static File getUserHome() {
        return new File(System.getProperty("user.home"));
    }

    /**
     * user.name 参数值
     * JVM虚拟机所在操作系统用户名
     *
     * @return 属性值
     */
    public static String getUserName() {
        return System.getProperty("user.name");
    }

    /**
     * 返回临时文件目录
     *
     * @return 目录
     */
    public static String getTempDir() {
        return System.getProperty("java.io.tmpdir");
    }

    /**
     * zh_CN
     *
     * @return 属性值
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
     * 返回当前Java虚拟机启动命令
     * com.ibm.wsspi.bootstrap.WSPreLauncher -nosplash -application com.ibm.ws.bootstrap.WSLauncher com.ibm.ws.runtime.WsServer /was/IBM/WebSphere/AppServer/profiles/AppSrv01/config LocalhostNode01Cell LocalhostNode01 server1
     *
     * @return 属性值
     */
    public static String getJavaCommand() {
        String value = System.getProperty("sun.java.command");
        return value == null ? null : value.trim();
    }

}
