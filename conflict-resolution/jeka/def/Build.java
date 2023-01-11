import dev.jeka.core.api.depmanagement.resolution.JkResolutionParameters;
import dev.jeka.core.api.java.JkJavaVersion;
import dev.jeka.core.api.testing.JkTestProcessor;
import dev.jeka.core.api.testing.JkTestSelection;
import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.tool.JkBean;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.builtins.ide.IntellijJkBean;
import dev.jeka.core.tool.builtins.project.ProjectJkBean;

class Build extends JkBean {

    ProjectJkBean projectBean = getBean(ProjectJkBean.class).configure(this::configure);

    Build() {
        getBean(IntellijJkBean.class).useJekaDefinedInModule("wrapper-common");
    }

    /*
     * Configures plugins to be bound to this command class. When this method is called, option
     * fields have already been injected from command line.
     */
    private void configure(JkProject project) {
        project
            .setJvmTargetVersion(JkJavaVersion.V8)
            .dependencyResolver
                .getDefaultParams()
                    .setConflictResolver(JkResolutionParameters.JkConflictResolver.STRICT);
        project
            .prodCompilation
                .configureDependencies(deps -> deps
                    .and("com.google.api-client:google-api-client:1.30.7")
                        .withLocalExclusions("com.google.guava:guava")  // remove dependency to avoid conflict
                    .and("com.google.guava:guava:28.0-jre")
                    .and("org.codehaus.plexus:plexus-container-default:2.1.0")
                );
        project
            .testing
                .testSelection
                    .addIncludeStandardPatterns()
                    .addIncludePatterns(JkTestSelection.IT_INCLUDE_PATTERN);
        project
            .testing
                .testProcessor
                    .setForkingProcess(true)
                    .engineBehavior
                        .setProgressDisplayer(JkTestProcessor.JkProgressOutputStyle.TREE);
        project.includeJavadocAndSources(false, false);
    }

    public void cleanPack() {
        projectBean.clean(); projectBean.pack();
    }


}