import dev.jeka.core.api.depmanagement.JkDependencySet;
import dev.jeka.core.api.depmanagement.JkJavaDepScopes;
import dev.jeka.core.api.java.JkJavaCompiler;
import dev.jeka.core.tool.JkCommands;
import dev.jeka.core.tool.JkImport;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.builtins.java.JkPluginJava;
import org.eclipse.jdt.internal.compiler.tool.EclipseCompiler;


@JkImport("org.eclipse.jdt.core.compiler:ecj:4.6.1")
class ExtenalCompilerBuild extends JkCommands {

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