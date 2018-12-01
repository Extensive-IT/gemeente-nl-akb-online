package gemeente.nlakbonline.controller.model;

/**
 * <code>AkbDonationSession</code> keeps track of all state within a session of giving towards AKB
 */
public class AkbDonationSession {
    private AkbDonationStep1 akbDonationStep1;
    private AkbDonationStep2 akbDonationStep2;

    /**
     * Construct a new {@link AkbDonationSession}
     */
    public AkbDonationSession() {
        final AkbDonationStep1 akbDonationStep1 = new AkbDonationStep1();
        this.setAkbDonationStep1(akbDonationStep1);
    }

    public AkbDonationStep1 getAkbDonationStep1() {
        return akbDonationStep1;
    }

    public void setAkbDonationStep1(AkbDonationStep1 akbDonationStep1) {
        this.akbDonationStep1 = akbDonationStep1;
    }

    public AkbDonationStep2 createOrReuseAkbDonationStep2() {
        if (getAkbDonationStep2() == null) {
            this.setAkbDonationStep2(new AkbDonationStep2());
        }
        return this.getAkbDonationStep2();
    }

    public AkbDonationStep2 getAkbDonationStep2() {
        return akbDonationStep2;
    }

    public void setAkbDonationStep2(AkbDonationStep2 akbDonationStep2) {
        this.akbDonationStep2 = akbDonationStep2;
    }

    public void reset() {
        this.setAkbDonationStep1(new AkbDonationStep1());
        this.setAkbDonationStep2(null);
    }

    public boolean isReadyForStep2() {
        return this.getAkbDonationStep1().isComplete();
    }
}
