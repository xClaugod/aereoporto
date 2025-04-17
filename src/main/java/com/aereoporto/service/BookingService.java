package com.aereoporto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import com.aereoporto.model.Flight;
import com.aereoporto.repository.FlightRepository;

@Service
public class BookingService {

    @Autowired
    public FlightRepository flightRepository; //public to test

    /**
     * Books a flight by updating the number of passengers and the baggage weight.
     *
     * @param flightId      the ID of the flight to be booked
     * @param numPassengers the number of passengers to add to the flight
     * @param baggageWeight the weight of baggage to add to the flight
     * @throws Exception if the flight is not found or a concurrency error occurs
     */

    public void bookFlight(int flightId, int numPassengers, int baggageWeight) throws Exception {
        Flight flight = flightRepository.findById(flightId).orElseThrow(() -> new Exception("Flight not found"));

        try {
            flight.setPassengers(flight.getPassengers() + numPassengers);
            flight.setGoods(flight.getGoods() + baggageWeight);
            flightRepository.save(flight);

        } catch (OptimisticLockingFailureException e) {
            throw new Exception("Concurrency error. Retry");
        }
    }
}
