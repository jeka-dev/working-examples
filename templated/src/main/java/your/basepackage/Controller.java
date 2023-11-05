package your.basepackage;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class Controller {

    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello World";
    }

}


