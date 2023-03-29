import dev.jeka.core.api.java.JkJavaVersion;
import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.tool.JkBean;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.JkInjectClasspath;
import dev.jeka.core.tool.builtins.ide.IntellijJkBean;
import dev.jeka.core.tool.builtins.project.ProjectJkBean;
import org.eclipse.jdt.internal.compiler.tool.EclipseCompiler;


@JkInjectClasspath("org.eclipse.jdt:ecj:3.25.0")
class ExternalCompilerBuild extends JkBean {

    ProjectJkBean projectJkBean = getBean(ProjectJkBean.class).configure(this::configure);

    public boolean useEclipseCompiler = true;

    ExternalCompilerBuild() {
        getBean(IntellijJkBean.class).useJekaDefinedInModule("wrapper-common");
    }

    private void configure(JkProject project) {
        project.flatFacade()
            .setJvmTargetVersion(JkJavaVersion.V8)
            .configureCompileDependencies(deps -> deps
                    .and("org.apache.commons:commons-dbcp2:2.7.0"))
            .configureCompileDependencies(deps -> deps
                    .minus("org.apache.commons:commons-dbcp2"));  // Only needed at compile time (provided)
        if (useEclipseCompiler) {
            project.compiler
                        .setCompileTool(new EclipseCompiler(), "-warn:nullDereference,unusedPrivate");
        }
    }

    public void cleanPack() {
        cleanOutput(); projectJkBean.pack();
    }

}
