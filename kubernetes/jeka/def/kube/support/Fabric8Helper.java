package kube.support;

import dev.jeka.core.api.utils.JkUtilsString;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.util.ListIterator;


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
        return existing != null ? existing : client.namespaces().create(new NamespaceBuilder()
                .withNewMetadata()
                    .withName(namespace)
                .endMetadata()
                .build());
    }
}
