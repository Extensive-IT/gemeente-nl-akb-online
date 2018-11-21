package gemeente.nlakbonline.controller;

import gemeente.nlakbonline.controller.model.*;
import gemeente.nlakbonline.service.AccountService;
import gemeente.nlakbonline.service.AkbService;
import gemeente.nlakbonline.service.AutomaticPaymentInformationConfiguration;
import gemeente.nlakbonline.service.ManualPaymentInformationConfiguration;
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
import java.util.Map;

@Controller
@SessionAttributes({"akbDonationSession"})
public class DefaultController {

    @Autowired
    private AkbService akbService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ContentConfiguration contentConfiguration;

    @Autowired
    private AutomaticPaymentInformationConfiguration automaticPaymentInformationConfiguration;

    @Autowired
    private ManualPaymentInformationConfiguration manualPaymentInformationConfiguration;

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
    public String start(@ModelAttribute final AkbDonationSession akbDonationSession, Map<String, Object> model) {
        final AkbDonationStep1 akbDonationStep1 = akbDonationSession.getAkbDonationStep1();
        akbDonationStep1.setAkbPersonRegistration(createAkbPersonRegistration());
        return showFirstGivePage(akbDonationSession.getAkbDonationStep1(), model);
    }

    private String showFirstGivePage(final AkbDonationStep1 akbDonationStep1, Map<String, Object> model) {
        this.akbService.retrieveAkbDonations(2018).stream().findAny().ifPresent((akbDonation -> model.put("donation", akbDonation)));
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
        if (!akbDonationSession.isReadyForStep2()) {
            return "redirect:/akb/give";
        }

        final AkbDonationStep2 akbDonationStep2 = akbDonationSession.createOrReuseAkbDonationStep2();
        final String akbRegistrationNumber = "REG";
        akbDonationStep2.setPaymentInformation(createBankManualPaymentInformation(akbRegistrationNumber));
        model.put("akbDonationStep1", akbDonationSession.getAkbDonationStep1());
        model.put("akbDonationStep2", akbDonationStep2);
        model.put("page", contentConfiguration.getById("give-bank-transfer").get());
        return "akb-give-step-bank-transfer";
    }

    @RequestMapping(value = "/akb/give-step-bank-automatic", method = RequestMethod.GET)
    public String stepBankAutomatic(@ModelAttribute final AkbDonationSession akbDonationSession, Map<String, Object> model) {
        if (!akbDonationSession.isReadyForStep2()) {
            return "redirect:/akb/give";
        }

        final Page page = contentConfiguration.getById("give-bank-automatic").get();
        final String akbRegistrationNumber = "REG";
        final String paymentReason = page.getTitle();

        final AkbDonationStep2 akbDonationStep2 = akbDonationSession.createOrReuseAkbDonationStep2();
        akbDonationStep2.setPaymentInformation(createBankAutomaticPaymentInformation(akbRegistrationNumber, paymentReason));
        model.put("akbDonationStep1", akbDonationSession.getAkbDonationStep1());
        model.put("akbDonationStep2", akbDonationStep2);
        model.put("page", page);
        return "akb-give-step-bank-automatic";
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
                    return stepBankTransfer(akbDonationSession, model);
                case BANK_AUTOMATIC:
                    return stepBankAutomatic(akbDonationSession, model);
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

    AkbPersonRegistration createAkbPersonRegistration() {
        final AkbPersonRegistration result = new AkbPersonRegistration();
        final String result2 = (String)accountService.getAccountInformation();
        return result;
    }

    BankAutomaticPaymentInformation createBankAutomaticPaymentInformation(final String mandateReference, final String paymentReason) {
        final BankAutomaticPaymentInformation result = new BankAutomaticPaymentInformation();
        result.setCreditorId(automaticPaymentInformationConfiguration.getCreditorId());
        result.setCreditorName(automaticPaymentInformationConfiguration.getCreditorName());
        result.setDayOfMonth(automaticPaymentInformationConfiguration.getDayOfMonth());
        result.setMandateReference(automaticPaymentInformationConfiguration.getMandateReferencePrefix() + mandateReference + automaticPaymentInformationConfiguration.getMandateReferencePostfix());
        result.setPaymentReason(paymentReason);
        return result;
    }

    BankManualPaymentInformation createBankManualPaymentInformation(final String reference) {
        final BankManualPaymentInformation result = new BankManualPaymentInformation();
        result.setBankAccountNumber(manualPaymentInformationConfiguration.getBankAccountNumber());
        result.setCreditorName(manualPaymentInformationConfiguration.getCreditorName());
        result.setReference(reference);
        return result;
    }
}
