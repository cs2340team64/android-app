package cs2340team64.dirtyrat.model;

import android.os.Environment;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by diogo on 10/12/2017.
 */

public class ReportListWrapper implements Serializable {

    private static ReportListWrapper instance;

    private TreeMap<Long, Report> reportMap = new TreeMap<>();
    private TreeMap<Long, AtomicInteger> reportsPerDay = new TreeMap<>();
    private Report currentReport;
    private long latestUniqueKey;

/*
    private ReportListWrapper() {
        try {
            readReportList();
            Log.d("Persistence", "attempting to read ArrayList from memory");
        } catch (Exception e) {
            reports = new ArrayList<>();
            latestUniqueKey = 0;
            Log.d("Persistence", "creating a new ArrayList");
        }
    }
*/

    private ReportListWrapper() {
        latestUniqueKey = 0;
        Log.d("Persistence", "creating a new ArrayList");
    }

    /**
     * Gets singleton instance of ReportListWrapper
     * @return ReportListWrapper object
     */
    public static ReportListWrapper getInstance() {
        if (instance == null) {
            instance = new ReportListWrapper();
        }
        return instance;
    }


    /**
     * Sets the current report that the user is looking at (used for ReportDetailActivity)
     * @param report the current report
     */
    public void setCurrentReport(Report report) {
        currentReport = report;
    }

    public Report getCurrentReport() {
        return currentReport;
    }

    /**
     * Returns the ArrayList with reports
     * @return reportList
     */

    public ArrayList<Report> getAllReports() {
        return new ArrayList<>(reportMap.values());
    }

    public ArrayList<Report> filter(long minDate, long maxDate) {
       // -20171022000000L, -20171023000000L returns 10/22/2017
        Log.d("filter","Filtering with minDate=" + minDate + " and maxDate=" + maxDate);
        try {
            TreeMap copy = new TreeMap<>(reportMap);
            return new ArrayList<>(copy.tailMap(maxDate, false).headMap(minDate, true).values());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Wrapper method for ArrayList.add()
     * @param report the report to add
     */
    public void add(Report report) {
        long code = uniqueReportDateTimeCode(report);
        reportMap.put(code, report);
        Log.d("Logic", "report date: " + report.getCreated_Date() + " datetimecode: " + code);
    }

    public void printKeys() {
        for (Long num : reportMap.keySet()) {
            Log.d("treemap", num + "");
        }
    }

    public Long dateTimeCode(String date, boolean addOne) {
        //Log.d("filter", date);
        String[] split = date.split("/");
        int month = Integer.valueOf(split[0]);
        int day = Integer.valueOf(split[1]);
        int year;
        if (split[2].length() > 4) {
            year = Integer.valueOf(split[2].substring(0, 4));
        } else {
            year = Integer.valueOf(split[2]);
        }

        // Fix this later to include the actual days per month and not just 31 days for every month
        day = addOne ? ++day : day;
        if (day > 31) {
            day = 1;
            ++month;
            if (month > 12) {
                month = 1;
                ++year;
            }
        }

        // multiply this result by 1,000,000, this allows up to 1,000,000 reports per day
        long code = -1000000L * ((year * 10000) + (month * 100) + day);
        //Log.d("filter", code + "");
        return code;
    }

    public Long uniqueReportDateTimeCode(Report report) {
        try {

            long code = dateTimeCode(report.getCreated_Date(), false);

            AtomicInteger entry = reportsPerDay.get(code);
            if (entry != null) {
                int next = reportsPerDay.get(code).getAndIncrement();
                code = code - next;
            } else {
                reportsPerDay.put(code, new AtomicInteger(1));
            }
            // code has format: YYYYMMDDXXXXXX  (where XXXXXX is the report# for that specific date)
            return code;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * generates the next unique key and updates the latestUniqueKey
     * @return the next unique key
     */
    public long generateNextUniqueKey() {
        return reportMap.firstEntry().getValue().getUnique_Key() + 10;
    }

}
