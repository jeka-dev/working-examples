package kube;

import com.google.common.collect.Streams;
import dev.jeka.core.api.utils.JkUtilsString;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.client.utils.Serialization;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.yaml.snakeyaml.Yaml;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Object model of the Kubernetes resources for this project.
 * The model segregates resources that are mutable (updatable) from the ones that can not be updayed.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class Resources {

    static Resources ofDefault() {
        return new Resources().setAppImageFullName("localhost:5000/" + Images.APP_IMAGE_REPO + ":latest");
    }

    static Resources ofStaging() {
        Resources resources = ofDefault().setAppImageRegistry("my.official.registry:5000");
        resources.appDeployment.getSpec().setReplicas(2);
        return resources;
    }

    static Resources ofProd() {
        Resources resources = ofStaging();
        resources.appDeployment.getSpec().setReplicas(4);
        return resources;
    }

    private Deployment appDeployment = parse(Deployment.class, "app-deployment.yaml");

    private Service appService = parse(Service.class, "app-service.yaml");

    private Deployment dbDeployment = parse(Deployment.class, "db-deployment.yaml");

    private Service dbService = parse(Service.class, "db-service.yaml");

    private PersistentVolumeClaim pvc = parse(PersistentVolumeClaim.class, "db-volumeClaim.yaml");

    List<HasMetadata> mutableResources() {
        return List.of(appDeployment, appService, dbDeployment, dbService);
    }

    List<HasMetadata> immutableResources() {
        return List.of(pvc);
    }

    List<HasMetadata> allResources() {
        return Streams.concat(immutableResources().stream(), mutableResources().stream()).toList();
    }

    // --------- setters

    private Resources setAppImageFullName(String imageFullName) {
        appDeployment.getSpec().getTemplate().getSpec().getContainers().get(0).setImage(imageFullName);
        return this;
    }

    private String fullImageName() {
        return appDeployment.getSpec().getTemplate().getSpec().getContainers().get(0).getImage();
    }

    private Resources setAppImageRegistry(String registry) {
        String result = registry + "/" + JkUtilsString.substringAfterFirst(fullImageName(), "/");
        appDeployment.getSpec().getTemplate().getSpec().getContainers().get(0).setImage(result);
        return this;
    }

    Resources setAppImageVersion(String version) {
        String result =  JkUtilsString.substringBeforeLast(fullImageName(), ":") + ":" + version;
        appDeployment.getSpec().getTemplate().getSpec().getContainers().get(0).setImage(result);
        return this;
    }

    String getImageRegistry() {
      return JkUtilsString.substringBeforeFirst(fullImageName(), "/");
    }

    // -------

    String renderMutableResources() {
        return render(mutableResources());
    }

    String renderImmutableResources() {
        return render(immutableResources());
    }

    private static <T> T parse(Class<T> targetClass, String resourceName) {
        return new Yaml().loadAs(Kube.class.getResourceAsStream(resourceName), targetClass);
    }

    private static List<EnvVar> toEnvVars(Map<String, String> values) {
        return values.entrySet().stream()
                .map(entry -> new EnvVarBuilder().addToAdditionalProperties(entry.getKey(), entry.getValue()).build())
                .collect(Collectors.toList());
    }

    static String render(List<?> resources) {
        StringBuilder sb = new StringBuilder();
        resources.forEach(res -> sb.append(Serialization.asYaml(res)));
        return sb.toString();
    }

}
