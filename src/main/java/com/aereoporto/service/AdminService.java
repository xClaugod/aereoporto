package com.aereoporto.service;

import com.aereoporto.model.Flight;
import com.aereoporto.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    private FlightService flightService;

    @Autowired
    private AirplaneService airplaneService;

    @Autowired
    private AirportService airportService;

    @Autowired
    private FlightRepository flightRepository;

    public String prepareDashboard(Model model) {
        model.addAttribute("flights", flightService.getAllFlights());
        return "admin/dashboard";
    }

    public String prepareNewFlightForm(Model model) {
        model.addAttribute("flight", new Flight());
        model.addAttribute("airports", airportService.getAllAirports());
        model.addAttribute("airplanes", airplaneService.getAllAirplanes());
        return "admin/new-flight";
    }

    public String createFlight(String departureCity, String arrivalCity,
                               LocalDateTime departureTime, LocalDateTime arrivalTime,
                               String idAirplane) {
        if (arrivalTime.isBefore(departureTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Data di arrivo non può essere precedente alla partenza.");
        }

        LocalDate day = departureTime.toLocalDate();
        Date sqlDate = Date.valueOf(day);
        Timestamp dpTime = Timestamp.valueOf(departureTime);
        Timestamp aTime = Timestamp.valueOf(arrivalTime);

        Flight flight = new Flight(null, sqlDate, departureCity, arrivalCity, dpTime, aTime, idAirplane);
        flightService.addFlight(flight);

        return "redirect:/admin/dashboard";
    }

    public String prepareEditFlightForm(int flightId, Model model) {
        Flight flight = flightService.getFlight(flightId);
        model.addAttribute("flight", flight);
        model.addAttribute("airplanes", airplaneService.getAllAirplanes());
        model.addAttribute("airports", airportService.getAllAirports());
        return "admin/edit-flight";
    }

    public String updateFlight(int flightId, String departureCity, String arrivalCity,
                               LocalDateTime departureTime, LocalDateTime arrivalTime,
                               String idAirplane, int passengers, int goods) {
        if (arrivalTime.isBefore(departureTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Data di arrivo non può essere precedente alla partenza.");
        }

        LocalDate day = departureTime.toLocalDate();
        Date sqlDate = Date.valueOf(day);
        Timestamp dpTime = Timestamp.valueOf(departureTime);
        Timestamp aTime = Timestamp.valueOf(arrivalTime);

        Flight flight = flightService.getFlight(flightId);
        flight.setDepartureCity(departureCity);
        flight.setArrivalCity(arrivalCity);
        flight.setDay(sqlDate);
        flight.setDepartureTime(dpTime);
        flight.setArrivalTime(aTime);
        flight.setIdAirplane(idAirplane);
        flight.setPassengers(passengers);
        flight.setGoods(goods);

        flightRepository.save(flight);
        return "redirect:/admin/dashboard";
    }

    public String deleteFlight(int id) {
        flightService.deleteFlight(id);
        return "redirect:/admin/dashboard";
    }

    public String prepareSelectFlightToEdit(Model model) {
        model.addAttribute("flights", flightService.getAllFlights());
        return "admin/select-flight-to-edit";
    }

    public String prepareSelectFlightToDelete(Model model) {
        model.addAttribute("flights", flightService.getAllFlights());
        return "admin/select-flight-to-delete";
    }

    public String prepareUpcomingFlights(Model model) {
        List<Flight> upcoming = flightService.getFlightsFromNowOn();
        model.addAttribute("flights", upcoming);
        return "admin/upcoming-flights";
    }
}
