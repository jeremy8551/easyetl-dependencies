package icu.etl.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 组件注解
 * IOC容器启动时会扫描带 {@linkplain EasyBean} 注解的类
 *
 * @author jeremy8551@qq.com
 * @createtime 2021-02-08
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EasyBean {

    /**
     * 组件管理模式
     *
     * @return true表示组件是单例模式, false表示是原型模式（每次生成的组件都是新创建的）
     */
    boolean singleton() default false;

    /**
     * 是否使用延迟加载模式
     *
     * @return true表示单例组件使用延迟加载模式
     */
    boolean lazy() default true;

    /**
     * 组件名称
     *
     * @return 种类信息
     */
    String name() default "";

    /**
     * 排序编号
     * 如果注册了多个同名的组件导致冲突时，容器使用排序编号最大组件
     *
     * @return 默认0，值越大权重越高
     */
    int priority() default 0;

    /**
     * 描述信息
     *
     * @return 描述信息
     */
    String description() default "";

}
