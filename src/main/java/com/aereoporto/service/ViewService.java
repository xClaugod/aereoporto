package com.aereoporto.service;

import com.aereoporto.model.Airplane;
import com.aereoporto.model.Flight;
import com.aereoporto.model.FlightWithAvailability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
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

    /**
     * Prepare the home page view.
     *
     * @param model the model to populate.
     * @return the name of the view to render.
     */
    public String prepareHomePage(Model model) {
        model.addAttribute("airports", airportService.getAllAirports());
        return "home";
    }

    /**
     * Prepare the results view.
     *
     * @param from          the departure city.
     * @param to            the arrival city.
     * @param date          the date of the flight.
     * @param numPassengers the number of passengers.
     * @param baggageWeight the baggage weight to book.
     * @param model         the model to populate.
     * @return the name of the view to render.
     */
    public String processFlightSearch(String from, String to, LocalDate date, int numPassengers, Integer baggageWeight,
            Model model) {
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

    /**
     * Processes the booking of a flight.
     * 
     * @param flightId      the id of the flight to book.
     * @param numPassengers the number of passengers.
     * @param baggageWeight the baggage weight to book.
     * @param model         the model to populate.
     * @return the name of the view to render.
     */
    public String bookFlight(int flightId, int numPassengers, int baggageWeight, Model model) {
        try {
            bookingService.bookFlight(flightId, numPassengers, baggageWeight);
            model.addAttribute("message", "Booking confirmed!");
            return "bookConfirmation";
        } catch (OptimisticLockingFailureException e) {
            model.addAttribute("error", "Someone else booked the flight :( Retry)");
            return "results";
        } catch (Exception e) {
            model.addAttribute("error", "Flight not found");
            return "results";
        }
    }
}
