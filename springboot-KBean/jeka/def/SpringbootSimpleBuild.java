
import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.tool.JkBean;
import dev.jeka.core.tool.JkInjectClasspath;
import dev.jeka.core.tool.builtins.ide.IntellijJkBean;
import dev.jeka.plugins.springboot.SpringbootJkBean;

@JkInjectClasspath("dev.jeka:springboot-plugin")
class SpringbootSimpleBuild extends JkBean {

    final SpringbootJkBean springboot = getBean(SpringbootJkBean.class);

    SpringbootSimpleBuild() {
        springboot.setSpringbootVersion("3.2.0");
        springboot.projectBean.lately(this::configure);
        getBean(IntellijJkBean.class).useJekaDefinedInModule("wrapper-common");
    }

    private void configure(JkProject project) {
        project.flatFacade()
                .addCompileDeps(
                        "org.springframework.boot:spring-boot-starter-web"
                )
                .addTestDeps(
                        "org.springframework.boot:spring-boot-starter-test"
                );
    }

}