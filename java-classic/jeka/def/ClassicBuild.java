import dev.jeka.core.api.java.JkJavaVersion;
import dev.jeka.core.tool.JkClass;
import dev.jeka.core.tool.builtins.git.JkPluginGit;
import dev.jeka.core.tool.builtins.java.JkPluginJava;

/**
 * @formatter:off
 */
class ClassicBuild extends JkClass {

    JkPluginJava java = getPlugin(JkPluginJava.class);

    JkPluginGit git = getPlugin(JkPluginGit.class);

    @Override
    protected void setup() {
        java.getProject().simpleFacade()
            .setJavaVersion(JkJavaVersion.V8)
            .setCompileDependencies(deps -> deps
                .and("com.google.guava:guava:22.0")
                .and("com.github.djeang:vincer-dom:1.4.0")
            )
            .setTestDependencies(deps -> deps
                .and("junit:junit::4.12")
            )
            .setPublishedMavenModuleId("org.jerkar:examples-java-classic")
            .setPublishedMavenVersionFromGitTag();
    }

    public void cleanPack() {
        clean(); java.pack();
    }

}

