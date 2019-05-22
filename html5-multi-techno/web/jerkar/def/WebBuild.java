import org.jerkar.api.depmanagement.JkDependencySet;
import org.jerkar.api.depmanagement.JkJavaDepScopes;
import org.jerkar.api.java.project.JkJavaProject;
import org.jerkar.api.system.JkProcess;
import org.jerkar.tool.JkDoc;
import org.jerkar.tool.JkImportProject;
import org.jerkar.tool.JkInit;
import org.jerkar.tool.JkRun;
import org.jerkar.tool.builtins.java.JkPluginJava;
import org.jerkar.tool.builtins.java.JkPluginWar;

import java.nio.file.Path;

import static org.jerkar.api.depmanagement.JkPopularModules.GUAVA;
import static org.jerkar.api.depmanagement.JkPopularModules.JAVAX_SERVLET_API;


class WebBuild extends JkRun {
	
	@JkDoc("Build html5 project and embed it in produced WAR file.")
	boolean embbedHtml5 = true;

	@JkImportProject("../core")
	private CoreBuild coreBuild;
	
	private Path html5ProjectDir = getBaseDir().resolve("../client-html5");
	
	private Path html5Folder = html5ProjectDir.resolve("build/prod");
	
	private JkProcess grunt = JkProcess.ofWinOrUx("grunt.cmd", "grunt")
			.withWorkingDir(html5ProjectDir);
	
	JkPluginWar warPlugin = getPlugin(JkPluginWar.class);

	JkPluginJava javaPlugin = getPlugin((JkPluginJava.class));

	@Override
	public void setup() {
		if(embbedHtml5) {
			warPlugin.setStaticResourceDir(html5Folder);
		}
		JkJavaProject project = javaPlugin.getProject();
		project.addDependencies(JkDependencySet.of()
				.and(coreBuild.javaPlugin.getProject())
				.and(GUAVA, "22.0")
				.and(JAVAX_SERVLET_API, "3.1.0", JkJavaDepScopes.PROVIDED)
		);
		warPlugin.getStaticResouceComputation().chain(grunt);
	}

	public static void main(String[] args) {
		JkInit.instanceOf(WebBuild.class).javaPlugin.clean().pack();
	}
	
}