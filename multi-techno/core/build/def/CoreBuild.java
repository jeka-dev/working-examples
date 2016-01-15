import org.jerkar.api.depmanagement.JkDependencies;
import org.jerkar.tool.JkProject;

class CoreBuild extends AbstractBuild {
	
	@JkProject("../utility")
	UtilityBuild utilityBuild;
	
	@Override
	protected JkDependencies dependencies() {
		return JkDependencies.builder()
				.on(utilityBuild.asJavaDependency())
				.build();
	}

}