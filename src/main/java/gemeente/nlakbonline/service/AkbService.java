package gemeente.nlakbonline.service;

import com.google.common.collect.Lists;
import gemeente.nlakbonline.AuthenticationFacade;
import gemeente.nlakbonline.domain.AkbDonation;
import gemeente.nlakbonline.domain.AkbDonationId;
import gemeente.nlakbonline.repository.AkbDonationDatabaseObject;
import gemeente.nlakbonline.repository.AkbDonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AkbService {

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private AkbDonationRepository akbDonationRepository;

    public List<AkbDonation> retrieveAkbDonations() {
        final String userName = this.authenticationFacade.getAuthentication().getName();
        final List<AkbDonation> result = new ArrayList<>();
        akbDonationRepository.findByUser(userName).forEach(item -> result.add(from(item)));
        return result;
    }

    public List<AkbDonation> retrieveAkbDonations(int year) {
        final String userName = this.authenticationFacade.getAuthentication().getName();
        final List<AkbDonation> result = new ArrayList<>();
        akbDonationRepository.findByUserAndYear(userName, year).forEach(item -> result.add(from(item)));
        return result;
    }

    private static AkbDonation from(final AkbDonationDatabaseObject databaseObject) {
        final AkbDonationId id = new AkbDonationId(databaseObject.getUser(), databaseObject.getYear());
        return new AkbDonation(id, databaseObject.getAmount(), databaseObject.getPaymentMonths());
    }
}
