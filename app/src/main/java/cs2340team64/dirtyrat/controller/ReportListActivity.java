package cs2340team64.dirtyrat.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cs2340team64.dirtyrat.R;
import cs2340team64.dirtyrat.model.Report;
import cs2340team64.dirtyrat.model.ReportListWrapper;

/**
 * Created by diogo on 10/12/2017.
 */

public class ReportListActivity extends Activity {

    ListView listView;
    ArrayAdapter<Report> adapter;
    ReportListWrapper reportListWrapper = ReportListWrapper.getInstance();
    LinearLayout filterPane;

    EditText fromDate;
    EditText toDate;
    Button filter;


    /**
     * activity startup code
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_list_screen);

        filterPane = (LinearLayout) findViewById(R.id.linearLayout);
        //filterPane.setVisibility(View.GONE);

        fromDate = (EditText) findViewById(R.id.fromDate);
        toDate = (EditText) findViewById(R.id.toDate);
        filter = (Button) findViewById(R.id.filter_button);

        listView = (ListView) findViewById(R.id.list);
        //adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, reportListWrapper.getAllReports());

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        fromDate.setText(sdf.format(new Date(0, 0, 1)));
        toDate.setText(sdf.format(new Date()));

        ArrayList<Report> initialList = reportListWrapper.filter(reportListWrapper.dateTimeCode(fromDate.getText().toString(), false),
                reportListWrapper.dateTimeCode(toDate.getText().toString(), true));
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, initialList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                Report report = (Report) o;
                reportListWrapper.setCurrentReport(report);
                openDetailView();
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clear();
                adapter.addAll(
                        reportListWrapper.filter(reportListWrapper.dateTimeCode(fromDate.getText().toString(), false),
                                reportListWrapper.dateTimeCode(toDate.getText().toString(), true))
                );
                adapter.notifyDataSetChanged();
            }
        });

    }


    /**
     * opens a detailed view of the report that was clicked on
     */
    public void openDetailView() {
        startActivity(new Intent(this, ReportDetailActivity.class));
    }

}
