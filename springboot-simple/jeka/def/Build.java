import dev.jeka.core.api.depmanagement.JkDependencySet;
import dev.jeka.core.tool.JkClass;
import dev.jeka.core.tool.JkDefClasspath;
import dev.jeka.plugins.springboot.JkPluginSpringboot;

import static dev.jeka.core.api.depmanagement.JkScope.TEST;

@JkDefClasspath("dev.jeka:springboot-plugin:3.0.0.RC6")
class Build extends JkClass {

    private final JkPluginSpringboot springboot = getPlugin(JkPluginSpringboot.class);

    public boolean runIT = true;

    @Override
    protected void setup() {
        springboot.setSpringbootVersion("2.2.6.RELEASE");
        springboot.javaPlugin().getProject().simpleFacade()
            .addDependencies(JkDependencySet.of()
                .and("org.springframework.boot:spring-boot-starter-web")
                .and("org.springframework.boot:spring-boot-starter-test", TEST)
                    .withLocalExclusions("org.junit.vintage:junit-vintage-engine")
                )
            .addTestExcludeFilterSuffixedBy("IT", !runIT);
    }

}