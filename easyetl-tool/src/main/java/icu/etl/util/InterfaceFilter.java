package icu.etl.util;

public interface InterfaceFilter {

    /**
     * 过滤接口信息
     *
     * @param cls  接口的类信息
     * @param name 接口的全名
     * @return 返回true表示接受接口 false表示不接受接口
     */
    boolean accept(Class<?> cls, String name);
}
