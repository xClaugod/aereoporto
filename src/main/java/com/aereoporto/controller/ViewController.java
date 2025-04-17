package com.aereoporto.controller;

import com.aereoporto.service.ViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class ViewController {

    @Autowired
    private ViewService viewService;

    @GetMapping("/")
    public String showHomePage(Model model) {
        return viewService.prepareHomePage(model);
    }

    @PostMapping("/search-flights")
    public String searchFlights(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam int numPassengers,
            @RequestParam(required = false) Integer baggageWeight,
            Model model) {

        return viewService.processFlightSearch(from, to, date, numPassengers, baggageWeight, model);
    }

    @PostMapping("/book")
    public String bookFlight(@RequestParam int flightId,
            @RequestParam int numPassengers,
            @RequestParam int baggageWeight,
            Model model) {
        return viewService.bookFlight(flightId, numPassengers, baggageWeight, model);
    }
}
