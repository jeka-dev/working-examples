import dev.jeka.core.api.java.JkJavaVersion;
import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.tool.JkBean;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.builtins.maven.MavenJkBean;
import dev.jeka.core.tool.builtins.project.ProjectJkBean;

class Build extends JkBean {

    ProjectJkBean projectJkBean = getBean(ProjectJkBean.class).configure(this::configure);

    MavenJkBean maven = getBean(MavenJkBean.class);

    /*
     * Configures plugins to be bound to this command class. When this method is called, option
     * fields have already been injected from command line.
     */
    private void configure(JkProject project) {
        project.simpleFacade()
            .setJvmTargetVersion(JkJavaVersion.V8)
            .configureCompileDeps(deps -> deps
                .and("com.google.guava:guava:21.0"))
            .configureTestDeps(deps -> deps
                .and("org.junit.jupiter:junit-jupiter:5.6.2"))

            // Only necessary if your project is published in a binary repository.
            .setPublishedModuleId("your.group:your.project")
            .setPublishedVersionFromGitTag();  // Version inferred from Git
    }

    public void cleanPack() {
        clean(); projectJkBean.pack();
    }

    public void printMigrationCode() {
        maven.migrationCode();
    }

    public static void main(String[] args) {
        JkInit.instanceOf(Build.class, args).cleanPack();
    }

}