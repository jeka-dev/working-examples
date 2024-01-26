import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.tool.JkInjectProject;
import dev.jeka.core.tool.KBean;
import dev.jeka.core.tool.builtins.project.ProjectKBean;

@JkInjectProject("../springboot-multi-modules.build-commons")
class UtilsBuild extends KBean {

    final JkProject project = load(ProjectKBean.class).project;

    @Override
    protected void init() {
        project.flatFacade()
            .customizeCompileDeps(deps -> deps
                .and("com.google.guava:guava")
                .withVersionProvider(BuildCommon.VERSION_PROVIDER));
    }

}