import dev.jeka.core.api.depmanagement.JkDependencySet;
import dev.jeka.core.api.java.JkJavaVersion;
import dev.jeka.core.api.java.project.JkJavaProject;
import dev.jeka.core.api.tooling.JkGitWrapper;
import dev.jeka.core.tool.JkCommandSet;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.builtins.java.JkPluginJava;

/**
 * @formatter:off
 */
class ClassicBuild extends JkCommandSet {

    JkPluginJava java = getPlugin(JkPluginJava.class);

    @Override
    protected void setup() {
        java.getProject()
            .getJarProduction()
                .getDependencyManagement()
                    .addDependencies(JkDependencySet.of()
                        .and("com.google.guava:guava:18.0")
                        .and("junit:junit::4.12")).__
                .getCompilation()
                    .setJavaVersion(JkJavaVersion.V8).__.__
            .getPublication()
                .setModuleId("org.jerkar:examples-java-classic")
                .setVersion(JkGitWrapper.of(getBaseDir()).getVersionFromTags());
    }

    public void cleanPack() {
        clean(); java.pack();
    }

}

