package cs2340team64.dirtyrat.model;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * Created by diogo on 10/12/2017.
 */

public class Report implements Comparable<Report>, Serializable {
    private long Unique_Key;
    private double Latitude;
    private double Longitude;
    private String City;
    private String Borough;
    private String Incident_Address;
    private String Incident_Zip;
    private String Location_Type;
    private String Created_Date;

    @Override
    public int compareTo(Report other) {
        // temporary: later should probably group by distance from the user
        return (int) (other.getUnique_Key() - this.Unique_Key);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (!(o instanceof Report)) {
            return false;
        } else {
            return ((Report) o).getUnique_Key() == Unique_Key;
        }
    }

    public Report() {
    }

    public long getUnique_Key() {
        return Unique_Key;
    }

    public double getLatitude() {
        return Latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public String getCity() {
        return City;
    }

    public String getBorough() {
        return Borough;
    }

    public String getIncident_Address() {
        return Incident_Address;
    }

    public String getIncident_Zip() {
        return Incident_Zip;
    }

    public String getLocation_Type() {
        return Location_Type;
    }

    public String getCreated_Date() {
        return Created_Date;
    }

    public void setUnique_Key(long unique_Key) {
        Unique_Key = unique_Key;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public void setCity(String city) {
        City = city;
    }

    public void setBorough(String borough) {
        Borough = borough;
    }

    public void setIncident_Address(String incident_Address) {
        Incident_Address = incident_Address;
    }

    public void setIncident_Zip(String incident_Zip) {
        Incident_Zip = incident_Zip;
    }

    public void setLocation_Type(String location_Type) {
        Location_Type = location_Type;
    }

    public void setCreated_Date(String created_Date) {
        Created_Date = created_Date;
    }

    @Override
    public String toString() {
        return "Rat Sighting Report #" + Unique_Key;
    }
}

