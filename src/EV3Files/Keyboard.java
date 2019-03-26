package EV3Files;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Keys;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.utility.Delay;

public class Keyboard {
	
	public static String typedString = ""; //This is the string that is "returned" by the keyboard. It can be accessed at any time
	
	public static void runKeyboard(int displayX, int displayY)
	{
		typedString = "";
		GraphicsLCD lcd = BrickFinder.getDefault().getGraphicsLCD();
		int w = lcd.getWidth();
		int h = lcd.getHeight();
		
		//Keyboard contents
		String[][] keys = {{"1", "2", "3", "4", "5", "6", "7", "8", "9", "0", ":"}, 
				{"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", ";"},
				{"A", "S", "D", "F", "G", "H", "J", "K", "L", " ", ">"},
				{"Z", "X", "C", "V", "B", "N", "M", ",", ".", "?", "!"}};
		
		//Draw those keys
		drawKeyboard(keys);
		
		int cursorX = 0, cursorY = 0;
		int lastX = 0, lastY = 0;
		
		invertRegion(10 + (10 * 0) + (0 * 5), 60 + (15 * 0) + (0), 22 + (10 * 0) + (0 * 5), 76 + (15 * 0) + (0));
		
		while(true)
		{
			//Key responses for each button available for use
			if(BrickFinder.getLocal().getKeys().getButtons() == Keys.ID_DOWN){
				lastX = cursorX; lastY = cursorY; //Update stuff for the highlighted key display
				if( (cursorY + 1) < 4){cursorY++;} //Loop the cursor if needed
				else if((cursorY + 1) >= 4){cursorY = 0;}
				toggleKey(cursorX, cursorY, lastX, lastY); //Toggle the visual state of the key
				Delay.msDelay(250); //Make sure users dont fat finger
			}
			else if(BrickFinder.getLocal().getKeys().getButtons() == Keys.ID_UP){
				lastX = cursorX; lastY = cursorY;
				if( (cursorY - 1) >= 0){cursorY--;}
				else if((cursorY - 1) < 0){cursorY = 3;}
				toggleKey(cursorX, cursorY, lastX, lastY);
				Delay.msDelay(250);
			}
			else if(BrickFinder.getLocal().getKeys().getButtons() == Keys.ID_RIGHT){
				lastX = cursorX; lastY = cursorY;
				if( (cursorX + 1) < keys[cursorY].length){cursorX++;}
				else if((cursorX + 1) >= keys[cursorY].length){cursorX = 0;}
				toggleKey(cursorX, cursorY, lastX, lastY);
				Delay.msDelay(250);
			}
			else if(BrickFinder.getLocal().getKeys().getButtons() == Keys.ID_LEFT){
				lastX = cursorX; lastY = cursorY;
				if( (cursorX - 1) >= 0){cursorX--;}
				else if((cursorX - 1) < 0){cursorX = keys[cursorY].length - 1;}
				toggleKey(cursorX, cursorY, lastX, lastY);
				Delay.msDelay(250);
			}
			
			if(BrickFinder.getLocal().getKeys().getButtons() == Keys.ID_ENTER)
			{
				if(keys[cursorY][cursorX].equals(">")) //Should we exit while keeping the contents?
				{
					break;
				}
				else
				{
					//Otherwise just apphend the new clicked button and display it
					typedString += keys[cursorY][cursorX];
					displayMessage.drawStringAtPos(displayX, displayY, typedString);
				}
				Delay.msDelay(250);
			}
			
			//Back button doesnt return the typed string if used
			if(BrickFinder.getLocal().getKeys().getButtons() == Keys.ID_ESCAPE)
			{
				typedString = "";
				Delay.msDelay(250);
				break;
			}
		}
	}
	
	//Toggle the new and old key visual states
	private static void toggleKey(int nX, int nY, int oX, int oY)
	{
		invertRegion(10 + (10 * oX) + (oX * 5), 60 + (15 * oY) + (oY), 22 + (10 * oX) + (oX * 5), 76 + (15 * oY) + (oY));
		invertRegion(10 + (10 * nX) + (nX * 5), 60 + (15 * nY) + (nY), 22 + (10 * nX) + (nX * 5), 76 + (15 * nY) + (nY));
	}
	
	//Draw keyboard keys as defined in a 2xString array
	private static void drawKeyboard(String[][] keys)
	{
		GraphicsLCD lcd = BrickFinder.getDefault().getGraphicsLCD();
		int w = lcd.getWidth();
		int h = lcd.getHeight();
		
		lcd.drawLine(1, 50, w, 50);
		for(int x = 0; x < keys.length; x++)
		{
			for(int y = 0; y < keys[x].length; y++)
			{
				displayMessage.drawStringAtPos((y *  1.5) + 1, (x * 1.05) + 4, keys[x][y]);
			}
		}
	}
	
	//Invert a region (x1, y1) to (x2, y2)
	private static void invertRegion(double x1, double y1, double x2, double y2)
	{
		GraphicsLCD lcd = BrickFinder.getDefault().getGraphicsLCD();
		int w = lcd.getWidth();
		int h = lcd.getHeight();
		for(double x = x1; x < x2; x++)
		{
			for(double y = y1; y < y2; y++)
			{
				if(lcd.getPixel((int) x,(int) y) == 0)
				{
					lcd.setPixel((int) x,(int) y, 1);
				}
				else if(lcd.getPixel((int) x,(int) y) == 1)
				{
					lcd.setPixel((int) x,(int) y, 0);
				}
			}
		}
	}
}
