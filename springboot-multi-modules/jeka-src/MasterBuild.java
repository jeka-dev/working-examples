import dev.jeka.core.api.file.JkPathTree;
import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.api.system.JkLog;
import dev.jeka.core.tool.JkDoc;
import dev.jeka.core.tool.JkInjectRunbase;
import dev.jeka.core.tool.KBean;

import java.nio.file.Path;

class MasterBuild extends KBean {

	@JkInjectRunbase("springboot-multi-modules.springbootapp")
	private SpringbootBuild springbootBuild;

	@JkInjectRunbase("springboot-multi-modules.swingapp")
	private SwingBuild swingBuild;

	Path distribFolder = getOutputDir();

	@JkDoc("Clean and package distribution (server app + Swing client)")
	public void cleanPack() {
		cleanAll();
		JkLog.info("============== Packing Springboot ==================");
		springbootBuild.project.pack();
		JkLog.info("================= Packing Swing ====================");
		swingBuild.project.pack();
		copyJars();
	}

	@JkDoc("Clean all sub-projects")
	public void cleanAll() {
		super.cleanOutput();
		this.getImportedKBeans().get(true).forEach(KBean::cleanOutput);
	}

	@JkDoc("Launch both server and Swing applications.")
	public void run() {
		swingBuild.project.prepareRunJar(JkProject.RuntimeDeps.EXCLUDE).execAsync();
		springbootBuild.project.prepareRunJar(JkProject.RuntimeDeps.EXCLUDE).setInheritIO(true).exec();
	}

	private void copyJars() {
		JkPathTree.of(distribFolder)
				.importFiles(springbootBuild.project.artifactLocator.getMainArtifactPath())
				.importFiles(swingBuild.project.artifactLocator.getMainArtifactPath());
		JkLog.info("Distrib jar files copied in " + distribFolder);
	}

}