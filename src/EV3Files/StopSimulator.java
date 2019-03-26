package EV3Files;

import java.util.ArrayList;
import java.util.List;

import GTFS_Project.Subject.Stop;
import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import lejos.hardware.lcd.*;
import lejos.utility.Delay;

public class StopSimulator {
	private static String lastChosen = "";
	public static volatile ArrayList<Stop> choices = new ArrayList<>();
	
	public static void openStopPicker(List<Stop> stops)
	{
		Delay.msDelay(250);
		choices.clear();
		int selectedID = 0, realID = 0, minID = 0;
		TextLCD tlcd = BrickFinder.getLocal().getTextLCD(Font.getSmallFont());
		
		String[] stopsList = new String[stops.size()];
		
		for(int x = 0; x < stopsList.length; x++)
		{
			stopsList[x] = stops.get(x).getName();
		}
		
		updateUI(stopsList, selectedID, realID, minID);
		
		while(true)
		{
			if(BrickFinder.getLocal().getKeys().getButtons() == Keys.ID_DOWN){
				if((selectedID + 1) < (tlcd.getTextHeight() - 4))
				{
					selectedID++;
				}
				else if( ((minID + 1) <= (stops.size() - selectedID)) && (selectedID == (tlcd.getTextHeight() - 5)) && ((minID + 1) < stopsList.length))
				{
					minID++;
				}
				
				if((realID + 1) < stopsList.length)
				{
					realID++;
				}
				updateUI(stopsList, selectedID, realID, minID);
				Delay.msDelay(250);
			}
			if(BrickFinder.getLocal().getKeys().getButtons() == Keys.ID_RIGHT){
				MovementManager.status = "Active";
				MovementManager.statusDetail = MovementManager.STATUS_STOPSERIES;
				Delay.msDelay(250);
				break;
			}
			else if(BrickFinder.getLocal().getKeys().getButtons() == Keys.ID_UP){
				if((selectedID - 1) >= 0)
				{
					selectedID--;
				}
				else if(((minID - 1) >= 0) && (selectedID == 0))
				{
					minID--;
				}
				if((realID - 1) >= 0)
				{
					realID--;
				}
				updateUI(stopsList, selectedID, realID, minID);
				Delay.msDelay(250);
			}
			
			if(BrickFinder.getLocal().getKeys().getButtons() == Keys.ID_ENTER)
			{
				lastChosen = stops.get(selectedID + minID).getName();
				choices.add(stops.get(selectedID + minID));
				updateUI(stopsList, selectedID, realID, minID);
				Delay.msDelay(250);
			}

			if(BrickFinder.getLocal().getKeys().getButtons() == Keys.ID_ESCAPE)
			{
				Delay.msDelay(250);
				break;
			}
		}
	}
	
	private static void updateUI(String[] options, int selectedID, int realID, int minID)
	{
		TextLCD tlcd = BrickFinder.getLocal().getTextLCD(Font.getSmallFont());
		GraphicsLCD glcd = BrickFinder.getLocal().getGraphicsLCD();
		tlcd.clear();
		
		tlcd.drawString("--------Stop Selection--------", 0, 0);
		tlcd.drawString("Last Selected: " + lastChosen, 0, 1);
		tlcd.drawString("Number Selected:" + choices.size(), 0, 2);
		tlcd.drawString("Next->", tlcd.getTextWidth() - 6, 2);
		//tlcd.drawString(selectedID + " - " + realID + " - " + minID, 0, 2);
		glcd.drawLine(0, (Font.getSmallFont().height * 3) + 2, glcd.getWidth(), (Font.getSmallFont().height * 3) + 2);
		
		for(int y = minID; (y - minID) < (tlcd.getTextHeight() - 4) && (y < options.length); y++)
		{
			tlcd.drawString(options[y], 0, y - minID + 4, (y - minID) == selectedID);
		}
		
		if(minID > 0){ tlcd.drawString("/\\", tlcd.getTextWidth() - 2, 4); }
		if((minID + (tlcd.getTextHeight() - 4)) < options.length){ tlcd.drawString("\\/", tlcd.getTextWidth() - 2, tlcd.getTextHeight() - 1); }
	}
}
