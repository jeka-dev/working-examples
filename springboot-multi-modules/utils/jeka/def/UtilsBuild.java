import dev.jeka.core.api.depmanagement.JkDependencySet;
import dev.jeka.core.tool.JkCommandSet;
import dev.jeka.core.tool.JkDefImport;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.builtins.java.JkPluginJava;

/**
 * @formatter:off
 */
@JkDefImport("../build-commons")
class UtilsBuild extends JkCommandSet {

    final JkPluginJava javaPlugin = getPlugin(JkPluginJava.class);

    UtilsBuild() {
        BuildCommon.setup(javaPlugin.getProject());
        javaPlugin.getProject().setDependencies(JkDependencySet.of()
                .and("com.google.guava:guava")
                .withVersionProvider(BuildCommon.VERSION_PROVIDER)
        );
    }

    public void cleanPack() {
        clean(); javaPlugin.pack();
    }

    public static void main(String[] args) {
        JkInit.instanceOf(UtilsBuild.class).cleanPack();
    }
 
}