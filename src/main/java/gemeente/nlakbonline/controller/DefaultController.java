package gemeente.nlakbonline.controller;

import gemeente.nlakbonline.domain.AkbDonation;
import gemeente.nlakbonline.service.AkbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
public class DefaultController {

    @Autowired
    private AkbService akbService;

    @Autowired
    private ContentConfiguration contentConfiguration;

    @Value("${content.default.footer:}")
    private String footer = "";

    @Value("${content.akb.title:AKB}")
    private String akbTitle = "";

    @Value("${content.default.title:Welcome}")
    private String defaultTitle = "";

    @RequestMapping("/")
    public String index(Map<String, Object> model) {
        return welcome(model);
    }

    @RequestMapping("/akb/welcome")
    public String welcome(Map<String, Object> model) {
        model.put("page", contentConfiguration.getById("welcome").get());
        return "akb-welcome";
    }

    @RequestMapping("/akb/give")
    public String give(Map<String, Object> model) {
        model.put("page", contentConfiguration.getById("give").get());
        final List<AkbDonation> donations = this.akbService.retrieveAkbDonations();
        donations.addAll(this.akbService.retrieveAkbDonations(2018));
        model.put("donations", donations);
        return "akb-give-step1";
    }

    @RequestMapping(value = "/akb/give", method = RequestMethod.POST)
    public String step1(Map<String, Object> model) {
        model.put("page", contentConfiguration.getById("give").get());
        final List<AkbDonation> donations = this.akbService.retrieveAkbDonations();
        donations.addAll(this.akbService.retrieveAkbDonations(2018));
        model.put("donations", donations);
        return "akb-give-step2";
    }

    @RequestMapping("/user")
    public Principal user(Principal principal) {
        return principal;
    }
}
