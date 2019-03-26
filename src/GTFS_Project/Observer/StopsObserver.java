/**
 * SE2030 -021
 * Fall 2017
 * GTFS Project: StopsObserver
 * Copyright [2017] - [2022] Max Beihoff, Chris Betances, Eduardo Avilles
 * All Rights Reserved
 * 05 Oct 2017
 */
package GTFS_Project.Observer;

import GTFS_Project.Subject.Stops;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * @author beihoffmc
 * @version 1.0
 * 05-Oct-2017 4:43:36 PM
 */
public class StopsObserver implements Observer {

	private TableView stopsView;
	private TableColumn stopsStopID, stopsStopName, stopsLatitude, stopsLongitude;
	private Stops stops;

	/**
	 * Default Constructor
	 */
	public StopsObserver(){}

	/**
	 * Constructor that accepts a Stops to be passed in
	 *
	 * @param stops Stops is passed in
	 */
	public StopsObserver(Stops stops)throws Exception {
		if (stops == null) {
			throw new NullPointerException("Stops is null");
		} else {
			this.stops = stops;

		}
	}

	/**
	 * Stops Getter
	 *
	 * @return stops location is returned
	 */
	public Stops getStops(){
		return stops;
	}

	public void setTableColumns(TableColumn tc1,TableColumn tc2,TableColumn tc3,TableColumn tc4,TableView tv ){
	stopsStopID = tc1;
	stopsStopName = tc2;
	stopsLatitude = tc3;
	stopsLongitude = tc4;
	stopsView = tv;
	}

	public void refresh(){
		stopsView.refresh();
	}


	/**
	 * @author Max Beihoff
	 * This method accesses the Stops Observer to populate the Stops Table
	 */
	public void updateStopsTable(){

		stopsStopID.setCellValueFactory(new PropertyValueFactory<>("stopID"));
		stopsStopName.setCellValueFactory(new PropertyValueFactory<>("name"));
		stopsLatitude.setCellValueFactory(new PropertyValueFactory<>("latitude"));
		stopsLongitude.setCellValueFactory(new PropertyValueFactory<>("longitude"));
		stopsView.setItems(FXCollections.observableArrayList(getStops().getStops()));

	}




	/**
	 * Method that handles updates
	 */
	public void update(){
		stops = stops.update();
	}
}