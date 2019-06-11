import dev.jeka.core.api.depmanagement.JkDependencySet;
import dev.jeka.core.tool.JkCommands;
import dev.jeka.core.tool.JkImportProject;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.builtins.java.JkPluginJava;


/**
 * @formatter:off
 */
class SwingBuild extends JkCommands {

	JkPluginJava javaPlugin = getPlugin(JkPluginJava.class);

	@JkImportProject("../core")
	CoreBuild coreBuild;


	@Override
    protected void setup() {
        javaPlugin.getProject().getMaker().defineMainArtifactAsFatJar(true);
        javaPlugin.getProject().getManifest().addMainClass("auto");

        // depends on core project
        javaPlugin.getProject().addDependencies(JkDependencySet.of().and(coreBuild.javaPlugin.getProject()));
    }
	
	public static void main(String[] args) {
		JkInit.instanceOf(SwingBuild.class, args).javaPlugin.getProject().getMaker().makeAllArtifacts();
	}

}