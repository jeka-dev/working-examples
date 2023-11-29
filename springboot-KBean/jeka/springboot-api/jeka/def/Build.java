import dev.jeka.core.api.project.JkIdeSupport;
import dev.jeka.core.api.project.JkIdeSupportSupplier;
import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.tool.JkBean;
import dev.jeka.core.tool.JkDoc;
import dev.jeka.core.tool.JkInjectClasspath;
import dev.jeka.plugins.jacoco.JkJacoco;
import dev.jeka.plugins.sonarqube.JkSonarqube;
import dev.jeka.plugins.springboot.JkSpringboot;

@JkInjectClasspath("dev.jeka:springboot-plugin")
@JkInjectClasspath("dev.jeka:jacoco-plugin")
@JkInjectClasspath("dev.jeka:sonarqube-plugin")
class Build extends JkBean implements JkIdeSupportSupplier {

    @JkDoc("Clean output directory then compile, test, create jar, and launch sSonarQube analysis")
    public void cleanPack() {
        project().clean().pack();
        JkSonarqube.ofVersion("5.0.1.3006")
                .setProperties(getRuntime().getProperties())
                .configureFor(project())
                .run();
    }

    @JkDoc("Runs the built bootable jar")
    public void runJar() {
        project().runMainJar(false, "", "");
    }

    @JkDoc("Display project info on console")
    public void info() {
        System.out.println(project().getInfo());
    }

    @JkDoc("Display dependency tree on console")
    public void depTree() {
        project().displayDependencyTree();
    }

    @Override
    public JkIdeSupport getJavaIdeSupport() {
        return project().getJavaIdeSupport();
    }

    private JkProject project() {
        JkProject project = JkProject.of();
        JkSpringboot.of()
                .setSpringbootVersion("3.2.0")
                .configure(project);
        JkJacoco.ofVersion("0.8.11")
                .configureForAndApplyTo(project);
        project.flatFacade()
                .addCompileDeps(
                        "org.springframework.boot:spring-boot-starter-web"
                )
                .addCompileOnlyDeps(
                        "org.projectlombok:lombok:1.18.30"
                )
                .addTestDeps(
                        "org.springframework.boot:spring-boot-starter-test"
                )
                .setPublishedVersionFromGitTag();  // Infer version from Git
        return project;
    }

}