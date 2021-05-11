import dev.jeka.core.api.depmanagement.resolution.JkResolutionParameters;
import dev.jeka.core.api.java.JkJavaVersion;
import dev.jeka.core.api.java.testing.JkTestProcessor;
import dev.jeka.core.api.java.testing.JkTestSelection;
import dev.jeka.core.tool.JkClass;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.builtins.java.JkPluginJava;

class Build extends JkClass {

    final JkPluginJava java = getPlugin(JkPluginJava.class);

    /*
     * Configures plugins to be bound to this command class. When this method is called, option
     * fields have already been injected from command line.
     */
    @Override
    protected void setup() {
        java.getProject().getConstruction()
                .getDependencyResolver()
                    .getParams()
                        .setConflictResolver(JkResolutionParameters.JkConflictResolver.STRICT).__.__
                .getCompilation()
                    .setJavaVersion(JkJavaVersion.V8)
                    .setDependencies(deps -> deps
                        .and("com.google.api-client:google-api-client:1.30.7")
                            .withLocalExclusions("com.google.guava:guava")  // remove dependency to avoid conflict
                        .and("com.google.guava:guava:28.0-jre")
                        .and("org.codehaus.plexus:plexus-container-default:2.1.0")
                    ).__
                .getTesting()
                    .getTestSelection()
                        .addIncludeStandardPatterns()
                        .addIncludePatterns(JkTestSelection.IT_INCLUDE_PATTERN).__
                    .getTestProcessor()
                        .setForkingProcess(true)
                        .getEngineBehavior()
                            .setProgressDisplayer(JkTestProcessor.JkProgressOutputStyle.TREE);
    }

    public void cleanPack() {
        clean(); java.pack();
    }

    @Override
    public void clean() {
        super.clean();
    }

    public static void main(String[] args) {
        JkInit.instanceOf(Build.class, args).cleanPack();
    }

}