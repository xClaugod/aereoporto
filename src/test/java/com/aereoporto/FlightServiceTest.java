package com.aereoporto;

import com.aereoporto.model.Flight;
import com.aereoporto.repository.FlightRepository;
import com.aereoporto.service.FlightService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FlightServiceTest {

    private FlightRepository flightRepository;
    private FlightService flightService;

    @BeforeEach
    void setUp() {
        flightRepository = mock(FlightRepository.class);
        flightService = new FlightService();
        flightService.flightRepository = flightRepository;
    }

    @Test
    void getAllFlights_shouldReturnAllFlights() {
        List<Flight> mockFlights = Arrays.asList(new Flight(), new Flight());
        when(flightRepository.findAll()).thenReturn(mockFlights);

        List<Flight> result = flightService.getAllFlights();

        assertEquals(2, result.size());
        verify(flightRepository).findAll();
    }

    @Test
    void getFlight_shouldReturnFlight_whenFound() {
        Flight flight = new Flight();
        flight.setId(1);

        when(flightRepository.findById(1)).thenReturn(Optional.of(flight));

        Flight result = flightService.getFlight(1);

        assertEquals(1, result.getId());
        verify(flightRepository).findById(1);
    }

    @Test
    void getFlight_shouldThrowException_whenNotFound() {
        when(flightRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            flightService.getFlight(1);
        });

        assertEquals("Flight not found", exception.getMessage());
    }

    @Test
    void addFlight_shouldSaveFlight() {
        Flight flight = new Flight();

        flightService.addFlight(flight);

        verify(flightRepository).save(flight);
    }

    @Test
    void updateFlight_shouldUpdateFlight_whenFound() {
        Flight existing = new Flight();
        existing.setId(1);

        Flight updated = new Flight();
        updated.setArrivalCity("Catania");
        updated.setDepartureCity("Roma");
        updated.setIdAirplane("AZ123");
        updated.setPassengers(150);

        when(flightRepository.findById(1)).thenReturn(Optional.of(existing));
        when(flightRepository.save(any(Flight.class))).thenAnswer(inv -> inv.getArgument(0));

        Flight result = flightService.updateFlight(1, updated);

        assertEquals("Catania", result.getArrivalCity());
        assertEquals("Roma", result.getDepartureCity());
        assertEquals("AZ123", result.getIdAirplane());
        assertEquals(150, result.getPassengers());

        verify(flightRepository).save(existing);
    }

    @Test
    void updateFlight_shouldThrowException_whenNotFound() {
        when(flightRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            flightService.updateFlight(1, new Flight());
        });

        assertEquals("Flight not found", exception.getMessage());
    }

    @Test
    void deleteFlight_shouldDeleteFlight_whenExists() {
        when(flightRepository.existsById(1)).thenReturn(true);

        flightService.deleteFlight(1);

        verify(flightRepository).deleteById(1);
    }

    @Test
    void deleteFlight_shouldThrowException_whenNotExists() {
        when(flightRepository.existsById(1)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            flightService.deleteFlight(1);
        });

        assertEquals("Flight not found", exception.getMessage());
    }

    @Test
    void getFlightsFromNowOn_shouldReturnUpcomingFlights() {
        List<Flight> mockFlights = List.of(new Flight(), new Flight());
        when(flightRepository.findUpcomingFlights()).thenReturn(mockFlights);

        List<Flight> result = flightService.getFlightsFromNowOn();

        assertEquals(2, result.size());
        verify(flightRepository).findUpcomingFlights();
    }
}
