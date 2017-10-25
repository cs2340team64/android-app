package cs2340team64.dirtyrat.controller;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cs2340team64.dirtyrat.R;
import cs2340team64.dirtyrat.model.Auth;
import cs2340team64.dirtyrat.model.Report;
import cs2340team64.dirtyrat.model.ReportListWrapper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private enum ViewState {
        WELCOME, LOGIN, REGISTER;
    }

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    Button goToLoginButton;
    Button loginButton;
    Button goToRegistrationButton;
    Button registerButton;
    TextView description;
    TextView cancel;
    TextView successText;
    TextView error;
    EditText email;
    EditText password;
    EditText confirmPassword;
    Spinner userTypeSpinner;

    ReportListWrapper reportListWrapper;
    ArrayList<Report> reportList;
    DatabaseReference db;
    DatabaseReference db_new;
    Auth auth;

    long lastFetch;

    List<? extends View> welcomeWidgets;
    List<? extends View> loginWidgets;
    List<? extends View> registrationWidgets;
    List<TextView> messages;


    /**
     * acitivity startup
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);

        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }

        // Link all the widgets to the UI
        goToLoginButton = (Button) findViewById(R.id.goto_login_button);
        goToRegistrationButton = (Button) findViewById(R.id.goto_register_button);
        loginButton = (Button) findViewById(R.id.login_button);
        registerButton = (Button) findViewById(R.id.register_button);
        description = (TextView) findViewById(R.id.app_description);
        successText = (TextView) findViewById(R.id.account_creation_success);
        error = (TextView) findViewById(R.id.error_message);
        cancel = (TextView) findViewById(R.id.cancel_link);
        email = (EditText) findViewById(R.id.login_email);
        password = (EditText) findViewById(R.id.login_password);
        confirmPassword = (EditText) findViewById(R.id.confirm_password);
        userTypeSpinner = (Spinner) findViewById(R.id.user_type_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, new String[]{"User", "Admin"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTypeSpinner.setAdapter(adapter);

        // Set click event listeners
        goToLoginButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        goToRegistrationButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        cancel.setOnClickListener(this);

        // Group widgets into lists to easily show/hide (see ChangeState() method)
        // welcomeWidgets is the first screen you see when you open the app, loginWidgets is the
        // login screen, and registrationWidgets is the registration screen
        welcomeWidgets = Arrays.asList(goToLoginButton, goToRegistrationButton, description);
        loginWidgets = Arrays.asList(loginButton, cancel, email, password);
        registrationWidgets = Arrays.asList(registerButton, cancel, email, password, confirmPassword, userTypeSpinner);
        messages = Arrays.asList(successText, error);


        auth = Auth.getInstance();

        // Set starting state to the welcome screen
        changeState(ViewState.WELCOME);

        // The following block pulls the list of reports from Firebase
        db = FirebaseDatabase.getInstance().getReference().child("Reports");

        reportListWrapper = ReportListWrapper.getInstance();

        lastFetch = 0;

        //Query query = db.limitToFirst(50);
        //query.addChildEventListener(new ChildEventListener() {
        db.addChildEventListener(new ChildEventListener() {
        @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Report report = dataSnapshot.getValue(Report.class);
                reportListWrapper.add(report);
                long currentTime = System.currentTimeMillis();
                if ((currentTime - lastFetch) > 100) {
                    Log.d("Firebase", "Grabbing report id#" + report.getUnique_Key() + " from Firebase");
                    lastFetch = currentTime;
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // ignore for now
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // ignore for now
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                // ignore for now
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Firebase", databaseError.getMessage());
                Log.d("Firebase", databaseError.getDetails());
            }
        });

    }

    /**
     * Universal click listener for this activity
     * @param view the view that is clicked on
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.goto_login_button:
                changeState(ViewState.LOGIN);
                break;
            case R.id.login_button:
                //reportListWrapper.printKeys();
                login(email.getText().toString(), password.getText().toString());
                break;
            case R.id.goto_register_button:
                changeState(ViewState.REGISTER);
                break;
            case R.id.register_button:
                register(email.getText().toString(), password.getText().toString(), confirmPassword.getText().toString());
                break;
            case R.id.cancel_link:
                clearInput();
                changeState(ViewState.WELCOME);
                break;
        }
    }

    /**
     * Changes the layout state between the welcoem screen, login screen, and register screen
     * @param state the state to transition to
     */
    private void changeState(ViewState state) {
        clearMessages();
        switch (state) {
            case WELCOME:
                for (View view : welcomeWidgets) view.setVisibility(View.VISIBLE);
                for (View view : loginWidgets) view.setVisibility(View.GONE);
                for (View view : registrationWidgets) view.setVisibility(View.GONE);
                break;
            case LOGIN:
                for (View view : welcomeWidgets) view.setVisibility(View.GONE);
                for (View view : registrationWidgets) view.setVisibility(View.GONE);
                for (View view : loginWidgets) view.setVisibility(View.VISIBLE);
                break;
            case REGISTER:
                for (View view : welcomeWidgets) view.setVisibility(View.GONE);
                for (View view : loginWidgets) view.setVisibility(View.GONE);
                for (View view : registrationWidgets) view.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * clears the email/password fields
     */
    private void clearInput() {
        email.setText("");
        password.setText("");
        confirmPassword.setText("");
    }

    /**
     * clears TextView messages
     */
    private void clearMessages() {
        for (TextView text : messages) text.setText("");
    }

    /**
     * wrapper for the auth login method
     * @param email login email
     * @param password login password
     */
    private void login(String email, String password) {
        if (email == null || email.equals("") || password == null || password.equals("")) {
            return;
        }
        auth.login(this, email, password);
    }

    /**
     * callback for the auth.login method
     * @param success true if login was successful
     */
    public void loginCallback(boolean success) {
        if (!success) {
            error.setText(auth.getError());
            clearInput();
        } else {
            Intent loggedIn = new Intent(this, LoggedInActivity.class);
            startActivity(loggedIn);
        }
    }

    /**
     * wrapper for the auth.register method
     * @param email registration email
     * @param password registraition password
     * @param confirmPassword registration password
     */
    private void register(String email, String password, String confirmPassword) {
        auth.register(this, email, password, confirmPassword, userTypeSpinner.getSelectedItem().equals("Admin"));
    }

    /**
     * callback for the async auth.register method
     * @param success true if registration was successful
     */
    public void registerCallback(boolean success) {
        Log.d("Register", "Auth done");
        if (!success) {
            error.setText(auth.getError());
            clearInput();
        } else {
            changeState(ViewState.WELCOME);
            successText.setText("Successfully created account!");
            clearInput();
        }
    }

}
