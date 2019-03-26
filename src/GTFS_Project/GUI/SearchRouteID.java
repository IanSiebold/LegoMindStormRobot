/**
 * SE2030 -021
 * Fall 2017
 * GTFS Project: SearchRouteID
 * Copyright [2017] - [2022] Max Beihoff, Chris Betances, Eduardo Avilles
 * All Rights Reserved
 * 05 Oct 2017
 */
package GTFS_Project.GUI;

import GTFS_Project.Subject.*;
import javafx.application.Platform;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * GUI that displays Stops with RouteID
 *
 * @author: Max Beihoff
 * @version: 11/1/2017
 */
public class SearchRouteID {

    private static TextField textField = new TextField();
    private static TextArea textArea = new TextArea();
    private static Button searchButton = new Button();
    private static Text searchText = new Text();
    private HBox hbox = new HBox();
    private BorderPane root = new BorderPane();
    private Stage main = new Stage();
    private Routes routes;
    private Controller controller;
    private ArrayList<Stop> export;

    /**
     * Constructor for the SearchRouteID Class. A new stage is created for the input and data retrieved.
     * upon input the string is passed to the Routes.searchRouteID() method.
     */
    public SearchRouteID() {
        export = new ArrayList<>();
        searchButton.setText("Search for Stops ");
        searchText.setText(" Enter a Route ID Value:");
        hbox.getChildren().addAll(searchText, textField, searchButton);
        hbox.setSpacing(10);
        root.setCenter(textArea);
        root.setBottom(hbox);
        Scene scene = new Scene(root, 400, 400);
        main.setScene(scene);
        main.setTitle("Search for Stops by Route ID");
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
     * Routes Setter
     *
     * @param routes
     */
    public void setRoutes(Routes routes) {
        this.routes = routes;
    }

    /**
     * Checks if GUI is visible
     *
     * @return boolean
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
        main.show();
    }

    /**
     * Terminates GUI
     */
    public void close() {
        main.close();
    }

    public List<Stop> getExport(){
            List<Stop> stopsTemp = routes.searchRoute(textField.getText());
            String s = "";
            for (Stop stop : stopsTemp) {
            //    System.out.println(stop.getStopID());
                s += stop.export();
            }
            textArea.setText(s);
        return stopsTemp;
    }

    public Button getSearchButton(){
        return searchButton;
    }

    public static TextField getTextField() {
        return textField;
    }

    /**
     * Handles when the x in the corner is selected
     */
    public void stop(){
        main.close();
    }
}