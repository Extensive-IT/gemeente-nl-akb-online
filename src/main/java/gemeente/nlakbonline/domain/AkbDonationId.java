package gemeente.nlakbonline.domain;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

public class AkbDonationId {

    private final UUID accountId;
    private final int year;

    public AkbDonationId(final UUID accountId, final int year) {
        this.accountId = Objects.requireNonNull(accountId);
        this.year = year;
    }

    public UUID getAccountId() {
        return this.accountId;
    }

    public int getYear() {
        return this.year;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AkbDonationId.class.getSimpleName() + "[", "]")
                .add("accountId='" + accountId + "'")
                .add("year=" + year)
                .toString();
    }
}
