package icu.etl.maven;

import java.util.Iterator;
import java.util.Map;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.codehaus.plexus.component.repository.ComponentDescriptor;

/**
 * 插件公共工具包
 *
 * @author jeremy8551@qq.com
 * @createtime 2023/11/4
 */
public class CommonUtils {

    public static String getPuginName(AbstractMojo mojo) {
        String name = "easyetl-maven-plugin";
        try {
            Map map = mojo.getPluginContext();
            PluginDescriptor pluginDescriptor = (PluginDescriptor) map.get("pluginDescriptor");
            Iterator<ComponentDescriptor<?>> it = pluginDescriptor.getComponents().iterator();
            while (it.hasNext()) {
                ComponentDescriptor<?> next = it.next();
                String hint = next.getRoleHint();
                if (hint != null) {
                    String[] array = hint.split("\\:");
                    if (array.length >= 2) {
                        return array[1];
                    }
                }
            }
            return name;
        } catch (Exception e) {
            return name;
        }
    }

}
