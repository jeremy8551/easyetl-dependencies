package icu.etl.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
        assertTrue(FileUtils.getTempDir(FileUtilsTest.class).exists() && FileUtils.getTempDir(FileUtilsTest.class).isDirectory());
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
        assertEquals(3, fs.size());
        assertTrue(fs.indexOf(f11) != -1);
        assertTrue(fs.indexOf(f21) != -1);
        assertTrue(fs.indexOf(f22) != -1);

        fs = FileUtils.findFile(d0, "20200102[^ ]{4}");
        assertEquals(3, fs.size());
        assertTrue(fs.indexOf(f11) != -1);
        assertTrue(fs.indexOf(f21) != -1);
        assertTrue(fs.indexOf(f22) != -1);
    }

    @Test
    public void testReplaceLinSeparator() {
        assertEquals("", FileUtils.replaceLineSeparator("", ":"));
        assertNull(FileUtils.replaceLineSeparator(null, ":"));
        assertEquals("1", FileUtils.replaceLineSeparator("1", ":"));
        assertEquals("1:22:3:44:", FileUtils.replaceLineSeparator("1\r22\n3\r\n44\r\n", ":"));
        assertEquals("1:22:3:44", FileUtils.replaceLineSeparator("1\r22\n3\r\n44", ":"));
        assertEquals("1:22:3:44", FileUtils.replaceLineSeparator("1\r22\n3\r\n44", ":"));
        assertEquals(("1" + FileUtils.lineSeparator + "22" + FileUtils.lineSeparator + "3" + FileUtils.lineSeparator + "44"), FileUtils.replaceLineSeparator("1\r22\n3\r\n44"));
    }

    @Test
    public void test0111() {
        assertNull(FileUtils.getParent(null));
        assertNull(FileUtils.getParent(""));
        assertNull(FileUtils.getParent("/"));
        assertNull(FileUtils.getParent("/1"));
        assertEquals("/1", FileUtils.getParent("/1/2"));
        assertEquals("/1", FileUtils.getParent("/1/2/"));
        assertEquals("/1/2", FileUtils.getParent("/1/2/3.txt"));
    }

    @Test
    public void testdos2unix() throws IOException {
        File file = getFile();
        assertTrue(FileUtils.write(file, StringUtils.CHARSET, false, "1\r\n2\r\n3\r\n"));
        String nt = FileUtils.readline(file, StringUtils.CHARSET, 0);
        assertEquals("1\\r\\n2\\r\\n3\\r\\n", StringUtils.escapeLineSeparator(nt));
        assertTrue(nt.indexOf(FileUtils.lineSeparatorWindows) != -1);
        assertTrue(FileUtils.dos2unix(file, StringUtils.CHARSET));
        String text = FileUtils.readline(file, StringUtils.CHARSET, 0);
        assertEquals(-1, text.indexOf(FileUtils.lineSeparatorWindows));
    }

    @Test
    public void testCheckFile() throws IOException {
        File file = getFile();

        try {
            FileUtils.checkPermission(file, true, false);
            fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }

        try {
            FileUtils.checkPermission(file, false, true);
            fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }

        FileUtils.write(file, StringUtils.CHARSET, false, "ceshi neirong ");
        file.setReadable(true);
        file.setWritable(true);
        try {
            FileUtils.checkPermission(file, true, true);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testExists() throws IOException {
        File file = getFile();
        FileUtils.delete(file);

        assertFalse(FileUtils.exists(file.getAbsolutePath()));

        FileUtils.createFile(file);
        assertTrue(FileUtils.exists(file.getAbsolutePath()));
    }

    @Test
    public void testCleanFileFile() throws IOException {
        File file = getFile();
        FileUtils.write(file, StringUtils.CHARSET, false, "测试内容是否删除");
        FileUtils.clearFile(file);
        assertEquals(0, file.length());
    }

    @Test
    public void testCleanFileString() throws IOException {
        File file = getFile();
        FileUtils.createFile(file);
        FileUtils.write(file, StringUtils.CHARSET, false, "测试内容是否");

        assertTrue(FileUtils.clearFile(file) && file.length() == 0);
    }

    @Test
    public void testIsFileFile() throws IOException {
        File file = getFile();
        FileUtils.delete(file);
        assertTrue(FileUtils.createFile(file) && FileUtils.isFile(file));
    }

    @Test
    public void testIsFileString() throws IOException {
        File file = getFile();
        FileUtils.delete(file);

        FileUtils.createFile(file);
        assertTrue(FileUtils.isFile(file.getAbsolutePath()));

        FileUtils.delete(file);
        file.mkdirs();
        assertFalse(FileUtils.isFile(file.getAbsolutePath()));
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
        assertTrue(FileUtils.isDirectory(file.getAbsolutePath()));

        FileUtils.delete(file);
        FileUtils.createFile(file);
        assertFalse(FileUtils.isDirectory(file.getAbsolutePath()));
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
    public void testCloseWithReflect() {
        Assert.assertTrue(true);
    }

    @Test
    public void testCloseWriter() {
        Assert.assertTrue(true);
    }

    @Test
    public void testCloseOutputStream() {
        Assert.assertTrue(true);
    }

    @Test
    public void testCloseReader() {
        Assert.assertTrue(true);
    }

    @Test
    public void testCloseCloseable() {
        Assert.assertTrue(true);
    }

    @Test
    public void testCloseCloseableArray() {
        Assert.assertTrue(true);
    }

    @Test
    public void testCloseZipFile() {
        Assert.assertTrue(true);
    }

    @Test
    public void testCloseWritableWorkbook() {
        Assert.assertTrue(true);
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
    public void testGetFilename() {
        assertEquals("", FileUtils.getFilename(""));
        assertEquals("test", FileUtils.getFilename("/home/test/test"));
        assertEquals("test.", FileUtils.getFilename("/home/test/test."));
        assertEquals("test.txt", FileUtils.getFilename("/home/test/test.txt"));
        assertEquals("test", FileUtils.getFilename("/home/test./test"));
        assertEquals("test", FileUtils.getFilename("/home/.test\\test"));
        assertEquals("test.txt", FileUtils.getFilename("/home/.test\\test.txt"));
        assertEquals(".txt", FileUtils.getFilename("/home/.test\\.txt"));
        assertEquals(".txt", FileUtils.getFilename("/home/.test/.txt"));
    }

    @Test
    public void testGetFilenameNoExt() {
        assertEquals("test", FileUtils.getFilenameNoExt("/home/test/shell/test.txt"));
        assertEquals("test", FileUtils.getFilenameNoExt("/home/test/shell/test."));
        assertEquals("test", FileUtils.getFilenameNoExt("/home/test/shell/test"));
        assertEquals("test", FileUtils.getFilenameNoExt("shell/test"));
        assertEquals("test", FileUtils.getFilenameNoExt("test"));
        assertEquals("t", FileUtils.getFilenameNoExt("t"));
        assertEquals("", FileUtils.getFilenameNoExt(""));
        assertNull(FileUtils.getFilenameNoExt(null));
    }

    @Test
    public void testGetFilenameNoSuffix() {
        assertNull(FileUtils.getFilenameNoSuffix(null));
        assertEquals("1", FileUtils.getFilenameNoSuffix("1.del"));
        assertEquals("", FileUtils.getFilenameNoSuffix("."));
        assertEquals("", FileUtils.getFilenameNoSuffix(".del.gz"));
        assertEquals("INC_QYZX_ECC_LACKOFINTERESTS18", FileUtils.getFilenameNoSuffix("INC_QYZX_ECC_LACKOFINTERESTS18.del.gz"));
        assertEquals("INC_QYZX_ECC_LACKOFINTERESTS18", FileUtils.getFilenameNoSuffix("D:\\home\\test\\INC_QYZX_ECC_LACKOFINTERESTS18.del.gz"));
        assertEquals("INC_QYZX_ECC_LACKOFINTERESTS18", FileUtils.getFilenameNoSuffix("D:\\home\\test\\INC_QYZX_ECC_LACKOFINTERESTS18.del"));
    }

    @Test
    public void testGetFilenameExt() {
        assertEquals("", FileUtils.getFilenameExt(""));
        assertEquals("", FileUtils.getFilenameExt("/home/test/test"));
        assertEquals("", FileUtils.getFilenameExt("/home/test/test."));
        assertEquals("txt", FileUtils.getFilenameExt("/home/test/test.txt"));
        assertEquals("", FileUtils.getFilenameExt("/home/test./test"));
        assertEquals("", FileUtils.getFilenameExt("/home/.test\\test"));
        assertEquals("txt", FileUtils.getFilenameExt("/home/.test\\test.txt"));
        assertEquals("txt", FileUtils.getFilenameExt("/home/.test\\.txt"));
        assertEquals("txt", FileUtils.getFilenameExt("/home/.test/.txt"));
    }

    @Test
    public void testGetFilenameSuffix() {
        assertNull(FileUtils.getFilenameSuffix(null));
        assertEquals("", FileUtils.getFilenameSuffix(""));
        assertEquals("", FileUtils.getFilenameSuffix("1"));
        assertEquals("", FileUtils.getFilenameSuffix("1."));
        assertEquals("d", FileUtils.getFilenameSuffix("1.d"));
        assertEquals("del", FileUtils.getFilenameSuffix("1.del"));
        assertEquals("del.gz", FileUtils.getFilenameSuffix("1.del.gz"));
        assertEquals("del.gz", FileUtils.getFilenameSuffix("\\1.del.gz"));
        assertEquals("del.gz", FileUtils.getFilenameSuffix("/1.del.gz"));
        assertEquals("del.gz", FileUtils.getFilenameSuffix("1/1.del.gz"));
    }

    @Test
    public void testStripFilenameExt() {
        assertNull(FileUtils.removeFilenameExt(null));
        assertEquals("", FileUtils.removeFilenameExt(""));
        assertEquals("/home/test/", FileUtils.removeFilenameExt("/home/test/"));
        assertEquals("/home/test/test", FileUtils.removeFilenameExt("/home/test/test.txt"));
        assertEquals("/home/test/test", FileUtils.removeFilenameExt("/home/test/test"));
        assertEquals("/home/.test/test", FileUtils.removeFilenameExt("/home/.test/test"));
        assertEquals("/home/test/test", FileUtils.removeFilenameExt("/home/test/test."));
        assertEquals("/home/test/", FileUtils.removeFilenameExt("/home/test/.test"));

        assertEquals("/home/test\\", FileUtils.removeFilenameExt("/home/test\\"));
        assertEquals("/home/test\\test", FileUtils.removeFilenameExt("/home/test\\test.txt"));
        assertEquals("/home/test\\test", FileUtils.removeFilenameExt("/home/test\\test"));
        assertEquals("/home/.test\\test", FileUtils.removeFilenameExt("/home/.test\\test"));
        assertEquals("/home/test\\test", FileUtils.removeFilenameExt("/home/test\\test."));
        assertEquals("/home/test\\", FileUtils.removeFilenameExt("/home/test\\.test"));
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
        assertFalse(file.exists());

        FileUtils.createFile(file);
        assertTrue(file.exists());
    }

    @Test
    public void testCreateFileFile() throws IOException {
        File file = getFile();
        FileUtils.delete(file);
        assertFalse(file.exists());

        FileUtils.createFile(file);
        assertTrue(file.exists());
    }

    @Test
    public void testCreatefileFile() throws IOException {
        File file = getFile();
        FileUtils.delete(file);
        assertTrue(FileUtils.createFile(file) && file.exists());
    }

    @Test
    public void testCreatefileString() throws IOException {
        File file = getFile();
        FileUtils.delete(file);
        assertTrue(FileUtils.createFile(file) && file.exists());
    }

    @Test
    public void testCreatefileFileBoolean() throws IOException {
        File file = getFile();
        FileUtils.delete(file);
        FileUtils.createDirectory(file);
        assertTrue(FileUtils.createFile(file, true));
    }

    @Test
    public void testCreateDirecotryString() {
        File file = getFile();
        FileUtils.delete(file);
        FileUtils.createDirectory(file);
        assertTrue(file.exists() && file.isDirectory());
    }

    @Test
    public void testCreateDirecotryFile() {
        File file = getFile();
        FileUtils.delete(file);
        FileUtils.createDirectory(file);
        assertTrue(file.exists() && file.isDirectory());
    }

    @Test
    public void testCreateDirectoryString() {
        File file = getFile();
        FileUtils.delete(file);
        assertTrue(FileUtils.createDirectory(file) && file.exists() && file.isDirectory());
    }

    @Test
    public void testCreateDirectoryFile() {
        File file = getFile();
        FileUtils.delete(file);
        assertTrue(FileUtils.createDirectory(file) && file.exists() && file.isDirectory());
    }

    @Test
    public void testCreateDirectoryFileBoolean() throws IOException {
        File file = getFile();
        FileUtils.delete(file);
        FileUtils.createFile(file);
        assertTrue(FileUtils.createDirectory(file, true) && file.exists() && file.isDirectory());

        File f = new File(FileUtils.getTempDir(FileUtilsTest.class), "dirdir0000");
        FileUtils.delete(f);
        f.mkdirs();
        File f0 = new File(f, FileUtils.joinFilepath("t1", "t2", "t3"));
        assertTrue(FileUtils.createDirectory(f0) && f0.exists() && f0.isDirectory());
        System.out.println("f0: " + f0.getAbsolutePath());
    }

    @Test
    public void testTranslateSeperator() {
        String str1 = "/home/test/shell/qyzx/";
        String str2 = StringUtils.replaceAll(str1, "/", File.separator);
        assertEquals(FileUtils.replaceFolderSeparator(str1), str2);
    }

    @Test
    public void testSpellFileStringString() {
        assertEquals(FileUtils.joinFilepath("/home/test", "shell"), "/home/test" + File.separator + "shell");
        assertEquals(FileUtils.joinFilepath("/home/test", "shell/qyzx"), "/home/test" + File.separator + "shell/qyzx");
    }

    @Test
    public void testSpellFileStringArray() {
        assertEquals(FileUtils.joinFilepath(new String[]{"home", "test", "shell", "grzx"}), "home" + File.separator + "test" + File.separator + "shell" + File.separator + "grzx");
    }

    @Test
    public void testRemoveEndFileSeparator() {
        assertEquals("\\home\\test\\shell\\qyzx", FileUtils.rtrimFolderSeparator("\\home\\test\\shell\\qyzx\\"));
        assertEquals("\\home\\test\\shell\\qyzx", FileUtils.rtrimFolderSeparator("\\home\\test\\shell\\qyzx\\/"));
    }

    @Test
    public void testChangeFilenameExt() {
        assertEquals("C:/test/ceshi/test.enc", FileUtils.changeFilenameExt("C:/test/ceshi/test.txt", "enc"));
        assertEquals("C:/test/.ceshi/test.enc", FileUtils.changeFilenameExt("C:/test/.ceshi/test.txt", "enc"));
        assertEquals("C:\\.test\\.ceshi\\test.enc", FileUtils.changeFilenameExt("C:\\.test\\.ceshi\\test.txt", "enc"));
        assertEquals("C:/test/.ceshi/.test.enc", FileUtils.changeFilenameExt("C:/test/.ceshi/.test.txt", "enc"));
        assertEquals("C:/test/.ceshi/.enc", FileUtils.changeFilenameExt("C:/test/.ceshi/.test", "enc"));
        assertEquals("C:/test/.ceshi/test.enc", FileUtils.changeFilenameExt("C:/test/.ceshi/test", "enc"));
    }

    @Test
    public void testLoadPropertiesFile() throws IOException {
        File file = new File(FileUtils.getTempDir(FileUtilsTest.class), "a.properties");
        FileOutputStream fs = new FileOutputStream(file);
        Properties p = new Properties();
        p.put("path", "/home/user/shell/grzx/grzx_execute.xml");
        p.store(fs, "测试");

        Properties nc = FileUtils.loadProperties(file.getAbsolutePath());
        assertEquals("/home/user/shell/grzx/grzx_execute.xml", nc.getProperty("path"));
    }

    @Test
    public void testWriteProperties() throws IOException {
        Properties p = new Properties();
        p.put("path", "/home/user/shell/grzx/grzx_execute.xml");

        File file = getFile();
        File newFile = FileUtils.storeProperties(p, file);

        Properties nc = FileUtils.loadProperties(newFile.getAbsolutePath());
        assertEquals("/home/user/shell/grzx/grzx_execute.xml", nc.getProperty("path"));
    }

    @Test
    public void testDelete() throws IOException {
        File file = getFile();
        FileUtils.createFile(file);
        FileUtils.delete(file);
        assertFalse(file.exists());

        FileUtils.delete(file);
        FileUtils.createDirectory(file);
        FileUtils.delete(file);
        assertFalse(file.exists());
    }

    @Test
    public void testDeleteFileFile() throws IOException {
        File f0 = getFile();
        FileUtils.createFile(f0);
        assertTrue(FileUtils.deleteFile(f0) && !f0.exists());

        File f1 = getFile();
        FileUtils.createDirectory(f1);
        assertTrue(!FileUtils.deleteFile(f1) && f1.exists());
    }

    @Test
    public void testDeleteFileString() throws IOException {
        File f0 = getFile();
        FileUtils.createFile(f0);
        assertTrue(FileUtils.deleteFile(new File(f0.getAbsolutePath())) && !f0.exists());

        File f1 = getFile();
        FileUtils.createDirectory(f1);
        assertTrue(!FileUtils.deleteFile(new File(f1.getAbsolutePath())) && f1.exists());
    }

    @Test
    public void testDeleteDirectoryString() throws IOException {
        File f0 = getFile();
        FileUtils.createFile(f0);
        assertTrue(!FileUtils.deleteDirectory(new File(f0.getAbsolutePath())) && f0.exists());

        File f1 = getFile();
        FileUtils.createDirectory(f1);
        assertTrue(FileUtils.deleteDirectory(new File(f1.getAbsolutePath())) && !f1.exists());
    }

    @Test
    public void testDeleteDirectoryFile() throws IOException {
        File f0 = getFile();
        FileUtils.createFile(f0);
        assertTrue(!FileUtils.deleteDirectory(f0) && f0.exists());

        File f1 = getFile();
        FileUtils.createDirectory(f1);
        assertTrue(FileUtils.deleteDirectory(f1) && !f1.exists());
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

        assertTrue(FileUtils.clearDirectory(dir) && !cdir.exists() && !f1.exists() && !f2.exists());
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

        assertTrue(FileUtils.clearDirectory(new File(dir.getAbsolutePath())) && !cdir.exists() && !f1.exists() && !f2.exists());
    }

    @Test
    public void testGetLineContent() throws IOException {
        File file = getFile();
        FileUtils.write(file, StringUtils.CHARSET, false, "l1\nl2\nl3");
        assertEquals("l1", FileUtils.readline(file, null, 1));
        assertEquals("l2", FileUtils.readline(file, null, 2));
        assertEquals("l3", FileUtils.readline(file, null, 3));
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

        assertTrue(FileUtils.moveFile(dir, dest) && !dir.exists());

        File ff = getFile();
        FileUtils.createFile(ff);
        assertTrue(FileUtils.moveFile(ff, dest) && !dir.exists());
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

        assertTrue(FileUtils.moveFile(new File(dir.getAbsolutePath()), new File(dest.getAbsolutePath())) && !dir.exists());

        File ff = getFile();
        FileUtils.createFile(ff);
        assertTrue(FileUtils.moveFile(new File(ff.getAbsolutePath()), new File(dest.getAbsolutePath())) && !dir.exists());
    }

    @Test
    public void testMoveFileToRecycle() throws IOException {
        File file = getFile();
        File dir = getFile();
        FileUtils.createFile(file);
        FileUtils.createDirectory(dir);

//		File recFile = new File(IOUtils.getSystemRecycle(), file.getName());
//		System.out.println(recFile.getAbsolutePath() + ", " + recFile.exists());

        assertTrue(FileUtils.moveFileToRecycle(file) && !file.exists());
    }

    @Test
    public void testRenameFileStringString() throws IOException {
        File file = getFile();
        FileUtils.delete(file);
        FileUtils.createFile(file);

        File newFile = new File(file.getParentFile(), "test_rename_g.txt");
        assertTrue(FileUtils.delete(newFile) && FileUtils.rename(file, "test_rename_g", ".txt") == 0 && newFile.exists());
    }

    @Test
    public void testRenameFileFile() throws IOException {
        File f0 = getFile();
        FileUtils.delete(f0);
        FileUtils.createFile(f0);

        File f1 = new File(f0.getParentFile(), "renameFileFile.del");
        FileUtils.delete(f1);

        FileUtils.rename(f0, f1);
        assertTrue(true);
    }

    @Test
    public void testByte2Megabyte() {
//		Asserts.assertTrue(Numbers.byte2Megabyte(0) == 0);
//		Asserts.assertTrue(Numbers.byte2Megabyte((long) 1024 * 1024) == 1);
//		Asserts.assertTrue(Numbers.byte2Megabyte((long) 1024 * 1024 * 1024) == 1024);
    }

    @Test
    public void testReplaceFileSeparator() {
        assertEquals("|", FileUtils.replaceFolderSeparator("/", '|'));
        assertEquals("|", FileUtils.replaceFolderSeparator("\\", '|'));
        assertEquals("|home", FileUtils.replaceFolderSeparator("/home", '|'));
        assertEquals("|home|test|shell|qyzx", FileUtils.replaceFolderSeparator("/home/test/shell/qyzx", '|'));
        assertEquals("|home|test|shell|qyzx|", FileUtils.replaceFolderSeparator("/home/test/shell/qyzx\\", '|'));
    }

    @Test
    public void testGetLineSeparator() throws IOException {
        File file = getFile();
        FileUtils.write(file, StringUtils.CHARSET, false, "a\nb\nc\n\n");
        assertEquals("\n", FileUtils.readLineSeparator(file));
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
        assertTrue(StringUtils.isNotBlank(FileUtils.getFilenameRandom("", "")));
    }

    @Test
    public void testGetSystemTempDir() {
        assertTrue(FileUtils.getTempDir(FileUtilsTest.class).exists() && FileUtils.getTempDir(FileUtilsTest.class).isDirectory());
    }

    @Test
    public void testGetSystemRecycle() {
        assertTrue(FileUtils.getRecyDir().exists() && FileUtils.getRecyDir().isDirectory());
        System.out.println(FileUtils.getRecyDir().getAbsolutePath());
    }

    @Test
    public void testCopy() throws IOException {
        File f0 = getFile();
        FileUtils.write(f0, StringUtils.CHARSET, false, "1,2,3,4,5");

        File f1 = new File(f0.getParentFile(), "clone_" + f0.getName());
        assertTrue(FileUtils.copy(f0, f1) && FileUtils.readline(f1, null, 1).equals("1,2,3,4,5"));
    }

    @Test
    public void testWriteFileFileBooleanString() throws IOException {
        File file = getFile();
        FileUtils.write(file, StringUtils.CHARSET, false, "1\n");
        assertEquals("1", FileUtils.readline(file, StringUtils.CHARSET, 1));

        FileUtils.write(file, StringUtils.CHARSET, false, "1\n2");
        assertEquals("1", FileUtils.readline(file, StringUtils.CHARSET, 1));

        FileUtils.write(file, StringUtils.CHARSET, true, "\n3");
        assertEquals("3", FileUtils.readline(file, StringUtils.CHARSET, 3));
    }

    @Test
    public void testWriteFileFileString() throws IOException {
        File file = getFile();
        FileUtils.write(file, StringUtils.CHARSET, false, "1\n2\n3");
        assertEquals("3", FileUtils.readline(file, StringUtils.CHARSET, 3));
    }

    @Test
    public void testEqualsFileFileInt() throws IOException {
        File f0 = getFile();
        File f1 = getFile();

        FileUtils.write(f0, StringUtils.CHARSET, false, "");
        FileUtils.write(f1, StringUtils.CHARSET, false, "");
        assertTrue(FileUtils.equals(f0, f1, 0));

        FileUtils.write(f0, StringUtils.CHARSET, false, "1");
        FileUtils.write(f1, StringUtils.CHARSET, false, "1");
        assertTrue(FileUtils.equals(f0, f1, 0));

        FileUtils.write(f0, StringUtils.CHARSET, false, "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        FileUtils.write(f1, StringUtils.CHARSET, false, "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        assertTrue(FileUtils.equals(f0, f1, 0));

        FileUtils.write(f0, StringUtils.CHARSET, false, "1");
        FileUtils.write(f1, StringUtils.CHARSET, false, "2");
        assertFalse(FileUtils.equals(f0, f1, 0));
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
            fail();
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
                        fail();
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
        assertEquals(1, list.size());
        assertEquals(file, list.get(0));
    }

    @Test
    public void testcreateTempfile() throws IOException {
        File tempfile = FileUtils.createTempfile(String.class, "txt", "testfile");
        System.out.println(tempfile.getAbsolutePath());
        assertTrue(tempfile.exists());

        // 重复创建同一个文件来测试
        FileUtils.createTempfile(String.class, "txt", "testfile");
    }

}
