package EV3Files.threads;

import java.io.File;
import java.util.Date;

import EV3Files.MovementManager;
import lejos.hardware.Sound;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class TrafficThread extends Thread{	
	@Override
	public void run()
    {
		Date stopStart = null;
		boolean isStopped = false;
		float oA = 0, oB = 0;
        try
        {
        	EV3UltrasonicSensor us = new EV3UltrasonicSensor(SensorPort.S4);
        	us.enable();
        	SampleProvider sp = us.getDistanceMode();
        	float[] distanceRead = new float[1];
        	while(MovementManager.statusDetail != MovementManager.STATUS_END_TRAFFIC)
        	{
        		if(MovementManager.statusDetail != MovementManager.STATUS_INACTIVE)
        		{
	        		sp.fetchSample(distanceRead, 0);
	        		if(distanceRead[0] <= 0.1)
	        		{
	        			if(!isStopped)
	        			{
	        				oA = Motor.A.getSpeed();
	        				oB = Motor.B.getSpeed();
		        			stopStart = new Date();
		        			isStopped = true;
		        			Motor.A.setSpeed(0.01F);;
		        			Motor.B.setSpeed(0.01F);
		        			Sound.playSample(new File("./honk_honk_x.wav"), 100);
	        			}
	        		}
	        		else
	        		{
	            		if(isStopped)
	            		{
	            			isStopped = false;
	            			Date stop = new Date();
	            			long time = (stop.getTime() - stopStart.getTime())/1000;
	            			while(time >= 60)
	            			{
	            				if(time >= 3600)
	            				{
	            					time -= 3600;
	            					MovementManager.delayH++;
	            				}
	            				else if(time >= 60)
	            				{
	            					time -= 60;
	            					MovementManager.delayM++;
	            				}
	            			}
	            			MovementManager.delayS = (int) time;
	            			Motor.A.setSpeed(oA);
	            			Motor.B.setSpeed(oB);
	            		}
	        		}
        		}
        	}
        	us.disable();
        	MovementManager.isTrafficRunning = false;
        	return;
        }
        catch (Exception e)
        {
            System.out.println ("Error in thread TrafficThread: " + this.getId() + e.toString());
            return;
        }
    }
}
