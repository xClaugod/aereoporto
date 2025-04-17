package com.aereoporto.service;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import org.springframework.stereotype.Service;
import com.aereoporto.model.Airplane;
import com.aereoporto.repository.AirplaneRepository;

@Service
public class AirplaneService {
    @Autowired
    public AirplaneRepository airplaneRepository; //public to test

    /**
     * @return all airplanes
     */
    public List<Airplane> getAllAirplanes() {
        return airplaneRepository.findAll();
    }

    /**
     * Retrieves an airplane by its unique identifier.
     * 
     * @param id the unique identifier of the airplane
     * @return the airplane with the specified id
     * @throws RuntimeException if no airplane is found with the given id
     */

    public Airplane getAirplane(String id) {
        return airplaneRepository.findById(id).orElseThrow(() -> new RuntimeException("Airplane not found"));
    }

    /**
     * Saves a new airplane in the database.
     *
     * @param Airplane the airplane to be saved
     */
    public void addAirplane(Airplane Airplane) {
        airplaneRepository.save(Airplane);
    }

    /**
     * Updates an existing airplane in the database with new information.
     *
     * @param id           the unique identifier of the airplane to be updated
     * @param updatedAereo the airplane object containing updated information
     * @return the updated airplane object
     * @throws RuntimeException if no airplane is found with the given id
     */

    public Airplane updateAirplane(String id, Airplane updatedAereo) {
        return airplaneRepository.findById(id).map(Airplane -> {
            Airplane.setNumPassengers(updatedAereo.getNumPassengers());
            Airplane.setQtyGoods(updatedAereo.getQtyGoods());
            return airplaneRepository.save(Airplane);
        }).orElseThrow(() -> new RuntimeException("Airplane not found"));
    }

    /**
     * Deletes an airplane from the database.
     * 
     * @param id the unique identifier of the airplane to be deleted
     * @throws RuntimeException if no airplane is found with the given id
     */
    public void deleteAirplane(String id) {
        if (!airplaneRepository.existsById(id)) {
            throw new RuntimeException("Airplane not found");
        }
        airplaneRepository.deleteById(id);
    }
}
