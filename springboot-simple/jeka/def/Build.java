import dev.jeka.core.tool.JkClass;
import dev.jeka.core.tool.JkDefClasspath;
import dev.jeka.plugins.springboot.JkPluginSpringboot;

@JkDefClasspath("dev.jeka:springboot-plugin:3.0.0.RC10")
class Build extends JkClass {

    private final JkPluginSpringboot springboot = getPlugin(JkPluginSpringboot.class);

    public boolean runIT = true;

    @Override
    protected void setup() {
        springboot.setSpringbootVersion("2.5.5");
        springboot.javaPlugin().getProject().simpleFacade()
            .setCompileDependencies(deps -> deps
                .and("org.springframework.boot:spring-boot-starter-web")
            )
            .setTestDependencies(deps -> deps
                .and("org.springframework.boot:spring-boot-starter-test")
                    .withLocalExclusions("org.junit.vintage:junit-vintage-engine")
            )
            .addTestExcludeFilterSuffixedBy("IT", !runIT);
    }

    public void cleanPack() {
        clean();
        springboot.javaPlugin().pack();
    }

}