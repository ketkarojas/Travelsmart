package org.example;

import java.util.*;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FlightService flightService = new FlightService();

        // Load flight data
        flightService.loadFlights("src\\main\\resources\\AirIndia_Flights.txt");
        flightService.loadFlights("src\\main\\resources\\JetAirways_Flights.txt");
        flightService.loadFlights("src\\main\\resources\\SpiceJet_Flights.txt");

        try {
            System.out.print("Enter source city: ");
            String sourceCity = scanner.nextLine().trim();
            System.out.print("Enter destination city: ");
            String destinationCity = scanner.nextLine().trim();

            Future<List<Flight>> future = flightService.searchFlightsAsync(sourceCity, destinationCity);
            List<Flight> flights = future.get();

            if (flights.isEmpty()) {
                System.out.println("No flights found between " + sourceCity + " and " + destinationCity);
            } else {
                System.out.println("Available flights:");
                printFlightsTable(flights);

                System.out.println("Sort by: 1. Fare 2. Duration 3. Both");
                int sortOption = scanner.nextInt();
                Comparator<Flight> comparator = getSortingComparator(sortOption);
                flightService.sortFlights(flights, comparator);

                System.out.println("Sorted flights:");
                printFlightsTable(flights);

                System.out.print("Select a flight (1-" + flights.size() + "): ");
                int choice = scanner.nextInt();
                if (choice > 0 && choice <= flights.size()) {
                    Flight selectedFlight = flights.get(choice - 1);
                    System.out.println("You selected:");
                    printSingleFlightTable(selectedFlight);

                    System.out.print("Enter the number of tickets: ");
                    int numberOfTickets = scanner.nextInt();

                    int totalFare = selectedFlight.getFare() * numberOfTickets;
                    System.out.println("Total fare for " + numberOfTickets + " tickets: " + totalFare);
                } else {
                    System.out.println("Invalid choice.");
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            flightService.shutdown();
            scanner.close();
        }
    }

    private static void printFlightsTable(List<Flight> flights) {
        String format = "| %-3s | %-15s | %-15s | %-15s | %-10s | %-10s |%n";
        System.out.format("+-----+-----------------+-----------------+-----------------+------------+------------+%n");
        System.out.format("| No. | Airline         | Source City     | Destination City| Fare       | Duration   |%n");
        System.out.format("+-----+-----------------+-----------------+-----------------+------------+------------+%n");
        for (int i = 0; i < flights.size(); i++) {
            Flight flight = flights.get(i);
            System.out.format(format, (i + 1), flight.getFlightCompany(), flight.getSourceCity(), flight.getDestinationCity(), flight.getFare(), flight.getDuration());
        }
        System.out.format("+-----+-----------------+-----------------+-----------------+------------+------------+%n");
    }

    private static void printSingleFlightTable(Flight flight) {
        String format = "| %-15s | %-15s | %-15s | %-10s | %-10s |%n";
        System.out.format("+-----------------+-----------------+-----------------+------------+------------+%n");
        System.out.format("| Airline         | Source City     | Destination City| Fare       | Duration   |%n");
        System.out.format("+-----------------+-----------------+-----------------+------------+------------+%n");
        System.out.format(format, flight.getFlightCompany(), flight.getSourceCity(), flight.getDestinationCity(), flight.getFare(), flight.getDuration());
        System.out.format("+-----------------+-----------------+-----------------+------------+------------+%n");
    }

    private static Comparator<Flight> getSortingComparator(int option) {
        switch (option) {
            case 1: return Flight.compareByFare();
            case 2: return Flight.compareByDuration();
            case 3: return Flight.compareByFareAndDuration();
            default: throw new IllegalArgumentException("Invalid sorting option.");
        }
    }
}
