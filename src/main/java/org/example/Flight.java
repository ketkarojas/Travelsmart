package org.example;
import java.util.Comparator;

public class Flight implements Comparable<Flight> {
    private String FlightNumber;
    private String FlightCompany;
    private String SourceCity;
    private String DestinationCity;
    private String airline;
    private int fare;
    int duration;

    // Constructor 
    public Flight(String FlightNumber, String SourceCity, String DestinationCity, int fare, int duration) {
        this.FlightNumber = FlightNumber;
        this.SourceCity = SourceCity;
        this.DestinationCity = DestinationCity;
        this.fare = fare;
        this.duration = duration;
        setFlightCompany();
    }

    // Getter-setter methods
    public String getFlightNumber() {
        return FlightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        FlightNumber = flightNumber;
    }

    public String getSourceCity() {
        return SourceCity;
    }

    public void setSourceCity(String sourceCity) {
        SourceCity = sourceCity;
    }

    public String getDestinationCity() {
        return DestinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        DestinationCity = destinationCity;
    }

    public int getFare() {
        return fare;
    }

    public void setFare(int fare) {
        this.fare = fare;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    // Function to set the airline depending on the flight code
    public void setFlightCompany() {
        if (FlightNumber.startsWith("1")) FlightCompany = "AirIndia";
        else if (FlightNumber.startsWith("2")) FlightCompany = "JetAirways";
        else if (FlightNumber.startsWith("3")) FlightCompany = "SpiceJet";
        airline = FlightCompany;
    }

    public String getFlightCompany() {
        return airline;
    }

    // To string method
    @Override
    public String toString() {
        return FlightCompany + " [FlightNumber=" + FlightNumber + ", SourceCity=" + SourceCity + ", DestinationCity="
                + DestinationCity + ", fare=" + fare + ", duration=" + duration + "]";
    }

    // Function to compare flights based on fare, duration or both
    @Override
    public int compareTo(Flight other) {
        return Comparator.comparingInt(Flight::getFare)
                .thenComparingInt(Flight::getDuration)
                .compare(this, other);
    }

    public static Comparator<Flight> compareByFare() {
        return Comparator.comparingInt(Flight::getFare);
    }

    public static Comparator<Flight> compareByDuration() {
        return Comparator.comparingInt(Flight::getDuration);
    }

    public static Comparator<Flight> compareByFareAndDuration() {
        return Comparator.comparingInt(Flight::getFare)
                .thenComparingInt(Flight::getDuration);
    }
}