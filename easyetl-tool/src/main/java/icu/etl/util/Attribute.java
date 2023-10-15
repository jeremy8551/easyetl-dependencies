package icu.etl.util;

/**
 * 属性接口
 *
 * @param <E>
 * @author jeremy8551@qq.com
 * @createtime 2021-05-27
 */
public interface Attribute<E> {

    /**
     * 判断是否存在属性
     *
     * @param key 属性名（大小写不敏感）
     * @return
     */
    boolean contains(String key);

    /**
     * 保存属性
     *
     * @param key   属性名（大小写不敏感）
     * @param value 属性值
     */
    void setAttribute(String key, E value);

    /**
     * 返回属性值
     *
     * @param key 属性名（大小写不敏感）
     * @return
     */
    E getAttribute(String key);

}
