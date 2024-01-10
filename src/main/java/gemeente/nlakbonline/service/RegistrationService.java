package gemeente.nlakbonline.service;

import gemeente.authorization.api.*;
import gemeente.nlakbonline.domain.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RegistrationService {

    @Value("${account.service.uri}")
    private String accountServiceUri;

    @Autowired
    private OAuth2RestTemplate restOperations;

    /**
     * Register the person in the account service, so that he/she has an account.
     *
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

    public Optional<Account> upsert(final Registration registration) {
        final String uri = createAccountServiceUri("import");
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

    /**
     * Retrieve all account registrations available
     *
     * @param
     * @return list off all accounts
     */
    public List<Account> retrieveAllRegistrations() {
        final String uri = createAccountServiceUri("registrations");

        final ResponseEntity<Accounts> result = restOperations.getForEntity(uri, Accounts.class);
        return result.getBody().getAccounts();
    }

    public Map<UUID, Account> retrieveAllRegistrationsMap() {
        final List<Account> accounts = this.retrieveAllRegistrations();
        final Map<UUID, Account> results = new HashMap<>();
        accounts.stream().forEach((account -> {
            results.put(account.getId(), account);
        }));
        return results;
    }

    private String createAccountServiceUri(final String action) {
        return this.accountServiceUri + "/" + action;
    }
}
