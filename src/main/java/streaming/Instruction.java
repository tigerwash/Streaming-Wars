package streaming;

import java.util.Optional;

import static java.util.stream.Stream.*;

public enum Instruction {

    CREATE_DEMO("create_demo"),
    DISPLAY_DEMO("display_demo"),

    CREATE_EVENT("create_event"),
    WATCH_EVENT("watch_event"),

    CREATE_STUDIO("create_studio"),
    DISPLAY_STUDIO("display_studio"),

    CREATE_STREAM("create_stream"),
    DISPLAY_STREAM("display_stream"),

    OFFER_MOVIE("offer_movie"),
    OFFER_PPV("offer_ppv"),

    DISPLAY_EVENTS("display_events"),
    DISPLAY_OFFERS("display_offers"),

    DISPLAY_TIME("display_time"),
    NEXT_MONTH("next_month"),
    UPDATE_DEMO( "update_demo"),
    UPDATE_EVENT("update_event"),
    UPDATE_STREAM("update_stream"),
    RETRACT_MOVIE("retract_movie"),
    // User commands
    REGISTER("register"),
    UPDATE_PASSWORD("update_password"),
    UPDATE_ROLE("update_role"),
    RESET_ALL_USERS("reset_all_users"),

    STOP("stop");

    private final String command;

    Instruction(String command) {
        this.command = command;
    }

    public String getCommand() {
        return this.command;
    }

    public static Instruction searchCommand(String command) {
        Optional<Instruction> result = of(Instruction.values()).filter(c -> c.getCommand().equals(command)).findAny();
        if (result.isPresent()) {
            return result.get();
        } else {
            return null;
        }
    }

    public static void printMessage(Instruction instruction) {
        System.out.println(instruction.getCommand() + "_acknowledged");
    }
}

