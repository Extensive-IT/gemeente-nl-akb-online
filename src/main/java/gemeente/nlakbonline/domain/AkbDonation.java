package gemeente.nlakbonline.domain;

import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;

public class AkbDonation extends Donation {

    private final AkbDonationId id;

    private List<Month> paymentMonths;

    public AkbDonation(final AkbDonationId id, final double amount, final Collection<Month> paymentsMonths) {
        super(amount);
        this.id = id;
        this.paymentMonths = new ArrayList<>(paymentsMonths);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AkbDonation.class.getSimpleName() + "[", "]")
                .add("id=" + id.toString())
                .add("paymentMonths=" + paymentMonths)
                .add("base=" + super.toString())
                .toString();
    }
}
