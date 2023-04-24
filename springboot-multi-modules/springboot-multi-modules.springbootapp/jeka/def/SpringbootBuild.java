import dev.jeka.core.api.file.JkPathTree;
import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.api.system.JkLog;
import dev.jeka.core.tool.JkBean;
import dev.jeka.core.tool.JkInjectClasspath;
import dev.jeka.core.tool.JkInjectProject;
import dev.jeka.core.tool.builtins.ide.IntellijJkBean;
import dev.jeka.plugins.nodejs.NodeJsJkBean;
import dev.jeka.plugins.springboot.JkSpringModules.Boot;
import dev.jeka.plugins.springboot.SpringbootJkBean;

import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@JkInjectClasspath("dev.jeka:springboot-plugin")
@JkInjectClasspath("dev.jeka:nodejs-plugin")
class SpringbootBuild extends JkBean {

    final SpringbootJkBean springboot = getBean(SpringbootJkBean.class);

    final NodeJsJkBean nodeJs = getBean(NodeJsJkBean.class);

    @JkInjectProject("../springboot-multi-modules.core")
    private CoreBuild coreBuild;

    SpringbootBuild() {
        springboot.setSpringbootVersion("2.7.3");
        springboot.projectBean.configure(this::configure);
        getBean(IntellijJkBean.class).useJekaDefinedInModule("wrapper-common");
        nodeJs.setWorkingDir(getBaseDir().resolve("../web"));
    }

    private void configure(JkProject project) {
        project.flatFacade()
                .applyOnProject(BuildCommon::setup)
                .configureCompileDependencies(deps -> deps
                    .and(Boot.STARTER_WEB)
                    .and(this.coreBuild.projectJkBean.getProject().toDependency()))
                .configureTestDependencies(deps -> deps
                        .and(Boot.STARTER_TEST))
                .getProject().compilation.postCompileActions.append("Web client buiild", this::npmBuild);
    }

    public void cleanPack() {
        cleanOutput(); springboot.projectBean.pack();
    }

    public void run() {
        this.springboot.projectBean.runJar();
    }

    private void npmBuild() {
        JkLog.startTask("Packing web project");
        Path webDir = getBaseDir().resolve("../web");
        Path webDist = webDir.resolve("dist");
        Path staticDir = springboot.projectBean.getProject().compilation
                .layout.resolveClassDir().resolve("static");

        nodeJs.npm("install --loglevel=error");
        nodeJs.npm("run build");
        JkPathTree.of(webDist).createIfNotExist().copyTo(staticDir, StandardCopyOption.REPLACE_EXISTING);
        JkLog.endTask();
    }

}