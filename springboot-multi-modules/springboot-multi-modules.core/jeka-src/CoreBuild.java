import dev.jeka.core.api.tooling.intellij.JkIml;
import dev.jeka.core.tool.JkInject;
import dev.jeka.core.tool.JkPostInit;
import dev.jeka.core.tool.KBean;
import dev.jeka.core.tool.builtins.project.ProjectKBean;
import dev.jeka.core.tool.builtins.tooling.ide.IntellijKBean;

class CoreBuild extends KBean {
	
	@JkInject("../springboot-multi-modules.utils")
	private ProjectKBean utilsProjectKBean;

	@JkPostInit(required = true)
	private void postInit(ProjectKBean projectKBeant) {
		projectKBeant.project.flatFacade.dependencies.compile
				.add(utilsProjectKBean.project.toDependency());
	}

}