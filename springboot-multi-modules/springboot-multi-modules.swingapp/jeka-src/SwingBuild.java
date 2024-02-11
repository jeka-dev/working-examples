import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.api.project.JkProjectPackaging;
import dev.jeka.core.tool.JkInjectRunbase;
import dev.jeka.core.tool.KBean;
import dev.jeka.core.tool.builtins.project.ProjectKBean;
import dev.jeka.core.tool.builtins.tooling.ide.IntellijKBean;

class SwingBuild extends KBean {

	final JkProject project = load(ProjectKBean.class).project;

	@JkInjectRunbase("../springboot-multi-modules.core")
	private CoreBuild coreBuild;

	SwingBuild() {
		load(IntellijKBean.class).replaceLibByModule("springboot-multi-modules.core/.jeka-work/jeka-src-classes",
				"springboot-multi-modules.core");
	}

	@Override
    protected void init() {
		project.flatFacade()
				.setMainClass(JkProject.AUTO_FIND_MAIN_CLASS)
				.customizeCompileDeps(deps -> deps.and(coreBuild.project.toDependency()))
				.setMainArtifactJarType(JkProjectPackaging.JarType.FAT);
    }

}