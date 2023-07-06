package kube;

import com.google.cloud.tools.jib.api.*;
import dev.jeka.core.api.depmanagement.JkVersion;
import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.api.system.JkLog;
import dev.jeka.core.api.utils.JkUtilsString;
import dev.jeka.plugins.springboot.SpringbootJkBean;
import lombok.Value;

import static java.util.Collections.singletonList;

/**
 * Contains logic to build and push container images.
 */
@Value
public class Images {

    static final String APP_IMAGE_REPO= "knote-java";

    SpringbootJkBean springbootBean;

    String imageRegistry;

    void buildImage() throws Exception {
        JkLog.info("Building image " + imageName());
        Containerizer containerizer = registryContainerizer(imageName(), true);
        handleEvents(containerizer);
        springbootImage("openjdk:17", springbootBean).containerize(containerizer);;
        JkLog.info("Image pushed at " + imageName());

    }

    String imageName() {
        return imageRegistry + "/" + APP_IMAGE_REPO + ":" + imageTag();
    }

    String imageTag() {
        JkVersion version = springbootBean.projectBean.getProject().publication.getVersion();
        return version.isSnapshot() ? "latest" : version.toString();
    }

    // ---- helper methods that can be exported to a generic module ---

    private static JibContainerBuilder springbootImage(String fromImage, SpringbootJkBean springbootBean) throws Exception {
        JkProject project = springbootBean.projectBean.getProject();
        return Jib.from(fromImage)
                .addLayer(project.packaging.resolveRuntimeDependencies().getFiles().getEntries(), "/app/libs")
                .addLayer(singletonList(project.compilation.layout.resolveClassDir()), "/app")
                .setEntrypoint("java", "-cp", "/app/classes:/app/libs/*", springbootBean.getMainClass());
    }

    private static Containerizer registryContainerizer(String imageName, boolean allowHttp) throws InvalidImageReferenceException {
        RegistryImage image = RegistryImage.named(imageName);
        return Containerizer.to(image).setAllowInsecureRegistries(allowHttp);
    }

    private static Containerizer dockerDeamonContainerizer(String imageName) throws InvalidImageReferenceException {
        DockerDaemonImage dockerDaemonImage = DockerDaemonImage.named(imageName);
        return Containerizer.to(dockerDaemonImage);
    }

    private void handleEvents(Containerizer containerizer) {
        containerizer.addEventHandler(LogEvent.class, event -> System.out.println
                (JkUtilsString.padEnd(event.getLevel().name() ,  12, ' ') + ": " + event.getMessage()));
    }

}
