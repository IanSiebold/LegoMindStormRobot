/**
 * SE2030 -021
 * Fall 2017
 * GTFS Project: TripsObserver
 * Copyright [2017] - [2022] Max Beihoff, Chris Betances, Eduardo Avilles
 * All Rights Reserved
 * 05 Oct 2017
 */
package GTFS_Project.Observer;

import GTFS_Project.Subject.Trips;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * @author beihoffmc
 * @version 1.0
 * 05-Oct-2017 4:43:36 PM
 */
public class TripsObserver implements Observer {
	private TableView tripsView;
	private TableColumn tripsTripID,tripsRouteID;
	private Trips trips;

	/**
	 * Default Constructor
	 */
	public TripsObserver(){

	}

	/**
	 * Constructor that accepts a Trips to be passed in
	 * 
	 * @param trips Trips is passed in
	 */
	public TripsObserver(Trips trips)throws Exception{
    if(trips == null){
    	throw new NullPointerException("No Trips");
	}else
		this.trips = trips;
	}

	public Trips getTrips(){
		return trips;
	}

	public void setTableColumns(TableColumn tc1,TableColumn tc2, TableView tv){
		tripsTripID = tc1;
		tripsRouteID = tc2;
		tripsView = tv;
	}

	public void refresh(){
		tripsView.refresh();
	}

	/**
	 * @author Max Beihoff
	 * This method accesses the Trips Observer to populate the Trips Table
	 */

	public void updateTripsTable(){

		tripsTripID.setCellValueFactory(new PropertyValueFactory<>("tripID"));
		tripsRouteID.setCellValueFactory(new PropertyValueFactory<>("routeID"));
		tripsView.setItems(FXCollections.observableArrayList(getTrips().getTrips()));

	}
	/**
	 * Method that handles observer updates
	 */
	public void update(){
	trips = trips.update();
	}
}