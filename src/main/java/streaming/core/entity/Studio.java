package streaming.core.entity;

public class Studio extends Entity {
    public Studio(String shortName, String longName) {
        super(shortName, longName);
    }

    @Override
    public String toString() {
        return "studio," + this.getShortName() + "," + this.getLongName() + "\n" +
                "current_period," + this.getCurrentPeriod() + "\n" +
                "previous_period," + this.getPreviousPeriod() + "\n" +
                "total," + this.getTotalAmount();
    }
}
