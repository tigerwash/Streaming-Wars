package streaming;

import streaming.db.DbUtils;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        DbUtils dbUtils = new DbUtils();
        dbUtils.initializeDb();
        AdminSystem adminSystem =  AdminSystem.getInstance();
        adminSystem.setDbUtils(dbUtils);
        Boolean showState = Boolean.FALSE;

        if (args.length >= 2 && (args[1].equals("-v") || args[1].equals("-verbose")))
        { showState = Boolean.TRUE; }
         adminSystem.processInstructions(showState);
    }
}
