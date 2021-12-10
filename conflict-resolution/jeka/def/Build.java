import dev.jeka.core.api.depmanagement.resolution.JkResolutionParameters;
import dev.jeka.core.api.java.JkJavaVersion;
import dev.jeka.core.api.java.testing.JkTestProcessor;
import dev.jeka.core.api.java.testing.JkTestSelection;;
import dev.jeka.core.tool.JkBean;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.builtins.project.ProjectJkBean;

class Build extends JkBean {

    final ProjectJkBean projectBean = getRuntime().getBean(ProjectJkBean.class);

    /*
     * Configures plugins to be bound to this command class. When this method is called, option
     * fields have already been injected from command line.
     */
    @Override
    protected void init() {
        projectBean.getProject().getConstruction()
                .setJvmTargetVersion(JkJavaVersion.V8)
                .getDependencyResolver()
                    .getParams()
                        .setConflictResolver(JkResolutionParameters.JkConflictResolver.STRICT)
                    .__
                .__
                .getCompilation()
                    .configureDependencies(deps -> deps
                        .and("com.google.api-client:google-api-client:1.30.7")
                            .withLocalExclusions("com.google.guava:guava")  // remove dependency to avoid conflict
                        .and("com.google.guava:guava:28.0-jre")
                        .and("org.codehaus.plexus:plexus-container-default:2.1.0")
                    )
                .__
                .getTesting()
                    .getTestSelection()
                        .addIncludeStandardPatterns()
                        .addIncludePatterns(JkTestSelection.IT_INCLUDE_PATTERN)
                    .__
                    .getTestProcessor()
                        .setForkingProcess(true)
                        .getEngineBehavior()
                            .setProgressDisplayer(JkTestProcessor.JkProgressOutputStyle.TREE);
    }

    public void cleanPack() {
        clean(); projectBean.pack();
    }

    @Override
    public void clean() {
        super.clean();
    }

    public static void main(String[] args) {
        JkInit.instanceOf(Build.class, args).cleanPack();
    }

}