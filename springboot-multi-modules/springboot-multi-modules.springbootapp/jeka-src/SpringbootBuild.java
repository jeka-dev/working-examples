import dev.jeka.core.api.file.JkPathTree;
import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.api.system.JkLog;
import dev.jeka.core.api.tooling.intellij.JkIntellijIml;
import dev.jeka.core.tool.*;
import dev.jeka.core.tool.builtins.project.ProjectKBean;
import dev.jeka.core.tool.builtins.tooling.ide.IntellijKBean;
import dev.jeka.plugins.nodejs.JkNodeJs;
import dev.jeka.plugins.springboot.JkSpringbootProject;

import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@JkDep("dev.jeka:springboot-plugin")
@JkDep("dev.jeka:nodejs-plugin")
class SpringbootBuild extends KBean {

    final JkNodeJs nodeJs = JkNodeJs.ofVersion("26.13.1")
            .setWorkingDir(getBaseDir().resolve("web"));

    @JkInject("../springboot-multi-modules.core")
    private ProjectKBean coreProjectKBean;

    @JkPostInit(required = true)
    protected void postInit(ProjectKBean projectKBean) {
        JkProject project = projectKBean.project;
        project.flatFacade.dependencies.compile
                .add("org.springframework.boot:spring-boot-starter-webmvc")
                .add(coreProjectKBean.project.toDependency());
        project.flatFacade.dependencies.test
                .add("org.springframework.boot:spring-boot-starter-webmvc-test");
        project.compilation.postCompileActions.append("build-web-client", () -> npmBuild(project));
        JkSpringbootProject.of(project)
                .includeParentBom("4.2.0")
                .configure();
    }

    private void npmBuild(JkProject project) {
        JkLog.startTask("pack-web-project");
        Path webDist = nodeJs.getWorkingDir().resolve("dist");
        nodeJs.npm("install --loglevel=error");
        nodeJs.npm("run build");

        // Copy the result of npm build into /static dir of the server
        Path staticDir = project.compilation.layout.resolveClassDir().resolve("static");
        JkPathTree.of(webDist).createIfNotExist().copyTo(staticDir, StandardCopyOption.REPLACE_EXISTING);
        JkLog.endTask();
    }

}