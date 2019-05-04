import org.jerkar.api.depmanagement.JkDependencies;
import org.jerkar.api.depmanagement.JkPopularModules;

/**
 * @formatter:off
 */
class UtilityBuild extends AbstractBuild {

    @Override
    public JkDependencies dependencies() {
        return JkDependencies.builder()
        		.on(JkPopularModules.GUAVA,"18.0", COMPILE).build();
    }
 
}