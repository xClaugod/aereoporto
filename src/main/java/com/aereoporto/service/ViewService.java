package com.aereoporto.service;

import com.aereoporto.model.Airplane;
import com.aereoporto.model.Flight;
import com.aereoporto.model.FlightWithAvailability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ViewService {

    @Autowired
    private AirportService airportService;

    @Autowired
    private FlightService flightService;

    @Autowired
    private AirplaneService airplaneService;

    @Autowired
    private BookingService bookingService;

    public String prepareHomePage(Model model) {
        model.addAttribute("airports", airportService.getAllAirports());
        return "home";
    }

    public String processFlightSearch(String from, String to, LocalDate date, int numPassengers, Integer baggageWeight, Model model) {
        final int effectiveBaggageWeight = (baggageWeight != null) ? baggageWeight : 0;

        List<Flight> allFlights = flightService.getAllFlights();
        Timestamp timestamp = Timestamp.valueOf(date.atStartOfDay());

        List<FlightWithAvailability> filtered = allFlights.stream()
                .filter(f -> f.getDepartureCity().equals(from)
                        && f.getArrivalCity().equals(to)
                        && f.getDepartureTime().after(timestamp))
                .map(f -> {
                    Airplane airplane = airplaneService.getAirplane(f.getIdAirplane());
                    int availableSeats = airplane.getNumPassengers() - f.getPassengers() - numPassengers;
                    int availableWeight = airplane.getQtyGoods() - f.getGoods() - effectiveBaggageWeight;

                    if (availableSeats > 0 && availableWeight >= 0) {
                        return new FlightWithAvailability(f, availableSeats + numPassengers,
                                availableWeight + effectiveBaggageWeight);
                    } else {
                        return null;
                    }
                })
                .filter(f -> f != null)
                .collect(Collectors.toList());

        model.addAttribute("flightsWithAvailability", filtered);
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        model.addAttribute("numPassengers", numPassengers);
        model.addAttribute("baggageWeight", effectiveBaggageWeight);
        model.addAttribute("noFlights", filtered.isEmpty());

        return "results";
    }

    public String bookFlight(int flightId, int numPassengers, int baggageWeight, Model model) {
        try {
            bookingService.bookFlight(flightId, numPassengers, baggageWeight);
            model.addAttribute("message", "La tua prenotazione è stata elaborata.");
            return "bookConfirmation";
        } catch (OptimisticLockingFailureException e) {
            model.addAttribute("error", "Il volo è stato prenotato da qualcun altro. Riprova.");
            return "results";
        } catch (Exception e) {
            model.addAttribute("error", "Volo non trovato.");
            return "results";
        }
    }
}
