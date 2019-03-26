/**
 * SE2030 -021
 * Fall 2017
 * GTFS Project: MapObserver
 * Copyright [2017] - [2022] Max Beihoff, Chris Betances, Eduardo Avilles
 * All Rights Reserved
 * 05 Oct 2017
 */

package tests.GTFS_Project;

import java.util.ArrayList;
import java.util.List;
import GTFS_Project.Observer.TripsObserver;
import GTFS_Project.Subject.Trip;
import GTFS_Project.Subject.Trips;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.File;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Class for the Trips Class
 *
 * @author Eduardo Avilles
 * @version 1.0
 * 10/17/2017
 */
public class TripsTest {

    private Trips trips;
    private Trips tripsTest;
    private File validFile, invalidFile, nullFile;

    @BeforeEach
    public void setUp() throws Exception {
        trips = new Trips(new File("test_trips.txt"));
        validFile = new File("test_trips.txt");
        invalidFile = new File("txt");
        nullFile = null;
    }

    @AfterEach
    public void tearDown(){
        trips = null;
        validFile = null;
        invalidFile = null;
        tripsTest = null;
    }

    @Test
    void importValidFile() {
        try{
            tripsTest = new Trips();
            assertTrue(tripsTest.importFile(validFile));
        }catch (Exception e){
            e.printStackTrace();
        }
        assertEquals(13, tripsTest.getTripsSize());
    }

    @Test
    void importInvalidFile(){
        assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                try{
                    tripsTest =  new Trips();
                    tripsTest.importFile(invalidFile);
                }catch (Exception e){
                    throw e;
                }
            }
        });
    }

    @Test
    void importNullFile(){
        assertThrows(NullPointerException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                try{
                    tripsTest = new Trips();
                    tripsTest.importFile(nullFile);
                }catch (Exception e){
                    throw e;
                }
            }
        });

    }
    
    @Test
    void getTrips() {
    	List<Trip> testTrips = new ArrayList<Trip>();
    	testTrips.add(new Trip("1116B94971", "1116B94971_R01_WKDY"));
    	testTrips.add(new Trip("20A546C35C", "20A546C35C_R01_SAT"));
    	testTrips.add(new Trip("20A546C35C", "20A546C35C_R01_WKDY"));
    	testTrips.add(new Trip("20A546C35C", "20A546C35C_R02_SAT"));
    	testTrips.add(new Trip("20A546C35C", "20A546C35C_R02_WKDY"));
    	testTrips.add(new Trip("20A546C35C", "20A546C35C_R03_SAT"));
    	testTrips.add(new Trip("20A546C35C", "20A546C35C_R03_WKDY"));
    	testTrips.add(new Trip("20A546C35C", "20A546C35C_R04_SAT"));
    	testTrips.add(new Trip("20A546C35C", "20A546C35C_R04_WKDY"));
    	testTrips.add(new Trip("20A546C35C", "20A546C35C_R05_SAT"));
    	testTrips.add(new Trip("20A546C35C", "20A546C35C_R05_WKDY"));
    	testTrips.add(new Trip("20A546C35C", "20A546C35C_R06_SAT"));
    	testTrips.add(new Trip("20A546C35C", "20A546C35C_R06_WKDY"));
    	for (int i = 0; i < testTrips.size(); i++) {
    		assertEquals(testTrips.get(i).getTripID(), trips.getTrip(i).getTripID());
    		assertEquals(testTrips.get(i).getRouteID(), trips.getTrip(i).getRouteID());
    	}
    }

    @Test
    void addTrip() {
        trips.addTrip(new Trip());
        assertEquals(14, trips.getTripsSize());
    }

    @Test
    void removeTrip() {
        trips.removeTrip(trips.getTrip(1));
        assertEquals(12, trips.getTripsSize());
        trips.removeTrip(trips.getTrip(3));
        assertEquals(11, trips.getTripsSize());
    }

    @Test
    void addObserver() {
        try {
            assertTrue(trips.addObserver(new TripsObserver(trips)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void removeObserver(){
      try{  trips.addObserver(new TripsObserver());
        trips.addObserver(new TripsObserver(trips));
        assertTrue(trips.removeObserver(new TripsObserver(trips)));
    }catch(Exception e){
          e.printStackTrace();
      }
    }

    @Test
    void export() throws Exception {
        String testFile = trips.export();
        PrintWriter out = new PrintWriter("testTripsExport.txt");
        out.print(testFile);
        out.close();
        Trips testTrips = new Trips();
        assertTrue(testTrips.importFile(new File("testRoutesExport.txt")));
    }
}