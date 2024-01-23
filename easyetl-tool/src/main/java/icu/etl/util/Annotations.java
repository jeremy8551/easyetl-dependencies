package icu.etl.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 注解帮助类
 *
 * @author jeremy8551@qq.com
 * @createtime 2011-05-24
 */
public class Annotations {

    /**
     * 返回方法上所有注解信息
     *
     * @param <E>            泛型类型
     * @param method         方法
     * @param annotationName 注解类名
     * @return 注解集合
     */
    @SuppressWarnings("unchecked")
    public static <E> List<E> getAnnotations(Method method, String annotationName) {
        if (method == null) {
            throw new NullPointerException();
        }

        List<E> list = new ArrayList<E>();
        Annotation[] array = method.getAnnotations();
        for (Annotation anno : array) {
            if (anno != null && anno.annotationType().getName().equals(annotationName)) {
                list.add((E) anno);
            }
        }
        return list;
    }

    /**
     * 返回类上所有方法上的注解信息
     *
     * @param <E>            注解类型
     * @param cls            类信息
     * @param annotationName 注解类名
     * @return 注解集合
     */
    @SuppressWarnings("unchecked")
    public static <E> List<E> getAnnotations(Class<?> cls, String annotationName) {
        if (cls == null) {
            throw new NullPointerException();
        }

        List<E> list = new ArrayList<E>();
        Method[] methods = cls.getMethods();
        for (Method method : methods) {
            Annotation[] array = method.getAnnotations();
            for (Annotation anno : array) {
                if (anno != null && anno.annotationType().getName().equals(annotationName)) {
                    list.add((E) anno);
                }
            }
        }
        return list;
    }

}
