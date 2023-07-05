package kube;

import dev.jeka.core.tool.JkDoc;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Contains modifications to apply to k8s Resources.
 * Generally, we should have one patch per environment (staging, prod, ...).<br/>
 * Here we only define two, as the default resources stand for the local environment.
 */
class Patch {

    String namespace = "default";

    String imageRegistry = "localhost:5000";

    int replicaCount = 1;

    static Patch stating() {
        Patch patch = new Patch();
        patch.imageRegistry = "my.staging.registry:5000";
        patch.namespace = "knote-staging";
        patch.replicaCount = 2;
        return patch;
    }

    static Patch prod() {
        Patch patch = stating();
        patch.imageRegistry = "my.prod.registry:5000";
        patch.namespace = "knote-prod";
        return patch;
    }

    Resources resources() {
        Resources resources = new Resources();
        this.applyTo(resources);
        return resources;
    }

    void applyTo(Resources resources) {
        resources.setAppReplicaCount(replicaCount);
    }

}
