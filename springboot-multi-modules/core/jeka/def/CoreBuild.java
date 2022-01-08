import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.tool.JkBean;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.JkInjectProject;
import dev.jeka.core.tool.builtins.project.ProjectJkBean;

class CoreBuild extends JkBean {
	
	@JkInjectProject("../utils")
	UtilsBuild utilsBuild;

	ProjectJkBean projectJkBean = getBean(ProjectJkBean.class).configure(this::configure);

	private void configure(JkProject project) {
		project.simpleFacade()
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