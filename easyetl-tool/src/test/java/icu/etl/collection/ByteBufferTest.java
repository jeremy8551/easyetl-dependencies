package icu.etl.collection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import icu.etl.util.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ByteBufferTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testByteBufferInt() {
        ByteBuffer b = new ByteBuffer(10);
        assertEquals(10, b.getBytes().length);
    }

    @Test
    public void testByteBufferIntInt() throws IOException {
        ByteBuffer b = new ByteBuffer(10, 2);
        b.append("0123456789");
        b.append("0");
        assertEquals(12, b.getBytes().length);
    }

    @Test
    public void testByteBufferIntString() throws IOException {
        ByteBuffer b = new ByteBuffer(10, "UTF-8");
        b.append("0123456789");
        b.append("中文");
        assertEquals("0123456789中文", b.toString());
    }

    @Test
    public void testByteBufferIntIntString() throws IOException {
        ByteBuffer b = new ByteBuffer(10, 5, "UTF-8");
        b.append("0123456789");
        b.append("中文");
        assertEquals("0123456789中文", b.toString());
        assertEquals(16, b.getBytes().length);
    }

    @Test
    public void testReset() throws IOException {
        ByteBuffer b = new ByteBuffer(10, 5, "UTF-8");
        b.append("0123456789");
        b.append("中文");
        b.restore(20);
        assertEquals(20, b.getBytes().length);
    }

    @Test
    public void testValue() throws IOException {
        ByteBuffer b = new ByteBuffer(10, 5, "UTF-8");
        b.append("0123456789");
        b.append("中文");
        assertEquals("0123456789中文", StringUtils.toString(b.value(), "UTF-8"));
    }

    @Test
    public void testGetBytes() throws IOException {
        ByteBuffer b = new ByteBuffer(10, 2, "UTF-8");
        b.append("0123456789");
        b.append("0");
//		System.out.println(ST.toString(b.getBytes())); // 拼写字符串 将字符串写入到字节缓冲区中
        assertEquals("01234567890", new String(b.getBytes(), 0, 11));
        assertEquals(12, b.getBytes().length);
    }

    @Test
    public void testExpandValueArray() {
        ByteBuffer b = new ByteBuffer(10, 2, "UTF-8");
        b.expandValueArray(20);
        assertEquals(20, b.getBytes().length);
    }

    @Test
    public void testAddByteArrays() {
        ByteBuffer b = new ByteBuffer(10, 2, "UTF-8");
        byte[] array = StringUtils.toBytes("中文测试数据", "UTF-8");
        b.addByteArrays(array, 0, array.length);
        assertEquals("中文测试数据", b.toString());
    }

    @Test
    public void testSetByte() throws IOException {
        ByteBuffer b = new ByteBuffer(10, 2, "UTF-8");
        b.append("0123456789");
        b.set(0, (byte) 'a');
        b.set(9, (byte) 'b');
        assertEquals("a12345678b", b.toString());
    }

    @Test
    public void testByteAt() throws IOException {
        ByteBuffer b = new ByteBuffer(10, 2, "UTF-8");
        b.append("0123456789");
        assertEquals('0', b.byteAt(0));
        assertEquals('9', b.byteAt(9));
    }

    @Test
    public void testInsert() throws IOException {
        ByteBuffer b = new ByteBuffer(10, 2, "UTF-8");
        b.append("0123456789");
        byte[] array = StringUtils.toBytes("0123456789", "UTF-8");
        b.insert(0, array, 0, array.length);
        assertEquals("01234567890123456789", b.toString());
    }

    @Test
    public void testAppendString() throws IOException {
        ByteBuffer b = new ByteBuffer(10, 2, "UTF-8");
        b.append("0123456789");
        assertEquals("0123456789", b.toString());
    }

    @Test
    public void testAppendStringString() throws IOException {
        ByteBuffer b = new ByteBuffer(10, 2, "UTF-8");
        b.append("中文数据", "UTF-8");
        assertEquals("中文数据", b.toString());
    }

    @Test
    public void testAppendByteBuffer() throws IOException {
        ByteBuffer b = new ByteBuffer(10, 2, "UTF-8");
        b.append("中文数据", "UTF-8");

        ByteBuffer c = new ByteBuffer(10, 2, "UTF-8");
        c.append("0");
        c.append(b);

        assertEquals("0中文数据", c.toString());
    }

    @Test
    public void testAppendByteArray() throws IOException {
        ByteBuffer b = new ByteBuffer(10, 2, "UTF-8");
        b.append("0123456789");
        b.append(StringUtils.toBytes("中文数据", "UTF-8"));
        assertEquals("0123456789中文数据", b.toString());
    }

    @Test
    public void testAppendByteArrayIntInt() throws IOException {
        ByteBuffer b = new ByteBuffer(10, 2, "UTF-8");
        b.append("0123456789");
        b.append(StringUtils.toBytes("中文数据", "UTF-8"), 0, 12);
        b.append(StringUtils.toBytes("中文数据", "UTF-8"), 3, 6);
        assertEquals("0123456789中文数据文数", b.toString());
    }

    @Test
    public void testAppendChar() throws IOException {
        ByteBuffer b = new ByteBuffer(10, 2, "UTF-8");
        b.append('0');
        b.append('1');
        assertEquals("01", b.toString());
    }

    @Test
    public void testAppendCharString() throws IOException {
        ByteBuffer b = new ByteBuffer(10, 2, "UTF-8");
        b.append('0');
        b.append('1');
        b.append('中', "UTF-8");
        assertEquals("01中", b.toString());
    }

    @Test
    public void testAppendByte() throws IOException {
        ByteBuffer b = new ByteBuffer(10, 2, "UTF-8");
        b.append('0');
        b.append((byte) '1');
        Assert.assertTrue(b.toString().equals("01"));
    }

    @Test
    public void testAppendInputStreamInt() throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(StringUtils.toBytes("中文测试数据", "UTF-8"));

        ByteBuffer b = new ByteBuffer(10, 2, "UTF-8");
        b.destory();
        b.append("0123");
        b.append(is, 3);
        assertEquals("0123中", b.toString());
    }

    @Test
    public void testAppendInputStream() throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(StringUtils.toBytes("中文测试数据", "UTF-8"));

        ByteBuffer b = new ByteBuffer(10, 2, "UTF-8");
        b.destory();
        b.append("0123");
        b.append(is);
        assertEquals("0123中文测试数据", b.toString());
    }

    @Test
    public void testAppendInputStreamByteArray() throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(StringUtils.toBytes("中文测试数据", "UTF-8"));

        byte[] buf = new byte[2];
        ByteBuffer b = new ByteBuffer(10, 2, "UTF-8");
        b.destory();
        b.append("0123");
        b.append(is, buf);
        assertEquals("0123中文测试数据", b.toString());
    }

    @Test
    public void testWriteOutputStream() throws IOException {
        ByteBuffer b = new ByteBuffer(10, 2, "UTF-8");
        b.append("0123456789");
        b.append(StringUtils.toBytes("中文数据", "UTF-8"));
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        b.write(os); // 输出信息
        assertEquals("0123456789中文数据", os.toString("UTF-8"));
    }

    @Test
    public void testWriteOutputStreamIntInt() throws IOException {
        ByteBuffer b = new ByteBuffer(10, 2, "UTF-8");
        b.append("0123456789");
        b.append(StringUtils.toBytes("中文数据", "UTF-8"));
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        b.write(os, 10, 6);
        assertEquals(os.toString("UTF-8"), "中文");
    }

    @Test
    public void testGetInputStream() throws IOException {
        ByteBuffer b = new ByteBuffer(10, 2, "UTF-8");
        b.append("0123456789");
        b.append(StringUtils.toBytes("中文数据", "UTF-8"));
        InputStream is = b.getInputStream();

        byte[] buf = new byte[4];
        is.read(buf, 0, buf.length);
        assertEquals("0123", new String(buf));
    }

    @Test
    public void testGetIncrCapacity() {
        ByteBuffer b = new ByteBuffer(10, 2, "UTF-8");
        assertEquals(2, b.getIncrCapacity());
    }

    @Test
    public void testSetIncrCapacity() {

    }

    @Test
    public void testClear() throws IOException {
        ByteBuffer b = new ByteBuffer(10, 2, "UTF-8");
        b.append("0123456789");
        b.clear();
        Assert.assertTrue(b.isEmpty());
    }

    @Test
    public void testDestory() throws IOException {
        ByteBuffer b = new ByteBuffer(10, 2, "UTF-8");
        b.append("0123456789");
        b.destory();
        Assert.assertTrue(b.isEmpty());
    }

    @Test
    public void testResize() throws IOException {
        ByteBuffer b = new ByteBuffer(10, 2, "UTF-8");
        b.append("01234567890");
        b.resize();
        assertEquals(11, b.getBytes().length);
    }

    @Test
    public void testLength() throws IOException {
        ByteBuffer b = new ByteBuffer(10, 2, "UTF-8");
        b.append("01234567890");
        assertEquals(11, b.length());
    }

    @Test
    public void testToStringString() throws IOException {
        ByteBuffer b = new ByteBuffer(10, 2, "GBK");
        b.append("0123456789中文");
        assertEquals("0123456789中文", b.toString("GBK"));
    }

    @Test
    public void testToString() throws IOException {
        ByteBuffer b = new ByteBuffer(10, 2, "GBK");
        b.append("0123456789中文");
        assertEquals("0123456789中文", b.toString());
    }

    @Test
    public void testtoString() throws IOException {
        ByteBuffer b = new ByteBuffer(10, 2, "GBK");
        b.append("0123456789中文");
        assertEquals("0123456789中文", b.toString());
    }

    @Test
    public void testEncodingToStringString() throws IOException {
        ByteBuffer b = new ByteBuffer(10, 2, "GBK");
        b.append("0123456789中文");
        assertEquals("0123456789中文", b.toString("gbk"));
    }

    @Test
    public void testGetCharsetName() {
        ByteBuffer b = new ByteBuffer(10, 2, "GBK");
        Assert.assertTrue(b.getCharsetName().equalsIgnoreCase("gbk"));
    }

    @Test
    public void testSetCharsetName() {
        ByteBuffer b = new ByteBuffer(10, 2, "GBK");
        b.setCharsetName("utf-8");
        Assert.assertTrue(b.getCharsetName().equalsIgnoreCase("utf-8"));
    }

    @Test
    public void testSubbytes() throws IOException {
        ByteBuffer b = new ByteBuffer(10, 2, "GBK");
        b.append("0123456789");
        assertEquals("1234567", b.subbytes(1, 8).toString());
    }

    @Test
    public void testIsEmpty() throws IOException {
        ByteBuffer b = new ByteBuffer(10, 2, "GBK");
        b.append("0123456789");
        Assert.assertFalse(b.isEmpty());
        b.clear();
        Assert.assertTrue(b.isEmpty());
    }

}
