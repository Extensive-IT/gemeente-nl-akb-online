package gemeente.nlakbonline.controller;

import gemeente.nlakbonline.domain.AkbDonation;
import gemeente.nlakbonline.service.AkbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
public class DefaultController {

    @Autowired
    private AkbService akbService;

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
        final List<AkbDonation> donations = this.akbService.retrieveAkbDonations();
        donations.addAll(this.akbService.retrieveAkbDonations(2018));
        model.put("donations", donations);
        return "akb-view";
    }

    @RequestMapping("/user")
    public Principal user(Principal principal) {
        return principal;
    }
}
