package org.example;

import java.util.Comparator;

public class Flight {
    private final String flightCompany;
    private final String sourceCity;
    private final String destinationCity;
    private final int fare;
    private final int duration;

    public Flight(String flightCompany, String sourceCity, String destinationCity, int fare, int duration) {
        this.flightCompany = flightCompany;
        this.sourceCity = sourceCity;
        this.destinationCity = destinationCity;
        this.fare = fare;
        this.duration = duration;
    }

    public String getFlightCompany() { return flightCompany; }
    public String getSourceCity() { return sourceCity; }
    public String getDestinationCity() { return destinationCity; }
    public int getFare() { return fare; }
    public int getDuration() { return duration; }

    public static Comparator<Flight> compareByFare() {
        return Comparator.comparingInt(Flight::getFare);
    }

    public static Comparator<Flight> compareByDuration() {
        return Comparator.comparingInt(Flight::getDuration);
    }

    public static Comparator<Flight> compareByFareAndDuration() {
        return compareByFare().thenComparing(compareByDuration());
    }
}
