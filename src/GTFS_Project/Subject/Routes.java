/**
 * SE2030 -021
 * Fall 2017
 * GTFS Project: Routes
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
 * @author beihoffmc
 * @version 1.0
 * 05-Oct-2017 4:43:36 PM
 */
public class Routes implements Subject {

	private List<Observer> observers;
	private List<Route> routes;
	private Scanner inFile;
    private Trips trips;
    private Stops stops;
    private StopTimes stopTimes;
    private Controller controller;

	/**
	 * Default constructor
	 */
	public Routes()throws Exception{
		observers = new ArrayList<>();
		routes = new ArrayList<>();
	}

	/**
	 * Constructor for Routes with a routesFile passed in
	 * The file is them processed and each Route is added to the Collection routes
     *
	 * @param routesFile File
	 */
	public Routes(File routesFile)throws Exception {
		observers = new ArrayList<>();
		routes = new ArrayList<>();
		importFileNew(routesFile);
	}

	/**
	 * Method that returns a list of Routes
     *
	 * @return routes
	 */
	public List<Route> getRoutes(){
		return routes;
	}

    /**
     * Route Getter at specific index
     *
     * @param tgt Index
     * @return route
     */
	public Route getRoute(int tgt){
	    return routes.get(tgt);
    }

    /**
     * Getter that returns a Route
     *
     * @param tgtID index of the route in the routes List
     * @return Route at the given index
     */
	public Route getRoute(String tgtID){
	    if(verifyRoute(tgtID)) {
            for (Route route : routes) {
                if (route.getRouteID().equals(tgtID)) {
                    return route;
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
    public List<Route> getRoute(List<String> tgtIDs){
        List<Route> foundRoutes = new ArrayList<>();
        for(String tgtRoute : tgtIDs){
            for(Route route : routes){
                if(route.getRouteID().equals(tgtRoute)){
                    foundRoutes.add(route);
                }
            }
        }
        return foundRoutes;
    }

    /**
     * Size Getter
     *
     * @return int value of the routes List size
     */
    public int getRouteSize(){
    	return routes.size();
	}

    public void setTrips(Trips trips) {
        this.trips = trips;
    }

    public void setStops(Stops stops) {
        this.stops = stops;
    }

    public void setStopTimes(StopTimes stopTimes) {
        this.stopTimes = stopTimes;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public boolean importFileNew(File routesFile)
    {
    	//route_id,agency_id,route_short_name,route_long_name,route_desc,route_type,route_url,route_color,route_text_color
		try {
			BufferedReader br = new BufferedReader(new FileReader(routesFile));
			String readLine = br.readLine();
			readLine = br.readLine();
			while(readLine != null)
			{
				String[] line = readLine.split(",");
				addRoute(new Route(line[0], line[7]));
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
     * Method to import a file to populate the routes List
     *
     * @param routesFile is the File that is to be used
     */
	public boolean importFile(File routesFile)throws Exception {
        try {
            inFile = new Scanner(routesFile);
            inFile.nextLine();
            String inLine;
            String[] splitLine;
            String routeID;
            String color;
            while (inFile.hasNextLine()) {
                inLine = inFile.nextLine();
                splitLine = inLine.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                routeID = splitLine[0];
                color = splitLine[7];
                addRoute(new Route(routeID, color));
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
	 * Method to add a new Route to the routes Collection
	 *
	 * @param route Route to be added
	 */
	public void addRoute(Route route){
		routes.add(route);
		notifyObservers();
	}

	/**
	 * Method to remove a Route from the routes collection
     *
	 * @param route to be removed
	 */
	public void removeRoute(Route route){
		routes.remove(route);
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
     * This Method is used to search for a route to find all the stops that are on it.
     * Each List is traversed to find the IDs that match up linking each together.
     *
     * @param routeID is the String of the requested route
     * @return List of Stops that match the requested route
     */
    public List<Stop> searchRoute(String routeID){
        List<String> tgtRoute = new ArrayList<>();
        List<String> foundTripIDs;
        List<String> foundStopIDs;
        List<Stop> foundStops;
        tgtRoute.add(routeID.toUpperCase());
        if(verifyRoute(routeID)){
            foundTripIDs = trips.getTripByRoute(tgtRoute);
        }else{
        	controller.displayError("The Route ID does not exist in the current selection");
			return null;
        }
        if(!foundTripIDs.isEmpty()){
            foundStopIDs = stopTimes.getStopTimeByTrip(foundTripIDs);
        }else{
        	controller.displayError("The Route ID couldn't be associated with the Trip IDs");
			return null;
        }
        if(!foundStopIDs.isEmpty()){
            foundStops = stops.getStop(foundStopIDs);
        }else{
			controller.displayError("The Stops could not be Associated to a Trip");
			return null;
        }
        if(foundStops.size() > 0) {
            return foundStops;
        }else{
			controller.displayError("The route ID you searched was not found");
            return null;
        }
	}

    /**
     * This method verifies that the Route to be searched is valid
     *
     * @param tgtID is the routeID being passed in
     * @return true if the routeID is valid
     */
	public boolean verifyRoute(String tgtID) {
		for (Route route : routes) {
			if (route.getRouteID().equals(tgtID)) {
                return true;
			}
		}
        return false;
	}

    /**
     * Method to compile the data in the routes List to be saved to a file
     *
     * @return the String value of the whole routes List
     */
	public String export(){
		String routeData = "route_id,agency_id,route_short_name,route_long_name,route_desc,route_type,route_url," +
				"route_color,route_text_color\n";
		for(Route route : routes){
			routeData += route.export();
		}
		return routeData;
	}

    /**
     * Method that provides a current version of Routes object
     *
     * @return this instance of Routes
     */
	@Override
	public Routes update() {
		return this;
	}
}