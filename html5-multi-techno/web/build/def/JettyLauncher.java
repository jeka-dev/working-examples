import org.jerkar.tool.JkInit;


public class JettyLauncher {
	
	public static void main(String[] args) throws Exception {
		JkInit.instanceOf(WebBuild.class).jetty();
	}

}
