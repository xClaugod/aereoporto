package com.aereoporto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.aereoporto.Converter.CustomTimestampConverter;
import com.aereoporto.model.Flight;
import com.aereoporto.repository.FlightRepository;

@Service
public class FlightService {
    @Autowired
    private FlightRepository flightRepository;

    /* 
    public List<FlightDTO> getAllFlights() {
        return flightRepository.findAll().
                                stream()
                                .map(FlightDTO::new)
                                .collect(Collectors.toList());
    }*/

    public List<Flight> getAllFlights(){return flightRepository.findAll();}

	public Flight getFlight(Integer id) {
        return flightRepository.findById(id).orElseThrow(() -> new RuntimeException("Flight not found"));
	}

    public void addFlight(Flight Flight){ flightRepository.save(Flight); }

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

/*
    public List<Flight> searchAerei(String nome, String cognome, String squadra) {
        Specification<Flight> specificRequest = Specification.where(null); //query vuota

        if (nome != null && !nome.isEmpty()) {
            specificRequest = specificRequest.and((root, query, criteriaBuilder) -> //l'and combina più condizioni
                criteriaBuilder.like(root.get("nome"), "%" + nome + "%")); //aggiungo like sul parametro nome
        }

        if (cognome != null && !cognome.isEmpty()) {
            specificRequest = specificRequest.and((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("cognome"), "%" + cognome + "%"));
        }

        if (squadra != null && !squadra.isEmpty()) {
            specificRequest = specificRequest.and((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("squadra"), "%" + squadra + "%"));
        }

        return flightRepository.findAll(specificRequest);
    }*/
    
}
