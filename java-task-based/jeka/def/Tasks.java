import java.nio.file.Path;

import dev.jeka.core.api.file.JkPathTree;
import dev.jeka.core.api.java.JkJavaCompileSpec;
import dev.jeka.core.api.java.JkJavaCompiler;
import dev.jeka.core.tool.JkCommandSet;
import dev.jeka.core.tool.JkDoc;
import dev.jeka.core.api.java.junit.JkUnit;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.api.java.JkClasspath;
import dev.jeka.core.api.java.JkManifest;

class Tasks extends JkCommandSet {
	
	@JkDoc("Run test in a forked process if true.")
	boolean forkTest;

	private JkPathTree src = getBaseTree().goTo("src");
	private Path classDir = getOutputDir().resolve("classes");
	private Path jarFile = getOutputDir().resolve("capitalizer.jar");
	private JkClasspath classpath = JkClasspath.of(getBaseTree().andMatching("libs/compile/*.jar").getFiles());
	private Path testSrc = getBaseDir().resolve("test");
	private Path testClassDir = getOutputDir().resolve("test-classes");
	private JkClasspath testClasspath = classpath.and(classDir)
			.and(getBaseTree().andMatching("libs/test/*.jar").getFiles());
	private Path reportDir = getOutputDir().resolve("junitRreport");

	public void compile() {
		JkJavaCompiler.ofJdk().compile(
				JkJavaCompileSpec.of().of().setClasspath(classpath).addSources(src).setOutputDir(classDir));
		src.andMatching(false,"**/*.java").copyTo(classDir);  /// copy resources
	}

	public void jar() {
		JkManifest.ofEmpty().addMainClass("org.jerkar.samples.RunClass").writeToStandardLocation(classDir);
		JkPathTree.of(classDir).zipTo(jarFile);
	}
	
	private void compileTest() {
		JkJavaCompiler.ofJdk().compile(
				JkJavaCompileSpec.of().setClasspath(testClasspath).addSources(testSrc).setOutputDir(testClassDir));
		src.andMatching(false,"**/*.java").copyTo(testClassDir);  /// copy test resources
	}
	
	public void junit() {
		compileTest();
		JkUnit.of()
				.withReportDir(reportDir).withReport(JkUnit.JunitReportDetail.FULL)
				.withForking(forkTest)
				.run(testClasspath.and(classDir), JkPathTree.of(testClassDir));
	}

	public void doDefault() {
		clean(); compile(); junit(); jar();
	}


	public static void main(String[] args) {
		JkInit.instanceOf(Tasks.class, args).doDefault();
	}

}