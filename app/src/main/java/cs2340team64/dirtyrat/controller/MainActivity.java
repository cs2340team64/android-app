package cs2340team64.dirtyrat.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cs2340team64.dirtyrat.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);

        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ((EditText) findViewById(R.id.login_username)).getText().toString();
                String password = ((EditText) findViewById(R.id.login_password)).getText().toString();
                Log.d("login attempt", "username: " + username + " | password: " + password);
                login(username, password);
            }
        });
    }

    private void login(String username, String password) {
        if (username.equalsIgnoreCase("user") && password.equals("pass")) {
            Intent loginSuccess = new Intent(this, LoggedIn.class);
            startActivity(loginSuccess);
        }
    }

}
