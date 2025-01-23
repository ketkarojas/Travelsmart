package org.example;

import java.util.Comparator;

public class Flight implements Comparable<Flight> {
    private String flightNumber;
    private String flightCompany;
    private String sourceCity;
    private String destinationCity;
    private String airline;
    private int fare;
    int duration;

    // Factory method
    public static Flight createFlight(String FlightNumber, String SourceCity, String DestinationCity, int fare,
            int duration) {
        if (fare < 0 || duration < 0) {
            throw new IllegalArgumentException("Fare and duration must be non-negative.");
        }
        return new Flight(FlightNumber, SourceCity, DestinationCity, fare, duration);
    }

    // Constructor to initialize flight details with validation
    public Flight(String flightNumber, String sourceCity, String destinationCity, int fare, int duration) {
        this.flightNumber = flightNumber;
        this.sourceCity = sourceCity;
        this.destinationCity = destinationCity;
        this.fare = fare;
        this.duration = duration;
        setFlightCompany();
    }

    // Getter for flight number
    public String getFlightNumber() {
        return flightNumber;
    }

    // Setter for flight number
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    // Getter for source city
    public String getSourceCity() {
        return sourceCity;
    }

    // Setter for source city
    public void setSourceCity(String sourceCity) {
        this.sourceCity = sourceCity;
    }

    // Getter for destination city
    public String getDestinationCity() {
        return destinationCity;
    }

    // Setter for destination city
    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    // Getter for fare
    public int getFare() {
        return fare;
    }

    // Setter for fare
    public void setFare(int fare) {
        this.fare = fare;
    }

    // Getter for duration
    public int getDuration() {
        return duration;
    }

    // Setter for duration
    public void setDuration(int duration) {
        this.duration = duration;
    }

    // Function to set the airline depending on the flight code
    public void setFlightCompany() {
        if (flightNumber.startsWith("1"))
            flightCompany = "AirIndia";
        else if (flightNumber.startsWith("2"))
            flightCompany = "JetAirways";
        else if (flightNumber.startsWith("3"))
            flightCompany = "SpiceJet";
        airline = flightCompany;
    }

    // Getter for flight company
    public String getFlightCompany() {
        return airline;
    }

    // To string method to represent flight details as a string
    @Override
    public String toString() {
        return flightCompany + " [FlightNumber=" + flightNumber + ", SourceCity=" + sourceCity + ", DestinationCity="
                + destinationCity + ", fare=" + fare + ", duration=" + duration + "]";
    }

    // Function to compare flights based on fare, duration or both
    @Override
    public int compareTo(Flight other) {
        return Comparator.comparingInt(Flight::getFare)
                .thenComparingInt(Flight::getDuration)
                .compare(this, other);
    }

    // Comparator to compare flights by fare
    public static Comparator<Flight> compareByFare() {
        return Comparator.comparingInt(Flight::getFare);
    }

    // Comparator to compare flights by duration
    public static Comparator<Flight> compareByDuration() {
        return Comparator.comparingInt(Flight::getDuration);
    }

    // Comparator to compare flights by fare and duration
    public static Comparator<Flight> compareByFareAndDuration() {
        return Comparator.comparingInt(Flight::getFare)
                .thenComparingInt(Flight::getDuration);
    }
}