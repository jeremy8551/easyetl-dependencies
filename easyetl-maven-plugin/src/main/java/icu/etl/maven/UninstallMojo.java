package icu.etl.maven;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;

import icu.etl.util.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * 从本地仓库中删除当前工程已安装的jar文件
 *
 * @author jeremy8551@qq.com
 * @createtime 2023-11-04
 */
@Mojo(name = "uninstall")
public class UninstallMojo extends AbstractMojo {

    /**
     * 本地仓库的绝对路径
     */
    @Parameter(defaultValue = "${settings.localRepository}")
    private File localRepository;

    /**
     * 当前项目的 groupId
     */
    @Parameter(defaultValue = "${project.groupId}")
    private String groupId;

    /**
     * 当前项目的 artifactId
     */
    @Parameter(defaultValue = "${project.artifactId}")
    private String artifactId;

    /**
     * 当前项目的 version
     */
    @Parameter(defaultValue = "${project.version}")
    private String version;

    public void execute() throws MojoExecutionException {
        if (!this.localRepository.exists()) {
            throw new MojoExecutionException("本地仓库 " + this.localRepository.getAbsolutePath() + " 不存在!");
        }

        File groupDir = new File(this.localRepository, this.groupId.replace('.', File.separatorChar));
        if (!groupDir.exists()) {
            getLog().info("目录 " + groupDir.getAbsolutePath() + " 不存在!");
            return;
        }
        if (!groupDir.isDirectory()) {
            throw new MojoExecutionException("路径 " + groupDir.getAbsolutePath() + " 不是合法目录!");
        }

        File artifactId = new File(groupDir, this.artifactId.replace('.', File.separatorChar));
        if (!artifactId.exists()) {
            getLog().info("目录 " + artifactId.getAbsolutePath() + " 不存在!");
            return;
        }
        if (!artifactId.isDirectory()) {
            throw new MojoExecutionException("路径 " + artifactId.getAbsolutePath() + " 不是合法目录!");
        }

        // 删除全部版本
        if ("all".equalsIgnoreCase(this.version)) {
            for (Iterator<File> it = Arrays.asList(FileUtils.array(artifactId.listFiles())).iterator(); it.hasNext(); ) {
                File file = it.next();
                if (file.isDirectory()) {
                    this.uninstall(artifactId, file.getName());
                }
            }
            this.cleanDirectory(artifactId);
        } else { // 卸载某个版本
            this.uninstall(artifactId, this.version);
        }
    }

    /**
     * 删除指定版本
     *
     * @param artifactId 本地仓库中工件目录
     * @param name       版本号
     * @throws MojoExecutionException 插件错误
     */
    private void uninstall(File artifactId, String name) throws MojoExecutionException {
        File dir = new File(artifactId, name);
        if (!dir.exists()) {
            getLog().info("目录 " + dir.getAbsolutePath() + " 不存在!");
            return;
        }

        if (dir.isDirectory()) {
            this.cleanDirectory(dir);
            dir.delete();
        } else {
            throw new MojoExecutionException("路径 " + dir.getAbsolutePath() + " 不是合法目录!");
        }
    }

    /**
     * 清空目录中的所有文件
     *
     * @param dir 目录
     */
    public void cleanDirectory(File dir) {
        if (dir == null || !dir.exists() || dir.isFile()) {
            return;
        }

        File[] files = FileUtils.array(dir.listFiles());
        for (File file : files) {
            if (file == null) {
                continue;
            }

            if (file.isDirectory()) {
                this.cleanDirectory(file);
            }

            getLog().info("删除文件 " + file.getAbsolutePath() + " [" + (file.delete() ? "成功" : "失败") + "]");
        }
    }

}

