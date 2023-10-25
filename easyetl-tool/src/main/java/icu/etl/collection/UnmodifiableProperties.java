package icu.etl.collection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import icu.etl.util.CollectionUtils;

/**
 * 不可修改的 Properties 集合 <br>
 *
 * @author jeremy8551@qq.com
 */
public class UnmodifiableProperties extends Properties implements Cloneable {
    private final static long serialVersionUID = 1L;
    private Properties obj;

    public UnmodifiableProperties(Properties defaults) {
        if (defaults == null) {
            throw new NullPointerException();
        }
        this.obj = defaults;
    }

    public String getProperty(String key, String defaultValue) {
        return obj.getProperty(key, defaultValue);
    }

    public String getProperty(String key) {
        return obj.getProperty(key);
    }

    public void list(PrintStream out) {
        obj.list(out);
    }

    public void list(PrintWriter out) {
        obj.list(out);
    }

    public void load(InputStream inStream) throws IOException {
        throw new UnsupportedOperationException();
    }

    public void loadFromXML(InputStream in) {
        throw new UnsupportedOperationException();
    }

    public Enumeration<?> propertyNames() {
        return obj.propertyNames();
    }

    @SuppressWarnings("deprecation")
    public void save(OutputStream out, String comments) {
        this.obj.save(out, comments);
    }

    public Object setProperty(String key, String value) {
        throw new UnsupportedOperationException();
    }

    public void store(OutputStream out, String comments) throws IOException {
        this.obj.store(out, comments);
    }

    public void storeToXML(OutputStream os, String comment, String encoding) throws IOException {
        this.obj.storeToXML(os, comment, encoding);
    }

    public void storeToXML(OutputStream os, String comment) throws IOException {
        this.obj.storeToXML(os, comment);
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public Properties clone() {
        Properties obj = new Properties();
        obj.putAll(this.obj);
        return obj;
    }

    public boolean contains(Object value) {
        return obj.contains(value);
    }

    public boolean containsKey(Object key) {
        return obj.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return obj.containsValue(value);
    }

    public Enumeration<Object> elements() {
        return obj.elements();
    }

    public Set<Map.Entry<Object, Object>> entrySet() {
        return Collections.unmodifiableSet(obj.entrySet());
    }

    public boolean equals(Object o) {
        return obj.equals(o);
    }

    public Object get(Object key) {
        return obj.get(key);
    }

    public int hashCode() {
        return obj.hashCode();
    }

    public boolean isEmpty() {
        return obj.isEmpty();
    }

    public Enumeration<Object> keys() {
        return obj.keys();
    }

    public Set<Object> keySet() {
        return Collections.unmodifiableSet(obj.keySet());
    }

    public Object put(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    public void putAll(Map<? extends Object, ? extends Object> t) {
        throw new UnsupportedOperationException();
    }

    public Object remove(Object key) {
        throw new UnsupportedOperationException();
    }

    public int size() {
        return obj.size();
    }

    public String toString() {
        return obj.toString();
    }

    public Collection<Object> values() {
        return Collections.unmodifiableCollection(obj.values());
    }

    public void load(Reader reader) throws IOException {
        throw new UnsupportedOperationException();
    }

    public void store(Writer writer, String comments) throws IOException {
        obj.store(new PropertiesWriter(writer, StandardCharsets.UTF_8.name()), comments);
    }

    class PropertiesWriter extends OutputStream {

        /** 字符输出流 */
        private Writer out;

        /** 字符集编码 */
        private String charsetName;

        public PropertiesWriter(Writer out, String charsetName) {
            super();
            this.out = out;
            this.charsetName = charsetName;
        }

        public void write(int b) throws IOException {
            this.out.write(b);
        }

        public void close() throws IOException {
            this.out.close();
        }

        public void flush() throws IOException {
            this.out.flush();
        }

        public void write(byte[] b, int off, int len) throws IOException {
            String str = new String(b, off, len, this.charsetName);
            this.out.write(str);
        }

        public void write(byte[] b) throws IOException {
            String str = new String(b, 0, b.length, this.charsetName);
            this.out.write(str);
        }
    }

    public Set<String> stringPropertyNames() {
        return Collections.unmodifiableSet(CollectionUtils.stringPropertyNames(obj));
    }

    // public synchronized Object getOrDefault(Object key, Object defaultValue) {
    // return super.getOrDefault(key, defaultValue);
    // }
    //
    // public synchronized void forEach(BiConsumer<? super Object, ? super Object> action) {
    // super.forEach(action);
    // }
    //
    // public synchronized void replaceAll(BiFunction<? super Object, ? super Object, ? extends Object> function) {
    // throw new UnsupportedOperationException();
    // }
    //
    // public synchronized Object putIfAbsent(Object key, Object value) {
    // throw new UnsupportedOperationException();
    // }
    //
    // public synchronized boolean remove(Object key, Object value) {
    // throw new UnsupportedOperationException();
    // }
    //
    // public synchronized boolean replace(Object key, Object oldValue, Object newValue) {
    // throw new UnsupportedOperationException();
    // }
    //
    // public synchronized Object replace(Object key, Object value) {
    // throw new UnsupportedOperationException();
    // }
    //
    // public synchronized Object computeIfAbsent(Object key, Function<? super Object, ? extends Object> mappingFunction) {
    // throw new UnsupportedOperationException();
    // }
    //
    // public synchronized Object computeIfPresent(Object key, BiFunction<? super Object, ? super Object, ? extends Object> remappingFunction) {
    // throw new UnsupportedOperationException();
    // }
    //
    // public synchronized Object compute(Object key, BiFunction<? super Object, ? super Object, ? extends Object> remappingFunction) {
    // throw new UnsupportedOperationException();
    // }
    //
    // public synchronized Object merge(Object key, Object value, BiFunction<? super Object, ? super Object, ? extends Object> remappingFunction) {
    // throw new UnsupportedOperationException();
    // }

}
