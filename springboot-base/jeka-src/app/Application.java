package app;

import dev.jeka.core.tool.JkDep;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@JkDep("org.springframework.boot:spring-boot-dependencies::pom:4.2.0") // declare BOM
@JkDep("org.springframework.boot:spring-boot-starter-webmvc")

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


}
