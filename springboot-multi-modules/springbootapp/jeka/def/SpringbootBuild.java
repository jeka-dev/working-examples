import dev.jeka.core.api.depmanagement.JkDependencySet;
import dev.jeka.core.api.file.JkPathTree;
import dev.jeka.core.api.system.JkLog;
import dev.jeka.core.api.system.JkProcess;
import dev.jeka.core.tool.JkCommandSet;
import dev.jeka.core.tool.JkDefClasspath;
import dev.jeka.core.tool.JkDefImport;
import dev.jeka.core.tool.builtins.java.JkPluginJava;
import dev.jeka.plugins.springboot.JkPluginSpringboot;
import dev.jeka.plugins.springboot.JkSpringModules.Boot;

import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static dev.jeka.core.api.depmanagement.JkScope.TEST;

@JkDefClasspath("dev.jeka:springboot-plugin:2.3.2.RELEASE")
class SpringbootBuild extends JkCommandSet {

    final JkPluginJava java = getPlugin(JkPluginJava.class);

    private final JkPluginSpringboot springboot = getPlugin(JkPluginSpringboot.class);

    @JkDefImport("../core")
    private CoreBuild coreBuild;

    @Override
    protected void setup() {
        springboot.setSpringbootVersion("2.2.4.RELEASE");
        java.getProject()
            .getDependencyManagement()
                .addDependencies(JkDependencySet.of()
                .and(Boot.STARTER_WEB)
                .and(Boot.STARTER_TEST, TEST)
                .and(coreBuild.java.getProject().toDependency())).__
            .getProduction()
                .getCompilation()
                    .getAfterCompile().append(this::packWeb);
    }

    public void cleanPack() {
        clean(); java.pack();
    }

    public void run() {
        this.springboot.run();
    }

    private void packWeb() {
        JkLog.startTask("Packing web project");
        Path webDir = getBaseDir().resolve("../web");
        Path webDist = webDir.resolve("dist");
        Path staticDir = java.getProject().getProduction().getCompilation().getLayout().resolveClassDir().resolve("static");
        JkProcess.of("npm", "run", "build").withWorkingDir(webDir).runSync();
        JkPathTree.of(webDist).copyTo(staticDir, StandardCopyOption.REPLACE_EXISTING);
        JkLog.endTask();
    }

}