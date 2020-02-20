import dev.jeka.core.api.depmanagement.JkDependencySet;
import dev.jeka.core.tool.JkCommands;
import dev.jeka.core.tool.JkImportProject;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.builtins.java.JkPluginJava;

class CoreBuild extends JkCommands {
	
	@JkImportProject("../utility")
	UtilityBuild utilityBuild;

	JkPluginJava javaPlugin = getPlugin(JkPluginJava.class);

	CoreBuild() {
		Common.setup(javaPlugin.getProject());
		javaPlugin.getProject().setDependencies(JkDependencySet.of()
				.and(utilityBuild.javaPlugin.getProject()));
	}

	public void cleanPack() {
		clean(); javaPlugin.pack();
	}

	public static void main(String[] args) {
		CoreBuild coreBuild = JkInit.instanceOf(CoreBuild.class);
		coreBuild.cleanPack();
	}

}