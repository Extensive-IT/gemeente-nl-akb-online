package gemeente.nlakbonline.service;

import gemeente.authorization.api.Account;
import gemeente.authorization.api.AccountInformationResponse;
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
        final AccountInformationResponse result = restOperations.getForObject(accountServiceUri, AccountInformationResponse.class);
        return Optional.ofNullable(result.getAccount());
    }

    public Optional<Account> createAccount() {
        //restOperations.postForEntity()
        return Optional.empty();
    }
}
