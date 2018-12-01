package gemeente.nlakbonline.controller.model;

import gemeente.nlakbonline.domain.PaymentType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AkbDonationStep1 {

    private static Month[] MONTHS = new Month[]{Month.FEBRUARY, Month.MARCH, Month.APRIL, Month.MAY, Month.JUNE, Month.JULY, Month.AUGUST, Month.SEPTEMBER, Month.OCTOBER, Month.NOVEMBER, Month.DECEMBER};

    @NotNull
    @Positive
    private Double amount;

    @Size(min = 1)
    private List<Month> paymentMonths;

    @NotNull
    private PaymentType paymentType;

    private AkbPersonRegistration akbPersonRegistration;

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

    public AkbPersonRegistration getAkbPersonRegistration() {
        return akbPersonRegistration;
    }

    public void setAkbPersonRegistration(AkbPersonRegistration akbPersonRegistration) {
        this.akbPersonRegistration = akbPersonRegistration;
    }

    public Double calculateAmountByMonth(final Month month) {
        if (paymentMonths != null && amount != null && paymentMonths.contains(month)) {
            return amount / (paymentMonths.size());
        }
        return 0.00;
    }

    public List<Month> getAllMonths() {
        return Arrays.asList(MONTHS);
    }

    public boolean isComplete() {
        return !Objects.isNull(this.amount) && this.amount > 0.0 && this.paymentMonths != null && this.paymentMonths.size() > 0;
    }
}
