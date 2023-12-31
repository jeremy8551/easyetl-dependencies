package icu.etl.maven;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;

import icu.etl.util.CharTable;
import icu.etl.util.CharsetName;
import icu.etl.util.FileUtils;
import icu.etl.util.IO;
import icu.etl.util.StringUtils;
import icu.etl.util.XMLUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 将聚合工程中所有子模块（除了当前模块以外的所有子模块）的源代码和资源文件复制到当前模块中
 *
 * @author jeremy8551@qq.com
 * @createtime 2023-10-01
 */
@Mojo(name = "sources")
public class SourcesMojo extends AbstractMojo {

    /**
     * 当前工程的根目录（包含pom.xml文件的目录）
     */
    @Parameter(defaultValue = "${project.basedir}")
    private File project;

    /**
     * 源代码文件的字符集
     */
    @Parameter(defaultValue = "${maven.compiler.charset}")
    private String sourceEncoding;

    public void execute() throws MojoExecutionException {
        CharTable ct = new CharTable();
        ct.addTitle("name").addTitle("value");

        if (StringUtils.isBlank(this.sourceEncoding)) {
            this.sourceEncoding = CharsetName.UTF_8;
            ct.addCell("设置属性 ${maven.compiler.charset} 的默认值:").addCell(this.sourceEncoding);
        } else {
            this.sourceEncoding = CharsetName.UTF_8;
            ct.addCell("使用pom中设置的属性 ${maven.compiler.charset} 值:").addCell(this.sourceEncoding);
        }

        try {
            try {
                this.run(ct, this.project);
            } finally { // 如果发生异常，则先打印日志，再处理异常
                ct.toString(CharTable.Style.simple);
                for (Iterator<String> it = ct.iterator(); it.hasNext(); ) {
                    getLog().info(it.next());
                }
            }
        } catch (Throwable e) {
            String message = "easyetl 插件发生错误";
            getLog().error(message, e);
            throw new MojoExecutionException(message, e);
        }
    }

    public void run(CharTable ct, File basedir) throws Exception {
        ct.addCell("当前项目的根目录:");
        ct.addCell(basedir.getAbsolutePath());

        String currentModuleName = basedir.getName();
        File parentdir = basedir.getParentFile();
        File pomxml = new File(parentdir, "pom.xml");
        if (!pomxml.exists()) {
            throw new IOException(pomxml.getAbsolutePath());
        }
        ct.addCell("父工程的POM:");
        ct.addCell(pomxml.getAbsolutePath());

        // 返回生成jar包的目录
        File srcMainJava = this.getSrcMainJava(basedir);
        FileUtils.assertCreateDirectory(srcMainJava);
        File srcMainResources = this.getSrcMainResources(basedir);
        FileUtils.assertCreateDirectory(srcMainResources);

        ct.addCell("清空目录:");
        ct.addCell(srcMainJava.getAbsolutePath());
        FileUtils.assertClearDirectory(srcMainJava);

        ct.addCell("清空目录:");
        ct.addCell(srcMainResources.getAbsolutePath());
        FileUtils.assertClearDirectory(srcMainResources);

        ct.addCell("打包项目的源代码目录:");
        ct.addCell(srcMainJava.getAbsolutePath());
        ct.addCell("打包项目的资源目录:");
        ct.addCell(srcMainResources.getAbsolutePath());

        // 解析父工程的pom.xml文件，解析里面的模块名
        Element root = XMLUtils.getRoot(pomxml);
        NodeList list = root.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node item = list.item(i);
            if ("modules".equalsIgnoreCase(item.getNodeName())) {
                NodeList nodes = item.getChildNodes();
                for (int j = 0; j < nodes.getLength(); j++) {
                    Node node = nodes.item(j);
                    if (!"module".equalsIgnoreCase(node.getNodeName())) {
                        continue;
                    }

                    String moduleName = StringUtils.trimBlank(node.getTextContent());
                    if (StringUtils.isBlank(moduleName) || currentModuleName.equalsIgnoreCase(moduleName)) {
                        continue;
                    }

                    File module = new File(parentdir, moduleName);
                    if (!module.exists()) {
                        throw new IOException(module.getAbsolutePath());
                    }

                    File moduleSrcMainJava = this.getSrcMainJava(module);
                    ct.addCell("复制 " + moduleSrcMainJava.getAbsolutePath());
                    ct.addCell(" 到 " + srcMainJava.getAbsolutePath());
                    this.copy(ct, moduleSrcMainJava, srcMainJava);

                    File moduleSrcMainResources = this.getSrcMainResources(module);
                    if (moduleSrcMainResources.exists()) {
                        ct.addCell("复制 " + moduleSrcMainResources.getAbsolutePath());
                        ct.addCell(" 到 " + srcMainResources.getAbsolutePath());
                        this.copy(ct, moduleSrcMainResources, srcMainResources);
                    }
                }
            }
        }
    }

    /**
     * 返回存放源代码文件的目录, 就是 ${project.basedir}/src/main/java
     *
     * @param barsedir 项目的根目录（就是pom.xml文件所在的目录）
     * @return 存放源代码文件的目录
     * @throws IOException 存放源代码文件的目录不存在
     */
    private File getSrcMainJava(File barsedir) throws IOException {
        String filepath = FileUtils.joinPath(barsedir.getAbsolutePath(), "src", "main", "java");
        return new File(filepath);
    }

    /**
     * 返回存放资源文件的目录, 就是 ${project.basedir}/src/main/resources
     *
     * @param barsedir 项目的根目录（就是pom.xml文件所在的目录）
     * @return 存放资源文件的目录
     */
    private File getSrcMainResources(File barsedir) {
        String filepath = FileUtils.joinPath(barsedir.getAbsolutePath(), "src", "main", "resources");
        return new File(filepath);
    }

    /**
     * 复制文件参数file 到文件参数newFile
     *
     * @param ct   字符图形表格
     * @param file 文件或目录
     * @param dest 复制后的文件
     * @throws IOException 写文件发生错误
     */
    public void copy(CharTable ct, File file, File dest) throws IOException {
        if (file == null || !file.exists() || file.equals(dest)) {
            throw new IllegalArgumentException(StringUtils.toString(file));
        }
        if (dest == null) {
            throw new NullPointerException();
        }
        if (StringUtils.inArray(file.getName(), ".DS_Store")) { // 需要过滤的文件名
            return;
        }

        // 复制文件
        if (file.isFile()) {
            FileInputStream in = new FileInputStream(file);
            FileOutputStream out = new FileOutputStream(dest, false);
            IO.write(in, out);
            return;
        }

        // 复制目录
        if (file.isDirectory()) {
            if (!dest.exists()) {
                FileUtils.assertCreateDirectory(dest);
            }

            if (dest.exists() && !dest.isDirectory()) { // 文件存在，且文件不是一个目录
                throw new IOException(dest.getAbsolutePath() + " 不是目录!");
            }

            // 复制子文件
            File[] files = FileUtils.array(file.listFiles());
            for (File childfile : files) {
                File newfile = new File(dest, childfile.getName());
                if (newfile.exists() && !newfile.isDirectory()) {
                    // 如果是POM属性类
                    if (newfile.getName().equals(PomMojo.CLASS_NAME + ".java")) {
                        continue;
                    }

                    if (StringUtils.rtrim(childfile.getParentFile().getAbsolutePath(), '/', '\\').endsWith(FileUtils.replaceFolderSeparator("META-INF/services"))) { // 对SPI配置文件进行合并
                        ct.addCell("将文件 " + childfile.getAbsolutePath());
                        ct.addCell(" 合并到 " + newfile.getAbsolutePath());
                        this.merge(childfile, newfile);
                        continue;
                    }

                    throw new IOException("复制文件" + childfile.getAbsolutePath() + " 失败，文件 " + newfile.getAbsolutePath() + " 已存在不能覆盖!");
                } else {
                    this.copy(ct, childfile, newfile);
                }
            }
        }

        // 不支持的文件类型
        else {
            throw new UnsupportedOperationException("不支持复制 " + file.getAbsolutePath());
        }
    }

    /**
     * 将文件 {@code file} 内容追加到文件参数 {@code dest} 中
     *
     * @param file 文件
     * @param dest 文件
     * @throws IOException 访问文件错误
     */
    public void merge(File file, File dest) throws IOException {
        String content = FileUtils.readline(dest, this.sourceEncoding, 0);
        String lineSeparator = content.contains(FileUtils.lineSeparatorWindows) ? FileUtils.lineSeparatorWindows : FileUtils.lineSeparatorUnix;
        boolean addLineSeparator = !content.endsWith("\n") && !content.endsWith("\r"); // 如果文件没有以回车或换行符结尾，则返回true

        BufferedReader in = IO.getBufferedReader(file, this.sourceEncoding);
        try {
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(dest, true), this.sourceEncoding);
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    if (addLineSeparator) {
                        out.write(lineSeparator);
                        addLineSeparator = false; // 只添加一次换行符
                    }

                    out.write(line);
                    out.write(lineSeparator);
                }
                out.flush();
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }

}

