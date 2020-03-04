import dev.jeka.core.api.file.JkPathTree;
import dev.jeka.core.api.java.project.JkJavaProjectMaker;
import dev.jeka.core.api.system.JkLog;
import dev.jeka.core.tool.JkCommandSet;
import dev.jeka.core.tool.JkDefImport;
import dev.jeka.core.tool.JkInit;

import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * @formatter:off
 */
class MasterBuild extends JkCommandSet {

	@JkDefImport("../springbootapp")
	SpringbootBuild springbootBuild;

	@JkDefImport("../swingapp")
	SwingBuild swingBuild;

	Path distribFolder = getOutputDir();

	@Override
	protected void setup() {
		//springbootBuild.embbedHtml5 = false;
	}

	public void run() {
		deepClean();
		springbootBuild.cleanPack();
		swingBuild.javaPlugin.pack();
		copyJars();
	}

	public void deepClean() {
		springbootBuild.deepClean();
		swingBuild.deepClean();
	}

	private void copyJars() {
		JkJavaProjectMaker swingMaker = swingBuild.javaPlugin.getProject().getMaker();
		JkPathTree.of(distribFolder)
				.importFiles(springbootBuild.javaPlugin.getProject().getMaker().getMainArtifactPath(),
						StandardCopyOption.REPLACE_EXISTING)
				.importFiles(swingMaker.getArtifactPath(swingMaker.getMainArtifactId()), StandardCopyOption.REPLACE_EXISTING);
		JkLog.info("Diste" +
				"Distrib jar files copied in " + distribFolder);
	}

	public static void main(String[] args) {
		JkInit.instanceOf(MasterBuild.class, args).run();
	}

}