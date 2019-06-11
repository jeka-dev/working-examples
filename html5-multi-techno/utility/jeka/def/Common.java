import dev.jeka.core.api.depmanagement.JkPopularModules;
import dev.jeka.core.api.depmanagement.JkVersion;
import dev.jeka.core.api.depmanagement.JkVersionProvider;

abstract class Common {
	
	public static final JkVersion VERSION = JkVersion.of("0.1-SNAPSHOT");
	
	public static final JkVersionProvider VERSION_PROVIDER =
			JkVersionProvider.of(JkPopularModules.GUAVA, "18.0");

}
