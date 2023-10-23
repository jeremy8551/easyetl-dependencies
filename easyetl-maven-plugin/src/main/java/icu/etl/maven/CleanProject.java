package icu.etl.maven;

import java.io.File;

import icu.etl.util.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Maven插件，清空打包模块中所有的代码与class文件
 *
 * @author jeremy8551@qq.com
 * @createtime 2023-10-01
 */
@Mojo(name = "reset", defaultPhase = LifecyclePhase.CLEAN)
@Execute(phase = LifecyclePhase.CLEAN)
public class CleanProject extends AbstractMojo {

    /**
     * ${project.basedir} 表示当前模块的根目录，即包含pom.xml文件的目录
     */
    @Parameter(defaultValue = "${project.basedir}")
    private File projectBasedir;

    public void execute() throws MojoExecutionException, MojoFailureException {
        File dir0 = new File(FileUtils.joinFilepath(this.projectBasedir.getAbsolutePath(), "src", "main", "java"));
        getLog().info("clean " + dir0.getAbsolutePath());
        FileUtils.clearDirectory(dir0);

        File dir1 = new File(FileUtils.joinFilepath(this.projectBasedir.getAbsolutePath(), "src", "main", "resources"));
        getLog().info("clean " + dir1.getAbsolutePath());
        FileUtils.clearDirectory(dir1);

        File dir2 = new File(FileUtils.joinFilepath(this.projectBasedir.getAbsolutePath(), "src", "test", "java"));
        getLog().info("clean " + dir2.getAbsolutePath());
        FileUtils.clearDirectory(dir2);

        File dir3 = new File(FileUtils.joinFilepath(this.projectBasedir.getAbsolutePath(), "src", "test", "resources"));
        getLog().info("clean " + dir3.getAbsolutePath());
        FileUtils.clearDirectory(dir3);
    }

}

