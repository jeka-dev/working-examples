
import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.tool.JkBean;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.JkInjectClasspath;
import dev.jeka.core.tool.builtins.ide.IntellijJkBean;
import dev.jeka.plugins.springboot.JkSpringModules;
import dev.jeka.plugins.springboot.SpringbootJkBean;

@JkInjectClasspath("dev.jeka:springboot-plugin")
class Build extends JkBean {

    final SpringbootJkBean springboot = getBean(SpringbootJkBean.class);

    public boolean runIT = true;

    Build() {
        springboot.setSpringbootVersion("2.5.5");
        springboot.projectBean().configure(this::configure);
        getBean(IntellijJkBean.class).useJekaDefinedInModule("wrapper-common");
    }

    private void configure(JkProject project) {
        project.simpleFacade()
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
        cleanOutput();
        springboot.projectBean().pack();
    }

    public static void main(String[] args) {
        JkInit.instanceOf(Build.class).cleanPack();
    }

}