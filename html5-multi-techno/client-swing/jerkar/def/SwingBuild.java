import org.jerkar.api.depmanagement.JkDependencySet;
import org.jerkar.tool.JkImportProject;
import org.jerkar.tool.JkInit;
import org.jerkar.tool.JkRun;
import org.jerkar.tool.builtins.java.JkPluginJava;

/**
 * @formatter:off
 */
class SwingBuild extends JkRun {

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