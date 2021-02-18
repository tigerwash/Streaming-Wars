package streaming.user;

import streaming.db.DbUtils;

import java.io.*;
import java.util.HashMap;

public class User {

    // Static variable
    private static HashMap<String, User> userMap = new HashMap<>();
    private static String loginDbPath = "./dbUser.txt";

    // Instance variable
    private String name;
    private String password;
    private Role role;

    // Constructor
    public User(String name, String password, Role role, Boolean hashed) {
        this.name = name;
        this.password = hashed ? password : createHash(password);
        this.role = role;
        this.findDbPath();
    }

    public User() {
        this.findDbPath();
    }

    private void findDbPath() {
        String currentPath = "";
        try {
            currentPath = new File(DbUtils.class.getProtectionDomain().getCodeSource().getLocation()
                    .toURI()).getParent();
        } catch (Exception e) {
            System.out.println("Initialize DB is failed.");
        }
        this.loginDbPath = currentPath + "/dbUser.txt";
    }

    // Getter and setter methods
    public static HashMap<String, User> getUserMap() {
        return userMap;
    }

    // Tostring methods
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role.getName() + '\'' +
                '}';
    }

    public static void mapToString() {
        loadLoginDb();
        for (User myUser : userMap.values()) {
            System.out.println(myUser.toString());
        }
    }

    // Itemized methods
    public static User authenticateUser(String name, String password) {
        loadLoginDb();
        if (!userMap.containsKey(name)) {
            System.out.println("User doesn't exist! Please signup first!");
            return null;
        }
        User myUser = userMap.get(name);
        if (verifyPassword(password, myUser.password)) {
            System.out.println("Welcome " + name + "!");
            return myUser;
        } else {
            System.out.println("Invalid password, please try again!");
            return null;
        }
    }

    public static void addUser(String name, String password, String role) {
        loadLoginDb();
        if (userMap.containsKey(name)) {
            System.out.println("User exists already! Please login instead!");
            return;
        }
        if (!Role.isValidRole(role)) {
            System.out.println("Role must be among 'Admin', 'General' and 'Guest'! Please re-enter!");
            return;
        }
        User myUser = new User(name, password, Role.searchRole(role), false);
        userMap.put(name, myUser);
        updateLoginDb();
        System.out.println("User successfully created!");
    }

    public static void resetAllUser(String name, String password, String resetPassword) {
        loadLoginDb();
        User myUser = userMap.get(name);
        if (verifyPassword(password, myUser.password) && resetPassword.equals("IMSURE")) {
            // Clear DB
            userMap = new HashMap<>();
            resetLoginDb();

            // Reset default users
            User adminUser = new User("admin", "admin", Role.searchRole("Admin"), false);
            userMap.put("Admin", adminUser);
            User generalUser = new User("general", "general", Role.searchRole("General"), false);
            userMap.put("General", generalUser);
            User guestUser = new User("guest", "guest", Role.searchRole("Guest"), false);
            userMap.put("Guest", guestUser);
            updateLoginDb();

            // Display message
            System.out.println("All user data have been reset :(");
        } else {
            System.out.println("Invalid password, please try again!");
        }
    }

    public static void updatePassword(String name, String oldPassword, String newPassword) {
        loadLoginDb();
        if (!userMap.containsKey(name)) {
            System.out.println("User doesn't exist! Please signup first!");
            return;
        }

        User myUser = userMap.get(name);

        if (verifyPassword(oldPassword, myUser.password)) {
            myUser.password = myUser.createHash(newPassword);
            userMap.put(name, myUser);
            updateLoginDb();
            System.out.println("Password successfully updated!");
        } else {
            System.out.println("Invalid password, please try again!");
        }
    }

    public static void updateRole(String name, String password, String role) {
        loadLoginDb();
        if (!userMap.containsKey(name)) {
            System.out.println("User doesn't exist! Please signup first!");
            return;
        }

        User myUser = userMap.get(name);

        if (verifyPassword(password, myUser.password)) {
            myUser.role = Role.searchRole(role);
            userMap.put(name, myUser);
            updateLoginDb();
            System.out.println("Role successfully updated!");
        } else {
            System.out.println("Invalid password, please try again!");
        }
    }

    // Authentication methods
    private String createHash(String password) {
        try {
            return PasswordStorage.createHash(password);
        } catch (PasswordStorage.CannotPerformOperationException e) {
            e.printStackTrace();
        }
        return password;
    }

    private static Boolean verifyPassword(String password, String hash) {
        try {
            return PasswordStorage.verifyPassword(password, hash);
        } catch (PasswordStorage.CannotPerformOperationException e) {
            e.printStackTrace();
        } catch (PasswordStorage.InvalidHashException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Connect to Db methods
    private static void loadLoginDb() {
        String line = "";
        String delimiter = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(loginDbPath))) {
            while ((line = br.readLine()) != null) {
                String[] login = line.split(delimiter);
                User myUser = new User(login[0], login[1], Role.searchRole(login[2]), true);
                userMap.put(login[0], myUser);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void updateLoginDb() {
        try {
            // create FileWriter object with file as parameter
            FileWriter loginDb = new FileWriter(loginDbPath);
            for (User myUser : userMap.values()) {
                loginDb.write(myUser.name + "," + myUser.password + "," + myUser.role.getName() + "\n");
            }
            loginDb.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void resetLoginDb() {
        try {
            // create FileWriter object with file as parameter
            FileWriter loginDb = new FileWriter(loginDbPath);
            loginDb.flush();
            loginDb.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
