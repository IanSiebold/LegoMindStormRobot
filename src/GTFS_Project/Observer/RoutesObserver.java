/**
 * SE2030 -021
 * Fall 2017
 * GTFS Project: RoutesObserver
 * Copyright [2017] - [2022] Max Beihoff, Chris Betances, Eduardo Avilles
 * All Rights Reserved
 * 05 Oct 2017
 */
package GTFS_Project.Observer;

import GTFS_Project.Subject.Routes;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * @author beihoffmc
 * @version 1.0
 * 05-Oct-2017 4:43:36 PM
 */
public class RoutesObserver implements Observer {

	private Routes routes;
	private TableView routesView;
	private TableColumn routesRouteID,routesRouteColor;
	/**
	 * Default Constructor
	 */
	public RoutesObserver(){

	}

	/**
	 * Constructor that accepts a Routes to be passed in
	 *
	 * @param routes Routes is passed in
	 */
	public RoutesObserver(Routes routes)throws Exception{
		if(routes == null){
			throw new NullPointerException("There is no routes file");
		}else{
			this.routes = routes;
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
	 * Method that assigns control of the table columns
	 * @param tc1 Route ID
	 * @param tc2 Route Color
	 */
	public void setTableColumns(TableColumn tc1, TableColumn tc2,TableView tv){
		routesRouteID = tc1;
		routesRouteColor = tc2;
		routesView = tv;
	}

	/**
	 * @author Max Beihoff
	 * This method accesses the Routes Observer to populate the Routes Table
	 */

	public void updateRoutesTable(){
		routesRouteID.setCellValueFactory(new PropertyValueFactory<>("routeID"));
		routesRouteColor.setCellValueFactory(new PropertyValueFactory<>("color"));
		routesView.setItems(FXCollections.observableArrayList(getRoutes().getRoutes()));
	}
	public void refresh(){
		routesView.refresh();
	}

		/**
         * Update Method
         */
	public void update(){
		routes = routes.update();
	}
}