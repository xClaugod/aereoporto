package com.aereoporto.service;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import org.springframework.stereotype.Service;
import com.aereoporto.model.Flight;
import com.aereoporto.repository.FlightRepository;

@Service
public class FlightService {
    @Autowired
    private FlightRepository flightRepository;

    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    public Flight getFlight(Integer id) {
        return flightRepository.findById(id).orElseThrow(() -> new RuntimeException("Flight not found"));
    }

    public void addFlight(Flight Flight) {
        flightRepository.save(Flight);
    }

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

    public void deleteFlight(Integer id) {
        if (!flightRepository.existsById(id)) {
            throw new RuntimeException("Flight not found");
        }
        flightRepository.deleteById(id);
    }

    public List<Flight> getFlightsFromNowOn() {
        return flightRepository.findUpcomingFlights();
    }

}
