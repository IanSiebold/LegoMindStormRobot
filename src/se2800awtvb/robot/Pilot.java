/*
 * PilotTest.java 
 * Demo application for a two-track EV3 robot
 * This application causes the robot to move forward until it reaches an obstacle
 * then it reverses the same amount and turns.
 * Written by Derek Riley
 */
package se2800awtvb.robot;

import EV3Files.displayMessage;
import lejos.hardware.lcd.Font;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;

public class Pilot {
	  DifferentialPilot pilot;
	  EV3UltrasonicSensor us;
	  SampleProvider sp;
	  RegulatedMotor leftMotor;
	  RegulatedMotor rightMotor;
	  float[] sample;
	  
	  /*
	   * This method initializes instance variables for the robot
	   */
	  public void initialize() {
		  us = new EV3UltrasonicSensor(SensorPort.S4);
		  sp = us.getDistanceMode();
		  sample = new float[1];
	  }
	 
	  /*
	   * This method tells the robot to go forward until it reaches an obstacle.
	   * Upon reaching an obstacle, it reverses the amount it went forward, 
	   * turns 90 degrees moves and turns back.  
	   */
	  public void go() {
        pilot.setLinearAcceleration(4000);
		pilot.setLinearSpeed(20); //straight speed cm/sec
		pilot.setAngularSpeed(50); //rotation/turning deg/sec
		
		System.out.println("Going forwards");
	    pilot.forward();;
	    while (pilot.isMoving()) {
	      sp.fetchSample(sample, 0);
	      System.out.println(sample[0]);
	      if (sample[0] < 0.1) {
	    	  pilot.stop();
	      }
	    }
	    float dist = pilot.getMovement().getDistanceTraveled();
	    System.out.println("Distance = " + dist);
	    pilot.travel(-dist,false);
	    pilot.rotate(90,false);
	    pilot.travel(10,false);
	    pilot.rotate(-90,false);
	  }
	  
	  /**
	   * This method sets the linear speed of the robot in cm per second
	   * @param speed represents the speed from 1 to 100 as a ratio where
	   * 0 is stopped and 100 is the max speed of the robot.
	   */
	  public void setLinearSpeed(int speed) {
		  if (speed < 1 || speed > 100) {
			  displayMessage.displayCenterMessage("The speed must be between 1 and 100", Font.getSmallFont());
		  } else {
			  pilot.setLinearSpeed(((1.0 * speed) / 100) * pilot.getMaxLinearSpeed());
		  }
	  }
	 
	  /*
	   * Main method
	   */
	  public static void main(String[] args) {
		  Pilot traveler = new Pilot();
		  float wheelDiameter = 5.0f;
		  float trackWidth = 25.0f;
		  traveler.leftMotor = Motor.B;
		  traveler.rightMotor = Motor.C;
    	
		  traveler.pilot = new DifferentialPilot(wheelDiameter,trackWidth,traveler.leftMotor,traveler.rightMotor);
		  traveler.initialize();
		  traveler.go();
	}
}
