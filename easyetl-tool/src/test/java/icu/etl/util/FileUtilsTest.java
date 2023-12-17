package icu.etl.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.fail;

public class FileUtilsTest {

    @Test
    public void testcreateTempDirectory() throws IOException {
        // 清空临时文件
        FileUtils.clearDirectory(FileUtils.getTempDir());

        System.out.println(FileUtils.createTempDirectory(null).getAbsolutePath());
        System.out.println(FileUtils.createTempDirectory("").getAbsolutePath());

        File dir1 = FileUtils.createTempDirectory("1");
        Assert.assertEquals("1", dir1.getName());
        System.out.println(dir1.getAbsolutePath());

        File dir2 = FileUtils.createTempDirectory("a");
        Assert.assertEquals("a", dir2.getName());
        System.out.println(dir2.getAbsolutePath());

        File dir3 = FileUtils.createTempDirectory("abc");
        Assert.assertEquals("abc", dir3.getName());
        System.out.println(dir3.getAbsolutePath());
    }

    @Test
    public void testfindFile() throws IOException {
        File root = FileUtils.getTempDir("test", FileUtilsTest.class.getSimpleName());
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

        List<File> fs = FileUtils.find(d0, "20200102.txt");
        Assert.assertEquals(3, fs.size());
        Assert.assertTrue(fs.indexOf(f11) != -1);
        Assert.assertTrue(fs.indexOf(f21) != -1);
        Assert.assertTrue(fs.indexOf(f22) != -1);

        fs = FileUtils.find(d0, "20200102[^ ]{4}");
        Assert.assertEquals(3, fs.size());
        Assert.assertTrue(fs.indexOf(f11) != -1);
        Assert.assertTrue(fs.indexOf(f21) != -1);
        Assert.assertTrue(fs.indexOf(f22) != -1);
    }

    @Test
    public void testReplaceLinSeparator() {
        Assert.assertEquals("", FileUtils.replaceLineSeparator("", ":"));
        Assert.assertNull(FileUtils.replaceLineSeparator(null, ":"));

        Assert.assertEquals("1", FileUtils.replaceLineSeparator("1", ":"));
        Assert.assertEquals("1:22:3:44:", FileUtils.replaceLineSeparator("1\r22\n3\r\n44\r\n", ":"));
        Assert.assertEquals("1:22:3:44", FileUtils.replaceLineSeparator("1\r22\n3\r\n44", ":"));
        Assert.assertEquals("1:22:3:44", FileUtils.replaceLineSeparator("1\r22\n3\r\n44", ":"));
        Assert.assertEquals(("1" + FileUtils.lineSeparator + "22" + FileUtils.lineSeparator + "3" + FileUtils.lineSeparator + "44"), FileUtils.replaceLineSeparator("1\r22\n3\r\n44"));
    }

    @Test
    public void test0111() {
        Assert.assertNull(FileUtils.getParent(null));
        Assert.assertNull(FileUtils.getParent(""));
        Assert.assertNull(FileUtils.getParent("/"));
        Assert.assertNull(FileUtils.getParent("/1"));
        Assert.assertEquals("/1", FileUtils.getParent("/1/2"));
        Assert.assertEquals("/1", FileUtils.getParent("/1/2/"));
        Assert.assertEquals("/1/2", FileUtils.getParent("/1/2/3.txt"));
    }

    @Test
    public void testdos2unix() throws IOException {
        File file = FileUtils.createTempFile(null);
        Assert.assertTrue(FileUtils.write(file, StringUtils.CHARSET, false, "1\r\n2\r\n3\r\n"));

        String nt = FileUtils.readline(file, StringUtils.CHARSET, 0);
        Assert.assertEquals("1\\r\\n2\\r\\n3\\r\\n", StringUtils.escapeLineSeparator(nt));
        Assert.assertTrue(nt.indexOf(FileUtils.lineSeparatorWindows) != -1);
        Assert.assertTrue(FileUtils.dos2unix(file, StringUtils.CHARSET, null));

        String text = FileUtils.readline(file, StringUtils.CHARSET, 0);
        Assert.assertEquals(-1, text.indexOf(FileUtils.lineSeparatorWindows));
    }

    @Test
    public void testExists() throws IOException {
        File file = FileUtils.createTempDirectory(null);
        System.out.println(file.getAbsolutePath());

        Assert.assertTrue(FileUtils.delete(file));
        Assert.assertFalse(FileUtils.exists(file.getAbsolutePath()));

        Assert.assertTrue(FileUtils.createFile(file));
        Assert.assertTrue(FileUtils.exists(file.getAbsolutePath()));
    }

    @Test
    public void testCleanFileFile() throws IOException {
        File file = FileUtils.createTempFile(null);
        FileUtils.write(file, StringUtils.CHARSET, false, "测试内容是否删除");
        Assert.assertTrue(FileUtils.clearFile(file));
        Assert.assertEquals(0, file.length());
    }

    @Test
    public void testCleanFileString() throws IOException {
        File file = FileUtils.createTempFile(null);
        Assert.assertTrue(FileUtils.write(file, StringUtils.CHARSET, false, "测试内容是否"));
        Assert.assertTrue(FileUtils.clearFile(file) && file.length() == 0);
    }

    @Test
    public void testIsFileFile() throws IOException {
        File file = FileUtils.createTempFile(null);
        Assert.assertTrue(FileUtils.delete(file));
        Assert.assertTrue(FileUtils.createFile(file) && FileUtils.isFile(file));
    }

    @Test
    public void testIsFileString() throws IOException {
        File file = FileUtils.createTempFile(null);
        Assert.assertTrue(FileUtils.delete(file));
        Assert.assertTrue(FileUtils.createFile(file));
        Assert.assertTrue(FileUtils.isFile(file.getAbsolutePath()));

        Assert.assertTrue(FileUtils.delete(file));
        Assert.assertTrue(file.mkdirs());
        Assert.assertFalse(FileUtils.isFile(file.getAbsolutePath()));
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
        File file = FileUtils.createTempDirectory(null);
        Assert.assertTrue(FileUtils.delete(file));

        Assert.assertTrue(FileUtils.createDirectory(file));
        Assert.assertTrue(FileUtils.isDirectory(file.getAbsolutePath()));

        Assert.assertTrue(FileUtils.delete(file));
        Assert.assertTrue(FileUtils.createFile(file));
        Assert.assertFalse(FileUtils.isDirectory(file.getAbsolutePath()));
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
        Assert.assertEquals("", FileUtils.getFilename(""));
        Assert.assertEquals("test", FileUtils.getFilename("/home/test/test"));
        Assert.assertEquals("test.", FileUtils.getFilename("/home/test/test."));
        Assert.assertEquals("test.txt", FileUtils.getFilename("/home/test/test.txt"));
        Assert.assertEquals("test", FileUtils.getFilename("/home/test./test"));
        Assert.assertEquals("test", FileUtils.getFilename("/home/.test\\test"));
        Assert.assertEquals("test.txt", FileUtils.getFilename("/home/.test\\test.txt"));
        Assert.assertEquals(".txt", FileUtils.getFilename("/home/.test\\.txt"));
        Assert.assertEquals(".txt", FileUtils.getFilename("/home/.test/.txt"));
    }

    @Test
    public void testGetFilenameNoExt() {
        Assert.assertEquals("test", FileUtils.getFilenameNoExt("/home/test/shell/test.txt"));
        Assert.assertEquals("test", FileUtils.getFilenameNoExt("/home/test/shell/test."));
        Assert.assertEquals("test", FileUtils.getFilenameNoExt("/home/test/shell/test"));
        Assert.assertEquals("test", FileUtils.getFilenameNoExt("shell/test"));
        Assert.assertEquals("test", FileUtils.getFilenameNoExt("test"));
        Assert.assertEquals("t", FileUtils.getFilenameNoExt("t"));
        Assert.assertEquals("", FileUtils.getFilenameNoExt(""));
        Assert.assertNull(FileUtils.getFilenameNoExt(null));
    }

    @Test
    public void testGetFilenameNoSuffix() {
        Assert.assertNull(FileUtils.getFilenameNoSuffix(null));
        Assert.assertEquals("1", FileUtils.getFilenameNoSuffix("1.del"));
        Assert.assertEquals("", FileUtils.getFilenameNoSuffix("."));
        Assert.assertEquals("", FileUtils.getFilenameNoSuffix(".del.gz"));
        Assert.assertEquals("INC_QYZX_ECC_LACKOFINTERESTS18", FileUtils.getFilenameNoSuffix("INC_QYZX_ECC_LACKOFINTERESTS18.del.gz"));
        Assert.assertEquals("INC_QYZX_ECC_LACKOFINTERESTS18", FileUtils.getFilenameNoSuffix("D:\\home\\test\\INC_QYZX_ECC_LACKOFINTERESTS18.del.gz"));
        Assert.assertEquals("INC_QYZX_ECC_LACKOFINTERESTS18", FileUtils.getFilenameNoSuffix("D:\\home\\test\\INC_QYZX_ECC_LACKOFINTERESTS18.del"));
    }

    @Test
    public void testGetFilenameExt() {
        Assert.assertEquals("", FileUtils.getFilenameExt(""));
        Assert.assertEquals("", FileUtils.getFilenameExt("/home/test/test"));
        Assert.assertEquals("", FileUtils.getFilenameExt("/home/test/test."));
        Assert.assertEquals("txt", FileUtils.getFilenameExt("/home/test/test.txt"));
        Assert.assertEquals("", FileUtils.getFilenameExt("/home/test./test"));
        Assert.assertEquals("", FileUtils.getFilenameExt("/home/.test\\test"));
        Assert.assertEquals("txt", FileUtils.getFilenameExt("/home/.test\\test.txt"));
        Assert.assertEquals("txt", FileUtils.getFilenameExt("/home/.test\\.txt"));
        Assert.assertEquals("txt", FileUtils.getFilenameExt("/home/.test/.txt"));
    }

    @Test
    public void testGetFilenameSuffix() {
        Assert.assertNull(FileUtils.getFilenameSuffix(null));
        Assert.assertEquals("", FileUtils.getFilenameSuffix(""));
        Assert.assertEquals("", FileUtils.getFilenameSuffix("1"));
        Assert.assertEquals("", FileUtils.getFilenameSuffix("1."));
        Assert.assertEquals("d", FileUtils.getFilenameSuffix("1.d"));
        Assert.assertEquals("del", FileUtils.getFilenameSuffix("1.del"));
        Assert.assertEquals("del.gz", FileUtils.getFilenameSuffix("1.del.gz"));
        Assert.assertEquals("del.gz", FileUtils.getFilenameSuffix("\\1.del.gz"));
        Assert.assertEquals("del.gz", FileUtils.getFilenameSuffix("/1.del.gz"));
        Assert.assertEquals("del.gz", FileUtils.getFilenameSuffix("1/1.del.gz"));
    }

    @Test
    public void testStripFilenameExt() {
        Assert.assertNull(FileUtils.removeFilenameExt(null));
        Assert.assertEquals("", FileUtils.removeFilenameExt(""));
        Assert.assertEquals("/home/test/", FileUtils.removeFilenameExt("/home/test/"));
        Assert.assertEquals("/home/test/test", FileUtils.removeFilenameExt("/home/test/test.txt"));
        Assert.assertEquals("/home/test/test", FileUtils.removeFilenameExt("/home/test/test"));
        Assert.assertEquals("/home/.test/test", FileUtils.removeFilenameExt("/home/.test/test"));
        Assert.assertEquals("/home/test/test", FileUtils.removeFilenameExt("/home/test/test."));
        Assert.assertEquals("/home/test/", FileUtils.removeFilenameExt("/home/test/.test"));

        Assert.assertEquals("/home/test\\", FileUtils.removeFilenameExt("/home/test\\"));
        Assert.assertEquals("/home/test\\test", FileUtils.removeFilenameExt("/home/test\\test.txt"));
        Assert.assertEquals("/home/test\\test", FileUtils.removeFilenameExt("/home/test\\test"));
        Assert.assertEquals("/home/.test\\test", FileUtils.removeFilenameExt("/home/.test\\test"));
        Assert.assertEquals("/home/test\\test", FileUtils.removeFilenameExt("/home/test\\test."));
        Assert.assertEquals("/home/test\\", FileUtils.removeFilenameExt("/home/test\\.test"));
    }

    @Test
    public void testGetNoRepeatFilename() throws IOException {
        File tempDir = FileUtils.createTempDirectory(null);
        File parent = new File(tempDir, Dates.format08(new Date()));
        Assert.assertTrue(FileUtils.createDirectory(parent));

        // 先创建一个文件
        File file = new File(parent, "test_repeat_file.dat.tmp");
        Assert.assertTrue(FileUtils.createFile(file));

        // 再创建一个不重名文件
        File newfile = FileUtils.allocate(parent, "test_repeat_file.dat.tmp");
        Assert.assertNotEquals(newfile, file);
        Assert.assertTrue(FileUtils.createFile(newfile));

        // 再创建一个不重名文件
        File newfile1 = FileUtils.allocate(parent, "test_repeat_file.dat.tmp");
        Assert.assertNotEquals(newfile1, file);
        Assert.assertNotEquals(newfile1, newfile);
        Assert.assertTrue(FileUtils.createFile(newfile1));
    }

    @Test
    public void testcreateUniqueFile() {
        File tempDir = FileUtils.createTempDirectory(null);
        File newfile = FileUtils.allocate(tempDir, "test_repeat_file.dat");
        Assert.assertTrue(FileUtils.createFile(newfile));
    }

    @Test
    public void testGetResourceAsStream() throws IOException {
        Assert.assertNotNull(FileUtils.loadProperties("/testfile.properties"));
        Assert.assertNotNull(FileUtils.loadProperties("classpath:/testfile.properties"));
    }

    @Test
    public void testCreateFileString() throws IOException {
        File file = FileUtils.createTempFile(null);
        Assert.assertTrue(FileUtils.delete(file));
        Assert.assertFalse(file.exists());

        Assert.assertTrue(FileUtils.createFile(file));
        Assert.assertTrue(file.exists());
    }

    @Test
    public void testCreateFileFile() throws IOException {
        File file = FileUtils.createTempFile(null);
        Assert.assertTrue(FileUtils.delete(file));
        Assert.assertFalse(file.exists());

        Assert.assertTrue(FileUtils.createFile(file));
        Assert.assertTrue(file.exists());
    }

    @Test
    public void testCreatefileFile() throws IOException {
        File file = FileUtils.createTempFile(null);
        Assert.assertTrue(FileUtils.delete(file));
        Assert.assertTrue(FileUtils.createFile(file) && file.exists());
    }

    @Test
    public void testCreatefileString() throws IOException {
        File file = FileUtils.createTempFile(null);
        Assert.assertTrue(FileUtils.delete(file));
        Assert.assertTrue(FileUtils.createFile(file) && file.exists());
    }

    @Test
    public void testCreatefileFileBoolean() throws IOException {
        Assert.assertFalse(FileUtils.createFile(null, true));

        File file = FileUtils.createTempDirectory(null);
        Assert.assertTrue(FileUtils.delete(file));
        Assert.assertTrue(FileUtils.createDirectory(file));
        Assert.assertTrue(FileUtils.createFile(file, true)); // 测试强制新建文件
    }

    @Test
    public void testCreatefileFileBoolean1() throws IOException {
        File file = FileUtils.createTempDirectory(null);
        Assert.assertTrue(FileUtils.delete(file));

        String path = FileUtils.joinPath(file.getAbsolutePath(), "a", "b", "c");
        File dir = new File(path);
        Assert.assertTrue(FileUtils.createDirectory(dir));
        Assert.assertTrue(FileUtils.isDirectory(dir));
    }

    @Test
    public void testCreateDirecotryString() {
        File file = FileUtils.createTempDirectory(null);
        FileUtils.delete(file);
        Assert.assertTrue(FileUtils.createDirectory(file));
        Assert.assertTrue(file.exists() && file.isDirectory());
    }

    @Test
    public void testCreateDirecotryFile() {
        File file = FileUtils.createTempDirectory(null);
        Assert.assertTrue(FileUtils.delete(file));
        Assert.assertTrue(FileUtils.createDirectory(file));
        Assert.assertTrue(file.exists() && file.isDirectory());
    }

    @Test
    public void testCreateDirectoryString() {
        File file = FileUtils.createTempDirectory(null);
        Assert.assertTrue(FileUtils.delete(file));
        Assert.assertTrue(FileUtils.createDirectory(file) && file.exists() && file.isDirectory());
    }

    @Test
    public void testCreateDirectoryFile() {
        File file = FileUtils.createTempDirectory(null);
        Assert.assertTrue(FileUtils.delete(file));
        Assert.assertTrue(FileUtils.createDirectory(file) && file.exists() && file.isDirectory());
    }

    @Test
    public void testCreateDirectoryFileBoolean() throws IOException {
        File file = FileUtils.createTempDirectory(null);
        Assert.assertTrue(FileUtils.delete(file));
        Assert.assertTrue(FileUtils.createFile(file));
        Assert.assertTrue(FileUtils.createDirectory(file, true) && file.exists() && file.isDirectory());

        File file1 = FileUtils.getTempDir("test", FileUtilsTest.class.getSimpleName(), "dirdir0000");
        Assert.assertTrue(FileUtils.delete(file1));
        Assert.assertTrue(file1.mkdirs());

        File f0 = new File(file1, FileUtils.joinPath("t1", "t2", "t3"));
        Assert.assertTrue(FileUtils.createDirectory(f0) && f0.exists() && f0.isDirectory());
        System.out.println("f0: " + f0.getAbsolutePath());
    }

    @Test
    public void testTranslateSeperator() {
        String str1 = "/home/test/shell/qyzx/";
        String str2 = StringUtils.replaceAll(str1, "/", File.separator);
        Assert.assertEquals(FileUtils.replaceFolderSeparator(str1), str2);
    }

    @Test
    public void testSpellFileStringString() {
        Assert.assertEquals(FileUtils.joinPath("/home/test", "shell"), "/home/test" + File.separator + "shell");
        Assert.assertEquals(FileUtils.joinPath("/home/test", "shell/qyzx"), "/home/test" + File.separator + "shell/qyzx");
    }

    @Test
    public void testSpellFileStringArray() {
        Assert.assertEquals(FileUtils.joinPath(new String[]{"home", "test", "shell", "grzx"}), "home" + File.separator + "test" + File.separator + "shell" + File.separator + "grzx");
    }

    @Test
    public void testRemoveEndFileSeparator() {
        Assert.assertEquals("\\home\\test\\shell\\mulu", FileUtils.rtrimFolderSeparator("\\home\\test\\shell\\mulu\\"));
        Assert.assertEquals("\\home\\test\\shell\\mulu", FileUtils.rtrimFolderSeparator("\\home\\test\\shell\\mulu\\/"));
    }

    @Test
    public void testChangeFilenameExt() {
        Assert.assertEquals("C:/test/ceshi/test.enc", FileUtils.changeFilenameExt("C:/test/ceshi/test.txt", "enc"));
        Assert.assertEquals("C:/test/.ceshi/test.enc", FileUtils.changeFilenameExt("C:/test/.ceshi/test.txt", "enc"));
        Assert.assertEquals("C:\\.test\\.ceshi\\test.enc", FileUtils.changeFilenameExt("C:\\.test\\.ceshi\\test.txt", "enc"));
        Assert.assertEquals("C:/test/.ceshi/.test.enc", FileUtils.changeFilenameExt("C:/test/.ceshi/.test.txt", "enc"));
        Assert.assertEquals("C:/test/.ceshi/.enc", FileUtils.changeFilenameExt("C:/test/.ceshi/.test", "enc"));
        Assert.assertEquals("C:/test/.ceshi/test.enc", FileUtils.changeFilenameExt("C:/test/.ceshi/test", "enc"));
    }

    @Test
    public void testLoadPropertiesFile() throws IOException {
        File file = FileUtils.createTempFile("a.properties");
        FileOutputStream out = new FileOutputStream(file);
        Properties p = new Properties();
        p.put("path", "/home/user/shell/test/execute.properties");
        p.store(out, "测试");

        Properties np = FileUtils.loadProperties(file.getAbsolutePath());
        Assert.assertEquals("/home/user/shell/test/execute.properties", np.getProperty("path"));
    }

    @Test
    public void testWriteProperties() throws IOException {
        Properties p = new Properties();
        p.put("path", "/home/user/shell/test/config.properties");

        File file = FileUtils.createTempFile(".properties");
        System.out.println(file.getAbsolutePath());
        Assert.assertTrue(FileUtils.store(p, file));

        Properties nc = FileUtils.loadProperties(file.getAbsolutePath());
        Assert.assertEquals("/home/user/shell/test/config.properties", nc.getProperty("path"));
    }

    @Test
    public void testDelete() throws IOException {
        File file = FileUtils.createTempFile(null);
        Assert.assertTrue(FileUtils.createFile(file));
        Assert.assertTrue(FileUtils.delete(file));
        Assert.assertFalse(file.exists());

        Assert.assertTrue(FileUtils.delete(file));
        Assert.assertTrue(FileUtils.createDirectory(file));
        Assert.assertTrue(FileUtils.delete(file));
        Assert.assertFalse(file.exists());
    }

    @Test
    public void testDeleteFileFile() throws IOException {
        File f0 = FileUtils.createTempFile(null);
        FileUtils.createFile(f0);
        Assert.assertTrue(FileUtils.deleteFile(f0) && !f0.exists());

        File f1 = FileUtils.createTempDirectory(null);
        Assert.assertTrue(FileUtils.createDirectory(f1));
        Assert.assertTrue(!FileUtils.deleteFile(f1) && f1.exists());
    }

    @Test
    public void testDeleteFileString() throws IOException {
        File f0 = FileUtils.createTempFile(null);
        FileUtils.createFile(f0);
        Assert.assertTrue(FileUtils.deleteFile(new File(f0.getAbsolutePath())) && !f0.exists());

        File f1 = FileUtils.createTempDirectory(null);
        Assert.assertTrue(FileUtils.createDirectory(f1));
        Assert.assertTrue(!FileUtils.deleteFile(new File(f1.getAbsolutePath())) && f1.exists());
    }

    @Test
    public void testDeleteDirectoryString() throws IOException {
        File f0 = FileUtils.createTempFile(null);
        Assert.assertTrue(FileUtils.createFile(f0));
        Assert.assertTrue(!FileUtils.deleteDirectory(new File(f0.getAbsolutePath())) && f0.exists());

        File f1 = FileUtils.createTempDirectory(null);
        Assert.assertTrue(FileUtils.createDirectory(f1));
        Assert.assertTrue(FileUtils.deleteDirectory(new File(f1.getAbsolutePath())) && !f1.exists());
    }

    @Test
    public void testDeleteDirectoryFile() throws IOException {
        File f0 = FileUtils.createTempFile(null);
        FileUtils.assertCreateFile(f0);
        Assert.assertTrue(!FileUtils.deleteDirectory(f0) && f0.exists());

        File dir1 = FileUtils.createTempDirectory(null);
        Assert.assertTrue(FileUtils.createDirectory(dir1));
        Assert.assertTrue(FileUtils.deleteDirectory(dir1) && !dir1.exists());
    }

    @Test
    public void testCleanDirectoryString() throws IOException {
        File dir = FileUtils.createTempDirectory(null);
        Assert.assertTrue(FileUtils.createDirectory(dir));

        File cdir = new File(dir, "cdir");
        Assert.assertTrue(FileUtils.createDirectory(cdir));

        File f1 = new File(cdir, "test.del");
        Assert.assertTrue(FileUtils.createFile(f1));

        File f2 = new File(dir, "test.del");
        Assert.assertTrue(FileUtils.createFile(f2));
        Assert.assertTrue(FileUtils.clearDirectory(dir) && !cdir.exists() && !f1.exists() && !f2.exists());
    }

    @Test
    public void testCleanDirectoryString1() throws IOException {
        File dir = FileUtils.createTempDirectory(null);
        Assert.assertTrue(FileUtils.delete(dir));
        Assert.assertTrue(FileUtils.clearDirectory(dir));

        Assert.assertTrue(FileUtils.delete(dir));
        Assert.assertTrue(FileUtils.createFile(dir));
        Assert.assertFalse(FileUtils.clearDirectory(dir));
    }

    @Test
    public void testCleanDirectoryFile() throws IOException {
        File dir = FileUtils.createTempDirectory(null);
        Assert.assertTrue(FileUtils.createDirectory(dir));

        File cdir = new File(dir, "cdir");
        Assert.assertTrue(FileUtils.createDirectory(cdir));

        File f1 = new File(cdir, "test.del");
        Assert.assertTrue(FileUtils.createFile(f1));

        File f2 = new File(dir, "test.del");
        Assert.assertTrue(FileUtils.createFile(f2));
        Assert.assertTrue(FileUtils.clearDirectory(dir) && !cdir.exists() && !f1.exists() && !f2.exists());
    }

    @Test
    public void testGetLineContent() throws IOException {
        File file = FileUtils.createTempFile(null);
        Assert.assertTrue(FileUtils.write(file, StringUtils.CHARSET, false, "l1\nl2\nl3"));
        Assert.assertEquals("l1", FileUtils.readline(file, null, 1));
        Assert.assertEquals("l2", FileUtils.readline(file, null, 2));
        Assert.assertEquals("l3", FileUtils.readline(file, null, 3));
    }

    @Test
    public void testrename() throws IOException {
        File dir = FileUtils.getTempDir("test", Dates.format17());

        File f1 = new File(dir, "test1.txt");
        FileUtils.assertCreateFile(f1);
        File f2 = new File(dir, "test2.txt");
        FileUtils.assertDelete(f2);

        Assert.assertTrue(FileUtils.rename(f1, f2, null));
        Assert.assertFalse(f1.exists());
        Assert.assertTrue(f2.exists());
    }

    @Test
    public void testrename1() throws IOException {
        File dir = FileUtils.getTempDir("test", Dates.format17());

        File f1 = new File(dir, "test1.txt");
        FileUtils.assertCreateFile(f1);

        File f2 = new File(dir, "test2.txt");
        FileUtils.assertCreateFile(f2);

        Assert.assertTrue(FileUtils.rename(f1, f2, null));
        Assert.assertFalse(f1.exists());
        Assert.assertTrue(f2.exists());
    }

    @Test
    public void testrename2() throws IOException {
        File dir = FileUtils.getTempDir("test", Dates.format17());

        File dir1 = new File(dir, Dates.format17());
        FileUtils.assertCreateDirectory(dir1);
        Assert.assertTrue(FileUtils.clearDirectory(dir1));

        File f1 = new File(dir, "test1.txt");
        FileUtils.assertCreateFile(f1);

        File f2 = new File(dir, "test2.txt");
        FileUtils.assertCreateFile(f2);

        File newfile2 = new File(dir1, f2.getName());
        Assert.assertTrue(FileUtils.rename(f1, f2, newfile2));
        Assert.assertFalse(f1.exists());
        Assert.assertTrue(f2.exists());
        Assert.assertTrue(newfile2.exists());
    }

    @Test
    public void testrename3() throws IOException {
        File dir = FileUtils.getTempDir("test", Dates.format17());

        File f1 = new File(dir, "test1.txt");
        FileUtils.assertCreateFile(f1);

        File dir1 = new File(dir, Dates.format17());
        FileUtils.assertCreateDirectory(dir1);

        File f2 = new File(dir1, "test2.txt");
        FileUtils.assertCreateFile(f2);
        Assert.assertTrue(FileUtils.rename(f1, f2, null));
        Assert.assertFalse(f1.exists());
        Assert.assertTrue(f2.exists());
    }

    @Test
    public void testMoveFileToRecycle() throws IOException {
        File file = FileUtils.createTempFile(null);
        File dir = FileUtils.createTempDirectory(null);
        Assert.assertTrue(FileUtils.createFile(file));
        Assert.assertTrue(FileUtils.createDirectory(dir));
        Assert.assertTrue(FileUtils.move2Recycle(file) && !file.exists());
    }

    @Test
    public void testcreateFileQuiet() throws IOException {
        File file = new FileProxy(FileUtils.createTempDirectory(null));
        Assert.assertFalse(FileUtils.Atomic.createFileQuiet(file));
        System.out.println(file.getAbsolutePath());
    }

    @Test
    public void createDirectory() {
        File parent = FileUtils.createTempDirectory(null);
        FileUtils.createDirectory(parent, "a", "b", "c", "d");

        parent = FileUtils.createTempDirectory(null);
        FileUtils.createDirectory(parent, "a");

        parent = FileUtils.createTempDirectory(null);
        FileUtils.createDirectory(parent);
    }

    private static class FileProxy extends File {
        public FileProxy(File file) {
            super(file.getAbsolutePath());
        }

        public boolean createNewFile() throws IOException {
            throw new IOException("test createNewFile()");
        }
    }

    @Test
    public void testByte2Megabyte() {
//		Asserts.assertTrue(Numbers.byte2Megabyte(0) == 0);
//		Asserts.assertTrue(Numbers.byte2Megabyte((long) 1024 * 1024) == 1);
//		Asserts.assertTrue(Numbers.byte2Megabyte((long) 1024 * 1024 * 1024) == 1024);
    }

    @Test
    public void testReplaceFileSeparator() {
        Assert.assertEquals("|", FileUtils.replaceFolderSeparator("/", '|'));
        Assert.assertEquals("|", FileUtils.replaceFolderSeparator("\\", '|'));
        Assert.assertEquals("|home", FileUtils.replaceFolderSeparator("/home", '|'));
        Assert.assertEquals("|home|test|shell|mulu", FileUtils.replaceFolderSeparator("/home/test/shell/mulu", '|'));
        Assert.assertEquals("|home|test|shell|mulu|", FileUtils.replaceFolderSeparator("/home/test/shell/mulu\\", '|'));
    }

    @Test
    public void testGetLineSeparator() throws IOException {
        File file = FileUtils.createTempFile(null);
        Assert.assertTrue(FileUtils.write(file, StringUtils.CHARSET, false, "a\nb\nc\n\n"));
        Assert.assertEquals("\n", FileUtils.readLineSeparator(file));
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
    }

    @Test
    public void testGetSystemTempDir() {
        Assert.assertTrue(FileUtils.getTempDir("test", FileUtilsTest.class.getSimpleName()).exists() && FileUtils.getTempDir("test", FileUtilsTest.class.getSimpleName()).isDirectory());
    }

    @Test
    public void testGetSystemRecycle() {
        Assert.assertTrue(FileUtils.getRecycle().exists() && FileUtils.getRecycle().isDirectory());
        System.out.println(FileUtils.getRecycle().getAbsolutePath());
    }

    @Test
    public void testCopy() throws IOException {
        File f0 = FileUtils.createTempFile(null);
        FileUtils.write(f0, StringUtils.CHARSET, false, "1,2,3,4,5");

        File f1 = new File(f0.getParentFile(), "clone_" + f0.getName());
        Assert.assertTrue(FileUtils.copy(f0, f1) && FileUtils.readline(f1, null, 1).equals("1,2,3,4,5"));
    }

    @Test
    public void testWriteFileFileBooleanString() throws IOException {
        File file = FileUtils.createTempFile(null);
        Assert.assertTrue(FileUtils.write(file, StringUtils.CHARSET, false, "1\n"));
        Assert.assertEquals("1", FileUtils.readline(file, StringUtils.CHARSET, 1));

        Assert.assertTrue(FileUtils.write(file, StringUtils.CHARSET, false, "1\n2"));
        Assert.assertEquals("1", FileUtils.readline(file, StringUtils.CHARSET, 1));

        Assert.assertTrue(FileUtils.write(file, StringUtils.CHARSET, true, "\n3"));
        Assert.assertEquals("3", FileUtils.readline(file, StringUtils.CHARSET, 3));
    }

    @Test
    public void testWriteFileFileString() throws IOException {
        File file = FileUtils.createTempFile(null);
        Assert.assertTrue(FileUtils.write(file, StringUtils.CHARSET, false, "1\n2\n3"));
        Assert.assertEquals("3", FileUtils.readline(file, StringUtils.CHARSET, 3));
    }

    @Test
    public void testwrite() throws IOException {
        File file = FileUtils.createTempFile(null);
        Assert.assertTrue(FileUtils.write(file, StringUtils.CHARSET, false, "1\n2\n3"));
        Assert.assertEquals("3", FileUtils.readline(file, StringUtils.CHARSET, 3));

        File file1 = FileUtils.createTempFile(null);
        FileInputStream in = new FileInputStream(file);
        Assert.assertTrue(FileUtils.write(file1, StringUtils.CHARSET, false, in));
        in.close();
        Assert.assertEquals("3", FileUtils.readline(file1, StringUtils.CHARSET, 3));
    }

    @Test
    public void testEqualsFileFileInt() throws IOException {
        File f0 = FileUtils.createTempFile(null);
        File f1 = FileUtils.createTempFile(null);

        Assert.assertTrue(FileUtils.write(f0, StringUtils.CHARSET, false, ""));
        Assert.assertTrue(FileUtils.write(f1, StringUtils.CHARSET, false, ""));
        Assert.assertTrue(FileUtils.equals(f0, f1, 0));

        Assert.assertTrue(FileUtils.write(f0, StringUtils.CHARSET, false, "1"));
        Assert.assertTrue(FileUtils.write(f1, StringUtils.CHARSET, false, "1"));
        Assert.assertTrue(FileUtils.equals(f0, f1, 0));

        Assert.assertTrue(FileUtils.write(f0, StringUtils.CHARSET, false, "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"));
        Assert.assertTrue(FileUtils.write(f1, StringUtils.CHARSET, false, "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"));
        Assert.assertTrue(FileUtils.equals(f0, f1, 0));

        Assert.assertTrue(FileUtils.write(f0, StringUtils.CHARSET, false, "1"));
        Assert.assertTrue(FileUtils.write(f1, StringUtils.CHARSET, false, "2"));
        Assert.assertFalse(FileUtils.equals(f0, f1, 0));
    }

    @Test
    public void testEqualsIgnoreLineSeperator() throws IOException {
        File f0 = FileUtils.createTempFile(null);
        File f1 = FileUtils.createTempFile(null);

        Assert.assertTrue(FileUtils.write(f0, StringUtils.CHARSET, false, "1"));
        Assert.assertTrue(FileUtils.write(f1, StringUtils.CHARSET, false, "1"));
        Assert.assertEquals(FileUtils.equalsIgnoreLineSeperator(f0, StringUtils.CHARSET, f1, StringUtils.CHARSET, 0), 0);

        Assert.assertTrue(FileUtils.write(f0, StringUtils.CHARSET, false, "1234567\n890123456789012345678\r90123456789012345\r\n678901234567890123456789012345\n678901234567890"));
        Assert.assertTrue(FileUtils.write(f1, StringUtils.CHARSET, false, "1234567\r\n890123456789012345678\n90123456789012345\n678901234567890123456789012345\r\n678901234567890"));
        Assert.assertEquals(FileUtils.equalsIgnoreLineSeperator(f0, StringUtils.CHARSET, f1, StringUtils.CHARSET, 0), 0);
    }

    @Test
    public void test100() throws IOException, InterruptedException {
        File parent = FileUtils.createTempDirectory(null);
        FileUtils.assertClearDirectory(parent);

        final File file = new File(parent, "test.del");
        Assert.assertTrue(FileUtils.write(file, "utf-8", false, "testset"));
        System.out.println("before: " + FileUtils.readline(file, "utf-8", 0));

        Thread thread = new Thread() {
            public void run() {
                try {
                    Thread.sleep(1000);
                    String str = "test" + Dates.currentTimeStamp();
                    System.out.println("写入: " + str + " > " + file.getAbsolutePath());
                    FileUtils.write(file, "utf-8", false, str);
                } catch (Exception e) {
                    e.printStackTrace();
                    fail();
                }
            }
        };
        thread.start();

        List<File> list = FileUtils.isWriting(parent, 2000);
        System.out.println("主线程恢复运行 ..");
        System.out.println(" after: " + FileUtils.readline(file, "utf-8", 0));
        Assert.assertEquals(1, list.size());
        Assert.assertEquals(file, list.get(0));
    }

    @Test
    public void testcreateTempfile() throws IOException {
        File tempfile = FileUtils.createTempFile("testfile.txt");
        System.out.println(tempfile.getAbsolutePath());
        Assert.assertTrue(tempfile.exists());

        // 重复创建同一个文件来测试
        File newfile = FileUtils.createTempFile("testfile.txt");
        System.out.println(newfile.getAbsolutePath());
        Assert.assertNotEquals(tempfile, newfile);
    }

}
