
import dev.jeka.core.api.depmanagement.JkDependencySet;
import dev.jeka.core.api.java.JkJavaVersion;
import dev.jeka.core.api.java.project.JkJavaProject;
import dev.jeka.core.tool.JkCommands;

import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.builtins.java.JkPluginJava;

/**
 * @formatter:off
 */
class ClassicBuild extends JkCommands {

    JkPluginJava javaPlugin = getPlugin(JkPluginJava.class);

    @Override
    protected void setup() {
        JkJavaProject project = javaPlugin.getProject();
        project.setVersionedModule("org.jerkar:examples-java-classic", "1.0");
        project.getCompileSpec().setSourceAndTargetVersion(JkJavaVersion.V8);
        project.addDependencies(JkDependencySet.of()
                .and("com.google.guava:guava:18.0")
                .and("junit:junit::4.12"));
    }

    public void cleanPack() {
        clean(); javaPlugin.pack();
    }

    public static void main(String[] args) {
        JkInit.instanceOf(ClassicBuild.class, args).cleanPack();
    }

}