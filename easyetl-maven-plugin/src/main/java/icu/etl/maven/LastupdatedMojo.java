package icu.etl.maven;

import java.io.File;

import icu.etl.util.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * 删除本地仓库中依赖下载失败所产生的临时文件( lastupdated )
 *
 * @author jeremy8551@qq.com
 * @createtime 2023/11/4
 */
@Mojo(name = "lastupdated", defaultPhase = LifecyclePhase.CLEAN)
@Execute(phase = LifecyclePhase.CLEAN)
public class LastupdatedMojo extends AbstractMojo {

    /**
     * 本地仓库的绝对路径
     */
    @Parameter(defaultValue = "${settings.localRepository}")
    private File localRepository;

    @Override
    public void execute() throws MojoExecutionException {
        if (this.localRepository != null && this.localRepository.exists() && this.localRepository.isDirectory()) {
            this.clear(this.localRepository);
        } else {
            throw new MojoExecutionException("本地仓库 " + this.localRepository.getAbsolutePath() + " 不存在!");
        }
    }

    /**
     * 清空目录中的所有文件
     *
     * @param dir 目录
     */
    public void clear(File dir) {
        File[] files = FileUtils.array(dir.listFiles());
        for (File file : files) {
            if (file == null) {
                continue;
            }

            if (file.isDirectory()) {
                this.clear(file);
                continue;
            }

            if (file.isFile() && file.getName().endsWith(".lastupdated")) {
                getLog().info("删除文件 " + file.getAbsolutePath() + " [" + (file.delete() ? "成功" : "失败") + "]");
                continue;
            }
        }
    }

}
