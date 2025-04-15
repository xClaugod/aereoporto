package com.aereoporto.repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.aereoporto.model.Flight;
public interface FlightRepository extends JpaRepository<Flight,Integer>,JpaSpecificationExecutor<Flight> {
    Optional<Flight> findById(Integer id);
    
    void deleteById(Integer id);

    @Query(value = "SELECT * FROM VOLI WHERE STR_TO_DATE(GIORNO, '%d-%m-%Y') >= CURRENT_DATE", nativeQuery = true)
    List<Flight> findUpcomingFlights();

} 