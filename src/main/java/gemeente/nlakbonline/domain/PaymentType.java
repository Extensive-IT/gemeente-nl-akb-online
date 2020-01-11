package gemeente.nlakbonline.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum PaymentType {
    /**
     * Pay donation by wire transfer
     */
    BANK_TRANSFER,

    /**
     * Pay donation without manual interaction (e.g. incasso)
     */
    BANK_AUTOMATIC;

    @JsonCreator
    public static PaymentType forValues(@JsonProperty("paymentType") String paymentType) {
        for (PaymentType current : PaymentType.values()) {
            if (current.toString().equals(paymentType)) {
                return current;
            }
        }

        return null;
    }
}
