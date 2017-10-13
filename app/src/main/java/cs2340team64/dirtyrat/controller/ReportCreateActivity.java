package cs2340team64.dirtyrat.controller;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import cs2340team64.dirtyrat.R;
import cs2340team64.dirtyrat.model.Report;
import cs2340team64.dirtyrat.model.ReportList;

/**
 * Created by lei on 10/13/17.
 */

public class ReportCreateActivity extends Activity {

    private EditText city;

    private Button reportAddButton;
    private Button reportCancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_create_screen);

        city = (EditText) findViewById(R.id.city_input);

        reportAddButton = (Button) findViewById(R.id.report_add_button);
        reportCancelButton = (Button) findViewById(R.id.report_cancel_button);

        reportAddButton.setOnClickListener(reportCreateListner);
        reportCancelButton.setOnClickListener(reportCreateListner);
    }

    private View.OnClickListener reportCreateListner = new View.OnClickListener() {
        public void onClick(View v) {
            if (v.getId() == R.id.report_add_button) {
                Report rep = new Report();
                String city_str = city.getText().toString();
                rep.setCity(city_str);

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.push().setValue(city_str);

            } else if (v.getId() == R.id.report_cancel_button) {
                finish();
            }
        }
    };


}
