import org.jerkar.api.depmanagement.JkPopularModules;
import org.jerkar.api.depmanagement.JkVersion;
import org.jerkar.api.depmanagement.JkVersionProvider;


abstract class Common {
	
	public static final JkVersion VERSION = JkVersion.of("0.1-SNAPSHOT");
	
	public static final JkVersionProvider VERSION_PROVIDER =
			JkVersionProvider.of(JkPopularModules.GUAVA, "18.0");

}
