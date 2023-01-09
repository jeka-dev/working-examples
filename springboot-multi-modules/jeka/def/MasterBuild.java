import dev.jeka.core.api.file.JkPathTree;
import dev.jeka.core.api.system.JkLog;
import dev.jeka.core.tool.JkBean;
import dev.jeka.core.tool.JkInjectProject;
import dev.jeka.core.tool.builtins.project.ProjectJkBean;

import java.nio.file.Path;

/**
 * @formatter:off
 */
class MasterBuild extends JkBean {

	@JkInjectProject("springboot-multi-modules.springbootapp")
	private SpringbootBuild springbootBuild;

	@JkInjectProject("springboot-multi-modules.swingapp")
	private SwingBuild swingBuild;

	Path distribFolder = getOutputDir();

	public void build() {
		clean();
		JkLog.info("Packing springboot");
		springbootBuild.springboot.projectBean.pack();
		JkLog.info("Packing swing");
		swingBuild.projectJkBean.pack();
		copyJars();
	}

	public void clean() {
		super.cleanOutput();
		this.getImportedBeans().get(true).stream()
				.map(JkBean::getRuntime)
				.distinct()
				.map(runtime -> runtime.getBean(ProjectJkBean.class))
				.map(ProjectJkBean.class::cast)
				.forEach(ProjectJkBean::clean);
	}

	private void copyJars() {
		JkPathTree.of(distribFolder)
				.importFiles(springbootBuild.springboot.projectBean.getProject().artifactProducer.getMainArtifactPath())
				.importFiles(swingBuild.projectJkBean.getProject().artifactProducer.getMainArtifactPath());
		JkLog.info("Distrib jar files copied in " + distribFolder);
	}



}