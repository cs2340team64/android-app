package cs2340team64.dirtyrat.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cs2340team64.dirtyrat.R;
import cs2340team64.dirtyrat.model.Auth;

public class LoggedIn extends AppCompatActivity implements View.OnClickListener {

    TextView welcomeUser;
    Button logoutButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logged_in_screen);

        welcomeUser = (TextView) findViewById(R.id.welcome_user_text);
        logoutButton = (Button) findViewById(R.id.logout_button);

        logoutButton.setOnClickListener(this);

        welcomeUser.setText("Welcome, " + Auth.getCurrentUsername());
    }

    @Override
    public void onClick(View view) {
        logout();
    }

    public void logout() {
        Intent logout = new Intent(this, MainActivity.class);
        startActivity(logout);
    }
}
