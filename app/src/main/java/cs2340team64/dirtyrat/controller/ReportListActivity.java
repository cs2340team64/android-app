package cs2340team64.dirtyrat.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

    /**
     * activity startup code
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_list_screen);

        listView = (ListView) findViewById(R.id.list);
        //adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, reportListWrapper.getReports());
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, reportListWrapper.filter(-20171022000000L, -20171023000000L));
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
    }

    /**
     * opens a detailed view of the report that was clicked on
     */
    public void openDetailView() {
        startActivity(new Intent(this, ReportDetailActivity.class));
    }

}
