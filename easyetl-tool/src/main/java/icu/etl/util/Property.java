package icu.etl.util;

/**
 * 属性信息
 *
 * @author jeremy8551@qq.com
 * @createtime 2013-09-30
 */
public class Property {

    /**
     * 属性名
     */
    protected String name;

    /**
     * 属性值
     */
    protected Object value;

    /**
     * 属性描述信息
     */
    protected String description;

    /**
     * 属性排序编号
     */
    protected int order;

    /**
     * 初始化
     */
    public Property() {
    }

    /**
     * 初始化
     *
     * @param name  属性名
     * @param value 属性值
     */
    public Property(String name, Object value) {
        if (name == null) {
            throw new NullPointerException();
        }

        this.name = name;
        this.value = value;
    }

    /**
     * 返回属性排序编号
     *
     * @return 排序编号
     */
    public int getOrder() {
        return order;
    }

    /**
     * 设置属性排序编号
     *
     * @param order 排序编号
     */
    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * 设置属性名
     *
     * @param name 属性名
     */
    public void setKey(String name) {
        if (name == null) {
            throw new NullPointerException();
        }

        this.name = name;
    }

    /**
     * 设置属性值
     *
     * @param value 属性值
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * 返回属性名
     *
     * @return 属性名
     */
    public String getKey() {
        return name;
    }

    /**
     * 返回属性值
     *
     * @return 属性值
     */
    @SuppressWarnings("unchecked")
    public <E> E getValue() {
        return (E) this.value;
    }

    /**
     * 将属性值转为字符串
     *
     * @return 字符串
     */
    public String getString() {
        return this.value == null ? null : this.value.toString();
    }

    /**
     * 返回属性描述信息
     *
     * @return 属性描述信息
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置属性描述信息
     *
     * @param description 属性描述信息
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 判断属性是否相等
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof Property)) {
            return false;
        }

        Property p = (Property) obj;
        if (this.value == null) {
            return p.getValue() == null && this.name.equals(p.getKey());
        } else {
            return this.name.equals(p.getKey()) && this.value.equals(p.getValue());
        }
    }

    public String toString() {
        return "Property [name=" + name + ", value=" + value + ", description=" + description + ", order=" + order + "]";
    }

}