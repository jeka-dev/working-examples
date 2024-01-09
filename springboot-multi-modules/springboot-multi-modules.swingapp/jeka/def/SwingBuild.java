import dev.jeka.core.api.project.JkIdeSupport;
import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.api.project.JkProjectPackaging;
import dev.jeka.core.tool.JkInjectProject;
import dev.jeka.core.tool.KBean;
import dev.jeka.core.tool.builtins.ide.IntellijKBean;
import dev.jeka.core.tool.builtins.project.ProjectKBean;

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
			.packaging
				.manifest
					.addMainClass("swing.Main");
		project
			.compilation
				.configureDependencies(deps -> deps.and(coreBuild.project.toDependency()));
		project.flatFacade().setMainArtifactJarType(JkProjectPackaging.JarType.FAT);
    }

}