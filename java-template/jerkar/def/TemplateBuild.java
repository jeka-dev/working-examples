import static org.jerkar.api.depmanagement.JkPopularModules.*;

import org.jerkar.api.depmanagement.*;
import org.jerkar.api.java.JkJavaVersion;
import org.jerkar.api.java.project.JkJavaProject;
import org.jerkar.tool.JkInit;
import org.jerkar.tool.JkRun;
import org.jerkar.tool.builtins.java.JkPluginJava;


/**
 * @formatter:off
 */
class TemplateBuild extends JkRun {

    JkPluginJava javaPlugin = getPlugin(JkPluginJava.class);

    @Override
    protected void setup() {
        JkJavaProject project = javaPlugin.getProject();
        project.setVersionedModule("org.jerkar:examples-java-template", "1.0");
        project.getCompileSpec().setSourceAndTargetVersion(JkJavaVersion.V8);
        project.setDependencies(JkDependencySet.of()
                .and("com.google.guava:guava:18.0")
                .and("junit:junit::4.12"));
    }

    public static void main(String[] args) {
        JkInit.instanceOf(TemplateBuild.class, args).javaPlugin.clean().pack();
    }

}