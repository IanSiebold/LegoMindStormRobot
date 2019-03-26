package EV3Files.threads;

import java.util.ArrayList;

import EV3Files.MovementManager;
import EV3Files.Main;
import EV3Files.StopSimulator;
import GTFS_Project.Subject.Stop;
import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Delay;

public class StopSeriesThread extends Thread{	
	@Override
	public void run()
    {
        try
        {
        	if(!MovementManager.isTrafficRunning)
        	{
	        	TrafficThread traffic = new TrafficThread();
	        	traffic.start();        	
	        	MovementManager.isTrafficRunning = true;
        	}
        	
        	DifferentialPilot pilot = new DifferentialPilot(5.0, 25, Motor.B, Motor.A);
			MovementManager.status = "Active";
			MovementManager.curTrip = "Custom Trip";
			for(int x = 0; x < StopSimulator.choices.size(); x++)
			{
				MovementManager.status = "Moving";
				if((x + 1) < StopSimulator.choices.size())
				{
					MovementManager.nextStop = StopSimulator.choices.get(x + 1).getName();
					MovementManager.currentStop = StopSimulator.choices.get(x);
					double dist = Main.findDistance(StopSimulator.choices.get(x).getLatitude(), StopSimulator.choices.get(x).getLongitude(),
							StopSimulator.choices.get(x + 1).getLatitude(), StopSimulator.choices.get(x + 1).getLongitude());
					double angle = Main.angleFromCoordinate(StopSimulator.choices.get(x).getLatitude(), StopSimulator.choices.get(x).getLongitude(),
							StopSimulator.choices.get(x + 1).getLatitude(), StopSimulator.choices.get(x + 1).getLongitude());
					pilot.rotate(angle);
					pilot.travel(dist * MovementManager.scale);
				}
				else if((x + 1) >= StopSimulator.choices.size())
				{
					MovementManager.nextStop = "End of Queue            ";
					MovementManager.statusDetail = MovementManager.STATUS_EXPORT_QUEUE;
					break;
				}

				MovementManager.status = "Stopped";
				MovementManager.statusDetail = MovementManager.STATUS_STOPPED;
				while(MovementManager.statusDetail == MovementManager.STATUS_STOPPED);
				Delay.msDelay(1000);
			}
			MovementManager.status = "Inactive";
			MovementManager.statusDetail = MovementManager.STATUS_INACTIVE;
			MovementManager.nextStop = "                       ";
			MovementManager.curTrip = "                       ";
        	return;
        }
        catch (Exception e)
        {
            System.out.println ("Error in thread StopSeriesThread: " + this.getId());
            return;
        }
    }
}
