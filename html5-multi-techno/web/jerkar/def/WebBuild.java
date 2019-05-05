import static org.jerkar.api.depmanagement.JkPopularModules.GUAVA;
import static org.jerkar.api.depmanagement.JkPopularModules.JAVAX_SERVLET_API;

import java.io.File;
import java.nio.file.Path;

import org.jerkar.api.depmanagement.JkDependencies;
import org.jerkar.api.system.JkProcess;
import org.jerkar.tool.JkDoc;
import org.jerkar.tool.JkImportRun;
import org.jerkar.tool.JkInit;
import org.jerkar.tool.JkProject;
import org.jerkar.tool.JkRun;
import org.jerkar.tool.builtins.javabuild.jee.JkBuildPluginWar;


class WebBuild extends JkRun {
	
	@JkDoc("Build html5 project and embed it in produced WAR file.")
	boolean embbedHtml5 = true;

	@JkImportRun("../core")
    CoreBuild coreBuild;
	
	private Path html5ProjectDir = getBaseDir().resolve("../client-html5");
	
	File html5Folder = new File(html5ProjectDir, "build/prod");
	
	private JkProcess grunt = JkProcess.ofWinOrUx("grunt.cmd", "grunt").withWorkingDir(html5ProjectDir);
	
	JkBuildPluginWar pluginWar = new JkBuildPluginWar();
	
	public WebBuild() {
		this.plugins.activate(pluginWar);
	}
	
	@Override
	public void init() {
		if(embbedHtml5) {
			pluginWar.importStaticResources(html5Folder);
		}
	}
	
	@Override
	protected JkDependencies dependencies() {
		return JkDependencies.builder()
				.on(coreBuild.asJavaDependency())
				.on(GUAVA)
				.on(JAVAX_SERVLET_API, "3.1.0", PROVIDED).build();
	}
	
	@Override
	public void pack() {
		grunt.runSyncIf(embbedHtml5);
		super.pack();
	}

	public static void main(String[] args) {
		JkInit.instanceOf(WebBuild.class).doDefault();
	}
	
}