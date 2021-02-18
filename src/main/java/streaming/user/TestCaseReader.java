package streaming.user;

import java.util.Scanner;

public class TestCaseReader {

    public TestCaseReader() {
    }

    public void processInstructions(Boolean verboseMode) {
        Scanner commandLineInput = new Scanner(System.in);
        String wholeInputLine;
        String[] tokens;
        final String DELIMITER = ",";

        System.out.print("======================================================================================== \n");
        System.out.print("Welcome! Please enter commands using formats below: \n");
        System.out.print("- Register: Username, Password, Role; [e.g.] register,test,123,admin\n");
        System.out.print("- Login: Username, Password; [e.g.] login,test,123\n");
        System.out.print("- Update password: Username, Old Password, New Password; [e.g.] update_password,test,123,456\n");
        System.out.print("- Update role: Username, Password, New Role; [e.g.] update_role,test,123,movie\n");
        System.out.print("- Reset all users [admin only]: Username, Password, ResetPassword; [e.g.] reset_all_users,test,456,456\n");

        while (true) {
            try {
                // Determine the next command and echo it to the monitor for testing purposes
                wholeInputLine = commandLineInput.nextLine();
                tokens = wholeInputLine.split(DELIMITER);
                System.out.println("> " + wholeInputLine);

                if (tokens[0].equals("register")) {
                    if (verboseMode) { System.out.println("Signup_acknowledged"); }
                    User.addUser(tokens[1], tokens[2], tokens[3]);

                } else if (tokens[0].equals("login")) {
                    User.authenticateUser(tokens[1], tokens[2]);

                } else if (tokens[0].equals("update_password")) {
                    User.updatePassword(tokens[1], tokens[2], tokens[3]);

                } else if (tokens[0].equals("update_role")) {
                    User.updateRole(tokens[1], tokens[2], tokens[3]);

                } else if (tokens[0].equals("reset_all_users")) {
                        User.resetAllUser(tokens[1], tokens[2], tokens[3]);

                } else if (tokens[0].equals("stop")) {
                    break;

                } else {
                    if (verboseMode) { System.out.println("command_" + tokens[0] + "_NOT_acknowledged"); }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println();
            }
        }

        if (verboseMode) { System.out.println("stop_acknowledged"); }
        commandLineInput.close();
    }

}