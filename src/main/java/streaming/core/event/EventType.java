package streaming.core.event;

public enum EventType {
    MOVIE("movie"),
    PPV("ppv");

    private final String type;

    EventType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public static boolean isMovie(String typeStr) {
        return typeStr.equals(MOVIE.type);
    }

    public static EventType getEnum(String typeStr) {
        return isMovie(typeStr)? MOVIE: PPV;
    }
}
