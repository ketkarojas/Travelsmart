package org.example;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Main {
    private List<Flight> flights = new ArrayList<>();
    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    Main() {
        loadFlights("src\\main\\java\\org\\example\\AirIndia_Flights.txt");
        loadFlights("src\\main\\java\\org\\example\\JetAirways_Flights.txt");
        loadFlights("src\\main\\java\\org\\example\\SpiceJet_Flights.txt");
    }

    private void loadFlights(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            // Skip the header line
            br.readLine();
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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Prompt for source and destination cities
        System.out.print("Enter source city: ");
        String sourceCity = scanner.nextLine().trim();
        System.out.print("Enter destination city: ");
        String destinationCity = scanner.nextLine().trim();

        Main main = new Main();

        try {
            Future<List<Flight>> future = main.searchFlightsAsync(sourceCity, destinationCity);
            List<Flight> flights = future.get();

            if (flights.isEmpty()) {
                System.out.println("No flights found between " + sourceCity + " and " + destinationCity);
            } else {
                System.out.println("Available flights:");
                for (int i = 0; i < flights.size(); i++) {
                    System.out.println((i + 1) + ". " + flights.get(i));
                }
                // Prompt for sorting preference
                System.out.println("Sort by: 1. Fare 2. Duration 3. Both");
                int sortOption = scanner.nextInt();
                Comparator<Flight> comparator;
                switch (sortOption) {
                    case 1:
                        comparator = Flight.compareByFare();
                        break;
                    case 2:
                        comparator = Flight.compareByDuration();
                        break;
                    case 3:
                        comparator = Flight.compareByFareAndDuration();
                        break;
                    default:
                        System.out.println("Invalid option. Defaulting to sort by fare.");
                        comparator = Flight.compareByFare();
                }
                if (!flights.isEmpty()) {
                    flights.sort(comparator);
                }

                System.out.println("Sorted flights:");
                for (int i = 0; i < flights.size(); i++) {
                    System.out.println((i + 1) + ". " + flights.get(i));
                }

                // Select a flight from the list
                System.out.print("Select a flight (1-" + flights.size() + "): ");
                int choice = scanner.nextInt();
                if (choice > 0 && choice <= flights.size()) {
                    Flight selectedFlight = flights.get(choice - 1);
                    System.out.println("You selected: " + selectedFlight);

                    // Prompt for number of tickets
                    System.out.print("Enter the number of tickets: ");
                    int numberOfTickets = scanner.nextInt();

                    // Calculate total fare
                    int totalFare = selectedFlight.getFare() * numberOfTickets;
                    System.out.println("Total fare for " + numberOfTickets + " tickets: " + totalFare);
                } else {
                    System.out.println("Invalid choice.");
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            main.executorService.shutdown();
            scanner.close();
        }
    }
}