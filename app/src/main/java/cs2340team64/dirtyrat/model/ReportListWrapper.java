package cs2340team64.dirtyrat.model;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by diogo on 10/12/2017.
 */

public class ReportListWrapper implements Serializable {

    private static ReportListWrapper instance;

    private ArrayList<Report> reports = new ArrayList<>();
    private Report currentReport;

    private ReportListWrapper() {
        try {
            readReportList();
            Log.d("Persistence", "attempting to read ArrayList from memory");
        } catch (Exception e) {
            reports = new ArrayList<>();
            Log.d("Persistence", "creating a new ArrayList");
        }
    }

    public static ReportListWrapper getInstance() {
        if (instance == null) {
            instance = new ReportListWrapper();
        }
        return instance;
    }



    public void setCurrentReport(Report report) {
        currentReport = report;
    }

    public Report getCurrentReport() {
        return currentReport;
    }

    public void saveReportList() {
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "/reports2.txt");
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(reports);
            oos.close();
            Log.d("Persistence", "saving report list of size " + reports.size());
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Log.d("Persistence", "successfully saved report list of size: " + ((ArrayList<Report>) ois.readObject()).size());
            ois.close();
        } catch (Exception e) {
            Log.d("Persistence", "could not save report list");
            e.printStackTrace();
        }
    }

    public void readReportList() {
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "/reports2.txt");
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            reports = ((ArrayList<Report>) ois.readObject());
            Log.d("Persistence", "reading report list of size: " + (reports.size()));
            ois.close();
        } catch (Exception e) {
            Log.d("Persistence", "could not read report list");
            e.printStackTrace();
        }
    }

    public ArrayList<Report> getList() {
        return reports;
    }

    public void add(Report report) {
        reports.add(report);
    }

    public void sort() {
        Collections.sort(reports);
    }

}
