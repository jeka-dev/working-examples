package springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class Application implements WebMvcConfigurer {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path webDist = Paths.get("../web/dist");
        if (!Files.exists(webDist)) {
            webDist = Paths.get("webDist");
        }
        if (Files.exists(webDist)) {
            //LOG.info("Adding {} to static resources", webDist);
            //registry.addResourceHandler("/**").addResourceLocations("file:" + webDist);
        }
    }

}
