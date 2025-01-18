import dev.jeka.core.api.depmanagement.JkPopularLibs;
import dev.jeka.core.api.depmanagement.JkVersionProvider;
import dev.jeka.core.tool.KBean;

/*
 * This class is reusable in any Jeka project def importing this project
 */
public class BuildCommon extends KBean {

    public static final JkVersionProvider VERSION_PROVIDER =
            JkVersionProvider.of(JkPopularLibs.GUAVA, "22.0");

}
