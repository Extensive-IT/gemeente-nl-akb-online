package gemeente.nlakbonline.service;

import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class AkbDonation {
    private String user;
    private int year;
    private double totalAmount;
    private List<Month> paymentMonths;

    public AkbDonation(final String user, final int year, final double totalAmount, final Collection<Month> paymentsMonths) {
        this.user = Objects.requireNonNull(user);
        this.year = year;
        this.totalAmount = totalAmount;
        this.paymentMonths = new ArrayList<Month>(paymentsMonths);
    }

    @Override
    public String toString() {
        return "AkbDonation{" +
                "user='" + user + '\'' +
                ", year=" + year +
                ", totalAmount=" + totalAmount +
                ", paymentMonths=" + paymentMonths +
                '}';
    }
}
