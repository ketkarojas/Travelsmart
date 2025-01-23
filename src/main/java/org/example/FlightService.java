package org.example;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class FlightService {
    // Singleton instance of FlightService
    private static final FlightService instance = new FlightService();
    
    // Executor service for handling asynchronous tasks
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    
    // List to store flight details
    private final List<Flight> flights = new ArrayList<>();

    // Private constructor for Singleton pattern
    private FlightService() {
    }

    // Method to get the singleton instance of FlightService
    public static FlightService getInstance() {
        return instance;
    }

    // Method to clear the list of flights
    public void clearFlights() {
        flights.clear();
    }

    // Method to load flights from a CSV file
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

    // Method to search for flights based on source and destination cities
    public List<Flight> searchFlights(String sourceCity, String destinationCity) {
        return flights.stream()
                .filter(flight -> flight.getSourceCity().equalsIgnoreCase(sourceCity)
                        && flight.getDestinationCity().equalsIgnoreCase(destinationCity))
                .collect(Collectors.toList());
    }

    // Method to search for flights asynchronously based on source and destination cities
    public Future<List<Flight>> searchFlightsAsync(String sourceCity, String destinationCity) {
        return executorService.submit(() -> searchFlights(sourceCity, destinationCity));
    }

    // Method to shut down the executor service
    public void shutdown() {
        executorService.shutdown();
    }

    // Method to sort flights based on a given comparator
    public void sortFlights(List<Flight> flights, Comparator<Flight> comparator) {
        flights.sort(comparator);
    }
}