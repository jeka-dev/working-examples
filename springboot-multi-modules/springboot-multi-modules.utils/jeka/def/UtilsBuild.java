import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.tool.JkInjectProject;
import dev.jeka.core.tool.KBean;
import dev.jeka.core.tool.builtins.project.ProjectKBean;

@JkInjectProject("../springboot-multi-modules.build-commons")
class UtilsBuild extends KBean {

    private ProjectKBean projectKBean = load(ProjectKBean.class);

    final JkProject project = projectKBean.project;

    @Override
    protected void init() {
        project.flatFacade()
            .configureCompileDependencies(deps -> deps
                .and("com.google.guava:guava")
                .withVersionProvider(BuildCommon.VERSION_PROVIDER));
    }

}