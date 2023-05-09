package kube;

import com.google.cloud.tools.jib.api.*;
import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.api.system.JkLog;
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
        Containerizer containerizer = registryContainerizer(imageRegistry, true);
        springbootImage("openjdk:17", springbootBean).containerize(containerizer);
    }

    String imageName() {
        return imageRegistry + "/" + APP_IMAGE_REPO + ":" + springbootBean.projectBean.getProject()
                .publication.getVersion().toString();
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

}
