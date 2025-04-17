package com.aereoporto.service;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import org.springframework.stereotype.Service;
import com.aereoporto.model.Flight;
import com.aereoporto.repository.FlightRepository;

@Service
public class FlightService {
    @Autowired
    public FlightRepository flightRepository; //public to test

    /**
     * Gets all flights from the database.
     *
     * @return A list of all flights in the database.
     */
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    /**
     * Retrieves a flight from the database by ID.
     * 
     * @param id the ID of the flight to retrieve
     * @return the flight with the given ID
     * @throws RuntimeException if the flight is not found
     */
    public Flight getFlight(Integer id) {
        return flightRepository.findById(id).orElseThrow(() -> new RuntimeException("Flight not found"));
    }

    /**
     * Adds a new flight to the database.
     *
     * @param flight the flight entity to be added
     */

    public void addFlight(Flight Flight) {
        flightRepository.save(Flight);
    }

    /**
     * Updates an existing flight in the database with the provided details.
     *
     * @param id            the ID of the flight to update
     * @param updatedFlight the flight object containing updated information
     * @return the updated flight entity
     * @throws RuntimeException if the flight is not found
     */

    public Flight updateFlight(Integer id, Flight uptadedFlight) {
        return flightRepository.findById(id).map(Flight -> {
            Flight.setArrivalCity(uptadedFlight.getArrivalCity());
            Flight.setArrivalTime(uptadedFlight.getArrivalTime());
            Flight.setDay(uptadedFlight.getDay());
            Flight.setDepartureCity(uptadedFlight.getDepartureCity());
            Flight.setArrivalCity(uptadedFlight.getArrivalCity());
            Flight.setIdAirplane(uptadedFlight.getIdAirplane());
            Flight.setPassengers(uptadedFlight.getPassengers());
            return flightRepository.save(Flight);
        }).orElseThrow(() -> new RuntimeException("Flight not found"));
    }

    /**
     * Deletes a flight from the database by ID.
     *
     * @param id the ID of the flight to delete
     * @throws RuntimeException if the flight is not found
     */
    public void deleteFlight(Integer id) {
        if (!flightRepository.existsById(id)) {
            throw new RuntimeException("Flight not found");
        }
        flightRepository.deleteById(id);
    }

    /**
     * Retrieves a list of flights scheduled to depart from the current date
     * onwards.
     *
     * @return a list of upcoming flights
     */

    public List<Flight> getFlightsFromNowOn() {
        return flightRepository.findUpcomingFlights();
    }

}
