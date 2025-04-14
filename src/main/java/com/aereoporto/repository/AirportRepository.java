package com.aereoporto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.aereoporto.model.Airport;

public interface AirportRepository extends JpaRepository<Airport,Integer>,JpaSpecificationExecutor<Airport> {
    Optional<Airport> findById(Integer id);
    void deleteById(Integer id);
}
