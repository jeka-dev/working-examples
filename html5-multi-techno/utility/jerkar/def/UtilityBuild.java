import org.jerkar.api.depmanagement.JkDependencySet;
import org.jerkar.tool.JkRun;
import org.jerkar.tool.builtins.java.JkPluginJava;

/**
 * @formatter:off
 */
class UtilityBuild extends JkRun {

    final JkPluginJava javaPlugin = getPlugin(JkPluginJava.class);

    UtilityBuild() {
        javaPlugin.getProject().setDependencies(JkDependencySet.of()
            .and("com.google.guava:guava:jar:23.0"));
    }

 
}