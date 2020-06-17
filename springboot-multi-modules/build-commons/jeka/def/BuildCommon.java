import dev.jeka.core.api.depmanagement.JkPopularModules;
import dev.jeka.core.api.depmanagement.JkVersionProvider;
import dev.jeka.core.api.java.JkJavaCompileSpec;
import dev.jeka.core.api.java.JkJavaVersion;
import dev.jeka.core.api.java.project.JkJavaProject;

/*
 * This class is reusable in any Jeka project def importing this project
 */
public abstract class BuildCommon {

    public static final JkVersionProvider VERSION_PROVIDER =
            JkVersionProvider.of(JkPopularModules.GUAVA, "22.0");

    public static void setup(JkJavaProject project) {
        project
            .getJarProduction()
                .getCompilation()
                    .setJavaVersion(JkJavaVersion.V8);

    }

}
