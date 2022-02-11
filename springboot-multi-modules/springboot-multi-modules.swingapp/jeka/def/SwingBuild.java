import dev.jeka.core.api.java.JkJavaProcess;
import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.tool.JkBean;
import dev.jeka.core.tool.JkInit;
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
			.getConstruction()
				.getManifest()
					.addMainClass("swing.Main")
				.__
				.getCompilation()
					.configureDependencies(deps -> deps
						.and(coreBuild.projectJkBean.getProject().toDependency()))
					.__
				.__
			.getArtifactProducer()
				.putMainArtifact(project.getConstruction()::createFatJar);
    }

    public void cleanPack() {
		clean(); projectJkBean.pack();
	}

    public void run() {
		JkJavaProcess.ofJavaJar(projectJkBean.getProject().getArtifactProducer().getMainArtifactPath(), null)
				.exec();
	}
	
	public static void main(String[] args) {
		JkInit.instanceOf(SwingBuild.class, args).run();
	}

}