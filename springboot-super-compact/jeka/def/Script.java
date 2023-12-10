import app.Application;
import dev.jeka.core.api.java.JkClassLoader;
import dev.jeka.core.api.java.JkJavaProcess;
import dev.jeka.core.api.testing.JkTestProcessor;
import dev.jeka.core.api.testing.JkTestSelection;
import dev.jeka.core.tool.JkBean;
import dev.jeka.core.tool.JkConstants;
import dev.jeka.core.tool.JkDoc;

import java.nio.file.Paths;

class Script extends JkBean {

    // We can not just run Application#main cause Spring-Boot seems
    // requiring that the Java process specifies Spring-Boot Application.class as the main class.
    @JkDoc("Launch Spring-Boot application")
    public void run() {
        JkJavaProcess.ofJava(Application.class.getName())
                .setClasspath(JkClassLoader.ofCurrent().getClasspath())
                .setInheritIO(true)
                .setInheritSystemProperties(true)
                .setDestroyAtJvmShutdown(true)
                .exec();  // exec() is blocking. This method ends when the sub-process terminates.
    }

    @JkDoc("Launch test suite")
    public void test() {
        JkTestSelection testSelection = JkTestSelection.of().addTestClassRoots(Paths.get(JkConstants.DEF_BIN_DIR));
        JkTestProcessor.of()
                .setForkingProcess(true)
                .launch(JkClassLoader.ofCurrent().getClasspath(), testSelection);
    }

}