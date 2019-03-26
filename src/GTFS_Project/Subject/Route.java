/**
 * SE2030 -021
 * Fall 2017
 * GTFS Project: Route
 * Copyright [2017] - [2022] Max Beihoff, Chris Betances, Eduardo Avilles
 * All Rights Reserved
 * 05 Oct 2017
 */
package GTFS_Project.Subject;

/**
 * @author beihoffmc
 * @version 1.0
 * 05-Oct-2017 4:43:36 PM
 */
public class Route {

    private String routeID, color;

    /**
     * Default Constructor
     */
    public Route(){}

    /**
     * Constructor for the Route Class with all values in the data line passed in
     *
     * @param routeID is the identification number for this Route
     * @param color is the identification color for this Route
     */
    public Route(String routeID, String color) {
        this.routeID = routeID;
        this.color = color;
    }

    public String getRouteID() {
        return routeID;
    }

    public void setRouteID(String routeID) {
        this.routeID = routeID;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Method to compile data for a single Route in String form
     *
     * @return String value of this Route
     */
    public String export(){
        return String.format("%s,,,,,,,%s,\n", routeID, color);
    }

    /**
     * Method to converts value of the Class to a String
     *
     * @return String of each variable within the Stop
     */
    @Override
    public String toString(){
        return getRouteID() + ", " + getColor();
}
}
