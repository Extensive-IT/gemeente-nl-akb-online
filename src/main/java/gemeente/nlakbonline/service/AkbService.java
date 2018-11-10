package gemeente.nlakbonline.service;

import gemeente.nlakbonline.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.Arrays;
import java.util.List;

@Service
public class AkbService {

    @Autowired
    private AuthenticationFacade authenticationFacade;

    public List<AkbDonation> retrieveAkbDonations() {
        final String userName = this.authenticationFacade.getAuthentication().getName();
        final AkbDonation akbDonation = new AkbDonation(userName, 2018, 500.00, Arrays.asList(Month.AUGUST, Month.DECEMBER));
        return Arrays.asList(akbDonation);
    }
}
