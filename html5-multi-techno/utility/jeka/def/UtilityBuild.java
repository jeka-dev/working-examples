import dev.jeka.core.api.depmanagement.JkDependencySet;
import dev.jeka.core.tool.JkCommands;
import dev.jeka.core.tool.builtins.java.JkPluginJava;

/**
 * @formatter:off
 */
class UtilityBuild extends JkCommands {

    final JkPluginJava javaPlugin = getPlugin(JkPluginJava.class);

    UtilityBuild() {
        javaPlugin.getProject().setDependencies(JkDependencySet.of()
            .and("com.google.guava:guava:jar:23.0"));
    }

 
}