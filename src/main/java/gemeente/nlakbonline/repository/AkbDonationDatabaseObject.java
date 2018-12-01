package gemeente.nlakbonline.repository;

import com.google.common.collect.Lists;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.util.CollectionUtils;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * Represents the {@link gemeente.nlakbonline.domain.AkbDonation} in the database
 */
@Table("akb_donation")
public class AkbDonationDatabaseObject {

    @Id
    @Column("id")
    private final Integer id;

    @Column("account_id")
    private final String accountId;

    @Column("year")
    private final int year;

    @Column("payment_months")
    private final String paymentMonths;

    @Column("payment_type")
    private final String paymentType;

    @Column("amount")
    private final double amount;

    public static AkbDonationDatabaseObject of(String accountId, int year, double amount, String paymentType, String paymentMonths) {
        return new AkbDonationDatabaseObject(null, accountId, year, amount,
                paymentType, paymentMonths);
    }

    AkbDonationDatabaseObject(final Integer id, final String accountId, final int year, final double amount, final String paymentType, final String paymentMonths) {
        this.id = id;
        this.accountId = accountId;
        this.year = year;
        this.amount = amount;
        this.paymentType = paymentType;
        this.paymentMonths = paymentMonths;
    }

    AkbDonationDatabaseObject withId(Integer id) {
        return new AkbDonationDatabaseObject(id, this.accountId, this.year, this.amount, this.paymentType, this.paymentMonths);
    }

    public Integer getId() {
        return id;
    }

    public String getAccountId() {
        return accountId;
    }

    public int getYear() {
        return year;
    }

    public String getPaymentMonths() {
        return paymentMonths;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AkbDonationDatabaseObject.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("accountId=" + accountId)
                .add("paymentMonths=" + paymentMonths)
                .add("amount=" + amount)
                .toString();
    }

    public static List<Month> convertPaymentMonths(final String paymentMonths) {
        final List<Month> months = new ArrayList<>();

        if (paymentMonths != null) {
            Lists.newArrayList(paymentMonths.split(",")).forEach((value) -> {
                try {
                    months.add(Month.valueOf(value));
                }
                catch (IllegalArgumentException e) {
                    // ?
                }
            });
        }
        return months;
    }

    public static String convertPaymentMonths(final List<Month> paymentMonths) {
        if (CollectionUtils.isEmpty(paymentMonths)) {
            return "";
        }

        return paymentMonths.stream().map(Month::toString).collect(Collectors.joining(","));
    }
}
