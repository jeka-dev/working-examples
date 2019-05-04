import java.nio.file.Path;

import org.jerkar.api.file.JkPathTree;
import org.jerkar.api.java.JkClasspath;
import org.jerkar.api.java.JkJavaCompileSpec;
import org.jerkar.api.java.JkJavaCompiler;
import org.jerkar.api.java.JkManifest;
import org.jerkar.api.java.junit.JkUnit;
import org.jerkar.api.java.junit.JkUnit.JunitReportDetail;

import org.jerkar.tool.JkDoc;
import org.jerkar.tool.JkInit;
import org.jerkar.tool.JkRun;

class TaskBuild extends JkRun {
	
	@JkDoc("Run test in a forked process if true.")
	boolean forkTest;

	private JkPathTree src = getBaseTree().goTo("src");
	private Path classDir = getOutputDir().resolve("classes");
	private Path jarFile = getOutputDir().resolve("capitalizer.jar");
	private JkClasspath classpath = JkClasspath.of(getBaseTree().andMatching("libs/compile/*.jar").getFiles());
	private Path testSrc = getBaseDir().resolve("test");
	private Path testClassDir = getOutputDir().resolve("test-classes");
	private JkClasspath testClasspath = classpath.and(getBaseTree().andMatching("libs/test/*.jar").getFiles());
	private Path reportDir = getOutputDir().resolve("junitRreport");

	public void doDefault() {
		clean();
		compile();
		junit();
		jar();
	}

	public void compile() {
		JkJavaCompiler.ofJdk().compile(
				JkJavaCompileSpec.of().setClasspath(classpath).addSources(src).setOutputDir(classDir));
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
				.withReportDir(reportDir).withReport(JunitReportDetail.FULL)
				.withForking(forkTest)
				.run(testClasspath.and(classDir), JkPathTree.of(testClassDir));
	}


	public static void main(String[] args) {
		JkInit.instanceOf(TaskBuild.class, args).doDefault();
	}

}