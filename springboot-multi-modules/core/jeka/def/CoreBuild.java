import dev.jeka.core.tool.JkBean;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.JkInjectProject;
import dev.jeka.core.tool.builtins.project.ProjectJkBean;

class CoreBuild extends JkBean {
	
	@JkInjectProject("../utils")
	UtilsBuild utilsBuild;

	ProjectJkBean projectJkBean = getRuntime().getBean(ProjectJkBean.class);

	@Override
	protected void init() {
		projectJkBean.getProject().simpleFacade()
				.applyOnProject(BuildCommon::setup)
				.configureCompileDeps(deps -> deps
						.and(utilsBuild.projectJkBean.getProject().toDependency()));
	}

	public void cleanPack() {
		clean(); projectJkBean.pack();
	}

	public static void main(String[] args) {
		CoreBuild coreBuild = JkInit.instanceOf(CoreBuild.class);
		coreBuild.cleanPack();
		System.out.println("Hello Word");
	}

}