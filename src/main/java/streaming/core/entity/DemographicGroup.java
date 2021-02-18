package streaming.core.entity;

import java.util.HashMap;
import java.util.Map;

public class DemographicGroup extends Entity {
    private int accountCount;
    private Map<Stream, Integer> subscriptionPercentageMap  = new HashMap<>();

    public DemographicGroup(String shortName, String longName, int accountCount) {
        super(shortName, longName);
        this.accountCount = accountCount;
    }

    public int getAccountCount() {
        return accountCount;
    }

    public void initializeSubscriptionPercentageMap() {
        this.subscriptionPercentageMap  = new HashMap<>();
    }

    public int updatePercentageMap(Stream stream, int percentage) {
        if (!this.subscriptionPercentageMap.containsKey(stream)) {
            this.subscriptionPercentageMap.put(stream, percentage);
            return percentage;
        }
        int currentPercentage = this.subscriptionPercentageMap.get(stream);
        int targetPercentage, diff;
        if (currentPercentage < 100 && currentPercentage < percentage) {
            targetPercentage = Math.min(percentage, 100);
            diff = targetPercentage - currentPercentage;
            this.subscriptionPercentageMap.put(stream, targetPercentage);
            return diff;
        }
        return 0;
    }

    public void setAccountCount(int accountCount) {
        this.accountCount = accountCount;
    }

    @Override
    public String toString() {
        return "demo," + this.getShortName() + "," + this.getLongName() + "\n" +
                "size," + this.accountCount + "\n" +
                "current_period," + this.getCurrentPeriod() + "\n" +
                "previous_period," + this.getPreviousPeriod() + "\n" +
                "total," + this.getTotalAmount();
    }

    @Override
    public void bookRecord() {
        super.bookRecord();
        this.initializeSubscriptionPercentageMap();
    }
}
