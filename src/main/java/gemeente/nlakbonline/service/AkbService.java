package gemeente.nlakbonline.service;

import gemeente.nlakbonline.AuthenticationFacade;
import gemeente.nlakbonline.domain.AkbDonation;
import gemeente.nlakbonline.domain.AkbDonationId;
import gemeente.nlakbonline.domain.PaymentType;
import gemeente.nlakbonline.repository.AkbDonationDatabaseObject;
import gemeente.nlakbonline.repository.AkbDonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static gemeente.nlakbonline.repository.AkbDonationDatabaseObject.convertPaymentMonths;

@Service
public class AkbService {

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private AkbDonationRepository akbDonationRepository;

    public List<AkbDonation> retrieveAkbDonations(String accountId) {
        final List<AkbDonation> result = new ArrayList<>();
        akbDonationRepository.findByUser(accountId).forEach(item -> result.add(from(item)));
        return result;
    }

    public List<AkbDonation> retrieveAkbDonations(String accountId, int year) {
        final List<AkbDonation> result = new ArrayList<>();
        akbDonationRepository.findByUserAndYear(accountId, year).forEach(item -> result.add(from(item)));
        return result;
    }

    public List<AkbDonation> retrieveAkbDonations(int year) {
        final List<AkbDonation> result = new ArrayList<>();
        akbDonationRepository.findByYear(year).forEach(item -> result.add(from(item)));
        return result;
    }

    public void storeDonation(final AkbDonation akbDonation) {
        akbDonationRepository.save(from(akbDonation));
    }

    private static AkbDonation from(final AkbDonationDatabaseObject databaseObject) {
        final AkbDonationId id = new AkbDonationId(UUID.fromString(databaseObject.getAccountId()), databaseObject.getYear());
        return new AkbDonation(id, databaseObject.getAmount(), PaymentType.valueOf(databaseObject.getPaymentType()), convertPaymentMonths(databaseObject.getPaymentMonths()));
    }

    private static AkbDonationDatabaseObject from(final AkbDonation akbDonation) {
        return AkbDonationDatabaseObject.of(akbDonation.getId().getAccountId().toString(), akbDonation.getId().getYear(), akbDonation.getAmount(), akbDonation.getPaymentType().toString(), convertPaymentMonths(akbDonation.getPaymentMonths()), LocalDateTime.now());
    }
}
