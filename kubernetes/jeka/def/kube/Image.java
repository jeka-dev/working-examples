package kube;

import com.google.cloud.tools.jib.api.*;
import dev.jeka.core.api.system.JkLog;
import dev.jeka.plugins.springboot.SpringbootJkBean;
import kube.support.JibHelper;
import lombok.Value;

/**
 * Contains logic to build and push container images.
 */
@Value
public class Image {

    static final String APP_IMAGE_REPO = "knote-java";

    static final String APP_IMAGE_REGISTRY = "localhost:5000";

    String appVersion;

    void build(SpringbootJkBean springbootBean) throws Exception {
        Containerizer containerizer = JibHelper.registryContainerizer(imageName());
        JibHelper.javaImage("openjdk:17", springbootBean).containerize(containerizer);;
        JkLog.info("Image pushed at " + imageName());
    }

    private String imageName() {
        return name()+ ":" + imageTag();
    }

    private String imageTag() {
        return appVersion == null ? "latest" : appVersion;
    }

    static String name() {
        return APP_IMAGE_REGISTRY + "/" + APP_IMAGE_REPO;
    }

}
