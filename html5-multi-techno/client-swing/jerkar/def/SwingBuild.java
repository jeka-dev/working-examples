import org.jerkar.api.depmanagement.JkDependencies;
import org.jerkar.tool.JkInit;
import org.jerkar.tool.JkProject;

/**
 * @formatter:off
 */
class SwingBuild extends AbstractBuild {
	
	{
		pack.fatJar = true;
		manifest.mainClass = "auto";
	}

	@JkProject("../core")
    CoreBuild coreBuild;
	
	@Override
	protected JkDependencies dependencies() {
		return JkDependencies.builder().on(coreBuild.asJavaDependency()).build();
	}
	
	public static void main(String[] args) {
		JkInit.instanceOf(SwingBuild.class, args).doDefault();
	}

}