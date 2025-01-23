import org.example.Flight;//imports the flight class from org.example package
import org.junit.Test;//it imports the test annotation from junit which marks a method as a test case which junit should run
import static org.junit.Assert.assertEquals;//imports assertequals from the assert class of junit and its used to assert that two values are equal else test will fail

import java.util.Arrays;
import java.util.List;

public class FlightTest {

    @Test
    public void testCompareByFare() {
        Flight flight1 = new Flight("Airline A", "New York", "London", 500, 7);
        Flight flight2 = new Flight("Airline B", "New York", "Paris", 400, 8);
        Flight flight3 = new Flight("Airline C", "New York", "Tokyo", 700, 10);

        List<Flight> flights = Arrays.asList(flight1, flight2, flight3);
        flights.sort(Flight.compareByFare());

        assertEquals(400, flights.get(0).getFare()); // Airline B should be first by fare
        assertEquals(500, flights.get(1).getFare()); // Airline A should be second
        assertEquals(700, flights.get(2).getFare()); // Airline C should be last
    }

    @Test
    public void testCompareByDuration() {
        Flight flight1 = new Flight("Airline A", "New York", "London", 500, 7);
        Flight flight2 = new Flight("Airline B", "New York", "Paris", 400, 8);
        Flight flight3 = new Flight("Airline C", "New York", "Tokyo", 700, 6);

        List<Flight> flights = Arrays.asList(flight1, flight2, flight3);
        flights.sort(Flight.compareByDuration());

        assertEquals(6, flights.get(0).getDuration()); // Airline C should be first by duration
        assertEquals(7, flights.get(1).getDuration()); // Airline A should be second
        assertEquals(8, flights.get(2).getDuration()); // Airline B should be last
    }

    @Test
    public void testCompareByFareAndDuration() {
        Flight flight1 = new Flight("Airline A", "New York", "London", 500, 7);
        Flight flight2 = new Flight("Airline B", "New York", "Paris", 500, 8);
        Flight flight3 = new Flight("Airline C", "New York", "Tokyo", 500, 6);

        List<Flight> flights = Arrays.asList(flight1, flight2, flight3);
        flights.sort(Flight.compareByFareAndDuration());

        // All flights have the same fare, so it will sort based on duration
        assertEquals(6, flights.get(0).getDuration()); // Airline C should be first by duration
        assertEquals(7, flights.get(1).getDuration()); // Airline A should be second
        assertEquals(8, flights.get(2).getDuration()); // Airline B should be last
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFlightWithNegativeFare() {
        // Attempt to create a flight with a negative fare
        Flight.createFlight("AI101", "Delhi", "Mumbai", -5000, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFlightWithNegativeDuration() {
        // Attempt to create a flight with a negative duration
        Flight.createFlight("AI101", "Delhi", "Mumbai", 5000, -2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFlightWithNegativeFareAndDuration() {
        // Attempt to create a flight with both negative fare and duration
        Flight.createFlight("AI101", "Delhi", "Mumbai", -5000, -2);
    }
}
