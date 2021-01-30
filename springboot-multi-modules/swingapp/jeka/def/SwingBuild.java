import dev.jeka.core.api.depmanagement.JkDependencySet;
import dev.jeka.core.api.java.JkJavaProcess;
import dev.jeka.core.tool.JkClass;
import dev.jeka.core.tool.JkDefImport;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.builtins.java.JkPluginJava;

/**
 * @formatter:off
 */
class SwingBuild extends JkClass {

	JkPluginJava java = getPlugin(JkPluginJava.class);

	@JkDefImport("../core")
	CoreBuild coreBuild;

	@Override
    protected void setup() {
		java.getProject()
			.getConstruction()
				.getManifest()
					.addMainClass("swing.Main").__
				.getDependencyManagement()
					.addDependencies(JkDependencySet.of()
						.and(coreBuild.java.getProject().toDependency())).__.__
			.getPublication()
				.getArtifactProducer()
					.putMainArtifact(java.getProject().getConstruction()::createFatJar);
    }

    public void cleanPack() {
		clean(); java.pack();
	}

    public void run() {
		JkJavaProcess.of().runJarSync(java.getProject().getPublication().getArtifactProducer().getMainArtifactPath());
	}
	
	public static void main(String[] args) {
		JkInit.instanceOf(SwingBuild.class, args).run();
	}

}