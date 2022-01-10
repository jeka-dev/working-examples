import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.tool.JkBean;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.JkInjectProject;
import dev.jeka.core.tool.builtins.ide.IntellijJkBean;
import dev.jeka.core.tool.builtins.project.ProjectJkBean;

/**
 * @formatter:off
 */
@JkInjectProject("../build-commons")
class UtilsBuild extends JkBean {

    final ProjectJkBean projectJkBean = getBean(ProjectJkBean.class).configure(this::configure);

    UtilsBuild() {
        getBean(IntellijJkBean.class).skipJeka();
    }

    private void configure(JkProject project) {
        project.simpleFacade()
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