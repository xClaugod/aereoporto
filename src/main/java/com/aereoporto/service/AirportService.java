package com.aereoporto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;
import org.springframework.stereotype.Service;

import com.aereoporto.model.Airport;
import com.aereoporto.repository.AirportRepository;

@Service
public class AirportService {
    @Autowired
    private AirportRepository airportRepository;

    public List<Airport> getAllAirports() {return airportRepository.findAll();}

	public Airport getAirport(Integer id) {
        return airportRepository.findById(id).orElseThrow(() -> new RuntimeException("Airport not found"));
	}

    public void addAirport(Airport Airport){ airportRepository.save(Airport); }

    public Airport updateAirport(Integer id, Airport updatedAirport) {
        return airportRepository.findById(id).map(Airport -> {
            Airport.setCity(updatedAirport.getCity());
            Airport.setNation(updatedAirport.getNation());
            Airport.setNumRunway(updatedAirport.getNumRunway());
            return airportRepository.save(Airport);
        }).orElseThrow(() -> new RuntimeException("Airport not found"));
    }

    public void deleteAirport(Integer id) {
        if (!airportRepository.existsById(id)) {
            throw new RuntimeException("Airport not found");
        }
        airportRepository.deleteById(id);	
    }

/*
    public List<Airport> searchAerei(String nome, String cognome, String squadra) {
        Specification<Airport> specificRequest = Specification.where(null); //query vuota

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

        return airportRepository.findAll(specificRequest);
    }*/
    
}
