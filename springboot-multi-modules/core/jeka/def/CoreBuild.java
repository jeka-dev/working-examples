import dev.jeka.core.tool.JkClass;
import dev.jeka.core.tool.JkDefImport;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.builtins.java.JkPluginJava;

class CoreBuild extends JkClass {
	
	@JkDefImport("../utils")
	UtilsBuild utilsBuild;

	JkPluginJava java = getPlugin(JkPluginJava.class);

	CoreBuild() {
		java.getProject().simpleFacade()
			.applyOnProject(BuildCommon::setup)
			.setCompileDependencies(deps -> deps
				.and(utilsBuild.java.getProject().toDependency()));
	}

	public void cleanPack() {
		clean(); java.pack();
	}

	public static void main(String[] args) {
		CoreBuild coreBuild = JkInit.instanceOf(CoreBuild.class);
		coreBuild.cleanPack();
		System.out.println("Hello Word");
	}

}