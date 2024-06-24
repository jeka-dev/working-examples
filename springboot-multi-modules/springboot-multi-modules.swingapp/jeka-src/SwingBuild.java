import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.api.project.JkProjectPackaging;
import dev.jeka.core.tool.JkInjectRunbase;
import dev.jeka.core.tool.KBean;
import dev.jeka.core.tool.builtins.project.ProjectKBean;

class SwingBuild extends KBean {

	final JkProject project = load(ProjectKBean.class).project;

	@JkInjectRunbase("../springboot-multi-modules.core")
	private CoreBuild coreBuild;

	SwingBuild() {
		/*
		load(IntellijKBean.class)
				.replaceLibByModule("springboot-multi-modules.core/.jeka-work/jeka-src-classes", "springboot-multi-modules.core")
				.replaceLibByModule("springboot-multi-modules.utils/.jeka-work/jeka-src-classes", "springboot-multi-modules.utils")
				.replaceLibByModule("springboot-multi-modules.build-commons/.jeka-work/jeka-src-classes", "springboot-multi-modules.build-commons")
				.setModuleAttributes("springboot-multi-modules.core", JkIml.Scope.COMPILE, false);

		 */
	}

	@Override
    protected void init() {
		project.flatFacade
				.setMainClass(JkProject.AUTO_FIND_MAIN_CLASS)
				.setMainArtifactJarType(JkProjectPackaging.JarType.FAT)
				.compileDependencies.add(coreBuild.project.toDependency()))

    }

}