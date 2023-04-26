package kube;

import dev.jeka.core.api.system.JkLog;
import dev.jeka.core.api.system.JkProcess;
import dev.jeka.core.tool.JkBean;
import dev.jeka.core.tool.JkDoc;
import dev.jeka.core.tool.JkInit;
import dev.jeka.core.tool.JkInjectClasspath;
import dev.jeka.plugins.springboot.SpringbootJkBean;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

// see
// - https://learnk8s.io/spring-boot-kubernetes-guide
// - https://github.com/fabric8io/kubernetes-client
// - https://github.com/fabric8io/kubernetes-client/blob/master/doc/CHEATSHEET.md

@JkInjectClasspath("com.google.cloud.tools:jib-core:0.23.0")
@JkInjectClasspath("io.fabric8:kubernetes-client:6.5.1")
@JkInjectClasspath("io.fabric8:kubernetes-client-api:6.5.1")
@JkInjectClasspath("org.slf4j:slf4j-simple:2.0.7")
@JkInjectClasspath("org.projectlombok:lombok:1.18.26")
class Kube extends JkBean {

    @RequiredArgsConstructor
    enum Env {

        LOCAL(Patch::new), STAGING(Patch::stating), PROD(Patch::prod);

        final Supplier<Patch> patcher;
    }

    private final SpringbootJkBean springboot = getBean(SpringbootJkBean.class);

    public Env env = Env.LOCAL;

    @JkDoc("Build and push the application container image. This assumes that application ahs already been built.")
    public void buildImage() {
        images().buildImage();
    }

    @JkDoc("Applies the defined resources to the Kubernetes cluster")
    public void apply()  {
        JkLog.startTask("Apply to kube");
        Resources res = resources();
        for (HasMetadata immutableResource : res.immutableResources()) {
            var serverRes = client().resource(immutableResource).inNamespace(patch().namespace);
            if (serverRes.get() == null) {
                serverRes.create();
            }
        }
        System.out.println(res.renderMutableResources());
        client().resourceList(res.mutableResources()).inNamespace(patch().namespace).createOrReplace();
        JkLog.endTask();
    }

    @JkDoc("Displays the defined Kubernetes resources to deploy")
    public void showResources() {
        System.out.println(resources().allResources());
    }

    @JkDoc("Removes the defined resources from the Kubernetes cluster")
    public void delete() {
        Resources res = resources();
        client().resourceList(res.allResources()).inNamespace(patch().namespace).delete();
    }

    @JkDoc("Builds the application + container image + apply to the Kubernetes cluster.")
    public void pipeline()  {
        springboot.projectBean.clean();
        springboot.projectBean.test();
        buildImage();
        apply();
    }

    @JkDoc("Execute a port-forward in order the application is reachable from outside k8s.")
    public void portForward() {
        JkProcess.of("kubectl", "port-forward", "deployment/knote", "8080:8080").exec();
    }

    private Patch patch() {
        return env.patcher.get();
    }

    private Resources resources() {
        return patch().resources()
                .setAppImageName(images().imageName());
    }

    private Images images() {
        return new Images(this.springboot, patch().imageRegistry);
    }

    private KubernetesClient client() {
        return new KubernetesClientBuilder().build();
    }

    // Convenient if you don't have Jeka intellij plugin
    public static void main(String[] args) {
        JkInit.instanceOf(Kube.class).apply();
    }

}
