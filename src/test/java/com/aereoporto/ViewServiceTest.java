package com.aereoporto;

import com.aereoporto.model.*;
import com.aereoporto.service.AirplaneService;
import com.aereoporto.service.AirportService;
import com.aereoporto.service.BookingService;
import com.aereoporto.service.FlightService;
import com.aereoporto.service.ViewService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.ui.Model;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ViewServiceTest {

    private ViewService viewService;
    private AirportService airportService;
    private FlightService flightService;
    private AirplaneService airplaneService;
    private BookingService bookingService;
    private Model model;

    @BeforeEach
    void setUp() {
        airportService = mock(AirportService.class);
        flightService = mock(FlightService.class);
        airplaneService = mock(AirplaneService.class);
        bookingService = mock(BookingService.class);
        model = mock(Model.class);

        viewService = new ViewService();
        viewService.airportService = airportService;
        viewService.flightService = flightService;
        viewService.airplaneService = airplaneService;
        viewService.bookingService = bookingService;
    }

    @Test
    void prepareHomePage_shouldPopulateModelAndReturnHome() {
        when(airportService.getAllAirports()).thenReturn(List.of(new Airport()));

        String result = viewService.prepareHomePage(model);

        verify(model).addAttribute(eq("airports"), anyList());
        assertEquals("home", result);
    }

    @Test
    void processFlightSearch_shouldFilterAndReturnResults() {
        Flight flight = new Flight();
        flight.setDepartureCity("Roma");
        flight.setArrivalCity("Catania");
        flight.setDepartureTime(Timestamp.valueOf(LocalDate.now().plusDays(1).atTime(10, 0)));
        flight.setPassengers(50);
        flight.setGoods(200);
        flight.setIdAirplane("ABC123");

        Airplane airplane = new Airplane();
        airplane.setNumPassengers(100);
        airplane.setQtyGoods(500);

        when(flightService.getAllFlights()).thenReturn(List.of(flight));
        when(airplaneService.getAirplane("ABC123")).thenReturn(airplane);

        String result = viewService.processFlightSearch("Roma", "Catania", LocalDate.now().plusDays(1), 2, 20, model);

        verify(model).addAttribute(eq("flightsWithAvailability"), anyList());
        verify(model).addAttribute("from", "Roma");
        verify(model).addAttribute("to", "Catania");
        verify(model).addAttribute("numPassengers", 2);
        verify(model).addAttribute("baggageWeight", 20);
        verify(model).addAttribute(eq("noFlights"), eq(false));
        assertEquals("results", result);
    }

    @Test
    void processFlightSearch_shouldHandleNoAvailability() {
        Flight flight = new Flight();
        flight.setDepartureCity("Roma");
        flight.setArrivalCity("Catania");
        flight.setDepartureTime(Timestamp.valueOf(LocalDate.now().plusDays(1).atTime(10, 0)));
        flight.setPassengers(99);
        flight.setGoods(490);
        flight.setIdAirplane("DEF456");

        Airplane airplane = new Airplane();
        airplane.setNumPassengers(100);
        airplane.setQtyGoods(500);

        when(flightService.getAllFlights()).thenReturn(List.of(flight));
        when(airplaneService.getAirplane("DEF456")).thenReturn(airplane);

        String result = viewService.processFlightSearch("Roma", "Catania", LocalDate.now().plusDays(1), 2, 20, model);

        verify(model).addAttribute(eq("flightsWithAvailability"), eq(List.of()));
        verify(model).addAttribute("noFlights", true);
        assertEquals("results", result);
    }

    @Test
    void bookFlight_shouldReturnConfirmation_whenSuccessful() throws Exception {
        doNothing().when(bookingService).bookFlight(1, 2, 10);

        String result = viewService.bookFlight(1, 2, 10, model);

        verify(bookingService).bookFlight(1, 2, 10);
        verify(model).addAttribute("message", "Booking confirmed!");
        assertEquals("bookConfirmation", result);
    }

    @Test
    void bookFlight_shouldReturnErrorOnOptimisticLockingFailure() throws Exception {
        doThrow(new OptimisticLockingFailureException("conflict"))
                .when(bookingService).bookFlight(1, 2, 10);

        String result = viewService.bookFlight(1, 2, 10, model);

        verify(model).addAttribute("error", "Someone else booked the flight :( Retry)");
        assertEquals("results", result);
    }

    @Test
    void bookFlight_shouldReturnErrorOnGenericException() throws Exception {
        doThrow(new Exception("Flight not found"))
                .when(bookingService).bookFlight(1, 2, 10);

        String result = viewService.bookFlight(1, 2, 10, model);

        verify(model).addAttribute("error", "Flight not found");
        assertEquals("results", result);
    }
}
