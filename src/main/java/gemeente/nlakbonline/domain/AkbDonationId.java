package gemeente.nlakbonline.domain;

import org.springframework.data.annotation.Id;

import java.util.Objects;
import java.util.StringJoiner;

public class AkbDonationId {

    private final String user;
    private final int year;

    public AkbDonationId(final String user, final int year) {
        this.user = Objects.requireNonNull(user);
        this.year = year;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AkbDonationId.class.getSimpleName() + "[", "]")
                .add("user='" + user + "'")
                .add("year=" + year)
                .toString();
    }
}
