import java.io.File;

import org.jerkar.api.file.JkFileTree;
import org.jerkar.api.file.JkZipper;
import org.jerkar.tool.JkBuildDependencySupport;
import org.jerkar.tool.JkInit;
import org.jerkar.tool.JkProject;
import org.jerkar.tool.builtins.javabuild.JkJavaBuild;

class Build extends JkBuildDependencySupport {
	
	@JkProject("../web")
	WebBuild webBuild;
	
	@JkProject("../client-swing")
	SwingBuild fooBuild;
	
	File distribFolder = ouputDir("distrib");
	
	File fatJar = new File(distribFolder, "example-fat.jar");
	
	@Override
	public void doDefault() {
		this.slaves().invokeDoDefaultMethodOnAll();
		distribFolder.mkdirs();
		fatJar();
		copyJars();
	}
	
	private void fatJar() {
		JkZipper.of(
				webBuild.packer().jarFile(), 
				fooBuild.packer().jarFile())
				.merge(webBuild.depsFor(JkJavaBuild.RUNTIME))
				.merge(fooBuild.depsFor(JkJavaBuild.RUNTIME)).to(fatJar);
	}
	
	private void copyJars() {
		JkFileTree.of(distribFolder).importFiles(
				webBuild.packer().jarFile(), 
				fooBuild.packer().jarFile(),
				fooBuild.coreBuild.packer().jarFile(),
				webBuild.html5Folder,
				webBuild.coreBuild.packer().jarFile());
	}
	
	public static void main(String[] args) {
		JkInit.instanceOf(Build.class, args).doDefault();
	}
	
}