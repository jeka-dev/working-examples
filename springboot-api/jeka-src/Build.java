import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.api.testing.JkTestProcessor;
import dev.jeka.core.tool.*;
import dev.jeka.core.tool.builtins.project.ProjectKBean;
import dev.jeka.plugins.jacoco.JacocoKBean;
import dev.jeka.plugins.jacoco.JkJacoco;
import dev.jeka.plugins.sonarqube.JkSonarqube;
import dev.jeka.plugins.sonarqube.SonarqubeKBean;
import dev.jeka.plugins.springboot.JkSpringbootProject;
import dev.jeka.plugins.springboot.SpringbootKBean;

@JkDep("dev.jeka:jacoco-plugin")      // These plugins are fetched from a Maven repo (Maven Central)
@JkDep("dev.jeka:springboot-plugin")  // Versions are not specified as JeKA will pick the right one
@JkDep("dev.jeka:sonarqube-plugin")   // according its running version.
class Build extends KBean {

    @JkDoc("Run Sonarqube")
    public void sonar() {
        load(SonarqubeKBean.class).run();
    }

    @JkPreInit
    private static void preInit(ProjectKBean projectKBean) {
        projectKBean.compilation.compilerOptions = "-Xlint:-options";
        projectKBean.tests.progress = JkTestProcessor.JkProgressStyle.STEP;
        projectKBean.gitVersioning.enable = true;
    }

    @JkPostInit(required = true)
    private void postInit(ProjectKBean projectKBean) {
        JkProject project = projectKBean.project;
        project.testing.compilation.addJavaCompilerOptions("-Xlint:-options");
        project.flatFacade.dependencies.compile
                .add("org.springframework.boot:spring-boot-dependencies::pom:3.4.2")
                .add("org.springframework.boot:spring-boot-starter-web")
                .add("org.springframework.boot:spring-boot-starter-logging");
        project.flatFacade.addCompileOnlyDeps(
                "org.projectlombok:lombok:1.18.30");
        project.flatFacade.dependencies.test
                .add( "org.springframework.boot:spring-boot-starter-test");
    }

    @JkPostInit(required = true)
    private void postInit(SpringbootKBean springbootKBean) {}

}