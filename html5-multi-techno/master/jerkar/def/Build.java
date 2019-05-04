import java.io.File;

import org.jerkar.api.file.JkFileTree;
import org.jerkar.api.file.JkZipper;
import org.jerkar.tool.JkBuildDependencySupport;
import org.jerkar.tool.JkInit;
import org.jerkar.tool.JkProject;
import org.jerkar.tool.builtins.javabuild.JkJavaBuild;

/**
 * @formatter:off
 */
class Build extends JkBuildDependencySupport {

	@JkProject("../web")
	WebBuild webBuild;

	@JkProject("../client-swing")
	SwingBuild swingBuild;

	File distribFolder = ouputDir("distrib");

	@Override
	public void doDefault() {
		clean();
		this.slaves().invokeDoDefaultMethodOnAll();
		distribFolder.mkdirs();
		copyJars();
	}

	private void copyJars() {
		JkFileTree.of(distribFolder).importFiles(
				webBuild.pluginWar.warFile(),
				swingBuild.packer().fatJarFile(),
				swingBuild.coreBuild.packer().jarFile(),
				webBuild.html5Folder);
	}

	public static void main(String[] args) {
		JkInit.instanceOf(Build.class, args).doDefault();
	}

}