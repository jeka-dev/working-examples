import org.jerkar.api.file.JkPathTree;
import org.jerkar.api.java.project.JkJavaProjectMaker;
import org.jerkar.tool.JkImportRun;
import org.jerkar.tool.JkInit;
import org.jerkar.tool.JkRun;

import java.nio.file.Path;

/**
 * @formatter:off
 */
class Build extends JkRun {

	@JkImportRun("../web")
	WebBuild webBuild;

	@JkImportRun("../client-swing")
	SwingBuild swingBuild;

	Path distribFolder = getOutputDir().resolve("distrib");


	public void doDefault() {
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
		JkInit.instanceOf(Build.class, args).doDefault();
	}

}