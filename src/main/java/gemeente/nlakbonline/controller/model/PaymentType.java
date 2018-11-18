package gemeente.nlakbonline.controller.model;

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
