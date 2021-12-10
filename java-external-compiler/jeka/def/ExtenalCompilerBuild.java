import dev.jeka.core.api.java.JkJavaVersion;
import dev.jeka.core.tool.JkBean;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.JkInjectClasspath;
import dev.jeka.core.tool.builtins.project.ProjectJkBean;
import org.eclipse.jdt.internal.compiler.tool.EclipseCompiler;


@JkInjectClasspath("org.eclipse.jdt:ecj:3.25.0")
class ExtenalCompilerBuild extends JkBean {

    ProjectJkBean projectJkBean = getRuntime().getBean(ProjectJkBean.class);

    boolean eclipseCompiler = true;

    @Override
    protected void init() {
        projectJkBean.getProject().simpleFacade()
            .setJvmTargetVersion(JkJavaVersion.V8)
            .configureCompileDeps(deps -> deps
                    .and("org.apache.commons:commons-dbcp2:2.7.0"))
            .configureCompileDeps(deps -> deps
                    .minus("org.apache.commons:commons-dbcp2"));  // Only needed at compile time (provided)
        if (eclipseCompiler) {
            projectJkBean.getProject()
                    .getConstruction()
                        .getCompiler()
                            .setCompileTool(new EclipseCompiler(), "-warn:nullDereference,unusedPrivate");
        }
    }

    public void cleanPack() {
        clean(); projectJkBean.pack();
    }

    public static void main(String[] args) {
        JkInit.instanceOf(ExtenalCompilerBuild.class, args).cleanPack();
    }

}