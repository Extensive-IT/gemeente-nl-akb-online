package gemeente.nlakbonline.controller;

import com.google.common.collect.Lists;
import gemeente.authorization.api.Account;
import gemeente.nlakbonline.controller.model.Registration;
import gemeente.nlakbonline.domain.AkbDonation;
import gemeente.nlakbonline.domain.AkbDonationId;
import gemeente.nlakbonline.service.AccountService;
import gemeente.nlakbonline.service.AkbService;
import gemeente.nlakbonline.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@Controller
public class AdminController {

    @Autowired
    private AkbService akbService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private ContentConfiguration contentConfiguration;

    @Value("${content.collection.year}")
    private Integer collectionYear;

    @GetMapping("/akb/admin/createRegistration")
    public String showCreateRegistrationForm(Map<String, Object> model) {
        model.put("page", createCreateRegistrationPage());
        model.put("registration", new Registration());
        accountService.getAccountInformation().ifPresent(account -> {
            model.put("account", account);
        });
        return "akb-admin-create-registration";
    }

    @GetMapping("/akb/admin")
    public String show(Map<String, Object> model) {
        model.put("page", createOverviewPage());
        model.put("donations", this.akbService.retrieveAkbDonations(collectionYear));
        accountService.getAccountInformation().ifPresent(account -> {
            model.put("account", account);
        });
        return "akb-admin-overview";
    }

    @PostMapping(value = "/akb/admin/createRegistration")
    public String processCreateRegistrationForm(@Valid @ModelAttribute final Registration registration, BindingResult bindingResult, Map<String, Object> model) {
        if (bindingResult.hasErrors()) {
            model.put("page", createCreateRegistrationPage());
            return "akb-admin-create-registration";
        }

        final Optional<Account> account = this.registrationService.register(extractRegistration(registration));
        account.ifPresent(createdAccount -> {
            if (registration.getPreviousYearAmount() != null) {
                final AkbDonationId akbDonationId = new AkbDonationId(createdAccount.getId(), collectionYear - 1);
                final AkbDonation akbDonation = new AkbDonation(akbDonationId, registration.getPreviousYearAmount(), registration.getPreviousYearPaymentType(), Lists.newArrayList());
                this.akbService.storeDonation(akbDonation);
            }
        });

        return "redirect:/akb/admin";
    }

    private gemeente.nlakbonline.domain.Registration extractRegistration(Registration registration) {
        final gemeente.nlakbonline.domain.Registration result = new gemeente.nlakbonline.domain.Registration();
        result.setRegistrationNumber(registration.getRegistrationNumber());
        result.setCity(registration.getCity());
        result.setCountry(registration.getCountry());
        result.setEmail(registration.getEmail());
        result.setStreet(registration.getStreet());
        result.setPostalCode(registration.getPostalCode());
        result.setSalutation(registration.getSalutation());
        return result;
    }

    private Page createCreateRegistrationPage() {
        final Page page = new Page();
        page.setTitle("Nieuwe registratie");
        page.setId("nieuwe-registratie");
        return page;
    }

    private Page createOverviewPage() {
        final Page page = new Page();
        page.setTitle("Toekenningen jaar " + collectionYear);
        page.setId("toekenningen-huidig-jaar");
        return page;
    }
}
