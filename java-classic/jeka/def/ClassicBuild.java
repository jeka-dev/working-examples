import dev.jeka.core.api.depmanagement.JkPopularLibs;
import dev.jeka.core.api.java.JkJavaVersion;
import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.tool.JkBean;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.builtins.project.ProjectJkBean;

/**
 * @formatter:off
 */
class ClassicBuild extends JkBean {

    ProjectJkBean projectJkBean = getBean(ProjectJkBean.class).configure(this::configure);

    private void configure(JkProject project) {
        project.flatFacade()
            .setJvmTargetVersion(JkJavaVersion.V8)
            .configureCompileDependencies(deps -> deps
                .and("com.google.guava:guava:22.0")
                .and("com.github.djeang:vincer-dom:1.4.0")
            )
            .configureTestDependencies(deps -> deps
                .and(JkPopularLibs.JUNIT_5.toCoordinate("5.8.1"))
            )
            .setPublishedModuleId("org.jerkar:examples-java-classic")
            .setPublishedVersionFromGitTag();
    }

    public void cleanPack() {
        cleanOutput(); projectJkBean.pack();
    }

    public static void main(String[] args) {
        JkInit.instanceOf(ClassicBuild.class).cleanPack();
    }

}

