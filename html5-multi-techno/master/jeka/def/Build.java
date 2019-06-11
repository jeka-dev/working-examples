import dev.jeka.core.api.file.JkPathTree;
import dev.jeka.core.api.java.project.JkJavaProjectMaker;
import dev.jeka.core.tool.JkCommands;
import dev.jeka.core.tool.JkImportProject;
import dev.jeka.core.tool.JkInit;

import java.nio.file.Path;

/**
 * @formatter:off
 */
class Build extends JkCommands {

	@JkImportProject("../web")
	WebBuild webBuild;

	@JkImportProject("../client-swing")
	SwingBuild swingBuild;

	Path distribFolder = getOutputDir().resolve("distrib");


	public void run() {
		clean();
		webBuild.javaPlugin.pack();
		swingBuild.javaPlugin.pack();
		copyJars();
	}

	private void copyJars() {
		JkJavaProjectMaker swingMaker = swingBuild.javaPlugin.getProject().getMaker();
		JkPathTree.of(distribFolder)
				.bring(webBuild.warPlugin.getWarFile())
				.bring(swingMaker.getArtifactPath(swingMaker.getMainArtifactId()));
	}

	public static void main(String[] args) {
		JkInit.instanceOf(Build.class, args).run();
	}

}