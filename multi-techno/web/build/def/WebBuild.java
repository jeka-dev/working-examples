import static org.jerkar.api.depmanagement.JkPopularModules.*;

import java.io.File;

import org.jerkar.api.depmanagement.JkDependencies;
import org.jerkar.api.system.JkProcess;
import org.jerkar.tool.JkInit;
import org.jerkar.tool.JkProject;
import org.jerkar.tool.builtins.javabuild.jee.JkBuildPluginWar;

class WebBuild extends AbstractBuild {

	@JkProject("../core")
    CoreBuild coreBuild;
	
	File html5ProjectDir = file("../client-html5");  
	
	File html5Folder = new File(html5ProjectDir, "build/prod");
	
	JkProcess grunt = JkProcess.of("grunt").withWorkingDir(html5ProjectDir);
	
	public WebBuild() {
		JkBuildPluginWar pluginWar = new JkBuildPluginWar();
		this.plugins.activate(pluginWar);
	}
	
	@Override
	protected JkDependencies dependencies() {
		return JkDependencies.builder()
				.on(coreBuild.asJavaDependency())
				.on(GUAVA)
				.on(JAVAX_SERVLET_API, "2.5", PROVIDED).build();
	}
	
	public static void main(String[] args) {
		JkInit.instanceOf(WebBuild.class).doDefault();
	}
	
	

}