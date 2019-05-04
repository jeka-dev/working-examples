import org.eclipse.jdt.internal.compiler.tool.EclipseCompiler;
import org.jerkar.api.depmanagement.JkDependencySet;
import org.jerkar.api.depmanagement.JkJavaDepScopes;
import org.jerkar.api.java.JkJavaCompiler;
import org.jerkar.tool.JkImport;
import org.jerkar.tool.JkInit;
import org.jerkar.tool.builtins.java.JkJavaProjectBuild;

@JkImport("org.eclipse.jdt.core.compiler:ecj:4.6.1")
class IncrBuild extends JkJavaProjectBuild {

    boolean eclipseCompiler = false;

    @Override
    public void setup() {
        if (eclipseCompiler) {
            maker().getCompileTasks().setCompiler(JkJavaCompiler.of(new EclipseCompiler()));
            project().getCompileSpec().addOptions("-warn:nullDereference,unusedPrivate"); //  ecj specific options
        }
        project().setDependencies(JkDependencySet.of().
                and("commons-dbcp:commons-dbcp:1.5.4", JkJavaDepScopes.PROVIDED));
    }

    public static void main(String[] args) {
        JkInit.instanceOf(IncrBuild.class, args).maker().makeAllArtifacts();
    }

}