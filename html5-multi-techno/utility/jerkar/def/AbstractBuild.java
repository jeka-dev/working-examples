import org.jerkar.api.depmanagement.JkPopularModules;
import org.jerkar.api.depmanagement.JkVersion;
import org.jerkar.api.depmanagement.JkVersionProvider;
import org.jerkar.api.java.JkJavaCompiler;
import org.jerkar.tool.builtins.javabuild.JkJavaBuild;


abstract class AbstractBuild extends JkJavaBuild {
	
	@Override
	protected JkVersion version() {
		return JkVersion.ofName("0.1-SNAPSHOT");
	}
	
	@Override
	public String javaSourceVersion() {
		return JkJavaCompiler.V7;
	}
	
	@Override
	protected JkVersionProvider versionProvider() {
		return JkVersionProvider.of(JkPopularModules.GUAVA, "18.0");
	}

}
