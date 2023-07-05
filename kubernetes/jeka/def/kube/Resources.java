package kube;

import com.google.common.collect.Streams;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.yaml.snakeyaml.Yaml;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Model of the Kubernetes resources for this project.
 * The model segregates resources that are mutable (updatable) from the ones that can not be updayed.
 */
class Resources {

    Deployment appDeployment = parse(Deployment.class, "app-deployment.yaml");

    Service appService = parse(Service.class, "app-service.yaml");

    Deployment dbDeployment = parse(Deployment.class, "db-deployment.yaml");

    Service dbService = parse(Service.class, "db-service.yaml");

    PersistentVolumeClaim pvc = parse(PersistentVolumeClaim.class, "db-volumeClaim.yaml");

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

    Resources setAppImageName(String fullImageName) {
        appDeployment = new DeploymentBuilder(appDeployment)
                .editSpec().editTemplate().editSpec().editContainer(0).withImage(fullImageName)
                        .endContainer().endSpec().endTemplate().endSpec().build();
        return this;
    }

    Resources setAppReplicaCount(int count) {
        appDeployment = new DeploymentBuilder(appDeployment).editSpec().withReplicas(count)
                .endSpec().build();
        return this;
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

    private static List<EnvVar> envVars(Map<String, String> values) {
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
