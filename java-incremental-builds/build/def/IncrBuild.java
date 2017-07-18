import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.tool.EclipseCompiler;
import org.eclipse.jdt.internal.compiler.tool.Options;
import org.jerkar.api.depmanagement.*;
import org.jerkar.api.file.JkFileTreeSet;
import org.jerkar.api.java.JkJavaCompiler;
import org.jerkar.tool.JkImport;
import org.jerkar.tool.JkInit;
import org.jerkar.tool.builtins.javabuild.JkJavaBuild;

import javax.tools.JavaCompiler;

import static org.jerkar.api.depmanagement.JkPopularModules.*;

@JkImport("org.eclipse.jdt.core.compiler:ecj:4.6.1")
class IncrBuild extends JkJavaBuild {

    boolean eclipseCompiler = false;

    @Override
    public JkJavaCompiler productionCompiler() {
        if (!eclipseCompiler) {
            return super.productionCompiler();
        }
        return super.productionCompiler().withCompiler(new EclipseCompiler())
                .andOptions("-warn:nullDereference,unusedPrivate");  // Now you can use ecj specific options
    }

    @Override
    protected JkDependencies dependencies() {
        return JkDependencies.builder()
                .on(JkPopularModules.APACHE_COMMONS_DBCP, "1.5.4", JkJavaBuild.PROVIDED)
                .build();
    }

    @Override
    public JkFileTreeSet editedSources() {
        return baseDir().from("/src1").and(baseDir().from("/src2"));
    }

    public static void main(String[] args) {
        JkInit.instanceOf(IncrBuild.class, args).doDefault();
    }

}