/**
 * SE2030 -021
 * Fall 2017
 * GTFS Project: Stop
 * Copyright [2017] - [2022] Max Beihoff, Chris Betances, Eduardo Avilles
 * All Rights Reserved
 * 05 Oct 2017
 */
package GTFS_Project.Subject;

/**
 * Class that contains individual Stop Object
 * @author Max Beihoff
 * @version 11/1/2017
 */
public class Stop {
    private String stopID, name;
    private double latitude, longitude;

    /**
     * Default Constructor
     */
    public Stop(){

    }

    /**
     * This is the constructor for the Stop class with variables passed in.
     *
     * @param stopID is the identification number for the Stop
     * @param name is the name of the Stop
     * @param lat is the Latitude of the Stop
     * @param lon is the Longitude of the Stop
     */
    public Stop(String stopID , String name,  double lat, double lon){

        this.stopID = stopID;
        this.name = name;
        this.latitude = lat;
        this.longitude = lon;
    }

    public String getStopID() {
        return stopID;
    }

    public void setStopID(String stopID) {
        this.stopID = stopID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Method to compile data for a single Stop in String form
     *
     * @return String value of this Stop
     */
    public String export(){
        return String.format("%s,%s,,%s,%s\n", stopID, name, latitude, longitude);
    }

    /**
     * Method to converts value of the Class to a String
     *
     * @return String of each variable within the Stop
     */
    @Override
    public String toString(){
        return getStopID() + ", " + getName() + ", " + getLatitude() + ", " +getLongitude();
    }
}
