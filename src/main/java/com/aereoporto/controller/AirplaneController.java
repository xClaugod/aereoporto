package com.aereoporto.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.aereoporto.model.Airplane;
import com.aereoporto.service.AirplaneService;

@RestController
public class AirplaneController {
	@Autowired
	AirplaneService airplaneService;

	@GetMapping("/airplanes")
	public List<Airplane> getAllAirplanes() {
		return airplaneService.getAllAirplanes();
	}

	@GetMapping("/airplanes/{id}")
	public Airplane getAirplane(@PathVariable String id) {
		return airplaneService.getAirplane(id);
	}

	@PostMapping("/airplanes/add")
	public void addAirplane(@RequestBody Airplane airplane) {
		airplaneService.addAirplane(airplane);
	}

	@PutMapping("/airplanes/{id}")
	public void updateAirplane(@PathVariable String id, @RequestBody Airplane airplane) {
		airplaneService.updateAirplane(id, airplane);
	}

	@DeleteMapping("/airplanes/{id}")
	public void deleteAirplane(@PathVariable String id) {
		airplaneService.deleteAirplane(id);
	}

}
