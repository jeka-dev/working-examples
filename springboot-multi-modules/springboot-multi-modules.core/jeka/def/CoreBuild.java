import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.tool.JkBean;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.JkInjectProject;
import dev.jeka.core.tool.builtins.project.ProjectJkBean;

class CoreBuild extends JkBean {
	
	@JkInjectProject("../springboot-multi-modules.utils")
	private UtilsBuild utilsBuild;

	ProjectJkBean projectJkBean = getBean(ProjectJkBean.class).lately(this::configure);

	private void configure(JkProject project) {
		project.flatFacade()
				.applyOnProject(BuildCommon::setup)
				.configureCompileDependencies(deps -> deps
						.and(utilsBuild.projectJkBean.getProject().toDependency()));
	}

	public void cleanPack() {
		cleanOutput(); projectJkBean.pack();
	}

	public static void main(String[] args) {
		CoreBuild coreBuild = JkInit.instanceOf(CoreBuild.class);
		coreBuild.cleanPack();
		System.out.println("Hello Word");
	}

}