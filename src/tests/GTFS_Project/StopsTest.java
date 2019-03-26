/**
 * SE2030 -021
 * Fall 2017
 * GTFS Project: StopsTest
 * Copyright [2017] - [2022] Max Beihoff, Chris Betances, Eduardo Avilles
 * All Rights Reserved
 * 05 Oct 2017
 */

package tests.GTFS_Project;

import GTFS_Project.Observer.StopsObserver;
import GTFS_Project.Subject.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Class for the Stops Class
 *
 * @author Chris Betances
 * @version 1.0
 * 10/17/2017
 */
class StopsTest {
    private Stops stops;
    private Routes routes;
    private Trips trips;
    private StopTimes stopTimes;
    private File validFile, nullFile, invalidFile;

    @BeforeEach
    void setUp() throws Exception {
        stops = new Stops(new File("test_stops.txt"));
        routes = new Routes(new File("test_routes.txt"));
        trips = new Trips(new File("test_trips.txt"));
        stopTimes = new StopTimes(new File("test_stop_times.txt"));
        stops.setTrips(trips);
        stops.setStopTimes(stopTimes);
        stops.setRoutes(routes);
        validFile = new File("test_stops.txt");
        invalidFile = new File("test_routes.txt");
        nullFile = null;
    }

    @AfterEach
    void tearDown() {
        stops = null;
        validFile = null;
        invalidFile = null;
    }


    @Test
    void importValidFile() {
        try {
            assertTrue(stops.importFile(validFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void importInvalidFile() {
        assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                try {
                    stops.importFile(invalidFile);
                } catch (Exception e) {
                    throw e;
                }
            }
        });
    }

    @Test
    void importNullFile() {
        assertThrows(NullPointerException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                try {
                    stops.importFile(nullFile);
                } catch (Exception e) {
                    throw e;
                }
            }
        });
    }

    @Test
    void addStop() {
        try {
            stops.addStop(new Stop());
            assertEquals(24, stops.getStopsSize());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void removeStop() {
        try {
            stops.removeStop(stops.getStop(2));
            assertEquals(22, stops.getStopsSize());
            stops.removeStop(stops.getStop(2));
            assertEquals(21, stops.getStopsSize());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    void getStop() {
        Stop testStop = new Stop("7CCA4EE43B", "\"Vine and Delong Middle School Driveway\"",
                44.819230, -91.535290);
        List<String> testList = new ArrayList<>();
        testList.add("7CCA4EE43B");
        assertEquals(testStop.getStopID(), stops.getStop(4).getStopID());
        assertEquals(testStop.getStopID(), stops.getStop("7CCA4EE43B").getStopID());
        assertEquals(testStop.getStopID(), stops.getStop(testList).get(0).getStopID());
    }
    
    @Test
    void searchStopEV3() {
    	List<String> routesOnStop = stops.searchStopEV3("3F56F87E65", stopTimes, trips);
    	assertEquals(routesOnStop.get(0), "1116B94971");
    	assertEquals(routesOnStop.get(1), "20A546C35C");
    }
    
    @Test
    void getStops() {
    	List<Stop> testStops = new ArrayList<Stop>();
    	testStops.add(new Stop("3F56F87E65", "Transfer Center", 44.810060, -91.497640));
    	testStops.add(new Stop("9F0E64DAB2", "Walmart Supercenter", 44.775970, -91.429180));
    	testStops.add(new Stop("5A52D3DAB2", "Gateway and S.Hastings Service Rd", 44.780780, -91.434250));
    	testStops.add(new Stop("63FEA496C1", "Festival Foods lot N", 44.789320, -91.459730));
    	testStops.add(new Stop("7CCA4EE43B", "Vine and Delong Middle School Driveway", 44.819230, -91.535290));
    	testStops.add(new Stop("E31390AC08", "Starr and Connell's 2 entrance", 44.862330, -91.482250));
    	testStops.add(new Stop("461589DB2C", "Margaret and Main", 44.812270, -91.478650));
    	testStops.add(new Stop("AC729B9CEE", "Margaret and Altoona", 44.809160, -91.478650));
    	testStops.add(new Stop("FF5192DA85", "Margaret and Highland", 44.807340, -91.478640));
    	testStops.add(new Stop("DAE51C9EA6", "Margaret and Fenwick", 44.803720, -91.478630));
    	testStops.add(new Stop("908738A94F", "Rudolph and Benton", 44.796760, -91.478560));
    	testStops.add(new Stop("0BD3731D52", "Fairfax and Skeels", 44.789498, -91.468369));
    	testStops.add(new Stop("548ECD3057", "Festival Foods lot S", 44.787510, -91.459750));
    	testStops.add(new Stop("942DAC8E65", "Mall Dr and E. Hamilton", 44.786860, -91.461250));
    	testStops.add(new Stop("65A8CA15C1", "E. Hamilton and Family Dollar", 44.785550, -91.458170));
    	testStops.add(new Stop("A10ECC4BD2", "E. Hamilton and Miller", 44.785520, -91.455020));
    	testStops.add(new Stop("5D6D58DB42", "Barstow and Eau Claire", 44.812180, -91.500540));
    	testStops.add(new Stop("BFD4717811", "Barstow and Galloway", 44.814540, -91.501250));
    	testStops.add(new Stop("DC301BC54C", "Galloway and N Dewey", 44.814740, -91.499300));
    	testStops.add(new Stop("C43B92367A", "Putnam and Wisconsin", 44.815760, -91.495290));
    	testStops.add(new Stop("F98DB5D7AC", "Madison and Bellevue", 44.817630, -91.492670));
    	testStops.add(new Stop("455590F2B4", "Madison and McDonough", 44.817770, -91.489070));
    	testStops.add(new Stop("B1DFE3AF9C", "Ball and CDC Parking Lot", 44.816000, -91.485850));
    	for (int i = 0; i < testStops.size(); i++) {
    		assertEquals(testStops.get(i).getStopID(), stops.getStops().get(i).getStopID());
    	}
    }

    @Test
    void addObserver() throws Exception {
        assertTrue(stops.addObserver(new StopsObserver(stops)));
    }

    @Test
    void removeObserver() throws Exception {
        stops.addObserver(new StopsObserver());
        stops.addObserver(new StopsObserver(stops));
        assertTrue(stops.removeObserver(new StopsObserver(stops)));
        assertFalse(stops.removeObserver(null));
    }

    @Test
    void searchStop() {
        List<Route> foundStops = stops.searchStop("3F56F87E65");
        assertEquals(2, foundStops.size());
    }

    @Test
    void verifyGoodStop(){
        assertTrue(stops.verifyStop("3F56F87E65"));
    }

    @Test
    void verifyBadStop(){
        assertFalse(stops.verifyStop(""));
    }

    @Test
    void export() throws Exception {
        String testFile = stops.export();
        PrintWriter out = new PrintWriter("testStopsExport.txt");
        out.print(testFile);
        out.close();
        Stops testStops = new Stops();
        assertTrue(testStops.importFile(new File("testStopsExport.txt")));
    }
}