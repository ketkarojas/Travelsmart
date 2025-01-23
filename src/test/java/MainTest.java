import org.example.Flight;
import org.example.Main;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.concurrent.CompletableFuture;


import static org.junit.Assert.assertEquals;
import org.example.Flight;
import org.example.Main;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Comparator;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MainTest {

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetSortingComparatorByFare() {
        Comparator<Flight> comparator = Main.getSortingComparator(1);
        Flight flight1 = new Flight("AI101", "Delhi", "Mumbai", 5000, 2);
        Flight flight2 = new Flight("AI102", "Delhi", "Mumbai", 3000, 3);
        assertTrue(comparator.compare(flight1, flight2) > 0);
    }

    @Test
    public void testGetSortingComparatorByDuration() {
        Comparator<Flight> comparator = Main.getSortingComparator(2);
        Flight flight1 = new Flight("AI101", "Delhi", "Mumbai", 5000, 2);
        Flight flight2 = new Flight("AI102", "Delhi", "Mumbai", 3000, 3);
        assertTrue(comparator.compare(flight1, flight2) < 0);
    }

    @Test
    public void testGetSortingComparatorByFareAndDuration() {
        Comparator<Flight> comparator = Main.getSortingComparator(3);
        Flight flight1 = new Flight("AI101", "Delhi", "Mumbai", 5000, 2);
        Flight flight2 = new Flight("AI102", "Delhi", "Mumbai", 5000, 3);
        assertTrue(comparator.compare(flight1, flight2) < 0);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testGetSortingComparatorInvalidOption() {
        Main.getSortingComparator(4);
    }
}