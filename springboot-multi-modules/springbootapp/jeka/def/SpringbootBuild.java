import dev.jeka.core.api.depmanagement.JkDependencySet;
import dev.jeka.core.api.file.JkPathTree;
import dev.jeka.core.api.java.project.JkJavaProject;
import dev.jeka.core.api.system.JkLog;
import dev.jeka.core.api.system.JkProcess;
import dev.jeka.core.tool.*;
import dev.jeka.core.tool.builtins.java.JkPluginJava;
import dev.jeka.plugins.springboot.JkPluginSpringboot;
import dev.jeka.plugins.springboot.JkSpringModules.Boot;

import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static dev.jeka.core.api.depmanagement.JkJavaDepScopes.TEST;

@JkDefClasspath("../../../../IdeaProjects/springboot-plugin/dev.jeka.plugins.spring-boot/jeka/output/dev.jeka.springboot-plugin.jar")
class SpringbootBuild extends JkCommandSet {

    final JkPluginJava javaPlugin = getPlugin(JkPluginJava.class);

    private final JkPluginSpringboot springbootPlugin = getPlugin(JkPluginSpringboot.class);

    @JkDefImport("../core")
    private CoreBuild coreBuild;

    @Override
    protected void setup() {
        springbootPlugin.setSpringbootVersion("2.2.4.RELEASE");
        JkJavaProject project = javaPlugin.getProject();
        project.addDependencies(JkDependencySet.of()
                .and(Boot.STARTER_WEB)
                .and(Boot.STARTER_TEST, TEST)
                .and(coreBuild.javaPlugin.getProject())
        );
        project.getMaker().getTasksForCompilation().getPostActions().chain(this::packWeb);
    }

    public void cleanPack() {
        clean(); javaPlugin.pack();
    }

    public void run() {
        this.springbootPlugin.run();
    }

    private void packWeb() {
        JkLog.startTask("Packing web project");
        Path webDir = getBaseDir().resolve("../web");
        Path webDist = webDir.resolve("dist");
        Path staticDir = javaPlugin.getProject().getMaker().getOutLayout().getClassDir().resolve("static");
        JkProcess.of("npm", "run", "build").withWorkingDir(webDir).runSync();
        JkPathTree.of(webDist).copyTo(staticDir, StandardCopyOption.REPLACE_EXISTING);
        JkLog.endTask();
    }

}