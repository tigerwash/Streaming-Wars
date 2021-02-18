package streaming.core.event;

import streaming.core.entity.Entity;

public class EventFactory {
    public static Event createEvent(EventType type, String name, int year, int duration, Entity producer, int licenseFee) {
        if (type.equals(EventType.MOVIE)) {
            return new Movie(name, year, duration, producer, licenseFee);
        } else {
            return new PPV(name, year, duration, producer, licenseFee);
        }
    }
}
