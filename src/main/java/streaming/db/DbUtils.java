package streaming.db;

import streaming.Instruction;

import java.io.*;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static streaming.Instruction.*;

public class DbUtils {
    private String currentDirectory = "";
    private String DB_USER = "/dbUser.txt";
    private String DB_STREAMING = "/dbStreaming.txt";
    private File userDbFile;
    private File stDbFile;
    public static final Set<Instruction> DB_INSTRUCTION_SET = Stream.of(
            CREATE_DEMO,
            CREATE_EVENT,
            WATCH_EVENT,
            CREATE_STUDIO,
            CREATE_STREAM,
            OFFER_MOVIE,
            OFFER_PPV,
            NEXT_MONTH,
            UPDATE_DEMO,
            UPDATE_EVENT,
            UPDATE_STREAM,
            RETRACT_MOVIE)
            .collect(Collectors.toSet());

    public void initializeDb() {
        System.out.println("Initializing DB");
        try {
            this.currentDirectory = new File(DbUtils.class.getProtectionDomain().getCodeSource().getLocation()
                    .toURI()).getParent();

            // check if user file exists
            userDbFile = new File(this.currentDirectory + DB_USER);
            // if not found, create new file and write file
            if (!userDbFile.exists()) {
                userDbFile.createNewFile();
                FileWriter dbWriter = new FileWriter(userDbFile);
                InputStream in = this.getClass().getResourceAsStream(DB_USER);
                String line = "";
                try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
                    while ((line = br.readLine()) != null) {
                        dbWriter.write(line);
                        dbWriter.write("\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dbWriter.close();
            }

            // check if streaming db exists
            stDbFile = new File(this.currentDirectory + DB_STREAMING);
            // if not found, create new file and write file
            if (!stDbFile.exists()) {
                stDbFile.createNewFile();
                FileWriter dbWriter = new FileWriter(stDbFile);
                InputStream in = this.getClass().getResourceAsStream(DB_STREAMING);
                String line = "";
                try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
                    while ((line = br.readLine()) != null) {
                        dbWriter.write(line);
                        dbWriter.write("\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dbWriter.close();
            }
        } catch (Exception e) {
            System.out.println("Initialize DB is failed.");
        }
    }

    public File getUserDbFile() {
        return userDbFile;
    }

    public File getStDbFile() {
        return stDbFile;
    }
}
