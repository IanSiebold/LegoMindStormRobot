package EV3Files;

import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.LCD;
import lejos.hardware.lcd.TextLCD;
import lejos.utility.Delay;


/**
 * This class was given to us by another group from the other class. Its functionality is to print a string either centered on the screen or
 * with a scroll box
 *
 */
public class displayMessage {
	@Deprecated
	public static void displayCenter(String message) {
		GraphicsLCD graphic = BrickFinder.getDefault().getGraphicsLCD();
		graphic.clear();
		graphic.refresh();
		final int width = graphic.getWidth();
		final int height = graphic.getHeight();
		graphic.drawString(message, width / 2, height / 2, GraphicsLCD.BASELINE | GraphicsLCD.HCENTER);
	}
	 
	public static void displayCenterMessage(String msg, Font font)
	{
		TextLCD lcd = BrickFinder.getLocal().getTextLCD(font);
		lcd.clear();
		lcd.drawString(msg, (lcd.getTextWidth()  - msg.length()) / 2, (lcd.getTextHeight() / 2) - 1);
	}
	
	public static void promptEditString(String title, String originalString)
	{
		GraphicsLCD lcd = BrickFinder.getLocal().getGraphicsLCD();
		lcd.clear();
		lcd.refresh();
		int w = lcd.getWidth();
		int h = lcd.getHeight();
		int stingLength = originalString.length();
		
		lcd.drawRect(0, 0, w - 1, h - 1);
		drawStringAtPos(0, 0, title);
		drawStringAtPos(0, 1, "Orig:" + originalString);
		drawStringAtPos(0, 2, "New:");
		Keyboard.runKeyboard(4, 2);
	}
	
	public static void drawStringAtPos(double x, double y, String string)
	{
		BrickFinder.getLocal().getGraphicsLCD().drawString(string, (int) (1 + (x * 10)), (int) (1 + (y * 15)), 4 | 16);
	}
	
	public static void displayScrollableMessage(String[] messageLines, Font fontSize) {
		TextLCD lcd = BrickFinder.getLocal().getTextLCD(fontSize);
		lcd.clear();
		
		int currentLine = 0;
		updateScroll(currentLine, lcd, messageLines);
		while(true)
		{			
			if(BrickFinder.getLocal().getKeys().getButtons() == Keys.ID_DOWN){
				if((currentLine + lcd.getTextHeight() - 2) < messageLines.length)
				{
					currentLine++;
					updateScroll(currentLine, lcd, messageLines);
				}
				Delay.msDelay(250);
			}
			else if(BrickFinder.getLocal().getKeys().getButtons() == Keys.ID_UP){
				if((currentLine - 1) >= 0)
				{
					currentLine--;
					updateScroll(currentLine, lcd, messageLines);
				}
				Delay.msDelay(250);
			}
			else if(BrickFinder.getLocal().getKeys().getButtons() == Keys.ID_ESCAPE){
				break;
			}
		}
	}
	
	private static void updateScroll(int currentLine, TextLCD lcd, String[] messageLines)
	{
		lcd.clear(); 
		lcd.drawString("Lines " + currentLine + " - " + (currentLine + lcd.getTextHeight() - 2) + " / " + messageLines.length, 0, 0);
		for(int x = 0; x < (lcd.getTextHeight() - 2); x++)
		{
			if((currentLine + x) <= (messageLines.length - 1))
			{
				lcd.drawString(messageLines[currentLine + x], 0, x + 1);
			}
			else
			{
				lcd.drawString("-", 0, x + 1);
			}
		}
		if((currentLine + lcd.getTextHeight() - 2) < messageLines.length)
		{
			lcd.drawString("\\/", (lcd.getTextWidth() - 2), lcd.getTextHeight() - 1);
		}
		if(currentLine > 0)
		{
			lcd.drawString("/\\", (lcd.getTextWidth() - 2), 0);
		}
	}
}
