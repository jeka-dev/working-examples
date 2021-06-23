import dev.jeka.core.api.java.JkJavaVersion;
import dev.jeka.core.tool.JkClass;
import dev.jeka.core.tool.JkDefClasspath;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.builtins.java.JkPluginJava;
import org.eclipse.jdt.internal.compiler.tool.EclipseCompiler;


@JkDefClasspath("org.eclipse.jdt:ecj:3.25.0")
class ExtenalCompilerBuild extends JkClass {

    JkPluginJava java = getPlugin(JkPluginJava.class);

    boolean eclipseCompiler = true;

    @Override
    protected void setup() {
        java.getProject().simpleFacade()
            .setJavaVersion(JkJavaVersion.V8)
            .setCompileDependencies(deps -> deps
                    .and("org.apache.commons:commons-dbcp2:2.7.0"))
            .setRuntimeDependencies(deps -> deps
                    .minus("org.apache.commons:commons-dbcp2"));  // Only needed at compile time (provided)
        if (eclipseCompiler) {
            java.getProject()
                    .getConstruction()
                        .getCompiler()
                            .setCompileTool(new EclipseCompiler(), "-warn:nullDereference,unusedPrivate");
        }
    }

    public void cleanPack() {
        clean(); java.pack();
    }

    public static void main(String[] args) {
        JkInit.instanceOf(ExtenalCompilerBuild.class, args).cleanPack();
    }

}