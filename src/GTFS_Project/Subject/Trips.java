/**
 * SE2030 -021
 * Fall 2017
 * GTFS Project: Trips
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

/**
 * This class contanis the Arraylist of Trip objects
 * @author beihoffmc
 * @version 1.0
 * 05-Oct-2017 4:43:36 PM
 */
public class Trips implements Subject {

    private List<Observer> observers;
    private List<Trip> trips;
    private Scanner inFile;
    private Controller controller;

    /**
     * Default Constructor
     */
    public Trips() throws Exception{
        trips = new ArrayList<>();
        observers = new ArrayList<>();
    }

    /**
     * Constructor for Trips class with the tripsFile file passed in
     *
     * @param tripsFile File that is used to initially populate the trips List
     */
    public Trips(File tripsFile) throws Exception {
        trips = new ArrayList<>();
        observers = new ArrayList<>();
        importFileNew(tripsFile);
    }

    public List<Trip> getTrips(){
        return trips;
    }

    /**
     * Trip Getter at specific index
     *
     * @param tgt Target index
     * @return Trip is returned based in the request
     */
    public Trip getTrip(int tgt){
        return trips.get(tgt);
    }

    /**
     * This method returns a List of tripsIDs associated with the ids of routeIDs passed in without repeats
     *
     * @param tgtIDs is the List of tgtRouteIDs
     * @return List<String> of the found tripIDs
     */
    public List<String> getTripByRoute(List<String> tgtIDs){
        List<String> foundTrips = new ArrayList<>();
        for (String tgtID : tgtIDs) {
            for (Trip trip : trips) {
                if (trip.getRouteID().equals(tgtID) && !foundTrips.contains(trip.getTripID())) {
                    foundTrips.add(trip.getTripID());
                }
            }
        }
        return foundTrips;
    }

    /**
     * This method returns a List of routeIDs associated with the is of tripsIDs passed in without repeats
     *
     * @param tgtIDs is the List of tgtTripIDs
     * @return List<String> of the found routeIDs
     */
    public List<String> getTripByTrip(List<String> tgtIDs){
        List<String> foundRoutes = new ArrayList<>();
        for (String tgtID : tgtIDs) {
            for (Trip trip : trips) {
                if (trip.getTripID().equals(tgtID) && !foundRoutes.contains(trip.getRouteID())) {
                    foundRoutes.add(trip.getRouteID());
                }
            }
        }
        return foundRoutes;
    }

    /**
     * Trips Size Getter method
     *
     * @return size int of the size of the trips List
     */
    public int getTripsSize(){
        return trips.size();
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
    
    public boolean importFileNew(File tripsFile)
    {
    	//route_id,service_id,trip_id,trip_headsign,direction_id,block_id,shape_id
		try {
			BufferedReader br = new BufferedReader(new FileReader(tripsFile));
			String readLine = br.readLine();
			readLine = br.readLine();
			while(readLine != null)
			{
				String[] line = readLine.split(",");
				addTrip(new Trip(line[0], line[2]));
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
     * Method to import a file to populate the trips List
     *
     * @param tripsFile that is to be used
     */
    public boolean importFile(File tripsFile) throws Exception {
        try {
            String routeID = "", tripID = "";
            String[] splitLine;
            inFile = new Scanner(tripsFile);
            inFile.nextLine();
            while (inFile.hasNextLine()) {
                splitLine = inFile.nextLine().split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                routeID = splitLine[0];
                tripID = splitLine[2];
                addTrip(new Trip(routeID, tripID));
            }
            inFile.close();
            return true;
        } catch (NullPointerException e) {
            controller.displayError(e.toString());
        } catch (ArrayIndexOutOfBoundsException e) {
            controller.displayError(e.toString());
        } catch (NumberFormatException e) {
            controller.displayError(e.toString());
        } catch (FileNotFoundException e) {
            controller.displayError(e.toString());
        }
        return false;
    }

    /**
     * Method to add a trip to the trips List
     *
     * @param trip Trip to be added
     */
    public void addTrip(Trip trip) {
        trips.add(trip);
        notifyObservers();
    }

    /**
     * Method to remove a Trip from the trips List
     *
     * @param trip Trip to be removed
     */
    public void removeTrip(Trip trip) {
        trips.remove(trip);
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
        for(Observer o: observers) {
            o.update();
        }
    }

    /**
     * Method to compile the data in the trips List to be saved to a file
     *
     * @return the String value of the whole trips List
     */
    public String export(){
        String tripData = "route_id,service_id,trip_id,trip_headsign,direction_id,block_id,shape_id\n";
        for(Trip trip : trips){
            tripData += trip.export();
        }
        return tripData;
    }

    /**
     * Update Method that returns an instance of the current Trips object
     *
     * @return this instance of Trips
     */
    @Override
    public Trips update(){
        return this;
    }
}