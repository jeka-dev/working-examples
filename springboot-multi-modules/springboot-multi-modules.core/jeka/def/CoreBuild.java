import dev.jeka.core.api.project.JkIdeSupport;
import dev.jeka.core.api.project.JkIdeSupportSupplier;
import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.tool.JkInjectProject;
import dev.jeka.core.tool.KBean;
import dev.jeka.core.tool.builtins.project.ProjectKBean;

class CoreBuild extends KBean {
	
	@JkInjectProject("../springboot-multi-modules.utils")
	private UtilsBuild utilsBuild;

	private final ProjectKBean projectKBean = load(ProjectKBean.class);

	final JkProject project = projectKBean.project;

	@Override
	protected void init() {
		project.flatFacade()
				.configureCompileDependencies(deps -> deps
						.and(utilsBuild.project.toDependency()));
	}

}