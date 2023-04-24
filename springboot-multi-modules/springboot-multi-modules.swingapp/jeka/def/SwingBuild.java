import dev.jeka.core.api.java.JkJavaProcess;
import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.tool.JkBean;
import dev.jeka.core.tool.JkInjectProject;
import dev.jeka.core.tool.builtins.ide.IntellijJkBean;
import dev.jeka.core.tool.builtins.project.ProjectJkBean;

/**
 * @formatter:off
 */
class SwingBuild extends JkBean {

	ProjectJkBean projectJkBean = getBean(ProjectJkBean.class).configure(this::configure);

	@JkInjectProject("../springboot-multi-modules.core")
	private CoreBuild coreBuild;

	{
		getBean(IntellijJkBean.class).useJekaDefinedInModule("wrapper-common");
	}

    private void configure(JkProject project) {
		project
			.packaging
				.manifest
					.addMainClass("swing.Main");
		project
			.compilation
				.configureDependencies(deps -> deps
					.and(coreBuild.projectJkBean.getProject().toDependency())
				);
		project
			.artifactProducer
				.putMainArtifact(project.packaging::createFatJar);
    }

    public void cleanPack() {
		cleanOutput(); projectJkBean.pack();
	}

    public void run() {
		JkJavaProcess.ofJavaJar(projectJkBean.getProject().artifactProducer.getMainArtifactPath(), null)
				.exec();
	}

}