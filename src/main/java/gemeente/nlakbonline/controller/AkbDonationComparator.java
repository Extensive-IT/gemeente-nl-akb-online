package gemeente.nlakbonline.controller;

import gemeente.authorization.api.Account;
import gemeente.nlakbonline.domain.AkbDonation;

import java.util.Comparator;
import java.util.Map;
import java.util.UUID;

public class AkbDonationComparator implements Comparator<AkbDonation> {
    private final Map<UUID, Account> registrations;

    public AkbDonationComparator(Map<UUID, Account> registrations) {
        this.registrations = registrations;
    }

    @Override
    public int compare(AkbDonation d1, AkbDonation d2) {
        final Account firstAccount = registrations.get(d1.getId().getAccountId());
        final Account secondAccount = registrations.get(d2.getId().getAccountId());
        if (firstAccount != null && secondAccount != null) {
            if (firstAccount.getRegistrationReferenceId() != null && secondAccount.getRegistrationReferenceId() != null) {
                return firstAccount.getRegistrationReferenceId().compareTo(secondAccount.getRegistrationReferenceId());
            }
        }
        return 0;
    }
}
