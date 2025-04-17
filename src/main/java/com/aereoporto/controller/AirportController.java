package com.aereoporto.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.aereoporto.model.Airport;
import com.aereoporto.service.AirportService;

@RestController
public class AirportController {
	@Autowired
	AirportService airportService;

	@GetMapping("/airports")
	public List<Airport> getAllAirports() {
		return airportService.getAllAirports();
	}

	@GetMapping("/airports/{id}")
	public Airport getAirport(@PathVariable Integer id) {
		return airportService.getAirport(id);
	}

	@PostMapping("/airports/add")
	public void addFlight(@RequestBody Airport airport) {
		airportService.addAirport(airport);
	}

	@PutMapping("/airports/{id}")
	public void updateFlight(@PathVariable Integer id, @RequestBody Airport airport) {
		airportService.updateAirport(id, airport);
	}

	@DeleteMapping("/airports/{id}")
	public void deleteFlight(@PathVariable Integer id) {
		airportService.deleteAirport(id);
	}
}
