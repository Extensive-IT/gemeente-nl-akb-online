package gemeente.nlakbonline.service;

import gemeente.authorization.api.Account;
import gemeente.authorization.api.AccountCreationRequest;
import gemeente.authorization.api.AccountInformationResponse;
import gemeente.authorization.api.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Value("${account.service.uri}")
    private String accountServiceUri;

    @Autowired
    private OAuth2RestTemplate restOperations;

    public Optional<Account> getAccountInformation() {
        final String uri = createAccountServiceUri("me");
        final AccountInformationResponse result = restOperations.getForObject(uri, AccountInformationResponse.class);
        return Optional.ofNullable(result.getAccount());
    }

    private String createAccountServiceUri(final String action) {
        return this.accountServiceUri + "/" + action;
    }
}
