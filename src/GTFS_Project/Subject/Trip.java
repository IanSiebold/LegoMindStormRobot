/**
 * SE2030 -021
 * Fall 2017
 * GTFS Project: Trip
 * Copyright [2017] - [2022] Max Beihoff, Chris Betances, Eduardo Avilles
 * All Rights Reserved
 * 05 Oct 2017
 */
package GTFS_Project.Subject;

/**
 * Created by avilese on 10/10/2017.
 */

public class Trip {

    private String routeID, tripID;

    /**
     * default constructor
     */
    public Trip(){
    }

    /**
     * constructor that instantiates the data
     *
     * @param routeID is the ID of the Route that this Trip is associated with
     * @param tripID is the Identification Number of this trip
     */
    public Trip(String routeID, String tripID){
        this.routeID = routeID;
        this.tripID = tripID;
    }

    public String getRouteID() {
        return routeID;
    }

    public void setRouteID(String routeID) {
        this.routeID = routeID;
    }

    public String getTripID() {
        return tripID;
    }

    public void setTripID(String tripID) {
        this.tripID = tripID;
    }

    public String export(){
        return String.format("%s,,%s,,,,\n", routeID, tripID);
    }

    /**
     * Method to convert value of this Class to a String
     *
     * @return String of each variable within the Trip
     */
    @Override
    public String toString(){
        return getRouteID() + ", " + getTripID();
    }
}