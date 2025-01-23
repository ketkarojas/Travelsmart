import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.example.Flight;
import org.example.FlightService;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class FlightServiceTest {

    private FlightService flightService;

    @SuppressWarnings("deprecation")
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        flightService = FlightService.getInstance();
        flightService.clearFlights(); // Reset the singleton instance
    }

    @Test
    public void testLoadFlights() throws IOException {
        String fileName = "test_flights.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("FlightNumber,SourceCity,DestinationCity,Fare,Duration\n");
            writer.write("AI101,Delhi,Mumbai,5000,2\n");
            writer.write("AI102,Delhi,Mumbai,3000,3\n");
        }

        flightService.loadFlights(fileName);
        List<Flight> flights = flightService.searchFlights("Delhi", "Mumbai");
        assertEquals(2, flights.size());
    }

    @Test
    public void testSearchFlights() {
        Flight flight1 = new Flight("AI101", "Delhi", "Mumbai", 5000, 2);
        Flight flight2 = new Flight("AI102", "Delhi", "Mumbai", 3000, 3);
        flightService.loadFlights("test_flights.csv");
        List<Flight> flights = Arrays.asList(flight1, flight2);
        flightService.sortFlights(flights, Comparator.comparingInt(Flight::getFare));
        assertEquals(flight2, flights.get(0));
    }

    @Test
    public void testSearchFlightsAsync() throws ExecutionException, InterruptedException {
        Flight flight1 = new Flight("AI101", "Delhi", "Mumbai", 5000, 2);
        Flight flight2 = new Flight("AI102", "Delhi", "Mumbai", 3000, 3);
        flightService.loadFlights("test_flights.csv");
        Future<List<Flight>> futureFlights = flightService.searchFlightsAsync("Delhi", "Mumbai");
        List<Flight> flights = futureFlights.get();
        assertEquals(2, flights.size());
    }

    @Test
    public void testSortFlights() {
        Flight flight1 = new Flight("AI101", "Delhi", "Mumbai", 5000, 2);
        Flight flight2 = new Flight("AI102", "Delhi", "Mumbai", 3000, 3);
        List<Flight> flights = Arrays.asList(flight1, flight2);
        flightService.sortFlights(flights, Comparator.comparingInt(Flight::getFare));
        assertEquals(flight2, flights.get(0));
    }
}