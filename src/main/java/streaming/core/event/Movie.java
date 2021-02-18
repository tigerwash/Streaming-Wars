package streaming.core.event;

import streaming.core.entity.Entity;
import streaming.core.entity.Stream;

import java.util.HashSet;
import java.util.Set;

public class Movie extends Event {
    private Set<Stream> streamSet;

    public Movie(String name, int year, int duration, Entity producer, int licenseFee) {
        super(EventType.MOVIE, name, year, duration, producer, licenseFee);
        this.streamSet = new HashSet<>();
    }

    public Set<Stream> getStreamSet() {
        return streamSet;
    }

    public void addStream(Stream stream) {
        this.streamSet.add(stream);
    }
}
