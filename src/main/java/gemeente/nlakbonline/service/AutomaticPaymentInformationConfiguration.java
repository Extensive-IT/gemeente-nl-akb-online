package gemeente.nlakbonline.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "content.automatic-payment-information")
public class AutomaticPaymentInformationConfiguration {
    private String creditorName;
    private String creditorId;
    private Integer dayOfMonth;
    private String mandateReferencePrefix;
    private String mandateReferencePostfix;

    public String getCreditorName() {
        return creditorName;
    }

    public void setCreditorName(String creditorName) {
        this.creditorName = creditorName;
    }

    public String getCreditorId() {
        return creditorId;
    }

    public void setCreditorId(String creditorId) {
        this.creditorId = creditorId;
    }

    public Integer getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public String getMandateReferencePrefix() {
        return mandateReferencePrefix;
    }

    public void setMandateReferencePrefix(String mandateReferencePrefix) {
        this.mandateReferencePrefix = mandateReferencePrefix;
    }

    public String getMandateReferencePostfix() {
        return mandateReferencePostfix;
    }

    public void setMandateReferencePostfix(String mandateReferencePostfix) {
        this.mandateReferencePostfix = mandateReferencePostfix;
    }
}
