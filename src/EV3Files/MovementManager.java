package EV3Files;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.io.File;
import java.io.FileWriter;

import EV3Files.threads.StopSeriesThread;
import EV3Files.threads.TrafficThread;
import GTFS_Project.Subject.*;
import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import lejos.hardware.lcd.*;
import lejos.utility.Delay;

public class MovementManager {
	public static volatile int pas = 0, scale = 100, speed = 0;
	public static volatile String nextStop = "None", curTrip = "None", status = "Inactive";
	public static volatile int delayH = 0, delayM = 0, delayS = 0;

	private static String eLn = "-             ";
	
	private static int stoppedMenu = 0;
	
	private static String[] displayOptions_Main = {"Run Trip", "Run # Stops",  "Run Custom Stops", eLn, "Change Speed", "Change Scale", eLn, eLn, eLn, eLn};
	private static String[][] displayOptions_Stopped = {{"Add Passenger", "Remove Passenger", eLn, "Record Arrival", eLn, eLn, eLn, eLn, eLn, eLn},
			{"Add Passenger", "Remove Passenger", eLn, "Record Departure", eLn, eLn, eLn, eLn, eLn, eLn},
			{"Add Passenger", "Remove Passenger", eLn, "Continue Trip", eLn, eLn, eLn, eLn, eLn, eLn}};
	private static String[] displayOptions_Moving = {eLn, eLn, eLn, eLn, eLn, eLn, eLn, eLn, eLn, eLn};
	private static String[] displayOptions = displayOptions_Main;
	
	private static ArrayList<String> customQueue = new ArrayList<>();
	private static String currentStopTimeData = "";
	
	public static volatile boolean isTrafficRunning = false;
	
	public static volatile int statusDetail = -1;
	
	public static int STATUS_INACTIVE = -1, STATUS_STOPPED = 0, STATUS_ACTIVE = 1, STATUS_STOPSERIES = 2, 
			STATUS_EXPORT_QUEUE = 3, STATUS_END_TRAFFIC = 4;
	
	private static boolean shouldClear = false;
	
	private static DateFormat dF = new SimpleDateFormat("HH:mm:ss");
	
	public static volatile Stop currentStop;
	
	public static void openGUI(Stops s, Routes r, Trips t, StopTimes ti)
	{
		BrickFinder.getLocal().getGraphicsLCD().clear(); 
		updateUI(0);
		Delay.msDelay(250);
		int selectedItem = 0;
		int refresher = 0;
		
		while(true)
		{
			if(statusDetail == STATUS_STOPSERIES)
			{
				Thread stopSeriesThread = new StopSeriesThread();
				MovementManager.statusDetail = MovementManager.STATUS_ACTIVE;
				stopSeriesThread.start();
				updateUI(selectedItem);
				Delay.msDelay(250);
			}
			if(statusDetail == STATUS_EXPORT_QUEUE)
			{
				statusDetail = STATUS_INACTIVE;				
				try {
					String Line = "";
					File stopTimesCustomFile = new File("./custom_stoptimes.txt");
					FileWriter fw = new FileWriter(stopTimesCustomFile);
					for(int x = 0; x < customQueue.size(); x++)
					{
						Line += customQueue.get(x);
					}
					fw.write(Line);
					fw.flush();
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if(refresher < 5000)
			{
				refresher++;
			}
			else{updateUI(selectedItem); refresher = 0;}
			
			if(BrickFinder.getLocal().getKeys().getButtons() == Keys.ID_DOWN){
				if((selectedItem + 1) <= 9)
				{
					selectedItem++;
				}
				else { selectedItem = 0; }
				updateUI(selectedItem);
				Delay.msDelay(250);
			}
			else if(BrickFinder.getLocal().getKeys().getButtons() == Keys.ID_UP){
				if((selectedItem - 1) >= 0)
				{
					selectedItem--;
				}
				else { selectedItem = 9; }
				updateUI(selectedItem);
				Delay.msDelay(250);
			}
			
			if(BrickFinder.getLocal().getKeys().getButtons() == Keys.ID_ENTER)
			{
				runCommand(selectedItem, s, r, t, ti);
				Delay.msDelay(250);
			}

			if(BrickFinder.getLocal().getKeys().getButtons() == Keys.ID_ESCAPE)
			{
				Delay.msDelay(250);
				break;
			}
		}
	}
	
	private static void updateUI(int selectedItem)
	{
		TextLCD tlcd = BrickFinder.getLocal().getTextLCD(Font.getSmallFont());
		GraphicsLCD glcd = BrickFinder.getLocal().getGraphicsLCD();
		if(shouldClear) { tlcd.clear(); shouldClear = false; }
		
		if(status.equals("Stopped"))
		{
			displayOptions = displayOptions_Stopped[stoppedMenu];
		}
		else if(status.equals("Moving"))
		{
			displayOptions = displayOptions_Moving;
		}
		else{displayOptions = displayOptions_Main;}
		
		
		tlcd.refresh();
		
		tlcd.drawString("Current Trip:" + curTrip, 0, 0);
		tlcd.drawString("Next Stop:" + nextStop, 0, 1);
		
		tlcd.drawString("Passengers:    ", 0, 3);
		tlcd.drawString(pas + "", 11, 3);
		
		tlcd.drawString("Robot Scale:", 0, 5);
		tlcd.drawString("              ", 0, 6);
		tlcd.drawString(scale + "", 0, 6);
		
		tlcd.drawString("Robot Speed:  ", 0, 8);
		tlcd.drawString("              ", 0, 9);
		tlcd.drawString(speed + "", 0, 9);
		
		tlcd.drawString("Total Delay:", 0, 11);
		tlcd.drawString("              ", 0, 12);
		tlcd.drawString(delayH + "h " + delayM + "m " + delayS + "s", 0, 12);
		
		BrickFinder.getLocal().getTextLCD(Font.getDefaultFont()).drawString("Status:             ", 0, 7);
		BrickFinder.getLocal().getTextLCD(Font.getDefaultFont()).drawString(status, 7, 7);
		
		int lower = glcd.getHeight() - Font.getDefaultFont().height - 3;
		glcd.drawLine(glcd.getWidth() / 2, (Font.getSmallFont().height * 2) + 3, glcd.getWidth()/2, lower);
		glcd.drawLine(0, lower, glcd.getWidth(), lower);
		glcd.drawLine(0, glcd.getHeight() - lower, glcd.getWidth(), glcd.getHeight() - lower);
		
		for(int y = 0; y < 10; y++)
		{
			tlcd.drawString("              ", 16, 3 + y);
			tlcd.drawString(displayOptions[y], 16, 3 + y, y == selectedItem);
		}
	}
	
	private static void runCommand(int selectedItem, Stops s, Routes r, Trips t, StopTimes ti) 
	{
		shouldClear = true;		
		String chosen = displayOptions[selectedItem];
		
		if(chosen.equals("Run Trip"))
		{
			if(!MovementManager.isTrafficRunning)
        	{
	        	TrafficThread traffic = new TrafficThread();
	        	traffic.start();        	
	        	MovementManager.isTrafficRunning = true;
        	}
			Main.moveTrip();
		}
		else if(chosen.equals("Run # Stops"))
		{
			if(!MovementManager.isTrafficRunning)
        	{
	        	TrafficThread traffic = new TrafficThread();
	        	traffic.start();        	
	        	MovementManager.isTrafficRunning = true;
        	}
			Main.moveNumStops();
		}else if (chosen.equals("Change Speed")) {
			Main.setSpeedMenu();
		}else if (chosen.equals("Change Scale")) {
			Main.setScaleMenu();
		}
		else if(chosen.equals("Continue Trip"))
		{
			status = "Waking";
			Delay.msDelay(1000);
			statusDetail = STATUS_ACTIVE;
			stoppedMenu = 0;
			currentStopTimeData += currentStop.getStopID() + "," + customQueue.size() + ",,,";
			customQueue.add(currentStopTimeData);
		}
		else if(chosen.equals("Record Arrival"))
		{
			currentStopTimeData += "CUSTOM_QUEUE_TRIP," + dF.format(new Date()) + ",";
			stoppedMenu = 1;
		}
		else if(chosen.equals("Record Departure"))
		{
			currentStopTimeData += dF.format(new Date()) + ",";
			stoppedMenu = 2;
		}
		else if(chosen.equals("Run Custom Stops"))
		{
			if(s != null) {
				StopSimulator.openStopPicker(s.getStops());
			}
			else
			{
				displayMessage.displayCenterMessage("Please import stops first", Font.getSmallFont());
				Delay.msDelay(1000);
			}
		}
//		else if(chosen.equals("~Toggle Mode~"))
//		{
//			if(status.equalsIgnoreCase("Inactive"))
//			{
//				status = "Active";
//			}
//			else if(status.equalsIgnoreCase("Active"))
//			{
//				status = "Stopped";
//			}	
//			else if(status.equalsIgnoreCase("Stopped"))
//			{
//				status = "Inactive";
//			}
//		}
		else if(chosen.equals("Add Passenger"))
		{
			if(statusDetail == STATUS_STOPPED)
			{
				pas++;
			}
		}
		else if(chosen.equals("Remove Passenger"))
		{
			if((statusDetail == STATUS_STOPPED) && ((pas - 1) >= 0))
			{
				pas--;
			}
		}
		else return;
	}
}
