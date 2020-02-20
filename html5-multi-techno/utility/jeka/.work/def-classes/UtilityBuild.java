import dev.jeka.core.api.depmanagement.JkDependencySet;
import dev.jeka.core.tool.JkCommands;
import dev.jeka.core.tool.JkImport;
import dev.jeka.core.tool.JkImportProject;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.builtins.java.JkPluginJava;

/**
 * @formatter:off
 */
@JkImportProject("../build-commons")
class UtilityBuild extends JkCommands {

    final JkPluginJava javaPlugin = getPlugin(JkPluginJava.class);

    UtilityBuild() {
        Common.setup(javaPlugin.getProject());
        javaPlugin.getProject().setDependencies(JkDependencySet.of()
                .and("com.google.guava:guava")
                .withVersionProvider(Common.VERSION_PROVIDER)
        );
    }

    public void cleanPack() {
        clean(); javaPlugin.pack();
    }

    public static void main(String[] args) {
        JkInit.instanceOf(UtilityBuild.class).cleanPack();
    }
 
}