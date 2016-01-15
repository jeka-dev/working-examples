import org.jerkar.tool.JkProject;

/**
 * @formatter:off
 */
class FooBuild extends AbstractBuild {

	@JkProject("../core")
    CoreBuild coreBuild;

}