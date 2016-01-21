import static org.jerkar.api.depmanagement.JkPopularModules.*;

import java.io.File;

import org.jerkar.api.depmanagement.JkDependencies;
import org.jerkar.api.system.JkProcess;
import org.jerkar.tool.JkDoc;
import org.jerkar.tool.JkInit;
import org.jerkar.tool.JkProject;
import org.jerkar.tool.builtins.javabuild.jee.JkBuildPluginWar;

class WebBuild extends AbstractBuild {
	
	@JkDoc("Build html5 project and embed it in produced WAR file.")
	boolean embbedHtml5 = true;

	@JkProject("../core")
    CoreBuild coreBuild;
	
	File html5ProjectDir = file("../client-html5");  
	
	File html5Folder = new File(html5ProjectDir, "build/prod");
	
	JkProcess grunt = JkProcess.ofWinOrUx("grunt.cmd", "grunt").withWorkingDir(html5ProjectDir);
	
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
				.on(JAVAX_SERVLET_API, "2.5", PROVIDED).build();
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