import static org.jerkar.api.depmanagement.JkPopularModules.*;

import org.jerkar.api.depmanagement.*;
import org.jerkar.api.java.JkJavaCompiler;
import org.jerkar.tool.JkInit;
import org.jerkar.tool.builtins.javabuild.JkJavaBuild;

/**
 * @formatter:off
 */
class Build extends JkJavaBuild {
	
	{
		pack.fatJar = true;
	}
	
	@Override
	public JkModuleId moduleId() {
		return JkModuleId.of("org.jerkar", "examples-java-template");
	}
	
	@Override
	public String javaSourceVersion() {
		return JkJavaCompiler.V7;
	}

    @Override
    public JkDependencies dependencies() {
        return JkDependencies.builder()
        		.on(GUAVA, "18.0")
        		.on(JUNIT, "4.12", TEST)
        		.build();
    }
    
    public static void main(String[] args) {
		JkInit.instanceOf(Build.class, args).doDefault();
	}

}