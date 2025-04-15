package com.aereoporto.controller;

import com.aereoporto.model.Airplane;
import com.aereoporto.model.Airport;
import com.aereoporto.model.Flight;
import com.aereoporto.model.FlightWithAvailability;
import com.aereoporto.service.AirplaneService;
import com.aereoporto.service.AirportService;
import com.aereoporto.service.BookingService;
import com.aereoporto.service.FlightService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ViewController {

    @Autowired
    private AirportService airportService;

    @Autowired
    private FlightService flightService;

    @Autowired
    private AirplaneService airplaneService;

    @GetMapping("/")
    public String showHomePage(Model model) {
        model.addAttribute("airports", airportService.getAllAirports());
        return "home";
    }

    @PostMapping("/")
    public String searchFlights(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam int numPassengers,
            @RequestParam int baggageWeight,
            Model model) {

        List<Flight> allFlights = flightService.getAllFlights();

        Timestamp timestamp = Timestamp.valueOf(date.atStartOfDay());

        List<FlightWithAvailability> filtered = allFlights.stream()
        .filter(f -> f.getDepartureCity().equals(from)
                && f.getArrivalCity().equals(to)
                && f.getDepartureTime().after(timestamp))
        .map(f -> {
            Airplane airplane = airplaneService.getAirplane(f.getIdAirplane());
            int availableSeats = airplane.getNumPassengers() - f.getPassengers() - numPassengers ;
            int availableWeight = airplane.getQtyGoods() - f.getGoods() - baggageWeight;

            if (availableSeats > 0 && availableWeight >= 0) {
                return new FlightWithAvailability(f, availableSeats + numPassengers, availableWeight + baggageWeight);
            } else {
                return null; // Non includere il volo se non ha posti disponibili
            }
        })
        .filter(f -> f != null) // Rimuovi i voli che sono nulli
        .collect(Collectors.toList());
        
        model.addAttribute("flightsWithAvailability", filtered);
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        model.addAttribute("numPassengers", numPassengers); // Aggiungi il peso del bagaglio al modello
        model.addAttribute("baggageWeight", baggageWeight); // Aggiungi il peso del bagaglio al modello

        if (filtered.isEmpty()) model.addAttribute("noFlights", true);
        else  model.addAttribute("noFlights", false);

        return "results";
    }

    
    @Autowired
    private BookingService bookingService;

    // Endpoint per prenotare un volo
    @PostMapping("/book")
    public String bookFlight(@RequestParam int flightId, @RequestParam int numPassengers, @RequestParam int baggageWeight, Model model) {
        try {
            bookingService.bookFlight(flightId, numPassengers, baggageWeight); // Prenotazione asincrona
            model.addAttribute("message", "La tua prenotazione è stata elaborata.");
            return "bookConfirmation";
        } catch (OptimisticLockingFailureException e) {
            model.addAttribute("error", "Il volo è stato prenotato da qualcun altro. Riprova.");
            return "results"; 
        }catch (Exception e) {
            model.addAttribute("error", "Volo non trovato.");
            return "results"; 
        }
    }

}
