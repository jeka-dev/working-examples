package kube;

import com.google.common.collect.Streams;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import kube.support.Fabric8Helper;

import java.util.List;
import java.util.Map;

/**
 * Object model of the Kubernetes resources for this project.
 * The model segregates resources that are mutable (updatable) from the ones that can not be updated.
 */
class Resources {

    private final PersistentVolumeClaim persistentVolumeClaim;

    private final Deployment appDeployment;

    private final Service appService;

    private final Deployment mongoDeployment;

    private final Service mongoService ;

    private Resources() {

        persistentVolumeClaim = Fabric8Helper.persistenceVolumeClaim("mongo-pvc",
                Map.of("storage", new Quantity("256Mi")), "ReadWriteOnce");

        appDeployment = Fabric8Helper.basicDeployment("knote", Image.name(), 8080, true );
        Probe readinessProbe = Fabric8Helper.addSpringbootActuatorReadiness(appContainer());
        readinessProbe.setPeriodSeconds(3);
        Fabric8Helper.setEnvVar(appContainer(), "MONGO_URL", "mongodb://mongo:27017/dev");

        appService = Fabric8Helper.serviceFor(appDeployment, 80);

        mongoDeployment = Fabric8Helper.basicDeployment("mongo", "mongo", 27017, true);
        Container mongoContainer = Fabric8Helper.firstContainer(mongoDeployment);
        String volumeName = "storage";
        mongoContainer.getVolumeMounts().add(Fabric8Helper.volumeMount(volumeName, "/data/db"));
        mongoDeployment.getSpec().getTemplate().getSpec().getVolumes().add(
                Fabric8Helper.refToPersistentVolumeClaim(volumeName, persistentVolumeClaim));

        mongoService = Fabric8Helper.serviceFor(mongoDeployment, 27017);
    }

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
        resources.storageQuantity("512Mi");
        return resources;
    }

    static Resources ofProd() {
        Resources resources = ofStaging();
        resources.springProfilesActive("prod" );
        resources.storageQuantity("4Gi");
        return resources;
    }

    // These resources can be applied only once.
    List<HasMetadata> immutableResources() {
        return List.of(persistentVolumeClaim);
    }

    List<HasMetadata> mutableResources() {
        return List.of(appDeployment, appService, mongoDeployment, mongoService);
    }

    List<HasMetadata> allResources() {
        return Streams.concat(immutableResources().stream(), mutableResources().stream()).toList();
    }

    Resources setAppImageTag(String tag) {
        Fabric8Helper.changeImageTag(appContainer(), tag);
        return this;
    }

    private Container appContainer() {
        return appDeployment.getSpec().getTemplate().getSpec().getContainers().get(0);
    }

    private void springProfilesActive(String profiles) {
        Fabric8Helper.setEnvVar(appContainer(), "SPRING_PROFILES_ACTIVE", profiles );
    }

    private void storageQuantity(String quantity) {
        persistentVolumeClaim.getSpec().getResources().getRequests().put("storage", new Quantity(quantity));
    }

}
