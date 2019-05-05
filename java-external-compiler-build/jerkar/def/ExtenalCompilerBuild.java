import org.eclipse.jdt.internal.compiler.tool.EclipseCompiler;
import org.jerkar.api.depmanagement.JkDependencySet;
import org.jerkar.api.depmanagement.JkJavaDepScopes;
import org.jerkar.api.java.JkJavaCompiler;
import org.jerkar.tool.JkImport;
import org.jerkar.tool.JkInit;
import org.jerkar.tool.JkRun;
import org.jerkar.tool.builtins.java.JkPluginJava;

@JkImport("org.eclipse.jdt.core.compiler:ecj:4.6.1")
class ExtenalCompilerBuild extends JkRun {

    JkPluginJava javaPlugin = getPlugin(JkPluginJava.class);

    boolean eclipseCompiler = false;

    @Override
    public void setup() {
        if (eclipseCompiler) {
            javaPlugin.getProject().getMaker().getTasksForCompilation().setCompiler(JkJavaCompiler.of(new EclipseCompiler()));
            javaPlugin.getProject().getCompileSpec().addOptions("-warn:nullDereference,unusedPrivate"); //  ecj specific options
        }
        javaPlugin.getProject().setDependencies(JkDependencySet.of().
                and("commons-dbcp:commons-dbcp:1.5.4", JkJavaDepScopes.PROVIDED));
    }

    public static void main(String[] args) {
        JkInit.instanceOf(ExtenalCompilerBuild.class, args).javaPlugin.getProject().getMaker().makeAllArtifacts();
    }

}