package com.aereoporto.service;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import org.springframework.stereotype.Service;
import com.aereoporto.model.Airport;
import com.aereoporto.repository.AirportRepository;

@Service
public class AirportService {
    @Autowired
    private AirportRepository airportRepository;

    /**
     * Retrieve all airports from the database.
     *
     * @return a list of all airports in the database
     */
    public List<Airport> getAllAirports() {
        return airportRepository.findAll();
    }

    /**
     * Retrieve an airport from the database by ID.
     *
     * @param id the ID of the airport to retrieve
     * @return the airport with the given ID
     * @throws RuntimeException if the airport is not found
     */
    public Airport getAirport(Integer id) {
        return airportRepository.findById(id).orElseThrow(() -> new RuntimeException("Airport not found"));
    }

    /**
     * Add a new airport to the database.
     *
     * @param Airport the airport entity to add
     */

    public void addAirport(Airport Airport) {
        airportRepository.save(Airport);
    }

    /**
     * Update an existing airport in the database.
     *
     * @param id             the ID of the airport to update
     * @param updatedAirport the airport entity containing updated information
     * @return the updated airport entity
     * @throws RuntimeException if the airport is not found
     */

    public Airport updateAirport(Integer id, Airport updatedAirport) {
        return airportRepository.findById(id).map(Airport -> {
            Airport.setCity(updatedAirport.getCity());
            Airport.setNation(updatedAirport.getNation());
            Airport.setNumRunway(updatedAirport.getNumRunway());
            return airportRepository.save(Airport);
        }).orElseThrow(() -> new RuntimeException("Airport not found"));
    }

    /**
     * Delete an airport from the database.
     *
     * @param id the ID of the airport to delete
     * @throws RuntimeException if the airport is not found
     */
    public void deleteAirport(Integer id) {
        if (!airportRepository.existsById(id)) {
            throw new RuntimeException("Airport not found");
        }
        airportRepository.deleteById(id);
    }
}
