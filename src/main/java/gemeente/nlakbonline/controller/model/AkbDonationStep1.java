package gemeente.nlakbonline.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.Month;
import java.util.List;

public class AkbDonationStep1 {

    @NotNull
    @Positive
    private Double amount;

    @Size(min = 1)
    private List<Month> paymentMonths;

    @NotNull
    private PaymentType paymentType;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public List<Month> getPaymentMonths() {
        return paymentMonths;
    }

    public void setPaymentMonths(List<Month> paymentMonths) {
        this.paymentMonths = paymentMonths;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }
}
