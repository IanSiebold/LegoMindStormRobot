/**
 * SE2030 -021
 * Fall 2017
 * GTFS Project: Driver
 * Copyright [2017] - [2022] Max Beihoff, Chris Betances, Eduardo Avilles
 * All Rights Reserved
 * 05 Oct 2017
 */
package GTFS_Project.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This class contains the main method of the GTFS program
 *
 * @author beihoffmc
 * @version 1.0
 * 05-Oct-2017 4:43:36 PM
 */
public class Driver extends Application {

	public void finalize() throws Throwable {
		super.finalize();
	}

	/**
	 * Main Method
     *
	 * @param args String[]
	 */
	public static void main(String[] args){
	launch(args);
	}

	/**
	 * Start Method
     *
	 * @param primaryStage Stage
 	 */
	public void start(Stage primaryStage)throws Exception{
		Parent root = FXMLLoader.load(getClass().getResource("gtfsGUI.fxml"));
		primaryStage.setTitle("Transit Application");
		primaryStage.setScene(new Scene(root, 910, 910));
		primaryStage.show();
	}
}