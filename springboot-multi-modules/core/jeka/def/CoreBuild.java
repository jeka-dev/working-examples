import dev.jeka.core.api.depmanagement.JkDependencySet;
import dev.jeka.core.tool.JkCommandSet;
import dev.jeka.core.tool.JkDefImport;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.builtins.java.JkPluginJava;

class CoreBuild extends JkCommandSet {
	
	@JkDefImport("../utils")
	UtilsBuild utilsBuild;

	JkPluginJava java = getPlugin(JkPluginJava.class);

	CoreBuild() {
		BuildCommon.setup(java.getProject());
		java.getProject().getJarProduction()
			.getDependencyManagement()
				.addDependencies(JkDependencySet.of()
					.and(utilsBuild.java.getProject().toDependency()));
	}

	public void cleanPack() {
		clean(); java.pack();
	}

	public static void main(String[] args) {
		CoreBuild coreBuild = JkInit.instanceOf(CoreBuild.class);
		coreBuild.cleanPack();
		System.out.println("eeeghjkkjkhjg");
	}

}