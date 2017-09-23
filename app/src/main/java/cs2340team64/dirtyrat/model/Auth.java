package cs2340team64.dirtyrat.model;

import java.util.ArrayList;
import java.util.List;


// ----------------------------------------------------------------------------------------------------------------
// NOTE : Design will be changed to model facade later, but for now this class is directly accessed by controllers.
// ----------------------------------------------------------------------------------------------------------------

public class Auth {

    private static class User {
        private String username;
        private String password;

        private User(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }
    // Currently logged in user on this device
    private static User currentUser;

    public static String getCurrentUsername() {
        return currentUser.username;
    }

    private static String errorMessage;

    public static String getError() {
        return errorMessage;
    }

    // **TEMPORARY**: Obviously this will be an actual database in the near future
    private static List<User> users = new ArrayList<>();

    public static boolean login(String username, String password) {
        for (User user : users) {
            if (user.username.equals(username)) {
                if (user.password.equals(password)) {
                    currentUser = user;
                    return true;
                } else {
                    errorMessage = "Incorrect password";
                    return false;
                }
            }
        }
        errorMessage = "User not found";
        return false;
    }

    public static boolean register(String username, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            errorMessage = "Passwords do not match.";
            return false;
        }
        for (User user : users) {
            if (user.username.equals(username)) {
                errorMessage = "Sorry, a user with that name already exists.";
                return false;
            }
        }
        // again, temporary until we start using a database
        users.add(new User(username, password));
        return true;
    }

}
