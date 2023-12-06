package icu.etl.maven;

import java.io.File;

import icu.etl.util.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * 删除工程中的所有（包含测试）java源文件与资源目录下的文件
 *
 * @author jeremy8551@qq.com
 * @createtime 2023-10-01
 */
@Mojo(name = "reset")
public class ResetMojo extends AbstractMojo {

    /**
     * 当前工程的根目录（包含pom.xml文件的目录）
     */
    @Parameter(defaultValue = "${project.basedir}")
    private File project;

    public void execute() {
        File mainJava = new File(FileUtils.joinFilepath(this.project.getAbsolutePath(), "src", "main", "java"));
        getLog().info("清空目录 " + mainJava.getAbsolutePath());
        FileUtils.clearDirectory(mainJava);

        File mainResources = new File(FileUtils.joinFilepath(this.project.getAbsolutePath(), "src", "main", "resources"));
        getLog().info("清空目录 " + mainResources.getAbsolutePath());
        FileUtils.clearDirectory(mainResources);

        File testJava = new File(FileUtils.joinFilepath(this.project.getAbsolutePath(), "src", "test", "java"));
        getLog().info("清空目录 " + testJava.getAbsolutePath());
        FileUtils.clearDirectory(testJava);

        File testResources = new File(FileUtils.joinFilepath(this.project.getAbsolutePath(), "src", "test", "resources"));
        getLog().info("清空目录 " + testResources.getAbsolutePath());
        FileUtils.clearDirectory(testResources);
    }

}

