package cs2340team64.dirtyrat.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.data.DataBufferObserverSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cs2340team64.dirtyrat.R;
import cs2340team64.dirtyrat.model.Auth;
import cs2340team64.dirtyrat.model.Report;
import cs2340team64.dirtyrat.model.ReportList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private enum ViewState {
        WELCOME, LOGIN, REGISTER;
    }

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

    DatabaseReference db;
    DatabaseReference db_new;
    Auth auth;

    List<? extends View> welcomeWidgets;
    List<? extends View> loginWidgets;
    List<? extends View> registrationWidgets;
    List<TextView> messages;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);

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
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Report report = snapshot.getValue(Report.class);
                    ReportList.reports.add(report);
                }
                // now sorting in descending order
                Collections.sort(ReportList.reports, new Comparator<Report>() {
                    @Override
                    public int compare(Report rep1, Report rep2) {
                        return -rep1.compareTo(rep2);
                    }
                });
                ReportList.setLatestUniqueKey(ReportList.reports.get(0).getUnique_Key());
//                for (Report r : ReportList.reports) {
//                    System.out.println(r.getUnique_Key());
//                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // For now using a separate database key ("Reports_new") to store user-created entires
        //      just to be safe
        db_new = FirebaseDatabase.getInstance().getReference().child("Reports_new");
        db_new.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Report report = snapshot.getValue(Report.class);
                    ReportList.reports.add(report);
                }
                Collections.sort(ReportList.reports, new Comparator<Report>() {
                    @Override
                    public int compare(Report rep1, Report rep2) {
                        return -rep1.compareTo(rep2);
                    }
                });
                if (ReportList.reports.size() > 0) {
                    ReportList.setLatestUniqueKey(ReportList.reports.get(0).getUnique_Key());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.goto_login_button:
                changeState(ViewState.LOGIN);
                break;
            case R.id.login_button:
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

    private void clearInput() {
        email.setText("");
        password.setText("");
        confirmPassword.setText("");
    }

    private void clearMessages() {
        for (TextView text : messages) text.setText("");
    }

    private void login(String email, String password) {
        auth.login(this, email, password);
    }
    public void loginCallback(boolean success) {
        if (!success) {
            error.setText(auth.getError());
            clearInput();
        } else {
            Intent loggedIn = new Intent(this, LoggedInActivity.class);
            startActivity(loggedIn);
        }
    }

    private void register(String email, String password, String confirmPassword) {
        auth.register(this, email, password, confirmPassword, userTypeSpinner.getSelectedItem().equals("Admin"));
    }
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
