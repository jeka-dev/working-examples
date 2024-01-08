import dev.jeka.core.api.file.JkPathTree;
import dev.jeka.core.api.system.JkLog;
import dev.jeka.core.tool.JkDoc;
import dev.jeka.core.tool.JkInjectProject;
import dev.jeka.core.tool.KBean;

import java.nio.file.Path;

class MasterBuild extends KBean {

	@JkInjectProject("springboot-multi-modules.springbootapp")
	private SpringbootBuild springbootBuild;

	@JkInjectProject("springboot-multi-modules.swingapp")
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
		swingBuild.project.prepareRunJar(false).execAsync();
		springbootBuild.project.prepareRunJar(false).setInheritIO(true).exec();
	}

	private void copyJars() {
		JkPathTree.of(distribFolder)
				.importFiles(springbootBuild.project.artifactLocator.getMainArtifactPath())
				.importFiles(swingBuild.project.artifactLocator.getMainArtifactPath());
		JkLog.info("Distrib jar files copied in " + distribFolder);
	}

}