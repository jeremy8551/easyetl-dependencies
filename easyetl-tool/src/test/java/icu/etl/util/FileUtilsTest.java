package icu.etl.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class FileUtilsTest {

    /**
     * 返回一个临时文件
     *
     * @return
     */
    public static File getFile() {
        return getFile(null);
    }

    /**
     * 使用指定用户名创建一个文件
     *
     * @param name
     * @return
     */
    public static File getFile(String name) {
        if (StringUtils.isBlank(name)) {
            name = FileUtils.getFilenameRandom("testfile", "_tmp") + ".txt";
        }

        File dir = new File(FileUtils.getTempDir(FileUtilsTest.class), "单元测试");
        if (!dir.exists() && !dir.mkdirs()) {
            throw new RuntimeException("创建目录 " + dir.getAbsolutePath() + " 失败!");
        } else {
            return new File(dir, name); // 返回一个临时文件信息
        }
    }

    /**
     * 执行单元测试前建立必要测试目录
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        Ensure.isTrue(FileUtils.getTempDir(FileUtilsTest.class).exists() && FileUtils.getTempDir(FileUtilsTest.class).isDirectory());
    }

    @Test
    public void testfindFile() throws IOException {
        File root = FileUtils.getTempDir(FileUtilsTest.class);
        File d0 = new File(root, "findfile");
        d0.mkdirs();

        File d1 = new File(d0, "d1");
        d1.mkdirs();

        File f11 = new File(d1, "20200102.txt");
        f11.createNewFile();

        File d2 = new File(d0, "d2");
        d2.mkdirs();

        File f21 = new File(d2, "20200102.txt");
        f21.createNewFile();

        File d3 = new File(d2, "d3");
        d3.mkdirs();

        File f22 = new File(d3, "20200102.txt");
        f22.createNewFile();

        List<File> fs = FileUtils.findFile(d0, "20200102.txt");
        assertTrue(fs.size() == 3);
        assertTrue(fs.indexOf(f11) != -1);
        assertTrue(fs.indexOf(f21) != -1);
        assertTrue(fs.indexOf(f22) != -1);

        fs = FileUtils.findFile(d0, "20200102[^ ]{4}");
        assertTrue(fs.size() == 3);
        assertTrue(fs.indexOf(f11) != -1);
        assertTrue(fs.indexOf(f21) != -1);
        assertTrue(fs.indexOf(f22) != -1);
    }

    @Test
    public void testReplaceLinSeparator() {
        assertTrue("".equals(FileUtils.replaceLineSeparator("", ":")));
        assertTrue(FileUtils.replaceLineSeparator(null, ":") == null);
        assertTrue("1".equals(FileUtils.replaceLineSeparator("1", ":")));
        assertTrue("1:22:3:44:".equals(FileUtils.replaceLineSeparator("1\r22\n3\r\n44\r\n", ":")));
        assertTrue("1:22:3:44".equals(FileUtils.replaceLineSeparator("1\r22\n3\r\n44", ":")));
        assertTrue("1:22:3:44".equals(FileUtils.replaceLineSeparator("1\r22\n3\r\n44", ":")));
        assertTrue(("1" + FileUtils.lineSeparator + "22" + FileUtils.lineSeparator + "3" + FileUtils.lineSeparator + "44").equals(FileUtils.replaceLineSeparator("1\r22\n3\r\n44")));
    }

    @Test
    public void test0111() {
        Ensure.isTrue(FileUtils.getParent(null) == null);
        Ensure.isTrue(FileUtils.getParent("") == null);
        Ensure.isTrue(FileUtils.getParent("/") == null);
        Ensure.isTrue(FileUtils.getParent("/1") == null);
        Ensure.isTrue(FileUtils.getParent("/1/2").equals("/1"));
        Ensure.isTrue(FileUtils.getParent("/1/2/").equals("/1"));
        Ensure.isTrue(FileUtils.getParent("/1/2/3.txt").equals("/1/2"));
    }

    @Test
    public void testdos2unix() throws IOException {
        File file = getFile();
        Ensure.isTrue(FileUtils.write(file, StringUtils.CHARSET, false, "1\r\n2\r\n3\r\n"));
        String nt = FileUtils.readline(file, StringUtils.CHARSET, 0);
        Assert.assertEquals("1\\r\\n2\\r\\n3\\r\\n", StringUtils.escapeLineSeparator(nt));
        Ensure.isTrue(nt.indexOf(FileUtils.lineSeparatorWindows) != -1);
        Ensure.isTrue(FileUtils.dos2unix(file, StringUtils.CHARSET));
        String text = FileUtils.readline(file, StringUtils.CHARSET, 0);
        Ensure.isTrue(text.indexOf(FileUtils.lineSeparatorWindows) == -1, text);
    }

    @Test
    public void testCheckFile() throws IOException {
        File file = getFile();

        try {
            FileUtils.checkPermission(file, true, false);
            Ensure.isTrue(false);
        } catch (Exception e) {
            Ensure.isTrue(true);
        }

        try {
            FileUtils.checkPermission(file, false, true);
            Ensure.isTrue(false);
        } catch (Exception e) {
            Ensure.isTrue(true);
        }

        FileUtils.write(file, StringUtils.CHARSET, false, "ceshi neirong ");
        file.setReadable(true);
        file.setWritable(true);
        try {
            FileUtils.checkPermission(file, true, true);
        } catch (Exception e) {
            Ensure.isTrue(false);
        }

    }

    @Test
    public void testExists() throws IOException {
        File file = getFile();
        FileUtils.delete(file);

        Ensure.isTrue(!FileUtils.exists(file.getAbsolutePath()));

        FileUtils.createFile(file);
        Ensure.isTrue(FileUtils.exists(file.getAbsolutePath()));
    }

    @Test
    public void testCleanFileFile() throws IOException {
        File file = getFile();
        FileUtils.write(file, StringUtils.CHARSET, false, "测试内容是否删除");
        FileUtils.clearFile(file);
        Ensure.isTrue(file.length() == 0);
    }

    @Test
    public void testCleanFileString() throws IOException {
        File file = getFile();
        FileUtils.createFile(file);
        FileUtils.write(file, StringUtils.CHARSET, false, "测试内容是否");

        Ensure.isTrue(FileUtils.clearFile(file) && file.length() == 0);
    }

    @Test
    public void testIsFileFile() throws IOException {
        File file = getFile();
        FileUtils.delete(file);
        Ensure.isTrue(FileUtils.createFile(file) && FileUtils.isFile(file));
    }

    @Test
    public void testIsFileString() throws IOException {
        File file = getFile();
        FileUtils.delete(file);

        FileUtils.createFile(file);
        Ensure.isTrue(FileUtils.isFile(file.getAbsolutePath()));

        FileUtils.delete(file);
        file.mkdirs();
        Ensure.isTrue(!FileUtils.isFile(file.getAbsolutePath()));
    }

    @Test
    public void testIsDirFile() {
//		File file = getFile();
//		
//		FT.delete(file);
//		FT.createDirecotry(file);
//		Asserts.assertTrue(FT.isDir(file));
//		
//		FT.delete(file);
//		FT.createfile(file);
//		Asserts.assertTrue(!FT.isDir(file));
    }

    @Test
    public void testIsDirString() throws IOException {
        File file = getFile();
        FileUtils.delete(file);

        FileUtils.createDirectory(file);
        Ensure.isTrue(FileUtils.isDirectory(file.getAbsolutePath()));

        FileUtils.delete(file);
        FileUtils.createFile(file);
        Ensure.isTrue(!FileUtils.isDirectory(file.getAbsolutePath()));
    }

    @Test
    public void testGetFileOutputStream() {
//		File file = getFile();
//		FileOutputStream out = FT.getFileOutputStream(file, true);
//		try {
//			out.write("中文字符串".getBytes("UTF-8"));
//			out.flush();
//			out.close();
//		} catch (Exception e) {
//			Asserts.assertTrue(false);
//		}
    }

    @Test
    public void testGetBufferedReaderReader() throws IOException {
        File file = getFile();
        FileUtils.write(file, StringUtils.CHARSET, false, "ceshi");
        BufferedReader r = IO.getBufferedReader(file, StringUtils.CHARSET);
        Ensure.isTrue(r.readLine().equals("ceshi"));
    }

    @Test
    public void testGetBufferedReaderString() throws IOException {
//		File file = getFile();
//		FT.writeFile(file, "ceshi");
//		BufferedReader r = FT.getBufferedReader(file.getAbsolutePath());
//		Asserts.assertTrue(r.readLine().equals("ceshi"));
    }

    @Test
    public void testGetBufferedReaderStringInt() throws IOException {
//		File file = getFile();
//		FT.writeFile(file, "ceshi");
//		BufferedReader r = FT.getBufferedReader(file, 100);
//		Asserts.assertTrue(r.readLine().equals("ceshi"));
    }

    @Test
    public void testGetBufferedReaderStringStringInt() throws IOException {
//		File file = getFile();
//		FT.writeFile(file, "ceshi");
//		BufferedReader r = FT.getBufferedReader(file.getAbsolutePath(), "UTF-8", 1024);
//		Asserts.assertTrue(r.readLine().equals("ceshi"));
    }

    @Test
    public void testGetBufferedReaderFile() throws IOException {
        File file = getFile();
        FileUtils.write(file, StringUtils.CHARSET, false, "ceshi");
        BufferedReader r = IO.getBufferedReader(file, StringUtils.CHARSET);
        Ensure.isTrue(r.readLine().equals("ceshi"));
    }

    @Test
    public void testGetBufferedReaderFileInt() throws IOException {
//		File file = getFile();
//		FT.writeFile(file, "ceshi");
//		BufferedReader r = FT.getBufferedReader(file, 1024);
//		Asserts.assertTrue(r.readLine().equals("ceshi"));
    }

    @Test
    public void testGetBufferedReaderFileStringInt() throws IOException {
//		File file = getFile();
//		FT.writeFile(file, "ceshi");
//		BufferedReader r = FT.getBufferedReader(file.getAbsolutePath(), 1024);
//		Asserts.assertTrue(r.readLine().equals("ceshi"));
    }

    @Test
    public void testGetBufferedReaderFileString() throws IOException {
        File file = getFile();
        FileUtils.write(file, StringUtils.CHARSET, false, "ceshi");
        BufferedReader r = IO.getBufferedReader(file, StringUtils.CHARSET);
        Ensure.isTrue(r.readLine().equals("ceshi"));
    }

    @Test
    public void testGetFileWriterStringBoolean() throws IOException {
//		File file = getFile();
//		FileWriter w = FT.getFileWriter(file, false);
//		w.flush();
//		w.close();
    }

    @Test
    public void testGetFileWriterString() {
//		File file = getFile();
//		FileWriter w = FT.getFileWriter(file);
//		try {
//			w.write("测试data");
//			w.flush();
//		} catch (Exception e) {
//			e.printStackTrace();
//			Asserts.assertTrue(false);
//		} finally {
//			FT.close((Writer) w);
//		}
    }

    @Test
    public void testGetFileWriterFileBoolean() throws IOException {
//		File file = getFile();
//		FileWriter w = FT.getFileWriter(file, false);
//		w.write(file.getName());
//		w.flush();
//		w.close();
    }

    @Test
    public void testGetFileWriterFile() throws IOException {
//		File file = getFile();
//		FileWriter w = FT.getFileWriter(file);
//		w.write(file.getName());
//		w.flush();
//		w.close();
    }

    @Test
    public void testFlush() {
        IO.flushQuietly(new Flushable() {
            @Override
            public void flush() throws IOException {
                Ensure.isTrue(true);
            }
        });
    }

    @Test
    public void testFlushQuietly() {
        IO.flush(new Flushable() {
            @Override
            public void flush() throws IOException {
                Ensure.isTrue(true);
            }
        });
    }

    @Test
    public void testCloseWithReflect() {
        Ensure.isTrue(true);
    }

    @Test
    public void testCloseWriter() {
        Ensure.isTrue(true);
    }

    @Test
    public void testCloseOutputStream() {
        Ensure.isTrue(true);
    }

    @Test
    public void testCloseReader() {
        Ensure.isTrue(true);
    }

    @Test
    public void testCloseCloseable() {
        Ensure.isTrue(true);
    }

    @Test
    public void testCloseCloseableArray() {
        Ensure.isTrue(true);
    }

    @Test
    public void testCloseZipFile() {
        Ensure.isTrue(true);
    }

    @Test
    public void testCloseWritableWorkbook() {
        Ensure.isTrue(true);
    }

    @Test
    public void testFinishQuietly() throws FileNotFoundException {
//		FT.finishQuietly(new ZipOutputStream(new FileOutputStream(getFile())) {
//			@Override
//			public void finish() throws IOException {
//				Asserts.assertTrue(true);
//			}
//		});
    }

    @Test
    public void testCloseQuietlyWriter() {
        IO.closeQuietly(new Writer() {

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
                Ensure.isTrue(true);
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
                Ensure.isTrue(true);
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
                Ensure.isTrue(true);
            }
        });
    }

    @Test
    public void testCloseQuietlyCloseable() {
        IO.closeQuietly(new Closeable() {
            @Override
            public void close() throws IOException {
                Ensure.isTrue(true);
            }
        });
    }

    @Test
    public void testGetFilename() {
        Ensure.isTrue(FileUtils.getFilename("").equals(""));
        Ensure.isTrue(FileUtils.getFilename("/home/udsf/test").equals("test"));
        Ensure.isTrue(FileUtils.getFilename("/home/udsf/test.").equals("test."));
        Ensure.isTrue(FileUtils.getFilename("/home/udsf/test.txt").equals("test.txt"));
        Ensure.isTrue(FileUtils.getFilename("/home/udsf./test").equals("test"));
        Ensure.isTrue(FileUtils.getFilename("/home/.udsf\\test").equals("test"));
        Ensure.isTrue(FileUtils.getFilename("/home/.udsf\\test.txt").equals("test.txt"));
        Ensure.isTrue(FileUtils.getFilename("/home/.udsf\\.txt").equals(".txt"));
        Ensure.isTrue(FileUtils.getFilename("/home/.udsf/.txt").equals(".txt"));
    }

    @Test
    public void testGetFilenameNoExt() {
        Ensure.isTrue(FileUtils.getFilenameNoExt("/home/udsf/shell/test.txt").equals("test"));
        Ensure.isTrue(FileUtils.getFilenameNoExt("/home/udsf/shell/test.").equals("test"));
        Ensure.isTrue(FileUtils.getFilenameNoExt("/home/udsf/shell/test").equals("test"));
        Ensure.isTrue(FileUtils.getFilenameNoExt("shell/test").equals("test"));
        Ensure.isTrue(FileUtils.getFilenameNoExt("test").equals("test"));
        Ensure.isTrue(FileUtils.getFilenameNoExt("t").equals("t"));
        Ensure.isTrue(FileUtils.getFilenameNoExt("").equals(""));
        Ensure.isTrue(FileUtils.getFilenameNoExt(null) == null);
    }

    @Test
    public void testGetFilenameNoSuffix() {
        Ensure.isTrue(FileUtils.getFilenameNoSuffix(null) == null);
        Ensure.isTrue(FileUtils.getFilenameNoSuffix("1.del").equals("1"));
        Ensure.isTrue(FileUtils.getFilenameNoSuffix(".").equals(""));
        Ensure.isTrue(FileUtils.getFilenameNoSuffix(".del.gz").equals(""));
        Ensure.isTrue(FileUtils.getFilenameNoSuffix("INC_QYZX_ECC_LACKOFINTERESTS18.del.gz").equals("INC_QYZX_ECC_LACKOFINTERESTS18"));
        Ensure.isTrue(FileUtils.getFilenameNoSuffix("D:\\home\\test\\INC_QYZX_ECC_LACKOFINTERESTS18.del.gz").equals("INC_QYZX_ECC_LACKOFINTERESTS18"));
        Ensure.isTrue(FileUtils.getFilenameNoSuffix("D:\\home\\test\\INC_QYZX_ECC_LACKOFINTERESTS18.del").equals("INC_QYZX_ECC_LACKOFINTERESTS18"));
    }

    @Test
    public void testGetFilenameExt() {
        Ensure.isTrue(FileUtils.getFilenameExt("").equals(""));
        Ensure.isTrue(FileUtils.getFilenameExt("/home/udsf/test").equals(""));
        Ensure.isTrue(FileUtils.getFilenameExt("/home/udsf/test.").equals(""));
        Ensure.isTrue(FileUtils.getFilenameExt("/home/udsf/test.txt").equals("txt"));
        Ensure.isTrue(FileUtils.getFilenameExt("/home/udsf./test").equals(""));
        Ensure.isTrue(FileUtils.getFilenameExt("/home/.udsf\\test").equals(""));
        Ensure.isTrue(FileUtils.getFilenameExt("/home/.udsf\\test.txt").equals("txt"));
        Ensure.isTrue(FileUtils.getFilenameExt("/home/.udsf\\.txt").equals("txt"));
        Ensure.isTrue(FileUtils.getFilenameExt("/home/.udsf/.txt").equals("txt"));
    }

    @Test
    public void testGetFilenameSuffix() {
        Ensure.isTrue(FileUtils.getFilenameSuffix(null) == null);
        Ensure.isTrue(FileUtils.getFilenameSuffix("").equals(""));
        Ensure.isTrue(FileUtils.getFilenameSuffix("1").equals(""));
        Ensure.isTrue(FileUtils.getFilenameSuffix("1.").equals(""));
        Ensure.isTrue(FileUtils.getFilenameSuffix("1.d").equals("d"));
        Ensure.isTrue(FileUtils.getFilenameSuffix("1.del").equals("del"));
        Ensure.isTrue(FileUtils.getFilenameSuffix("1.del.gz").equals("del.gz"));
        Ensure.isTrue(FileUtils.getFilenameSuffix("\\1.del.gz").equals("del.gz"));
        Ensure.isTrue(FileUtils.getFilenameSuffix("/1.del.gz").equals("del.gz"));
        Ensure.isTrue(FileUtils.getFilenameSuffix("1/1.del.gz").equals("del.gz"));
    }

    @Test
    public void testStripFilenameExt() {
        Ensure.isTrue(FileUtils.removeFilenameExt(null) == null);
        Ensure.isTrue(FileUtils.removeFilenameExt("").equals(""));
        Ensure.isTrue(FileUtils.removeFilenameExt("/home/udsf/").equals("/home/udsf/"));
        Ensure.isTrue(FileUtils.removeFilenameExt("/home/udsf/test.txt").equals("/home/udsf/test"));
        Ensure.isTrue(FileUtils.removeFilenameExt("/home/udsf/test").equals("/home/udsf/test"));
        Ensure.isTrue(FileUtils.removeFilenameExt("/home/.udsf/test").equals("/home/.udsf/test"));
        Ensure.isTrue(FileUtils.removeFilenameExt("/home/udsf/test.").equals("/home/udsf/test"));
        Ensure.isTrue(FileUtils.removeFilenameExt("/home/udsf/.test").equals("/home/udsf/"));

        Ensure.isTrue(FileUtils.removeFilenameExt("/home/udsf\\").equals("/home/udsf\\"));
        Ensure.isTrue(FileUtils.removeFilenameExt("/home/udsf\\test.txt").equals("/home/udsf\\test"));
        Ensure.isTrue(FileUtils.removeFilenameExt("/home/udsf\\test").equals("/home/udsf\\test"));
        Ensure.isTrue(FileUtils.removeFilenameExt("/home/.udsf\\test").equals("/home/.udsf\\test"));
        Ensure.isTrue(FileUtils.removeFilenameExt("/home/udsf\\test.").equals("/home/udsf\\test"));
        Ensure.isTrue(FileUtils.removeFilenameExt("/home/udsf\\.test").equals("/home/udsf\\"));
    }

    @Test
    public void testGetNoRepeatFilename() throws IOException {
        File tempDir = FileUtils.getTempDir(FileUtilsTest.class);
        File parent = new File(tempDir, Dates.format08(new Date()));
        FileUtils.createDirectory(parent);

        // 先创建一个文件
        File file = new File(parent, "test_repeat_file.dat.tmp");
        FileUtils.createFile(file);

        // 再创建一个不重名文件
        File newfile = FileUtils.getFileNoRepeat(parent, "test_repeat_file.dat.tmp");
        assertNotEquals(newfile, file);
        FileUtils.createFile(newfile);

        // 再创建一个不重名文件
        File newfile1 = FileUtils.getFileNoRepeat(parent, "test_repeat_file.dat.tmp");
        assertNotEquals(newfile1, file);
        assertNotEquals(newfile1, newfile);
        FileUtils.createFile(newfile1);
    }

    @Test
    public void testGetResourceAsStream() throws IOException {
        assertNotNull(FileUtils.loadProperties("/testfile.properties"));
    }

    @Test
    public void testCreateFileString() throws IOException {
        File file = getFile();
        FileUtils.delete(file);
        Ensure.isTrue(!file.exists());

        FileUtils.createFile(file);
        Ensure.isTrue(file.exists());
    }

    @Test
    public void testCreateFileFile() throws IOException {
        File file = getFile();
        FileUtils.delete(file);
        Ensure.isTrue(!file.exists());

        FileUtils.createFile(file);
        Ensure.isTrue(file.exists());
    }

    @Test
    public void testCreatefileFile() throws IOException {
        File file = getFile();
        FileUtils.delete(file);
        Ensure.isTrue(FileUtils.createFile(file) && file.exists());
    }

    @Test
    public void testCreatefileString() throws IOException {
        File file = getFile();
        FileUtils.delete(file);
        Ensure.isTrue(FileUtils.createFile(file) && file.exists());
    }

    @Test
    public void testCreatefileFileBoolean() throws IOException {
        File file = getFile();
        FileUtils.delete(file);
        FileUtils.createDirectory(file);
        Ensure.isTrue(FileUtils.createFile(file, true));
    }

    @Test
    public void testCreateDirecotryString() {
        File file = getFile();
        FileUtils.delete(file);
        FileUtils.createDirectory(file);
        Ensure.isTrue(file.exists() && file.isDirectory());
    }

    @Test
    public void testCreateDirecotryFile() {
        File file = getFile();
        FileUtils.delete(file);
        FileUtils.createDirectory(file);
        Ensure.isTrue(file.exists() && file.isDirectory());
    }

    @Test
    public void testCreateDirectoryString() {
        File file = getFile();
        FileUtils.delete(file);
        Ensure.isTrue(FileUtils.createDirectory(file) && file.exists() && file.isDirectory());
    }

    @Test
    public void testCreateDirectoryFile() {
        File file = getFile();
        FileUtils.delete(file);
        Ensure.isTrue(FileUtils.createDirectory(file) && file.exists() && file.isDirectory());
    }

    @Test
    public void testCreateDirectoryFileBoolean() throws IOException {
        File file = getFile();
        FileUtils.delete(file);
        FileUtils.createFile(file);
        Ensure.isTrue(FileUtils.createDirectory(file, true) && file.exists() && file.isDirectory());

        File f = new File(FileUtils.getTempDir(FileUtilsTest.class), "dirdir0000");
        FileUtils.delete(f);
        f.mkdirs();
        File f0 = new File(f, FileUtils.joinFilepath("t1", "t2", "t3"));
        Ensure.isTrue(FileUtils.createDirectory(f0) && f0.exists() && f0.isDirectory());
        System.out.println("f0: " + f0.getAbsolutePath());
    }

    @Test
    public void testTranslateSeperator() {
        String str1 = "/home/udsf/shell/qyzx/";
        String str2 = StringUtils.replaceAll(str1, "/", File.separator);
        Ensure.isTrue(FileUtils.replaceFolderSeparator(str1).equals(str2));
    }

    @Test
    public void testSpellFileStringString() {
        Ensure.isTrue(FileUtils.joinFilepath("/home/udsf", "shell").equals("/home/udsf" + File.separator + "shell"));
        Ensure.isTrue(FileUtils.joinFilepath("/home/udsf", "shell/qyzx").equals("/home/udsf" + File.separator + "shell/qyzx"));
    }

    @Test
    public void testSpellFileStringArray() {
        Ensure.isTrue(FileUtils.joinFilepath(new String[]{"home", "udsf", "shell", "grzx"}).equals("home" + File.separator + "udsf" + File.separator + "shell" + File.separator + "grzx"));
    }

    @Test
    public void testRemoveEndFileSeparator() {
        Ensure.isTrue(FileUtils.rtrimFolderSeparator("\\home\\udsf\\shell\\qyzx\\").equals("\\home\\udsf\\shell\\qyzx"));
        Ensure.isTrue(FileUtils.rtrimFolderSeparator("\\home\\udsf\\shell\\qyzx\\/").equals("\\home\\udsf\\shell\\qyzx"));
    }

    @Test
    public void testChangeFilenameExt() {
        Ensure.isTrue(FileUtils.changeFilenameExt("C:/test/ceshi/test.txt", "enc").equals("C:/test/ceshi/test.enc"));
        Ensure.isTrue(FileUtils.changeFilenameExt("C:/test/.ceshi/test.txt", "enc").equals("C:/test/.ceshi/test.enc"));
        Ensure.isTrue(FileUtils.changeFilenameExt("C:\\.test\\.ceshi\\test.txt", "enc").equals("C:\\.test\\.ceshi\\test.enc"));
        Ensure.isTrue(FileUtils.changeFilenameExt("C:/test/.ceshi/.test.txt", "enc").equals("C:/test/.ceshi/.test.enc"));
        Ensure.isTrue(FileUtils.changeFilenameExt("C:/test/.ceshi/.test", "enc").equals("C:/test/.ceshi/.enc"));
        Ensure.isTrue(FileUtils.changeFilenameExt("C:/test/.ceshi/test", "enc").equals("C:/test/.ceshi/test.enc"));
    }

    @Test
    public void testLoadPropertiesFile() throws IOException {
        File file = new File(FileUtils.getTempDir(FileUtilsTest.class), "a.properties");
        FileOutputStream fs = new FileOutputStream(file);
        Properties p = new Properties();
        p.put("path", "/home/udsfd/shell/grzx/grzx_execute.xml");
        p.store(fs, "测试");

        Properties nc = FileUtils.loadProperties(file.getAbsolutePath());
        Ensure.isTrue(nc.getProperty("path").equals("/home/udsfd/shell/grzx/grzx_execute.xml"));
    }

    @Test
    public void testWriteProperties() throws IOException {
        Properties p = new Properties();
        p.put("path", "/home/udsfd/shell/grzx/grzx_execute.xml");

        File file = getFile();
        File newFile = FileUtils.storeProperties(p, file);

        Properties nc = FileUtils.loadProperties(newFile.getAbsolutePath());
        Ensure.isTrue(nc.getProperty("path").equals("/home/udsfd/shell/grzx/grzx_execute.xml"));
    }

    @Test
    public void testDelete() throws IOException {
        File file = getFile();
        FileUtils.createFile(file);
        FileUtils.delete(file);
        Ensure.isTrue(!file.exists());

        FileUtils.delete(file);
        FileUtils.createDirectory(file);
        FileUtils.delete(file);
        Ensure.isTrue(!file.exists());
    }

    @Test
    public void testDeleteFileFile() throws IOException {
        File f0 = getFile();
        FileUtils.createFile(f0);
        Ensure.isTrue(FileUtils.deleteFile(f0) && !f0.exists());

        File f1 = getFile();
        FileUtils.createDirectory(f1);
        Ensure.isTrue(!FileUtils.deleteFile(f1) && f1.exists());
    }

    @Test
    public void testDeleteFileString() throws IOException {
        File f0 = getFile();
        FileUtils.createFile(f0);
        Ensure.isTrue(FileUtils.deleteFile(new File(f0.getAbsolutePath())) && !f0.exists());

        File f1 = getFile();
        FileUtils.createDirectory(f1);
        Ensure.isTrue(!FileUtils.deleteFile(new File(f1.getAbsolutePath())) && f1.exists());
    }

    @Test
    public void testDeleteDirectoryString() throws IOException {
        File f0 = getFile();
        FileUtils.createFile(f0);
        Ensure.isTrue(!FileUtils.deleteDirectory(new File(f0.getAbsolutePath())) && f0.exists());

        File f1 = getFile();
        FileUtils.createDirectory(f1);
        Ensure.isTrue(FileUtils.deleteDirectory(new File(f1.getAbsolutePath())) && !f1.exists());
    }

    @Test
    public void testDeleteDirectoryFile() throws IOException {
        File f0 = getFile();
        FileUtils.createFile(f0);
        Ensure.isTrue(!FileUtils.deleteDirectory(f0) && f0.exists());

        File f1 = getFile();
        FileUtils.createDirectory(f1);
        Ensure.isTrue(FileUtils.deleteDirectory(f1) && !f1.exists());
    }

    @Test
    public void testCleanDirectoryString() throws IOException {
        File dir = getFile();
        FileUtils.createDirectory(dir);

        File cdir = new File(dir, "cdir");
        FileUtils.createDirectory(cdir);

        File f1 = new File(cdir, "test.del");
        FileUtils.createFile(f1);
        File f2 = new File(dir, "test.del");
        FileUtils.createFile(f2);

        Ensure.isTrue(FileUtils.clearDirectory(dir) && !cdir.exists() && !f1.exists() && !f2.exists());
    }

    @Test
    public void testCleanDirectoryFile() throws IOException {
        File dir = getFile();
        FileUtils.createDirectory(dir);

        File cdir = new File(dir, "cdir");
        FileUtils.createDirectory(cdir);

        File f1 = new File(cdir, "test.del");
        FileUtils.createFile(f1);
        File f2 = new File(dir, "test.del");
        FileUtils.createFile(f2);

        Ensure.isTrue(FileUtils.clearDirectory(new File(dir.getAbsolutePath())) && !cdir.exists() && !f1.exists() && !f2.exists());
    }

    @Test
    public void testGetLineContent() throws IOException {
        File file = getFile();
        FileUtils.write(file, StringUtils.CHARSET, false, "l1\nl2\nl3");
        Ensure.isTrue(FileUtils.readline(file, null, 1).equals("l1"));
        Ensure.isTrue(FileUtils.readline(file, null, 2).equals("l2"));
        Ensure.isTrue(FileUtils.readline(file, null, 3).equals("l3"));
    }

    @Test
    public void testMoveFileToDirFileFile() throws IOException {
        File dir = getFile();
        FileUtils.createDirectory(dir);

        File cdir = new File(dir, "cdir");
        FileUtils.createDirectory(cdir);

        File f1 = new File(cdir, "test.del");
        FileUtils.createFile(f1);
        File f2 = new File(dir, "test.del");
        FileUtils.createFile(f2);

        File dest = getFile();
        FileUtils.createDirectory(dest);

        Ensure.isTrue(FileUtils.moveFile(dir, dest) && !dir.exists());

        File ff = getFile();
        FileUtils.createFile(ff);
        Ensure.isTrue(FileUtils.moveFile(ff, dest) && !dir.exists());
    }

    @Test
    public void testMoveFileToDirStringString() throws IOException {
        File dir = getFile();
        FileUtils.createDirectory(dir);

        File cdir = new File(dir, "cdir");
        FileUtils.createDirectory(cdir);

        File f1 = new File(cdir, "test.del");
        FileUtils.createFile(f1);
        File f2 = new File(dir, "test.del");
        FileUtils.createFile(f2);

        File dest = getFile();
        FileUtils.createDirectory(dest);

        Ensure.isTrue(FileUtils.moveFile(new File(dir.getAbsolutePath()), new File(dest.getAbsolutePath())) && !dir.exists());

        File ff = getFile();
        FileUtils.createFile(ff);
        Ensure.isTrue(FileUtils.moveFile(new File(ff.getAbsolutePath()), new File(dest.getAbsolutePath())) && !dir.exists());
    }

    @Test
    public void testMoveFileToRecycle() throws IOException {
        File file = getFile();
        File dir = getFile();
        FileUtils.createFile(file);
        FileUtils.createDirectory(dir);

//		File recFile = new File(IOUtils.getSystemRecycle(), file.getName());
//		System.out.println(recFile.getAbsolutePath() + ", " + recFile.exists());

        Ensure.isTrue(FileUtils.moveFileToRecycle(file) && !file.exists());
    }

    @Test
    public void testRenameFileStringString() throws IOException {
        File file = getFile();
        FileUtils.delete(file);
        FileUtils.createFile(file);

        File newFile = new File(file.getParentFile(), "test_rename_g.txt");
        Ensure.isTrue(FileUtils.delete(newFile) && FileUtils.rename(file, "test_rename_g", ".txt") == 0 && newFile.exists());
    }

    @Test
    public void testRenameFileFile() throws IOException {
        File f0 = getFile();
        FileUtils.delete(f0);
        FileUtils.createFile(f0);

        File f1 = new File(f0.getParentFile(), "renameFileFile.del");
        FileUtils.delete(f1);

        FileUtils.rename(f0, f1);
        Ensure.isTrue(true);
    }

    @Test
    public void testByte2Megabyte() {
//		Asserts.assertTrue(Numbers.byte2Megabyte(0) == 0);
//		Asserts.assertTrue(Numbers.byte2Megabyte((long) 1024 * 1024) == 1);
//		Asserts.assertTrue(Numbers.byte2Megabyte((long) 1024 * 1024 * 1024) == 1024);
    }

    @Test
    public void testReplaceFileSeparator() {
        Ensure.isTrue(FileUtils.replaceFolderSeparator("/", '|').equals("|"));
        Ensure.isTrue(FileUtils.replaceFolderSeparator("\\", '|').equals("|"));
        Ensure.isTrue(FileUtils.replaceFolderSeparator("/home", '|').equals("|home"));
        Ensure.isTrue(FileUtils.replaceFolderSeparator("/home/udsf/shell/qyzx", '|').equals("|home|udsf|shell|qyzx"));
        Ensure.isTrue(FileUtils.replaceFolderSeparator("/home/udsf/shell/qyzx\\", '|').equals("|home|udsf|shell|qyzx|"));
    }

    @Test
    public void testGetLineSeparator() throws IOException {
        File file = getFile();
        FileUtils.write(file, StringUtils.CHARSET, false, "a\nb\nc\n\n");
        Ensure.isTrue(FileUtils.readLineSeparator(file).equals("\n"));
    }

    @Test
    public void testToJavaIoFile() {
//		File f = getFile();
//		FT.writeFile(f, "1,2,3,4\r\n3,4,5,6,\r\n7,8,9,10");
//		
//		CommonTxtTableFile file = new CommonTxtTableFile();
//		file.setFile(f.getAbsolutePath());
//		File fs = FT.toJavaIoFile(file);
//		Asserts.assertTrue(fs.getAbsolutePath().equals(f.getAbsolutePath()));
    }

    @Test
    public void testGetRadomFileName() {
        Ensure.isTrue(StringUtils.isNotBlank(FileUtils.getFilenameRandom("", "")));
    }

    @Test
    public void testGetSystemTempDir() {
        Ensure.isTrue(FileUtils.getTempDir(FileUtilsTest.class).exists() && FileUtils.getTempDir(FileUtilsTest.class).isDirectory());
    }

    @Test
    public void testGetSystemRecycle() {
        Ensure.isTrue(FileUtils.getRecyDir().exists() && FileUtils.getRecyDir().isDirectory());
        System.out.println(FileUtils.getRecyDir().getAbsolutePath());
    }

    @Test
    public void testCopy() throws IOException {
        File f0 = getFile();
        FileUtils.write(f0, StringUtils.CHARSET, false, "1,2,3,4,5");

        File f1 = new File(f0.getParentFile(), "clone_" + f0.getName());
        Ensure.isTrue(FileUtils.copy(f0, f1) && FileUtils.readline(f1, null, 1).equals("1,2,3,4,5"));
    }

    @Test
    public void testWriteFileFileBooleanString() throws IOException {
        File file = getFile();
        FileUtils.write(file, StringUtils.CHARSET, false, "1\n");
        Ensure.isTrue(FileUtils.readline(file, StringUtils.CHARSET, 1).equals("1"));

        FileUtils.write(file, StringUtils.CHARSET, false, "1\n2");
        Ensure.isTrue(FileUtils.readline(file, StringUtils.CHARSET, 1).equals("1"));

        FileUtils.write(file, StringUtils.CHARSET, true, "\n3");
        Ensure.isTrue(FileUtils.readline(file, StringUtils.CHARSET, 3).equals("3"));
    }

    @Test
    public void testWriteFileFileString() throws IOException {
        File file = getFile();
        FileUtils.write(file, StringUtils.CHARSET, false, "1\n2\n3");
        Ensure.isTrue(FileUtils.readline(file, StringUtils.CHARSET, 3).equals("3"));
    }

    @Test
    public void testEqualsFileFileInt() throws IOException {
        File f0 = getFile();
        File f1 = getFile();

        FileUtils.write(f0, StringUtils.CHARSET, false, "");
        FileUtils.write(f1, StringUtils.CHARSET, false, "");
        Ensure.isTrue(FileUtils.equals(f0, f1, 0));

        FileUtils.write(f0, StringUtils.CHARSET, false, "1");
        FileUtils.write(f1, StringUtils.CHARSET, false, "1");
        Ensure.isTrue(FileUtils.equals(f0, f1, 0));

        FileUtils.write(f0, StringUtils.CHARSET, false, "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        FileUtils.write(f1, StringUtils.CHARSET, false, "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        Ensure.isTrue(FileUtils.equals(f0, f1, 0));

        FileUtils.write(f0, StringUtils.CHARSET, false, "1");
        FileUtils.write(f1, StringUtils.CHARSET, false, "2");
        Ensure.isTrue(!FileUtils.equals(f0, f1, 0));
    }

    @Test
    public void testEqualsIgnoreLineSeperator() throws IOException {
        File f0 = getFile();
        File f1 = getFile();

        FileUtils.write(f0, StringUtils.CHARSET, false, "1");
        FileUtils.write(f1, StringUtils.CHARSET, false, "1");
        assertEquals(FileUtils.equalsIgnoreLineSeperator(f0, StringUtils.CHARSET, f1, StringUtils.CHARSET, 0), 0);

        FileUtils.write(f0, StringUtils.CHARSET, false, "1234567\n890123456789012345678\r90123456789012345\r\n678901234567890123456789012345\n678901234567890");
        FileUtils.write(f1, StringUtils.CHARSET, false, "1234567\r\n890123456789012345678\n90123456789012345\n678901234567890123456789012345\r\n678901234567890");
        assertEquals(FileUtils.equalsIgnoreLineSeperator(f0, StringUtils.CHARSET, f1, StringUtils.CHARSET, 0), 0);
    }

    @Test
    public void test100() throws IOException {
        File root = FileUtils.getTempDir(FileUtils.class);
        FileUtils.clearDirectory(root);
        if (!FileUtils.isWriting(root, 500).isEmpty()) {
            Assert.fail();
        }

        FileUtils.clearDirectory(root);
        File file = new File(root, "test.del");
        FileUtils.write(file, "utf-8", false, "testset");
        Thread t = new Thread() {
            public void run() {
                TimeWatch w = new TimeWatch();
                boolean b = true;
                while (w.useSeconds() <= 10) {
                    try {
                        String str = "lvzhaojun123" + Dates.currentTimeStamp();
                        if (b) {
                            System.out.println("写入: " + str + " > " + file.getAbsolutePath());
                            b = false;
                        }
                        FileUtils.write(file, "utf-8", false, str);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Assert.fail();
                    }
                }
            }
        };
        t.start();

        System.out.println("等待2秒!");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<File> list = FileUtils.isWriting(root, 500);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals(file, list.get(0));
    }

    @Test
    public void test101() {

    }

}
