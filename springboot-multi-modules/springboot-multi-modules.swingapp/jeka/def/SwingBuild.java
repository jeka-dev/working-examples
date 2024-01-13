import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.api.project.JkProjectPackaging;
import dev.jeka.core.tool.JkInjectProject;
import dev.jeka.core.tool.KBean;

import dev.jeka.core.tool.builtins.project.ProjectKBean;
import dev.jeka.core.tool.builtins.tooling.ide.IntellijKBean;

class SwingBuild extends KBean {

	final JkProject project = load(ProjectKBean.class).project;

	@JkInjectProject("../springboot-multi-modules.core")
	private CoreBuild coreBuild;

	SwingBuild() {
		load(IntellijKBean.class).useJekaDefinedInModule("wrapper-common");
	}

	@Override
    protected void init() {
		project
			.compilation
				.configureDependencies(deps -> deps.and(coreBuild.project.toDependency()));
		project.flatFacade().setMainArtifactJarType(JkProjectPackaging.JarType.FAT);
    }

}