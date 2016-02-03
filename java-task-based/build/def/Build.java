import java.io.File;

import org.jerkar.api.file.JkFileTree;
import org.jerkar.api.java.JkClasspath;
import org.jerkar.api.java.JkJavaCompiler;
import org.jerkar.api.java.JkManifest;
import org.jerkar.api.java.junit.JkUnit;
import org.jerkar.api.java.junit.JkUnit.JunitReportDetail;
import org.jerkar.tool.JkBuild;
import org.jerkar.tool.JkDoc;
import org.jerkar.tool.JkInit;

class Build extends JkBuild {
	
	@JkDoc("Run test in a forked process if true.")
	boolean forkTest;

	private File src = file("src");
	private File classDir = ouputDir("classes");
	private File jarFile = ouputDir("capitalizer.jar");
	private JkClasspath classpath = JkClasspath.of(baseDir().include("libs/compile/*.jar"));
	private File testSrc = file("test");
	private File testClassDir = ouputDir("test-classes");
	private JkClasspath testClasspath = classpath.and(baseDir().include("libs/test/*.jar"));
	private File reportDir = ouputDir("junitRreport");

	@Override
	public void doDefault() {
		clean();
		compile();
		junit();
		jar();
	}

	public void compile() {
		JkJavaCompiler.ofOutput(classDir).withClasspath(classpath)
				.andSourceDir(src).compile();
		JkFileTree.of(src).exclude("**/*.java").copyTo(classDir);
	}

	public void jar() {
		JkManifest.empty().addMainClass("org.jerkar.samples.RunClass")
				.writeToStandardLocation(classDir);
		JkFileTree.of(classDir).zip().to(jarFile);
	}
	
	private void compileTest() {
		JkJavaCompiler.ofOutput(testClassDir).withClasspath(testClasspath.and(classDir))
				.andSourceDir(testSrc).compile();
		JkFileTree.of(testSrc).exclude("**/*.java").copyTo(testClassDir);
	}
	
	public void junit() {
		compileTest();
		JkUnit.of(testClasspath.and(classDir, testClassDir))
				.withClassesToTest(JkFileTree.of(testClassDir))
				.withReportDir(reportDir).withReport(JunitReportDetail.FULL)
				.forked(forkTest)
				.run();
	}


	public static void main(String[] args) {
		JkInit.instanceOf(Build.class, args).doDefault();
	}

}