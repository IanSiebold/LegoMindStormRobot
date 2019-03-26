/**
 * SE2030 -021
 * Fall 2017
 * GTFS Project: SearchStopID
 * Copyright [2017] - [2022] Max Beihoff, Chris Betances, Eduardo Avilles
 * All Rights Reserved
 * 05 Oct 2017
 */
package GTFS_Project.GUI;

import GTFS_Project.Subject.Route;
import GTFS_Project.Subject.Stops;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.List;

/**
 * Search StopID GUI
 *
 * @author Chris Betances, Max Beihoff
 * @version 10/26/2017
 */
public class SearchStopID {

    private static TextField textField = new TextField();
    private static TextArea textArea = new TextArea();
    private static Button searchButton = new Button();
    private static Text searchText = new Text();
    private HBox hbox = new HBox();
    private BorderPane root = new BorderPane();
    private Stage main = new Stage();
    private Stops stops;
    private Controller controller;

    /**
     * Constructor for the SearchStopID Class. A new stage is created for the input and data retrieved.
     * upon input the string is passed to the Stops.searchStopID() method.
     */
    public SearchStopID() {
        searchButton.setText("Search for Routes ");
        searchText.setText(" Enter a Stop ID Value:");
        hbox.getChildren().addAll(searchText, textField, searchButton);
        hbox.setSpacing(10);
        root.setCenter(textArea);
        root.setBottom(hbox);
        Scene scene = new Scene(root, 400, 400);
        main.setScene(scene);
        main.setTitle("Search for Route by Stop ID");
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
			    String tgtID = textField.getText();
			    System.out.println(tgtID);
			    List<Route> routesTemp = stops.searchStop(tgtID);
			    String s = "";
			    for (Route route : routesTemp) {
			        s += route.export();
			    }
			    textArea.setText(s);
			}
		});
    }

    /**
     * Controller Setter
     *
     * @param controller
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Stops Setter
     *
     * @param stops
     */
    public void setStops(Stops stops) {
        this.stops = stops;
    }

    /**
     * Checks if GUI is visible
     *
     * @return
     */
    public boolean checkVisibility() {
        return main.isShowing();
    }

    /**
     * Hides GUI
     */
    public void hide() {
        main.hide();
    }

    /**
     * Displays GUI
     */
    public void display() {
        main.showAndWait();
    }

    /**
     * Terminates GUI
     */
    public void close() {
        main.close();
    }

    /**
     * Handles when the x in the corner is selected
     */
    public void stop(){
        main.close();
    }
}
