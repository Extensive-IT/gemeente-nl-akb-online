package gemeente.nlakbonline.controller;

import gemeente.authorization.api.Account;
import gemeente.nlakbonline.controller.model.*;
import gemeente.nlakbonline.domain.AkbDonation;
import gemeente.nlakbonline.domain.AkbDonationId;
import gemeente.nlakbonline.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    private EmailService emailService;

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

    @Value("${content.collection.year}")
    private Integer collectionYear;

    @GetMapping({"/", "/akb/"})
    public String index(Map<String, Object> model) {
        return welcome(model);
    }

    @GetMapping("/akb/welcome")
    public String welcome(Map<String, Object> model) {
        model.put("page", contentConfiguration.getById("welcome").get());
        return "akb-welcome";
    }

    @GetMapping("/akb/give")
    public String start(@ModelAttribute final AkbDonationSession akbDonationSession, Map<String, Object> model) {
        final Optional<Account> accountOptional = this.accountService.getAccountInformation();
        final List<AkbDonation> donations =
                this.akbService.retrieveAkbDonations(accountOptional.get().getId().toString(), this.collectionYear);
        if (donations.size() > 0) {
            return "redirect:/akb/already-finalized";
        }

        final AkbDonationStep1 akbDonationStep1 = akbDonationSession.getAkbDonationStep1();
        akbDonationStep1.setAkbPersonRegistration(createAkbPersonRegistration(accountOptional));
        return showFirstGivePage(akbDonationSession.getAkbDonationStep1(), model);
    }

    private String showFirstGivePage(final AkbDonationStep1 akbDonationStep1, Map<String, Object> model) {
        this.akbService.retrieveAkbDonations(this.accountService.getAccountInformation().get().getId().toString(), this.collectionYear - 1).stream().findAny().ifPresent((akbDonation -> model.put("donation", akbDonation)));
        model.put("page", contentConfiguration.getById("give").get());
        model.put("akbDonationStep1", akbDonationStep1);
        accountService.getAccountInformation().ifPresent(account -> {
            model.put("account", account);
        });
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
    @PostMapping(value = "/akb/give-step1")
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

    @GetMapping(value = "/akb/give-step-bank-transfer")
    public String stepBankTransfer(@ModelAttribute final AkbDonationSession akbDonationSession, Map<String, Object> model) {
        if (!akbDonationSession.isReadyForStep2()) {
            return "redirect:/akb/give";
        }

        final AkbDonationStep2 akbDonationStep2 = akbDonationSession.createOrReuseAkbDonationStep2();
        final Optional<Account> accountOptional = this.accountService.getAccountInformation();
        final AkbPersonRegistration akbPersonRegistration = createAkbPersonRegistration(accountOptional);
        akbDonationSession.getAkbDonationStep1().setAkbPersonRegistration(akbPersonRegistration);
        akbDonationStep2.setPaymentInformation(createBankManualPaymentInformation(akbPersonRegistration.getRegistrationNumber()));
        model.put("akbDonationStep1", akbDonationSession.getAkbDonationStep1());
        model.put("akbDonationStep2", akbDonationStep2);
        model.put("page", contentConfiguration.getById("give-bank-transfer").get());
        accountOptional.ifPresent(account -> {
            model.put("account", account);
        });
        return "akb-give-step-bank-transfer";
    }

    @GetMapping(value = "/akb/give-step-bank-automatic")
    public String stepBankAutomatic(@ModelAttribute final AkbDonationSession akbDonationSession, Map<String, Object> model) {
        if (!akbDonationSession.isReadyForStep2()) {
            return "redirect:/akb/give";
        }

        final Page page = contentConfiguration.getById("give-bank-automatic").get();
        final String paymentReason = page.getTitle();

        final AkbDonationStep2 akbDonationStep2 = akbDonationSession.createOrReuseAkbDonationStep2();
        final Optional<Account> accountOptional = this.accountService.getAccountInformation();
        final AkbPersonRegistration akbPersonRegistration = createAkbPersonRegistration(accountOptional);
        akbDonationSession.getAkbDonationStep1().setAkbPersonRegistration(akbPersonRegistration);
        akbDonationStep2.setPaymentInformation(createBankAutomaticPaymentInformation(akbPersonRegistration.getRegistrationNumber(), paymentReason));
        model.put("akbDonationStep1", akbDonationSession.getAkbDonationStep1());
        model.put("akbDonationStep2", akbDonationStep2);
        model.put("page", page);
        accountOptional.ifPresent(account -> {
            model.put("account", account);
        });
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
    @PostMapping(value = "/akb/give-step2")
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
        final Optional<Account> accountOptional = this.accountService.getAccountInformation();
        final AkbDonation akbDonation = createAkbDonation(akbDonationSession, accountOptional);
        this.akbService.storeDonation(akbDonation);

        // E-mail confirmation
        final Account account = this.accountService.getAccountInformation().get();
        this.emailService.sendConfirmation(account, akbDonation);

        // Reset session
        akbDonationSession.reset();
        return "redirect:/akb/thanks";
    }

    @GetMapping("/akb/thanks")
    public String showThanksPage(Map<String, Object> model) {
        final Optional<Account> accountOptional = this.accountService.getAccountInformation();
        accountOptional.ifPresent(account -> {
            model.put("account", account);
        });
        model.put("page", contentConfiguration.getById("thanks").get());
        final List<AkbDonation> donations =
                this.akbService.retrieveAkbDonations(accountOptional.get().getId().toString(), this.collectionYear);
        if (!donations.isEmpty()) {
            model.put("donation", donations.get(0));
            model.put("manualPaymentInformation", this.manualPaymentInformationConfiguration);
            model.put("akbPersonRegistration", createAkbPersonRegistration(accountOptional));
        }
        return "akb-thanks";
    }

    @GetMapping("/akb/already-finalized")
    public String finalized(Map<String, Object> model) {
        final Optional<Account> accountOptional = this.accountService.getAccountInformation();
        accountOptional.ifPresent(account -> {
            model.put("account", account);
        });
        model.put("page", contentConfiguration.getById("finalized").get());
        final List<AkbDonation> donations =
                this.akbService.retrieveAkbDonations(accountOptional.get().getId().toString(), this.collectionYear);
        model.put("donations", donations);
        return "akb-finalized";
    }

    @ModelAttribute(value = "akbDonationSession")
    public AkbDonationSession createAkbDonationSession() {
        return new AkbDonationSession();
    }

    @RequestMapping("/user")
    public Principal user(Principal principal) {
        return principal;
    }

    AkbPersonRegistration createAkbPersonRegistration(final Optional<Account> accountOptional) {
        final AkbPersonRegistration result = new AkbPersonRegistration();
        accountOptional.ifPresent(account -> {
            result.setAddress(account.getAddress().getStreet());
            result.setPostalCode(account.getAddress().getPostalCode());
            result.setCity(account.getAddress().getCity());
            result.setRegistrationNumber(account.getRegistrationReferenceId());
            result.setSalutation(account.getFullName());
        });
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

    AkbDonation createAkbDonation(final AkbDonationSession akbDonationSession, final Optional<Account> accountOptional) {
        if (accountOptional.isPresent()) {
            final AkbDonationId id = new AkbDonationId(accountOptional.get().getId(), collectionYear);
            final AkbDonation akbDonation = new AkbDonation(id, akbDonationSession.getAkbDonationStep1().getAmount(), akbDonationSession.getAkbDonationStep1().getPaymentType(), akbDonationSession.getAkbDonationStep1().getPaymentMonths());
            return akbDonation;
        }
        throw new IllegalStateException("Cannot get account");
    }
}
