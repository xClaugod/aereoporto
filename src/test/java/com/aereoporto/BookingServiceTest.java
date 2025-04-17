package com.aereoporto;

import com.aereoporto.model.Flight;
import com.aereoporto.repository.FlightRepository;
import com.aereoporto.service.BookingService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.OptimisticLockingFailureException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    private FlightRepository flightRepository;
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        flightRepository = mock(FlightRepository.class);
        bookingService = new BookingService();
        bookingService.flightRepository = flightRepository;
    }

    @Test
    void bookFlight_shouldUpdatePassengersAndGoods_whenFlightExists() throws Exception {
        Flight flight = new Flight();
        flight.setId(1);
        flight.setPassengers(100);
        flight.setGoods(500);

        when(flightRepository.findById(1)).thenReturn(Optional.of(flight));
        when(flightRepository.save(any(Flight.class))).thenAnswer(inv -> inv.getArgument(0));

        bookingService.bookFlight(1, 10, 50);

        assertEquals(110, flight.getPassengers());
        assertEquals(550, flight.getGoods());
        verify(flightRepository).save(flight);
    }

    @Test
    void bookFlight_shouldThrowException_whenFlightNotFound() {
        when(flightRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            bookingService.bookFlight(1, 5, 20);
        });

        assertEquals("Flight not found", exception.getMessage());
    }

    @Test
    void bookFlight_shouldThrowException_whenOptimisticLockingFailureOccurs() {
        Flight flight = new Flight();
        flight.setId(1);
        flight.setPassengers(50);
        flight.setGoods(300);

        when(flightRepository.findById(1)).thenReturn(Optional.of(flight));
        when(flightRepository.save(any(Flight.class)))
            .thenThrow(new OptimisticLockingFailureException("Concurrency conflict"));

        Exception exception = assertThrows(Exception.class, () -> {
            bookingService.bookFlight(1, 10, 25);
        });

        assertEquals("Concurrency error. Retry", exception.getMessage());
    }
}
