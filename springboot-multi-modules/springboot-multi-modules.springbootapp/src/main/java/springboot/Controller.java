package springboot;

import core.MyCore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/formula")
    public int greeting(@RequestParam(value="input") int input) {
        System.out.println("Formula requested with input " + input);
        MyCore core = new MyCore();
        int result = core.magicFormula(input);
        return result;
    }

}
