package com.aereoporto.model;

public class FlightWithAvailability {
    private Flight flight;
    private int availableSeats;
    private int availableWeight;

    public FlightWithAvailability(Flight flight, int availableSeats, int availableWeight) {
        this.flight = flight;
        this.availableSeats = availableSeats;
        this.availableWeight = availableWeight;
    }

    public Flight getFlight() {
        return this.flight;
    }

    public int getAvailableSeats() {
        return this.availableSeats;
    }

    public int getAvailableWeight() {
        return this.availableWeight;
    }

    @Override
    public String toString() {
        return "FlightWithAvailability [flight=" + flight + ", availableSeats=" + availableSeats + ", availableWeight="
                + availableWeight + "]";
    }

}
