package icu.etl.util;

/**
 * 操作系统工具
 *
 * @author jeremy8551@qq.com
 * @createtime 2023-09-27
 */
public class OSUtils {

    /**
     * os名
     * <p>
     * return System.getProperty("os.name");
     * Windows XP
     *
     * @return 返回操作系统名
     */
    public static String getName() {
        return System.getProperty("os.name");
    }

    /**
     * 判断java虚拟机所在的操作系统是否是windows
     *
     * @return 返回true表示是windows系统 false表示不是windows系统
     */
    public static boolean isWindows() {
        return StringUtils.objToStr(System.getProperty("os.name")).toLowerCase().contains("windows");
    }

    /**
     * 判断java虚拟机所在操作系统是否是linux
     *
     * @return 返回true表示是linux系统 false表示不是linux系统
     */
    public static boolean isLinux() {
        return StringUtils.objToStr(System.getProperty("os.name")).toLowerCase().contains("linux");
    }

    /**
     * 苹果mac os
     *
     * @return 返回true表示是MacOS系统 false表示不是MacOS系统
     */
    public static boolean isMacOs() {
        return StringUtils.objToStr(System.getProperty("os.name")).equalsIgnoreCase("mac os");
    }

    /**
     * 苹果mac os x
     *
     * @return 返回true表示是MacOSX系统 false表示不是MacOSX系统
     */
    public static boolean isMacOsX() {
        return StringUtils.objToStr(System.getProperty("os.name")).equalsIgnoreCase("mac os x");
    }

    /**
     * ibm aix
     *
     * @return 返回true表示是aix系统 false表示不是aix系统
     */
    public static boolean isAix() {
        return StringUtils.objToStr(System.getProperty("os.name")).toLowerCase().equals("aix");
    }

}
