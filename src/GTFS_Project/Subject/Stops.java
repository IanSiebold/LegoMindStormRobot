/**
 * SE2030 -021
 * Fall 2017
 * GTFS Project: Stops
 * Copyright [2017] - [2022] Max Beihoff, Chris Betances, Eduardo Avilles
 * All Rights Reserved
 * 05 Oct 2017
 */
package GTFS_Project.Subject;

import GTFS_Project.GUI.Controller;
import GTFS_Project.Observer.Observer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import EV3Files.displayMessage;

/**
 * Class that has a list which contains a Stop objects
 *
 * @author Max Beihoff, Chris Betances
 * @version 1.0
 * 05-Oct-2017 4:43:36 PM
 */
public class Stops implements Subject {

    private List<Observer> observers;
    private List<Stop> stops;
    private Scanner inFile;
    private Trips trips;
    private Routes routes;
    private StopTimes stopTimes;
    private Controller controller;

    /**
     * Default constructor
     */
	public Stops()throws Exception{
        observers = new ArrayList<>();
        stops = new ArrayList<>();
    }

    /**
     * Constructor for Stops class with the stop file passed in
     *
     * @param stopsFile File that is used to initially populate the stops List
     */
    public Stops(File stopsFile)throws Exception{
        observers = new ArrayList<>();
        stops = new ArrayList<>();
        stopTimes = new StopTimes();
        importFileNew(stopsFile);
    }

    /**
     * Stops Getter
     *
     * @return stops
     */
    public List<Stop> getStops(){
        return stops;
    }

    /**
     * Stop Getter at specific index
     *
     * @param tgt Index
     * @return stop
     */
    public Stop getStop(int tgt){
        return stops.get(tgt);
    }

    /**
     * Getter that returns a single Stop
     *
     * @param tgtID index of the route in the routes List
     * @return Route at the given index
     */
    public Stop getStop(String tgtID){
        if(verifyStop(tgtID)) {
            for (Stop stop : stops) {
                if (stop.getStopID().equals(tgtID)) {
                    return stop;
                }
            }
        }
        return null;
    }

    /**
     * This method returns a List of the Routes that are included with the List of String passed in
     *
     * @param tgtIDs is the List of routeIDs passed in
     * @return List<Route> is passed back to be manipulated
     */
    public List<Stop> getStop(List<String> tgtIDs){
        List<Stop> foundStops = new ArrayList<>();
        for(String tgtRoute : tgtIDs){
            for(Stop stop : stops){
                if(stop.getStopID().equals(tgtRoute)){
                    foundStops.add(stop);
                }
            }
        }
        return foundStops;
    }

    /**
     * getter for size of the stops list
     *
     * @return int value of stops list size
     */
    public int getStopsSize(){
        return stops.size();
    }

    public void setTrips(Trips trips) {
        this.trips = trips;
    }

    public void setRoutes(Routes routes) {
        this.routes = routes;
    }

    public void setStopTimes(StopTimes stopTimes) {
        this.stopTimes = stopTimes;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
    
    public boolean importFileNew(File stopsFile)
    {
    	//stop_id,stop_name,stop_desc,stop_lat,stop_lon
		try {
			BufferedReader br = new BufferedReader(new FileReader(stopsFile));
			String readLine = br.readLine();
			readLine = br.readLine();
			while(readLine != null)
			{
				String[] line = readLine.split(",");
				addStop(new Stop(line[0], line[1], Double.valueOf(line[3]), Double.valueOf(line[4])));
				readLine = br.readLine();
			}
			br.close();
			return true;
		}
		catch (IOException e) {}
		catch (NullPointerException e) {} 
		catch (ArrayIndexOutOfBoundsException e) {} 
		catch (NumberFormatException e) {}
    	return false;
    }

    /**
     * Method to import a file to populate the stops List
     *
     * @param stopsFile that is to be used
     */
    public boolean importFile(File stopsFile)throws Exception {
        try {
            String inLine, stopID, name;
            double lat, lon;
            String[] splitLine;
            inFile = new Scanner(stopsFile);
            inFile.nextLine();
            while (inFile.hasNextLine()) {
                inLine = inFile.nextLine();
                splitLine = inLine.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                stopID = splitLine[0];
                name = splitLine[1];
                lat = Double.parseDouble(splitLine[3]);
                lon = Double.parseDouble(splitLine[4]);
                if (lat >= -90 && lat <= 90) {
                    if (lon >= -180 && lon <= 180) {
                        addStop(new Stop(stopID, name, lat, lon));
                    }
                }
            }
            inFile.close();
            return true;
        } catch (FileNotFoundException e) {
            controller.displayError(e.toString());
        } catch (ArrayIndexOutOfBoundsException e) {
            controller.displayError(e.toString());
        } catch (NumberFormatException e) {
            controller.displayError(e.toString());
        } catch (NullPointerException e) {
            controller.displayError(e.toString());
        }
        return false;
    }

    /**
     * Method to add a Stop to the Collection Stops
     *
     * @param stop Stop to be added
     */
    public void addStop(Stop stop){
        stops.add(stop);
        notifyObservers();
    }

    /**
     * Method to remove a Stop from the Collection stops
     *
     * @param stop Stop to be removed
     */
    public void removeStop(Stop stop){
        stops.remove(stop);
        notifyObservers();
    }

    /**
     * Method to allow an observer to subscribe to this subject
     *
     * @param o Observer
     * @return true if the Observer is successfully added
     */
    public boolean addObserver(Observer o) {
        if(o == null) {
            return false;
        }else{observers.add(o);
            return true;
        }
    }

    /**
     * Method to allow an observer to unsubscribe to this subject
     *
     * @param o Observer
     * @return true if the observer successfully unsubcribed
     */
    @Override
    public boolean removeObserver(Observer o) {
        if(o == null){
            return false;
        }else{
            observers.remove(o);
            return true;
        }
    }

    /**
     * Method to notify subscribed observers of an update
     */
    public void notifyObservers() {
        for(Observer o: observers)
            o.update();
    }

    /**
     * This Method is used to search for a stop to find all the routes that contain it.
     * Each List is traversed to find the IDs that match up linking each together.
     *
     * @param stopID is the String of the requested route
     * @return List of Routes that match the requested Stop
     */
    public List<Route> searchStop(String stopID){
        List<String> tgtIDs = new ArrayList<>();
        List<String> foundTripIDs = new ArrayList<>();
        List<String> foundRouteIDs = new ArrayList<>();
        List<Route> foundRoutes = new ArrayList<>();
        tgtIDs.add(stopID.toUpperCase());
        try {
            if (verifyStop(stopID)) {
            	foundTripIDs = stopTimes.getStopTimeByStop(tgtIDs);
            } else {
                controller.displayError("The stop ID you searched is not valid");
            }
            if (!foundTripIDs.isEmpty()) {
                foundRouteIDs = trips.getTripByTrip(foundTripIDs);
            } else {
                controller.displayError("The trip ID you searched could not be located");
            }
            if (!foundRouteIDs.isEmpty()) {
                foundRoutes = routes.getRoute(foundRouteIDs);
            } else {
                controller.displayError("The route IDs you searched could not be located");
            }
            if (foundRoutes.size() > 0) {
                return foundRoutes;
            } else {
                controller.displayError("The stop ID you searched was not found");
            }
        }catch (NullPointerException e){
            controller.displayError("A stop ID was not chosen");
        }
        return null;
    }
    
    /**
     * This Method is used to search for a stop to find all the routes that contain it.
     * Each List is traversed to find the IDs that match up linking each together.
     *
     * @param stopID is the String of the requested route
     * @param times the list of StopTimes
     * @param trips the list of Trips
     * @return List of Routes that match the requested Stop
     */
    public List<String> searchStopEV3(String stopID, StopTimes times, Trips trips){
    	List<String> tgtIDs = new ArrayList<>();
        List<String> foundTripIDs = new ArrayList<>();
        List<String> foundRouteIDs = new ArrayList<>();
        tgtIDs.add(stopID.toUpperCase());
        try {
        	foundTripIDs = times.getStopTimeByStop(tgtIDs);
            foundRouteIDs.add(foundTripIDs.get(0));
            //getTripByTrip returns a list of Routes
            foundRouteIDs = trips.getTripByTrip(foundTripIDs);
        }catch (NullPointerException e){
        	return foundRouteIDs;
        }catch (IndexOutOfBoundsException e) {
        	return foundRouteIDs;
        }
        return foundRouteIDs;
    }

    /**
     * This method verifies that the Route to be searched is valid
     *
     * @param tgtID is the routeID being passed in
     * @return true if the routeID is valid
     */
    public boolean verifyStop(String tgtID) {
        for (Stop stop : stops) {
            if (stop.getStopID().equals(tgtID)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method to compile the data in the stops List to be saved to a file
     *
     * @return the String value of the whole stops List
     */
    public String export(){
        if(!stops.isEmpty()) {
            String stopData = "stop_id,stop_name,stop_desc,stop_lat,stop_lon\n";
            for (Stop stop : stops) {
                stopData += stop.export();
            }
            return stopData;
        }else{
            controller.displayError("The list for Stops is empty");
            return null;
        }
    }

    /**
     * Method that provides a current version of the Stops Object
     *
     * @return this instance of Stops
     */
    @Override
    public Stops update() {
        return this;
    }
}