import dev.jeka.core.api.file.JkPathTree;
import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.api.system.JkLog;
import dev.jeka.core.tool.JkInjectClasspath;
import dev.jeka.core.tool.JkInjectProject;
import dev.jeka.core.tool.KBean;
import dev.jeka.core.tool.builtins.project.ProjectKBean;
import dev.jeka.core.tool.builtins.tooling.ide.IntellijKBean;
import dev.jeka.plugins.nodejs.JkNodeJs;
import dev.jeka.plugins.springboot.JkSpringModules.Boot;
import dev.jeka.plugins.springboot.JkSpringbootProject;

import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@JkInjectClasspath("dev.jeka:springboot-plugin")
@JkInjectClasspath("dev.jeka:nodejs-plugin")
class SpringbootBuild extends KBean {

    final JkProject project = load(ProjectKBean.class).project;

    final JkNodeJs nodeJs = JkNodeJs.ofVersion("18.12.0")
            .setWorkingDir(getBaseDir().resolve("web"));

    @JkInjectProject("../springboot-multi-modules.core")
    private CoreBuild coreBuild;

    SpringbootBuild() {
        load(IntellijKBean.class).useJekaDefinedInModule("wrapper-common");
    }

    @Override
    protected void init() {
        project.flatFacade()
                .customizeCompileDeps(deps -> deps
                    .and(Boot.STARTER_WEB)
                    .and(this.coreBuild.project.toDependency()))
                .customizeTestDeps(deps -> deps
                        .and(Boot.STARTER_TEST));
        project.compilation.postCompileActions.append("Web client build", this::npmBuild);
        JkSpringbootProject.of(project)
                .includeParentBom("3.2.1")
                .configure();
    }

    private void npmBuild() {
        JkLog.startTask("Packing web project");
        Path webDist = nodeJs.getWorkingDir().resolve("dist");
        nodeJs.npm("install --loglevel=error");
        nodeJs.npm("run build");

        // Copy the result of npm build into /static dir of the server
        Path staticDir = project.compilation.layout.resolveClassDir().resolve("static");
        JkPathTree.of(webDist).createIfNotExist().copyTo(staticDir, StandardCopyOption.REPLACE_EXISTING);
        JkLog.endTask();
    }

}