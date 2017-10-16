package cs2340team64.dirtyrat.model;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by diogo on 10/12/2017.
 */

public class ReportList {

    public static ArrayList<Report> reports = new ArrayList<>();

    private static Report currentReport;

    private static long latestUniqueKey;

    public static void setCurrentReport(Report report) {
        currentReport = report;
    }

    public static Report getCurrentReport() {
        return currentReport;
    }

    public static void setLatestUniqueKey(long uk) { latestUniqueKey = uk; }

    public static long getLatestUniqueKey() { return latestUniqueKey; }

    public static long generateNextUniqueKey() { return latestUniqueKey + 10; }

    public static void updateFromDataSnapshot(DataSnapshot dataSnapshot) {
        // update <reports> from DataSnapshot object

        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            Report report = snapshot.getValue(Report.class);
            reports.add(report);
        }
        // now sorting in descending order
        Collections.sort(reports, new Comparator<Report>() {
            @Override
            public int compare(Report rep1, Report rep2) {
                return -rep1.compareTo(rep2);
            }
        });

        if (reports.size() > 0) {
            setLatestUniqueKey(reports.get(0).getUnique_Key());
        }

//        for (Report r : reports) {
//            System.out.println(r.getUnique_Key());
//        }
    }
}
