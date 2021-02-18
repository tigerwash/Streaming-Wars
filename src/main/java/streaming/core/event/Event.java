package streaming.core.event;

import streaming.core.entity.Entity;

public class Event {
    private final EventType type;
    private final String name;
    private final int year;
    private int duration;
    private final Entity producer;
    private int licenseFee;

    protected Event(EventType type, String name, int year, int duration, Entity producer, int licenseFee) {
        this.type = type;
        this.name = name;
        this.year = year;
        this.duration = duration;
        this.producer = producer;
        this.licenseFee = licenseFee;
    }

    public EventType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    public int getDuration() {
        return duration;
    }

    public Entity getProducer() {
        return producer;
    }

    public int getLicenseFee() {
        return licenseFee;
    }

    public void setDuration(int duration) { this.duration = duration; }
    public void setLicenseFee(int fee) {this.licenseFee = fee; }

    @Override
    public String toString() {
        return type.getType() + ',' + name + ',' + year + ',' + duration + ',' + producer + ',' + licenseFee;
    }
}
