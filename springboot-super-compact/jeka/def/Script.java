import app.Application;
import dev.jeka.core.api.file.JkPathFile;
import dev.jeka.core.api.file.JkPathTree;
import dev.jeka.core.api.java.JkClassLoader;
import dev.jeka.core.api.java.JkJavaProcess;
import dev.jeka.core.api.system.JkProcess;
import dev.jeka.core.api.testing.JkTestProcessor;
import dev.jeka.core.api.testing.JkTestSelection;
import dev.jeka.core.api.utils.JkUtilsString;
import dev.jeka.core.tool.JkBean;
import dev.jeka.core.tool.JkConstants;
import dev.jeka.core.tool.JkDoc;

import java.nio.file.Files;
import java.nio.file.Path;
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

    @JkDoc("Create a docker image")
    public void buildImage() {
        cleanOutput();
        String dockerBuildTemplate = """
                FROM eclipse-temurin:17-jdk-jammy
                WORKDIR /app
                COPY libs /app/libs
                COPY classes /app/classes
                CMD ["java", "-cp", "/app/classes:/app/libs/*", "${mainClass}"]
                """;
        String dockerBuild = dockerBuildTemplate.replace("${mainClass}", Application.class.getName());
        Path dockerBuildDir = Paths.get(JkConstants.OUTPUT_PATH).resolve("docker");
        JkPathFile.of(dockerBuildDir.resolve("Dockerfile")).write(dockerBuild);

        // Copy compiled classes to docker/classes (exclude tests and this class)
        JkPathTree.of(Paths.get(JkConstants.DEF_BIN_DIR)).andMatching("app/**")
                .copyTo(dockerBuildDir.resolve("classes"));

        // Copy libs
        JkClassLoader.ofCurrent().getClasspath().getEntries().stream()
                .filter(path -> Files.isRegularFile(path))
                .map(JkPathFile::of)
                .forEach(file -> file.copyToDir(dockerBuildDir.resolve("libs")));

        // Build image using Docker daemon
        JkProcess.of("docker", "build", "-t", imageName(), "./" + dockerBuildDir).setInheritIO(true)
                .setLogCommand(true).exec();
    }

    @JkDoc("Run docker image")
    public void runImage() {
        JkProcess.ofCmdLine(String.format("docker run -t --rm -p 8080:8080 --name %s %s", imageName(), imageName()))
                .setLogCommand(true).setInheritIO(true).exec();
    }

    @JkDoc("Stop docker image")
    public void stopImage() {
        JkProcess.ofCmdLine("docker rm -f " + imageName()).setLogCommand(true).setInheritIO(true).exec();
    }

    private String imageName() {
        return getBaseDirName();
    }

}