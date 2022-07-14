import dev.jeka.core.api.file.JkPathTree;
import dev.jeka.core.api.system.JkLog;
import dev.jeka.core.tool.JkBean;
import dev.jeka.core.tool.JkInit;
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
		springbootBuild.springboot.projectBean().pack();
		swingBuild.projectJkBean.pack();
		copyJars();
	}

	@Override
	public void clean() {
		super.clean();
		this.getImportedJkBeans().get(true).stream()
				.map(JkBean::getRuntime)
				.distinct()
				.map(runtime -> runtime.getBean(ProjectJkBean.class))
				.forEach(JkBean::clean);
	}

	private void copyJars() {
		JkPathTree.of(distribFolder)
				.importFiles(springbootBuild.springboot.projectBean().getProject().getArtifactProducer().getMainArtifactPath())
				.importFiles(swingBuild.projectJkBean.getProject().getArtifactProducer().getMainArtifactPath());
		JkLog.info("Distrib jar files copied in " + distribFolder);
	}

	public static void main(String[] args) {
		JkInit.instanceOf(MasterBuild.class, args).build();
	}

}