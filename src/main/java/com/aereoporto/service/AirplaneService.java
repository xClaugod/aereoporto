package com.aereoporto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;
import org.springframework.stereotype.Service;

import com.aereoporto.model.Airplane;
import com.aereoporto.repository.AirplaneRepository;

@Service
public class AirplaneService {
    @Autowired
    private AirplaneRepository airplaneRepository;

    public List<Airplane> getAllAirplanes() {return airplaneRepository.findAll();}

	public Airplane getAirplane(String id) {
        return airplaneRepository.findById(id).orElseThrow(() -> new RuntimeException("Airplane not found"));
	}

    public void addAirplane(Airplane Airplane){ airplaneRepository.save(Airplane); }

    public Airplane updateAirplane(String id, Airplane updatedAereo) {
        return airplaneRepository.findById(id).map(Airplane -> {
            Airplane.setNumPassengers(updatedAereo.getNumPassengers());
            Airplane.setQtyGoods(updatedAereo.getQtyGoods());
            return airplaneRepository.save(Airplane);
        }).orElseThrow(() -> new RuntimeException("Airplane not found"));
    }

    public void deleteAirplane(String id) {
        if (!airplaneRepository.existsById(id)) {
            throw new RuntimeException("Airplane not found");
        }
        airplaneRepository.deleteById(id);	
    }

/*
    public List<Airplane> searchAerei(String nome, String cognome, String squadra) {
        Specification<Airplane> specificRequest = Specification.where(null); //query vuota

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

        return airplaneRepository.findAll(specificRequest);
    }*/
    
}
