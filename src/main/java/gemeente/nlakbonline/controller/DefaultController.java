package gemeente.nlakbonline.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Map;

@Controller
public class DefaultController {

    // inject via application.yml
    @Value("${welcome.message:test}")
    private String message = "Hello World";

    @RequestMapping("/")
    public String welcome(Map<String, Object> model) {
        model.put("message", this.message);
        return "welcome";
    }

    @RequestMapping("/akb-view")
    public String showCurrentAkb(Map<String, Object> model) {
        model.put("message", this.message);
        return "akb-view";
    }

    @RequestMapping("/user")
    public Principal user(Principal principal) {
        return principal;
    }
}
