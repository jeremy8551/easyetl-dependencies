package icu.etl.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 文件帮助类
 *
 * @author jeremy8551@qq.com
 * @createtime 2009-12-19 18:00:54
 */
public final class FileUtils {

    /** JDK日志输出接口 */
    private final static Logger log = Logger.getLogger(FileUtils.class.getName());

    /** 文件系统文件路径的分隔符集合 */
    public final static List<String> pathSeparators = java.util.Collections.unmodifiableList(ArrayUtils.asList("/", "\\"));

    /** 文件系统的换行符 */
    public final static String lineSeparator = System.getProperty("line.separator");

    /** windows文件系统的换行符 */
    public final static String lineSeparatorWindows = "\r\n";

    /** unix文件系统的换行符 */
    public final static String lineSeparatorUnix = "\n";

    /** mac系统换行符 */
    public final static String lineSeparatorMacOS = "\r";

    /** 如果文件小于10M，则读取全部内容进行格式转换 */
    public static long DOC2UNIX_FILESIZE = 1048576;

    public FileUtils() {
    }

    /**
     * 如果数组参数为null，则返回一个长度为0的空数组
     *
     * @param array 数组参数
     * @return 数组参数
     */
    public static File[] array(File... array) {
        return array == null ? new File[0] : array;
    }

    /**
     * 文件参数file存在且是一个文件时返回true
     *
     * @param file 文件
     * @return
     */
    public static boolean isFile(File file) {
        return file != null && file.exists() && file.isFile();
    }

    /**
     * 文件路径参数filepath存在且是一个文件时返回true
     *
     * @param filepath 文件绝对路径
     * @return
     */
    public static boolean isFile(String filepath) {
        return filepath != null && FileUtils.isFile(new File(filepath));
    }

    /**
     * 判断文件参数file是否存在且是一个有效目录
     *
     * @param file 文件
     * @return
     */
    public static boolean isDirectory(File file) {
        return file != null && file.exists() && file.isDirectory();
    }

    /**
     * 判断文件路径参数filepath是否存在且是一个有效目录
     *
     * @param filepath 文件绝对路径
     * @return
     */
    public static boolean isDirectory(String filepath) {
        return filepath != null && FileUtils.isDirectory(new File(filepath));
    }

    /**
     * 检查文件权限
     *
     * @param file  文件
     * @param read  true表示检查文件是否可读
     * @param write true表示检查文件是否可写
     */
    public static void checkPermission(File file, boolean read, boolean write) {
        if (file == null) {
            throw new NullPointerException();
        }

        String str = "";
        if (!file.exists()) {
            if (StringUtils.isNotBlank(str)) {
                str += ", ";
            }
            str += ResourcesUtils.getCommonMessage(18);
        }

        if (!file.isFile()) {
            if (StringUtils.isNotBlank(str)) {
                str += ", ";
            }
            str += ResourcesUtils.getCommonMessage(19);
        }

        if (read && !file.canRead()) {
            if (StringUtils.isNotBlank(str)) {
                str += ", ";
            }
            str += ResourcesUtils.getCommonMessage(20);
        }

        if (write && !file.canWrite()) {
            if (StringUtils.isNotBlank(str)) {
                str += ", ";
            }
            str += ResourcesUtils.getCommonMessage(21);
        }

        if (StringUtils.isNotBlank(str)) {
            throw new IllegalArgumentException(ResourcesUtils.getCommonMessage(17, file.getAbsolutePath()) + str);
        }
    }

    /**
     * 创建文件（非目录）
     *
     * @param file 文件
     * @return 返回 true 表示创建文件成功
     * @throws IOException 创建文件发生错误
     */
    public static boolean createFile(File file) throws IOException {
        return FileUtils.createFile(file, false);
    }

    /**
     * 创建文件（非目录）
     *
     * @param file  文件
     * @param force true表示删除目录创建文件
     * @return 返回 true 表示创建文件成功
     * @throws IOException 创建文件发生错误
     */
    public static boolean createFile(File file, boolean force) throws IOException {
        if (file == null) {
            return false;
        }

        // 文件已存在
        if (file.exists()) {
            if (file.isFile()) {
                return true;
            } else if (force) { // 强制
                return FileUtils.deleteDirectory(file) ? FileUtils.createFile0(file) : false;
            } else {
                return false;
            }
        }

        // 文件不存在
        File parent = file.getParentFile();
        if (parent == null) { // 如果父目录不存在
            return FileUtils.createFile0(file);
        } else if (parent.exists()) { // 父目录存在
            if (parent.isDirectory()) {
                return FileUtils.createFile0(file);
            } else if (force) { // 强制删除并创建文件
                return parent.delete() && parent.mkdirs() ? FileUtils.createFile0(file) : false;
            } else {
                return false;
            }
        } else { // 父目录不存在
            return FileUtils.createDirectory(parent, force) && FileUtils.createFile0(file);
        }
    }

    /**
     * 按文件的绝对路径参数 filepath 创建文件，并检查文件是否具有权限值 mode。 <br>
     * 如果权限值不够则自动在当前用户根目录下按目录结构参数 dirNames 创建目录与文件。
     *
     * @param filepath 文件绝对路径
     * @param mode     1-只读 2-只写 3-读写
     * @param dirNames 目录结构数组（权限不够时，默认在用户根目录下按目录结构数组创建目录和文件）
     * @return 返回成功创建的文件，如果返回 null 表示创建文件失败
     * @throws IOException 创建文件发生错误
     */
    public static File createFile(String filepath, int mode, String... dirNames) throws IOException {
        if (StringUtils.isBlank(filepath)) {
            return null;
        }

        File file = new File(filepath);
        boolean exists = file.exists();
        try {
            FileUtils.createFile(file, false); // 创建文件

            // 检查文件权限
            switch (mode) {
                case 1: // 需要有读权限
                    FileUtils.checkPermission(file, true, false);
                    break;

                case 2: // 需要有写权限
                    FileUtils.checkPermission(file, false, true);
                    break;

                case 3: // 需要有读写权限
                    FileUtils.checkPermission(file, true, true);
                    break;

                default:
                    throw new IllegalArgumentException(String.valueOf(mode));
            }
        } catch (Throwable e) { // 创建文件权限不够时，默认在用户根目录下创建目录结构与文件
            if (!exists && file.exists()) {
                file.delete(); // 如果文件之前不存在，则需要尝试删除
            }

            // 按目录结构在用户根目录下创建目录
            String dir = Settings.getUserHome().getAbsolutePath();
            for (String str : dirNames) {
                dir = FileUtils.joinFilepath(dir, str);
            }
            file = new File(dir, file.getName());
        }

        // 创建文件
        return FileUtils.createFile(file, false) ? file : null;
    }

    /**
     * 尝试创建文件
     *
     * @param file 文件
     * @return true表示创建文件成功 false表示创建文件发生错误
     */
    private static boolean createFile0(File file) {
        try {
            return file.createNewFile();
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * 创建目录及父目录 <br>
     * 如果目录已经存在返回 true
     *
     * @param file 文件绝对路径
     * @return false表示创建目录失败
     */
    public static boolean createDirectory(File file) {
        return FileUtils.createDirectory(file, false);
    }

    /**
     * 创建目录及父目录 <br>
     * 如果目录已经存在返回 true
     *
     * @param file  文件
     * @param force true表示强制删除文件建立目录
     * @return true表示目录已存在或创建目录成功 false表示创建目录失败或已有同名文件存在导致创建目录失败
     */
    public static boolean createDirectory(File file, boolean force) {
        if (file == null) {
            return false;
        } else if (file.exists()) { // 如果文件存在
            return file.isFile() ? (force ? (file.delete() && file.mkdir()) : false) : (file.isDirectory() ? true : FileUtils.deleteDirectory(file) && file.mkdir());
        } else { // 如果文件不存在
            File parent = file.getParentFile();
            if (parent == null) { // 父目录不存在
                return file.mkdir();
            } else if (parent.exists()) {
                return parent.isDirectory() ? file.mkdir() : (force ? parent.delete() && file.mkdirs() : false);
            } else {
                return FileUtils.createDirectory(parent, force) && file.mkdirs();
            }
        }
    }

    /**
     * 删除文件的内容，对目录不做处理
     *
     * @param file 文件
     * @return 返回 true 表示成功清空文件内容
     * @throws IOException 删除文件内容发生错误
     */
    public static boolean clearFile(File file) throws IOException {
        return file != null && file.exists() && file.isFile() && FileUtils.write(file, StringUtils.CHARSET, false, "");
    }

    /**
     * 删除目录中的所有文件
     *
     * @param dir 目录文件
     * @return 返回 true 表示成功删除目录下所有内容
     */
    public static boolean clearDirectory(File dir) {
        if (dir == null || !dir.exists()) {
            return true;
        }
        if (dir.isFile()) {
            return false;
        }

        boolean value = true;
        File[] files = FileUtils.array(dir.listFiles());
        for (File file : files) {
            if (file == null) {
                continue;
            }

            if (file.isDirectory() && !FileUtils.clearDirectory(file)) {
                value = false;
            }
            if (!file.delete()) {
                value = false;
            }
        }
        return value;
    }

    /**
     * 将 windows DOS 格式的文本文件转为 unix 文本格式
     *
     * @param file        文本文件
     * @param charsetName 文本文件的字符集编码
     * @return 返回 true 表示转换成功
     * @throws IOException
     */
    public static boolean dos2unix(File file, String charsetName) throws IOException {
        if (file == null || !file.isFile()) {
            return false;
        }

        // 如果文件小于10M，则读取全部内容进行格式转换
        if (file.exists() && file.length() <= DOC2UNIX_FILESIZE) {
            String content = FileUtils.readline(file, charsetName, 0);
            String newcontent = FileUtils.replaceLineSeparator(content, FileUtils.lineSeparatorUnix);
            return FileUtils.write(file, charsetName, false, newcontent);
        }

        // 创建一个不重名的临时文件
        File tmpfile = FileUtils.getFileNoRepeat(file.getParentFile(), file.getName());
        tmpfile = FileUtils.createFile(tmpfile.getAbsolutePath(), 3);

        // 读取旧文件内容，并进行转换并保存到临时文件中
        OutputStreamWriter out = null;
        BufferedReader in = IO.getBufferedReader(file, charsetName);
        try {
            out = new OutputStreamWriter(new FileOutputStream(tmpfile, false), charsetName);
            String line;
            while ((line = in.readLine()) != null) {
                out.write(line);
                out.write(FileUtils.lineSeparatorUnix);
            }
            out.flush();
        } finally {
            IO.close(in, out);
        }

        // 删除原有文件，将临时文件改名
        if (log.isLoggable(Level.CONFIG)) {
            log.log(Level.CONFIG, ResourcesUtils.getFilesMessage(1, tmpfile, file));
        }
        return FileUtils.deleteFile(file) && tmpfile.renameTo(file);
    }

    /**
     * 将 windows DOS 格式的文本文件转为 unix 文本格式
     *
     * @param cs
     * @return
     */
    public static String dos2unix(CharSequence cs) {
        return FileUtils.replaceLineSeparator(cs, FileUtils.lineSeparatorUnix);
    }

    /**
     * 删除文件或目录
     *
     * @param file 文件
     * @return
     */
    public static boolean delete(File file) {
        if (file == null) {
            return false;
        } else if (file.exists()) {
            return file.isFile() ? file.delete() : FileUtils.deleteDirectory(file);
        } else {
            return true;
        }
    }

    /**
     * 删除文件 file <br>
     * 参数 file 只能是文件不能删除目录
     *
     * @param file 文件
     * @return false表示删除失败
     */
    public static boolean deleteFile(File file) {
        return (file != null && file.exists()) ? (file.isFile() && file.delete()) : true;
    }

    /**
     * 删除目录及其子文件和目录
     *
     * @param file 文件
     * @return
     */
    public static boolean deleteDirectory(File file) {
        if (file != null && file.exists()) {
            if (file.isDirectory()) {
                return FileUtils.clearDirectory(file) && file.delete();
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * 从文件路径中解析文件名(包含扩展名)<br>
     * "mypath/myfile.txt" == "myfile.txt".
     *
     * @param filepath 文件绝对路径
     * @return
     */
    public static String getFilename(String filepath) {
        if (filepath == null) {
            return null;
        }

        int lx = filepath.lastIndexOf('/');
        int lf = filepath.lastIndexOf('\\');
        int lp = lx > lf ? lx : lf;
        return (lp >= 0 ? filepath.substring(lp + 1) : filepath);
    }

    /**
     * 从文件路径中解析文件名(不含扩展名)<br>
     * "mypath/myfile.txt" == "myfile"
     *
     * @param filepath 文件绝对路径
     * @return
     */
    public static String getFilenameNoExt(String filepath) {
        if (filepath == null) {
            return null;
        }

        int lx = filepath.lastIndexOf('/');
        int lf = filepath.lastIndexOf('\\');
        int lp = lx > lf ? lx : lf;
        if (lp < 0) { // 不包含目录
            lp = -1;
        }
        if (lp + 1 == filepath.length()) {
            return "";
        }

        int end = filepath.lastIndexOf('.');
        return (end == -1 || end <= lp) ? filepath.substring(lp + 1) : filepath.substring(lp + 1, end);
    }

    /**
     * 从文件路径中解析文件名(不含后缀 .txt.gz)<br>
     * "mypath/myfile.txt.gz" == "myfile"
     *
     * @param filepath 文件绝对路径
     * @return 文件名
     */
    public static String getFilenameNoSuffix(String filepath) {
        if (filepath == null) {
            return null;
        }

        int lx = filepath.lastIndexOf('/');
        int lf = filepath.lastIndexOf('\\');
        int lp = lx > lf ? lx : lf;
        if (lp < 0) { // 不包含目录
            lp = -1;
        }
        if (lp + 1 == filepath.length()) { // 如果不存在文件名
            return "";
        }
        int end = filepath.indexOf('.', lp + 1);
        return (end == -1 || end <= lp) ? filepath.substring(lp + 1) : filepath.substring(lp + 1, end);
    }

    /**
     * 从文件路径中返回文件扩展名<br>
     * "mypath/myfile.bak.txt" == "txt"
     *
     * @param filepath 文件绝对路径
     * @return
     */
    public static String getFilenameExt(String filepath) {
        if (filepath == null) {
            return null;
        }

        int lx = filepath.lastIndexOf('/');
        int lf = filepath.lastIndexOf('\\');
        int lp = lx > lf ? lx : lf;
        if (lp < 0) { // 不包含目录
            lp = 0;
        }
        int end = filepath.lastIndexOf('.');
        return end == -1 || end < lp || ((end + 1) == filepath.length()) ? "" : filepath.substring(end + 1);
    }

    /**
     * 从文件路径中返回文件名后缀<br>
     * "mypath/myfile.txt.gz" == "txt.gz"
     *
     * @param filepath 文件绝对路径
     * @return 文件名后缀
     */
    public static String getFilenameSuffix(String filepath) {
        if (filepath == null) {
            return null;
        }

        int lx = filepath.lastIndexOf('/');
        int lf = filepath.lastIndexOf('\\');
        int lp = lx > lf ? lx : lf;
        if (lp < 0) { // 不包含目录
            lp = 0;
        }
        int end = filepath.indexOf('.', lp);
        return end == -1 || end < lp || ((end + 1) == filepath.length()) ? "" : filepath.substring(end + 1);
    }

    /**
     * 生成一个随机文件名
     *
     * @param prefix 文件名的前缀
     * @param endfix 文件名的后缀
     * @return
     */
    public static String getFilenameRandom(String prefix, String endfix) {
        Random random = new Random();
        StringBuilder buf = new StringBuilder();
        if (StringUtils.isNotBlank(prefix)) {
            buf.append(prefix);
        }

        buf.append(Dates.format17(new Date()));
        buf.append('I');
        buf.append(String.valueOf(Math.abs(random.nextLong())).replace('.', 'P'));
        buf.append(String.valueOf(Math.abs(random.nextFloat())).replace('.', 'P'));
        buf.append(String.valueOf(Math.abs(random.nextInt())).replace('.', 'P'));
        if (StringUtils.isNotBlank(endfix)) {
            buf.append(endfix);
        }
        return buf.toString();
    }

    /**
     * 在目录下生成一个不重复的文件 filename <br>
     * 如果文件已经存在，自动在文件名与文件扩展名之间增加时间戳作字符串用以区分
     *
     * @param parent   目录
     * @param filename 文件名
     * @return
     */
    public static File getFileNoRepeat(File parent, String filename) {
        if (parent == null) {
            throw new NullPointerException();
        }
        if (StringUtils.isBlank(filename)) {
            throw new IllegalArgumentException(filename);
        }
        if (parent.exists() && !parent.isDirectory()) { // 目录存在，但不是一个目录
            throw new IllegalArgumentException(parent.getAbsolutePath());
        }
        if (!parent.exists() && !FileUtils.createDirectory(parent)) { // 目录不存在，且创建目录失败
            throw new IllegalArgumentException(parent.getAbsolutePath());
        }

        File file = new File(parent, filename);
        while (file.exists()) {
            String name = FileUtils.getFilenameNoSuffix(filename);
            String suffix = FileUtils.getFilenameSuffix(filename);

            if (StringUtils.isBlank(suffix)) {
                file = new File(parent, StringUtils.rtrim(name, '_') + "_" + Dates.format17(new Date()));
            } else {
                file = new File(parent, StringUtils.rtrim(name, '_') + "_" + Dates.format17(new Date()) + "." + suffix);
            }
        }
        return file;
    }

    /**
     * 返回文件路径的父目录
     *
     * @param filepath 文件路径
     * @param level    第几层目录，从1开始 <br>
     *                 等于 0 时表示返回参数 filepath 本身 <br>
     *                 等于 1 时表示上一级目录 <br>
     *                 等于 2 时表示上一级目录的父目录
     * @return
     */
    public static String getParent(String filepath, int level) {
        if (filepath == null) {
            throw new NullPointerException();
        }
        if (level < 0) {
            throw new IllegalArgumentException(String.valueOf(level));
        }

        String dir = filepath;
        for (int i = 0; i < level; i++) {
            dir = FileUtils.getParent(dir);
        }
        return dir;
    }

    /**
     * 返回文件路径的父目录
     *
     * @param filepath 文件路径
     * @return
     */
    public static String getParent(String filepath) {
        if (StringUtils.isBlank(filepath)) {
            return null;
        }

        String inputpath = filepath;
        filepath = FileUtils.rtrimFolderSeparator(filepath);
        if (filepath.length() == 0) {
            return null;
        }

        String dirName = FileUtils.getFilename(filepath);
        String parent = FileUtils.rtrimFolderSeparator(filepath.substring(0, filepath.length() - dirName.length()));
        if (parent.length() == 0) {
            return null;
        } else {
            return inputpath.length() == parent.length() ? null : parent;
        }
    }

    /**
     * 返回JAVA虚拟机的临时目录
     *
     * @return
     */
    public static File getTempDir() {
        String filepath = System.getProperty("java.io.tmpdir");
        File dir = new File(filepath);
        if (dir.exists() && dir.isDirectory()) {
            return dir;
        } else {
            throw new IllegalArgumentException(filepath);
        }
    }

    /**
     * 在 JVM 临时文件目录 java.io.tmpdir 下按照类信息所在包名结构建立临时目录
     *
     * @param names 临时目录结构（按字符串数组中从左到右顺序建立子目录）
     * @return
     */
    public static File getTempDir(String[] names) {
        File parent = getTempDir();
        if (names.length == 0) {
            return parent;
        } else {
            String part = FileUtils.joinFilepath(names);
            String filepath = FileUtils.joinFilepath(parent.getAbsolutePath(), part);
            File dir = new File(filepath);
            return FileUtils.createDirectory(dir) ? dir : parent;
        }
    }

    /**
     * 在 JVM 临时文件目录 java.io.tmpdir 下按照类信息所在包名结构建立临时目录
     *
     * @param cls 类信息（按JAVA类的包名层级建立目录）
     * @return
     */
    public static File getTempDir(Class<?> cls) {
        if (cls == null) {
            throw new NullPointerException();
        } else {
            String[] names = StringUtils.split(cls.getPackage().getName(), '.');
            return FileUtils.getTempDir(StringUtils.removeBlank(names));
        }
    }

    /**
     * 创建一个临时文件
     *
     * @param cls   在 JVM 临时文件目录 java.io.tmpdir 下按照类信息所在包名结构建立临时目录
     * @param array 文件名
     * @return 文件
     * @throws IOException 创建文件失败
     */
    public static File createTempfile(Class<?> cls, String ext, String... array) throws IOException {
        if (array.length > 1) {
            throw new IllegalArgumentException(Arrays.asList(array).toString());
        }

        File dir = FileUtils.getTempDir(cls);
        String filename = array.length == 0 || StringUtils.isBlank(array[0]) ? StringUtils.toRandomUUID() : array[0];
        File tempfile = new File(dir, filename + "." + ext);
        if (FileUtils.delete(tempfile)) {
            tempfile.createNewFile();
            return tempfile;
        } else {
            throw new IOException(tempfile.getAbsolutePath());
        }
    }

    /**
     * 返回操作系统的回收站
     *
     * @return 如果操作系统文件系统未设置回收站时返回临时目录
     */
    public static File getRecyDir() {
        if (OSUtils.isWindows()) {
            File file = new File("\\$RECYCLE.BIN");
            if (FileUtils.isDirectory(file)) {
                return file;
            }
        }

        if (OSUtils.isLinux()) {
            File recycleDir = new File(StringUtils.replaceEnvironment("${HOME}/.Trash"));
            File file = recycleDir.exists() && recycleDir.isDirectory() ? recycleDir : null;
            if (FileUtils.isDirectory(file)) {
                return file;
            }
        }

        if (OSUtils.isAix()) {
            File recycleDir = new File("/.dt/Trash");
            File file = recycleDir.exists() && recycleDir.isDirectory() ? recycleDir : null;
            if (FileUtils.isDirectory(file)) {
                return file;
            }
        }

        if (OSUtils.isMacOs() || OSUtils.isMacOsX()) {
            File recycleDir = new File(StringUtils.replaceEnvironment("${HOME}/.Trash"));
            File file = recycleDir.exists() && recycleDir.isDirectory() ? recycleDir : null;
            if (FileUtils.isDirectory(file)) {
                return file;
            }
        }

        String[] array = FileUtils.class.getPackage().getName().split("\\."); // 解析包名的前缀,如: org.apache
        return FileUtils.getTempDir(ArrayUtils.as(array[0], array[1], "recycle"));
    }

    /**
     * 在路径后面拼接一个文件或目录
     *
     * @param array 文件绝对路径数组
     * @return
     */
    public static String joinFilepath(String... array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return "";
        } else {
            String filepath = array[0];
            for (int i = 1; i < array.length; i++) {
                filepath = FileUtils.joinFilepath(filepath, array[i]);
            }
            return FileUtils.replaceFolderSeparator(filepath);
        }
    }

    /**
     * 在路径参数 filepath 后面拼接一个字符串参数 fileOrDir （文件名或目录文件）
     *
     * @param filepath  文件绝对路径
     * @param fileOrDir 文件名 或 目录名
     * @return
     */
    private static String joinFilepath(String filepath, String fileOrDir) {
        if (filepath == null || fileOrDir == null) {
            return filepath;
        }
        while (filepath.endsWith("\\") || filepath.endsWith("/")) { // 去掉最后的分隔符
            filepath = filepath.substring(0, filepath.length() - 1);
        }
        while (fileOrDir.startsWith("\\") || fileOrDir.startsWith("/")) { // 去掉前面的分隔符
            fileOrDir = fileOrDir.substring(1);
        }
        return filepath + File.separator + fileOrDir;
    }

    /**
     * 把参数文件 file 移动到文件参数 dir 下
     *
     * @param file 文件或目录
     * @param dir  目录
     * @return false表示移动失败
     * @throws IOException
     */
    public static boolean moveFile(File file, File dir) throws IOException {
        if (file == null || dir == null || (dir.exists() && dir.isFile()) || file.equals(dir) || !FileUtils.createDirectory(dir)) {
            return false;
        }
        if (file.getParentFile().equals(dir)) { // 已在目录下不需要移动
            return true;
        }

        File newfile = new File(dir, file.getName()); // 移动后的文件
        if (file.isFile()) {
            return file.renameTo(newfile) ? true : FileUtils.copy(file, newfile) && FileUtils.delete(file);
        } else {
            if (FileUtils.createDirectory(newfile)) {
                boolean value = true;
                File[] files = FileUtils.array(file.listFiles());
                for (File cfile : files) {
                    if (!FileUtils.moveFile(cfile, newfile)) {
                        value = false;
                    }
                }
                return value && FileUtils.delete(file);
            } else {
                return false;
            }
        }
    }

    /**
     * 把文件移动到操作系统的回收站中
     *
     * @param file 文件或目录
     * @return true表示移动成功
     * @throws IOException
     */
    public static boolean moveFileToRecycle(File file) throws IOException {
        if (file == null || !file.exists()) {
            return false;
        } else {
            String newName = FileUtils.getFilenameNoExt(file.getName()) + "_" + Dates.format17(new Date()) + Numbers.getRandom() + "." + FileUtils.getFilenameExt(file.getName());
            File newFile = new File(file.getParentFile(), newName);
            File recycle = FileUtils.getRecyDir();
            if (file.renameTo(newFile) && FileUtils.moveFile(newFile, recycle)) {
                if (log.isLoggable(Level.CONFIG)) {
                    log.log(Level.CONFIG, ResourcesUtils.getFilesMessage(2, file, recycle));
                }
                return true;
            } else {
                if (log.isLoggable(Level.WARNING)) {
                    log.log(Level.WARNING, ResourcesUtils.getFilesMessage(2, file, recycle));
                }
                return false;
            }
        }
    }

    /**
     * 去掉文件扩展名<br>
     * "mypath/myfile.txt" == "mypath/myfile"
     *
     * @param filepath 文件路径
     * @return
     */
    public static String removeFilenameExt(String filepath) {
        if (filepath == null) {
            return null;
        }

        int lx = filepath.lastIndexOf('/');
        int lf = filepath.lastIndexOf('\\');
        int lp = lx > lf ? lx : lf;
        if (lp < 0) { // 不包含目录
            lp = -1;
        }
        int end = filepath.lastIndexOf('.');
        return end == -1 || end < lp ? filepath : filepath.substring(0, end);
    }

    /**
     * 重命名文件或文件夹
     *
     * @param file     文件或目录
     * @param filename 文件新名（为null或空字符串默认不修改文件名）
     * @param newExt   文件新后缀（为null或空字符串默认不修改文件后缀）
     * @return 0移动成功 1待移动文件不存在 2移动后文件已经存在 3重命名文件失败
     */
    public static int rename(File file, String filename, String newExt) {
        if (file == null) {
            throw new NullPointerException();
        }
        if (StringUtils.isBlank(filename)) {
            throw new IllegalArgumentException(filename);
        }
        if (StringUtils.isBlank(newExt)) {
            throw new IllegalArgumentException(filename);
        }
        if (!file.exists()) {
            return 1;
        }

        String oldFilename = FileUtils.getFilenameNoExt(file.getName());
        String oldFileExt = FileUtils.getFilenameExt(file.getName());
        String newname = (StringUtils.isNotBlank(filename) ? filename : oldFilename) + (StringUtils.isNotBlank(newExt) ? newExt : oldFileExt);
        File newfile = new File(file.getParentFile(), newname);
        return newfile.exists() ? 2 : (file.renameTo(newfile) ? 0 : 3);
    }

    /**
     * 将文件参数 file 重命名为文件参数 newFile <br>
     *
     * @param file    文件
     * @param newfile 重命名后文件
     */
    public static boolean rename(File file, File newfile) {
        if (file.renameTo(newfile)) {
            return true;
        } else {
            throw new UnsupportedOperationException("rename(" + file + ", " + newfile + ")");
        }
    }

    /**
     * 删除文件路径参数 path 最后一位的目录分隔符 <br>
     * 如果文件路径 path 最后一个字符不是目录分隔符则不作处理
     *
     * @param filepath 文件路径
     * @return
     */
    public static String rtrimFolderSeparator(String filepath) {
        return filepath == null ? null : StringUtils.rtrim(StringUtils.rtrimBlank(filepath), '/', '\\');
    }

    /**
     * 使用操作系统默认的行间分隔符替换字符序列参数 str 中的行间分隔符
     *
     * @param str 字符序列
     * @return
     */
    public static String replaceLineSeparator(CharSequence str) {
        return FileUtils.replaceLineSeparator(str, FileUtils.lineSeparator);
    }

    /**
     * 使用行间分隔符参数替换字符序列参数 str 中的行分隔符
     *
     * @param str           字符序列
     * @param lineSeparator 行间分隔符
     * @return
     */
    public static String replaceLineSeparator(CharSequence str, String lineSeparator) {
        if (str == null) {
            return null;
        }

        StringBuilder buf = new StringBuilder(str.length() + 20);
        for (int i = 0, size = str.length(); i < size; i++) {
            char c = str.charAt(i);

            if (c == '\n') {
                buf.append(lineSeparator);
            } else if (c == '\r') {
                buf.append(lineSeparator);
                int next = i + 1;
                if (next < size && str.charAt(next) == '\n') {
                    i = next;
                }
            } else {
                buf.append(c);
            }
        }
        return buf.toString();
    }

    /**
     * 把文件路径参数 filepath 中的 '/' 和 '\' 字符替换成当前操作系统的路径分隔符
     *
     * @param filepath 文件路径
     * @return
     */
    public static String replaceFolderSeparator(String filepath) {
        return FileUtils.replaceFolderSeparator(filepath, File.separatorChar);
    }

    /**
     * 把文件路径参数 filepath 中的 '/' 和 '\' 字符替换成字符参数 delimiter
     *
     * @param filepath  文件路径
     * @param delimiter 替换后的分隔符
     * @return
     */
    public static String replaceFolderSeparator(String filepath, char delimiter) {
        return filepath == null ? null : filepath.replace('/', delimiter).replace('\\', delimiter);
    }

    /**
     * 替换路径中的文件夹分隔符
     *
     * @param filepath 文件路径
     * @param local    true表示替换成当前操作系统的路径分隔符 false表示替换成 '/'
     * @return
     */
    public static String replaceFolderSeparator(String filepath, boolean local) {
        if (local) {
            return FileUtils.replaceFolderSeparator(filepath);
        } else {
            return FileUtils.replaceFolderSeparator(filepath, '/');
        }
    }

    /**
     * 返回文件的第n行内容
     *
     * @param file        文件
     * @param charsetName 文件字符集, 为空时取操作系统默认值
     * @param number      文件行号（从1开始 -1表示读取最后一行 0表示读取文件所有内容）
     * @return 返回null表示文件中不存在第n行
     * @throws IOException
     */
    public static String readline(File file, String charsetName, long number) throws IOException {
        if (file == null) {
            throw new NullPointerException();
        }
        if (number < -1) {
            throw new IllegalArgumentException(String.valueOf(number));
        }

        // 读取最后一行
        if (number == -1) {
            BufferedReader in = IO.getBufferedReader(file, StringUtils.defaultString(charsetName, StringUtils.CHARSET));
            try {
                String last = null;
                String line = null;
                while ((line = in.readLine()) != null) {
                    last = line;
                }
                return last;
            } finally {
                IO.close(in);
            }
        }

        // 读取所有行
        if (number == 0) {
            FileInputStream in = null;
            try {
                in = new FileInputStream(file);
                byte[] buf = new byte[(int) file.length()];
                int read = in.read(buf);
                Ensure.equals(buf.length, read);
                return new String(buf, charsetName);
            } finally {
                IO.close(in);
            }
        }

        // 读取指定行
        else {
            BufferedReader in = IO.getBufferedReader(file, StringUtils.defaultString(charsetName, StringUtils.CHARSET));
            try {
                String line = null;
                int lineno = 0;
                while ((line = in.readLine()) != null) {
                    if (++lineno == number) {
                        return line;
                    }
                }
                return null;
            } finally {
                IO.close(in);
            }
        }
    }

    /**
     * 返回文件的换行符
     *
     * @param file 文件
     * @return 返回 null 表示文件不存在换行符
     * @throws IOException
     */
    public static String readLineSeparator(File file) throws IOException {
        if (file == null) {
            throw new NullPointerException();
        }

        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            int i = -1;
            byte[] buf = new byte[128];
            while ((i = in.read(buf)) != -1) {
                for (int j = 0; j < i; j++) {
                    byte b = buf[j];
                    if (b == '\r') {
                        int next = j + 1;
                        if (next < i) {
                            if (buf[next] == '\n') {
                                return "\r\n";
                            } else {
                                return "\r";
                            }
                        } else {
                            return "\r";
                        }
                    } else if (b == '\n') {
                        return "\n";
                    }
                }
            }

            return null;
        } finally {
            IO.close(in);
        }
    }

    /**
     * 读取字符数组中出现的第一个回车符，换行符或回车换行符
     *
     * @param chars
     * @return
     */
    public static String readLineSeparator(CharSequence chars) {
        for (int i = 0; i < chars.length(); i++) {
            char c = chars.charAt(i);
            if (c == '\r') {
                int next = i + 1;
                if (next < chars.length()) {
                    if (chars.charAt(next) == '\n') {
                        return "\r\n";
                    } else {
                        return "\r";
                    }
                } else {
                    return "\r";
                }
            } else if (c == '\n') {
                return "\n";
            }
        }
        return null;
    }

    /**
     * 修改文件路径中的扩展名<br>
     * 如：changeFilenameExt("/home/test/file.txt", "enc") 返回值: /home/test/file.enc <br>
     * 如：changeFilenameExt("file.txt", "enc") 返回值: file.enc <br>
     *
     * @param filepath   文件绝对路径
     * @param lastPrefix 文件扩展名 enc txt
     * @return
     */
    public static String changeFilenameExt(String filepath, String lastPrefix) {
        if (filepath == null) {
            throw new NullPointerException();
        }
        if (lastPrefix == null) {
            throw new NullPointerException();
        }

        int lastfix = filepath.lastIndexOf('.'); // 文件后缀
        int delimiter = filepath.lastIndexOf('/'); // 斜杠
        int lx = filepath.lastIndexOf('\\'); // 反斜杠
        int lf = delimiter > lx ? delimiter : lx;
        return lastfix < 0 || lastfix < lf ? (filepath + "." + lastPrefix) : (filepath.substring(0, lastfix) + "." + lastPrefix);
    }

    /**
     * 复制文件参数file 到文件参数newFile
     *
     * @param file 文件或目录
     * @param dest 复制后的文件
     * @return 返回true表示复制成功 false表示复制文件失败
     * @throws IOException
     */
    public static boolean copy(File file, File dest) throws IOException {
        if (file == null || !file.exists() || file.equals(dest)) {
            throw new IllegalArgumentException(StringUtils.toString(file));
        }
        if (dest == null) {
            throw new NullPointerException();
        }

        // 复制文件
        if (file.isFile()) {
            FileInputStream in = new FileInputStream(file);
            FileOutputStream out = new FileOutputStream(dest, false);
            IO.write(in, out);
            return dest.exists() && (dest.length() == file.length());
        }

        // 复制目录
        if (file.isDirectory()) {
            if (dest.isFile()) {
                FileUtils.deleteFile(dest);
            }

            if (!dest.exists()) {
                dest.mkdirs();
            }

            // 复制子文件
            boolean value = true;
            File[] files = FileUtils.array(file.listFiles());
            for (File cfile : files) {
                File newfile = new File(dest, cfile.getName());
                if (newfile.isFile()) {
                    FileUtils.delete(newfile);
                }
                if (!FileUtils.copy(cfile, newfile)) {
                    value = false;
                }
            }
            return value;
        }

        // 不支持的文件类型
        else {
            if (log.isLoggable(Level.SEVERE)) {
                log.log(Level.SEVERE, ResourcesUtils.getFilesMessage(3, file));
            }
            return false;
        }
    }

    /**
     * 将 Properties 类中的参数名和参数值写入到 file 文件中
     *
     * @param p    资源类
     * @param file 文件
     * @throws IOException
     */
    public static File storeProperties(Properties p, File file) throws IOException {
        if (p == null) {
            throw new NullPointerException();
        }
        if (file == null) {
            throw new NullPointerException();
        }
        if (file.exists()) {
            if (!file.isFile() || !file.canWrite()) {
                throw new IllegalArgumentException(file.getAbsolutePath());
            }
        } else {
            FileUtils.createFile(file);
        }

        StringBuilder buf = new StringBuilder((int) file.length());

        // 读取p对象中的参数值并替换到资源文件file中同名参数值
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.ISO_8859_1.name()));
        try {
            String line;
            while ((line = in.readLine()) != null) {
                if (StringUtils.isNotBlank(line) && !StringUtils.ltrimBlank(line).startsWith("#")) {
                    int split = line.indexOf("=");
                    if (split != -1) {
                        String key = FileUtils.toPropertiesStr(line.substring(0, split), true);
                        String oldValue = StringUtils.rtrim(line.substring(split + 1));
                        String newValue = FileUtils.toPropertiesStr(p.getProperty(key), false);
                        p.remove(key);
                        if (newValue == null) {
                            buf.append(FileUtils.lineSeparatorUnix);
                            continue;
                        } else {
                            if (oldValue.equals(newValue)) {
                                buf.append(line);
                                buf.append(FileUtils.lineSeparatorUnix);
                                continue;
                            } else {
                                buf.append(key).append("=").append(newValue);
                                buf.append(FileUtils.lineSeparatorUnix);
                                continue;
                            }
                        }
                    }
                } else {
                    buf.append(line);
                    buf.append(FileUtils.lineSeparatorUnix);
                }
            }
        } finally {
            in.close();
        }

        // 将剩余参数写入到资源文件中
        Enumeration<?> names = p.propertyNames();
        while (names.hasMoreElements()) {
            String key = FileUtils.toPropertiesStr(StringUtils.objToStr(names.nextElement()), true);
            String value = FileUtils.toPropertiesStr(p.getProperty(key), false);
            buf.append(FileUtils.lineSeparatorUnix).append(key).append("=").append(value).append(FileUtils.lineSeparatorUnix);
        }

        FileUtils.write(file, StandardCharsets.UTF_8.name(), false, buf.toString());
        return file;
    }

    /**
     * 将字符串转为存储到 properties 文件中格式
     *
     * @param str         字符串
     * @param escapeSpace true表示对字符串中的字符进行转义
     * @return 格式化之后的字符串
     */
    private static String toPropertiesStr(String str, boolean escapeSpace) {
        int len = str.length();
        int bufLen = len * 2;
        if (bufLen < 0) {
            bufLen = Integer.MAX_VALUE;
        }

        StringBuilder buf = new StringBuilder(bufLen);
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if ((c > 61) && (c < 127)) {
                if (c == '\\') {
                    buf.append('\\');
                    buf.append('\\');
                    continue;
                }
                buf.append(c);
                continue;
            }

            switch (c) {
                case ' ':
                    if (i == 0 || escapeSpace) buf.append('\\');
                    buf.append(' ');
                    break;
                case '\t':
                    buf.append('\\');
                    buf.append('t');
                    break;
                case '\n':
                    buf.append('\\');
                    buf.append('n');
                    break;
                case '\r':
                    buf.append('\\');
                    buf.append('r');
                    break;
                case '\f':
                    buf.append('\\');
                    buf.append('f');
                    break;
                case '=': // Fall through
                case ':': // Fall through
                case '#': // Fall through
                case '!':
                    buf.append('\\');
                    buf.append(c);
                    break;
                default:
                    if ((c < 0x0020) || (c > 0x007e)) { // c < 32 || c > 127
                        buf.append('\\');
                        buf.append('u');
                        buf.append(toHex((c >> 12) & 0xF));
                        buf.append(toHex((c >> 8) & 0xF));
                        buf.append(toHex((c >> 4) & 0xF));
                        buf.append(toHex(c & 0xF));
                    } else {
                        buf.append(c);
                    }
            }
        }
        return buf.toString();
    }

    private static char toHex(int i) {
        return "0123456789abcdef".charAt(i & 0xF);
    }

    /**
     * 加载资源文件
     *
     * @param filepath 文件绝对路径
     * @return
     * @throws IOException
     */
    public static Properties loadProperties(String filepath) throws IOException {
        if (StringUtils.isBlank(filepath)) {
            return null;
        }

        if (FileUtils.isFile(filepath)) {
            Properties p = new Properties();
            p.load(new FileInputStream(filepath));
            return p;
        }

        if (filepath.startsWith("/")) {
            URL url = StringUtils.class.getClassLoader().getResource(filepath);
            if (url != null) {
                Properties p = new Properties();
                p.load(new FileInputStream(filepath));
                return p;
            } else {
                InputStream in = ClassUtils.getResourceAsStream(filepath);
                if (in != null) {
                    Properties p = new Properties();
                    p.load(in);
                    return p;
                }
            }
        }

        throw new UnsupportedOperationException("loadProperties(" + filepath + ")");
    }

    /**
     * 写入字符串到文件，适用于小文件
     *
     * @param file        文件
     * @param charsetName 文件的字符集编码
     * @param append      true追加写入
     * @param content     写入内容
     * @throws IOException
     */
    public static boolean write(File file, String charsetName, boolean append, CharSequence content) throws IOException {
        if (!append && content == null) {
            return FileUtils.deleteFile(file) && FileUtils.createFile(file);
        }
        if (!FileUtils.createFile(file)) {
            return false;
        }

        Writer out = IO.getFileWriter(file, charsetName, append);
        try {
            out.write(content.toString());
            out.flush();
            return true;
        } finally {
            out.close();
        }
    }

    /**
     * 从输入流参数 in 中读取字节并写入到文件参数 file 中
     *
     * @param file        文件
     * @param charsetName 文件的字符集编码
     * @param append      true表示追加写入 false表示覆盖原文件内容
     * @param in          输入流
     * @return
     * @throws IOException
     */
    public static boolean write(File file, String charsetName, boolean append, InputStream in) throws IOException {
        if (in == null) {
            throw new IllegalArgumentException();
        }
        if (!FileUtils.createFile(file)) {
            return false;
        }

        Writer out = IO.getFileWriter(file, charsetName, append);
        try {
            byte[] bytes = new byte[10240];
            int length = -1;
            while ((length = in.read(bytes, 0, bytes.length)) != -1) {
                out.write(new String(bytes, 0, length, charsetName));
            }
            out.flush();
            return true;
        } finally {
            out.close();
        }
    }

    /**
     * 判断路径是否存在
     *
     * @param filepath 文件路径
     * @return
     */
    public static boolean exists(String filepath) {
        return StringUtils.isNotBlank(filepath) && new File(filepath).exists();
    }

    /**
     * 返回true 表示二个文件按字节比对相等
     *
     * @param file1      文件
     * @param file2      文件
     * @param bufferSize 读取文件时的字节缓冲区大小; 小于等于零自动赋默认值 8192
     * @return
     * @throws IOException
     */
    public static boolean equals(File file1, File file2, int bufferSize) throws IOException {
        if (file1 == null && file2 == null) {
            return true;
        }
        if (file1 == null || file2 == null) {
            return false;
        }
        if (file1.equals(file2)) {
            return true;
        }
        if (file1.length() != file2.length()) {
            return false;
        }
        if (bufferSize <= 0) {
            bufferSize = 8192;
        }

        FileInputStream in1 = null, in2 = null;
        try {
            in1 = new FileInputStream(file1);
            in2 = new FileInputStream(file2);
            byte[] b1 = new byte[bufferSize];
            byte[] b2 = new byte[bufferSize];
            int s1, s2;
            while ((s1 = in1.read(b1)) == (s2 = in2.read(b2))) {
                if (s1 == -1) {
                    break;
                }

                while (s2 >= 1) {
                    s2--;
                    if (b1[s2] != b2[s2]) {
                        return false;
                    }
                }
            }
            return s1 == s2;
        } finally {
            IO.close(in1, in2);
        }
    }

    /**
     * 比较二个文本文件内容是否相同，自动忽略换行符的不同
     *
     * @param file1        文本文件
     * @param charsetName1 文本文件字符集; 空白表示使用默认值
     * @param file2        文本文件
     * @param charsetName2 文本文件字符集; 空白表示使用默认值
     * @param bufferSize   读文件时缓冲区大小; 小于等于零自动赋默认值 8192
     * @return
     * @throws IOException
     */
    public static long equalsIgnoreLineSeperator(File file1, String charsetName1, File file2, String charsetName2, int bufferSize) throws IOException {
        if (file1 == null && file2 == null) {
            return 0;
        }
        if (file1 == null || file2 == null) {
            return 0;
        }
        if (file1.equals(file2)) {
            return 0;
        }
        if (bufferSize <= 0) {
            bufferSize = 8192;
        }

        long lineNumber = 0;
        BufferedReader in1 = null, in2 = null;
        try {
            in1 = IO.getBufferedReader(file1, StringUtils.defaultString(charsetName1, StringUtils.CHARSET), bufferSize);
            in2 = IO.getBufferedReader(file2, StringUtils.defaultString(charsetName2, StringUtils.CHARSET), bufferSize);
            String str1 = null, str2 = null;
            while (true) {
                lineNumber++;
                str1 = in1.readLine();
                str2 = in2.readLine();
                if (str1 == null || str2 == null) {
                    break;
                }
                if (!str1.equals(str2)) {
                    return lineNumber;
                }
            }
            return str1 == null && str2 == null ? 0 : lineNumber;
        } finally {
            IO.close(in1, in2);
        }
    }

    /**
     * 查找文件
     *
     * @param fileOrDir 文件，判断文件名与 {@code name} 参数是否匹配
     *                  目录，在目录中查找与 {@code name} 参数匹配的文件
     * @param name      文件名（含扩展名）或正则表达式
     * @return 匹配查找条件的文件
     */
    public static List<File> findFile(File fileOrDir, String name) {
        if (fileOrDir == null) {
            return new ArrayList<File>(0);
        } else if (!fileOrDir.exists()) {
            return new ArrayList<File>(0);
        } else if (fileOrDir.isDirectory()) {
            List<File> list = new ArrayList<File>();
            findfile0(fileOrDir, name, list);
            return list;
        } else {
            return fileOrDir.getName().equals(name) || fileOrDir.getName().matches(name) ? ArrayUtils.asList(fileOrDir) : new ArrayList<File>(0);
        }
    }

    /**
     * 搜索文件
     *
     * @param dir 目录
     * @return
     */
    private static void findfile0(File dir, String name, List<File> list) {
        if (dir.getName().equals(name) || dir.getName().matches(name)) {
            list.add(dir);
        }

        File[] array = FileUtils.array(dir.listFiles());
        for (File file : array) {
            if (file.isFile()) {
                if (file.getName().equals(name) || file.getName().matches(name)) {
                    list.add(file);
                    continue;
                }
            }

            if (file.isDirectory()) {
                findfile0(file, name, list);
            }
        }
    }

    /**
     * 在指定时间范围内，检查目录中的文件是否发生了变化（文件被写入了相同内容也算变化）
     *
     * @param dir     目录
     * @param time    时间范围（单位毫秒），1000表示1秒
     * @param filters 最多只能设置一个文件过滤器，用来筛选目录中的文件
     * @return true表示没有发生变化 false表示发生了变化
     */
    public static List<File> isWriting(File dir, long time, FilenameFilter... filters) {
        FilenameFilter filter = Ensure.onlyone(filters);
        if (filter == null) {
            filter = new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return !name.startsWith(".");
                }
            };
        }

        Map<File, Long> files = new HashMap<File, Long>();
        listfiles(dir, filter, files); // 读取所有文件的最后修改时间

        long begin = System.currentTimeMillis();
        try {
            Thread.sleep(time); // 等待指定时间
        } catch (Throwable e) {
            if (log.isLoggable(Level.CONFIG)) {
                log.log(Level.CONFIG, ResourcesUtils.getFilesMessage(4, dir.getAbsolutePath(), time), e);
            }
        } finally {
            while ((System.currentTimeMillis() - begin) <= time) {
            }
        }

        List<File> list = new ArrayList<File>(files.size());
        comparefiles(dir, filter, files, list); // 比较文件的最后修改时间
        list.addAll(files.keySet()); // 将已删除的文件添加到集合中
        return list;
    }

    /**
     * 比较文件的最后修改时间
     *
     * @param dir    目录
     * @param filter 文件过滤器
     * @param map    文件的历史状态，文件与文件的最后修改时间的映射关系
     * @param list   将发生变化的文件保存到集合中
     */
    protected static void comparefiles(File dir, FilenameFilter filter, Map<File, Long> map, List<File> list) {
        File[] files = dir.listFiles(filter);
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isFile()) {
                Long time = map.remove(file);
                if (time != null && file.lastModified() == time.longValue()) {
                    continue;
                } else {
                    list.add(file); // 文件发生了变化
                }
            }

            if (file.isDirectory()) {
                comparefiles(file, filter, map, list);
            }
        }
    }

    /**
     * 遍历目录中的文件，并将文件的最后修改时间保存到集合map中
     *
     * @param dir    目录
     * @param filter 文件过滤器
     * @param map    文件与最后修改时间的映射关系
     */
    protected static void listfiles(File dir, FilenameFilter filter, Map<File, Long> map) {
        File[] files = dir.listFiles(filter);
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isFile()) {
                map.put(file, file.lastModified());
                continue;
            }

            if (file.isDirectory()) {
                listfiles(file, filter, map);
            }
        }
    }

}