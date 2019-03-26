package tests.GTFS_Project;

import java.util.Date;
import java.util.ArrayList;

import GTFS_Project.GUI.Controller;
import GTFS_Project.Subject.Routes;
import GTFS_Project.Subject.StopTimes;
import GTFS_Project.Subject.Stops;
import GTFS_Project.Subject.Trips;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.File;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test Class for the StopTimes Class
 *
 * @author Max Beihoff
 * @version 1.0
 * 10/17/2017
 */
class StopTimesTest{
    private Routes routes;
    private Trips trips;
    private StopTimes stopTimes;
    private Stops stops;
    private Routes routesTest;
    private File validFile, invalidFile, nullFile;

    @BeforeEach
    void setUp() throws Exception{
    	routes = new Routes(new File("test_routes.txt"));
        trips = new Trips(new File("test_trips.txt"));
        stops = new Stops(new File("test_stops.txt"));
        validFile = new File("stop_times.txt");
        invalidFile = new File("test_stops.txt");
        nullFile = null;
    }

    @AfterEach
    void tearDown(){
        validFile = null;
        invalidFile = null;

    }
    @Test
    void importValidFile() {
        try{
            stopTimes = new StopTimes();
            assertTrue(stopTimes.importFile(validFile));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

//    @Test
//    void importInvalidFile(){
//        assertThrows(Exception.class, new Executable() {
//            @Override
//            public void execute() throws Throwable {
//                try {
//                    stopTimes = new StopTimes();
//                    stopTimes.importFile(invalidFile);
//                }catch (Exception e){
//                    throw e;
//                }
//            }
//        });
//
//    }

    @Test
    void importNullFile(){
        assertThrows(NullPointerException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                try {
                    stopTimes = new StopTimes();
                    stopTimes.importFile(nullFile);
                }catch (Exception e){
                    throw e;
                }
            }
        });
    }
    
    @Test
    public void getNextTripByTripIDs() {
    	try {
    		stopTimes = new StopTimes();
    		stopTimes.importFile(new File("test_stop_times.txt"));
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    	ArrayList<String> tripIDs = new ArrayList<>();
    	tripIDs.add("1116B94971_R01_WKDY");
    	tripIDs.add("20A546C35C_R01_SAT");
    	
        String tripID = stopTimes.getNextTripByTripIDsFromTime(tripIDs, "10:48:59");
        assertEquals("Next trip in the morning.", tripID);
    	tripID = stopTimes.getNextTripByTripIDsFromTime(tripIDs, "7:17:01");
    	assertEquals("1116B94971_R01_WKDY", tripID);
    	tripID = stopTimes.getNextTripByTripIDsFromTime(tripIDs, "8:30:01");
    	assertEquals("20A546C35C_R01_SAT", tripID);
    }
    
    @Test
    public void getNextRouteByTripId()throws Exception {
    	try {
    		stopTimes = new StopTimes();
    		stopTimes.importFile(validFile);
    	}catch(Exception e) {
    		e.printStackTrace(); 
    	}
    	assertEquals("No Next Trip", stopTimes.getNextTripByStopID(" "));
		assertEquals("Next trip in the morning", stopTimes.getNextTripByStopID("1"));
		assertEquals("Rt1Trp25", stopTimes.getNextTripByStopID("62").toString());
    }
    
    @Test
    void addStopTime() {
    }
    @Test
    void removeStopTime() {
    }

    @Test
    void addObserver() {
    }

    @Test
    void removeObserver() {
    }

    @Test
    void notifyObservers() {
    }

    @Test
    void export1() {
    }
}
