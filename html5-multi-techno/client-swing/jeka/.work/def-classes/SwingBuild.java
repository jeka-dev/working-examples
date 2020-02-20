import dev.jeka.core.api.depmanagement.JkDependencySet;
import dev.jeka.core.api.java.project.JkJavaProject;
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
		JkJavaProject project = javaPlugin.getProject();
		Common.setup(project);
        project.getMaker().defineMainArtifactAsFatJar(true);
        project.getManifest().addMainClass("auto");

        // depends on core project
        project.addDependencies(JkDependencySet.of().and(coreBuild.javaPlugin.getProject()));
    }
	
	public static void main(String[] args) {
		JkInit.instanceOf(SwingBuild.class, args).javaPlugin.getProject().getMaker().makeAllArtifacts();
	}

}