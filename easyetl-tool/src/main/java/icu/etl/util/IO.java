package icu.etl.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;
import java.util.Set;

import icu.etl.collection.Throwables;

/**
 * IO工具
 */
public class IO {

    /** 输入流缓存的默认长度，单位字符 */
    public final static String PROPERTY_READBUF = IO.class.getPackage().getName().split("\\.")[0] + "." + IO.class.getPackage().getName().split("\\.")[1] + ".readbuf";

    /** 输入流的缓冲区长度，单位: 字符 */
    public static int READER_BUFFER_SIZE = 1024 * 10;

    /** 字节输入流的缓冲区长度，单位: 字节 */
    public static int BYTES_BUFFER_SIZE = 1024 * 10;

    /** 文件输入流的缓冲区长度，单位: 50M的字符 */
    public static int FILE_BYTES_BUFFER_SIZE = getReaderBufferSize();

    public IO() {
    }

    /**
     * 返回输入流缓存的默认长度，单位字符
     *
     * @return 默认长度，单位：字符
     */
    private static int getReaderBufferSize() {
        String length = System.getProperty(PROPERTY_READBUF);
        if (length == null || length.length() == 0) {
            return 1024 * 1024 * 10; // 10M
        } else {
            return Integer.parseInt(length);
        }
    }

    /**
     * 通过将所有已缓冲输出写入基础流来刷新此流。
     *
     * @param array 数组
     */
    public static void flush(Flushable... array) {
        if (array == null || array.length == 0) {
            return;
        }

        Throwables throwables = new Throwables();
        for (Flushable obj : array) {
            if (obj != null) {
                try {
                    obj.flush();
                } catch (Throwable e) {
                    throwables.add(e.getLocalizedMessage(), e);
                }
            }
        }

        if (throwables.notEmpty()) {
            throw throwables;
        }
    }

    /**
     * 通过将所有已缓冲输出写入基础流来刷新此流。
     *
     * @param array 数组
     */
    public static void flushQuiet(Flushable... array) {
        if (array == null || array.length == 0) {
            return;
        }

        for (Flushable obj : array) {
            if (obj != null) {
                try {
                    obj.flush();
                } catch (Throwable e) {
                    if (JUL.isWarnEnabled()) {
                        JUL.warn(e.getLocalizedMessage(), e);
                    }
                }
            }
        }
    }

    /**
     * 通过将所有已缓冲输出写入基础流来刷新此流。
     *
     * @param array 数组
     */
    public static void flushQuietly(Flushable... array) {
        if (array == null || array.length == 0) {
            return;
        }

        for (Flushable obj : array) {
            if (obj != null) {
                try {
                    obj.flush();
                } catch (Throwable e) {
                    if (JUL.isDebugEnabled()) {
                        JUL.debug(e.getLocalizedMessage(), e);
                    }
                }
            }
        }
    }

    /**
     * 执行 close() 方法
     * 遍历所有 Closeable 对象并尝试执行 close 方法
     * 如果其中存在一个 close 函数报错, 等所有对象执行完 close 方法后再抛出异常
     *
     * @param array 数组
     */
    public static void close(Object... array) {
        if (array == null || array.length == 0) {
            return;
        }

        Throwables throwables = new Throwables();
        for (Object obj : array) {
            try {
                IO.close(obj);
            } catch (Throwable e) {
                throwables.add(e.getLocalizedMessage(), e);
            }
        }

        if (throwables.notEmpty()) {
            throw throwables;
        }
    }

    /**
     * 执行 close() 方法
     * 如果发生异常错误打印错误信息，但不抛出异常
     *
     * @param array 数组
     */
    public static void closeQuiet(Object... array) {
        if (array == null || array.length == 0) {
            return;
        }

        for (Object obj : array) {
            try {
                IO.close(obj);
            } catch (Throwable e) {
                if (JUL.isWarnEnabled()) {
                    JUL.warn(e.getLocalizedMessage(), e);
                }
            }
        }
    }

    /**
     * 执行 close() 方法
     * 如果发生异常错误不会打印错误信息，也不抛出异常
     *
     * @param array 数组
     */
    public static void closeQuietly(Object... array) {
        if (array == null || array.length == 0) {
            return;
        }

        for (Object obj : array) {
            try {
                IO.close(obj);
            } catch (Throwable e) {
                if (JUL.isDebugEnabled()) {
                    JUL.debug(e.getLocalizedMessage(), e);
                }
            }
        }
    }

    /**
     * 通过java反射机制执行对象中的 close() 函数
     *
     * @param obj 参数对象
     */
    private static void close(Object obj) {
        if (obj == null) {
            return;
        }

        try {
            if (obj instanceof Socket) {
                Socket socket = (Socket) obj;
                if (socket.isBound() && socket.isConnected() && !socket.isClosed()) {
                    if (!socket.isOutputShutdown()) {
                        socket.shutdownOutput();
                    }
                    if (!socket.isInputShutdown()) {
                        socket.shutdownInput();
                    }
                    socket.close();
                }
            } else if (obj instanceof Closeable) {
                ((Closeable) obj).close();
            } else if (obj instanceof Iterable) {
                IO.closeIterable((Iterable<?>) obj);
            } else if (obj instanceof Map<?, ?>) {
                IO.closeMap(obj);
            } else {
                IO.closeFunction(obj);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 调用 Map 集合中 value 对象中的 {@linkplain Closeable#close()} 接口
     *
     * @param obj 实例对象
     * @throws SecurityException        无权访问错误
     * @throws IllegalArgumentException 参数错误
     */
    private static void closeMap(Object obj) throws SecurityException, IllegalArgumentException {
        if (obj == null) {
            return;
        }

        Throwables throwables = new Throwables();
        Map<?, ?> map = (Map<?, ?>) obj;
        Set<?> keys = map.keySet();
        for (Object key : keys) {
            Object value = map.get(key);
            try {
                IO.closeFunction(value);
            } catch (Throwable e) {
                throwables.add(e.getLocalizedMessage(), e);
            }
        }

        if (throwables.notEmpty()) {
            throw throwables;
        }
    }

    /**
     * 通过反射调用参数迭代器中所有对象的 {@linkplain Closeable#close()} 接口
     *
     * @param it 遍历器
     */
    private static void closeIterable(Iterable<?> it) {
        if (it == null) {
            return;
        }

        Throwables throwables = new Throwables();
        for (Object obj : it) {
            try {
                IO.closeFunction(obj);
            } catch (Throwable e) {
                throwables.add(e.getLocalizedMessage(), e);
            }
        }

        if (throwables.notEmpty()) {
            throw throwables;
        }
    }

    /**
     * 通过反射调用参数对象中的 {@linkplain Closeable#close()} 接口
     *
     * @param obj 实例对象
     * @throws NoSuchMethodException     方法不存在
     * @throws SecurityException         无权访问错误
     * @throws IllegalAccessException    访问错误
     * @throws IllegalArgumentException  参数错误
     * @throws InvocationTargetException 底层方法抛出异常
     */
    private static void closeFunction(Object obj) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method method = obj.getClass().getMethod("close", (Class<?>[]) null);
        if (method != null) {
            method.invoke(obj, (Object[]) null);
        }
    }

    /**
     * 用 reader 参数初始化一个 BufferedReader 对象
     *
     * @param in 输入流
     * @return 如果 read 参数本身是 BufferedReader 对象，则强制转换后返回
     */
    public static BufferedReader getBufferedReader(Reader in) {
        return in instanceof BufferedReader ? (BufferedReader) in : new BufferedReader(in);
    }

    /**
     * 返回BufferedReader
     *
     * @param file        文件
     * @param charsetName 文件的字符集
     * @param buffer      缓冲区大小（字符）
     * @return 输入流
     * @throws IOException IO错误
     */
    public static BufferedReader getBufferedReader(File file, String charsetName, int buffer) throws IOException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(file), charsetName), buffer);
    }

    /**
     * 返回BufferedReader
     *
     * @param file        文件
     * @param charsetName 文件的字符集
     * @return 输入流
     * @throws IOException IO错误
     */
    public static BufferedReader getBufferedReader(File file, String charsetName) throws IOException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(file), charsetName));
    }

    /**
     * 返回 {@link FileWriter} 对象
     *
     * @param file        文件
     * @param charsetName 文件字符集编码
     * @param append      true表示追加方式写文件
     * @return 输出流
     * @throws IOException IO错误
     */
    public static OutputStreamWriter getFileWriter(File file, String charsetName, boolean append) throws IOException {
        return new OutputStreamWriter(new FileOutputStream(file, append), charsetName);
    }

    /**
     * 从输入流 in 中读取字节写入到输出流 out 中
     *
     * @param in  输入流（会自动关闭）
     * @param out 输出流（会自动关闭）
     * @return 返回总输出字节数
     * @throws IOException IO错误
     */
    public static long write(InputStream in, OutputStream out) throws IOException {
        if (in == null) {
            throw new NullPointerException();
        }
        if (out == null) {
            throw new NullPointerException();
        }

        try {
            long total = 0;
            byte[] array = new byte[IO.BYTES_BUFFER_SIZE];
            for (int len; (len = in.read(array)) != -1; ) {
                out.write(array, 0, len);
                out.flush();
                total += len;
            }
            return total;
        } finally {
            IO.close(in, out);
        }
    }

    /**
     * 从输入流中读取所有字符到缓冲区 buf 中
     *
     * @param in    输入流
     * @param buf   缓冲区
     * @param array 字符缓冲区
     * @return 读取字符的长度
     * @throws IOException 从输入流中读取字符发生错误
     */
    public static StringBuilder read(Reader in, StringBuilder buf, char... array) throws IOException {
        if (array == null || array.length == 0) {
            array = new char[IO.READER_BUFFER_SIZE];
        }

        for (int len; (len = in.read(array)) != -1; ) {
            buf.append(array, 0, len);
        }
        return buf;
    }

    /**
     * 将字符串 {@code buf} 写入到输出流 {@code out} 中
     *
     * @param out 输出流
     * @param buf 字符串
     * @return 写入的总字符数
     * @throws IOException 写入字符发生错误
     */
    public static long write(Writer out, StringBuilder buf) throws IOException {
        int length = buf.length();
        char[] array = new char[length];
        buf.getChars(0, length, array, 0);
        out.write(array, 0, length);
        return array.length;
    }

    /**
     * 从输入流中读取所有字节
     *
     * @param in 输入流
     * @return 字节数组
     * @throws IOException 从输入流中读取字节发生错误
     */
    public static byte[] read(InputStream in) throws IOException {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(in.available());
            byte[] array = new byte[IO.BYTES_BUFFER_SIZE]; // 缓冲区
            for (int length; (length = in.read(array)) != -1; ) {
                out.write(array, 0, length);
            }
            return out.toByteArray();
        } finally {
            IO.close(in);
        }
    }

    /**
     * 读取文件中的所有内容
     *
     * @param file 文件
     * @return 字节数组
     * @throws IOException 读取文件发生错误
     */
    public static byte[] read(File file) throws IOException {
        return IO.read(new FileInputStream(file));
    }

}
