package com.aereoporto;

import com.aereoporto.model.Airport;
import com.aereoporto.repository.AirportRepository;
import com.aereoporto.service.AirportService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AirportServiceTest {

    private AirportRepository airportRepository;
    private AirportService airportService;

    @BeforeEach
    void setUp() {
        airportRepository = mock(AirportRepository.class);
        airportService = new AirportService();
        airportService.airportRepository = airportRepository;
    }

    @Test
    void getAllAirports_shouldReturnAllAirports() {
        Airport a1 = new Airport(1, "Catania", "Italia", 2);
        Airport a2 = new Airport(2, "Roma", "Italia", 3);
        when(airportRepository.findAll()).thenReturn(Arrays.asList(a1, a2));

        List<Airport> result = airportService.getAllAirports();

        assertEquals(2, result.size());
        verify(airportRepository).findAll();
    }

    @Test
    void getAirport_shouldReturnAirport_whenExists() {
        Airport a = new Airport(1, "Milano", "Italia", 4);
        when(airportRepository.findById(1)).thenReturn(Optional.of(a));

        Airport result = airportService.getAirport(1);

        assertEquals("Milano", result.getCity());
        assertEquals(4, result.getNumRunway());
    }

    @Test
    void getAirport_shouldThrowException_whenNotFound() {
        when(airportRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            airportService.getAirport(1);
        });

        assertEquals("Airport not found", exception.getMessage());
    }

    @Test
    void addAirport_shouldCallRepositorySave() {
        Airport a = new Airport(1, "Napoli", "Italia", 2);
        airportService.addAirport(a);

        verify(airportRepository).save(a);
    }

    @Test
    void updateAirport_shouldUpdateAirport_whenExists() {
        Airport existing = new Airport(1, "Torino", "Italia", 1);
        Airport updated = new Airport(1, "Torino", "Italia", 3); // updated runway

        when(airportRepository.findById(1)).thenReturn(Optional.of(existing));
        when(airportRepository.save(any(Airport.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Airport result = airportService.updateAirport(1, updated);

        assertEquals(3, result.getNumRunway());
        verify(airportRepository).save(existing);
    }

    @Test
    void updateAirport_shouldThrowException_whenNotFound() {
        Airport updated = new Airport(1, "Bari", "Italia", 2);
        when(airportRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            airportService.updateAirport(1, updated);
        });

        assertEquals("Airport not found", exception.getMessage());
    }

    @Test
    void deleteAirport_shouldCallDelete_whenExists() {
        when(airportRepository.existsById(1)).thenReturn(true);

        airportService.deleteAirport(1);

        verify(airportRepository).deleteById(1);
    }

    @Test
    void deleteAirport_shouldThrowException_whenNotExists() {
        when(airportRepository.existsById(1)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            airportService.deleteAirport(1);
        });

        assertEquals("Airport not found", exception.getMessage());
    }
}
