package cs2340team64.dirtyrat.controller;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

import cs2340team64.dirtyrat.R;
import cs2340team64.dirtyrat.model.Report;
import cs2340team64.dirtyrat.model.ReportList;

/**
 * Created by lei on 10/13/17.
 */

public class ReportCreateActivity extends Activity {

    private EditText cityInput;
    private EditText boroughInput;
    private EditText addressInput;
    private EditText zipcodeInput;
    private Spinner propertyTypeInput;
    private EditText dateInput;
    private EditText timeInput;

    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_create_screen);

        cityInput = (EditText) findViewById(R.id.create_input_city);
        boroughInput = (EditText) findViewById(R.id.create_input_borough);
        addressInput = (EditText) findViewById(R.id.create_input_address);
        zipcodeInput = (EditText) findViewById(R.id.create_input_zipcode);
        propertyTypeInput = (Spinner)  findViewById(R.id.create_input_propertyType);
        dateInput = (EditText) findViewById(R.id.create_input_date);
        timeInput = (EditText) findViewById(R.id.create_input_time);

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,
                Report.allPropertyTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        propertyTypeInput.setAdapter(adapter);

        // populate the date/time fileds with current date/time
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ssaaa");
        String[] currentDateTimeStrings = sdf.format(new Date()).split(" ");
        dateInput.setText(currentDateTimeStrings[0]);
        timeInput.setText(currentDateTimeStrings[1]);

        Button reportAddButton = (Button) findViewById(R.id.report_add_button);
        Button reportCancelButton = (Button) findViewById(R.id.report_cancel_button);

        reportAddButton.setOnClickListener(reportCreateListner);
        reportCancelButton.setOnClickListener(reportCreateListner);
    }

    private View.OnClickListener reportCreateListner = new View.OnClickListener() {
        public void onClick(View v) {
            if (v.getId() == R.id.report_add_button) {

                // create new Report object
                Report rep = new Report();
                rep.setCity(cityInput.getText().toString());
                rep.setBorough(boroughInput.getText().toString());
                String dateText = dateInput.getText().toString();
                String timeText = timeInput.getText().toString();
                rep.setCreated_Date(dateText + " "
                        + timeText.substring(0, timeText.length() - 2) + " "
                        + timeText.substring(timeText.length() - 2));
                rep.setIncident_Address(addressInput.getText().toString());
                rep.setIncident_Zip(zipcodeInput.getText().toString());
                rep.setLocation_Type(propertyTypeInput.getSelectedItem().toString());

                // hard code the coordinates for now
                rep.setLatitude(40.7831);
                rep.setLongitude(73.9712);
                rep.setUnique_Key(ReportList.generateNextUniqueKey());

                // push to database
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Reports_new");
                mDatabase.push().setValue(rep);
                finish();

            } else if (v.getId() == R.id.report_cancel_button) {
                finish();
            }
        }
    };


}
