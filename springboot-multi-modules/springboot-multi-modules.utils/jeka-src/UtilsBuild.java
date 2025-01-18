import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.tool.JkInjectRunbase;
import dev.jeka.core.tool.KBean;
import dev.jeka.core.tool.builtins.project.ProjectKBean;

@JkInjectRunbase("../springboot-multi-modules.build-commons")
class UtilsBuild extends KBean {

    final JkProject project = load(ProjectKBean.class).project;

    @Override
    protected void init() {
        project.compilation.dependencies
                .add("com.google.guava:guava")
                .addVersionProvider(BuildCommon.VERSION_PROVIDER);
    }

}