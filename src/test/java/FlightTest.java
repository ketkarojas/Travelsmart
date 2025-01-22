import org.example.Flight;
import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.*;

public class FlightTest {

    @Test
    public void testFlightConstructorAndGetters() {
        Flight flight = new Flight("AI101", "Delhi", "Mumbai", 5000, 2);

        assertEquals("AI101", flight.getFlightNumber());
        assertEquals("Delhi", flight.getSourceCity());
        assertEquals("Mumbai", flight.getDestinationCity());
        assertEquals(5000, flight.getFare());
        assertEquals(2, flight.getDuration());
    }

    @Test
    public void testCompareByFare() {
        Flight flight1 = new Flight("AI101", "Delhi", "Mumbai", 5000, 2);
        Flight flight2 = new Flight("AI102", "Delhi", "Mumbai", 3000, 3);

        Comparator<Flight> comparator = Flight.compareByFare();

        assertTrue(comparator.compare(flight1, flight2) > 0); // flight1 has higher fare
    }

    @Test
    public void testCompareByDuration() {
        Flight flight1 = new Flight("AI101", "Delhi", "Mumbai", 5000, 2);
        Flight flight2 = new Flight("AI102", "Delhi", "Mumbai", 3000, 3);

        Comparator<Flight> comparator = Flight.compareByDuration();

        assertTrue(comparator.compare(flight1, flight2) < 0); // flight1 has lesser duration
    }

    @Test
    public void testCompareByFareAndDuration() {
        Flight flight1 = new Flight("AI101", "Delhi", "Mumbai", 5000, 3);
        Flight flight2 = new Flight("AI102", "Delhi", "Mumbai", 5000, 2);

        Comparator<Flight> comparator = Flight.compareByFareAndDuration();

        assertTrue(comparator.compare(flight1, flight2) > 0); // flight1 has longer duration but same fare
    }
}

