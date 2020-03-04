package springboot;

import core.MyCore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @RequestMapping("/formula")
    public int greeting(@RequestParam(value="input") int input) {
        MyCore core = new MyCore();
        int result = core.magicFormula(input);
        return result;
    }

}
