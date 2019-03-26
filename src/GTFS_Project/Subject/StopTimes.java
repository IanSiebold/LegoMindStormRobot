/**
 * SE2030 -021
 * Fall 2017
 * GTFS Project: StopTimes
 * Copyright [2017] - [2022] Max Beihoff, Chris Betances, Eduardo Avilles
 * All Rights Reserved
 * 05 Oct 2017
 */
package GTFS_Project.Subject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import GTFS_Project.GUI.Controller;
import GTFS_Project.Observer.Observer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * This class contains the list of Stop Time objects
 * @author Max Beihoff
 * @version 11/1/2017
 */
public class StopTimes implements Subject {

   private List<StopTime> stopTimes;
   private List<Observer> observers;
   private Controller controller;

    /**
     * Default Constructor
     */
    public StopTimes(){
        stopTimes = new ArrayList<>();
        observers = new ArrayList<>();
    }
    
    public void editTime(int id, StopTime time)
    {
    	stopTimes.set(id, time);
    }
    
    public boolean verifyStopTimeTime(String time)
    {
    	String[] split = time.split(":");
    	try
    	{
    		if(split.length != 3) { return false; }
    		int t1 = Integer.valueOf(split[0]);
    		int t2 = Integer.valueOf(split[1]);
    		int t3 = Integer.valueOf(split[2]);
    		if(t1 < 0 || t1 >= 24) { return false; }
    		if(t2 < 0 || t2 >= 60) { return false; }
    		if(t3 < 0 || t3 >= 60) { return false; }
    	}
    	catch(NumberFormatException e){	return false; }
    	return true;
    }
    
    public ArrayList<String> getTripIDsByStopID(String id)
    {
    	ArrayList<String> returnable = new ArrayList<>();
    	for(int x = 0; x < stopTimes.size(); x++)
    	{
    		if(stopTimes.get(x).getStopId().equals(id))
    		{
    			returnable.add(stopTimes.get(x).getTripId());
    		}
    	}
    	return returnable;
    }
    
    public ArrayList<StopTime> getStopTimesByStopID(String id)
    {
    	ArrayList<StopTime> returnable = new ArrayList<>();
    	for(int x = 0; x < stopTimes.size(); x++)
    	{
    		if(stopTimes.get(x).getStopId() == id)
    		{
    			returnable.add(stopTimes.get(x));
    		}
    	}
    	return returnable;
    }
    
    public int getStopIDIndex(String id)
    {
    	for(int x = 0; x < stopTimes.size(); x++)
    	{
    		if(stopTimes.get(x).getStopId() == id)
    		{
    			return x;
    		}
    	}
    	return -1;
    }
    
    public ArrayList<StopTime> getStopTimesByTripID(String id)
    {
    	ArrayList<StopTime> returnable = new ArrayList<>();
    	for(int x = 0; x < stopTimes.size(); x++)
    	{
    		if(stopTimes.get(x).getTripId() == id)
    		{
    			returnable.add(stopTimes.get(x));
    		}
    	}
    	return returnable;
    }
    
    /**
     * This method returns the tripID of the trip with the closest departure time from
     * a list of trip ids. This method is difficult to test, because the current time
     * changes, so junit testing is done with the method getnextTripByTripIDsFromTime,
     * in which a time can be passed in as a parameter to be used as the current time.
     * @param tripIDs the list of trip ids
     * @return the trip id of the trip with the next departure time
     */
    public String getNextTripByTripIDs(ArrayList<String> tripIDs) {
    	String nextTripTime = null;
    	String nextTripID = null;
    	ArrayList<StopTime> matchingStopTimes = new ArrayList<StopTime>();
    	SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    	dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("America/Chicago")); 	
    	Date currentDate = new Date();
    	currentDate.setHours(currentDate.getHours() - 1);
    	dateFormat.format(currentDate);
    	for (StopTime stopTime : stopTimes) {
    		if (tripIDs.contains(stopTime.getTripId())) {
    			matchingStopTimes.add(stopTime);
    		}
    	}
    	
    	if (matchingStopTimes.size() == 0) {
    		return "No next trip";
    	}
    	
    	try {
    	for (StopTime stopTime : matchingStopTimes) {
    		if (dateFormat.parse(stopTime.getDepartureTime()).after(dateFormat.parse(dateFormat.format(currentDate)))) {
    			if (nextTripTime == null) {
    				nextTripTime = stopTime.getDepartureTime();
    				nextTripID = stopTime.getTripId();
    			} else if (dateFormat.parse(stopTime.getDepartureTime()).before(dateFormat.parse(nextTripTime))) {
    				nextTripTime = stopTime.getDepartureTime();
    				nextTripID = stopTime.getTripId();
    			}
    		}
    	}
    	} catch (ParseException e) {
    		e.printStackTrace();
    	}
    	if (nextTripID == null) {
    		return "Next trip in the morning.";
    	}
    	//return nextTripTime;
    	return nextTripID;
    }
    
    /**
     * This method returns the tripID of the trip with the closest departure time from
     * a list of trip ids.
     * @param tripIDs the list of trip ids
     * @return the trip id of the trip with the next departure time
     */
    public String getNextTripByTripIDsFromTime(ArrayList<String> tripIDs, String time) {
    	try {
    		String nextTripTime = null;
    		String nextTripID = null;
    		ArrayList<StopTime> matchingStopTimes = new ArrayList<StopTime>();
    		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    		dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("America/Chicago"));
    		try {
    			Date currentDate = dateFormat.parse(time);
    			currentDate.setHours(currentDate.getHours() - 1);
    			dateFormat.format(currentDate);
    			for (StopTime stopTime : stopTimes) {
    				if (tripIDs.contains(stopTime.getTripId())) {
    					matchingStopTimes.add(stopTime);
    				}
    			}
    			
    			if (matchingStopTimes.size() == 0) {
    				return "No next trip";
    			}
    		
    			for (StopTime stopTime : matchingStopTimes) {
    				if (dateFormat.parse(stopTime.getDepartureTime()).after(dateFormat.parse(dateFormat.format(currentDate)))) {
    					if (nextTripTime == null) {
    						nextTripTime = stopTime.getDepartureTime();
    						nextTripID = stopTime.getTripId();
    					} else if (dateFormat.parse(stopTime.getDepartureTime()).before(dateFormat.parse(nextTripTime))) {
    						nextTripTime = stopTime.getDepartureTime();
    						nextTripID = stopTime.getTripId();
    					}
    				}
    			}
    		} catch (ParseException e) {
    			e.printStackTrace();
    		}
    		if (nextTripID == null) {
    			return "Next trip in the morning.";
    		}
    		//return nextTripTime;
    		return nextTripID;
    	} catch (NullPointerException e) {
    		return "No next trip";
    	}
    }

    public String getNextTripByStopID(String StopID) {
    	List<StopTime> foundStopTimes = new ArrayList<StopTime>();
    	List<StopTime> currentHourTimes = new ArrayList<StopTime>();
    	List<StopTime> nextHourTimes = new ArrayList<StopTime>();
    	for (StopTime stopTime : stopTimes) {
    		if (stopTime.getStopId().equals(StopID)) {
    			foundStopTimes.add(stopTime);
    		}
    	}
    	if (foundStopTimes.size() == 0) {
    		return "No Next Trip";
    	}
    	Date currentDate = new Date(System.currentTimeMillis());
    	int hour = currentDate.getHours();
    	int min = currentDate.getMinutes();
    	for (StopTime foundStopTime: foundStopTimes) {
    		int foundHour = Integer.parseInt(foundStopTime.getArrivalTime().split(":")[0]);
    		if (hour == foundHour) {
    			currentHourTimes.add(foundStopTime);
    		} else if ((hour+1) == foundHour) {
    			nextHourTimes.add(foundStopTime);
    		}
    	}
    	for (StopTime currentHour : currentHourTimes) {
    		if (Integer.parseInt(currentHour.getArrivalTime().split(":")[1]) > min) {
    			return currentHour.getTripId();
    		}
    	}
    	for (StopTime nextHour : nextHourTimes) {
    		if (Integer.parseInt(nextHour.getArrivalTime().split(":")[1]) == 0) {
    			return nextHour.getTripId();
    		}
    	}
		return "Next trip in the morning";
    }

    /**
     * Constructor for Stops class with the parsed StopTimes File
     *
     * @param stopTimesFile File that is used to initially populate the stops List
     */
    public StopTimes(File stopTimesFile) throws Exception {
        stopTimes = new ArrayList<>();
        observers = new ArrayList<>();
        //importFile(stopTimesFile);
        importFileNew(stopTimesFile);
    }

    public List<StopTime> getStopTimes() {
        return stopTimes;
    }


    /**
     * Stop Time Getter at specific index
     *
     *  @param tgt Index
     * @return StopTiem
     */
    public StopTime getStopTime(int tgt){
        return stopTimes.get(tgt);
    }

    /**
     * This method returns a List of tripIDs associated with the List of stopIDs passed in without repeats
     *
     * @param tgtIDs is the List of stopIDs
     * @return List<String> of the found tripIDs
     */
    public List<String> getStopTimeByStop(List<String> tgtIDs){
        List<String> foundTrips = new ArrayList<>();
        for (String tgtID : tgtIDs) {
            for (StopTime stopTime : stopTimes) {
                if (stopTime.getStopId().equals(tgtID) && !foundTrips.contains(stopTime.getTripId())) {
                    foundTrips.add(stopTime.getTripId());
                }
            }
        }
        return foundTrips;
    }

    /**
     * The method facilitates searching for the stopID in the stopTimes List using tripIDs.  All matching IDs are
     * then added to a List of Strings containing the stopIDs. If there are no matches, the List will return empty.
     *
     * @param tgtIDs is the String List of the tgtIDs
     * @return ArrayList<String> of the found stopIDs
     */
    public List<String> getStopTimeByTrip(List<String> tgtIDs) {
        List<String> foundStopTimes = new ArrayList<>();
        for (String tgtTrip : tgtIDs) {
            for (StopTime stopTime : stopTimes) {
                if (stopTime.getTripId().equals(tgtTrip) && !foundStopTimes.contains(tgtTrip)) {
                    foundStopTimes.add(stopTime.getStopId());
                }
            }
        }
        return foundStopTimes;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
    
    public boolean importFileNew(File stopTimesFile)
    {
    	//trip_id,arrival_time,departure_time,stop_id,stop_sequence,stop_headsign,pickup_type,drop_off_type
		try {
			BufferedReader br = new BufferedReader(new FileReader(stopTimesFile));
			String readLine = br.readLine();
			readLine = br.readLine();
			while(readLine != null)
			{
				String[] line = readLine.split(",");
				addStopTime(new StopTime(line[0], line[1], line[2], line[3], line[4]));
				readLine = br.readLine();
			}
			br.close();
			return true;
		}
		catch (IOException e) {}
		catch (NullPointerException e) {} 
		catch (ArrayIndexOutOfBoundsException e) {} 
		catch (NumberFormatException e) {}
    	return false;
    }

    /**
     * Method to import a file to populate the stopTimes List
     *
     * @param stopTimesFile that is to be used
     */
    public boolean importFile(File stopTimesFile)throws Exception{
        String trip_id,arrival_time,departure_time,stop_id,stop_sequence, inLine;
        String timeData[];
       try{
           Scanner in = new Scanner(stopTimesFile);
           in.nextLine();
           while(in.hasNextLine()){
               inLine = in.nextLine();
               timeData = inLine.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
               trip_id = timeData[0];
               arrival_time = timeData[1];
               departure_time = timeData[2];
               stop_id = timeData[3];
               stop_sequence = timeData[4];
               addStopTime(new StopTime(trip_id,arrival_time,departure_time,stop_id,stop_sequence));
           }
           in.close();
           return true;
       }catch (FileNotFoundException e) {
           controller.displayError(e.toString());
       }catch (ArrayIndexOutOfBoundsException e){
           controller.displayError(e.toString());
       }catch (NumberFormatException e){
           controller.displayError(e.toString());
       }catch (NullPointerException e){
           controller.displayError(e.toString());
       }
       return false;
    }

    /**
     * Method to add a StopTime to the Collection Stops
     *
     * @param stopTime Stop to be added
     */
    public void addStopTime(StopTime stopTime){
        stopTimes.add(stopTime);
        notifyObservers();
    }

    /**
     * Method to remove a StopTime from the Collection stops
     *
     * @param stopTime StopTime to be removed
     */
    public void removeStopTime(StopTime stopTime){
        stopTimes.remove(stopTime);
        notifyObservers();
    }

    /**
     * Method to allow an observer to subscribe to this subject
     *
     * @param o Observer
     * @return true if the Observer is successfully added
     */
    public boolean addObserver(Observer o) {
        if (o == null) {
            return false;
        } else {
            observers.add(o);
            return true;
        }
    }

    /**
     * Method to allow an observer to unsubscribe to this subject
     *
     * @param o Observer
     * @return true if the observer successfully unsubcribed
     */
    @Override
    public boolean removeObserver(Observer o) {
        if (o == null) {

            return false;

        } else {

            observers.remove(o);
            return true;

        }
    }

    /**
     * Method to notify subscribed observers of an update
     */
    public void notifyObservers() {
        for(Observer o: observers) {
            o.update();
        }
    }

    /**
     * Method to compile the data in the stops List to be saved to a file
     *
     * @return the String value of the whole stops List
     */
    public String export(){
        String timeData = "trip_id,arrival_time,departure_time,stop_id,stop_sequence,stop_headsign,pickup_type,drop_off_type\n";
        for(StopTime stopTime : stopTimes){
            timeData += stopTime.export();
        }
        return timeData;
    }

    /**
     * Method that provides an instance of StopTimes to the requested Observer
     *
     * @return this instance of StopTimes
     */
    @Override
    public StopTimes update() {
        return this;
    }
}