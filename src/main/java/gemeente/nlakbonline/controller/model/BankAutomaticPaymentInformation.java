package gemeente.nlakbonline.controller.model;

public class BankAutomaticPaymentInformation extends PaymentInformation {

    private String creditorName;
    private String paymentReason;
    private Integer dayOfMonth;
    private String creditorId;
    private String mandateReference;

    public String getCreditorName() {
        return creditorName;
    }

    public void setCreditorName(String creditorName) {
        this.creditorName = creditorName;
    }

    public String getPaymentReason() {
        return paymentReason;
    }

    public void setPaymentReason(String paymentReason) {
        this.paymentReason = paymentReason;
    }

    public Integer getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public String getCreditorId() {
        return creditorId;
    }

    public void setCreditorId(String creditorId) {
        this.creditorId = creditorId;
    }

    public String getMandateReference() {
        return mandateReference;
    }

    public void setMandateReference(String mandateReference) {
        this.mandateReference = mandateReference;
    }

    @Override
    PaymentType getPaymentType() {
        return PaymentType.BANK_AUTOMATIC;
    }
}
