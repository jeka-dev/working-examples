package kube.support;

import dev.jeka.core.api.system.JkLog;
import dev.jeka.core.api.utils.JkUtilsString;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * Generic helper class to create or modify Fabric8 objects conveniently;
 */
public class Fabric8Helper {

    /**
     * @param registry New registry for the image. If <code>null</code> or empty, the registry prefix will
     *                 be removed from the image name.
     */
    public static void changeImageRegistry(Container container, String registry) {
        String fullName = container.getImage();
        boolean hasRegistry = fullName.contains("/");
        final String newFullName;
        if (hasRegistry) {
            String withoutRegistry = JkUtilsString.substringAfterFirst(fullName, "/");
            newFullName = JkUtilsString.isBlank(registry) ? withoutRegistry : registry + "/" + withoutRegistry;
        } else {
            newFullName = JkUtilsString.isBlank(registry) ? fullName : registry + "/" + fullName;
        }
        container.setImage(newFullName);
    }

    /**
     * @param tag New tag for the image. If <code>null</code> or empty, the tag suffix will be removed
     *            from the image name.
     */
    public static void changeImageTag(Container container, String tag) {
        String fullName = container.getImage();
        boolean hasRegistry = fullName.contains("/");
        final String withouTag;
        if (hasRegistry) {
            String withoutRegistry = JkUtilsString.substringAfterFirst(fullName, "/");
            withouTag = withoutRegistry.contains(":") ? JkUtilsString.substringBeforeLast(fullName, ":")
                    : fullName;
        } else {
            withouTag = fullName.contains(":") ? JkUtilsString.substringBeforeLast(fullName, ":")
                    : fullName;
        }
        String newFullName = JkUtilsString.isBlank(tag) ? withouTag : withouTag + ":" + tag;
        container.setImage(newFullName);
    }

    /**
     * Adds or replace the specified environment variable on the container
     * @param value if <code>null</code> the environment variable will be removed.
     */
    public static void setEnvVar(Container container, String key, String value) {
        boolean found = false;
        for (ListIterator<EnvVar> it = container.getEnv().listIterator(); it.hasNext() && !found;) {
            EnvVar envVar = it.next();
            if (key.equals(envVar.getName())) {
                found = true;
                if (value == null) {
                    it.remove();
                } else {
                    it.set(new EnvVar(key, value, null));
                }
            }
        }
        if (!found && value != null) {
            container.getEnv().add(new EnvVar(key, value, null));
        }
    }

    public static Namespace createNamespaceIfNotExist(KubernetesClient client, String namespace) {
        Namespace existing = client.namespaces().withName(namespace).get();
        if (existing != null) {
            return existing;
        }
        JkLog.info("Create namespace %s", namespace);
        return client.namespaces().create(new NamespaceBuilder()
                .withNewMetadata()
                    .withName(namespace)
                .endMetadata()
                .build());
    }

    /**
     * Creates a service to expose the port of the first container defined in the specified deployment.
     * @see #serviceFor(Deployment, int, int, int)
     */
    public static Service serviceFor(Deployment deployment, int port) {
        return serviceFor(deployment, 0, 0, port);
    }

    /**
     * Creates a service to expose a port of a container defined in the specified deployment.
     * @param containerIndex The zero-based index to select which container within the specified deployment
     * @param portIndex The zero-based index to select which port within the specified container
     * @param port the port that will be visible for client. This is not the container port.
     */
    public static Service serviceFor(Deployment deployment, int containerIndex, int portIndex, int port) {
        String metadataName = deployment.getMetadata().getName();
        Map<String, String> matchLabels = deployment.getSpec().getSelector().getMatchLabels();
        int targetPort = deployment.getSpec().getTemplate().getSpec().getContainers().get(containerIndex)
                .getPorts().get(portIndex).getContainerPort();
        return new ServiceBuilder()
                .withNewMetadata()
                .withName(metadataName)
                .endMetadata()
                .withNewSpec()
                .withSelector(matchLabels)
                .withPorts(new ServicePortBuilder()
                        .withPort(port)
                        .withTargetPort(new IntOrString(targetPort))
                        .build())
                .withType("LoadBalancer")
                .endSpec()
                .build();
    }

    /**
     * Creates a basic deployment with a unique container.
     * @param name Name of the deployment. It is also used to name the container, and add a
     *             label having 'app' key.
     * @param image full name image used by the container
     * @param containerPort port exposed by the container
     * @param createAppNameLabels if true, creates a label 'app' = ${name} for <i>matchLabels</i> and <i>templateLabels</i>
     */
    public static Deployment basicDeployment(String name, String image, int containerPort, boolean createAppNameLabels) {
        System.out.println("__________________________________________");
        Deployment deployment = parse(Deployment.class,
                Fabric8Helper.class.getResourceAsStream("deployment-template.yaml"));
        Map<String, String> labels = new HashMap<>();
        if (createAppNameLabels) {
            labels.put("app", name);
        }
        deployment.getMetadata().setName(name);
        DeploymentSpec spec = deployment.getSpec();
        spec.getSelector().setMatchLabels(new HashMap<>(labels));
        PodTemplateSpec template = spec.getTemplate();
        template.getMetadata().setLabels(new HashMap<>(labels));
        Container container = template.getSpec().getContainers().get(0);
        container.setName(name);
        container.setImage(image);
        container.setImagePullPolicy("Always");
        ContainerPort containerPort0 = container.getPorts().get(0);
        containerPort0.setContainerPort(containerPort);
        return deployment;
    }

    public static <T> T parse(Class<T> targetClass, InputStream inputStream) {
        return new Yaml().loadAs(inputStream , targetClass);
    }

    /**
     * Provides a yaml representation of the specified kubernetes resources.
     * @param resources list of resources to render as a yaml string. This is the representation that will bbe
     *                  sent to K8S api when deploying.
     */
    public static String render(List<?> resources) {
        StringBuilder sb = new StringBuilder();
        resources.forEach(res -> sb.append(Serialization.asYaml(res)));
        return sb.toString();
    }

    /**
     * Creates resources from the specified list if they are not existing in the target namespace.
     * Resources are tested and created one by one.
     */
    public static void createResourcesIfNotExist(KubernetesClient client, String namespace, List<HasMetadata> resources) {
        for (HasMetadata resource : resources) {
            var serverRes = client.resource(resource).inNamespace(namespace);
            if (serverRes.get() == null) {
                System.out.println(render(List.of(resource)));
                serverRes.create();
            }
        }
    }

    public static Probe addSpringbootActuatorReadiness(Container container) {
        Probe probe = new ProbeBuilder()
                        .editOrNewHttpGet()
                            .withPath("/actuator/health/readiness")
                            .withPort(new IntOrString(container.getPorts().get(0).getContainerPort()))
                        .endHttpGet()
                .build();
        container.setReadinessProbe(probe);
        return probe;
    }

    public static Probe addSpringbootActuatorLiveness(Container container) {
        Probe probe = new ProbeBuilder()
                .editOrNewHttpGet()
                    .withPath("/actuator/health/liveness")
                    .withPort(new IntOrString(container.getPorts().get(0).getContainerPort()))
                .endHttpGet()
                .build();
        container.setReadinessProbe(probe);
        return probe;
    }

    public static Container firstContainer(Deployment deployment) {
        return deployment.getSpec().getTemplate().getSpec().getContainers().get(0);
    }

    public static VolumeMount volumeMount(String name, String mountPath) {
        return new VolumeMountBuilder()
                .withName("storage")
                .withMountPath("/data/db").build();
    }

    public static Volume refToPersistentVolumeClaim(String name, HasMetadata claim) {
        return new VolumeBuilder()
                .withName(name)
                .editOrNewPersistentVolumeClaim()
                .withClaimName(claim.getMetadata().getName())
                .endPersistentVolumeClaim()
                .build();
    }

    public static PersistentVolumeClaim persistenceVolumeClaim(String name,
                                                               Map<String, Quantity> requests, String... accessModes) {
        return new PersistentVolumeClaimBuilder()
                .editOrNewMetadata()
                    .withName(name)
                .endMetadata()
                .editOrNewSpec()
                    .withAccessModes(accessModes)
                    .editOrNewResources()
                        .withRequests(requests)
                    .endResources()
                .endSpec()
                .build();
    }
}
