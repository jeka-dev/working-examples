import dev.jeka.core.api.file.JkPathTree;
import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.api.system.JkLog;
import dev.jeka.core.api.tooling.intellij.JkIml;
import dev.jeka.core.tool.JkDoc;
import dev.jeka.core.tool.JkInject;
import dev.jeka.core.tool.JkPostInit;
import dev.jeka.core.tool.KBean;
import dev.jeka.core.tool.builtins.project.ProjectKBean;
import dev.jeka.core.tool.builtins.tooling.ide.IntellijKBean;

import java.nio.file.Path;

class MasterBuild extends KBean {

	@JkInject("springboot-multi-modules.springbootapp")
	private ProjectKBean springbootappProjectKBean;

	@JkInject("springboot-multi-modules.swingapp")
	private ProjectKBean swingProjectKBean;

	Path distribFolder = getOutputDir();

	@JkDoc("Clean and package distribution (server app + Swing client)")
	public void cleanPack() {
		cleanAll();
		JkLog.info("============== Packing Springboot ==================");
		springbootappProjectKBean.project.pack();
		JkLog.info("================= Packing Swing ====================");
		swingProjectKBean.project.pack();
		copyJars();
	}

	@JkDoc("Clean all sub-projects")
	public void cleanAll() {
		super.cleanOutput();
		this.getImportedKBeans().get(true).forEach(KBean::cleanOutput);
	}

	@JkPostInit
	private void postInit(IntellijKBean intellijKBean) {
		intellijKBean
				.addModule("springboot-multi-modules.springbootapp", JkIml.Scope.TEST)
				.addModule("springboot-multi-modules.swingapp", JkIml.Scope.TEST);
	}

	private void copyJars() {
		JkPathTree.of(distribFolder)
				.importFiles(springbootappProjectKBean.project.artifactLocator.getMainArtifactPath())
				.importFiles(swingProjectKBean.project.artifactLocator.getMainArtifactPath());
		JkLog.info("Distrib jar files copied in " + distribFolder);
	}

}