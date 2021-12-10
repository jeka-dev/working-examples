import dev.jeka.core.api.java.JkJavaProcess;
import dev.jeka.core.tool.JkBean;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.JkInjectProject;
import dev.jeka.core.tool.builtins.project.ProjectJkBean;

/**
 * @formatter:off
 */
public class SwingBuild extends JkBean {

	ProjectJkBean projectJkBean = getRuntime().getBean(ProjectJkBean.class);

	@JkInjectProject("../core")
	CoreBuild coreBuild;

	@Override
    protected void init() {
		projectJkBean.getProject()
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
				.putMainArtifact(projectJkBean.getProject().getConstruction()::createFatJar);
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