/**
 * SE2030 -021
 * Fall 2017
 * GTFS Project: MapObserver
 * Copyright [2017] - [2022] Max Beihoff, Chris Betances, Eduardo Avilles
 * All Rights Reserved
 * 05 Oct 2017
 */
package GTFS_Project.Observer;

import GTFS_Project.Subject.*;
import javafx.scene.web.WebEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * @author beihoffmc
 * @version 1.0
 * 05-Oct-2017 4:43:36 PM
 */
public class MapObserver implements Observer {

	private Routes routes;
	private Stops stops;
	private Trips trips;
	private StopTimes stopTimes;
	private WebEngine webEngine;

	/**
	 * Default Constructor
	 */
	public MapObserver(){

	}

	/**
	 * Constructor that is initiated with the subjects
	 *
	 * @param trips Trips subject is passed
	 * @param stops Stops subject is passed in
	 * @param routes Routes subject is passed in
	 * @param stopTimes StopTimes subject is passed in
	 */
	public MapObserver(Trips trips, Stops stops, Routes routes, StopTimes stopTimes, WebEngine wE)throws Exception{
		if(trips ==  null){
			throw new NullPointerException("No Trips");
		}else{
			this.trips = trips;
		}

		if(stops == null){
			throw new NullPointerException("No Stops");
		}else{
			this.stops = stops;
		}

		if(routes == null){
			throw new NullPointerException("No Routes");
		}else{
			this.routes = routes;
		}

		if(stopTimes == null){
			throw new NullPointerException("No Stop Times");
		}else{
			this.stopTimes = stopTimes;
		}

		if(wE == null){
			throw new NullPointerException("No WebEngine to load");
		}else {
			webEngine = wE;
		}
	}


	public void displayAllStops(){

		for(Stop stop: stops.getStops()){

			webEngine.executeScript("customStopMarker("+stop.getName() +","+ Double.toString(stop.getLatitude()) +"," +Double.toString(stop.getLongitude())+")");

		}

	}

	public void displaySearchStops(List<Stop> stopsList){
		webEngine.executeScript("centerMap("+Double.toString(stopsList.get(0).getLatitude()) + "," + Double.toString(stopsList.get(0).getLongitude())+ ")");
		for(Stop stop: stopsList){
			//System.out.println(stop.getStopID());
			webEngine.executeScript("customStopMarker("+stop.getName() +","+ Double.toString(stop.getLatitude()) +"," +Double.toString(stop.getLongitude())+")");
		}
	}

	/**
	 * Routes Getter
	 *
	 * @return routes
	 */
	public Routes getRoutes(){
		return routes;
	}

	/**
	 * Stops Getter
	 *
	 * @return stops
	 */
	public Stops getStops(){
		return stops;
	}

	/**
	 * Trips Getter
	 *
	 * @return trips
	 */
	public Trips getTrips(){
		return trips;
	}

	/**
	 * StopTimes Getter
	 *
	 * @return stopTimes
	 */
	public StopTimes getStopTimes(){
		return stopTimes;
	}

	/**
	 * Method to handle updates
	 */
	public void update(){
		routes = routes.update();
		stops = stops.update();
		trips = trips.update();
		stopTimes = stopTimes.update();
	}
}