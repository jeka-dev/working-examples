import static org.jerkar.api.depmanagement.JkPopularModules.*;

import org.jerkar.api.depmanagement.*;
import org.jerkar.api.java.JkJavaCompiler;
import org.jerkar.api.java.JkJavaVersion;
import org.jerkar.tool.JkInit;
import org.jerkar.tool.builtins.java.JkJavaProjectBuild;


/**
 * @formatter:off
 */
class TemplateBuild extends JkJavaProjectBuild {

    @Override
    protected void setup() {
        project().setVersionedModule("org.jerkar:examples-java-template", "1.0");
        project().getCompileSpec().setSourceAndTargetVersion(JkJavaVersion.V7);
        project().setDependencies(JkDependencySet.of()
                .and("com.google.guava:guava:18.0")
                .and("junit:junit::4.12"));
    }

    public static void main(String[] args) {
        JkInit.instanceOf(TemplateBuild.class, args).maker().makeAllArtifacts();
    }

}