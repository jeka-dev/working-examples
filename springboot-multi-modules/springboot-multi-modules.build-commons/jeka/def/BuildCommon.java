import dev.jeka.core.api.depmanagement.JkPopularLibs;
import dev.jeka.core.api.depmanagement.JkVersionProvider;
import dev.jeka.core.tool.KBean;
import dev.jeka.core.tool.builtins.tooling.ide.IntellijKBean;

/*
 * This class is reusable in any Jeka project def importing this project
 */
public class BuildCommon extends KBean {

    public static final JkVersionProvider VERSION_PROVIDER =
            JkVersionProvider.of(JkPopularLibs.GUAVA, "22.0");

    BuildCommon() {
        load(IntellijKBean.class).useJekaDefinedInModule("wrapper-common");
    }

}
