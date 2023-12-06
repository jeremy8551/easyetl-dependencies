package icu.etl.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * 为了在JDK5中能够支持SPI机制增加的类
 *
 * @author jeremy8551@qq.com
 * @createtime 2023/11/29
 */
public class SPI<E> {

    /**
     * 加载接口对应的实现类
     *
     * @param <E>     接口或类
     * @param loader  类加载器, 不能为null
     * @param service 接口或类，不能为null
     * @return 实现类集合
     */
    public static <E> List<E> load(ClassLoader loader, Class<E> service) {
        SPI<E> spi = new SPI<E>(service, loader);
        Iterator<E> it = spi.iterator();
        List<E> list = new ArrayList<E>();
        while (it.hasNext()) {
            list.add(it.next());
        }
        return list;
    }

    /** 类或接口 */
    private final Class<E> service;

    /** 类名与其实例对象的映射，按实例化顺序 */
    private final LinkedHashMap<String, E> providers;

    /** 延迟查找迭代器 */
    private final LazyIterator<E> iterator;

    private SPI(Class<E> service, ClassLoader classLoader) {
        this.service = Ensure.notNull(service);
        ClassLoader loader = Ensure.notNull(classLoader);
        this.providers = new LinkedHashMap<String, E>();
        this.iterator = new SPI.LazyIterator<E>(this.service, loader, this.providers);
    }

    private Iterator<E> iterator() {
        return new InstanceIterator<E>(providers.entrySet().iterator(), this.iterator);
    }

    private static class InstanceIterator<E> implements Iterator<E> {
        private final Iterator<Map.Entry<String, E>> knownProviders;
        private final LazyIterator<E> iterator;

        public InstanceIterator(Iterator<Map.Entry<String, E>> knownProviders, LazyIterator<E> iterator) {
            this.knownProviders = knownProviders;
            this.iterator = iterator;
        }

        public boolean hasNext() {
            if (this.knownProviders.hasNext()) {
                return true;
            } else {
                return this.iterator.hasNext();
            }
        }

        public E next() {
            if (this.knownProviders.hasNext()) {
                return this.knownProviders.next().getValue();
            } else {
                return this.iterator.next();
            }
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * 延迟加载迭代器
     */
    private static class LazyIterator<E> implements Iterator<E> {
        private final Class<E> service;
        private final ClassLoader loader;
        private Enumeration<URL> configs = null;
        private Iterator<String> pending = null;
        private String nextName = null;
        private final LinkedHashMap<String, E> providers;

        private LazyIterator(Class<E> service, ClassLoader loader, LinkedHashMap<String, E> providers) {
            this.service = service;
            this.loader = loader;
            this.providers = providers;
        }

        public boolean hasNext() {
            if (this.nextName != null) {
                return true;
            }

            // 初始化
            if (this.configs == null) {
                try {
                    String fullName = "META-INF/services/" + this.service.getName();
                    if (this.loader == null) {
                        this.configs = ClassLoader.getSystemResources(fullName);
                    } else {
                        this.configs = this.loader.getResources(fullName);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(service.getName() + ": Error locating configuration files", e);
                }
            }

            while ((this.pending == null) || !this.pending.hasNext()) {
                if (!this.configs.hasMoreElements()) {
                    return false;
                } else {
                    this.pending = this.parse(this.service, this.configs.nextElement());
                }
            }

            this.nextName = this.pending.next();
            return true;
        }

        private Iterator<String> parse(Class<?> service, URL url) throws Error {
            InputStream is = null;
            BufferedReader in = null;
            ArrayList<String> names = new ArrayList<String>();
            try {
                is = url.openStream();
                in = new BufferedReader(new InputStreamReader(is, CharsetName.UTF_8));
                for (int lineno = 1; (lineno = this.parseLine(in, lineno, names)) >= 0; ) ;
                return names.iterator();
            } catch (IOException x) {
                throw new RuntimeException(service.getName() + ": Error reading configuration file", x);
            } finally {
                IO.close(in, is);
            }
        }

        private int parseLine(BufferedReader in, int lineno, List<String> names) throws IOException, Error {
            String line = in.readLine();
            if (line == null) {
                return -1;
            }

            // 注释
            int index = line.indexOf('#');
            if (index >= 0) {
                line = line.substring(0, index);
            }
            line = StringUtils.trimBlank(line, ';');

            int length = line.length();
            if (length != 0) {
                if (!this.providers.containsKey(line) && !names.contains(line)) {
                    names.add(line);
                }
            }
            return lineno + 1;
        }

        public E next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }

            String className = this.nextName;
            this.nextName = null;
            Class<?> cls = ClassUtils.loadClass(className, false, this.loader);

            if (this.service.isAssignableFrom(cls)) {
                E obj = this.service.cast(ClassUtils.newInstance(cls));
                this.providers.put(className, obj);
                return obj;
            } else {
                throw new UnsupportedOperationException(service.getName() + ": " + "Provider " + className + " not a subtype");
            }
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public String toString() {
        return SPI.class.getSimpleName() + "[" + this.service.getName() + "]";
    }

}

