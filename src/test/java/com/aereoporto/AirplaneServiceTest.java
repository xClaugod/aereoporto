package com.aereoporto;

import com.aereoporto.model.Airplane;
import com.aereoporto.repository.AirplaneRepository;
import com.aereoporto.service.AirplaneService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AirplaneServiceTest {

    private AirplaneRepository airplaneRepository;
    private AirplaneService airplaneService;

    @BeforeEach
    void setUp() {
        airplaneRepository = mock(AirplaneRepository.class);
        airplaneService = new AirplaneService();
        airplaneService.airplaneRepository = airplaneRepository;
    }

    @Test
    void getAllAirplanes_shouldReturnListOfAirplanes() {
        Airplane a1 = new Airplane("A1", 100, 2000);
        Airplane a2 = new Airplane("A2", 150, 3000);
        when(airplaneRepository.findAll()).thenReturn(Arrays.asList(a1, a2));

        List<Airplane> result = airplaneService.getAllAirplanes();

        assertEquals(2, result.size());
        verify(airplaneRepository, times(1)).findAll();
    }

    @Test
    void getAirplane_shouldReturnAirplane_whenFound() {
        Airplane a = new Airplane("A1", 120, 2500);
        when(airplaneRepository.findById("A1")).thenReturn(Optional.of(a));

        Airplane result = airplaneService.getAirplane("A1");

        assertEquals(120, result.getNumPassengers());
        assertEquals(2500, result.getQtyGoods());
    }

    @Test
    void getAirplane_shouldThrowException_whenNotFound() {
        when(airplaneRepository.findById("A1")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            airplaneService.getAirplane("A1");
        });

        assertEquals("Airplane not found", exception.getMessage());
    }

    @Test
    void addAirplane_shouldCallRepositorySave() {
        Airplane a = new Airplane("A1", 130, 2600);
        airplaneService.addAirplane(a);

        verify(airplaneRepository, times(1)).save(a);
    }

    @Test
    void updateAirplane_shouldUpdateAirplane_whenFound() {
        Airplane existing = new Airplane("A1", 100, 2000);
        Airplane updated = new Airplane("A1", 180, 3500);

        when(airplaneRepository.findById("A1")).thenReturn(Optional.of(existing));
        when(airplaneRepository.save(any(Airplane.class))).thenAnswer(i -> i.getArgument(0));

        Airplane result = airplaneService.updateAirplane("A1", updated);

        assertEquals(180, result.getNumPassengers());
        assertEquals(3500, result.getQtyGoods());
        verify(airplaneRepository).save(existing);
    }

    @Test
    void updateAirplane_shouldThrowException_whenNotFound() {
        Airplane updated = new Airplane("A1", 180, 3500);
        when(airplaneRepository.findById("A1")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            airplaneService.updateAirplane("A1", updated);
        });

        assertEquals("Airplane not found", exception.getMessage());
    }

    @Test
    void deleteAirplane_shouldDelete_whenExists() {
        when(airplaneRepository.existsById("A1")).thenReturn(true);

        airplaneService.deleteAirplane("A1");

        verify(airplaneRepository).deleteById("A1");
    }

    @Test
    void deleteAirplane_shouldThrowException_whenNotFound() {
        when(airplaneRepository.existsById("A1")).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            airplaneService.deleteAirplane("A1");
        });

        assertEquals("Airplane not found", exception.getMessage());
    }
}
