package kube.support;

import com.google.cloud.tools.jib.api.*;
import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.api.system.JkLog;
import dev.jeka.core.api.utils.JkUtilsString;
import dev.jeka.plugins.springboot.SpringbootJkBean;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.List;

import static java.util.Collections.singletonList;

public class JibHelper {

    public static JibContainerBuilder javaImage(String fromImage, SpringbootJkBean springbootJkBean) {
        return javaImage(fromImage, springbootJkBean.projectBean.getProject(), springbootJkBean.getMainClass());
    }

    public static JibContainerBuilder javaImage(String fromImage, JkProject javaProject, String mainClass) {
        JkLog.startTask("Resolve dependencies for creating java image classpath");
        List<Path> jarFiles = javaProject.packaging.resolveRuntimeDependencies().getFiles().getEntries();
        JkLog.endTask();
        return javaImage(fromImage, javaProject.compilation.layout.resolveClassDir(), jarFiles, mainClass);
    }

    public static JibContainerBuilder javaImage(String fromImage, Path classDir, List<Path> jarFiles, String mainClass)  {
        String libsPath = jarFiles.stream()
                .map(path -> "/app/libs/" + path.getFileName())
                .reduce("", (from, add) -> from + ":" +  add );
        try {
            return Jib.from(fromImage)
                    .addLayer(jarFiles, "/app/libs")
                    .addLayer(singletonList(classDir), "/app")
                    .setEntrypoint("java", "-cp", "/app/classes" + libsPath, mainClass);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (InvalidImageReferenceException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Containerizer registryContainerizer(String imageName) {
        RegistryImage image = registryImage(imageName);
        Containerizer containerizer = Containerizer.to(image).setAllowInsecureRegistries(true);
        return handleEvents(containerizer);
    }

    public static Containerizer registryContainerizer(String imageName,String username, String password) {
        RegistryImage image = registryImage(imageName).addCredential(username, password);
        Containerizer containerizer = Containerizer.to(image);
        return handleEvents(containerizer);
    }

    public static Containerizer dockerDeamonContainerizer(String imageName) {
        DockerDaemonImage dockerDaemonImage = dockerDaemonImage(imageName);
        return handleEvents(Containerizer.to(dockerDaemonImage));
    }

    public static Containerizer handleEvents(Containerizer containerizer) {
        containerizer.addEventHandler(LogEvent.class, event -> System.out.println
                (JkUtilsString.padEnd(event.getLevel().name() ,  12, ' ') + ": " + event.getMessage()));
        return containerizer;
    }

    public static DockerDaemonImage dockerDaemonImage(String name) {
        try {
            return DockerDaemonImage.named(name);
        } catch (InvalidImageReferenceException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static RegistryImage registryImage(String name) {
        try {
            return RegistryImage.named(name);
        } catch (InvalidImageReferenceException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
