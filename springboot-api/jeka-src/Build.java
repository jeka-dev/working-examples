import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.api.testing.JkTestProcessor;
import dev.jeka.core.tool.JkDoc;
import dev.jeka.core.tool.JkInjectClasspath;
import dev.jeka.core.tool.KBean;
import dev.jeka.core.tool.builtins.project.ProjectKBean;
import dev.jeka.plugins.jacoco.JkJacoco;
import dev.jeka.plugins.sonarqube.JkSonarqube;
import dev.jeka.plugins.springboot.JkSpringbootProject;

@JkInjectClasspath("dev.jeka:jacoco-plugin")      // These plugins are fetched from a Maven repo (Maven Central)
@JkInjectClasspath("dev.jeka:springboot-plugin")  // Versions are not specified as JeKA will pick the right one
@JkInjectClasspath("dev.jeka:sonarqube-plugin")   // according its running version.
class Build extends KBean {

    final JkProject project = load(ProjectKBean.class).project;

    @JkDoc("Launch sonarQube analysis.")
    public void sonarqube() {
        JkSonarqube.ofVersion("5.0.1.3006")
                .setProperties(getRunbase().getProperties())  // Take Sonar properties from local.properties and System.getProperties()
                .configureFor(project)
                .run();
    }

    @Override
    protected void init() {
        JkSpringbootProject.of(project)
                .includeParentBom("3.2.0")
                .configure();
        JkJacoco.ofVersion("0.8.11")
                .configureAndApplyTo(project);
        project.compilation.dependencies
                .add("org.springframework.boot:spring-boot-starter-web")
                .add("org.springframework.boot:spring-boot-starter-logging");
        project.flatFacade.addCompileOnlyDeps("org.projectlombok:lombok:1.18.30");
        project.testing.compilation.dependencies
                .add( "org.springframework.boot:spring-boot-starter-test");
        project.flatFacade.setVersionFromGitTag();  // Infer version from Git
        project.compilation.addJavaCompilerOptions("-Xlint:-options");
        project.testing.compilation.addJavaCompilerOptions("-Xlint:-options");
        project.testing.testProcessor.engineBehavior
                .setProgressDisplayer(JkTestProcessor.JkProgressOutputStyle.BAR);
    }

}