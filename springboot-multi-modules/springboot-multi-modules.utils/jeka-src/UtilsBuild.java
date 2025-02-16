import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.api.tooling.intellij.JkIml;
import dev.jeka.core.tool.JkInject;

import dev.jeka.core.tool.JkPostInit;
import dev.jeka.core.tool.KBean;
import dev.jeka.core.tool.builtins.project.ProjectKBean;
import dev.jeka.core.tool.builtins.tooling.ide.IntellijKBean;

@JkInject("../springboot-multi-modules.build-commons")
class UtilsBuild extends KBean {

    @JkPostInit(required = true)
    private void postInit(ProjectKBean projectKBean) {
        projectKBean.project.compilation.dependencies
                .add("com.google.guava:guava")
                .addVersionProvider(BuildCommon.VERSION_PROVIDER);
    }

    @JkPostInit
    private void postInit(IntellijKBean intellijKBean) {
        intellijKBean.addModule("springboot-multi-modules.build-commons", JkIml.Scope.TEST);
    }

}