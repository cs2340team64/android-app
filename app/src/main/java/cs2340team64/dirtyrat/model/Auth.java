package cs2340team64.dirtyrat.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;



import cs2340team64.dirtyrat.controller.MainActivity;


// ----------------------------------------------------------------------------------------------------------------
// NOTE : Design may be changed to model facade later, but for now this class is directly accessed by controllers.
// ----------------------------------------------------------------------------------------------------------------

public class Auth {

    private FirebaseAuth auth;
    private DatabaseReference db;
    private boolean success;
    private MainActivity caller;

    private static Auth instance;

    /**
     * private singleton constructor
     */
    private Auth() {
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
    }

    /**
     * used to get the instance of the singleton
     * @return singleton instance
     */
    public static Auth getInstance() {
        if (instance == null) {
            instance = new Auth();
        }
        return instance;
    }

    // Currently logged in user on this device
    private FirebaseUser currentUser;

    /**
     * returns the currently logged in user
     * @return logged in user
     */
    public String getCurrentUserEmail() {
        currentUser = auth.getCurrentUser();
        return currentUser.getEmail();
    }

    private static String errorMessage;

    /**
     * getter for error message
     * @return error message
     */
    public String getError() {
        return errorMessage;
    }

    /**
     * attempts to login an email/password
     * @param callback the activity containing the callback function for when the async login method is done
     * @param email login email
     * @param password login password
     */
    public void login(MainActivity callback, String email, String password) {
        caller = callback;
        if (email.equals("") || password.equals("")) {
            errorMessage = "Please enter your email and password.";
            caller.loginCallback(false);
        }

        // this listener is an async call and we are not calling it from the controller class,
        // hence the need for a callback
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Log.d("Register", "success");
                            currentUser = auth.getCurrentUser();
                            success = true;
                        } else {
                            Log.d("Register", task.getException().toString());

                            // refine this error message later
                            errorMessage = "User with that email and password was not found. Please try again or create an account.";
                            success = false;
                        }
                        caller.loginCallback(success);
                    }
                });
    }

    /**
     * attempts to register an email/password
     * @param callback the activity containing the callback function for the async register method
     * @param email registration email
     * @param password registration pass
     * @param confirmPassword registration pass
     * @param admin if true, will register user as an admin
     */
    public void register(MainActivity callback, String email, String password, String confirmPassword, final boolean admin) {
        caller = callback;
        if (!password.equals(confirmPassword)) {
            errorMessage = "Passwords do not match.";
            caller.registerCallback(false);
        }

        if (password.length() < 6) {
            errorMessage = "Password must be 6 or more characters.";
            caller.registerCallback(false);
        }

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Register", "successfully created account");
                            db.child("admins").child(auth.getCurrentUser().getUid()).setValue(admin);
                            success = true;
                        } else {
                            Log.d("Register", task.getException().toString());

                            // refine this error message later
                            errorMessage = "Registration failed. That email may already be assigned to a user.";
                            success = false;
                        }
                        caller.registerCallback(success);
                    }
                });

    }

}
