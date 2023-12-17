package icu.etl.maven;

import java.io.File;
import java.io.InputStream;

import icu.etl.util.ClassUtils;
import icu.etl.util.Ensure;
import icu.etl.util.FileUtils;
import icu.etl.util.IO;
import icu.etl.util.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * 生成 POM 文件的属性类
 *
 * @author jeremy8551@qq.com
 * @createtime 2023/12/16
 */
@Mojo(name = "pom", requiresProject = false, aggregator = true)
public class PomMojo extends AbstractMojo {

    /** 类名 */
    public final static String CLASS_NAME = "ProjectPom";

    /**
     * 当前工程POM中定义的groupId
     */
    @Parameter(defaultValue = "${project.groupId}")
    private String projectGroupId;

    /**
     * 当前工程POM中定义的artifactId
     */
    @Parameter(defaultValue = "${project.artifactId}")
    private String projectArtifactId;

    /**
     * 当前工程POM中定义的version
     */
    @Parameter(defaultValue = "${project.version}")
    private String projectVersion;

    /**
     * 源代码目录, src/main/java/
     */
    @Parameter(defaultValue = "${project.build.sourceDirectory}")
    private String sourceDir;

    /**
     * 工程源代码字符集
     */
    @Parameter(defaultValue = "${project.build.sourceEncoding}")
    private String charsetName;

    /**
     * 包名
     */
    @Parameter
    private String packageName;

    public void execute() throws MojoFailureException {
        FileUtils.assertCreateDirectory(this.sourceDir);

        // 创建目录
        String packageName = StringUtils.defaultString(this.packageName, ClassUtils.getPackageName(PomMojo.class, 2)); // 包名
        String filepath = FileUtils.joinPath(this.sourceDir, packageName.replace('.', '/'));
        File dir = new File(filepath);
        FileUtils.assertCreateDirectory(dir);

        String name = CLASS_NAME; // 类名
        String javaFile = name + ".java"; // java文件名
        File classfile = new File(dir, javaFile); // 类文件
        this.getLog().info("生成POM类: " + classfile.getAbsolutePath() + " ..");

        String uri = "/" + PomMojo.class.getPackage().getName().replace('.', '/') + "/ProjectPom.txt";
        InputStream in = Ensure.notNull(ClassUtils.getResourceAsStream(uri, this));
        try {
            String str = new String(IO.read(in), this.charsetName);
            String src = StringUtils.replaceVariable(str, "packageName", packageName, "className", name, "groupId", this.projectGroupId, "artifactId", this.projectArtifactId, "version", this.projectVersion);
            Ensure.isTrue(FileUtils.write(classfile, this.charsetName, false, src));
        } catch (Exception e) {
            throw new MojoFailureException(e.getLocalizedMessage(), e);
        } finally {
            IO.close(in);
        }
    }

}
