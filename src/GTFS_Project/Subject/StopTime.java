/**
 * SE2030 -021
 * Fall 2017
 * GTFS Project: StopTime
 * Copyright [2017] - [2022] Max Beihoff, Chris Betances, Eduardo Avilles
 * All Rights Reserved
 * 05 Oct 2017
 */
package GTFS_Project.Subject;

/**
 * @author Max Beihoff
 * @version 11/1/2017
 */
public class StopTime {
    private String tripId, arrivalTime, departureTime, stopId, stopSequence;

    /**
     * Default Constructor
     */
    public StopTime(){

    }

    /**
     * This is the Constructor for the StopTime Class with variables passed in
     *
     * @param tripId is the Trip that this StopTime is associated with
     * @param arrivalTime is the time that a bus arrives at this StopTime
     * @param departureTime is the time that a bus departs at this StopTime
     * @param stopId is the Identification number of this Stop
     * @param stopSequence is the index of where in the trip that this Stop is placed
     */
    public StopTime(String tripId, String arrivalTime, String departureTime, String stopId, String stopSequence){
        this.tripId = tripId;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.stopId = stopId;
        this.stopSequence = stopSequence;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public String getStopSequence() {
        return stopSequence;
    }

    public void setStopSequence(String stopSequence) {
        this.stopSequence = stopSequence;
    }

    /**
     * Method to compile data for a single StopTime in String form
     *
     * @return String value of this StopTime
     */
    public String export(){
        return String.format("%s,%s,%s,%s,%s,,,\n", tripId, arrivalTime, departureTime, stopId, stopSequence);
    }

    /**
     * Method that generates a string based on values inside a StopTime
     *
     * @return String of each variable within the StopTime
     */
    @Override
    public String toString(){
        return getTripId() + ", " + getArrivalTime() + ", " + getDepartureTime() + ", " + getStopId() + ", " + getStopSequence();
    }
}
