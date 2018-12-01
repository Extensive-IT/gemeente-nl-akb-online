package gemeente.nlakbonline.domain;

import java.util.StringJoiner;

public class Donation {
    private final double amount;

    private final PaymentType paymentType;

    public Donation(final double amount, final PaymentType paymentType) {
        this.amount = amount;
        this.paymentType = paymentType;
    }

    public double getAmount() {
        return this.amount;
    }

    public PaymentType getPaymentType() {
        return this.paymentType;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Donation.class.getSimpleName() + "[", "]")
                .add("amount=" + amount)
                .toString();
    }
}
