package icu.etl.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.Closeable;
import java.io.File;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import org.junit.Assert;
import org.junit.Test;

public class IOTest {

    @Test
    public void test1() throws IOException {
        String str = "rtgfhajsdgjfhsadl;jslkdaghilouyweqoirt测试sdfasdfsadf;lj";
        CharArrayReader in = new CharArrayReader(str.toCharArray());
        StringBuilder buf = IO.read(in, new StringBuilder(), new char[10]);
        Assert.assertEquals(str, buf.toString());
    }

    @Test
    public void test2() throws IOException {
        CharArrayWriter writer = new CharArrayWriter(10);
        String str = "rtgfhajsdgjfhsadl;jslkdaghilouyweqoirt测试sdfasdfsadf;lj";
        long len = IO.write(writer, new StringBuilder(str));
        Assert.assertEquals(len, str.length());
        Assert.assertEquals(str, writer.toString());
    }

    @Test
    public void test3() throws IOException {
        String str = "rtgfhajsdgjfhsadl;jslkdaghilouyweqoirt测试sdfasdfsadf;lj";
        String charsetName = StringUtils.CHARSET;
        byte[] srcArray = str.getBytes(charsetName);
        ByteArrayInputStream in = new ByteArrayInputStream(srcArray);
        byte[] array = IO.read(in);
        Assert.assertEquals(new String(srcArray, charsetName), new String(array, charsetName));
    }

    @Test
    public void test4() throws IOException {
        String str = "rtgfhajsdgjfhsadl;jslkdaghilouyweqoirt测试sdfasdfsadf;lj";
        String charsetName = StringUtils.CHARSET;
        File file = FileUtils.createTempfile(IOTest.class, "txt");
        FileUtils.write(file, charsetName, false, str);
        byte[] array = IO.read(file);
        Assert.assertEquals(new String(array, charsetName), str);
    }

    @Test
    public void testGetBufferedReaderReader() throws IOException {
        File file = FileUtils.createTempfile(IOTest.class, "txt");
        FileUtils.write(file, StringUtils.CHARSET, false, "ceshi");
        BufferedReader r = IO.getBufferedReader(file, StringUtils.CHARSET);
        Assert.assertTrue(r.readLine().equals("ceshi"));
    }

    @Test
    public void testGetBufferedReaderFile() throws IOException {
        File file = FileUtils.createTempfile(IOTest.class, "txt");
        FileUtils.write(file, StringUtils.CHARSET, false, "ceshi");
        BufferedReader r = IO.getBufferedReader(file, StringUtils.CHARSET);
        Assert.assertTrue(r.readLine().equals("ceshi"));
    }

    @Test
    public void testGetBufferedReaderFileString() throws IOException {
        File file = FileUtils.createTempfile(IOTest.class, "txt");
        FileUtils.write(file, StringUtils.CHARSET, false, "ceshi");
        BufferedReader r = IO.getBufferedReader(file, StringUtils.CHARSET);
        Assert.assertTrue(r.readLine().equals("ceshi"));
    }

    @Test
    public void testFlush() {
        IO.flushQuietly(new Flushable() {
            @Override
            public void flush() throws IOException {
                Assert.assertTrue(true);
            }
        });
    }

    @Test
    public void testFlushQuietly() {
        IO.flush(new Flushable() {
            @Override
            public void flush() throws IOException {
                Assert.assertTrue(true);
            }
        });
    }

    @Test
    public void testCloseQuietlyWriter() {
        IO.closeQuietly(new Writer() {

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
                Assert.assertTrue(true);
            }

            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
            }
        });
    }

    @Test
    public void testCloseQuietlyOutputStream() {
        IO.closeQuietly(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
            }

            @Override
            public void close() throws IOException {
                Assert.assertTrue(true);
            }

        });
    }

    @Test
    public void testCloseQuietlyReader() {
        IO.closeQuietly(new Reader() {

            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                return 0;
            }

            @Override
            public void close() throws IOException {
                Assert.assertTrue(true);
            }
        });
    }

    @Test
    public void testCloseQuietlyCloseable() {
        IO.closeQuietly(new Closeable() {
            @Override
            public void close() throws IOException {
                Assert.assertTrue(true);
            }
        });
    }

}
