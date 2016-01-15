import java.io.File;

import org.jerkar.api.file.JkFileTree;
import org.jerkar.api.file.JkZipper;
import org.jerkar.tool.JkBuildDependencySupport;
import org.jerkar.tool.JkInit;
import org.jerkar.tool.JkProject;
import org.jerkar.tool.builtins.javabuild.JkJavaBuild;

class Build extends JkBuildDependencySupport {
	
	@JkProject("../bar")
	BarBuild barBuild;
	
	@JkProject("../foo")
	FooBuild fooBuild;
	
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
				barBuild.packer().jarFile(), 
				fooBuild.packer().jarFile())
				.merge(barBuild.depsFor(JkJavaBuild.RUNTIME))
				.merge(fooBuild.depsFor(JkJavaBuild.RUNTIME)).to(fatJar);
	}
	
	private void copyJars() {
		JkFileTree.of(distribFolder).importFiles(
				barBuild.packer().jarFile(), 
				fooBuild.packer().jarFile(),
				fooBuild.coreBuild.packer().jarFile(),
				barBuild.jarFromLegacyProject,
				barBuild.coreBuild.packer().jarFile());
	}
	
	public static void main(String[] args) {
		JkInit.instanceOf(Build.class, args).doDefault();
	}
	
}