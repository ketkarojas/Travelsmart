package org.example;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class FlightService {
    private static final FlightService instance = new FlightService();
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final List<Flight> flights = new ArrayList<>();

    // Private constructor for Singleton
    private FlightService() {
    }

    public static FlightService getInstance() {
        return instance;
    }

    public void clearFlights() {
        flights.clear();
    }

    public void loadFlights(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                flights.add(new Flight(details[0].trim(), details[1].trim(), details[2].trim(),
                        Integer.parseInt(details[3].trim()), Integer.parseInt(details[4].trim())));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Flight> searchFlights(String sourceCity, String destinationCity) {
        return flights.stream()
                .filter(flight -> flight.getSourceCity().equalsIgnoreCase(sourceCity)
                        && flight.getDestinationCity().equalsIgnoreCase(destinationCity))
                .collect(Collectors.toList());
    }

    public Future<List<Flight>> searchFlightsAsync(String sourceCity, String destinationCity) {
        return executorService.submit(() -> searchFlights(sourceCity, destinationCity));
    }

    public void shutdown() {
        executorService.shutdown();
    }

    public void sortFlights(List<Flight> flights, Comparator<Flight> comparator) {
        flights.sort(comparator);
    }
}
