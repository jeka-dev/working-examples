import dev.jeka.core.api.file.JkPathTree;
import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.api.system.JkLog;
import dev.jeka.core.api.system.JkProcess;
import dev.jeka.core.tool.JkBean;
import dev.jeka.core.tool.JkInjectClasspath;
import dev.jeka.core.tool.JkInjectProject;
import dev.jeka.core.tool.builtins.ide.IntellijJkBean;
import dev.jeka.plugins.springboot.JkSpringModules.Boot;
import dev.jeka.plugins.springboot.SpringbootJkBean;

import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@JkInjectClasspath("dev.jeka:springboot-plugin")
class SpringbootBuild extends JkBean {

    final SpringbootJkBean springboot = getBean(SpringbootJkBean.class);

    @JkInjectProject("../springboot-multi-modules.core")
    private CoreBuild coreBuild;

    SpringbootBuild() {
        springboot.setSpringbootVersion("2.5.5");
        springboot.projectBean().configure(this::configure);
    }

    private void configure(JkProject project) {
        project.simpleFacade()
                .applyOnProject(BuildCommon::setup)
                .configureCompileDeps(deps -> deps
                    .and(Boot.STARTER_WEB)
                    .and(this.coreBuild.projectJkBean.getProject().toDependency()))
                .configureTestDeps(deps -> deps
                        .and(Boot.STARTER_TEST))
                .getProject().getConstruction().getCompilation()
                    .getPostCompileActions()
                        .append(this::npmBuild);
    }

    public void cleanPack() {
        clean(); springboot.projectBean().pack();
    }

    public void run() {
        this.springboot.run();
    }

    private void npmBuild() {
        JkLog.startTask("Packing web project");
        Path webDir = getBaseDir().resolve("../web");
        Path webDist = webDir.resolve("dist");
        Path staticDir = springboot.projectBean().getProject().getConstruction().getCompilation()
                .getLayout().resolveClassDir().resolve("static");
        JkProcess.of("npm", "run", "build").setWorkingDir(webDir).exec();
        JkPathTree.of(webDist).copyTo(staticDir, StandardCopyOption.REPLACE_EXISTING);
        JkLog.endTask();
    }

}