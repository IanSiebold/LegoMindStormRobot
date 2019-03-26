/**
 * SE2030 -021
 * Fall 2017
 * GTFS Project: TimeObserver
 * Copyright [2017] - [2022] Max Beihoff, Chris Betances, Eduardo Avilles
 * All Rights Reserved
 * 05 Oct 2017
 */
package GTFS_Project.Observer;

import GTFS_Project.Subject.StopTimes;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * @author beihoffmc
 * @version 1.0
 * 05-Oct-2017 4:43:36 PM
 */
public class TimeObserver implements Observer {
	private TableView timeView;
	private TableColumn stopTimesStopID, stopTimesTripID, stopTimesArrivalTime,stopTimesDepartureTime,stopTimesStopSequence;
	private StopTimes stopTimes;

	/**
	 * Default Constructor
	 */
	public TimeObserver(){

	}

	/**
	 * Constructor that accepts a StopTimes to be passed in
	 *
	 * @param stopTimes StopTimes
	 */
	public TimeObserver(StopTimes stopTimes) throws Exception {
		if (stopTimes == null) {
			throw new NullPointerException("No StopTimes");
		} else {
			this.stopTimes = stopTimes;
		}
	}

	/**
	 * Stop Times Getter
	 *
	 * @return stopTimes location is passed back
	 */
	public StopTimes getStopTimes(){
		return stopTimes;
	}

	public void setTableColumns(TableColumn tc1, TableColumn tc2, TableColumn tc3, TableColumn tc4, TableColumn tc5, TableView tv){
		stopTimesStopID = tc1;
		stopTimesTripID = tc2;
		stopTimesArrivalTime = tc3;
		stopTimesDepartureTime = tc4;
		stopTimesStopSequence = tc5;
		timeView= tv;
	}

	/**
	 * @author Max Beihoff
	 * This method accesses the StopTimes Observer to populate the StopTimes Table
	 */
	public void updateStopTimesTable(){
		stopTimesTripID.setCellValueFactory(new PropertyValueFactory<>("tripId"));
		stopTimesArrivalTime.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
		stopTimesDepartureTime.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
		stopTimesStopID.setCellValueFactory(new PropertyValueFactory<>("stopId"));
		stopTimesStopSequence.setCellValueFactory(new PropertyValueFactory<>("stopSequence"));
		timeView.setItems(FXCollections.observableArrayList(getStopTimes().getStopTimes()));

	}

	public void refresh(){
		timeView.refresh();
	}
	/**
	 * Update Method
	 */
	public void update(){
		stopTimes = stopTimes.update();
	}
}