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

import java.nio.file.Path;

class Tasks extends KBean implements JkIdeSupportSupplier {
	
	@JkDoc("Run test in a forked process if true.")
	boolean forkTest;

	// The dependency resolver to fetch dependencies
    JkDependencyResolver dependencyResolver = JkDependencyResolver.of()
			.addRepos(JkRepo.ofMavenCentral());

	// Prod layout
	JkPathTree baseTree = JkPathTree.of(getBaseDir());
	JkPathTree src = JkPathTree.of(getBaseDir().resolve("src"));
	Path classDir = getOutputDir().resolve("classes");
	Path jarFile = getOutputDir().resolve("capitalizer.jar");

	// Test layout
	Path testSrc = getBaseDir().resolve("test");
	Path testClassDir = getOutputDir().resolve("test-classes");
	Path reportDir = getOutputDir().resolve("junitRreport");

	// cached test classpath
	JkPathSequence cachedTestClasspath;

	@JkDoc("Compile production source code")
	public void compile() {
		JkJavaCompileSpec compileSpec = JkJavaCompileSpec.of()
				.setClasspath(classpath())
				.setSources(src.toSet())
				.setOutputDir(classDir);
		JkJavaCompilerToolChain.of().compile(compileSpec);
		src.andMatching(false,"**/*.java").copyTo(classDir);  /// copy resources
	}

	@JkDoc("Jars the compiled classes into a JAR file.")
	public void jar() {
		JkManifest.of().addMainClass("org.jerkar.samples.RunClass").writeToStandardLocation(classDir);
		JkPathTree.of(classDir).zipTo(jarFile);
	}

	@JkDoc("Compile test source code")
	private void compileTest() {
		JkJavaCompilerToolChain.of().compile(
				JkJavaCompileSpec.of()
						.setClasspath(testClasspath())
						.setSources(JkPathTreeSet.ofRoots(testSrc))
						.setOutputDir(testClassDir));
		src.andMatching(false,"**/*.java").copyTo(testClassDir);  /// copy test resources
	}

	@JkDoc("Run tests")
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

	@JkDoc("Clean, compile, test and jar")
	public void build() {
		cleanOutput(); compile(); junit(); jar();
	}

	@Override
	public JkIdeSupport getJavaIdeSupport() {
		return JkIdeSupport.of(this.getBaseDir())
				.setProdLayout(JkCompileLayout.of().setSources("src").setMixResourcesAndSources())
				.setTestLayout(JkCompileLayout.of().setSources("test").setMixResourcesAndSources())
				.setDependencies(JkQualifiedDependencySet.of()
						.and("compile", JkFileSystemDependency.of(classpath()))
						.and("test", JkFileSystemDependency.of(testClasspath()))
				);
	}

	private JkPathSequence classpath() {

		// The production dependencies are stored locally
		return JkPathSequence.of(baseTree.andMatching("libs/compile/**.jar").getFiles());
	}

	private JkPathSequence testClasspath() {
		if (cachedTestClasspath == null) {
			cachedTestClasspath = classpath()
					.and(dependencyResolver.resolve("org.junit.jupiter:junit-jupiter:5.8.1").getFiles()
							.and(classDir)
							.and(baseTree.andMatching("libs/test/*.jar").getFiles()));
		}
		return cachedTestClasspath;
	}

	public static void main(String[] args) {
		System.out.println("\u2714 existing");
	}

}