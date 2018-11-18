package gemeente.nlakbonline.controller;

import gemeente.nlakbonline.controller.model.AkbDonationSession;
import gemeente.nlakbonline.controller.model.AkbDonationStep1;
import gemeente.nlakbonline.controller.model.AkbDonationStep2;
import gemeente.nlakbonline.domain.AkbDonation;
import gemeente.nlakbonline.service.AkbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@SessionAttributes({"akbDonationSession"})
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
    public String showFirstGivePage(@ModelAttribute final AkbDonationSession akbDonationSession, Map<String, Object> model) {
        final List<AkbDonation> donations = this.akbService.retrieveAkbDonations();
        donations.addAll(this.akbService.retrieveAkbDonations(2018));
        model.put("donations", donations);

        return showFirstGivePage(akbDonationSession.getAkbDonationStep1(), model);
    }

    private String showFirstGivePage(final AkbDonationStep1 akbDonationStep1, Map<String, Object> model) {
        model.put("page", contentConfiguration.getById("give").get());
        model.put("akbDonationStep1", akbDonationStep1);
        return "akb-give-step1";
    }
    /**
     * Store first form in session and redirect user to next step
     *
     * @param akbDonationStep1
     * @param akbDonationSession
     * @param model
     * @return
     */
    @RequestMapping(value = "/akb/give-step1", method = RequestMethod.POST)
    public String processFirstGivePage(@Valid @ModelAttribute final AkbDonationStep1 akbDonationStep1, BindingResult bindingResult, @ModelAttribute final AkbDonationSession akbDonationSession, Map<String, Object> model) {
        if (bindingResult.hasErrors()) {
            return showFirstGivePage(akbDonationStep1, model);
        }

        akbDonationSession.setAkbDonationStep1(akbDonationStep1);
        switch (akbDonationStep1.getPaymentType()) {
            case BANK_TRANSFER:
                return "redirect:/akb/give-step-bank-transfer";
            case BANK_AUTOMATIC:
                return "redirect:/akb/give-step-bank-automatic";
            default:
                throw new IllegalArgumentException("Unknown payment type");
        }
    }

    @RequestMapping(value = "/akb/give-step-bank-transfer", method = RequestMethod.GET)
    public String stepBankTransfer(@ModelAttribute final AkbDonationSession akbDonationSession, Map<String, Object> model) {
        model.put("akbDonationStep2", akbDonationSession.createOrReuseAkbDonationStep2());
        model.put("page", contentConfiguration.getById("give-bank-transfer").get());
        return "akb-give-step-bank-transfer";
    }

    /**
     * Store second form in session and redirect user to overview
     *
     * @param akbDonationStep2
     * @param akbDonationSession
     * @param model
     * @return
     */
    @RequestMapping(value = "/akb/give-step2", method = RequestMethod.POST)
    public String stepFinal(@Valid @ModelAttribute final AkbDonationStep2 akbDonationStep2, BindingResult bindingResult, @ModelAttribute final AkbDonationSession akbDonationSession, Map<String, Object> model) {
        akbDonationSession.setAkbDonationStep2(akbDonationStep2);
        if (bindingResult.hasErrors()) {
            switch (akbDonationSession.getAkbDonationStep1().getPaymentType()) {
                case BANK_TRANSFER:
                    return "akb-give-step-bank-transfer";
                case BANK_AUTOMATIC:
                    return "akb-give-step-bank-automatic";
                default:
                    throw new IllegalArgumentException("Unknown payment type");
            }
        }

        // Store donation

        return "redirect:/akb/welcome";
    }

    @ModelAttribute(value = "akbDonationSession")
    public AkbDonationSession createAkbDonationSession() {
        return new AkbDonationSession();
    }

    @RequestMapping("/user")
    public Principal user(Principal principal) {
        return principal;
    }
}
