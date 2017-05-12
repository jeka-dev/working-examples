import org.jerkar.api.depmanagement.*;
import org.jerkar.tool.JkBuildDependencySupport;
import org.jerkar.tool.JkInit;

/**
 * @formatter:off
 */
class Build extends JkBuildDependencySupport {

    @Override
    public JkDependencies dependencies() {
        return JkDependencies.builder().build();
    }

    public static void main(String[] args) {
        JkInit.instanceOf(Build.class, args).doDefault();
    }

}