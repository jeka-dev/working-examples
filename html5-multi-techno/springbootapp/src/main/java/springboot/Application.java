package springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@SpringBootApplication
public class Application implements WebMvcConfigurer {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    private final Environment env;

    Application(Environment environment) {
        this.env = environment;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path webDist = Paths.get("../web/dist");
        if (Arrays.asList(env.getActiveProfiles()).contains("dev")) {
            if (!Files.exists(webDist)) {
                webDist = Paths.get("webDist");
            }
            if (Files.exists(webDist) && Arrays.asList(env.getActiveProfiles()).contains("dev")) {
                LOG.info("Adding {} to static resources", webDist);
                registry.addResourceHandler("/**").addResourceLocations("file:" + webDist);
            }
        }
    }

}
