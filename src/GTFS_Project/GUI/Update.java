/**
 * SE2030 -021
 * Fall 2017
 * GTFS Project: Update
 * Copyright [2017] - [2022] Max Beihoff, Chris Betances, Eduardo Avilles
 * All Rights Reserved
 * 05 Oct 2017
 */
package GTFS_Project.GUI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * GUI for handling updates
 * @author Max Beihoff
 * @version 11/1/2017
 */
public class Update{

    private Controller controller;

    private Text textfieldLabel1 = new Text();
    private Text textfieldLabel2 = new Text();
    private Text textfieldLabel3 = new Text();

    private TextField oldIDField = new TextField();
    private TextField newIDField = new TextField();
    private TextField secondID = new TextField();
    private Button update = new Button();

    private Stage main = new Stage();
    private Scene scene;
    private Pane root = new Pane();
    private VBox vBox = new VBox();
    private HBox hBox1 = new HBox();
    private HBox hBox2 = new HBox();
    private HBox hBox3 = new HBox();
    private HBox hBox4 = new HBox();

    /**
     * Update GUI Constructor.  The selection that is passed in is used to navigate
     * the switch statement
     *
     * @param selection is what is used in the switch statement
     */
    Update(String selection){
        switch (selection) {
            case "RouteID":
                setUp("RouteID");
                break;
            case "RouteColor":
                setUp("RouteColor");
                break;
            case "StopID":
                setUp("StopID");
                break;
            case "StopName":
                setUp("StopName");
                break;
            case "StopLat":
                setUp("StopLat");
                break;
            case "StopLon":
                setUp("StopLon");
                break;
            case "ArrTime":
                setUp("ArrTime");
                break;
            case "DepTime":
                setUp("DepTime");
                break;
            case "StopSeq":
                setUp("StopSeq");
                break;
            case "TripID":
                setUp("TripID");
                break;
            default:
                controller.displayError("The following String is not a valid selection:" + selection);
                break;
        }
    }

    /**
     * Button Getter
     *
     * @return update Button
     */
    public Button getUpdateButton(){
        return update;
    }

    /**
     * String Getter for 1st TextField
     *
     * @return string value of the oldID
     */
    public String getOldID() {
        return oldIDField.getText();
    }

    /**
     * String Getter for 2nd TextField
     *
     * @return string value of the new ID
     */
    public String getNewID(){
        return newIDField.getText();
    }

    /**
     * String Getter for 3rd TextField
     *
     * @return string value of the second text box
     */
    public String getOldID2(){
        return secondID.getText();
    }

    /**
     * Controller Setter
     *
     * @param controller location of the Controller is passed in
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Method that Sets Up the layout for the GUI
     *
     * @param s udpate selection
     */
    public void setUp(String s) {
        switch (s) {
            case "RouteID":
                textfieldLabel1.setText("Old Route ID");
                textfieldLabel2.setText("New Route ID");
                update.setText("Update");

                hBox1.getChildren().addAll(textfieldLabel1, oldIDField);
                hBox1.setPadding(new Insets(10, 10, 10, 10));
                hBox1.setSpacing(15);

                hBox2.getChildren().addAll(textfieldLabel2, newIDField);
                hBox2.setPadding(new Insets(10, 10, 10, 10));
                hBox2.setSpacing(10);

                hBox3.getChildren().add(update);
                hBox3.setPadding(new Insets(10, 10, 10, 10));
                hBox3.setSpacing(10);
                hBox3.setAlignment(Pos.CENTER_RIGHT);

                vBox.getChildren().addAll(hBox1, hBox2, hBox3);

                root.getChildren().add(vBox);
                scene = new Scene(root, 250, 130);
                main = new Stage();
                main.setScene(scene);
                main.setTitle("Update ID");
                main.show();
                break;

            case "RouteColor":
                textfieldLabel1.setText("Route ID");
                textfieldLabel2.setText("Route Color");
                update.setText("Update");

                hBox1.getChildren().addAll(textfieldLabel1, oldIDField);
                hBox1.setPadding(new Insets(10, 10, 10, 10));
                hBox1.setSpacing(27);

                hBox2.getChildren().addAll(textfieldLabel2, newIDField);
                hBox2.setPadding(new Insets(10, 10, 10, 10));
                hBox2.setSpacing(10);

                hBox3.getChildren().add(update);
                hBox3.setPadding(new Insets(10, 10, 10, 10));
                hBox3.setSpacing(10);
                hBox3.setAlignment(Pos.CENTER_RIGHT);

                vBox.getChildren().addAll(hBox1, hBox2, hBox3);

                root.getChildren().add(vBox);
                scene = new Scene(root, 250, 130);
                main = new Stage();
                main.setScene(scene);
                main.setTitle("Update Color");
                main.show();
                break;

            case "StopID":
                textfieldLabel1.setText("Old Stop ID");
                textfieldLabel2.setText("New Stop ID");
                update.setText("Update");

                hBox1.getChildren().addAll(textfieldLabel1, oldIDField);
                hBox1.setPadding(new Insets(10, 10, 10, 10));
                hBox1.setSpacing(15);

                hBox2.getChildren().addAll(textfieldLabel2, newIDField);
                hBox2.setPadding(new Insets(10, 10, 10, 10));
                hBox2.setSpacing(10);

                hBox3.getChildren().add(update);
                hBox3.setPadding(new Insets(10, 10, 10, 10));
                hBox3.setSpacing(10);
                hBox3.setAlignment(Pos.CENTER_RIGHT);

                vBox.getChildren().addAll(hBox1, hBox2, hBox3);

                root.getChildren().add(vBox);
                scene = new Scene(root, 245, 130);
                main = new Stage();
                main.setScene(scene);
                main.setTitle("Update ID");
                main.show();
                break;

            case "StopName":
                textfieldLabel1.setText("Stop ID");
                textfieldLabel2.setText("Stop Name");
                update.setText("Update");

                hBox1.getChildren().addAll(textfieldLabel1, oldIDField);
                hBox1.setPadding(new Insets(10, 10, 10, 10));
                hBox1.setSpacing(30);

                hBox2.getChildren().addAll(textfieldLabel2, newIDField);
                hBox2.setPadding(new Insets(10, 10, 10, 10));
                hBox2.setSpacing(10);

                hBox3.getChildren().add(update);
                hBox3.setPadding(new Insets(10, 10, 10, 10));
                hBox3.setSpacing(10);
                hBox3.setAlignment(Pos.CENTER_RIGHT);

                vBox.getChildren().addAll(hBox1, hBox2, hBox3);

                root.getChildren().add(vBox);
                scene = new Scene(root, 240, 130);
                main = new Stage();
                main.setScene(scene);
                main.setTitle("Update Name");
                main.show();
                break;

            case "StopLat":
                textfieldLabel1.setText("Stop ID");
                textfieldLabel2.setText("Latitude");
                update.setText("Update");

                hBox1.getChildren().addAll(textfieldLabel1, oldIDField);
                hBox1.setPadding(new Insets(10, 10, 10, 10));
                hBox1.setSpacing(15);

                hBox2.getChildren().addAll(textfieldLabel2, newIDField);
                hBox2.setPadding(new Insets(10, 10, 10, 10));
                hBox2.setSpacing(10);

                hBox3.getChildren().add(update);
                hBox3.setPadding(new Insets(10, 10, 10, 10));
                hBox3.setSpacing(10);
                hBox3.setAlignment(Pos.CENTER_RIGHT);

                vBox.getChildren().addAll(hBox1, hBox2, hBox3);

                root.getChildren().add(vBox);
                scene = new Scene(root, 225, 130);
                main = new Stage();
                main.setScene(scene);
                main.setTitle("Update Latitude");
                main.show();
                break;

            case "StopLon":
                textfieldLabel1.setText("Stop ID");
                textfieldLabel2.setText("Longitude");
                update.setText("Update");

                hBox1.getChildren().addAll(textfieldLabel1, oldIDField);
                hBox1.setPadding(new Insets(10, 10, 10, 10));
                hBox1.setSpacing(25);

                hBox2.getChildren().addAll(textfieldLabel2, newIDField);
                hBox2.setPadding(new Insets(10, 10, 10, 10));
                hBox2.setSpacing(10);

                hBox3.getChildren().add(update);
                hBox3.setPadding(new Insets(10, 10, 10, 10));
                hBox3.setSpacing(10);
                hBox3.setAlignment(Pos.CENTER_RIGHT);

                vBox.getChildren().addAll(hBox1, hBox2, hBox3);

                root.getChildren().add(vBox);
                scene = new Scene(root, 230, 130);
                main = new Stage();
                main.setScene(scene);
                main.setTitle("Update Longitude");
                main.show();
                break;

            case "ArrTime":
                textfieldLabel1.setText("Trip ID");
                textfieldLabel2.setText("Stop ID");
                textfieldLabel3.setText("Arrive Time");
                update.setText("Update");

                hBox1.getChildren().addAll(textfieldLabel1, oldIDField);
                hBox1.setPadding(new Insets(10, 10, 10, 10));
                hBox1.setSpacing(35);

                hBox2.getChildren().addAll(textfieldLabel2, secondID);
                hBox2.setPadding(new Insets(10, 10, 10, 10));
                hBox2.setSpacing(30);

                hBox3.getChildren().addAll(textfieldLabel3, newIDField);
                hBox3.setPadding(new Insets(10, 10, 10, 10));
                hBox3.setSpacing(10);

                hBox4.getChildren().add(update);
                hBox4.setPadding(new Insets(10, 10, 10, 10));
                hBox4.setSpacing(10);
                hBox4.setAlignment(Pos.CENTER_RIGHT);

                vBox.getChildren().addAll(hBox1, hBox2, hBox3, hBox4);

                root.getChildren().add(vBox);
                scene = new Scene(root, 240, 180);
                main = new Stage();
                main.setScene(scene);
                main.setTitle("Update Arrive Time");
                main.show();
                break;

            case "DepTime":
                textfieldLabel1.setText("Trip ID");
                textfieldLabel2.setText("Stop ID");
                textfieldLabel3.setText("Departure");
                update.setText("Update");

                hBox1.getChildren().addAll(textfieldLabel1, oldIDField);
                hBox1.setPadding(new Insets(10, 10, 10, 10));
                hBox1.setSpacing(35);

                hBox2.getChildren().addAll(textfieldLabel2, secondID);
                hBox2.setPadding(new Insets(10, 10, 10, 10));
                hBox2.setSpacing(30);

                hBox3.getChildren().addAll(textfieldLabel3, newIDField);
                hBox3.setPadding(new Insets(10, 10, 10, 10));
                hBox3.setSpacing(20);

                hBox4.getChildren().add(update);
                hBox4.setPadding(new Insets(10, 10, 10, 10));
                hBox4.setSpacing(10);
                hBox4.setAlignment(Pos.CENTER_RIGHT);

                vBox.getChildren().addAll(hBox1, hBox2, hBox3, hBox4);

                root.getChildren().add(vBox);
                scene = new Scene(root, 240, 180);
                main = new Stage();
                main.setScene(scene);
                main.setTitle("Update Depart Time");
                main.show();
                break;

            case "StopSeq":
                textfieldLabel1.setText("Trip ID");
                textfieldLabel2.setText("Stop ID");
                textfieldLabel3.setText("Sequence");
                update.setText("Update");

                hBox1.getChildren().addAll(textfieldLabel1, oldIDField);
                hBox1.setPadding(new Insets(10, 10, 10, 10));
                hBox1.setSpacing(35);

                hBox2.getChildren().addAll(textfieldLabel2, secondID);
                hBox2.setPadding(new Insets(10, 10, 10, 10));
                hBox2.setSpacing(30);

                hBox3.getChildren().addAll(textfieldLabel3, newIDField);
                hBox3.setPadding(new Insets(10, 10, 10, 10));
                hBox3.setSpacing(20);

                hBox4.getChildren().add(update);
                hBox4.setPadding(new Insets(10, 10, 10, 10));
                hBox4.setSpacing(10);
                hBox4.setAlignment(Pos.CENTER_RIGHT);

                vBox.getChildren().addAll(hBox1, hBox2, hBox3, hBox4);

                root.getChildren().add(vBox);
                scene = new Scene(root, 240, 180);
                main = new Stage();
                main.setScene(scene);
                main.setTitle("Update Sequence");
                main.show();
                break;

            case "TripID":
                textfieldLabel1.setText("Old Trip ID");
                textfieldLabel2.setText("New Trip ID");
                update.setText("Update");

                hBox1.getChildren().addAll(textfieldLabel1, oldIDField);
                hBox1.setPadding(new Insets(10, 10, 10, 10));
                hBox1.setSpacing(15);

                hBox2.getChildren().addAll(textfieldLabel2, newIDField);
                hBox2.setPadding(new Insets(10, 10, 10, 10));
                hBox2.setSpacing(10);

                hBox3.getChildren().add(update);
                hBox3.setPadding(new Insets(10, 10, 10, 10));
                hBox3.setSpacing(10);
                hBox3.setAlignment(Pos.CENTER_RIGHT);

                vBox.getChildren().addAll(hBox1, hBox2, hBox3);

                root.getChildren().add(vBox);
                scene = new Scene(root, 240, 130);
                main = new Stage();
                main.setScene(scene);
                main.setTitle("Update ID");
                main.show();
                break;

            default:
                controller.displayError(s + " is not a valid selection...");
        }
    }
}



