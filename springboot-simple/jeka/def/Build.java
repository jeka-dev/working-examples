import dev.jeka.core.api.depmanagement.JkDependencySet;
import dev.jeka.core.api.java.testing.JkTestSelection;
import dev.jeka.core.tool.JkCommandSet;
import dev.jeka.core.tool.JkDefClasspath;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.builtins.java.JkPluginJava;
import dev.jeka.plugins.springboot.JkPluginSpringboot;

import static dev.jeka.core.api.depmanagement.JkScope.TEST;

@JkDefClasspath("dev.jeka:springboot-plugin:2.4.0.RC7")
class Build extends JkCommandSet {

    private final JkPluginJava java = getPlugin(JkPluginJava.class);

    private final JkPluginSpringboot springboot = getPlugin(JkPluginSpringboot.class);

    public boolean runIt = true;

    @Override
    protected void setup() {
        springboot.setSpringbootVersion("2.2.6.RELEASE");
        java.getProject().getJarProduction()
            .getDependencyManagement()
                .addDependencies(JkDependencySet.of()
                    .and("org.springframework.boot:spring-boot-starter-web")
                    .and("org.springframework.boot:spring-boot-starter-test", TEST)
                        .withLocalExclusions("org.junit.vintage:junit-vintage-engine")
                    ).__
            .getTesting()
                .getTestSelection()
                    .addIncludeStandardPatterns()
                    .addIncludePatternsIf(runIt, JkTestSelection.IT_INCLUDE_PATTERN);
    }

    public void cleanPack() {
        clean(); springboot.createBootJar();
    }

    // Clean, compile, test and generate springboot application jar
    public static void main(String[] args) {
        JkInit.instanceOf(Build.class, args).cleanPack();
    }

}