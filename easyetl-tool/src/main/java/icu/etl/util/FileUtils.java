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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 文件帮助类
 *
 * @author jeremy8551@qq.com
 * @createtime 2009-12-19 18:00:54
 */
public final class FileUtils {

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

    /** 临时文件目录 */
    private static volatile File tempDir;

    public FileUtils() {
    }

    /**
     * 断言删除文件或目录
     *
     * @param file 删除文件或目录
     */
    public static void assertDelete(File file) {
        if (!Atomic.delete(file)) {
            throw new UnsupportedOperationException(ResourcesUtils.getIoxMessage(16, file));
        }
    }

    /**
     * 断言文件存在
     *
     * @param file 文件或目录
     */
    public static void assertExists(File file) {
        if (file == null || !file.exists()) {
            throw new UnsupportedOperationException(ResourcesUtils.getIoxMessage(37, file));
        }
    }

    /**
     * 断言是文件
     *
     * @param file 文件
     */
    public static void assertFile(File file) {
        FileUtils.assertExists(file);
        if (!file.isFile()) {
            throw new UnsupportedOperationException(ResourcesUtils.getIoxMessage(60, file));
        }
    }

    /**
     * 断言是目录
     *
     * @param file 文件
     */
    public static void assertDirectory(File file) {
        FileUtils.assertExists(file);
        if (!file.isDirectory()) {
            throw new UnsupportedOperationException(ResourcesUtils.getIoxMessage(61, file));
        }
    }

    /**
     * 断言创建目录
     *
     * @param filepath 文件路径
     * @return 目录
     */
    public static File assertCreateDirectory(String filepath) {
        if (filepath != null && Atomic.createDirectory(new File(filepath), false)) {
            return new File(filepath);
        } else {
            throw new UnsupportedOperationException(ResourcesUtils.getIoxMessage(59, filepath));
        }
    }

    /**
     * 断言创建目录
     *
     * @param file 目录
     * @return 目录
     */
    public static File assertCreateDirectory(File file) {
        return FileUtils.assertCreateDirectory(file, false);
    }

    /**
     * 断言创建目录
     *
     * @param file 目录
     * @return 目录
     */
    public static File assertCreateDirectory(File file, boolean force) {
        if (Atomic.createDirectory(file, force)) {
            return file;
        } else {
            throw new UnsupportedOperationException(ResourcesUtils.getIoxMessage(59, file));
        }
    }

    /**
     * 断言创建文件
     *
     * @param file 文件
     * @return 文件
     */
    public static File assertCreateFile(File file) {
        if (Atomic.createFile(file, false)) {
            return file;
        } else {
            throw new UnsupportedOperationException(ResourcesUtils.getIoxMessage(58, file));
        }
    }

    /**
     * 断言创建文件
     *
     * @param filepath 文件路径
     * @return 文件
     */
    public static File assertCreateFile(String filepath) {
        return FileUtils.assertCreateFile(filepath != null ? new File(filepath) : null);
    }

    /**
     * 断言清空目录下的所有文件
     *
     * @param file 目录
     */
    public static void assertClearDirectory(File file) {
        if (!Atomic.clearDir(file)) {
            throw new UnsupportedOperationException(ResourcesUtils.getIoxMessage(55, file));
        }
    }

    /**
     * 返回一个目录中（还未创建）不重名的文件 <br>
     * 如果文件已存在，则在文件名与文件扩展名之间增加序号用以区分
     *
     * @param parent   上级目录
     * @param filename 文件名, 如果为null或空白，则随机生成文件名
     * @return 文件
     */
    public static File allocate(File parent, String filename) {
        FileUtils.assertCreateDirectory(parent, false);
        String suffix = FileUtils.getFilenameSuffix(filename);
        boolean noext = StringUtils.isBlank(suffix);

        String name = FileUtils.getFilenameNoSuffix(filename);
        if (StringUtils.isBlank(name)) {
            name = "f" + Dates.format17() + new Random().nextInt(10);
            filename = name + (noext ? "" : "." + suffix);
        }

        File file = new File(parent, filename);
        for (int i = 1; file.exists(); i++) {
            if (noext) {
                file = new File(parent, name + i);
            } else {
                file = new File(parent, name + i + "." + suffix);
            }
        }
        return file;
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
     * 判断路径是否存在
     *
     * @param filepath 文件路径
     * @return 返回true表示存在 false表示不存在
     */
    public static boolean exists(String filepath) {
        return filepath != null && new File(filepath).exists();
    }

    /**
     * 判断文件是否存在
     *
     * @param file 文件
     * @return 返回true表示存在 false表示不存在
     */
    public static boolean exists(File file) {
        return file != null && file.exists();
    }

    /**
     * 只有在文件存在，且是一个文件时返回true
     *
     * @param filepath 文件绝对路径
     * @return 返回true表示是文件 false表示文件不是文件
     */
    public static boolean isFile(String filepath) {
        return filepath != null && FileUtils.isFile(new File(filepath));
    }

    /**
     * 文件参数file存在且是一个文件时返回true
     *
     * @param file 文件
     * @return 返回true表示文件存在 false表示不存在或不是合法文件
     */
    public static boolean isFile(File file) {
        return file != null && file.exists() && file.isFile();
    }

    /**
     * 只有在文件路径存在，且是一个目录时返回true
     *
     * @param filepath 文件绝对路径
     * @return 返回true表示路径是目录 false表示路径不是目录
     */
    public static boolean isDirectory(String filepath) {
        return filepath != null && FileUtils.isDirectory(new File(filepath));
    }

    /**
     * 只有在文件存在，且是一个目录时返回true
     *
     * @param file 文件
     * @return 返回true表示文件是目录 false表示文件不是目录
     */
    public static boolean isDirectory(File file) {
        return file != null && file.exists() && file.isDirectory();
    }

    /**
     * 创建文件（非目录）
     *
     * @param file 文件
     * @return 返回true表示操作成功 false表示操作失败
     */
    public static boolean createFile(File file) {
        return createFile(file, false);
    }

    /**
     * 创建文件（非目录）
     *
     * @param file  文件
     * @param force true表示强制创建文件（如果文件存在是目录，则删除目录再创建文件）<br>
     *              false表示如果文件存在，但是目录或其他类型的文件，则返回false
     * @return 返回true表示操作成功 false表示操作失败
     */
    public static boolean createFile(File file, boolean force) {
        boolean value = Atomic.createFile(file, force);
        return log1(file, value);
    }

    /**
     * （在临时目录下）创建目录
     *
     * @param filename 文件名，如果是null或空白，则使用随机名
     * @return 如果创建文件失败，就返回null，否则返回成功创建的目录
     */
    public static File createTempDirectory(String filename) {
        File parent = FileUtils.getTempDir("create", "directory"); // 返回临时文件目录
        return FileUtils.createNewDirectory(parent, filename);
    }

    /**
     * （在临时目录下）创建文件
     *
     * @param filename 文件名，如果是null或空白，则使用随机名
     * @return 如果创建文件失败，就返回null，否则返回成功创建的临时文件
     */
    public static File createTempFile(String filename) {
        File parent = FileUtils.getTempDir("create", "files"); // 返回临时文件目录
        return FileUtils.createNewFile(parent, filename);
    }

    /**
     * 在指定目录下创建一个新文件
     *
     * @param parent   上级目录
     * @param filename 文件名，如果是null或空白，则使用随机名
     * @return 如果创建文件失败，就返回null，否则返回成功创建的文件
     */
    public static File createNewFile(File parent, String filename) {
        File file = FileUtils.allocate(parent, filename);
        if (Atomic.createFile(file, false)) {
            return file;
        } else {
            throw new UnsupportedOperationException(ResourcesUtils.getIoxMessage(58, file.getAbsolutePath()));
        }
    }

    /**
     * 在指定目录下创建一个新目录
     *
     * @param parent   上级目录
     * @param filename 文件名，如果是null或空白，则使用随机名
     * @return 如果创建文件失败，就返回null，否则返回成功创建的目录
     */
    public static File createNewDirectory(File parent, String filename) {
        File file = FileUtils.allocate(parent, filename);
        if (Atomic.createDirectory(file, false)) {
            return file;
        } else {
            throw new UnsupportedOperationException(ResourcesUtils.getIoxMessage(59, file.getAbsolutePath()));
        }
    }

    /**
     * 创建目录
     *
     * @param parent 上级目录
     * @param names  子目录名数组
     * @return 返回 true 表示操作成功 false表示操作失败
     */
    public static File createDirectory(File parent, String... names) {
        String path = FileUtils.joinPath(names);
        String filepath = FileUtils.joinPath(parent.getAbsolutePath(), path);
        File file = new File(filepath);
        if (Atomic.createDirectory(file, false)) {
            return file;
        } else {
            throw new UnsupportedOperationException(ResourcesUtils.getIoxMessage(59, file.getAbsolutePath()));
        }
    }

    /**
     * 创建目录及上级目录
     *
     * @param filepath 文件绝对路径
     * @return 返回 true 表示操作成功 false表示操作失败
     */
    public static boolean createDirectory(String filepath) {
        return FileUtils.createDirectory(filepath != null ? new File(filepath) : null, false);
    }

    /**
     * 创建目录及上级目录
     *
     * @param file 文件
     * @return 返回 true 表示操作成功 false表示操作失败
     */
    public static boolean createDirectory(File file) {
        return FileUtils.createDirectory(file, false);
    }

    /**
     * 创建目录及上级目录
     *
     * @param file  文件
     * @param force true表示强制创建目录（如果文件存在但不是目录，则删除再创建文件）<br>
     *              false表示如果文件存在，但是其他类型的文件，则返回false
     * @return 返回 true 表示操作成功 false表示操作失败
     */
    public static boolean createDirectory(File file, boolean force) {
        boolean value = Atomic.createDirectory(file, force);
        return log2(file, value);
    }

    /**
     * 删除文件的内容，对目录不做处理
     *
     * @param file 文件
     * @return 返回true表示操作成功 false表示操作失败
     * @throws IOException 访问文件错误
     */
    public static boolean clearFile(File file) throws IOException {
        return file != null && file.exists() && file.isFile() && FileUtils.write(file, StringUtils.CHARSET, false, "");
    }

    /**
     * 保留目录，删除目录中的所有文件（文件与子目录）
     *
     * @param file 目录
     * @return 返回true表示操作成功 false表示操作失败
     */
    public static boolean clearDirectory(File file) {
        if (file == null) {
            throw new NullPointerException();
        }

        // 如果路径是目录
        boolean value = Atomic.clearDir(file);
        return log7(file, value);
    }

    /**
     * 删除文件或目录
     *
     * @param file     文件，不能为null
     * @param tryTimes 尝试删除文件的次数, 从0开始
     * @param millis   每次尝试删除文件之间的时间间隔（0表示没有间隔），单位毫秒
     * @return 返回true表示删除成功 false表示删除失败
     */
    public static boolean delete(File file, int tryTimes, long millis) {
        if (file == null) {
            throw new NullPointerException();
        }
        if (tryTimes < 0) {
            throw new IllegalArgumentException(String.valueOf(tryTimes));
        }
        if (millis < 0) {
            throw new IllegalArgumentException(String.valueOf(millis));
        }

        for (int i = 0, size = tryTimes + 1; i < size; i++) {
            if (Atomic.delete(file)) { // 尝试删除文件
                return true;
            } else if (millis > 0) {
                Dates.sleep(millis, TimeUnit.MILLISECONDS);
            }
        }

        if (file.isDirectory()) {
            return log2(file, false);
        } else {
            return log3(file, false);
        }
    }

    /**
     * 删除文件或目录
     *
     * @param file 文件或目录
     * @return 返回true表示删除成功 false表示删除失败
     */
    public static boolean delete(File file) {
        if (file == null) {
            throw new NullPointerException();
        }

        boolean value = Atomic.delete(file);
        if (file.isDirectory()) {
            return log2(file, value);
        } else {
            return log3(file, value);
        }
    }

    /**
     * 删除文件（不能删除目录）
     *
     * @param file 文件
     * @return 返回true表示操作成功 false表示操作失败
     */
    public static boolean deleteFile(File file) {
        if (file == null) {
            throw new NullPointerException();
        }

        // 删除文件
        boolean value = Atomic.deleteFile(file);
        return log3(file, value);
    }

    /**
     * 删除目录及其子文件、子目录
     *
     * @param file 目录
     * @return 返回true表示删除成功 false表示删除失败
     */
    public static boolean deleteDirectory(File file) {
        if (file == null) {
            throw new NullPointerException();
        }

        // 删除目录及其子文件
        boolean value = Atomic.deleteDirectory(file);
        return log4(file, value);
    }

    /**
     * 将 windows DOS 格式的文本文件转为 unix 文本格式
     *
     * @param file        文件
     * @param charsetName 文件的字符集
     * @param parent      临时文件存储目录, 如果是null，就使用系统的临时目录
     * @return 返回true表示操作成功 false表示操作失败
     * @throws IOException 访问文件错误
     */
    public static boolean dos2unix(File file, String charsetName, File parent) throws IOException {
        if (file == null || !file.exists() || !file.isFile()) {
            return false;
        }

        // 如果文件小于10M，则读取全部内容
        if (file.exists() && file.length() <= DOC2UNIX_FILESIZE) {
            String content = FileUtils.readline(file, charsetName, 0);
            String newcontent = FileUtils.replaceLineSeparator(content, FileUtils.lineSeparatorUnix);
            return FileUtils.write(file, charsetName, false, newcontent);
        }

        // 创建一个不重名的临时文件
        File newfile = parent != null ? FileUtils.createNewFile(parent, file.getName()) : FileUtils.createTempFile(file.getName());
        if (!Atomic.createFile(newfile, false)) {
            return false;
        }

        // 读取文件内容进行格式转换
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), charsetName));
        try {
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(newfile, false), charsetName);
            try {
                String line;
                for (int i = 0; (line = in.readLine()) != null; i++) {
                    out.write(line);
                    out.write(FileUtils.lineSeparatorUnix);
                    if (i % 100 == 0) {
                        out.flush();
                    }
                }
                out.flush();
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }

        return FileUtils.rename(newfile, file, null);
    }

    /**
     * 从文件路径中解析文件名(包含扩展名) <br>
     * "mypath/myfile.txt" == "myfile.txt".
     *
     * @param filepath 文件绝对路径
     * @return 文件名
     */
    public static String getFilename(String filepath) {
        if (filepath == null) {
            return null;
        }

        int lx = filepath.lastIndexOf('/');
        int lf = filepath.lastIndexOf('\\');
        int lp = Math.max(lx, lf);
        return (lp >= 0 ? filepath.substring(lp + 1) : filepath);
    }

    /**
     * 从文件路径中解析文件名(不含扩展名) <br>
     * "mypath/myfile.txt" == "myfile"
     *
     * @param filepath 文件绝对路径
     * @return 文件名
     */
    public static String getFilenameNoExt(String filepath) {
        if (filepath == null) {
            return null;
        }

        int lx = filepath.lastIndexOf('/');
        int lf = filepath.lastIndexOf('\\');
        int lp = Math.max(lx, lf);

        // 如果文件名为空字符串
        if (lp + 1 == filepath.length()) {
            return "";
        }

        int end = filepath.lastIndexOf('.');
        return (end == -1 || end <= lp) ? filepath.substring(lp + 1) : filepath.substring(lp + 1, end);
    }

    /**
     * 从文件路径中解析文件名(不含后缀 .txt.gz) <br>
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
        int lp = Math.max(lx, lf);

        // 如果文件名为空字符串
        if (lp + 1 == filepath.length()) { // 如果不存在文件名
            return "";
        }

        int end = filepath.indexOf('.', lp + 1);
        return (end == -1 || end <= lp) ? filepath.substring(lp + 1) : filepath.substring(lp + 1, end);
    }

    /**
     * 从文件路径中返回文件扩展名 <br>
     * "mypath/myfile.bak.txt" == "txt"
     *
     * @param filepath 文件绝对路径
     * @return 文件扩展名
     */
    public static String getFilenameExt(String filepath) {
        if (filepath == null) {
            return null;
        }

        int lx = filepath.lastIndexOf('/');
        int lf = filepath.lastIndexOf('\\');
        int lp = Math.max(lx, lf);
        if (lp < 0) { // 不包含目录
            lp = 0;
        }
        int end = filepath.lastIndexOf('.');
        return end == -1 || end < lp || ((end + 1) == filepath.length()) ? "" : filepath.substring(end + 1);
    }

    /**
     * 从文件路径中返回文件名后缀 <br>
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
        int lp = Math.max(lx, lf);
        if (lp < 0) { // 不包含目录
            lp = 0;
        }
        int end = filepath.indexOf('.', lp);
        return end == -1 || end < lp || ((end + 1) == filepath.length()) ? "" : filepath.substring(end + 1);
    }

    /**
     * 返回文件路径的上级目录
     *
     * @param filepath 文件路径
     * @param level    第几层目录，从1开始 <br>
     *                 等于 0 时表示返回参数 filepath 本身 <br>
     *                 等于 1 时表示上一级目录 <br>
     *                 等于 2 时表示上一级目录的父目录 <br>
     * @return 上级目录
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
     * @return 上级目录
     */
    public static String getParent(String filepath) {
        if (filepath == null) {
            return null;
        }

        String bakpath = filepath;
        filepath = FileUtils.rtrimFolderSeparator(filepath);
        if (filepath.length() == 0) {
            return null;
        }

        String filename = FileUtils.getFilename(filepath);
        String parent = FileUtils.rtrimFolderSeparator(filepath.substring(0, filepath.length() - filename.length()));
        if (parent.length() == 0) {
            return null;
        } else {
            return bakpath.length() == parent.length() ? null : parent;
        }
    }

    /**
     * 返回JAVA虚拟机的临时目录
     *
     * @param array 目录结构（按字符串数组中从左到右顺序建立子目录）
     * @return 临时目录
     */
    public static File getTempDir(String... array) {
        if (tempDir == null) {
            synchronized (FileUtils.class) {
                if (tempDir == null) {
                    // 在临时目录下建立应用名
                    List<String> list = new ArrayList<String>(5);
                    StringUtils.split(Settings.getGroupID(), '.', list);
                    list.add(Settings.getApplicationName());

                    // 拼接文件路径
                    String filepath = Settings.getTempDir();
                    for (String str : list) {
                        filepath = FileUtils.joinPath(filepath, str);
                    }
                    File dir = new File(filepath);

                    FileUtils.assertCreateDirectory(dir, true);
                    tempDir = dir;
                }
            }
        }

        if (array == null || array.length == 0) {
            return tempDir;
        } else {
            return FileUtils.createDirectory(tempDir, array);
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
            if (file.exists() && file.isDirectory()) {
                return file;
            }
        }

        if (OSUtils.isLinux()) {
            File file = new File(StringUtils.replaceEnvironment("${HOME}/.Trash"));
            if (file.exists() && file.isDirectory()) {
                return file;
            }
        }

        if (OSUtils.isAix()) {
            File file = new File("/.dt/Trash");
            if (file.exists() && file.isDirectory()) {
                return file;
            }
        }

        if (OSUtils.isMacOs() || OSUtils.isMacOsX()) {
            File file = new File(StringUtils.replaceEnvironment("${HOME}/.Trash"));
            if (file.exists() && file.isDirectory()) {
                return file;
            }
        }

        // 在临时目录下建立结构
        return FileUtils.getTempDir("recycle");
    }

    /**
     * 在路径后面拼接一个文件或目录
     *
     * @param array 文件绝对路径数组
     * @return 文件路径
     */
    public static String joinPath(String... array) {
        if (array == null) {
            return null;
        }

        if (array.length == 0) {
            return "";
        }

        String filepath = array[0];
        for (int i = 1; i < array.length; i++) {
            filepath = FileUtils.joinPath(filepath, array[i]);
        }
        return FileUtils.replaceFolderSeparator(filepath);
    }

    /**
     * 在路径参数 filepath 后面拼接一个字符串参数 fileOrDir （文件名或目录文件）
     *
     * @param filepath  文件绝对路径
     * @param fileOrDir 文件名 或 目录名
     * @return 文件路径
     */
    private static String joinPath(String filepath, String fileOrDir) {
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
     * 把文件移动到操作系统的回收站中
     *
     * @param file 文件或目录
     * @return 返回true表示操作成功 false表示操作失败
     * @throws IOException 访问文件错误
     */
    public static boolean move2Recycle(File file) throws IOException {
        if (file == null || !file.exists()) {
            return false;
        }

        File recycle = FileUtils.getRecyDir();
        File parent = new File(recycle, Dates.format17());
        File newfile = FileUtils.allocate(parent, file.getName());

        if (JUL.isDebugEnabled()) {
            JUL.debug(ResourcesUtils.getFilesMessage(2, file, parent));
        }

        return FileUtils.rename(file, newfile, null);
    }

    /**
     * 去掉文件扩展名
     * "mypath/myfile.txt" == "mypath/myfile"
     *
     * @param filepath 文件路径
     * @return 文件名
     */
    public static String removeFilenameExt(String filepath) {
        if (filepath == null) {
            return null;
        }

        int lx = filepath.lastIndexOf('/');
        int lf = filepath.lastIndexOf('\\');
        int lp = Math.max(lx, lf);

        int end = filepath.lastIndexOf('.');
        return end == -1 || end < lp ? filepath : filepath.substring(0, end);
    }

    /**
     * 将文件 {@code file} 重命名为 {@code dest}，如果 {@code dest} 已存在，则将 {@code dest} 重命名为 {@code bakfile}
     *
     * @param file    文件，不能为null
     * @param dest    重命名后的文件，不能为null
     * @param bakfile 备份文件，如果为null，则默认使用临时文件所在目录
     * @return 返回true表示操作成功 false表示操作失败
     */
    public static boolean rename(File file, File dest, File bakfile) throws IOException {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException(String.valueOf(file));
        }
        if (dest == null) {
            throw new NullPointerException();
        }

        if (JUL.isDebugEnabled()) {
            JUL.debug(ResourcesUtils.getFilesMessage(1, file, dest));
        }

        // 需要恢复重命名之前的状态
        boolean restore = false;
        if (dest.exists()) { // 如果目标文件已存在
            if (bakfile == null) {
                File dir = FileUtils.getTempDir(FileUtils.class.getSimpleName(), "rename", Dates.format17());
                bakfile = new File(dir, dest.getName());
            }

            // 先删除，再重命名
            if (!Atomic.delete(bakfile)) {
                throw new IOException(bakfile.getAbsolutePath());
            }

            // 重命名
            if (dest.renameTo(bakfile)) {
                restore = true;
            } else if (Atomic.copy(dest, bakfile) && Atomic.delete(dest)) {
                restore = true;
            } else {
                throw new IOException(ResourcesUtils.getIoxMessage(35, dest.getAbsolutePath(), bakfile.getAbsolutePath()));
            }
        }

        // 重命名文件
        if (file.renameTo(dest)) {
            return true;
        } else if (Atomic.copy(file, dest) && Atomic.delete(file)) {
            return true;
        } else {
            if (restore) { // 如果移动文件失败，则需要将操作回滚
                if (bakfile.renameTo(dest)) {
                    return false;
                } else if (Atomic.copy(bakfile, dest)) {
                    return false;
                }
            }
            throw new IOException(ResourcesUtils.getIoxMessage(35, file.getAbsolutePath(), dest.getAbsolutePath()));
        }
    }

    /**
     * 删除文件路径最右侧的路径分隔符与空白字符
     * 如果文件路径 path 最后一个字符不是路径分隔符则不作处理
     *
     * @param filepath 文件路径
     * @return 文件路径
     */
    public static String rtrimFolderSeparator(String filepath) {
        return filepath == null ? null : StringUtils.rtrimBlank(filepath, '/', '\\');
    }

    /**
     * 使用操作系统默认的行间分隔符替换字符序列参数 str 中的行间分隔符
     *
     * @param cs 字符序列
     * @return 字符串
     */
    public static String replaceLineSeparator(CharSequence cs) {
        return FileUtils.replaceLineSeparator(cs, FileUtils.lineSeparator);
    }

    /**
     * 使用行间分隔符参数替换字符序列参数 str 中的行分隔符
     *
     * @param cs            字符序列
     * @param lineSeparator 行间分隔符
     * @return 字符串
     */
    public static String replaceLineSeparator(CharSequence cs, String lineSeparator) {
        if (cs == null) {
            return null;
        }

        StringBuilder buf = new StringBuilder(cs.length() + 20);
        for (int i = 0, size = cs.length(); i < size; i++) {
            char c = cs.charAt(i);

            if (c == '\n') {
                buf.append(lineSeparator);
            } else if (c == '\r') {
                buf.append(lineSeparator);
                int next = i + 1;
                if (next < size && cs.charAt(next) == '\n') {
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
     * @return 字符串
     */
    public static String replaceFolderSeparator(String filepath) {
        return FileUtils.replaceFolderSeparator(filepath, File.separatorChar);
    }

    /**
     * 把文件路径参数 filepath 中的 '/' 和 '\' 字符替换成字符参数 delimiter
     *
     * @param filepath  文件路径
     * @param delimiter 替换后的分隔符
     * @return 字符串
     */
    public static String replaceFolderSeparator(String filepath, char delimiter) {
        return filepath == null ? null : filepath.replace('/', delimiter).replace('\\', delimiter);
    }

    /**
     * 替换路径中的文件夹分隔符
     *
     * @param filepath 文件路径
     * @param local    true表示替换成当前操作系统的路径分隔符 false表示替换成 '/'
     * @return 字符串
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
     * @throws IOException 访问文件错误
     */
    public static String readline(File file, String charsetName, long number) throws IOException {
        if (file == null) {
            throw new NullPointerException();
        }
        if (number < -1) {
            throw new IllegalArgumentException(String.valueOf(number));
        }
        if (StringUtils.isBlank(charsetName)) {
            charsetName = StringUtils.CHARSET;
        }

        // 读取最后一行
        if (number == -1) {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), charsetName));
            try {
                String last = null;
                for (String line; (line = in.readLine()) != null; ) {
                    last = line;
                }
                return last;
            } finally {
                in.close();
            }
        }

        // 读取所有行
        if (number == 0) {
            FileInputStream in = new FileInputStream(file);
            try {
                byte[] buf = new byte[(int) file.length()];
                int len = in.read(buf);
                if (buf.length == len) {
                    return new String(buf, charsetName);
                } else {
                    throw new IOException(buf.length + " != " + len);
                }
            } finally {
                in.close();
            }
        }

        // 读取指定行
        else {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), charsetName));
            try {
                String line;
                for (int i = 1; (line = in.readLine()) != null; i++) {
                    if (i == number) {
                        return line;
                    }
                }
                return null;
            } finally {
                in.close();
            }
        }
    }

    /**
     * 返回文件的换行符
     *
     * @param file 文件
     * @return 返回 null 表示文件不存在换行符
     * @throws IOException 访问文件错误
     */
    public static String readLineSeparator(File file) throws IOException {
        if (file == null) {
            throw new NullPointerException();
        }

        FileInputStream in = new FileInputStream(file);
        try {
            byte[] buf = new byte[IO.BYTES_BUFFER_SIZE];
            for (int len; (len = in.read(buf)) != -1; ) {
                for (int j = 0; j < len; j++) {
                    byte b = buf[j];
                    if (b == '\r') {
                        int next = j + 1;
                        if (next < len) {
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
            in.close();
        }
    }

    /**
     * 读取字符数组中出现的第一个回车符，换行符或回车换行符
     *
     * @param cs 字符串
     * @return 字符串
     */
    public static String readLineSeparator(CharSequence cs) {
        if (cs == null) {
            throw new NullPointerException();
        }

        for (int i = 0; i < cs.length(); i++) {
            char c = cs.charAt(i);
            if (c == '\r') {
                int next = i + 1;
                if (next < cs.length()) {
                    if (cs.charAt(next) == '\n') {
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
     * 单线程计算文本行数
     *
     * @param file        文件
     * @param charsetName 文件的字符集
     * @return 文件行数
     * @throws IOException 读取文件发生错误
     */
    public static long count(File file, String charsetName) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), charsetName), IO.FILE_BYTES_BUFFER_SIZE);
        try {
            long count = 0;
            while (in.readLine() != null) {
                count++;
            }
            return count;
        } finally {
            in.close();
        }
    }

    /**
     * 修改文件路径中的扩展名
     * 如：changeFilenameExt("/home/test/file.txt", "enc") 返回值: /home/test/file.enc
     * 如：changeFilenameExt("file.txt", "enc") 返回值: file.enc
     *
     * @param filepath 文件绝对路径
     * @param suffix   文件扩展名 enc txt
     * @return 文件路径
     */
    public static String changeFilenameExt(String filepath, String suffix) {
        if (filepath == null) {
            throw new NullPointerException();
        }
        if (suffix == null) {
            throw new NullPointerException();
        }

        int lastfix = filepath.lastIndexOf('.'); // 文件后缀
        int delimiter = filepath.lastIndexOf('/'); // 斜杠
        int lx = filepath.lastIndexOf('\\'); // 反斜杠
        int lf = Math.max(delimiter, lx);
        return lastfix < 0 || lastfix < lf ? (filepath + "." + suffix) : (filepath.substring(0, lastfix) + "." + suffix);
    }

    /**
     * 复制文件或目录
     *
     * @param file 文件或目录
     * @param dest 目标文件
     * @return 返回true表示操作成功 false表示操作失败
     * @throws IOException 访问文件错误
     */
    public static boolean copy(File file, File dest) throws IOException {
        if (file == null) {
            throw new NullPointerException();
        }
        if (dest == null) {
            throw new NullPointerException();
        }

        boolean value = Atomic.copy(file, dest);
        if (file.isDirectory()) {
            return log5(file, value);
        } else {
            return log6(file, value);
        }
    }

    /**
     * 将属性集合保存到文件中
     *
     * @param p    属性信息集合
     * @param file 文件
     * @throws IOException 访问文件错误
     */
    public static boolean store(Properties p, File file) throws IOException {
        if (p == null) {
            throw new NullPointerException();
        }
        if (file == null) {
            throw new NullPointerException();
        }

        FileUtils.assertCreateFile(file);
        if (!file.canWrite()) {
            throw new IllegalArgumentException(ResourcesUtils.getIoxMessage(62, file));
        }

        // 读取p对象中的参数值并替换到资源文件file中同名参数值
        StringBuilder buf = new StringBuilder((int) file.length());
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), CharsetName.ISO_8859_1));
        try {
            String line;
            while ((line = in.readLine()) != null) {
                int index = line.indexOf("=");
                if (index != -1 && !StringUtils.startsWith(line, "#", 0, false, true)) {
                    String key = FileUtils.toPropertiesStr(line.substring(0, index), true);
                    String oldValue = StringUtils.rtrim(line.substring(index + 1));
                    String newValue = FileUtils.toPropertiesStr(p.getProperty(key), false);
                    p.remove(key);
                    if (oldValue.equals(newValue)) {
                        buf.append(line);
                    } else {
                        buf.append(key).append("=").append(newValue);
                    }
                    buf.append(FileUtils.lineSeparatorUnix);
                } else {
                    buf.append(line);
                    buf.append(FileUtils.lineSeparatorUnix);
                }
            }
        } finally {
            in.close();
        }

        // 将剩余属性写入文件
        Enumeration<?> e = p.propertyNames();
        while (e.hasMoreElements()) {
            String key = FileUtils.toPropertiesStr(StringUtils.objToStr(e.nextElement()), true);
            String value = FileUtils.toPropertiesStr(p.getProperty(key), false);
            buf.append(FileUtils.lineSeparatorUnix).append(key).append("=").append(value).append(FileUtils.lineSeparatorUnix);
        }

        return FileUtils.write(file, CharsetName.UTF_8, false, buf);
    }

    /**
     * 将字符串转为存储到 properties 文件中格式
     *
     * @param str         字符串
     * @param escapeSpace true表示对字符串中的字符进行转义
     * @return 格式化之后的字符串
     */
    private static String toPropertiesStr(String str, boolean escapeSpace) {
        int length = str.length();
        StringBuilder buf = new StringBuilder(length + 20);
        for (int i = 0; i < length; i++) {
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
     * @param filepath 文件路径或资源定位信息
     * @return 资源信息
     * @throws IOException 访问文件错误
     */
    public static Properties loadProperties(String filepath) throws IOException {
        if (StringUtils.isBlank(filepath)) {
            return null;
        }

        // 如果路径是文件
        if (FileUtils.isFile(filepath)) {
            Properties p = new Properties();
            p.load(new FileInputStream(filepath));
            return p;
        }

        // 删除前缀
        String prefix = "classpath:";
        if (filepath.startsWith(prefix)) {
            filepath = filepath.substring(prefix.length());
        }

        // 如果路径是资源描述信息
        URL url = StringUtils.class.getClassLoader().getResource(filepath);
        if (url != null) {
            Properties p = new Properties();
            p.load(new FileInputStream(filepath));
            return p;
        }

        // 如果路径是资源描述信息
        InputStream in = ClassUtils.getResourceAsStream(filepath);
        if (in == null) {
            throw new UnsupportedOperationException(filepath);
        }

        try {
            Properties p = new Properties();
            p.load(in);
            return p;
        } finally {
            in.close();
        }
    }

    /**
     * 写入字符串到文件，适用于小文件
     *
     * @param file        文件
     * @param charsetName 文件的字符集
     * @param append      true表示追加写入，false表示覆盖写入
     * @param content     写入内容, null与空字符串表示清空文件内容
     * @throws IOException 访问文件错误
     */
    public static boolean write(File file, String charsetName, boolean append, CharSequence content) throws IOException {
        if (file == null) {
            throw new NullPointerException();
        }

        // 清空文件
        if (!append && (content == null || content.length() == 0) && file.exists() && file.length() == 0) {
            return true;
        }

        // 创建文件失败
        if (!Atomic.createFile(file, false)) {
            return false;
        }

        // 写入文件
        Writer out = new OutputStreamWriter(new FileOutputStream(file, append), charsetName);
        try {
            out.write(content == null ? "" : content.toString());
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
     * @return 返回true表示操作成功 false表示操作失败
     * @throws IOException 访问文件错误
     */
    public static boolean write(File file, String charsetName, boolean append, InputStream in) throws IOException {
        if (in == null) {
            throw new IllegalArgumentException();
        }
        if (!Atomic.createFile(file, false)) {
            return false;
        }
        if (StringUtils.isBlank(charsetName)) {
            charsetName = StringUtils.CHARSET;
        }

        Writer out = new OutputStreamWriter(new FileOutputStream(file, append), charsetName);
        try {
            byte[] buf = new byte[IO.BYTES_BUFFER_SIZE];
            for (int len; (len = in.read(buf)) != -1; ) {
                out.write(new String(buf, 0, len, charsetName));
                out.flush();
            }
            return true;
        } finally {
            out.close();
        }
    }

    /**
     * 在指定时间范围内，检查目录中的文件是否发生了变化（文件被写入了相同内容也算变化）
     *
     * @param dir     目录
     * @param millis  线程的休眠时间（单位毫秒）
     * @param filters 文件过滤器数组，用来筛选目录中的文件
     * @return 如果在休眠期间目录下的文件有变化，则将变化的文件存储到集合中返回
     */
    public static List<File> isWriting(File dir, long millis, FilenameFilter... filters) {
        if (!FileUtils.isDirectory(dir)) {
            throw new IllegalArgumentException(String.valueOf(dir));
        }
        if (millis < 0) {
            throw new IllegalArgumentException(String.valueOf(millis));
        }
        if (filters == null || filters.length == 0) {
            filters = new FilenameFilter[1];
            filters[0] = new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return !name.startsWith(".");
                }
            };
        }

        Map<File, Long> map = new HashMap<File, Long>();
        Atomic.loadFileTime(dir, filters, map); // 读取所有文件的最后修改时间

        // 休眠
        if (millis > 0) {
            Dates.sleep(millis, TimeUnit.MILLISECONDS);
        }

        List<File> list = new ArrayList<File>(map.size());
        Atomic.compareFileTime(dir, filters, map, list); // 比较文件的最后修改时间
        list.addAll(map.keySet()); // 将已删除的文件添加到集合中
        map.clear();
        return list;
    }

    /**
     * 判断二个文件的内容是否相等
     *
     * @param file1 文件
     * @param file2 文件
     * @param size  文件输入流的字节缓冲区大小; 小于等于零自动赋默认值
     * @return 返回true表示相等 false表示不等
     * @throws IOException 访问文件错误
     */
    public static boolean equals(File file1, File file2, int size) throws IOException {
        if (file1 == null && file2 == null) {
            return true;
        }
        if (file1 == null || file2 == null) {
            return false;
        }
        if (!file1.exists() || !file2.exists()) {
            return false;
        }
        if (file1.equals(file2)) {
            return true;
        }
        if (file1.length() != file2.length()) {
            return false;
        }
        if (size <= 0) {
            size = IO.BYTES_BUFFER_SIZE;
        }

        FileInputStream in1 = new FileInputStream(file1);
        try {
            FileInputStream in2 = new FileInputStream(file2);
            try {
                byte[] b1 = new byte[size];
                byte[] b2 = new byte[size];
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
                in2.close();
            }
        } finally {
            in1.close();
        }
    }

    /**
     * 忽略换行符的不同，判断二个文件的内容是否相等
     *
     * @param file1        文件
     * @param charsetName1 文件字符集
     * @param file2        文件
     * @param charsetName2 文件字符集
     * @param size         读文件时缓冲区大小; 小于等于零自动赋默认值
     * @return 返回0表示相等 非0表示内容不等的行号
     * @throws IOException 访问文件错误
     */
    public static long equalsIgnoreLineSeperator(File file1, String charsetName1, File file2, String charsetName2, int size) throws IOException {
        if (file1 == null || !file1.exists()) {
            throw new IllegalArgumentException(String.valueOf(file1));
        }
        if (file2 == null || !file2.exists()) {
            throw new IllegalArgumentException(String.valueOf(file2));
        }
        if (file1.equals(file2)) {
            return 0;
        }
        if (size <= 0) {
            size = IO.BYTES_BUFFER_SIZE;
        }

        BufferedReader in1 = new BufferedReader(new InputStreamReader(new FileInputStream(file1), StringUtils.charset(charsetName1)), size);
        try {
            BufferedReader in2 = new BufferedReader(new InputStreamReader(new FileInputStream(file2), StringUtils.charset(charsetName2)), size);
            try {
                long lineNumber = 0;
                while (true) {
                    lineNumber++;
                    String str1 = in1.readLine();
                    String str2 = in2.readLine();

                    // 同时读取到最后一行
                    if (str1 == null && str2 == null) {
                        return 0;
                    }

                    // 没有同时读取最后一行
                    if (str1 == null || str2 == null) {
                        return lineNumber;
                    }

                    // 内容不等
                    if (!str1.equals(str2)) {
                        return lineNumber;
                    }
                }
            } finally {
                in2.close();
            }
        } finally {
            in1.close();
        }
    }

    /**
     * 搜索文件
     *
     * @param file 文件或目录 <br>
     *             如果是文件，判断文件名与 {@code name} 参数是否匹配 <br>
     *             如果是目录，在目录中查找与 {@code name} 参数匹配的文件
     * @param name 文件名（含扩展名）或正则表达式
     * @return 匹配查找条件的文件
     */
    public static List<File> find(File file, String name) {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException(String.valueOf(file));
        }

        // 如果是目录
        if (file.isDirectory()) {
            List<File> list = new ArrayList<File>();
            Atomic.find(file, name, list);
            return list;
        }

        // 如果是文件或其他类型文件
        if (file.getName().equals(name) || file.getName().matches(name)) {
            return ArrayUtils.asList(file);
        } else {
            return new ArrayList<File>(0);
        }
    }

    // 创建文件 {0} 失败!
    private static boolean log1(File file, boolean value) {
        if (value) {
            return true;
        }

        if (JUL.isWarnEnabled()) {
            JUL.warn(ResourcesUtils.getIoxMessage(58, file == null ? "null" : file.getAbsolutePath()));
        }
        return false;
    }

    // 创建目录 {0} 失败!
    private static boolean log2(File file, boolean value) {
        if (value) {
            return true;
        }

        if (JUL.isWarnEnabled()) {
            JUL.warn(ResourcesUtils.getIoxMessage(59, file == null ? "null" : file.getAbsolutePath()));
        }
        return false;
    }

    // 删除文件 {0} 失败!
    private static boolean log3(File file, boolean value) {
        if (value) {
            return true;
        }

        if (JUL.isWarnEnabled()) {
            JUL.warn(ResourcesUtils.getIoxMessage(16, file == null ? "null" : file.getAbsolutePath()));
        }
        return false;
    }

    // 删除目录 {0} 失败!
    private static boolean log4(File file, boolean value) {
        if (value) {
            return true;
        }

        if (JUL.isWarnEnabled()) {
            JUL.warn(ResourcesUtils.getIoxMessage(57, file == null ? "null" : file.getAbsolutePath()));
        }
        return false;
    }

    // 复制目录发生错误
    private static boolean log5(File file, boolean value) {
        if (value) {
            return true;
        }

        if (JUL.isWarnEnabled()) {
            JUL.warn(ResourcesUtils.getFilesMessage(5, file == null ? "null" : file.getAbsolutePath()));
        }
        return false;
    }

    // 复制文件 {0} 发生错误!
    private static boolean log6(File file, boolean value) {
        if (value) {
            return true;
        }

        if (JUL.isWarnEnabled()) {
            JUL.warn(ResourcesUtils.getFilesMessage(3, file == null ? "null" : file.getAbsolutePath()));
        }
        return false;
    }

    // 清空目录 {0} 失败！
    private static boolean log7(File file, boolean value) {
        if (value) {
            return true;
        }

        if (JUL.isWarnEnabled()) {
            JUL.warn(ResourcesUtils.getIoxMessage(55, file));
        }
        return false;
    }

    static class Atomic {

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
         * 删除文件或目录
         *
         * @param file 文件或目录
         * @return 返回true表示删除成功 false表示删除失败
         */
        public static boolean delete(File file) {
            if (file == null) {
                return false;
            }

            // 文件不存在
            if (!file.exists()) {
                return true;
            }

            // 如果是文件
            if (file.isFile()) {
                return Atomic.deleteFile(file);
            }

            // 如果是目录
            if (file.isDirectory()) {
                return Atomic.deleteDirectory(file);
            }

            // 是其他类型的文件
            return Atomic.deleteOther(file);
        }

        /**
         * 删除文件（不能删除目录）
         *
         * @param file 文件
         * @return 返回true表示操作成功 false表示操作失败
         */
        public static boolean deleteFile(File file) {
            // 不存在
            if (!file.exists()) {
                return true;
            }

            // 不是文件
            if (!file.isFile()) {
                return false;
            }

            return file.delete();
        }

        /**
         * 删除目录及其子文件、子目录
         *
         * @param file 目录
         * @return 返回true表示删除成功 false表示删除失败
         */
        public static boolean deleteDirectory(File file) {
            // 不存在
            if (!file.exists()) {
                return true;
            }

            // 不是目录
            if (!file.isDirectory()) {
                return false;
            }

            return Atomic.clearDirectory(file) && file.delete();
        }

        /**
         * 删除其他类型的文件（不是文件，也不是目录）
         *
         * @param file 文件
         * @return 返回 true 表示操作成功 false表示操作失败
         */
        public static boolean deleteOther(File file) {
            if (file.delete()) { // 尝试删除文件
                return true;
            } else {
                return Atomic.clearDirectory(file) && file.delete(); // 尝试按目录进行删除
            }
        }

        /**
         * 删除目录下的文件与目录
         *
         * @param dir 目录
         */
        public static boolean clearDirectory(File dir) {
            Atomic.clearDir(dir);
            return true;
        }

        /**
         * 删除目录下的文件与目录
         *
         * @param dir 目录
         */
        public static boolean clearDir(File dir) {
            if (dir == null) {
                return false;
            }

            // 如果路径不存在
            if (!dir.exists()) {
                return true;
            }

            // 如果路径不是目录
            if (!dir.isDirectory()) {
                return false;
            }

            boolean success = true;
            File[] filelist = Atomic.array(dir.listFiles());
            for (File file : filelist) {
                if (file == null) {
                    continue;
                }

                // 如果是目录
                if (file.isDirectory()) {
                    Atomic.clearDir(file);
                    // 因为下面会尝试删除目录，所以此处不需要判断清空目录是否成功
                }

                // 删除目录或文件
                if (!file.delete()) {
                    success = false;
                }
            }
            return success;
        }

        /**
         * 创建文件（非目录）
         *
         * @param file  文件
         * @param force true表示强制创建文件（如果文件存在是目录，则删除目录再创建文件）<br>
         *              false表示如果文件存在，但是目录或其他类型的文件，则返回false
         * @return 返回true表示操作成功 false表示操作失败
         */
        public static boolean createFile(File file, boolean force) {
            if (file == null) {
                return false;
            }

            // 文件已存在
            if (file.exists()) {
                // 如果是一个文件，则不需要创建文件
                if (file.isFile()) {
                    return true;
                }

                // 如果文件存在且是一个目录，则删除目录再创建文件
                if (file.isDirectory()) {
                    return force && Atomic.clearDirectory(file) && file.delete() && Atomic.createFileQuiet(file);
                }

                // 如果是其他类型的文件，则尝试删除文件或按目录进行删除，再创建文件
                return force && Atomic.deleteOther(file) && Atomic.createFileQuiet(file);
            }

            // 上级文件不存在
            File parent = file.getParentFile();
            if (parent == null) { // 如果上级目录不存在
                return Atomic.createFileQuiet(file);
            }

            // 上级路径存在
            if (parent.exists()) {
                // 上级路径是一个文件
                if (parent.isFile()) {
                    return force && parent.delete() && parent.mkdirs() && Atomic.createFileQuiet(file);
                }

                // 上级路径是一个目录
                if (parent.isDirectory()) {
                    return Atomic.createFileQuiet(file);
                }

                // 上级路径是其他类型文件
                return force && Atomic.deleteOther(parent) && parent.mkdirs() && Atomic.createFileQuiet(file);
            } else {
                // 上级路径不存在
                return Atomic.createDirectory(parent, force) && Atomic.createFileQuiet(file);
            }
        }

        /**
         * 创建目录及上级目录
         *
         * @param file  文件
         * @param force true表示强制创建目录（如果文件存在但不是目录，则删除再创建文件）<br>
         *              false表示如果文件存在，但是其他类型的文件，则返回false
         * @return 返回 true 表示操作成功 false表示操作失败
         */
        public static boolean createDirectory(File file, boolean force) {
            if (file == null) {
                return false;
            }

            // 如果路径存在
            if (file.exists()) {
                // 如果是文件
                if (file.isFile()) {
                    return force && file.delete() && file.mkdir();
                }

                // 如果是目录
                if (file.isDirectory()) {
                    return true;
                }

                // 如果是其他类型的文件
                return force && Atomic.deleteOther(file) && file.mkdir();
            }

            // 如果上级路径不存在
            File parent = file.getParentFile();
            if (parent == null) { // 父目录不存在
                return file.mkdir();
            }

            // 上级路径存在
            if (parent.exists()) {
                // 如果上级路径是文件
                if (parent.isFile()) {
                    return force && parent.delete() && file.mkdirs();
                }

                // 如果上级路径是目录
                if (parent.isDirectory()) {
                    return file.mkdir();
                }

                // 如果上级路径是其他类型的文件
                return force && Atomic.deleteOther(parent) && file.mkdirs();
            } else {
                // 上级路径不存在
                return Atomic.createDirectory(parent, force) && file.mkdirs();
            }
        }

        /**
         * 复制文件或目录
         *
         * @param file 文件或目录
         * @param dest 目标文件
         * @return 返回true表示操作成功 false表示操作失败
         * @throws IOException 访问文件错误
         */
        public static boolean copy(File file, File dest) throws IOException {
            if (!file.exists()) {
                throw new IllegalArgumentException(file.getAbsolutePath());
            }

            // 文件复制自身
            if (file.equals(dest)) {
                if (JUL.isWarnEnabled()) {
                    JUL.warn(ResourcesUtils.getFilesMessage(6, file));
                }
                return true;
            }

            // 复制文件
            if (file.isFile()) {
                return Atomic.copyFile(file, dest);
            }

            // 复制目录
            if (file.isDirectory()) {
                return Atomic.copyDirectory(file, dest);
            }

            // 其他类型
            else {
                return Atomic.copyOther(file, dest);
            }
        }

        /**
         * 复制文件
         *
         * @param file 文件
         * @param dest 目标文件
         * @return 返回true表示操作成功 false表示操作失败
         * @throws IOException 访问文件错误
         */
        public static boolean copyFile(File file, File dest) throws IOException {
            if (!Atomic.createFile(dest, false)) {
                return false;
            }

            FileInputStream in = new FileInputStream(file);
            try {
                FileOutputStream out = new FileOutputStream(dest, false);
                try {
                    byte[] buf = new byte[IO.BYTES_BUFFER_SIZE];
                    for (int len; (len = in.read(buf)) != -1; ) {
                        out.write(buf, 0, len);
                        out.flush();
                    }
                } finally {
                    out.close();
                }
            } finally {
                in.close();
            }

            return dest.exists() && (dest.length() == file.length());
        }

        /**
         * 复制目录
         *
         * @param file 目录
         * @param dest 目标文件
         * @return 返回true表示操作成功 false表示操作失败
         * @throws IOException 访问文件错误
         */
        public static boolean copyDirectory(File file, File dest) throws IOException {
            if (!Atomic.createDirectory(dest, false)) {
                return false;
            }

            // 复制子文件
            boolean value = true;
            File[] files = Atomic.array(file.listFiles());
            for (File child : files) {
                File newchild = new File(dest, child.getName());
                if (!Atomic.copy(child, newchild)) {
                    value = false;
                }
            }
            return value;
        }

        /**
         * 复制其他类型的文件（非文件、非目录）
         *
         * @param file 文件
         * @param dest 目标文件
         * @return 返回true表示操作成功 false表示操作失败
         * @throws IOException 访问文件错误
         */
        public static boolean copyOther(File file, File dest) throws IOException {
            return Atomic.copyFile(file, dest) || Atomic.copyDirectory(file, dest);
        }

        /**
         * 比较文件的最后修改时间
         *
         * @param dir     目录
         * @param filters 文件过滤器数组
         * @param map     文件的历史状态，文件与文件的最后修改时间的映射关系
         * @param list    将发生变化的文件保存到集合中
         */
        public static void compareFileTime(File dir, FilenameFilter[] filters, Map<File, Long> map, List<File> list) {
            for (FilenameFilter filter : filters) {
                File[] files = Atomic.array(dir.listFiles(filter));
                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    if (file == null) {
                        continue;
                    }

                    // 如果是目录
                    if (file.isDirectory()) {
                        compareFileTime(file, filters, map, list);
                        continue;
                    }

                    // 如果是文件或其他类型文件
                    Long time = map.remove(file);
                    if (time == null || file.lastModified() != time) {
                        list.add(file); // 文件发生了变化
                    }
                }
            }
        }

        /**
         * 搜索文件
         *
         * @param dir 目录
         */
        public static void find(File dir, String name, List<File> list) {
            if (dir.getName().equals(name) || dir.getName().matches(name)) {
                list.add(dir);
            }

            File[] array = Atomic.array(dir.listFiles());
            for (File file : array) {
                if (file.isFile()) {
                    if (file.getName().equals(name) || file.getName().matches(name)) {
                        list.add(file);
                        continue;
                    }
                }

                if (file.isDirectory()) {
                    find(file, name, list);
                }
            }
        }

        /**
         * 遍历目录中的文件，并将文件的最后修改时间保存到集合map中
         *
         * @param dir     目录
         * @param filters 文件过滤器数组
         * @param map     文件与最后修改时间的映射关系
         */
        public static void loadFileTime(File dir, FilenameFilter[] filters, Map<File, Long> map) {
            for (FilenameFilter filter : filters) {
                File[] files = Atomic.array(dir.listFiles(filter));
                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    if (file == null) {
                        continue;
                    }

                    // 如果是目录
                    if (file.isDirectory()) {
                        loadFileTime(file, filters, map);
                        continue;
                    }

                    // 如果是文件或其他类型文件
                    map.put(file, file.lastModified());
                }
            }
        }

        /**
         * 创建文件
         *
         * @param file 文件
         * @return 返回 true 表示操作成功 false表示操作失败
         */
        public static boolean createFileQuiet(File file) {
            try {
                return file != null && file.createNewFile() && file.isFile();
            } catch (Throwable e) {
                if (JUL.isWarnEnabled()) {
                    JUL.warn(e.getLocalizedMessage(), e);
                }
                return false;
            }
        }

    }

}