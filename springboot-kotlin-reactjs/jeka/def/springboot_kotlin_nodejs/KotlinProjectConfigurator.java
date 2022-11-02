package springboot_kotlin_nodejs;

import dev.jeka.core.api.depmanagement.JkDependencySet;
import dev.jeka.core.api.depmanagement.JkRepo;
import dev.jeka.core.api.depmanagement.JkRepoSet;
import dev.jeka.core.api.depmanagement.JkVersionProvider;
import dev.jeka.core.api.file.JkPathSequence;
import dev.jeka.core.api.file.JkPathTree;
import dev.jeka.core.api.file.JkPathTreeSet;
import dev.jeka.core.api.kotlin.JkKotlinCompiler;
import dev.jeka.core.api.kotlin.JkKotlinJvmCompileSpec;
import dev.jeka.core.api.kotlin.JkKotlinModules;
import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.api.project.JkProjectCompilation;
import dev.jeka.core.api.system.JkLog;
import dev.jeka.core.api.utils.JkUtilsString;

import static dev.jeka.core.api.project.JkProjectCompilation.JAVA_SOURCES_COMPILE_ACTION;

class KotlinProjectConfigurator {

    public static final String KOTLIN_JVM_SOURCES_COMPILE_ACTION = "kotlin-jvm-sources-compile";

    String kotlinVersion;

    boolean addStdlib = true;

    String kotlinSourceDir = "src/main/kotlin";

    String kotlinTestSourceDir = "src/test/kotlin";

    JkRepoSet downloadRepos = JkRepo.ofMavenCentral().toSet();

    private JkKotlinCompiler kotlinCompiler;

    void configure(JkProject project) {
        JkProjectCompilation<?> prodCompile = project.getCompilation();
        JkProjectCompilation<?> testCompile = project.getTesting().getCompilation();
        System.out.println("-------------------------------- i am configuring " + project);
        prodCompile
                .getPreCompileActions()
                .appendBefore(KOTLIN_JVM_SOURCES_COMPILE_ACTION, JAVA_SOURCES_COMPILE_ACTION,
                        () -> compileKotlin(getKotlinCompiler(), project))
                .__
                .configureDependencies(deps -> deps.andVersionProvider(kotlinVersionProvider()));
        testCompile
                .getPreCompileActions()
                .appendBefore(KOTLIN_JVM_SOURCES_COMPILE_ACTION, JAVA_SOURCES_COMPILE_ACTION,
                        () -> compileTestKotlin(getKotlinCompiler(), project))
                .__
                .getLayout()
                .addSource(kotlinTestSourceDir);
        JkPathTree javaInKotlinDir = JkPathTree.of(project.getBaseDir().resolve(kotlinSourceDir));
        JkPathTree javaInKotlinTestDir = JkPathTree.of(project.getBaseDir().resolve(kotlinTestSourceDir));
        prodCompile.getLayout().setSources(javaInKotlinDir);
        testCompile.getLayout().setSources(javaInKotlinTestDir);
        if (addStdlib) {
            prodCompile.configureDependencies(this::addStdLibsToProdDeps);
            testCompile.configureDependencies(this::addStdLibsToTestDeps);
        }
        project.setJavaIdeSupport(ideSupport -> {
            ideSupport.getProdLayout().addSource(project.getBaseDir().resolve(kotlinSourceDir));
            if (kotlinTestSourceDir != null) {
                ideSupport.getTestLayout().addSource(project.getBaseDir().resolve(kotlinTestSourceDir));
            }
            return ideSupport;
        });
    }

    private JkDependencySet addStdLibsToProdDeps(JkDependencySet deps) {
        return getKotlinCompiler().isProvidedCompiler()
                ? deps.andFiles(getKotlinCompiler().getStdLib())
                : deps.and(JkKotlinModules.STDLIB_JDK8).and(JkKotlinModules.REFLECT);
    }

    private JkDependencySet addStdLibsToTestDeps(JkDependencySet deps) {
        return getKotlinCompiler().isProvidedCompiler() ? deps.and(JkKotlinModules.TEST) : deps;
    }

    private JkVersionProvider kotlinVersionProvider() {
        return JkKotlinModules.versionProvider(getKotlinCompiler().getVersion());
    }


    public JkKotlinCompiler getKotlinCompiler() {
        if (kotlinCompiler != null) {
            return kotlinCompiler;
        }
        if (JkUtilsString.isBlank(kotlinVersion)) {
            kotlinCompiler = JkKotlinCompiler.ofKotlinHomeCommand("kotlinc");
            JkLog.warn("No version of kotlin has been specified, will use the version installed on KOTLIN_HOME : "
                    + kotlinCompiler.getVersion());
        } else {
            kotlinCompiler = JkKotlinCompiler.ofJvm(downloadRepos, kotlinVersion);
        }
        kotlinCompiler.setLogOutput(true);
        return kotlinCompiler;
    }

    private void compileKotlin(JkKotlinCompiler kotlinCompiler, JkProject javaProject) {
        JkProjectCompilation compilation = javaProject.getCompilation();
        JkPathTreeSet sources = compilation.getLayout().resolveSources()
                .and(javaProject.getBaseDir().resolve(kotlinSourceDir));
        if (sources.count(1, false) == 0) {
            JkLog.info("No source to compile in " + sources);
            return;
        }
        JkKotlinJvmCompileSpec compileSpec = JkKotlinJvmCompileSpec.of()
                .setClasspath(compilation.resolveDependencies().getFiles())
                .setOutputDir(compilation.getLayout().getOutputDir().resolve("classes"))
                .setTargetVersion(javaProject.getJvmTargetVersion())
                .setSources(sources);
        kotlinCompiler.compile(compileSpec);
    }

    private void compileTestKotlin(JkKotlinCompiler kotlinCompiler, JkProject javaProject) {
        JkProjectCompilation compilation = javaProject.getTesting().getCompilation();
        JkPathTreeSet sources = compilation.getLayout().resolveSources();
        if (kotlinTestSourceDir == null) {
            sources = sources.and(javaProject.getBaseDir().resolve(kotlinTestSourceDir));
        }
        if (sources.count(1, false) == 0) {
            JkLog.info("No source to compile in " + sources);
            return;
        }
        JkPathSequence classpath = compilation.resolveDependencies().getFiles()
                .and(compilation.getLayout().getClassDirPath());
        JkKotlinJvmCompileSpec compileSpec = JkKotlinJvmCompileSpec.of()
                .setSources(compilation.getLayout().resolveSources())
                .setClasspath(classpath)
                .setOutputDir(compilation.getLayout().getOutputDir().resolve("test-classes"))
                .setTargetVersion(javaProject.getJvmTargetVersion());
        kotlinCompiler.compile(compileSpec);
    }


}
