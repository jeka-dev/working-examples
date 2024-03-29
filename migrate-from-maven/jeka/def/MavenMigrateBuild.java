import dev.jeka.core.api.java.JkJavaVersion;
import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.tool.JkBean;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.builtins.ide.IntellijJkBean;
import dev.jeka.core.tool.builtins.maven.MavenJkBean;
import dev.jeka.core.tool.builtins.project.ProjectJkBean;

import java.util.MissingFormatArgumentException;

class MavenMigrateBuild extends JkBean {

    ProjectJkBean projectJkBean = getBean(ProjectJkBean.class).lately(this::configure);

    MavenJkBean maven = getBean(MavenJkBean.class);

    MavenMigrateBuild() {
        getBean(IntellijJkBean.class).useJekaDefinedInModule("wrapper-common");
    }

    /*
     * Configures plugins to be bound to this command class. When this method is called, option
     * fields have already been injected from command line.
     */
    private void configure(JkProject project) {
        project.flatFacade()
            .setJvmTargetVersion(JkJavaVersion.V8)
            .configureCompileDependencies(deps -> deps
                .and("com.google.guava:guava:21.0"))
            .configureTestDependencies(deps -> deps
                .and("org.junit.jupiter:junit-jupiter:5.6.2"))

            // Only necessary if your project is published in a binary repository.
            .setPublishedModuleId("your.group:your.project")
            .setPublishedVersionFromGitTag();  // Version inferred from Git
    }

    public void cleanPack() {
        cleanOutput(); projectJkBean.pack();
    }

    public void printMigrationCode() {
        maven.migrateToCode();
    }

    public static void main(String[] args) {
        JkInit.instanceOf(MavenMigrateBuild.class, args).cleanPack();
    }

}