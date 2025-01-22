import org.example.Flight;
import org.example.Main;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.concurrent.CompletableFuture;


import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public class MainTest {

    @Mock
    private Main main;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        main = mock(Main.class);
    }

    @Test
    public void testSearchFlights() throws ExecutionException, InterruptedException, ExecutionException {
        List<Flight> mockedFlights = Arrays.asList(
                new Flight("AI101", "Delhi", "Mumbai", 5000, 2),
                new Flight("AI102", "Delhi", "Mumbai", 3000, 3)
        );

        when(main.searchFlightsAsync("Delhi", "Mumbai")).thenReturn(CompletableFuture.completedFuture(mockedFlights));

        Future<List<Flight>> future = main.searchFlightsAsync("Delhi", "Mumbai");
        List<Flight> flights = future.get();

        assertNotNull(flights);
        assertEquals(2, flights.size());
        assertEquals("Delhi", flights.get(0).getSourceCity());
    }

    @Test
    public void testSortFlightsByFare() {
        List<Flight> flights = Arrays.asList(
                new Flight("AI101", "Delhi", "Mumbai", 5000, 2),
                new Flight("AI102", "Delhi", "Mumbai", 3000, 3)
        );

        flights.sort(Flight.compareByFare());

        assertEquals(3000, flights.get(0).getFare()); // The flight with the lower fare should be first
    }
}
