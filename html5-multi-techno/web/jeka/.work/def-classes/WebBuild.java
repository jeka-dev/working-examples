import dev.jeka.core.api.depmanagement.JkDependencySet;
import dev.jeka.core.api.depmanagement.JkJavaDepScopes;
import dev.jeka.core.api.depmanagement.JkPopularModules;
import dev.jeka.core.api.java.project.JkJavaProject;
import dev.jeka.core.api.system.JkProcess;
import dev.jeka.core.tool.JkCommands;
import dev.jeka.core.tool.JkDoc;
import dev.jeka.core.tool.JkImportProject;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.builtins.java.JkPluginJava;
import dev.jeka.core.tool.builtins.java.JkPluginWar;

import java.nio.file.Path;

import static dev.jeka.core.api.depmanagement.JkJavaDepScopes.PROVIDED;

class WebBuild extends JkCommands {
	
	@JkDoc("Build html5 project and embed it in produced WAR file.")
	boolean embbedHtml5 = false;

	@JkImportProject("../core")
	private CoreBuild coreBuild;
	
	private Path html5ProjectDir = getBaseDir().resolve("../client-html5");
	
	private Path html5Folder = html5ProjectDir.resolve("build/prod");
	
	private JkProcess grunt = JkProcess.ofWinOrUx("grunt.cmd", "grunt")
			.withWorkingDir(html5ProjectDir);
	
	JkPluginWar warPlugin = getPlugin(JkPluginWar.class);

	JkPluginJava javaPlugin = getPlugin((JkPluginJava.class));

	@Override
	protected void setup() {
		if(embbedHtml5) {
			warPlugin.setStaticResourceDir(html5Folder);
			warPlugin.getStaticResouceComputation().chain(grunt);
		}
		JkJavaProject project = javaPlugin.getProject();
		Common.setup(project);
		project.addDependencies(JkDependencySet.of()
				.and(coreBuild.javaPlugin.getProject())
				.and(JkPopularModules.GUAVA, "22.0")
				.and(JkPopularModules.JAVAX_SERVLET_API, "3.1.0", PROVIDED)
		);
	}

	public void cleanPack() {
		clean();javaPlugin.pack();
	}

	public static void main(String[] args) {
		JkInit.instanceOf(WebBuild.class).cleanPack();
	}
	
}