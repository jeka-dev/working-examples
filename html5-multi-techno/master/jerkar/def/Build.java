import org.jerkar.api.file.JkPathTree;
import org.jerkar.api.java.project.JkJavaProject;
import org.jerkar.tool.JkImportRun;
import org.jerkar.tool.JkInit;
import org.jerkar.tool.JkRun;
import org.jerkar.tool.builtins.java.JkPluginJava;

import java.io.File;
import java.nio.file.Path;

/**
 * @formatter:off
 */
class Build extends JkRun {

	JkPluginJava javaPlugin = getPlugin(JkPluginJava.class);

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
		JkJavaProject swingProject = swingBuild.javaPlugin.getProject();
		JkPathTree.of(distribFolder)
				.bring(webBuild.warPlugin.getWarFile())
				.bring(swingProject.getMaker().getArtifactPath(swingProject.getMaker().getMainArtifactId()));
	}

	public static void main(String[] args) {
		JkInit.instanceOf(Build.class, args).doDefault();
	}

}