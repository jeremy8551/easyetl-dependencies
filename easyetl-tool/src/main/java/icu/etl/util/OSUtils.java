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
     * @return
     */
    public static String getName() {
        return System.getProperty("os.name");
    }

    /**
     * 判断java虚拟机所在的操作系统是否是windows
     *
     * @return
     */
    public static boolean isWindows() {
        return StringUtils.objToStr(System.getProperty("os.name")).toLowerCase().indexOf("windows") != -1;
    }

    /**
     * 判断java虚拟机所在操作系统是否是linux
     *
     * @return
     */
    public static boolean isLinux() {
        return StringUtils.objToStr(System.getProperty("os.name")).toLowerCase().indexOf("linux") != -1;
    }

    /**
     * 苹果mac os
     *
     * @return
     */
    public static boolean isMacOs() {
        return StringUtils.objToStr(System.getProperty("os.name")).toLowerCase().equals("mac os");
    }

    /**
     * 苹果mac os x
     *
     * @return
     */
    public static boolean isMacOsX() {
        return StringUtils.objToStr(System.getProperty("os.name")).toLowerCase().equals("mac os x");
    }

    /**
     * ibm aix
     *
     * @return
     */
    public static boolean isAix() {
        return StringUtils.objToStr(System.getProperty("os.name")).toLowerCase().equals("aix");
    }

}
