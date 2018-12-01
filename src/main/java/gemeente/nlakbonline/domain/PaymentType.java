package gemeente.nlakbonline.domain;

public enum PaymentType {
    /**
     * Pay donation by wire transfer
     */
    BANK_TRANSFER,

    /**
     * Pay donation without manual interaction (e.g. incasso)
     */
    BANK_AUTOMATIC
}
