import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.api.project.JkProjectPackaging;
import dev.jeka.core.api.tooling.intellij.JkIml;
import dev.jeka.core.tool.JkInject;
import dev.jeka.core.tool.JkInjectRunbase;
import dev.jeka.core.tool.JkPostInit;
import dev.jeka.core.tool.KBean;
import dev.jeka.core.tool.builtins.project.ProjectKBean;
import dev.jeka.core.tool.builtins.tooling.ide.IntellijKBean;

class SwingBuild extends KBean {

	@JkInject("../springboot-multi-modules.core")
	private ProjectKBean coreProjectKBean;

	@JkPostInit(required = true)
    private void postInit(ProjectKBean projectKBean) {
		JkProject project = projectKBean.project;
		project.packaging.setDetectMainClass(true);
		project.flatFacade
				.setMainArtifactJarType(JkProjectPackaging.JarType.FAT)
				.dependencies.compile
					.add(coreProjectKBean.project.toDependency());
    }

}