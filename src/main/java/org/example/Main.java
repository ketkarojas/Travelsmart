package org.example;

import java.util.*;
import java.util.concurrent.*;

public class Main {

    // Custom exception for invalid ticket number
    public static class InvalidTicketNumberException extends RuntimeException {
        public InvalidTicketNumberException(String message) {
            super(message);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FlightService flightService = FlightService.getInstance();

        // Load flight data from files
        flightService.loadFlights("src\\main\\resources\\AirIndia_Flights.txt");
        flightService.loadFlights("src\\main\\resources\\JetAirways_Flights.txt");
        flightService.loadFlights("src\\main\\resources\\SpiceJet_Flights.txt");

        try {
            // Get source and destination cities from user
            System.out.print("Enter source city: ");
            String sourceCity = scanner.nextLine().trim();
            System.out.print("Enter destination city: ");
            String destinationCity = scanner.nextLine().trim();

            // Search for flights asynchronously
            Future<List<Flight>> future = flightService.searchFlightsAsync(sourceCity, destinationCity);
            List<Flight> flights = future.get();

            // Check if any flights are found
            if (flights.isEmpty()) {
                System.out.println("No flights found in between " + sourceCity + " and " + destinationCity);
            } else {
                System.out.println("Available flights:");
                printFlightsTable(flights);

                // Get sorting option from user
                System.out.println("Sort by: 1. Fare 2. Duration 3. Both");
                int sortOption = scanner.nextInt();
                Comparator<Flight> comparator = getSortingComparator(sortOption);
                flightService.sortFlights(flights, comparator);

                // Print sorted flights
                System.out.println("Sorted flights:");
                printFlightsTable(flights);

                // Get flight selection from user
                System.out.print("Select a flight (1-" + flights.size() + "): ");
                int choice = scanner.nextInt();
                if (choice > 0 && choice <= flights.size()) {
                    Flight selectedFlight = flights.get(choice - 1);
                    System.out.println("You selected:");
                    printSingleFlightTable(selectedFlight);

                    // Get number of tickets from user
                    System.out.print("Enter the number of tickets: ");
                    int numberOfTickets = scanner.nextInt();

                    // Validate ticket number
                    if (numberOfTickets <= 0) {
                        throw new InvalidTicketNumberException("Ticket number must be positive.");
                    }

                    // Calculate and print total fare
                    int totalFare = selectedFlight.getFare() * numberOfTickets;
                    System.out.println("Total fare for " + numberOfTickets + " tickets: " + totalFare);
                } else {
                    System.out.println("Invalid choice.");
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            // Shutdown the flight service and close the scanner
            flightService.shutdown();
            scanner.close();
        }
    }

    // Method to print a table of flights
    private static void printFlightsTable(List<Flight> flights) {
        String format = "| %-3s | %-10s | %-15s | %-15s | %-15s | %-10s | %-10s |%n";
        System.out.format(
                "+-----+------------+-----------------+-----------------+-----------------+------------+------------+%n");
        System.out.format(
                "| No. | Flight No. | Airline         | Source City     | Destination City| Fare       | Duration   |%n");
        System.out.format(
                "+-----+------------+-----------------+-----------------+-----------------+------------+------------+%n");
        for (int i = 0; i < flights.size(); i++) {
            Flight flight = flights.get(i);
            System.out.format(format, (i + 1), flight.getFlightNumber(), flight.getFlightCompany(),
                    flight.getSourceCity(), flight.getDestinationCity(), flight.getFare(), flight.getDuration());
        }
        System.out.format(
                "+-----+------------+-----------------+-----------------+-----------------+------------+------------+%n");
    }

    // Method to print details of a single flight
    private static void printSingleFlightTable(Flight flight) {
        String format = "| %-10s | %-15s | %-15s | %-15s | %-10s | %-10s |%n";
        System.out.format(
                "+------------+-----------------+-----------------+-----------------+------------+------------+%n");
        System.out.format(
                "| Flight No. | Airline         | Source City     | Destination City| Fare       | Duration   |%n");
        System.out.format(
                "+------------+-----------------+-----------------+-----------------+------------+------------+%n");
        System.out.format(format, flight.getFlightNumber(), flight.getFlightCompany(), flight.getSourceCity(),
                flight.getDestinationCity(), flight.getFare(), flight.getDuration());
        System.out.format(
                "+------------+-----------------+-----------------+-----------------+------------+------------+%n");
    }

    // Method to get the comparator for sorting flights based on user option
    public static Comparator<Flight> getSortingComparator(int option) {
        switch (option) {
            case 1:
                return Flight.compareByFare();
            case 2:
                return Flight.compareByDuration();
            case 3:
                return Flight.compareByFareAndDuration();
            default:
                throw new IllegalArgumentException("Invalid sorting option.");
        }
    }
}