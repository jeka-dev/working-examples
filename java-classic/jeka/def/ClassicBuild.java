import dev.jeka.core.api.java.JkJavaVersion;
import dev.jeka.core.tool.JkBean;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.builtins.project.ProjectJkBean;

/**
 * @formatter:off
 */
class ClassicBuild extends JkBean {

    ProjectJkBean projectJkBean = getRuntime().getBean(ProjectJkBean.class);

    @Override
    protected void init() {
        projectJkBean.getProject().simpleFacade()
            .setJvmTargetVersion(JkJavaVersion.V8)
            .configureCompileDeps(deps -> deps
                .and("com.google.guava:guava:22.0")
                .and("com.github.djeang:vincer-dom:1.4.0")
            )
            .configureTestDeps(deps -> deps
                .and("junit:junit::4.12")
            )
            .setPublishedModuleId("org.jerkar:examples-java-classic")
            .setPublishedVersionFromGitTag();
    }

    public void cleanPack() {
        clean(); projectJkBean.pack();
    }

    public static void main(String[] args) {
        JkInit.instanceOf(ClassicBuild.class).cleanPack();
    }

}

