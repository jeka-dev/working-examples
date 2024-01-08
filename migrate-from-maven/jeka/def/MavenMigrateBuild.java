import dev.jeka.core.api.java.JkJavaVersion;
import dev.jeka.core.tool.KBean;
import dev.jeka.core.tool.builtins.ide.IntellijKBean;
import dev.jeka.core.tool.builtins.maven.MavenMigrationKBean;
import dev.jeka.core.tool.builtins.project.ProjectKBean;

class MavenMigrateBuild extends KBean {

    ProjectKBean projectKBean = load(ProjectKBean.class);

    MavenMigrationKBean mavenMigrationKBean = load(MavenMigrationKBean.class);

    MavenMigrateBuild() {
        load(IntellijKBean.class).useJekaDefinedInModule("wrapper-common");
    }


    protected void init() {
        projectKBean.project.flatFacade()
            .setJvmTargetVersion(JkJavaVersion.V8)
            .configureCompileDependencies(deps -> deps
                .and("com.google.guava:guava:21.0"))
            .configureTestDependencies(deps -> deps
                .and("org.junit.jupiter:junit-jupiter:5.6.2"))

            // Only necessary if your project is published in a binary repository.
            .setModuleId("your.group:your.project")
            .setVersionFromGitTag();  // Version inferred from Git
    }

    public void printMigrationCode() {
        mavenMigrationKBean.migrateToCode();
    }

}