package streaming.core.event;

import streaming.core.entity.Entity;
import streaming.core.entity.Stream;

import java.util.HashMap;
import java.util.Map;

public class PPV extends Event {
    private Map<Stream, Integer> rateMap;

    public PPV(String name, int year, int duration, Entity producer, int licenseFee) {
        super(EventType.PPV, name, year, duration, producer, licenseFee);
        this.rateMap = new HashMap<>();
    }

    public Map<Stream, Integer> getRateMap() {
        return rateMap;
    }

    public void addStreamSubscription(Stream stream, int rate) {
        this.rateMap.put(stream, rate);
    }
}
