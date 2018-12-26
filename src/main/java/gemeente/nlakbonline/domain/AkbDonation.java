package gemeente.nlakbonline.domain;

import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;

public class AkbDonation extends Donation {

    private final AkbDonationId id;

    private List<Month> paymentMonths;

    public AkbDonation(final AkbDonationId id, final double amount, final PaymentType paymentType, final Collection<Month> paymentsMonths) {
        super(amount, paymentType);
        this.id = id;
        this.paymentMonths = new ArrayList<>(paymentsMonths);
    }

    public AkbDonationId getId() {
        return this.id;
    }

    public List<Month> getPaymentMonths() {
        return this.paymentMonths;
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
