import org.jerkar.api.depmanagement.JkDependencySet;
import org.jerkar.tool.JkImportRun;
import org.jerkar.tool.JkRun;
import org.jerkar.tool.builtins.java.JkPluginJava;

class CoreBuild extends JkRun {
	
	@JkImportRun("../utility")
	UtilityBuild utilityBuild;

	JkPluginJava javaPlugin = getPlugin(JkPluginJava.class);

	CoreBuild() {
		javaPlugin.getProject().setDependencies(JkDependencySet.of()
				.and(utilityBuild.javaPlugin.getProject()));
	}

}