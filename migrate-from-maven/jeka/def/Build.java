import dev.jeka.core.api.java.JkJavaVersion;
import dev.jeka.core.tool.JkBean;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.builtins.project.ProjectJkBean;

class Build extends JkBean {

    final ProjectJkBean projectJkBean = getRuntime().getBean(ProjectJkBean.class);

    /*
     * Configures plugins to be bound to this command class. When this method is called, option
     * fields have already been injected from command line.
     */
    @Override
    protected void init() {
        projectJkBean.getProject().simpleFacade()
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

    public static void main(String[] args) {
        JkInit.instanceOf(Build.class, args).cleanPack();
    }

}