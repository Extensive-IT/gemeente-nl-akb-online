package gemeente.nlakbonline.domain;

import java.util.StringJoiner;

public class Donation {
    private final double amount;

    public Donation(final double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return this.amount;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Donation.class.getSimpleName() + "[", "]")
                .add("amount=" + amount)
                .toString();
    }
}
