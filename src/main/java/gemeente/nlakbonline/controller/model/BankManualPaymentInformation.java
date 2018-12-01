package gemeente.nlakbonline.controller.model;

import gemeente.nlakbonline.domain.PaymentType;

public class BankManualPaymentInformation extends PaymentInformation {

    private String bankAccountNumber;
    private String creditorName;
    private String reference;

    @Override
    PaymentType getPaymentType() {
        return PaymentType.BANK_TRANSFER;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getCreditorName() {
        return creditorName;
    }

    public void setCreditorName(String creditorName) {
        this.creditorName = creditorName;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
