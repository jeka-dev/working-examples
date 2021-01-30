import dev.jeka.core.api.depmanagement.JkDependencySet;
import dev.jeka.core.api.java.JkJavaVersion;
import dev.jeka.core.tool.JkClass;
import dev.jeka.core.tool.JkDefClasspath;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.builtins.java.JkPluginJava;
import org.eclipse.jdt.internal.compiler.tool.EclipseCompiler;

import static dev.jeka.core.api.depmanagement.JkScope.PROVIDED;


@JkDefClasspath("org.eclipse.jdt.core.compiler:ecj:4.6.1")
class ExtenalCompilerBuild extends JkClass {

    JkPluginJava java = getPlugin(JkPluginJava.class);

    boolean eclipseCompiler = true;

    @Override
    protected void setup() {
        java.getProject().simpleFacade()
            .setJavaVersion(JkJavaVersion.V8)
            .addDependencies(JkDependencySet.of()
                    .and("org.apache.commons:commons-dbcp2:2.7.0", PROVIDED));
        if (eclipseCompiler) {
            java.getProject().getConstruction().getCompilation()
                    .getCompiler()
                        .setCompilerTool(new EclipseCompiler()).__
                    .addOptions("-warn:nullDereference,unusedPrivate");
        }
    }

    public void cleanPack() {
        clean(); java.pack();
    }

    public static void main(String[] args) {
        JkInit.instanceOf(ExtenalCompilerBuild.class, args).cleanPack();
    }

}