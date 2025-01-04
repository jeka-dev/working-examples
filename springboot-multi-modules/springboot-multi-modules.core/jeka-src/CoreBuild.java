import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.tool.JkInjectRunbase;
import dev.jeka.core.tool.KBean;
import dev.jeka.core.tool.builtins.project.ProjectKBean;

class CoreBuild extends KBean {
	
	@JkInjectRunbase("../springboot-multi-modules.utils")
	private UtilsBuild utilsBuild;

	final JkProject project = load(ProjectKBean.class).project;

	@Override
	protected void init() {
		project.flatFacade.dependencies.compile
				.add(utilsBuild.project.toDependency());
	}

}