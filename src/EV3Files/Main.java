package EV3Files;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.List;

import EV3Files.threads.TrafficThread;

import java.util.ArrayList;
import java.util.Date;
import java.lang.*;

import GTFS_Project.Subject.*;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Delay;
import lejos.utility.Stopwatch;
import lejos.utility.TextMenu;
import se2800awtvb.robot.Pilot;

public class Main{
	private static Stops stops;
	private static Routes routes;
	private static Trips trips;
	private static StopTimes times;
	//Default value for amount of cm you want the robot to move per km of irl distance.
	private static int desiredCMperKM = 100;
	private static double desiredAngularSpeed = 50;
	private static double desiredLinearSpeed = 10;
	private static DifferentialPilot pilot;
	private static RegulatedMotor leftMotor = Motor.B;
	private static RegulatedMotor rightMotor = Motor.A;
	private static float wheelDi = 5.0f;
	private static float trackWid = 25.0f;
	private static Stopwatch stopwatch;
	private static double currentAngle = 0;
	
	public static void main(String[] args) throws InterruptedException{
		if(!MovementManager.isTrafficRunning)
    	{
        	TrafficThread traffic = new TrafficThread();
        	traffic.start();        	
        	MovementManager.isTrafficRunning = true;
    	}
		String[] options = { "Search", "Import", "Export", "Edit Stop Times", "Move","Exit" };
		while (true) {
			LCD.clear();
			LCD.refresh();
			Button.LEDPattern(4);
			switch((new TextMenu(options, 1, "Main Menu")).select()) { 
			case 0:
				Button.LEDPattern(1);
				searchMenu();
				break;
			case 1:
				Button.LEDPattern(3);
				importMenu();
				break;
			case 2:
				Button.LEDPattern(2);
				export();
				break;
			case 3:
				editMenu();
				break;
			case 4:
				MovementManager.openGUI(stops, routes, trips, times);
				break;
			case 5:
				System.exit(0);
				break;
			case -1:
			default:
				System.exit(0);
				break;
			}
		}
	}
	
	private static void export() {
		LCD.clear();
		Button.LEDPattern(1);
		String[] options = {"Stops","Trips","Routes","Stop Times"};
		FileWriter fw;
		String sf = null;
		String tf = null;
		String rf = null;
		String stf = null;
		File stopsFile;
		File tripsFile;
		File routesFile;
		File stopTimesFile;
		while(true) {
			switch((new TextMenu(options, 1,"Export Menu")).select()) {
			case 0:
				try {
				sf = stops.export();
				} catch(Exception e) {
					displayMessage.displayCenter("Import First");
					Button.ENTER.waitForPress();
				}
				stopsFile = new File("./stopsFile.txt");
				try {
				stopsFile.createNewFile();
				fw = new FileWriter(stopsFile);
				fw.write(sf);
				fw.flush();
				fw.close();
				displayMessage.displayCenter("Finished Exporting");
				Button.ENTER.waitForPress();
				} catch(Exception e) {
					displayMessage.displayCenter("Import First");
					Button.ESCAPE.waitForPress();
				}
				break;
			case 1:
				try {
				tf = trips.export();
				}catch(Exception e) {
					displayMessage.displayCenter("Import First");
					Button.ENTER.waitForPress();
				}
				tripsFile = new File("./tripsFile.txt");
				try {
				tripsFile.createNewFile();
				fw = new FileWriter(tripsFile);
				fw.write(tf);
				fw.flush(); 
				fw.close();
				displayMessage.displayCenter("Finished Exporting");
				Button.ENTER.waitForPress();
				}catch(Exception e) {
					displayMessage.displayCenter("Import First");
					Button.ESCAPE.waitForPress();
				}
				break;
			case 2:
				try {
				rf = routes.export(); 
				} catch(Exception e) {
					displayMessage.displayCenter("Import First");
					Button.ENTER.waitForPress();
				}
				routesFile = new File("./routesFile.txt");
				try {
				routesFile.createNewFile();
				fw = new FileWriter(routesFile);
				fw.write(rf);
				fw.flush();
				fw.close();
				displayMessage.displayCenter("Finished Exporting");
				Button.ENTER.waitForPress();
				}catch(Exception e) {
					displayMessage.displayCenter("Import First");
					Button.ENTER.waitForPress();
				}
				break;
			case 3:
				try {
				stf = times.export();
				}
				catch(Exception e) {
					displayMessage.displayCenter("Import First");
					Button.ENTER.waitForPress();
				}
				stopTimesFile = new File("./stopTimesFile.txt");
				try {
				stopTimesFile.createNewFile();
				fw = new FileWriter(stopTimesFile);
				fw.write(stf);
				fw.flush();
				fw.close();
				displayMessage.displayCenter("Finished Exporting");
				Button.ENTER.waitForPress();
				}catch(Exception e) {
					displayMessage.displayCenter("Import First");
					Button.ESCAPE.waitForPress();
				}
				break;
			case -1:
			default:
				return;
			}
		}	
		
	}
	public static void moveTrip()
    {
        try {
            String[] tripIDs = new String[trips.getTrips().size()];
            ArrayList<StopTime> stopTimes = new ArrayList<StopTime>();
            ArrayList<Stop> stops = new ArrayList<Stop>();
            
            for (int i = 0; i < trips.getTrips().size(); i++) {
                tripIDs[i] = trips.getTrips().get(i).getTripID();
            }
            int indexTrip = (new TextMenu(tripIDs, 1, "Trip IDs")).select();
            //Finds what stoptimes have the trip id then adds the stop id to a arraylist
            if (indexTrip >= 0) {
                String tripID = tripIDs[indexTrip];
                for (StopTime stopTime : times.getStopTimes()) {
                    if (stopTime.getTripId().equals(tripID)) {
                        stopTimes.add(stopTime);
                    }
                }
                for (StopTime stopTime : stopTimes) {
                    for (Stop stop : Main.stops.getStops()) {
                        if (stopTime.getStopId().equals(stop.getStopID())) {
                            stops.add(stop);
                        }
                    }
                }
                moveAlongTrip(stops, stops.size());
            }
        } catch (NullPointerException e) {
            displayMessage.displayCenter("Import first");
            Button.ESCAPE.waitForPress();
        }
    }
	
	public static void moveNumStops()
    {
        try {
            String[] tripIDs = new String[trips.getTrips().size()];
            ArrayList<StopTime> stopTimes = new ArrayList<StopTime>();
            ArrayList<Stop> stops = new ArrayList<Stop>();
            for (int i = 0; i < trips.getTrips().size(); i++) {
                tripIDs[i] = trips.getTrips().get(i).getTripID();
            }
            int indexTrip = (new TextMenu(tripIDs, 1, "Trip IDs")).select();
            if (indexTrip >= 0) {
                String tripID = tripIDs[indexTrip];
                for (StopTime stopTime : times.getStopTimes()) {
                    if (stopTime.getTripId().equals(tripID)) {
                        stopTimes.add(stopTime);
                    }
                }
                for (StopTime stopTime : stopTimes) {
                    for (Stop stop : Main.stops.getStops()) {
                        if (stopTime.getStopId().equals(stop.getStopID())) {
                            stops.add(stop);
                        }
                    }
                }
                LCD.clear();
                String[] stopNumbers = new String[stops.size()];
                if (stops.size() > 0) {
                    stopNumbers[0] = 1 + "Stop";
                }
                for (int i = 1; i < stops.size(); i++) {
                    stopNumbers[i] = i + 1 + "Stops";
                }
                int numberOfStops = (new TextMenu(stopNumbers, 1, "Number of Stops")).select() + 1;
                moveAlongTrip(stops, numberOfStops);
            }
        } catch (NullPointerException e) {
            displayMessage.displayCenter("Import first");
            Button.ESCAPE.waitForPress();
        }
    }
	
	private static void editMenu()
	{
		LCD.clear();
		try
		{
			String[] stList = new String[times.getStopTimes().size()];
			for(int x = 0; x < times.getStopTimes().size(); x++)
			{
				stList[x] = times.getStopTimes().get(x).getStopId();
			}
			int chosenStopIDIndex = (new TextMenu(stList, 1, "Pick a Stop:")).select();
			if(chosenStopIDIndex >= 0)
			{
				Object[] tList = times.getTripIDsByStopID(stList[chosenStopIDIndex]).toArray();
				String[] tListS = new String[tList.length];
				for(int x = 0; x < tList.length; x++)
				{
					tListS[x] = tList[x].toString();
				}
				
				int chosenTripIDIndex = (new TextMenu(tListS, 1, "Pick a Trip:").select());
				if(chosenTripIDIndex >= 0)
				{	
					Delay.msDelay(250);
					StopTime oldStop = times.getStopTimesByTripID(tListS[chosenTripIDIndex]).get(0);
					
					displayMessage.promptEditString("Editing Arrival", oldStop.getArrivalTime());
					if(Keyboard.typedString == "")
					{
						displayMessage.displayCenter("Skipping Edit");
						Delay.msDelay(500);
					}
					else if(times.verifyStopTimeTime(Keyboard.typedString))
					{
						oldStop.setArrivalTime(Keyboard.typedString);	
						Delay.msDelay(250);
					}
					else
					{
						displayMessage.displayCenter("Invalid Time!");
						Delay.msDelay(500);
					}
					displayMessage.promptEditString("Editing Departure", oldStop.getDepartureTime());
					if(Keyboard.typedString == "")
					{
						displayMessage.displayCenter("Skipping Edit");
						Delay.msDelay(500);
					}
					else if(times.verifyStopTimeTime(Keyboard.typedString))
					{
						oldStop.setDepartureTime(Keyboard.typedString);
						Delay.msDelay(250);
					}
					else
					{
						displayMessage.displayCenter("Invalid Time!");
						Delay.msDelay(500);
					}
					times.editTime(times.getStopIDIndex(oldStop.getStopId()), oldStop);
					displayMessage.displayCenter("Success");
					Delay.msDelay(500);
					return;	
				}
			}
		}
		catch(NullPointerException e)
		{
			displayMessage.displayCenter("No stops loaded. Please load stops before editing.");
			Delay.msDelay(1000);
		}
	}
		
	private static void searchMenu() {
		LCD.clear();
		String[] options = { "Trip by route", "Route by stop", "Next trip by stop" };
		while (true) {
			switch ((new TextMenu(options, 1, "Search")).select()) {
			case 0:
				try {
					String[] routeIDs = new String[routes.getRoutes().size()];
					ArrayList<String> tripIDs = new ArrayList<String>();
					for (int i = 0; i < routes.getRoutes().size(); i++) {
						routeIDs[i] = routes.getRoutes().get(i).getRouteID();
					}
					int indexRoute = (new TextMenu(routeIDs, 1, "Route IDs")).select();
					if (indexRoute >= 0) {
						// get all tripIDs for the specified route
						for (int i = 0; i < trips.getTrips().size(); i++) {
							if (trips.getTrip(i).getRouteID().equals(routeIDs[indexRoute])) {
								tripIDs.add(trips.getTrip(i).getTripID());
							}
						}
						if (tripIDs.size()==0) {
							displayMessage.displayCenter("No trips found!");
							Button.ENTER.waitForPress();
							return;
						}
						String nextTripID = times.getNextTripByTripIDs(tripIDs);
						//displayMessage.displayScrollableMessage(display, Font.getSmallFont());
						displayMessage.displayCenterMessage(nextTripID, Font.getSmallFont());
						Button.ESCAPE.waitForPress();
						return;
					}
				} catch (Exception e) {
					displayMessage.displayCenter("Import first");
					Button.ESCAPE.waitForPress();
				}
			break;
			case 1:
				try {
					String[] stopIDs = new String[stops.getStops().size()];
					List<String> routeIDsList = new ArrayList<String>();
					for (int i = 0; i < stops.getStops().size(); i++) {
						stopIDs[i] = stops.getStops().get(i).getStopID();
					}
					int indexStop = (new TextMenu(stopIDs, 1, "Stop IDs")).select();
				if (indexStop >= 0) {
					routeIDsList = stops.searchStopEV3(stopIDs[indexStop], times, trips);
					if (routeIDsList.size() == 0) {
						displayMessage.displayCenterMessage("No routes found!", Font.getSmallFont());
                        Button.ENTER.waitForPress();
						return;
					}
					String[] display = new String[routeIDsList.size()];
					for(int x = 0; x < routeIDsList.size(); x++)
					{
						display[x] = routeIDsList.get(x);
					}
					displayMessage.displayScrollableMessage(display, Font.getSmallFont());
					Button.ESCAPE.waitForPress();
					return;
				}
				} catch (NullPointerException e) {
				    displayMessage.displayCenter("Import first");
				    Button.ESCAPE.waitForPress();
				}
				break;
			case 2:
			    try {
				String[] IDs = new String[stops.getStops().size()];
				for (int i = 0; i < stops.getStops().size(); i++) {
					IDs[i] = stops.getStops().get(i).getStopID();
				}
				int index = (new TextMenu(IDs, 1, "Stop IDs")).select();
				if (index >= 0) {
					String message = times.getNextTripByStopID(IDs[index]);
					LCD.clear();
					displayMessage.displayCenterMessage(message, Font.getSmallFont());
					Button.ENTER.waitForPress();
					return;
				}
				} catch (Exception e) {
				    displayMessage.displayCenter("Import first");
				    Button.ESCAPE.waitForPress();
				}
				break;
			case -1:
			default:
				return;
			}
		}
	}
	
	
	private static void importAll()
	{
		LCD.clear(); 
		displayMessage.displayCenterMessage("Loading all files", Font.getDefaultFont());
		Delay.msDelay(500);
		try{
			routes = new Routes(new File("./routes.txt"));
		}catch (Exception e) {
			displayMessage.displayCenter("Failed Routes");
			Delay.msDelay(500);
		}
		
		try{
			stops = new Stops(new File("./stops.txt"));
		}catch (Exception e) {
			displayMessage.displayCenter("Failed Stops");
			Delay.msDelay(500);
		}
		
		try{
			times = new StopTimes(new File("./stop_times.txt"));
		}catch (Exception e) {
			displayMessage.displayCenter("Failed Stop Times");
			Delay.msDelay(500);
		}
		
		try{
			trips = new Trips(new File("./trips.txt"));
		}catch (Exception e) {
			displayMessage.displayCenter("Failed Trips");
			Delay.msDelay(500);
		}
		
		displayMessage.displayCenter("Imported All Files");
		Delay.msDelay(1000);
	}
		
	private static void importMenu() {
		LCD.clear();
		Button.LEDPattern(1);
		File gtfs = new File("./");
		File[] fileList = gtfs.listFiles();
		String[] options = { "Routes", "Stops", "Stop Times", "Trips", "All"};
		String[] fileNames = new String[fileList.length];
		for (int i = 0; i < fileList.length; i++) {
			fileNames[i] = fileList[i].getName();
		}
		while (true) {
			switch ((new TextMenu(options, 1, "Import")).select()) {
			case 0:
				int routeFileIndex = (new TextMenu(fileNames, 1, "Route GTFS")).select();
				if (routeFileIndex < 0) {
					displayMessage.displayCenterMessage("Please select a file", Font.getSmallFont());
					Button.ENTER.waitForPress();
					return;
				}
				File routeFile = fileList[routeFileIndex];
				LCD.clear();
				Button.LEDPattern(7);
				displayMessage.displayCenter("Importing...");
				try {
					routes = new Routes(routeFile);
					displayMessage.displayCenter("Success!");
					Button.ENTER.waitForPress();
					} catch (Exception e) { 
						displayMessage.displayCenterMessage("Failed. Bad file", Font.getSmallFont());
						Button.ENTER.waitForPress();}
				break;
			case 1:
				int stopFileIndex = (new TextMenu(fileNames, 1, "Route GTFS")).select();
				if (stopFileIndex < 0) {
					displayMessage.displayCenterMessage("Please select a file", Font.getSmallFont());
					Button.ENTER.waitForPress();
					return;
				}
				File stopFile = fileList[stopFileIndex];
				LCD.clear();
				Button.LEDPattern(7);
				displayMessage.displayCenter("Importing...");
				try {
					stops = new Stops(stopFile);
					displayMessage.displayCenter("Success!");
					Button.ENTER.waitForPress();
					} catch (Exception e) { displayMessage.displayCenterMessage("Failed. Bad file", Font.getSmallFont());
					Button.ENTER.waitForPress();}
				break;
			case 2:
				int stopTimeFileIndex = (new TextMenu(fileNames, 1, "Route GTFS")).select();
				if (stopTimeFileIndex < 0) {
					displayMessage.displayCenterMessage("Please select a file", Font.getSmallFont());
					Button.ENTER.waitForPress();
					return;
				}
				File stopTimeFile = fileList[stopTimeFileIndex];
				LCD.clear();
				Button.LEDPattern(7);
				displayMessage.displayCenter("Importing...");
				try {
					times = new StopTimes(stopTimeFile);
					displayMessage.displayCenter("Success!");
					Button.ENTER.waitForPress();
					} catch (Exception e) { displayMessage.displayCenterMessage("Failed. Bad file", Font.getSmallFont());
					Button.ENTER.waitForPress();}
				break;
			case 3:
				
				int tripFileIndex = (new TextMenu(fileNames, 1, "Route GTFS")).select();
				if (tripFileIndex < 0) {
					displayMessage.displayCenterMessage("Please select a file", Font.getSmallFont());
					Button.ENTER.waitForPress();
					return;
				}
				File tripFile = fileList[tripFileIndex];
				LCD.clear();
				Button.LEDPattern(7);
				displayMessage.displayCenter("Importing...");
				try {
					trips = new Trips(tripFile);
					displayMessage.displayCenter("Success!");
					Button.ENTER.waitForPress();
				} catch (Exception e) { displayMessage.displayCenterMessage("Failed. Bad file", Font.getSmallFont());
				Button.ENTER.waitForPress();}
				break;
			case 4:
				importAll();
				break;
			case -1:
			default:
				return;
			}
		}
	}
	
	/**
	 * Menu to allow the user to select a speed from 1 to 100.
	 */
	public static void setSpeedMenu() {
		LCD.clear();
		String[] speeds = new String[100];
		for (int i = 1; i <= 100; i++) {
			speeds[i - 1] = "" + i;
		}
		int chosenSpeedIndex = (new TextMenu(speeds, 1, "Pick a Speed:").select());
		MovementManager.speed = chosenSpeedIndex + 1;
	}
	
	/**
	 * Menu to allow the user to select a scale from 1 to 100.
	 */
	public static void setScaleMenu() {
		LCD.clear();
		String[] scales = new String[100];
		for (int i = 1; i <= 100; i++) {
			scales[i - 1] = "" + i;
		}
		int chosenScaleIndex = (new TextMenu(scales, 1, "Pick a Scale:").select());
		MovementManager.scale = chosenScaleIndex + 1;
	}

	/**
	 * Takes in an arraylist and number of stops then moves between them
	 * @param stopTimes
	 * @param numberOfStops
	 */
	private static void moveAlongTrip(ArrayList<Stop> stops, int numberOfStops) {
		LCD.clear();
        pilot = new DifferentialPilot(wheelDi, trackWid, leftMotor, rightMotor);
        pilot.setAngularSpeed(desiredAngularSpeed);
        pilot.setLinearSpeed(MovementManager.speed);
        pilot.setLinearAcceleration(4000);
        ArrayList<Stop> theStops = new ArrayList<Stop>();
        theStops.addAll(stops);
        int number = numberOfStops;
        //stopwatch = new Stopwatch();
        double currentLat;
        double currentLong;
        double distance;
        double angle;
        Stop currentStop;
        Stop nextStop;
        long timePassed = 0;
        currentStop = theStops.get(0);
        currentLat = currentStop.getLatitude();
        currentLong = currentStop.getLongitude();
        //stopwatch.reset();
        	for (int i = 1; i < number; i++) {
        		nextStop = theStops.get(i);
        		distance = findDistance(currentLat, currentLong, nextStop.getLatitude(), nextStop.getLongitude());
        		distance = distance * desiredCMperKM;
        		angle = angleFromCoordinate(currentLat, currentLong, nextStop.getLatitude(), nextStop.getLongitude());
        		MovementManager.status = "Moving";
        		pilot.rotate(angle);
        		long firstTime = System.currentTimeMillis()/1000;
        		pilot.travel(distance);
        		MovementManager.status = "Stopped";
        		long secondTime = System.currentTimeMillis()/1000;
        		timePassed = timePassed + (secondTime - firstTime);
        		displayMessage.displayCenter(Long.toString(timePassed) + "Seconds");
        		Sound.beep();
        		Delay.msDelay(2000);
        		currentStop = nextStop;
        	}
            //displayMessage.displayCenter(Integer.toString(stopwatch.elapsed()/1000));	
        Sound.buzz();
        Sound.beepSequence();
        Sound.beepSequenceUp();
        Sound.twoBeeps();
        MovementManager.status = "End of Trip";
    }
	
	/**
	 * Returns the distance between two points in kilometeres
	 * @param currentLat
	 * @param currentLong
	 * @param nextLat
	 * @param nextLong
	 * @return distance in km
	 */
	public static double findDistance(double currentLat, double currentLong, double nextLat, double nextLong) {
		double R = 6371; //Earth radius in km
	    double dLat = Math.toRadians(currentLat - nextLat);
	    double dLon = Math.toRadians(currentLong - nextLong);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(Math.toRadians(nextLat)) * Math.cos(Math.toRadians(currentLat)) * 
	            Math.sin(dLon/2) * Math.sin(dLon/2); 
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
	    return R * c;
	}
	
	/**
	 * Returns the angle between two points
	 * @param currentLat
	 * @param currentLong
	 * @param nextLat
	 * @param nextLong
	 * @return angle between coordinates
	 */
	public static double angleFromCoordinate(double currentLat, double currentLong, double nextLat, double nextLong) {
		double long1 = Math.toRadians(currentLong);
		double long2 = Math.toRadians(nextLong);
		double lat1 = Math.toRadians(currentLat);
		double lat2 = Math.toRadians(nextLat);
	    double dLon = (long2 - long1);

	    double y = Math.sin(dLon) * Math.cos(lat2);
	    double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
	            * Math.cos(lat2) * Math.cos(dLon);

	    double angle = Math.atan2(y, x);

	    angle = Math.toDegrees(angle);
	    angle = (angle + 360) % 360;
	    if (angle > 180) {
	    	angle = 360 - angle;
	    	angle = -angle;
	    }
	    angle = angle - currentAngle;
        currentAngle = currentAngle + angle;
	    return angle;
	}
}

