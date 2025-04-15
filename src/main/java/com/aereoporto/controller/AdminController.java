package com.aereoporto.controller;

import com.aereoporto.model.Flight;
import com.aereoporto.repository.FlightRepository;
import com.aereoporto.model.Airplane;
import com.aereoporto.model.Airport;
import com.aereoporto.service.FlightService;
import com.aereoporto.service.AirplaneService;
import com.aereoporto.service.AirportService;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final FlightRepository flightRepository;

    @Autowired
    private FlightService flightService;

    @Autowired
    private AirplaneService airplaneService;

    @Autowired
    private AirportService airportService;

    AdminController(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @GetMapping("/login-page")
    public String showLoginForm() {
        return "admin/login"; // login.html
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        model.addAttribute("flights", flightService.getAllFlights());
        return "admin/dashboard";
    }

    @GetMapping("/new-flight")
    public String showNewFlightForm(Model model) {
        model.addAttribute("flight", new Flight()); // Assicurati di avere un oggetto da bindare
        model.addAttribute("airports", airportService.getAllAirports());
        model.addAttribute("airplanes", airplaneService.getAllAirplanes());
        return "admin/new-flight";
    }

    @PostMapping("/new-flight")
    public String createFlight(
            @RequestParam String departureCity,
            @RequestParam String arrivalCity,
            @RequestParam String departureTime,
            @RequestParam int passengers,
            @RequestParam int goods,
            @RequestParam String idAirplane,
            Model model) {

        // Conversione da stringa (datetime-local) a Timestamp
        LocalDateTime departureDateTime = LocalDateTime.parse(departureTime);
        Timestamp departureTimestamp = Timestamp.valueOf(departureDateTime);

        // Calcolo la sola data
        Date day = Date.valueOf(departureDateTime.toLocalDate());

        // ArrivalTime fittizio 2h dopo
        Timestamp arrivalTimestamp = Timestamp.valueOf(departureDateTime.plusHours(2));

        // Creo il volo
        Flight flight = new Flight(
                null,
                day,
                departureCity,
                arrivalCity,
                departureTimestamp,
                arrivalTimestamp,
                idAirplane,
                passengers,
                goods);

        // Salvataggio
        flightService.addFlight(flight);

        return "redirect:/admin/dashboard";
    }

    @GetMapping("/edit-flight/{id}")
    public String showEditFlightPage(@PathVariable("id") int flightId, Model model) {
        Flight flight = flightService.getFlight(flightId);
        List<Airplane> airplanes = airplaneService.getAllAirplanes();
        List<Airport> airports = airportService.getAllAirports();
        model.addAttribute("flight", flight);
        model.addAttribute("airplanes", airplanes);
        model.addAttribute("airports", airports);

        return "admin/edit-flight"; // Nome del file HTML
    }

    @PostMapping("/edit-flight/{id}")
    public String updateFlight(@PathVariable("id") int flightId,
            @RequestParam String departureCity,
            @RequestParam String arrivalCity,
            @RequestParam("departureTime") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime departureTime,
            @RequestParam String idAirplane,
            @RequestParam int passengers,
            @RequestParam int goods) {
        Timestamp timestamp = Timestamp.valueOf(departureTime);
        Flight flight = flightService.getFlight(flightId);
        flight.setDepartureCity(departureCity);
        flight.setArrivalCity(arrivalCity);
        flight.setDepartureTime(timestamp);
        flight.setIdAirplane(idAirplane);
        flight.setPassengers(passengers);
        flight.setGoods(goods);

        flightRepository.save(flight);
        return "redirect:/admin/dashboard"; // Redireziona alla dashboard o alla lista dei voli
    }

    @PostMapping("/delete-flight/{id}")
    public String deleteFlight(@PathVariable int id) {
        flightService.deleteFlight(id);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/select-flight-to-edit")
    public String selectFlightToEdit(Model model) {
        model.addAttribute("flights", flightService.getAllFlights());
        return "admin/select-flight-to-edit";
    }

    @GetMapping("/select-flight-to-delete")
    public String selectFlightToDelete(Model model) {
        model.addAttribute("flights", flightService.getAllFlights());
        return "admin/select-flight-to-delete";
    }

    @GetMapping("/upcoming-flights")
    public String showUpcomingFlights(Model model) {
        List<Flight> upcoming = flightService.getFlightsFromNowOn();
        model.addAttribute("flights", upcoming);
        return "admin/upcoming-flights";
    }

}
