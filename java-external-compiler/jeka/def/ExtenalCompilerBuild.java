import dev.jeka.core.api.depmanagement.JkDependencySet;
import dev.jeka.core.api.java.JkJavaVersion;
import dev.jeka.core.api.java.project.JkJavaProjectCompilation;
import dev.jeka.core.tool.JkCommandSet;
import dev.jeka.core.tool.JkDefClasspath;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.builtins.java.JkPluginJava;
import org.eclipse.jdt.internal.compiler.tool.EclipseCompiler;

import static dev.jeka.core.api.depmanagement.JkScope.PROVIDED;


@JkDefClasspath("org.eclipse.jdt.core.compiler:ecj:4.6.1")
class ExtenalCompilerBuild extends JkCommandSet {

    JkPluginJava java = getPlugin(JkPluginJava.class);

    boolean eclipseCompiler = true;

    @Override
    protected void setup() {
        java.getProject()
            .getJarProduction()
                .getDependencyManagement()
                    .addDependencies(JkDependencySet.of().
                        and("org.apache.commons:commons-dbcp2:2.7.0", PROVIDED)).__
                .getCompilation()
                    .apply(this::setCompiler)
                    .setJavaVersion(JkJavaVersion.V8);
    }

    private void setCompiler(JkJavaProjectCompilation<?> compilation) {
        if (eclipseCompiler) {
            compilation
                .getCompiler()
                    .setCompilerTool(new EclipseCompiler()).__
                .getComputedCompileSpec()
                    .addOptions("-warn:nullDereference,unusedPrivate"); //  ecj specific options
        }
    }

    public void cleanPack() {
        clean(); java.pack();
    }

    public static void main(String[] args) {
        JkInit.instanceOf(ExtenalCompilerBuild.class, args).cleanPack();
    }

}