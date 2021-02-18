package streaming.user;

import streaming.Instruction;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Stream.of;
import static streaming.Instruction.*;

public enum Role {

    // Admin: Admin users can
    // all instructions
    ADMIN("Admin", of(Instruction.values()).collect(Collectors.toSet())),
    // General: general users can
    // take all types of actions in the core system (see Core System section for details).
    // General users do not have access to create, update and delete user information.
    GENERAL("General",
            Stream.of(
                    CREATE_DEMO,
                    DISPLAY_DEMO,
                    CREATE_EVENT,
                    WATCH_EVENT,
                    CREATE_STUDIO,
                    DISPLAY_STUDIO,
                    CREATE_STREAM,
                    DISPLAY_STREAM,
                    OFFER_MOVIE,
                    OFFER_PPV,
                    DISPLAY_EVENTS,
                    DISPLAY_OFFERS,
                    DISPLAY_TIME,
                    NEXT_MONTH,
                    UPDATE_DEMO,
                    UPDATE_EVENT,
                    UPDATE_STREAM,
                    RETRACT_MOVIE,
                    STOP)
                    .collect(Collectors.toSet())),
    // Guest: Guest users can
    // display_demo, display_studio, display_stream, display_events, display_offers, and display_time.
    GUEST("Guest",
            Stream.of(
                    DISPLAY_DEMO,
                    DISPLAY_STUDIO,
                    DISPLAY_STREAM,
                    DISPLAY_EVENTS,
                    DISPLAY_OFFERS,
                    DISPLAY_TIME,
                    STOP)
                    .collect(Collectors.toSet()));

    private final String name;
    private final Set<Instruction> instructionSet;

    Role(String name, Set<Instruction> instructionSet) {
        this.name = name;
        this.instructionSet = instructionSet;
    }

    public String getName() {
        return this.name;
    }

    public static boolean isValidRole(String roleString) {
        return of(Role.values()).filter(c -> c.getName().equals(roleString)).findAny().isPresent();
    }

    public static Role searchRole(String roleString) {
        Optional<Role> result = of(Role.values()).filter(c -> c.getName().equals(roleString)).findAny();
        if (result.isPresent()) {
            return result.get();
        } else {
            return GUEST;
        }
    }

    public Set<Instruction> getInstructionSet() {
        return instructionSet;
    }
}
