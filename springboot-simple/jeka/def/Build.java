
import dev.jeka.core.tool.JkBean;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.JkInjectClasspath;
import dev.jeka.plugins.springboot.SpringbootJkBean;

@JkInjectClasspath("dev.jeka:springboot-plugin:0.9.20.RC2")
class Build extends JkBean {

    private final SpringbootJkBean springboot = getRuntime().getBean(SpringbootJkBean.class);

    public boolean runIT = true;

    @Override
    protected void init() {
        springboot.setSpringbootVersion("2.5.5");
        springboot.projectBean().getProject().simpleFacade()
            .configureCompileDeps(deps -> deps
                .and("org.springframework.boot:spring-boot-starter-web")
            )
            .configureTestDeps(deps -> deps
                .and("org.springframework.boot:spring-boot-starter-test")
                    .withLocalExclusions("org.junit.vintage:junit-vintage-engine")
            )
            .addTestExcludeFilterSuffixedBy("IT", !runIT);
    }

    public void cleanPack() {
        clean();
        springboot.projectBean().pack();
    }

    public static void main(String[] args) {
        JkInit.instanceOf(Build.class).cleanPack();
    }

}