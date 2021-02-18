package streaming;

import streaming.core.entity.DemographicGroup;
import streaming.core.entity.Stream;
import streaming.core.entity.Studio;
import streaming.core.event.Event;
import streaming.core.event.EventFactory;
import streaming.core.event.EventType;
import streaming.core.util.Pair;
import streaming.db.DbUtils;
import streaming.user.Role;
import streaming.user.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import java.util.ArrayList;

/***
 * This is a singleton class to handle instructions
 */
public class AdminSystem {
    private Map<String, DemographicGroup> demographicGroupMap = new LinkedHashMap<>();
    private Map<String, Studio> studioMap = new LinkedHashMap<>();
    private Map<String, Stream> streamMap = new LinkedHashMap<>();
    private Map<Pair<String, Integer>, Event> eventMap = new LinkedHashMap<>();
    private Map<Pair<Event, Stream>, Integer> monthlyOfferMap = new LinkedHashMap<>();
    private int currentMonth = 10;
    private int currentYear = 2020;
    private final String DELIMITER = ",";
    private User user = null;
    private DbUtils dbUtils;
    private FileWriter dbFileWriter;
    private boolean useDb = false;
    private boolean isDbLoaded = false;

    private ArrayList<Event> eventWatchHistory = new ArrayList<>();


    private AdminSystem() {
    }

    public static AdminSystem getInstance() {
        return new AdminSystem();
    }

    public void processSingleLine(String wholeInputLine, boolean verboseMode) throws IOException {
        String[] tokens;
        tokens = wholeInputLine.split(DELIMITER);
        System.out.println("> " + wholeInputLine);
        Instruction instruction = Instruction.searchCommand(tokens[0]);
        if (instruction != null) {
            if (user.getRole().getInstructionSet().contains(instruction)) {
                switch (instruction) {
                    case CREATE_DEMO: // [1] create_demo,<short name>,<long name>,<number of accounts>
                        if (verboseMode) Instruction.printMessage(Instruction.CREATE_DEMO);
                        if (!this.demographicGroupMap.containsKey(tokens[1])) {
                            this.demographicGroupMap.put(tokens[1], new DemographicGroup(tokens[1], tokens[2], Integer.parseInt(tokens[3])));
                            this.writeIntoDb(instruction, wholeInputLine, verboseMode);
                        } else {
                            System.out.println("Input is wrong!!!!!!!!");
                        }
                        break;
                    case DISPLAY_DEMO: // [9] display_demo,<short name>
                        if (verboseMode) Instruction.printMessage(Instruction.DISPLAY_DEMO);
                        System.out.println(this.demographicGroupMap.get(tokens[1]));
                        break;
                    case CREATE_STUDIO: // [2] create_studio,<short name>,<long name>
                        if (verboseMode) Instruction.printMessage(Instruction.CREATE_STUDIO);
                        if (!this.studioMap.containsKey(tokens[1])) {
                            this.studioMap.put(tokens[1], new Studio(tokens[1], tokens[2]));
                            this.writeIntoDb(instruction, wholeInputLine, verboseMode);
                        } else {
                            System.out.println("Input is wrong!!!!!!!!");
                        }
                        break;
                    case DISPLAY_STUDIO: // [11] display_studio,<short name>
                        if (verboseMode) Instruction.printMessage(Instruction.DISPLAY_STUDIO);
                        System.out.println(this.studioMap.get(tokens[1]));
                        break;
                    case CREATE_STREAM: // [4] create_stream,<short name>,<long name>,<subscription price>
                        if (verboseMode) Instruction.printMessage(Instruction.CREATE_STREAM);
                        if (!this.streamMap.containsKey(tokens[1])) {
                            this.streamMap.put(tokens[1], new Stream(tokens[1], tokens[2], Integer.parseInt(tokens[3])));
                            this.writeIntoDb(instruction, wholeInputLine, verboseMode);
                        } else {
                            System.out.println("Input is wrong!!!!!!!!");
                        }
                        break;
                    case DISPLAY_STREAM: // [10] display_stream,<short name>
                        if (verboseMode) Instruction.printMessage(Instruction.DISPLAY_STREAM);
                        System.out.println(this.streamMap.get(tokens[1]));
                        break;
                    case CREATE_EVENT: // [3] create_event,<type>,<name>,<year produced>,<duration>,<studio>,<license fee>
                        if (verboseMode) Instruction.printMessage(Instruction.CREATE_EVENT);
                        if (this.createEvent(tokens)) this.writeIntoDb(instruction, wholeInputLine, verboseMode);
                        break;
                    case DISPLAY_EVENTS: // [12] display_events
                        if (verboseMode) Instruction.printMessage(Instruction.DISPLAY_EVENTS);
                        this.displayEvents();
                        break;
                    case OFFER_MOVIE: // [5] offer_movie,<streaming service>,<movie name>
                        if (verboseMode) Instruction.printMessage(Instruction.OFFER_MOVIE);
                        if (this.offerEvent(tokens, EventType.MOVIE))
                            this.writeIntoDb(instruction, wholeInputLine, verboseMode);
                        break;
                    case OFFER_PPV: // [6] offer_ppv,<streaming service>,<pay-per-view name>,<price>
                        if (verboseMode) Instruction.printMessage(Instruction.OFFER_PPV);
                        if (this.offerEvent(tokens, EventType.PPV))
                            this.writeIntoDb(instruction, wholeInputLine, verboseMode);
                        break;
                    case WATCH_EVENT: // [7] watch_event,<demographic group>,<percentage>,<streaming service>,<event name>
                        if (verboseMode) Instruction.printMessage(Instruction.WATCH_EVENT);
                        if (this.watchEvent(tokens)) this.writeIntoDb(instruction, wholeInputLine, verboseMode);
                        break;
                    case NEXT_MONTH: // [8] next_month
                        if (verboseMode) Instruction.printMessage(Instruction.NEXT_MONTH);
                        if (currentMonth >= 12) {
                            currentMonth = 1;
                            currentYear += 1;
                        } else {
                            currentMonth += 1;
                        }
                        demographicGroupMap.values().forEach(x -> x.bookRecord());
                        studioMap.values().forEach(x -> x.bookRecord());
                        streamMap.values().forEach(x -> x.bookRecord());
                        monthlyOfferMap.clear();
                        eventWatchHistory.clear();
                        this.writeIntoDb(instruction, wholeInputLine, verboseMode);
                        break;
                    case DISPLAY_OFFERS: // [13] display_offers
                        if (verboseMode) Instruction.printMessage(Instruction.DISPLAY_OFFERS);
                        this.displayOffers();
                        break;
                    case DISPLAY_TIME: // [14] display_time
                        if (verboseMode) Instruction.printMessage(Instruction.DISPLAY_TIME);
                        this.displayTime();
                        break;
                    case REGISTER: // [x] register
                        if (verboseMode) Instruction.printMessage(Instruction.REGISTER);
                        User.addUser(tokens[1], tokens[2], tokens[3]);
                        break;
                    case UPDATE_PASSWORD: // [x] update_password
                        if (verboseMode) Instruction.printMessage(Instruction.UPDATE_PASSWORD);
                        User.updatePassword(tokens[1], tokens[2], tokens[3]);
                        break;
                    case UPDATE_ROLE: // [x] update_role
                        if (verboseMode) Instruction.printMessage(Instruction.UPDATE_ROLE);
                        User.updateRole(tokens[1], tokens[2], tokens[3]);
                        break;
                    case RESET_ALL_USERS: // [x] reset_all_users
                        if (verboseMode) Instruction.printMessage(Instruction.RESET_ALL_USERS);
                        User.resetAllUser(tokens[1], tokens[2], tokens[3]);
                        break;
                    case UPDATE_DEMO: // update_demo,<short name>,<long name>,<number of accounts>
                        if (verboseMode) Instruction.printMessage(Instruction.UPDATE_DEMO);
                        if (this.updateDemo(tokens)) this.writeIntoDb(instruction, wholeInputLine, verboseMode);
                        break;
                    case UPDATE_EVENT: // update_event,<name>,<year produced>,<duration>,<license fee>
                        if (verboseMode) Instruction.printMessage(Instruction.UPDATE_EVENT);
                        if (this.updateEvent(tokens)) this.writeIntoDb(instruction, wholeInputLine, verboseMode);
                        break;
                    case UPDATE_STREAM: // update_stream,<short name>,<long name>,<subscription price>
                        if (verboseMode) Instruction.printMessage(Instruction.UPDATE_STREAM);
                        if (this.updateStream(tokens)) this.writeIntoDb(instruction, wholeInputLine, verboseMode);
                        break;
                    case RETRACT_MOVIE: //  retract_movie,<streaming service>,<movie name>,<movie year>
                        if (verboseMode) Instruction.printMessage(Instruction.RETRACT_MOVIE);
                        if (this.retractMovie(tokens)) this.writeIntoDb(instruction, wholeInputLine, verboseMode);
                        break;
                    case STOP: // [15] stop
                        if (verboseMode) {
                            System.out.println("stop_acknowledged");
                        }
                        System.exit(0);
                    default:
                        if (verboseMode) {
                            System.out.println("command_" + tokens[0] + "_NOT_acknowledged");
                        }
                }
            } else {
                System.out.println("Your role is not eligible for instruction: " + instruction.getCommand());
            }
        }
    }

    public void processInstructions(boolean verboseMode) {
        Scanner commandLineInput = new Scanner(System.in);
        String wholeInputLine;
        while (true) {
            try {
                wholeInputLine = commandLineInput.nextLine();
                processSingleLine(wholeInputLine, verboseMode);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println();
            }
        }
    }

    private void displayTime() {
        System.out.println("time," + this.currentMonth + ',' + this.currentYear);
    }

    private void displayEvents() {
        for (Event event : this.eventMap.values()) {
            String output = event.getType().getType() + ',' +
                    event.getName() + ',' +
                    event.getYear() + ',' +
                    event.getDuration() + ',' +
                    event.getProducer().getShortName() + ',' +
                    event.getLicenseFee();
            System.out.println(output);
        }
    }

    private void displayOffers() {
        for (Map.Entry<Pair<Event, Stream>, Integer> item : monthlyOfferMap.entrySet()) {
            Event event = item.getKey().getLeft();
            String output = item.getKey().getRight().getShortName() + ',' +
                    event.getType().getType() + ',' +
                    event.getName() + ',' +
                    event.getYear();
            if (event.getType().equals(EventType.PPV)) {
                output += (',' + item.getValue().toString());
            }
            System.out.println(output);
        }
    }

    private void handleTransaction(Event event, Stream stream, DemographicGroup demoGroup, int percentage) {
        int base, amount;
        if (event.getType().equals(EventType.PPV)) {
            Pair<Event, Stream> offerKey = Pair.of(event, stream);
            base = this.monthlyOfferMap.get(offerKey);
            amount = (int) (percentage / 100.0 * demoGroup.getAccountCount() * base);
        } else {
            base = stream.getSubscriptionFee();
            int additionalPercentage = demoGroup.updatePercentageMap(stream, percentage);
            amount = (int) (additionalPercentage / 100.0 * demoGroup.getAccountCount() * base);
        }
        demoGroup.addCurrent(amount);
        stream.addCurrent(amount);
        this.eventWatchHistory.add(event);
    }

    private boolean offerEvent(String[] tokens, EventType type) {
        Pair<String, Integer> eventKeyPair = Pair.of(tokens[2], Integer.parseInt(tokens[3]));
        int rate = type.equals(EventType.MOVIE) ? 0 : Integer.parseInt(tokens[4]);
        if (this.streamMap.containsKey(tokens[1]) && this.eventMap.containsKey(eventKeyPair)) {
            Event event = this.eventMap.get(eventKeyPair);
            event.getProducer().addCurrent(event.getLicenseFee());
            this.streamMap.get(tokens[1]).addLicensing(event.getLicenseFee());
            monthlyOfferMap.put(Pair.of(event, this.streamMap.get(tokens[1])), rate);
            return true;
        } else {
            System.out.println("Input is wrong!!!!!!!!");
            return false;
        }
    }

    private boolean watchEvent(String[] tokens) {
        Pair<String, Integer> eventKeyPair = Pair.of(tokens[4], Integer.parseInt(tokens[5]));
        if (this.demographicGroupMap.containsKey(tokens[1]) &&
                this.streamMap.containsKey(tokens[3]) &&
                this.eventMap.containsKey(eventKeyPair)
        ) {
            Event event = this.eventMap.get(eventKeyPair);
            int percentage = Integer.parseInt(tokens[2]);
            DemographicGroup demoGroup = this.demographicGroupMap.get(tokens[1]);
            Stream stream = this.streamMap.get(tokens[3]);
            this.handleTransaction(event, stream, demoGroup, percentage);
            return true;
        } else {
            System.out.println("Input is wrong!!!!!!!!");
            return false;
        }
    }

    private boolean createEvent(String[] tokens) {
        Pair<String, Integer> eventKeyPair = Pair.of(tokens[2], Integer.parseInt(tokens[3]));
        if (!this.eventMap.containsKey(eventKeyPair) && this.studioMap.containsKey(tokens[5])) {
            Event newEvent = EventFactory.createEvent(
                    EventType.getEnum(tokens[1]),
                    tokens[2],
                    Integer.parseInt(tokens[3]),
                    Integer.parseInt(tokens[4]),
                    this.studioMap.get(tokens[5]),
                    Integer.parseInt(tokens[6])
            );
            this.eventMap.put(eventKeyPair, newEvent);
            return true;
        } else {
            System.out.println("Input is wrong!!!!!!!!");
            return false;
        }
    }

    private boolean updateDemo(String[] tokens) {
//        The short name itself cannot be changed; and, if the short name does not exist, then the
//        command should be considered invalid. The number of accounts can be changed only at the
//        beginning of a time period before that specific demographic group has accessed and viewed any
//        movies or Pay-Per-View events
        if (this.demographicGroupMap.containsKey(tokens[1]) &&
                this.demographicGroupMap.get(tokens[1]).getCurrentPeriod() == 0) {
            DemographicGroup demoGroup = this.demographicGroupMap.get(tokens[1]);
            demoGroup.setLongName(tokens[2]);
            demoGroup.setAccountCount(Integer.parseInt(tokens[3]));
            return true;
        } else {
            System.out.println("Input is wrong!!!!!!!!");
            return false;
        }
    }

    private boolean updateEvent(String[] tokens) {
//        The name and the year produced cannot be changed; and, if the
//        combination of the name and year produced does not exist, then the command should be
//        considered invalid. The license fee can be changed only at the beginning of a time period before
//        that specific movie or Pay-Per-View event has been accessed and viewed any demographic groups.
//        A studio can make changes until a streaming service offers it.
        Pair<String, Integer> eventKeyPair = Pair.of(tokens[1], Integer.parseInt(tokens[2]));

        if (this.eventMap.containsKey(eventKeyPair) &&
                !this.eventWatchHistory.contains(this.eventMap.get(eventKeyPair))) {
            Event event = this.eventMap.get(eventKeyPair);
            for (Map.Entry<Pair<Event, Stream>, Integer> item : monthlyOfferMap.entrySet()) {
                Event offeredEvent = item.getKey().getLeft();
                if (offeredEvent.getName().equals(event.getName()) &&
                        offeredEvent.getYear() == event.getYear()){
                    System.out.println("Cannot update an event that is offered by StreamingService");
                    return false;
                }
            }
            event.setDuration(Integer.parseInt(tokens[3]));
            event.setLicenseFee(Integer.parseInt(tokens[4]));
            return true;
        } else {
            System.out.println("Input is wrong!!!!!!!!");
            return false;
        }
    }

    private boolean updateStream(String[] tokens) {
//        The short name itself cannot be changed; and, if the short name does not exist, then the
//        command should be considered invalid. The subscription price can be changed only at the
//        beginning of a time period before that specific streaming service has not been used to access and
//        view any movies.
//        A streaming service can make changes until a demographic group watches it.

        if (this.streamMap.containsKey(tokens[1]) &&
                this.streamMap.get(tokens[1]).getCurrentPeriod() == 0) {
            Stream stream = this.streamMap.get(tokens[1]);
            stream.setLongName(tokens[2]);
            stream.setSubscriptionFee(Integer.parseInt(tokens[3]));
            return true;
        } else {
            System.out.println("Input is wrong!!!!!!!!");
            return false;
        }
    }

    private boolean retractMovie(String[] tokens) {
        // command should be invalid if the event being retracted has already been watched.
        Stream stream = this.streamMap.get(tokens[1]);
        Pair<String, Integer> eventKeyPair = Pair.of(tokens[2], Integer.parseInt(tokens[3]));
        Event event = this.eventMap.get(eventKeyPair);
        Pair<Event, Stream> eventStreamPair = Pair.of(event, stream);
        if (this.monthlyOfferMap.containsKey(eventStreamPair) &&
                !this.eventWatchHistory.contains(event)) {
            monthlyOfferMap.remove(eventStreamPair);
            return true;
        } else {
            System.out.println("Input is wrong!!!!!!!!");
            return false;
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setDbUtils(DbUtils dbUtils) throws IOException {
        this.dbUtils = dbUtils;
        if (dbUtils != null && this.useDb) {
            this.loadStreamingDb();
        }
    }

    private void loadStreamingDb() {
        System.out.println("Loading data from DB...");
        BufferedReader reader;
        // need a General user
        User dbUser = new User("_", "_", Role.GENERAL, true);
        User currentUser = this.user;
        this.user = dbUser;
        try {
            reader = new BufferedReader(new FileReader(
                    this.dbUtils.getStDbFile()));
            String line;
            System.out.println("Starting to load db from: " + this.dbUtils.getStDbFile().getPath());
            while ((line = reader.readLine()) != null) {
                System.out.println("Loading data: " + line);
                this.processSingleLine(line, false);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // return User
        this.user = currentUser;
        this.isDbLoaded = true;
    }

    public void setUseDb(boolean useDb) {
        this.useDb = useDb;
    }

    private void writeIntoDb(Instruction instruction, String wholeInputLine, boolean verboseMode) throws IOException {
        if (DbUtils.DB_INSTRUCTION_SET.contains(instruction) && this.useDb && this.isDbLoaded) {
            this.dbFileWriter = new FileWriter(dbUtils.getStDbFile(), true);
            dbFileWriter.write(wholeInputLine);
            dbFileWriter.write("\n");
            dbFileWriter.close();
            if (verboseMode) System.out.println("Writing into db ... ");
        }
    }
}
