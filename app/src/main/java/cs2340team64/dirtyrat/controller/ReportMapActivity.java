package cs2340team64.dirtyrat.controller;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cs2340team64.dirtyrat.R;
import cs2340team64.dirtyrat.model.Report;
import cs2340team64.dirtyrat.model.ReportListWrapper;

/**
 * Created by diogo on 10/24/2017.
 */

public class ReportMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final LatLng NY = new LatLng(40.78, -73.97);
    private GoogleMap map;
    private SupportMapFragment mapFragment;

    ReportListWrapper reportListWrapper = ReportListWrapper.getInstance();
    ArrayList<Report> reportList;
    LinearLayout filterPane;
    EditText fromDate;
    EditText toDate;
    Button filter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_map_screen);

        filterPane = (LinearLayout) findViewById(R.id.linearLayout);
        //filterPane.setVisibility(View.GONE);
        fromDate = (EditText) findViewById(R.id.fromDate);
        toDate = (EditText) findViewById(R.id.toDate);
        filter = (Button) findViewById(R.id.filter_button);

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        fromDate.setText("08/01/2017");
        toDate.setText(sdf.format(new Date()));

        reportList = reportListWrapper.filter(fromDate.getText().toString(), toDate.getText().toString());

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportList = reportListWrapper.filter(fromDate.getText().toString(), toDate.getText().toString());
                refreshMap(reportList);
            }
        });
        //GOOGLE_MAPS_API_KEY=AIzaSyBcMyBcDZqB42YGGIaw4BE_1DQlru14Vig
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Map is ready to be used.
        map = googleMap;

        // Add a marker with a title that is shown in its info window.
        refreshMap(reportList);

        // Move the camera to show the marker.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(NY, 11));
    }

    public void refreshMap(ArrayList<Report> reportList) {
        map.clear();
        for (Report report : reportList) {
            map.addMarker(new MarkerOptions().position(new LatLng(report.getLatitude(), report.getLongitude()))
                    .title("Report id: " + report.getUnique_Key()).snippet(report.getCreated_Date()));
        }
    }
}
