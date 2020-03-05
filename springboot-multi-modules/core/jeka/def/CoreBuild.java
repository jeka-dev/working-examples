import dev.jeka.core.api.depmanagement.JkDependencySet;
import dev.jeka.core.tool.JkCommandSet;
import dev.jeka.core.tool.JkDefImport;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.builtins.java.JkPluginJava;

class CoreBuild extends JkCommandSet {
	
	@JkDefImport("../utils")
	UtilsBuild utilsBuild;

	JkPluginJava javaPlugin = getPlugin(JkPluginJava.class);

	CoreBuild() {
		BuildCommon.setup(javaPlugin.getProject());
		javaPlugin.getProject().setDependencies(JkDependencySet.of()
				.and(utilsBuild.javaPlugin.getProject()));
	}

	public void cleanPack() {
		clean(); javaPlugin.pack();
	}

	public static void main(String[] args) {
		CoreBuild coreBuild = JkInit.instanceOf(CoreBuild.class);
		coreBuild.cleanPack();
		System.out.println("eeeghjkkjkhjg");
	}

}