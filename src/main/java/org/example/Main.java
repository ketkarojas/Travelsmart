package org.example;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Main {
    private List<Flight> flights = new ArrayList<>();
    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    //constructor to load all the three data files
    Main() {
        loadFlights("src\\main\\java\\org\\example\\AirIndia_Flights.txt");
        loadFlights("src\\main\\java\\org\\example\\JetAirways_Flights.txt");
        loadFlights("src\\main\\java\\org\\example\\SpiceJet_Flights.txt");
    }

    //function to read the files, split the data according to regex patterns
    private void loadFlights(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            // Skip the header line
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                //split by commas, trim the input elements to eliminate whitespaces
                flights.add(new Flight(details[0].trim(), details[1].trim(), details[2].trim(),
                        Integer.parseInt(details[3].trim()), Integer.parseInt(details[4].trim())));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //function to implement search methodology
    public List<Flight> searchFlights(String sourceCity, String destinationCity) {
        return flights.stream()
                .filter(flight -> flight.getSourceCity().equalsIgnoreCase(sourceCity)
                        && flight.getDestinationCity().equalsIgnoreCase(destinationCity))
                .collect(Collectors.toList());
    }

    //function to handle multiple users trying to seach the flights at the same time
    public Future<List<Flight>> searchFlightsAsync(String sourceCity, String destinationCity) {
        return executorService.submit(() -> searchFlights(sourceCity, destinationCity));
    }


    //function to print the available flights in a tabular format
    private void printFlightsTable(List<Flight> flights) {
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

    //function to print the selected flight in a tabular format
    private void printSingleFlightTable(Flight flight) {
        String format = "| %-15s | %-15s | %-15s | %-10s | %-10s |%n";
        System.out.format("+-----------------+-----------------+-----------------+------------+------------+%n");
        System.out.format("| Airline         | Source City     | Destination City| Fare       | Duration   |%n");
        System.out.format("+-----------------+-----------------+-----------------+------------+------------+%n");
        System.out.format(format, flight.getFlightCompany(), flight.getSourceCity(), flight.getDestinationCity(), flight.getFare(), flight.getDuration());
        System.out.format("+-----------------+-----------------+-----------------+------------+------------+%n");
    }

    //main function
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

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
                main.printFlightsTable(flights);

                //Switch case to give choices to the user to sort the flights based on fare, duration or both
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
                main.printFlightsTable(flights);

                // Select a flight from the list
                System.out.print("Select a flight (1-" + flights.size() + "): ");
                int choice = scanner.nextInt();
                if (choice > 0 && choice <= flights.size()) {
                    Flight selectedFlight = flights.get(choice - 1);
                    System.out.println("You selected:");
                    main.printSingleFlightTable(selectedFlight);

                    // Select the number of tickets
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