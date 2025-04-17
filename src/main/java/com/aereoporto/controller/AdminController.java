package com.aereoporto.controller;

import com.aereoporto.model.Flight;
import com.aereoporto.service.AdminService;
import com.aereoporto.service.AirplaneService;
import com.aereoporto.service.AirportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AirplaneService airplaneService;

    @Autowired
    private AirportService airportService;

    @GetMapping("/login-page")
    public String showLoginForm() {
        return "admin/login";
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        return adminService.prepareDashboard(model);
    }

    @GetMapping("/new-flight")
    public String showNewFlightForm(Model model) {
        return adminService.prepareNewFlightForm(model);
    }

    @PostMapping("/new-flight")
    public String createFlight(
            @RequestParam String departureCity,
            @RequestParam String arrivalCity,
            @RequestParam("departureTime") LocalDateTime departureTime,
            @RequestParam("arrivalTime") LocalDateTime arrivalTime,
            @RequestParam String idAirplane,
            Model model) {
        return adminService.createFlight(departureCity, arrivalCity, departureTime, arrivalTime, idAirplane);
    }

    @GetMapping("/edit-flight/{id}")
    public String showEditFlightPage(@PathVariable("id") int flightId, Model model) {
        return adminService.prepareEditFlightForm(flightId, model);
    }

    @PostMapping("/edit-flight/{id}")
    public String updateFlight(@PathVariable("id") int flightId,
                               @RequestParam String departureCity,
                               @RequestParam String arrivalCity,
                               @RequestParam("departureTime") LocalDateTime departureTime,
                               @RequestParam("arrivalTime") LocalDateTime arrivalTime,
                               @RequestParam String idAirplane,
                               @RequestParam int passengers,
                               @RequestParam int goods,
                               Model model) {
        return adminService.updateFlight(flightId, departureCity, arrivalCity, departureTime, arrivalTime, idAirplane, passengers, goods);
    }

    @PostMapping("/delete-flight/{id}")
    public String deleteFlight(@PathVariable int id) {
        return adminService.deleteFlight(id);
    }

    @GetMapping("/select-flight-to-edit")
    public String selectFlightToEdit(Model model) {
        return adminService.prepareSelectFlightToEdit(model);
    }

    @GetMapping("/select-flight-to-delete")
    public String selectFlightToDelete(Model model) {
        return adminService.prepareSelectFlightToDelete(model);
    }

    @GetMapping("/upcoming-flights")
    public String showUpcomingFlights(Model model) {
        return adminService.prepareUpcomingFlights(model);
    }
}
