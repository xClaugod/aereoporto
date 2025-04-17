package com.aereoporto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import com.aereoporto.model.Flight;
import com.aereoporto.repository.FlightRepository;

@Service
public class BookingService {

    @Autowired
    private FlightRepository flightRepository;

    public void bookFlight(int flightId, int numPassengers, int baggageWeight) throws Exception {
        Flight flight = flightRepository.findById(flightId).orElseThrow(() -> new Exception("Volo non trovato"));

        try {
            // Tentativo di prenotare il volo
            flight.setPassengers(flight.getPassengers() + numPassengers);
            flight.setGoods(flight.getGoods() + baggageWeight);
            flightRepository.save(flight); // Hibernate verifica la versione

        } catch (OptimisticLockingFailureException e) {
            throw new Exception("Conflitto di concorrenza: il volo è stato aggiornato nel frattempo. Riprova.");
        }
    }
}
