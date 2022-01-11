import dev.jeka.core.api.depmanagement.JkPopularModules;
import dev.jeka.core.api.depmanagement.JkVersionProvider;
import dev.jeka.core.api.java.JkJavaVersion;
import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.tool.JkBean;
import dev.jeka.core.tool.builtins.ide.IntellijJkBean;

/*
 * This class is reusable in any Jeka project def importing this project
 */
public class BuildCommon extends JkBean {

    public static final JkVersionProvider VERSION_PROVIDER =
            JkVersionProvider.of(JkPopularModules.GUAVA, "22.0");

    BuildCommon() {
        getBean(IntellijJkBean.class).useJekaDefinedInModule("wrapper-common");
    }

    public static void setup(JkProject project) {
        project.simpleFacade().setJvmTargetVersion(JkJavaVersion.V8);
    }

}
