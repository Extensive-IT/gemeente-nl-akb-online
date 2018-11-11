package gemeente.nlakbonline.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Month;
import java.util.List;
import java.util.StringJoiner;

/**
 * Represents the {@link gemeente.nlakbonline.domain.AkbDonation} in the database
 */
@Table("akb_donation")
public class AkbDonationDatabaseObject {

    @Id
    private final Integer id;
    private final String user;
    private final int year;
    private final List<Month> paymentMonths;
    private final double amount;

    static AkbDonationDatabaseObject of(String user, int year, double amount, List<Month> paymentMonths) {
        return new AkbDonationDatabaseObject(null, user, year, amount,
                paymentMonths);
    }

    AkbDonationDatabaseObject(final Integer id, final String user, final int year, final double amount, final List<Month> paymentMonths) {
        this.id = id;
        this.user = user;
        this.year = year;
        this.amount = amount;
        this.paymentMonths = paymentMonths;
    }

    AkbDonationDatabaseObject withId(Integer id) {
        return new AkbDonationDatabaseObject(id, this.user, this.year, this.amount, this.paymentMonths);
    }

    public Integer getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public int getYear() {
        return year;
    }

    public List<Month> getPaymentMonths() {
        return paymentMonths;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AkbDonationDatabaseObject.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("user=" + user)
                .add("paymentMonths=" + paymentMonths)
                .add("amount=" + amount)
                .toString();
    }
}
