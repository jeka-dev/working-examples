package kube;

import com.google.cloud.tools.jib.api.Containerizer;
import com.google.cloud.tools.jib.api.Jib;
import com.google.cloud.tools.jib.api.JibContainerBuilder;
import com.google.cloud.tools.jib.api.RegistryImage;
import dev.jeka.core.api.project.JkProject;
import dev.jeka.plugins.springboot.SpringbootJkBean;
import lombok.SneakyThrows;
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

    @SneakyThrows
    void buildImage() {
        Containerizer containerizer = registryContainerizer(imageRegistry, true);
        springbootImage("openjdk:17", springbootBean).containerize(containerizer);
    }

    String imageName() {
        return imageRegistry + "/" + APP_IMAGE_REPO + ":" + springbootBean.projectBean.getProject()
                .publication.getVersion().toString();
    }

    // ---- helper methods that can be exported to a generic module ---

    @SneakyThrows
    private static JibContainerBuilder springbootImage(String fromImage, SpringbootJkBean springbootBean) {
        JkProject project = springbootBean.projectBean.getProject();
        return Jib.from(fromImage)
                .addLayer(project.packaging.resolveRuntimeDependencies().getFiles().getEntries(), "/app/libs")
                .addLayer(singletonList(project.compilation.layout.resolveClassDir()), "/app")
                .setEntrypoint("java", "-cp", "/app/classes:/app/libs/*", springbootBean.getMainClass());
    }

    @SneakyThrows
    private static Containerizer registryContainerizer(String imageName, boolean allowHttp) {
        RegistryImage image = RegistryImage.named(imageName);
        return Containerizer.to(image).setAllowInsecureRegistries(true);
    }
}
