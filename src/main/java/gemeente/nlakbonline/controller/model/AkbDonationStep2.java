package gemeente.nlakbonline.controller.model;

import javax.validation.constraints.AssertTrue;

public class AkbDonationStep2 {

    @AssertTrue
    private Boolean agreePrivacyTerms = Boolean.FALSE;

    public Boolean getAgreePrivacyTerms() {
        return agreePrivacyTerms;
    }

    public void setAgreePrivacyTerms(Boolean agreePrivacyTerms) {
        this.agreePrivacyTerms = agreePrivacyTerms;
    }
}
