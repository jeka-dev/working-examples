import dev.jeka.core.api.depmanagement.JkCoordinateDependency;
import dev.jeka.core.api.depmanagement.JkDependencySet;
import dev.jeka.core.api.depmanagement.JkDependencySet.Hint;
import dev.jeka.core.api.depmanagement.JkPopularLibs;
import dev.jeka.core.api.depmanagement.JkTransitivity;
import dev.jeka.core.api.java.JkJavaVersion;
import dev.jeka.core.api.project.JkCompileLayout;
import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.api.project.JkProjectPackaging;
import dev.jeka.core.tool.JkBean;
import dev.jeka.core.tool.JkDoc;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.builtins.ide.IntellijJkBean;
import dev.jeka.core.tool.builtins.project.ProjectJkBean;

/**
 * This build illustrate partially what is doable to configure through the project flat facade.
 */
class ClassicBuild extends JkBean {

    @JkDoc("Tell if the Integration Tests should be run.")
    public boolean runIT = true;

    ProjectJkBean projectJkBean = getBean(ProjectJkBean.class).lately(this::configure);

    ClassicBuild() {
        getBean(IntellijJkBean.class).useJekaDefinedInModule("wrapper-common");
    }

    private void configure(JkProject project) {
        project.flatFacade()
                .setJvmTargetVersion(JkJavaVersion.V8)
                .includeJavadocAndSources(false, true)
                .setLayoutStyle(JkCompileLayout.Style.SIMPLE) // don't use maven layout
                .mixResourcesAndSources()  // java sources and resources are located in same folder
                .setMainArtifactJarType(JkProjectPackaging.JarType.FAT)
                .configureCompileDependencies(deps -> deps
                        .and("com.google.guava:guava:22.0").withLocalExclusions(
                                    "com.google.j2objc:j2objc-annotations",
                                    "com.google.code.findbugs")

                        .and("com.github.djeang:vincer-dom:1.4.0")
                        .and("org.projectlombok:lombok:1.18.30")

                        .andBom("com.fasterxml.jackson:jackson-bom:2.16.0")
                        .and("com.fasterxml.jackson.core:jackson-core")
                        .and("com.fasterxml.jackson.core:jackson-databind")
                )
                .configureRuntimeDependencies(deps -> deps
                        .and(Hint.before(JkCoordinateDependency.of("com.github.djeang:vincer-dom")),
                                "commons-codec:commons-codec:1.16.0")
                        .minus("org.projectlombok:lombok")
                        .withMoving(Hint.first(), "com.fasterxml.jackson.core:jackson-databind")

                )
                .configureTestDependencies(deps -> deps
                        .and(JkPopularLibs.JUNIT_5.toCoordinate("5.8.1"))
                )
                .addTestIncludeFilterSuffixedBy("IT", runIT)
                .setPublishedModuleId("org.jerkar:examples-java-flat-facade")
                .setPublishedVersionFromGitTag();

        // Here we are modifying the dependencies mentioned in the published POM
        project.publication.maven
                .configureDependencies(deps -> deps
                        .minus("com.fasterxml.jackson.core")
                        .withTransitivity("com.github.djeang:vincer-dom", JkTransitivity.RUNTIME)
                );

    }

    public void cleanPack() {
        cleanOutput(); projectJkBean.pack();
    }

}

