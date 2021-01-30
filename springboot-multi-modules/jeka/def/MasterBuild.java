import dev.jeka.core.api.file.JkPathTree;
import dev.jeka.core.api.system.JkLog;
import dev.jeka.core.tool.JkClass;
import dev.jeka.core.tool.JkDefImport;
import dev.jeka.core.tool.JkInit;

import java.nio.file.Path;

/**
 * @formatter:off
 */
class MasterBuild extends JkClass {

	@JkDefImport("springbootapp")
	SpringbootBuild springbootBuild;

	@JkDefImport("swingapp")
	SwingBuild swingBuild;

	Path distribFolder = getOutputDir();

	public void build() {
		clean();
		springbootBuild.springboot.javaPlugin().pack();
		swingBuild.java.pack();
		copyJars();
	}

	@Override
	public void clean() {
		super.clean();
		this.getImportedJkClasses().getAll().forEach(JkClass::clean);
	}

	private void copyJars() {
		JkPathTree.of(distribFolder)
				.importFiles(springbootBuild.springboot.javaPlugin().getProject().getPublication().getArtifactProducer().getMainArtifactPath())
				.importFiles(swingBuild.java.getProject().getPublication().getArtifactProducer().getMainArtifactPath());
		JkLog.info("Distrib jar files copied in " + distribFolder);
	}

	public static void main(String[] args) {
		JkInit.instanceOf(MasterBuild.class, args).build();
	}

}