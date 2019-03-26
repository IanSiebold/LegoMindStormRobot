/**
 * SE2030 -021
 * Fall 2017
 * GTFS Project: Controller
 * Copyright [2017] - [2022] Max Beihoff, Chris Betances, Eduardo Avilles
 * All Rights Reserved
 * 05 Oct 2017
 */
package GTFS_Project.GUI;

import GTFS_Project.Observer.*;
import GTFS_Project.Subject.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.*;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

/**
 * Controller Class, which contains the logic for the GTFS program
 *
 * @author Max Beihoff
 * @version 1.0
 * 05-Oct-2017 4:43:36 PM
 */
public class Controller {

	@FXML
	TableView StopsView,TripsView,StopTimesView,RoutesView;
	@FXML
	TableColumn TripsTripID,TripsRouteID,RoutesRouteID,RoutesRouteColor, StopsStopID,
            StopsStopName, StopsLatitude,StopsLongitude,StopTimesStopID, StopTimesTripID,
            StopTimesArrivalTime,StopTimesDepartureTime,StopTimesStopSequence;
	@FXML
    WebView webView;
	@FXML
	Menu Search,UpdateAtt;
	@FXML
	MenuItem SaveFiles;

	public Routes routes;
	public Trips trips;
	public Stops stops;
	public StopTimes stopTimes;

	private WebEngine webEngine;
	private SearchRouteID searchRouteID = new SearchRouteID();
	private SearchStopID searchStopID = new SearchStopID();

	private RoutesObserver routesObserver;
	private MapObserver mapObserver;
	private StopsObserver stopsObserver;
	private TimeObserver timeObserver;
	private TripsObserver tripsObserver;

	private FileChooser fileChooser = new FileChooser();
    private Alert alert =  new Alert(Alert.AlertType.WARNING);
    private Alert warning = new Alert(Alert.AlertType.WARNING);
	private List<File> fileList;

    /**
     * Initialization Method
     */
    @FXML
	private void initialize(){
		webEngine = webView.getEngine();
		webEngine.load(new File("GoogleMap.html").toURI().toString());
//		webEngine.load("https://www.google.com/maps");
    }

    /**
     * GUI Constructor
     */
	public Controller() {
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text File", "*.txt"));

	}


	public void finalize() throws Throwable {

	}


	@FXML
	/**
     * Open Files Method
	 */
	public void handleOpenFiles() {
		try {

			alert.setTitle("Select Files");
			alert.setHeaderText("Multiselect");
			alert.setContentText("Please select all GTFS files at once. You may hold ctrl and left click on files wanted.");
			alert.showAndWait();

			fileChooser.setTitle("Open GTFS Files(Routes,Trips,Stops, and Stop Times)");
			fileList = fileChooser.showOpenMultipleDialog(new Stage());
			System.out.println(fileList.get(0).getName());
			for (File file : fileList) {
				if (file.getName().equals("routes.txt")) {
					routes = new Routes(file);
				} else if (file.getName().equals("stop_times.txt")) {
					stopTimes = new StopTimes(file);
				} else if (file.getName().equals("stops.txt")) {
					stops = new Stops(file);
				} else if (file.getName().equals("trips.txt")) {
					trips = new Trips(file);
				} else {
					throw new IllegalArgumentException("Tried to import incorrect file type!");
				}
			}

			stops.setRoutes(routes);
			stops.setStopTimes(stopTimes);
			stops.setTrips(trips);

			routes.setStopTimes(stopTimes);
			routes.setTrips(trips);
			routes.setStops(stops);

            stops.setController(this);
			routes.setController(this);
			trips.setController(this);
			stopTimes.setController(this);
			searchStopID.setController(this);
			searchRouteID.setController(this);

			searchRouteID.setRoutes(routes);
			searchStopID.setStops(stops);

			mapObserver = new MapObserver(trips,stops,routes,stopTimes,webEngine);
			tripsObserver = new TripsObserver(trips);
			routesObserver = new RoutesObserver(routes);
			stopsObserver = new StopsObserver(stops);
			timeObserver = new TimeObserver(stopTimes);

			Search.setDisable(false);
			UpdateAtt.setDisable(false);
			SaveFiles.setDisable(false);

			routesObserver.setTableColumns(RoutesRouteID,RoutesRouteColor,RoutesView);
			routesObserver.updateRoutesTable();

			tripsObserver.setTableColumns(TripsTripID,TripsRouteID,TripsView);
			tripsObserver.updateTripsTable();

			timeObserver.setTableColumns(StopTimesStopID, StopTimesTripID,StopTimesArrivalTime,StopTimesDepartureTime,StopTimesStopSequence, StopTimesView);
			timeObserver.updateStopTimesTable();

			stopsObserver.setTableColumns(StopsStopID,StopsStopName,StopsLatitude,StopsLongitude,StopsView);
			stopsObserver.updateStopsTable();

		} catch (Exception e) {

			displayError(e.getMessage());

		}
	}

	/**
	 * Search for Stops by RouteID
	 */
	@FXML
	public void handleSearchRouteID(){
		searchRouteID.display();
		warning.setTitle("Please wait");
        warning.setContentText("Please check to see that a list of stops of displayed before exiting. Then you can view the stop markers on the map.");
        warning.showAndWait();
		searchRouteID.getSearchButton().setOnAction(event -> {
			mapObserver.displaySearchStops(searchRouteID.getExport());
		});


	}

	/**
	 * Search for Routes by StopID
	 */
	@FXML
    public void handleSearchStopID() {
        try {
            if (!searchStopID.checkVisibility()) {
                searchStopID.display();

			} else if (searchStopID.checkVisibility()) {
                searchStopID.hide();
            }
        } catch (Exception e) {
            displayError(e.getMessage());
        }
    }

	@FXML
	/**
	 * Update RouteID Method
	 */
	public void handleRouteIDUpdate() {
        Update updateRouteID = new Update("RouteID");
        updateRouteID.getUpdateButton().setOnAction(e -> {
            try {
                for (Route route : routes.getRoutes()) {
                    if (route.getRouteID().equals(updateRouteID.getOldID())) {
                        route.setRouteID(updateRouteID.getNewID());
                    }
                }
                for (Trip trip : trips.getTrips()) {
                    if (trip.getRouteID().equals(updateRouteID.getOldID())) {
                        trip.setRouteID(updateRouteID.getNewID());
                    }
                }
                routes.notifyObservers();
                trips.notifyObservers();

                routesObserver.refresh();
                tripsObserver.refresh();

            } catch (NullPointerException exception) {
                displayError(exception.getMessage());
            }
        });
    }

	@FXML
	/**
	 * Update TripID Method
	 */
	public void handleTripIDUpdate(){
    	Update updateTripID = new Update("TripID");
    	updateTripID.getUpdateButton().setOnAction(e->{

			try {

				for (StopTime stopTime : stopTimes.getStopTimes()) {

					if (stopTime.getTripId().equals(updateTripID.getOldID())) {
						stopTime.setTripId(updateTripID.getNewID());
					}

				}

				for (Trip trip : trips.getTrips()) {

					if (trip.getTripID().equals(updateTripID.getOldID())) {
						trip.setTripID(updateTripID.getNewID());
					}

				}

				stopTimes.notifyObservers();
				trips.notifyObservers();
				timeObserver.refresh();
				tripsObserver.refresh();

			}catch (NullPointerException exception){
			    displayError(exception.getMessage());
			}
		});
	}

	@FXML
	/**
	 * Update StopID Method
	 */
	public void handleStopIDUpdate(){
		Update updateStopID = new Update("StopID");
		updateStopID.getUpdateButton().setOnAction(e-> {
			try {
				for (StopTime stopTime : stopTimes.getStopTimes()) {
					if (stopTime.getStopId().equals(updateStopID.getOldID())) {
						stopTime.setStopId(updateStopID.getNewID());
					}
				}
				for (Stop stop : stops.getStops()) {
					if (stop.getStopID().equals(updateStopID.getOldID())) {

						stop.setStopID(updateStopID.getNewID());

					}
				}

				stopTimes.notifyObservers();
				stops.notifyObservers();
				timeObserver.refresh();
				stopsObserver.refresh();

			} catch (NullPointerException exception) {
			    displayError(exception.getMessage());
			}
		});
	}

	@FXML
	/**
	 * Update Route Color Method
	 */
	public void handleRouteColorUpdate(){
		Update updateRouteColor = new Update("RouteColor");
		updateRouteColor.getUpdateButton().setOnAction(e -> {
			try {
				for (Route route : routes.getRoutes()) {
					if (route.getRouteID().equals(updateRouteColor.getOldID())) {
						route.setColor(updateRouteColor.getNewID());
					}
				}
				routes.notifyObservers();
				routesObserver.refresh();

			}catch (NullPointerException execpt){
			    displayError(execpt.getMessage());
			}
		});
	}

	@FXML
	/**
	 * Update StopName Method
 	 */
	public void handleStopNameUpdate(){
		Update updateStopName = new Update("StopName");
		updateStopName.getUpdateButton().setOnAction(event1 -> {
			try{
				for (Stop stop: stops.getStops()){
					if(stop.getStopID().equals(updateStopName.getOldID())){
						stop.setName(updateStopName.getNewID());
					} //Break after changing value?
				}
				stops.notifyObservers();
				stopsObserver.refresh();
			}catch (NullPointerException e){
			    displayError(e.getMessage());
			}
		});
	}

	@FXML
	/**
	 * Udpate Stop Latitude Method
	 */
	public void handleStopLat(){
		Update updateStopLat = new Update("StopLat");
		updateStopLat.getUpdateButton().setOnAction(event1 -> {
			try{
				for(Stop stop: stops.getStops()){
					if(stop.getStopID().equals(updateStopLat.getOldID())){
						stop.setLatitude(Double.parseDouble(updateStopLat.getNewID()));
					}
				}
				stops.notifyObservers();
				stopsObserver.refresh();
			}catch (NullPointerException e){
                displayError(e.getMessage());
			}catch (Exception e){
			    displayError(e.getMessage());
			}
		});
	}

	@FXML
	/**
	 * Update Stop Latitude Method
	 */
	public void handleStopLon(){
		Update updateStopLon = new Update("StopLon");
		updateStopLon.getUpdateButton().setOnAction(event1 -> {
			try{
				for(Stop stop: stops.getStops()){
					if(stop.getStopID().equals(updateStopLon.getOldID())){
						stop.setLongitude(Double.parseDouble(updateStopLon.getNewID()));
					}
				}

				stops.notifyObservers();
				stopsObserver.refresh();
			}catch (NullPointerException e){
                displayError(e.getMessage());
			}catch (Exception e){
                displayError(e.getMessage());
			}
		});
	}

	@FXML
	/**
	 * Update Arrival Time Method
	 */
	public void handleArrTimeUpdate() {
        Update arrTime = new Update("ArrTime");
        arrTime.getUpdateButton().setOnAction(event1 -> {
            try {
                for (StopTime stopTime : stopTimes.getStopTimes()) {
                    if (stopTime.getTripId().equals(arrTime.getOldID()) && stopTime.getStopId().equals(arrTime.getOldID2())) {
                        stopTime.setArrivalTime(arrTime.getNewID());
                    }
                }
                stopTimes.notifyObservers();
                stopsObserver.refresh();
            } catch (NullPointerException e) {
                displayError(e.getMessage());
            }
        });
    }

	@FXML
	/**
	 * Update Departure Time Method
	 */
	public void handleDepTimeUpdate(){
		Update depTime = new Update("DepTime");
		depTime.getUpdateButton().setOnAction(event1 -> {
			try {
				for (StopTime stopTime : stopTimes.getStopTimes()) {

					if (stopTime.getTripId().equals(depTime.getOldID()) && stopTime.getStopId().equals(depTime.getOldID2())) {

						stopTime.setDepartureTime(depTime.getNewID());

					}
				}
				stopTimes.notifyObservers();
				stopsObserver.refresh();
			}catch (NullPointerException e){
                displayError(e.getMessage());
			}
		});
	}

	@FXML
	/**
	 * Update Stop Sequence Method
	 */
	public void handleSeqUpdate(){
		Update stopSeq = new Update("StopSeq");
		stopSeq.getUpdateButton().setOnAction(event1 -> {
			try {
				for (StopTime stopTime : stopTimes.getStopTimes()) {
					if (stopTime.getTripId().equals(stopSeq.getOldID()) && stopTime.getStopId().equals(stopSeq.getOldID2())) {
						stopTime.setStopSequence(stopSeq.getNewID());
					}
				}
				stopTimes.notifyObservers();
				timeObserver.refresh();
			}catch (NullPointerException e){
                displayError(e.getMessage());
			}
		});
	}


	/**
	 * Save Files Method
	 */
	@FXML
	public void handleSaveFiles(){
		try{
			saveFile(fileSaveDialog("Trips"),trips);
			saveFile(fileSaveDialog("Routes"),routes);
			saveFile(fileSaveDialog("Stops"),stops);
			saveFile(fileSaveDialog("Stop_Times"),stopTimes);
		}catch (Exception e){
            displayError(e.getMessage());
		}
	}

	/**
	 * This method provides the user with an alternative method to exit the application
	 */
	@FXML
	public void handleClose(){
		searchRouteID.close();
		searchStopID.close();
		Platform.exit();
	}

    /**
     * This method handles when the x in the corner is clicked
     */
	@FXML
    public void stop(){
        searchRouteID.close();
        searchStopID.close();
        Platform.exit();
    }

    /**
     * This method logs an error to the error log through the printWriter
     *
     * @param error is the string value to be recorded
     */
	public void displayError(String error){
		alert.setTitle("Warning");
		alert.setHeaderText("Error");
		alert.setContentText(error);
		alert.showAndWait();
	}

	/**
	 * helper method that allows the user to select a file.
	 *
	 * @param selection String
	 * @return File
	 */
	private File fileOpenDialog(String selection){
		File file = null;
		switch (selection) {
			case "Routes":
				fileChooser.setTitle("Open Routes File");
				file = fileChooser.showOpenDialog(new Stage());
				break;
			case "Trips":
				fileChooser.setTitle("Open Trips File");
				file = fileChooser.showOpenDialog(new Stage());
				break;
			case "Stops":
				fileChooser.setTitle("Open Stops File");
				file = fileChooser.showOpenDialog(new Stage());
				break;
			case "Stop_Times":
				fileChooser.setTitle("Open Stop Times File");
				file = fileChooser.showOpenDialog(new Stage());
				break;
			default:
				System.out.println("You shouldn't be in this case statement, look at your code...");
		}
		return file ;
	}

	/**
	 * helper method that allows the user to select the file they want to save
	 *
	 * @param selection
	 * @return
	 */
	private File fileSaveDialog(String selection){
		File file = null;
		switch (selection) {
			case "Routes":
				fileChooser.setTitle("Save Routes File");
				file = fileChooser.showSaveDialog(new Stage());
				break;
			case "Trips":
				fileChooser.setTitle("Save Trips File");
				file = fileChooser.showSaveDialog(new Stage());
				break;
			case "Stops":
				fileChooser.setTitle("Save Stops File");
				file = fileChooser.showSaveDialog(new Stage());
				break;
			case "Stop_Times":
				fileChooser.setTitle("Save Stop Times File");
				file = fileChooser.showSaveDialog(new Stage());
				break;
			default:
				System.out.println("You shouldn't be in this case statement, look at your code...");
		}
		return file;
	}

	/**
	 * Helper method that saves the files
	 *
	 * @param file File
	 * @param s Subject
	 * @return true Boolean
	 * @throws Exception false
	 */
	private boolean saveFile(File file, Subject s)throws Exception{
		try{
			FileWriter fw = new FileWriter(file);
			fw.write(s.export());
			fw.close();
			return true;
		}catch (Exception e){
		    displayError(e.getMessage());
			throw e;
		}
	}

	/**
	 * @author Max Beihoff
	 * This method accesses the Map Observer to populate the Map View
	 */
	public void updateMap(){

    }
}