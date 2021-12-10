import dev.jeka.core.tool.JkBean;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.JkInjectProject;
import dev.jeka.core.tool.builtins.project.ProjectJkBean;

/**
 * @formatter:off
 */
@JkInjectProject("../build-commons")
class UtilsBuild extends JkBean {

    final ProjectJkBean projectJkBean = getRuntime().getBean(ProjectJkBean.class);

    UtilsBuild() {
        projectJkBean.getProject().simpleFacade()
            .applyOnProject(BuildCommon::setup)
            .configureCompileDeps(deps -> deps
                .and("com.google.guava:guava")
                .withVersionProvider(BuildCommon.VERSION_PROVIDER));
    }

    public void cleanPack() {
        clean(); projectJkBean.pack();
    }

    public static void main(String[] args) {
        JkInit.instanceOf(UtilsBuild.class).cleanPack();
    }
 
}