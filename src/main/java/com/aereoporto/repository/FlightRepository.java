package com.aereoporto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.aereoporto.model.Flight;
public interface FlightRepository extends JpaRepository<Flight,Integer>,JpaSpecificationExecutor<Flight> {
    Optional<Flight> findById(Integer id);
    void deleteById(Integer id);
    
} 