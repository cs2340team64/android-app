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
// NOTE : Design will be changed to model facade later, but for now this class is directly accessed by controllers.
// ----------------------------------------------------------------------------------------------------------------

public class Auth {

    private FirebaseAuth auth;
    private DatabaseReference db;
    private boolean success;
    private boolean done;
    private MainActivity caller;

    private static Auth instance;

    public Auth() {
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
    }

    public static Auth getInstance() {
        if (instance == null) {
            instance = new Auth();
        }
        return instance;
    }

    // Currently logged in user on this device
    private FirebaseUser currentUser;

    public String getCurrentUserEmail() {
        currentUser = auth.getCurrentUser();
        return currentUser.getEmail();
    }

    private static String errorMessage;

    public String getError() {
        return errorMessage;
    }

    public void login(MainActivity callback, String email, String password) {
        caller = callback;
        done = false;
        if (email.equals("") || password.equals("")) {
            errorMessage = "Please enter your email and password.";
            caller.loginCallback(false);
        }
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Log.d("Register", "success");
                            currentUser = auth.getCurrentUser();
                            success = true;
                            done = true;
                        } else {
                            Log.d("Register", task.getException().toString());

                            // refine this error message later
                            errorMessage = "User with that email and password was not found. Please try again or create an account.";
                            success = false;
                            done = true;
                            // JUST START THE INTENT FROM HERE ? ASYNCHRONOUSLY?
                        }
                        caller.loginCallback(success);
                    }
                });
    }

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
