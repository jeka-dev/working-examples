package app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Bean
    public CommandLineRunner run(CoffeeShopRepository repository) {
        return args -> {
            repository.save(new CoffeeShopModel(
                    "Oblique",
                    "3039 SE Stark St, Portland, OR 97214",
                    "555-111-4444",
                    1.50,
                    true,
                    5
            ));
            repository.save(new CoffeeShopModel(
                    "Epoch Coffee",
                    "221 W N Loop Blvd, Austin, TX 78751",
                    "555-111-2424",
                    2.50,
                    true,
                    3
            ));
        };
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Open me http://localhost:8080");
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}