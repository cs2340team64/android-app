package cs2340team64.dirtyrat.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import cs2340team64.dirtyrat.R;
import cs2340team64.dirtyrat.model.Auth;

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
    TextView success;
    TextView error;
    EditText username;
    EditText password;
    EditText confirmPassword;

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
        success = (TextView) findViewById(R.id.account_creation_success);
        error = (TextView) findViewById(R.id.error_message);
        cancel = (TextView) findViewById(R.id.cancel_link);
        username = (EditText) findViewById(R.id.login_username);
        password = (EditText) findViewById(R.id.login_password);
        confirmPassword = (EditText) findViewById(R.id.confirm_password);

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
        loginWidgets = Arrays.asList(loginButton, cancel, username, password);
        registrationWidgets = Arrays.asList(registerButton, cancel, username, password, confirmPassword);
        messages = Arrays.asList(success, error);

        // Set starting state to the welcome screen
        changeState(ViewState.WELCOME);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.goto_login_button:
                changeState(ViewState.LOGIN);
                break;
            case R.id.login_button:
                login(username.getText().toString(), password.getText().toString());
                break;
            case R.id.goto_register_button:
                changeState(ViewState.REGISTER);
                break;
            case R.id.register_button:
                register(username.getText().toString(), password.getText().toString(), confirmPassword.getText().toString());
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
        username.setText("");
        password.setText("");
        confirmPassword.setText("");
    }

    private void clearMessages() {
        for (TextView text : messages) text.setText("");
    }

    private void login(String username, String password) {
        boolean status = Auth.login(username, password);
        if (!status) {
            error.setText(Auth.getError());
            clearInput();
        } else {
            Intent loggedIn = new Intent(this, LoggedIn.class);
            startActivity(loggedIn);
        }
    }

    private void register(String username, String password, String confirmPassword) {
        boolean status = Auth.register(username, password, confirmPassword);
        if (!status) {
            error.setText(Auth.getError());
            clearInput();
        } else {
            changeState(ViewState.WELCOME);
            success.setText("Successfully created account: \"" + username + "\"!");
            clearInput();
        }
    }

}
