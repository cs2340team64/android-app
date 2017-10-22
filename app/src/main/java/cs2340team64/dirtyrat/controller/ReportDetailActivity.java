package cs2340team64.dirtyrat.controller;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import cs2340team64.dirtyrat.R;
import cs2340team64.dirtyrat.model.Report;
import cs2340team64.dirtyrat.model.ReportListWrapper;

/**
 * Created by diogo on 10/12/2017.
 */

public class ReportDetailActivity extends Activity {

    TextView key, date, type, zip, address, city, borough, lat, lon;

    /**
     * activity startup code
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_detail_screen);

        key = (TextView) findViewById(R.id.field_report_number);
        date = (TextView) findViewById(R.id.field_date);
        type = (TextView) findViewById(R.id.field_location_type);
        zip = (TextView) findViewById(R.id.field_zip);
        address = (TextView) findViewById(R.id.field_address);
        city = (TextView) findViewById(R.id.field_city);
        borough = (TextView) findViewById(R.id.field_borough);
        lat = (TextView) findViewById(R.id.field_lat);
        lon = (TextView) findViewById(R.id.field_lon);

        Report report = ReportListWrapper.getInstance().getCurrentReport();

        key.setText("" + report.getUnique_Key());
        date.setText(report.getCreated_Date());
        type.setText(report.getLocation_Type());
        zip.setText(report.getIncident_Zip());
        address.setText(report.getIncident_Address());
        city.setText(report.getCity());
        borough.setText(report.getBorough());
        lat.setText(String.format("%.6f", report.getLatitude()));
        lon.setText(String.format("%.6f", report.getLongitude()));




    }
}
