import dev.jeka.core.api.depmanagement.JkDependencySet;
import dev.jeka.core.tool.JkClass;
import dev.jeka.core.tool.JkDefImport;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.builtins.java.JkPluginJava;

/**
 * @formatter:off
 */
@JkDefImport("../build-commons")
class UtilsBuild extends JkClass {

    final JkPluginJava java = getPlugin(JkPluginJava.class);

    UtilsBuild() {
        java.getProject().simpleFacade()
            .applyOnProject(BuildCommon::setup)
            .addDependencies(JkDependencySet.of()
                .and("com.google.guava:guava")
                .withVersionProvider(BuildCommon.VERSION_PROVIDER));
    }

    public void cleanPack() {
        clean(); java.pack();
    }

    public static void main(String[] args) {
        JkInit.instanceOf(UtilsBuild.class).cleanPack();
    }
 
}