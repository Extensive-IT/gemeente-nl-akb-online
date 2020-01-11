package gemeente.nlakbonline.controller;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.common.collect.Lists;
import gemeente.authorization.api.Account;
import gemeente.nlakbonline.controller.model.ImportRegistrations;
import gemeente.nlakbonline.controller.model.ImportRegistrationsRecord;
import gemeente.nlakbonline.controller.model.Registration;
import gemeente.nlakbonline.domain.AkbDonation;
import gemeente.nlakbonline.domain.AkbDonationId;
import gemeente.nlakbonline.domain.PaymentType;
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
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.io.InputStream;
import java.util.List;
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

    @GetMapping("/akb/admin/importRegistrations")
    public String showImportRegistrationsForm(Map<String, Object> model) {
        model.put("page", createImportRegistrationsPage());
        model.put("registrations", new ImportRegistrations());
        accountService.getAccountInformation().ifPresent(account -> {
            model.put("account", account);
        });
        return "akb-admin-upload-registrations";
    }

    @PostMapping(value = "/akb/admin/importRegistrations")
    public String processImportRegistrationsForm(@Valid @ModelAttribute final ImportRegistrations registrations, BindingResult bindingResult, Map<String, Object> model) {
        if (bindingResult.hasErrors()) {
            model.put("page", createImportRegistrationsPage());
            return "akb-admin-upload-registrations";
        }

        try (InputStream inputStream = registrations.getFile().getInputStream()) {
            List<ImportRegistrationsRecord> records = loadObjectList(ImportRegistrationsRecord.class, inputStream);
            records.forEach(registrationRecord -> {
                final Optional<Account> account = this.registrationService.register(extractRegistration(registrationRecord));
                account.ifPresent(createdAccount -> {
                    if (registrationRecord.getPreviousYearAmount() != null) {
                        final AkbDonationId akbDonationId = new AkbDonationId(createdAccount.getId(), collectionYear - 1);
                        final PaymentType paymentType = registrationRecord.getPreviousYearPaymentType() != null ? registrationRecord.getPreviousYearPaymentType() : PaymentType.BANK_TRANSFER;
                        final AkbDonation akbDonation = new AkbDonation(akbDonationId, registrationRecord.getPreviousYearAmount(), paymentType, Lists.newArrayList());
                        this.akbService.storeDonation(akbDonation);
                    }
                });
            });
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        return "redirect:/akb/admin";
    }

    @GetMapping("/akb/admin")
    public String show(Map<String, Object> model, @RequestParam(value = "year", required = false) Integer year) {
        if (year == null || year > collectionYear) {
            year = collectionYear;
        }

        model.put("page", createOverviewPage(year));
        model.put("donations", this.akbService.retrieveAkbDonations(year));
        model.put("registrations", this.registrationService.retrieveAllRegistrationsMap());
        accountService.getAccountInformation().ifPresent(account -> {
            model.put("account", account);
        });
        return "akb-admin-overview";
    }

    @GetMapping("/akb/admin/registrations")
    public String showRegistrations(Map<String, Object> model) {
        model.put("page", createRegistrationsOverviewPage());
        model.put("registrations", this.registrationService.retrieveAllRegistrations());
        accountService.getAccountInformation().ifPresent(account -> {
            model.put("account", account);
        });
        return "akb-admin-registrations-overview";
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
                final List<AkbDonation> existingDonations = this.akbService.retrieveAkbDonations(createdAccount.getId().toString(), collectionYear - 1);
                if (existingDonations.isEmpty()) {
                    final AkbDonationId akbDonationId = new AkbDonationId(createdAccount.getId(), collectionYear - 1);
                    final AkbDonation akbDonation = new AkbDonation(akbDonationId, registration.getPreviousYearAmount(), registration.getPreviousYearPaymentType(), Lists.newArrayList());
                    this.akbService.storeDonation(akbDonation);
                }
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

    private gemeente.nlakbonline.domain.Registration extractRegistration(ImportRegistrationsRecord registration) {
        final gemeente.nlakbonline.domain.Registration result = new gemeente.nlakbonline.domain.Registration();
        result.setRegistrationNumber(registration.getRegistrationNumber());
        result.setCity(registration.getCity());
        result.setCountry(registration.getCountry());
        result.setEmail(registration.getEmail());
        result.setStreet(registration.getStreet() + " " + registration.getNumber() + "" + registration.getAddition());
        result.setPostalCode(registration.getPostalCode());
        result.setSalutation(registration.getSalutation());
        return result;
    }

    <T> List<T> loadObjectList(Class<T> type, InputStream inputStream) {
        try {
            CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
            CsvMapper mapper = new CsvMapper();
            MappingIterator<T> readValues =
                    mapper.readerFor(type).with(bootstrapSchema).readValues(inputStream);
            return readValues.readAll();
        } catch (Exception e) {
            throw new RuntimeException("Cannot load file, due: " + e.getMessage(), e);
        }
    }

    private Page createCreateRegistrationPage() {
        final Page page = new Page();
        page.setTitle("Nieuwe registratie");
        page.setId("nieuwe-registratie");
        return page;
    }

    private Page createImportRegistrationsPage() {
        final Page page = new Page();
        page.setTitle("Importeer nieuwe registraties");
        page.setId("import-registratie");
        return page;
    }

    private Page createOverviewPage(Integer year) {
        final Page page = new Page();
        page.setTitle("Toezeggingen jaar " + year);
        page.setId("toezeggingen-huidig-jaar");
        return page;
    }

    private Page createRegistrationsOverviewPage() {
        final Page page = new Page();
        page.setTitle("Registraties");
        page.setId("registraties-overzicht");
        return page;
    }
}
