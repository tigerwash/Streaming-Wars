package streaming.core.entity;

public class Entity {
    private String shortName;
    private String longName;
    private int currentPeriod;
    private int previousPeriod;
    private long totalAmount;

    public Entity(String shortName, String longName) {
        this.shortName = shortName;
        this.longName = longName;
    }

    public void addCurrent(int earning) {
        this.currentPeriod += earning;
    }

    public void bookRecord() {
        this.totalAmount += this.currentPeriod;
        this.previousPeriod = this.currentPeriod;
        this.currentPeriod = 0;
    }

    public String getShortName() {
        return shortName;
    }

    public String getLongName() {
        return longName;
    }

    public int getCurrentPeriod() {
        return currentPeriod;
    }

    public int getPreviousPeriod() {
        return previousPeriod;
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }
}
