package gemeente.nlakbonline.service;

import gemeente.authorization.api.Account;
import gemeente.authorization.api.AccountCreationRequest;
import gemeente.authorization.api.AccountInformationResponse;
import gemeente.authorization.api.Address;
import gemeente.nlakbonline.domain.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegistrationService {

    @Value("${account.service.uri}")
    private String accountServiceUri;

    @Autowired
    private OAuth2RestTemplate restOperations;

    /**
     * Register the person in the account service, so that he/she has an account.
     * @param registration
     * @return
     */
    public Optional<Account> register(final Registration registration) {
        final String uri = createAccountServiceUri("create");
        final AccountCreationRequest request = new AccountCreationRequest();
        request.setRegistrationReferenceId(registration.getRegistrationNumber());
        request.setEmailAddress(registration.getEmail());
        request.setFullName(registration.getSalutation());

        final Address address = new Address();
        address.setStreet(registration.getStreet());
        address.setPostalCode(registration.getPostalCode());
        address.setCity(registration.getCity());
        address.setCountry(registration.getCountry());
        request.setAddress(address);

        final ResponseEntity<AccountInformationResponse> result = restOperations.postForEntity(uri, request, AccountInformationResponse.class);
        return Optional.ofNullable(result.getBody().getAccount());
    }

    private String createAccountServiceUri(final String action) {
        return this.accountServiceUri + "/" + action;
    }
}
