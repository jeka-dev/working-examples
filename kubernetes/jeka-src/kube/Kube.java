package kube;

import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.api.system.JkLog;
import dev.jeka.core.api.system.JkProcess;
import dev.jeka.core.tool.JkDoc;
import dev.jeka.core.tool.JkInjectClasspath;
import dev.jeka.core.tool.KBean;
import dev.jeka.core.tool.builtins.project.ProjectKBean;
import dev.jeka.plugins.springboot.JkSpringbootProject;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import kube.support.Fabric8Helper;
import lombok.RequiredArgsConstructor;

// see
// - https://learnk8s.io/spring-boot-kubernetes-guide
// - https://github.com/fabric8io/kubernetes-client
// - https://github.com/fabric8io/kubernetes-client/blob/master/doc/CHEATSHEET.md
// - https://github.com/GoogleContainerTools/jib/tree/master/jib-core

@JkInjectClasspath("org.projectlombok:lombok:1.18.26")

@JkInjectClasspath("com.google.cloud.tools:jib-core:0.23.0")
@JkInjectClasspath("io.fabric8:kubernetes-client:6.13.4")
@JkInjectClasspath("io.fabric8:kubernetes-client-api:6.13.4")
@JkInjectClasspath("org.slf4j:slf4j-simple:2.0.7")

@JkInjectClasspath("dev.jeka:springboot-plugin")
class Kube extends KBean {

    @RequiredArgsConstructor
    enum Target {

        LOCAL("default", Resources.ofLocal()),
        STAGING("staging", Resources.ofStaging()),
        PROD("prod", Resources.ofProd());

        final String namespace;

        final Resources resources;
    }

    private final JkProject project = load(ProjectKBean.class).project;

    @JkDoc("Kubernetes environment to deploy application")
    public Target target = Target.LOCAL;

    @JkDoc("The version of the application to build or deploy. " +
            "This is supposed to be injected by the CI tool and contain information as calendar and build number.")
    public String appVersion;

    @Override
    protected void init() {
        JkSpringbootProject.of(project)
                .includeParentBom("3.2.1")
                .configure();
        project.compilation.addJavaCompilerOptions("-parameters");
    }

    @JkDoc("Build and push the application container image. This assumes that application ahs already been built.")
    public void buildImage() throws Exception {
        appImage().build(project);
    }

    @JkDoc("Applies the defined resources to the Kubernetes cluster")
    public void apply()  {
        JkLog.startTask("Apply to kube");
        KubernetesClient client = client();
        Resources res = resources();
        Fabric8Helper.createNamespaceIfNotExist(client, target.namespace);
        Fabric8Helper.createResourcesIfNotExist(client, target.namespace, res.immutableResources());
        JkLog.info(Fabric8Helper.render(res.mutableResources()));
        client().resourceList(res.mutableResources()).inNamespace(target.namespace).createOrReplace();
        JkLog.endTask();
    }

    @JkDoc("Displays the defined Kubernetes resources to deploy")
    public void render() {
        System.out.println(Fabric8Helper.render(resources().mutableResources()));
        System.out.println(Fabric8Helper.render(resources().immutableResources()));
    }

    @JkDoc("Removes the defined resources from the Kubernetes cluster")
    public void delete() {
        Resources res = resources();
        client().resourceList(res.allResources()).inNamespace(target.namespace).delete();
    }

    @JkDoc("Builds the application + container image + apply to the Kubernetes cluster.")
    public void buildAndApply() throws Exception {
        project.clean().pack();
        buildImage();
        apply();
    }

    @JkDoc("Execute a port-forward in order the application is reachable from outside k8s.")
    public void portForward() {
        JkProcess.of("kubectl", "port-forward", "service/knote", "8080:" + resources().appPort());
    }

    private Resources resources() {
        return target.resources.setAppImageTag(this.appVersion);
    }

    private Image appImage() {
        return new Image(this.appVersion);
    }

    private KubernetesClient client() {
        return new KubernetesClientBuilder().build();
    }

}
