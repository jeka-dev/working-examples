import dev.jeka.core.api.file.JkPathTree;
import dev.jeka.core.api.java.project.JkJavaProjectMaker;
import dev.jeka.core.tool.JkCommands;
import dev.jeka.core.tool.JkImportProject;
import dev.jeka.core.tool.JkInit;

import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * @formatter:off
 */
class MasterBuild extends JkCommands {

	@JkImportProject("../web")
	WebBuild webBuild;

	@JkImportProject("../client-swing")
	SwingBuild swingBuild;

	Path distribFolder = getOutputDir().resolve("distrib");

	@Override
	protected void setup() {
		webBuild.embbedHtml5 = false;
	}

	public void run() {
		webBuild.cleanPack();
		swingBuild.javaPlugin.pack();
		copyJars();
	}

	private void copyJars() {
		JkJavaProjectMaker swingMaker = swingBuild.javaPlugin.getProject().getMaker();
		JkPathTree.of(distribFolder)
				.importFiles(webBuild.warPlugin.getWarFile(), StandardCopyOption.REPLACE_EXISTING)
				.importFiles(swingMaker.getArtifactPath(swingMaker.getMainArtifactId()), StandardCopyOption.REPLACE_EXISTING);
	}

	public static void main(String[] args) {
		JkInit.instanceOf(MasterBuild.class, args).run();
	}

}