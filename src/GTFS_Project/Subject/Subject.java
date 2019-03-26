/**
 * SE2030 -021
 * Fall 2017
 * GTFS Project: Subject
 * Copyright [2017] - [2022] Max Beihoff, Chris Betances, Eduardo Avilles
 * All Rights Reserved
 * 05 Oct 2017
 */
package GTFS_Project.Subject;

import GTFS_Project.Observer.Observer;

import java.io.File;

/**
 * Subject Interface
 * @author beihoffmc
 * @version 1.0
 * 05-Oct-2017 4:43:36 PM
 */
public interface Subject {

	/**
	 * This method will handle the file import
	 *
	 * @param file
	 * @return boolean confirms successful or failed import
	 * @throws Exception
	 */
	boolean importFile(File file) throws Exception;

	/**
	 * Adds an Observer to the list of Observers
	 *
	 * @param o Observer
	 * @return boolean confirmas success/failure
	 */
	boolean addObserver(Observer o);

	/**
	 *Removes an Observer from the List of Observers
	 *
	 * @param o Observer
	 * @return boolean confirms success/failure
	 */
	boolean removeObserver(Observer o);

	/**
	 *Iterates through all the Observers and calls their update method
	 */
	void notifyObservers();

	/**
	 *Creates a string of all the data that is in the List
	 *
	 * @return string
	 */
	String export();

	/**
	 * Provides an instance of the class to the Observer that requested it.
	 *
	 * @return object
	 */
	Object update();
}