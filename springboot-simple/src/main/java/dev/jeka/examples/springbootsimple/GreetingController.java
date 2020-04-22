package dev.jeka.examples.springbootsimple;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class GreetingController {

    @GetMapping("/")
    String helloWorld() {
        return "Hello Word";
    }

}


