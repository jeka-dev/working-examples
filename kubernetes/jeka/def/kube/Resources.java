package kube;

import com.google.common.collect.Streams;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.utils.Serialization;
import kube.support.Fabric8Helper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.yaml.snakeyaml.Yaml;

import java.util.List;

/**
 * Object model of the Kubernetes resources for this project.
 * The model segregates resources that are mutable (updatable) from the ones that can not be updated.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class Resources {

    static Resources ofLocal() {
        Resources resources = new Resources();
        resources.appContainer().setImage(Image.APP_IMAGE_REGISTRY + "/" + Image.APP_IMAGE_REPO + ":latest");
        resources.springProfilesActive("local");
        return resources;
    }

    static Resources ofStaging() {
        Resources resources = ofLocal();
        resources.appDeployment.getSpec().setReplicas(2);
        resources.springProfilesActive("staging" );
        return resources;
    }

    static Resources ofProd() {
        Resources resources = ofStaging();
        resources.springProfilesActive("prod" );
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

    Resources setAppImageTag(String tag) {
        Fabric8Helper.changeImageTag(appContainer(), tag);
        return this;
    }

    String renderMutableResources() {
        return render(mutableResources());
    }

    String renderImmutableResources() {
        return render(immutableResources());
    }

    private Container appContainer() {
        return appDeployment.getSpec().getTemplate().getSpec().getContainers().get(0);
    }

    private void springProfilesActive(String profiles) {
        Fabric8Helper.setEnvVar(appContainer(), "SPRING_PROFILES_ACTIVE", profiles );
    }

    static String render(List<?> resources) {
        StringBuilder sb = new StringBuilder();
        resources.forEach(res -> sb.append(Serialization.asYaml(res)));
        return sb.toString();
    }

    private static <T> T parse(Class<T> targetClass, String resourceName) {
        return new Yaml().loadAs(Kube.class.getResourceAsStream(resourceName), targetClass);
    }

}
