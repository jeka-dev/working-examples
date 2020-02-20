import dev.jeka.core.api.depmanagement.JkDependencySet;
import dev.jeka.core.api.depmanagement.JkJavaDepScopes;
import dev.jeka.core.api.java.JkJavaCompiler;
import dev.jeka.core.api.java.JkJavaVersion;
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
    protected void setup() {
        if (eclipseCompiler) {
            javaPlugin.getProject().getMaker().getTasksForCompilation().setCompiler(JkJavaCompiler.of(new EclipseCompiler()));
            javaPlugin.getProject().getCompileSpec().addOptions("-warn:nullDereference,unusedPrivate"); //  ecj specific options
        }
        javaPlugin.getProject().setDependencies(JkDependencySet.of().
                and("org.apache.commons:commons-dbcp2:2.7.0", JkJavaDepScopes.PROVIDED));
        javaPlugin.getProject().setSourceVersion(JkJavaVersion.V8);
    }

    public void printtoto() {
        System.out.println(System.getProperty("java.version"));
        System.out.println(System.getProperties());
    }

    public static void main(String[] args) {
        JkInit.instanceOf(ExtenalCompilerBuild.class, args).javaPlugin.getProject().getMaker().makeAllArtifacts();
    }

}