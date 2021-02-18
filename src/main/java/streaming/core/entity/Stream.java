package streaming.core.entity;

public class Stream extends Entity {
    private int subscriptionFee;
    private int licensing;

    public Stream(String shortName, String longName) {
        super(shortName, longName);
    }

    public Stream(String shortName, String longName, int subscriptionFee) {
        super(shortName, longName);
        this.subscriptionFee = subscriptionFee;
    }

    public void addLicensing(int newLicensing) {
        this.licensing += newLicensing;
    }

    public int getSubscriptionFee() {
        return subscriptionFee;
    }

    public int getLicensing() {
        return licensing;
    }

    public void setSubscriptionFee(int fee) { this.subscriptionFee = fee; }

    @Override
    public String toString() {
        return "stream," + this.getShortName() + "," + this.getLongName() + "\n" +
                "subscription," + this.subscriptionFee + "\n" +
                "current_period," + this.getCurrentPeriod() + "\n" +
                "previous_period," + this.getPreviousPeriod() + "\n" +
                "total," + this.getTotalAmount() + "\n" +
                "licensing," + this.licensing;
    }
}