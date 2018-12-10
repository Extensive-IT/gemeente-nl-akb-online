package gemeente.nlakbonline.controller;

import gemeente.nlakbonline.controller.model.Registration;
import gemeente.nlakbonline.service.AccountService;
import gemeente.nlakbonline.service.AkbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class AdminController {

    @Autowired
    private AkbService akbService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ContentConfiguration contentConfiguration;

    @GetMapping("/akb/admin/createRegistration")
    public String showCreateRegistrationForm(Map<String, Object> model) {
        model.put("page", createCreateRegistrationPage());
        model.put("registration", new Registration());
        return "akb-admin-create-registration";
    }

    @PostMapping(value = "/akb/admin/createRegistration")
    public String processFirstGivePage(@Valid @ModelAttribute final Registration registration, BindingResult bindingResult, Map<String, Object> model) {
        if (bindingResult.hasErrors()) {
            model.put("page", createCreateRegistrationPage());
            return "akb-admin-create-registration";
        }

        // accountService.getAccountInformation()

        accountService.getAccountInformation()
        return "";
    }

    private Page createCreateRegistrationPage() {
        final Page page = new Page();
        page.setTitle("Nieuwe registratie");
        page.setId("nieuwe-registratie");
        return page;
    }
}
