import dev.jeka.core.api.depmanagement.JkFileSystemDependency;
import dev.jeka.core.api.depmanagement.JkQualifiedDependencySet;
import dev.jeka.core.api.depmanagement.JkRepo;
import dev.jeka.core.api.depmanagement.resolution.JkDependencyResolver;
import dev.jeka.core.api.file.JkPathSequence;
import dev.jeka.core.api.file.JkPathTree;
import dev.jeka.core.api.file.JkPathTreeSet;
import dev.jeka.core.api.java.JkJavaCompileSpec;
import dev.jeka.core.api.java.JkJavaCompilerToolChain;
import dev.jeka.core.api.java.JkManifest;
import dev.jeka.core.api.project.JkCompileLayout;
import dev.jeka.core.api.project.JkIdeSupport;
import dev.jeka.core.api.project.JkIdeSupportSupplier;
import dev.jeka.core.api.testing.JkTestProcessor;
import dev.jeka.core.api.testing.JkTestSelection;
import dev.jeka.core.tool.JkDoc;
import dev.jeka.core.tool.KBean;
import dev.jeka.core.tool.builtins.ide.IntellijKBean;

import java.nio.file.Path;

class Tasks extends KBean implements JkIdeSupportSupplier {

	Tasks() {
		load(IntellijKBean.class).useJekaDefinedInModule("wrapper-common");
	}
	
	@JkDoc("Run test in a forked process if true.")
	boolean forkTest;
    JkPathTree baseTree = JkPathTree.of(getBaseDir());
    JkDependencyResolver dependencyResolver = JkDependencyResolver.of().addRepos(JkRepo.ofMavenCentral());
	private JkPathTree src = baseTree.goTo("src");
	private Path classDir = getOutputDir().resolve("classes");
	private Path jarFile = getOutputDir().resolve("capitalizer.jar");

	private Path testSrc = getBaseDir().resolve("test");
	private Path testClassDir = getOutputDir().resolve("test-classes");
	private Path reportDir = getOutputDir().resolve("junitRreport");

	private JkPathSequence classpath() {
		return JkPathSequence.of(baseTree.andMatching("libs/compile/**.jar").getFiles());
	}

	private JkPathSequence cachedTestClasspath;

	private JkPathSequence testClasspath() {
		if (cachedTestClasspath == null) {
			cachedTestClasspath = classpath()
					.and(dependencyResolver.resolve("org.junit.jupiter:junit-jupiter:5.8.1").getFiles()
					.and(classDir)
					.and(baseTree.andMatching("libs/test/*.jar").getFiles()));
		}
		return cachedTestClasspath;
	}

	public void compile() {
		JkJavaCompilerToolChain.of().compile(
				JkJavaCompileSpec.of().of()
						.setClasspath(classpath())
						.setSources(src.toSet())
						.setOutputDir(classDir));
		src.andMatching(false,"**/*.java").copyTo(classDir);  /// copy resources
	}

	public void jar() {
		JkManifest.of().addMainClass("org.jerkar.samples.RunClass").writeToStandardLocation(classDir);
		JkPathTree.of(classDir).zipTo(jarFile);
	}
	
	private void compileTest() {
		JkJavaCompilerToolChain.of().compile(
				JkJavaCompileSpec.of()
						.setClasspath(testClasspath())
						.setSources(JkPathTreeSet.ofRoots(testSrc))
						.setOutputDir(testClassDir));
		src.andMatching(false,"**/*.java").copyTo(testClassDir);  /// copy test resources
	}
	
	public void junit() {
		compileTest();
		JkTestProcessor testProcessor = JkTestProcessor.of();
		testProcessor
			.setForkingProcess(forkTest)
			.engineBehavior
				.setLegacyReportDir(reportDir);
		testProcessor
			.launch(testClasspath().and(classDir).and(testClassDir),
					JkTestSelection.of().addTestClassRoots(testClassDir));
	}

	public void build() {
		cleanOutput(); compile(); junit(); jar();
	}

	@Override
	public JkIdeSupport getJavaIdeSupport() {
		return JkIdeSupport.of(this.getBaseDir())
				.setProdLayout(JkCompileLayout.of().setSources("src").mixResourcesAndSources())
				.setTestLayout(JkCompileLayout.of().setSources("test").mixResourcesAndSources())
				.setDependencies(JkQualifiedDependencySet.of()
						.and("compile", JkFileSystemDependency.of(classpath()))
						.and("test", JkFileSystemDependency.of(testClasspath()))
				);
	}

}