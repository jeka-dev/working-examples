import static org.jerkar.api.depmanagement.JkPopularModules.GUAVA;
import static org.jerkar.api.depmanagement.JkPopularModules.JAVAX_SERVLET_API;

import java.io.File;

import org.jerkar.api.depmanagement.JkDependencies;
import org.jerkar.api.system.JkLog;
import org.jerkar.api.system.JkProcess;
import org.jerkar.tool.JkDoc;
import org.jerkar.tool.JkImport;
import org.jerkar.tool.JkInit;
import org.jerkar.tool.JkProject;
import org.jerkar.tool.builtins.javabuild.jee.JkBuildPluginWar;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;

@JkImport("org.mortbay.jetty:jetty:6.1.25")
class WebBuild extends AbstractBuild {
	
	@JkDoc("Build html5 project and embed it in produced WAR file.")
	boolean embbedHtml5 = true;

	@JkProject("../core")
    CoreBuild coreBuild;
	
	private File html5ProjectDir = file("../client-html5");  
	
	File html5Folder = new File(html5ProjectDir, "build/prod");
	
	private JkProcess grunt = JkProcess.ofWinOrUx("grunt.cmd", "grunt").withWorkingDir(html5ProjectDir);
	
	private JkBuildPluginWar pluginWar = new JkBuildPluginWar();
	
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
	
	public void jetty() throws Exception {
		Server server = new Server(8080);
		WebAppContext webAppContext = new WebAppContext();
		webAppContext.setContextPath("/");
		webAppContext.setWar(this.pluginWar.warFile().getPath());
		server.setHandler(webAppContext);
		server.start();
		JkLog.info("server started");
		server.join();	
	}
	
	public static void main(String[] args) {
		JkInit.instanceOf(WebBuild.class).doDefault();
	}
	
}