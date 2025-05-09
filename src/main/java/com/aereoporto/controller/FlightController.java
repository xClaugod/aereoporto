package com.aereoporto.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.aereoporto.model.Flight;
import com.aereoporto.service.FlightService;

@RestController
public class FlightController {
	@Autowired
	FlightService flightService;

	@GetMapping("/flights")
	public List<Flight> getAllFlights() {
		return flightService.getAllFlights();
	}

	@GetMapping("/flights/{id}")
	public Flight getFlight(@PathVariable Integer id) {
		return flightService.getFlight(id);
	}

	@PostMapping("/flights/add")
	public void addFlight(@RequestBody Flight flight) {
		flightService.addFlight(flight);
	}

	@PutMapping("/flights/{id}")
	public void updateFlight(@PathVariable Integer id, @RequestBody Flight flight) {
		flightService.updateFlight(id, flight);
	}

	@DeleteMapping("/flights/{id}")
	public void deleteFlight(@PathVariable Integer id) {
		flightService.deleteFlight(id);
	}
}
