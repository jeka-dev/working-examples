import dev.jeka.core.api.file.JkPathSequence;
import dev.jeka.core.api.file.JkPathTree;
import dev.jeka.core.api.java.JkJavaCompileSpec;
import dev.jeka.core.api.java.JkJavaCompiler;
import dev.jeka.core.api.java.JkManifest;
import dev.jeka.core.api.java.testing.JkTestProcessor;
import dev.jeka.core.api.java.testing.JkTestSelection;
import dev.jeka.core.tool.JkClass;
import dev.jeka.core.tool.JkDoc;
import dev.jeka.core.tool.JkInit;

import java.nio.file.Path;

class Tasks extends JkClass {
	
	@JkDoc("Run test in a forked process if true.")
	boolean forkTest;

	private JkPathTree src = getBaseTree().goTo("src");
	private Path classDir = getOutputDir().resolve("classes");
	private Path jarFile = getOutputDir().resolve("capitalizer.jar");
	private JkPathSequence classpath = JkPathSequence.of(getBaseTree().andMatching("libs/compile/*.jar").getFiles());
	private Path testSrc = getBaseDir().resolve("test");
	private Path testClassDir = getOutputDir().resolve("test-classes");
	private JkPathSequence testClasspath = classpath.and(classDir)
			.and(getBaseTree().andMatching("libs/test/*.jar").getFiles());
	private Path reportDir = getOutputDir().resolve("junitRreport");

	public void compile() {
		JkJavaCompiler.of().compile(
				JkJavaCompileSpec.of().of().setClasspath(classpath).addSources(src).setOutputDir(classDir));
		src.andMatching(false,"**/*.java").copyTo(classDir);  /// copy resources
	}

	public void jar() {
		JkManifest.of().addMainClass("org.jerkar.samples.RunClass").writeToStandardLocation(classDir);
		JkPathTree.of(classDir).zipTo(jarFile);
	}
	
	private void compileTest() {
		JkJavaCompiler.of().compile(
				JkJavaCompileSpec.of().setClasspath(testClasspath).addSources(testSrc).setOutputDir(testClassDir));
		src.andMatching(false,"**/*.java").copyTo(testClassDir);  /// copy test resources
	}
	
	public void junit() {
		compileTest();
		JkTestProcessor.of()
			.setForkingProcess(forkTest)
			.getEngineBehavior()
				.setLegacyReportDir(reportDir).__
			.launch(testClasspath.and(classDir).and(testClassDir),
					JkTestSelection.of().addTestClassRoots(testClassDir));
	}

	public void doDefault() {
		clean(); compile(); junit(); jar();
	}


	public static void main(String[] args) {
		JkInit.instanceOf(Tasks.class, args).doDefault();
	}

}