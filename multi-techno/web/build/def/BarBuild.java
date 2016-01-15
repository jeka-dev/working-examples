import static org.jerkar.api.depmanagement.JkPopularModules.*;

import java.io.File;

import org.jerkar.api.depmanagement.JkDependencies;
import org.jerkar.api.tooling.JkMvn;
import org.jerkar.tool.JkProject;

class BarBuild extends AbstractBuild {

	@JkProject("../core")
    CoreBuild coreBuild;
	
	File jarFromLegacyProject;
	
	JkMvn mvn;
	
	protected BarBuild() {
		jarFromLegacyProject = file("../legacy/target/legacy-1.0.jar");
		mvn = JkMvn.of(file("../legacy"), "clean", "package");
	}
	
	@Override
	protected JkDependencies dependencies() {
		return JkDependencies.builder()
				.on(coreBuild.asJavaDependency())
				.on(GUAVA)
				.on(mvn.asProcess(), jarFromLegacyProject, COMPILE).build();
	}

}