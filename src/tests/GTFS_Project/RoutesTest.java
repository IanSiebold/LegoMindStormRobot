/**
 * SE2030 -021
 * Fall 2017
 * GTFS Project: RoutesTest
 * Copyright [2017] - [2022] Max Beihoff, Chris Betances, Eduardo Avilles
 * All Rights Reserved
 * 05 Oct 2017
 */

package tests.GTFS_Project;

import GTFS_Project.GUI.Controller;
import GTFS_Project.Observer.RoutesObserver;
import GTFS_Project.Subject.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import EV3Files.Main;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Class for the Routes Class
 *
 * @author Chris Betances
 * @version 1.0
 * 10/17/2017
 */
class RoutesTest {

    private Controller controller;
    private Routes routes;
    private Trips trips;
    private StopTimes stopTimes;
    private Stops stops;
    private Routes routesTest;
    private File validFile, invalidFile, nullFile;

    @BeforeEach
    void setUp() throws Exception {

        routes = new Routes(new File("test_routes.txt"));
        trips = new Trips(new File("test_trips.txt"));
        stopTimes = new StopTimes(new File("test_Stop_Times.txt"));
        stops = new Stops(new File("test_stops.txt"));
        routes.setStops(stops);
        routes.setTrips(trips);
        routes.setStopTimes(stopTimes);
        validFile = new File("test_routes.txt");
        invalidFile = new File("test_stops.txt");
        nullFile = null;
    }

    @AfterEach
    void tearDown(){
    routes = null;
    trips = null;
    stopTimes = null;
    stops = null;
    validFile = null;
    invalidFile = null;
    }

    @Test
    void importFile() {
        assertEquals(9, routes.getRouteSize());
    }

    @Test
    void importValidFile() {
        try{
            routesTest = new Routes();
            assertTrue(routesTest.importFile(validFile));
        }catch (Exception e){
            e.printStackTrace();
        }
        assertEquals(9, routesTest.getRouteSize());
    }

    @Test
    void importInvalidFile(){
        assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                try{
                routesTest =  new Routes();
                routesTest.importFile(invalidFile);
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
                    routesTest = new Routes();
                    routesTest.importFile(nullFile);
                }catch (Exception e){
                    throw e;
                }
            }
        });
    }

    @Test
    void ianTest() {
    	ArrayList<StopTime> testTimes = new ArrayList<StopTime>();
        ArrayList<Stop> testStops = new ArrayList<Stop>();
        String tripID = "1116B94971_R01_WKDY";
        for (StopTime stopTime : stopTimes.getStopTimes()) {
            if (stopTime.getTripId().equals(tripID)) {
            	System.out.println(stopTime.getStopId());
                testTimes.add(stopTime);
            }
        }
        for (StopTime stopTime : testTimes) {
            for (Stop stop : stops.getStops()) {
                if (stopTime.getStopId().equals(stop.getStopID())) {
                    testStops.add(stop);
                }
            }
        }
    }
    
    @Test
    void getRoute() {
        Route testRoute = new Route("D837EE7A8", "005BAB");
        List<String> testList = new ArrayList<>();
        testList.add("D837EE7A8");
        assertEquals(testRoute.getRouteID(), routes.getRoute(1).getRouteID());
        assertEquals(testRoute.getRouteID(), routes.getRoute("D837EE7A8").getRouteID());
        assertEquals(testRoute.getRouteID(), routes.getRoute(testList).get(0).getRouteID());
    }
    
    @Test
    void getRoutes() {
    	List<Route> testRoutes = new ArrayList<Route>();
    	testRoutes.add(new Route("2F52F87E6A", "008345"));
    	testRoutes.add(new Route("D837EE7A8", "005BAB"));
    	testRoutes.add(new Route("A7C1CB80", "74392D"));
    	testRoutes.add(new Route("EC19586170", "005BAB"));
    	testRoutes.add(new Route("C242B87D82", "F5821F"));
    	testRoutes.add(new Route("5E81AD26C8", "6D2383"));
    	testRoutes.add(new Route("619748505B", "61BB46"));
    	testRoutes.add(new Route("1116B94971", "000000"));
    	testRoutes.add(new Route("20A546C35C", "00ADEF"));
    	for (int i = 0; i < testRoutes.size(); i++) {
    		assertEquals(testRoutes.get(i).getRouteID(), routes.getRoutes().get(i).getRouteID());
    	}
    }

    @Test
    void addRoute() {
        routes.addRoute(new Route());
        assertEquals(10, routes.getRouteSize());
    }

    @Test
    void removeRoute() {
        routes.removeRoute(routes.getRoute(3));
        assertEquals(8, routes.getRouteSize());
        routes.removeRoute(routes.getRoute(3));
        assertEquals(7, routes.getRouteSize());
    }

    @Test
    void searchRoute() {
        List<Stop> foundStops = routes.searchRoute("1116B94971");
        assertEquals(8, foundStops.size());
    }

    @Test
    void addObserver(){
      try{
          assertTrue(routes.addObserver(new RoutesObserver(routes)));
      }catch(Exception e){
          e.printStackTrace();
      }
    }

    @Test
    void removeObserver() {
        try {
            routes.addObserver(new RoutesObserver());
            routes.addObserver(new RoutesObserver(routes));
            assertTrue(routes.removeObserver(new RoutesObserver(routes)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void verifyGoodRoute() {
        assertTrue(routes.verifyRoute("1116B94971"));
    }

    @Test
    void verifyBadRoute() {
        assertFalse(routes.verifyRoute(""));
    }

    @Test
    void export() throws Exception {
        String testFile = routes.export();
        PrintWriter out = new PrintWriter("testRoutesExport.txt");
        out.print(testFile);
        out.close();
        Routes testRoutes = new Routes();
        assertTrue(testRoutes.importFile(new File("testRoutesExport.txt")));
    }
}