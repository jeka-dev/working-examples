import dev.jeka.core.api.depmanagement.JkDependencySet;
import dev.jeka.core.api.java.JkJavaProcess;
import dev.jeka.core.api.java.project.JkJavaProject;
import dev.jeka.core.tool.JkCommandSet;
import dev.jeka.core.tool.JkDefImport;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.builtins.java.JkPluginJava;

/**
 * @formatter:off
 */
class SwingBuild extends JkCommandSet {

	JkPluginJava javaPlugin = getPlugin(JkPluginJava.class);

	@JkDefImport("../core")
	CoreBuild coreBuild;

	@Override
    protected void setup() {
		JkJavaProject project = javaPlugin.getProject();
		BuildCommon.setup(project);
        project.getMaker().defineMainArtifactAsFatJar(true);
        project.getManifest().addMainClass("swing.Main");
        project.addDependencies(JkDependencySet.of()
				.and(coreBuild.javaPlugin.getProject())
		);
    }

    public void cleanPack() {
		clean(); javaPlugin.pack();
	}

    public void run() {
		JkJavaProcess.of().runJarSync(javaPlugin.getProject().getMaker().getMainArtifactPath());
	}
	
	public static void main(String[] args) {
		JkInit.instanceOf(SwingBuild.class, args).run();
	}

}