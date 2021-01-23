import dev.jeka.core.api.depmanagement.JkDependencySet;
import dev.jeka.core.api.depmanagement.JkScope;
import dev.jeka.core.api.java.JkJavaVersion;
import dev.jeka.core.api.java.project.JkJavaProject;
import dev.jeka.core.api.tooling.JkGitWrapper;
import dev.jeka.core.tool.JkCommandSet;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.builtins.git.JkPluginGit;
import dev.jeka.core.tool.builtins.java.JkPluginJava;

/**
 * @formatter:off
 */
class ClassicBuild extends JkCommandSet {

    JkPluginJava java = getPlugin(JkPluginJava.class);

    JkPluginGit git = getPlugin(JkPluginGit.class);

    @Override
    protected void setup() {
        java.getProject().simpleFacade()
            .setJavaVersion(JkJavaVersion.V8)
            .addDependencies(JkDependencySet.of()
                .and("com.google.guava:guava:18.0")
                .and("junit:junit::4.12", JkScope.TEST)
            )
            .setPublishedModuleId("org.jerkar:examples-java-classic")
            .setPublishedVersion(git.getWrapper()::getVersionFromTags);
    }

    public void cleanPack() {
        clean(); java.pack();
    }

}

