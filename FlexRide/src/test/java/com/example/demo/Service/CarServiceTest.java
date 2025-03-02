package com.example.demo.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.entity.CarEntity;
import com.example.demo.repository.BookingRepo;
import com.example.demo.repository.CarRepo;
import com.example.demo.service.CarService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock
    private CarRepo carRepo;

    @Mock
    private BookingRepo bookingRepo;

    @InjectMocks
    private CarService carService;

    private CarEntity car;

    @BeforeEach
    void setUp() {
        car = new CarEntity();
        car.setId(1);
        car.setName("Tesla Model S");
        car.setModel("2023");
        car.setCompany("Tesla");
        car.setPricePerDay(200);
        car.setBooked(false);
    }

    @Test
    void testSaveCar() {
        when(carRepo.save(any(CarEntity.class))).thenReturn(car);

        carService.saveCar(car);

        verify(carRepo, times(1)).save(car);
    }

    @Test
    void testGetAllCars() {
        when(carRepo.findAll()).thenReturn(List.of(car));

        List<CarEntity> cars = carService.getAllCars();

        assertFalse(cars.isEmpty());
        assertEquals(1, cars.size());
        verify(carRepo, times(1)).findAll();
    }

    @Test
    void testGetCarById_Found() {
        when(carRepo.findById(1)).thenReturn(Optional.of(car));

        CarEntity foundCar = carService.getCarById(1);

        assertNotNull(foundCar);
        assertEquals("Tesla Model S", foundCar.getName());
        verify(carRepo, times(1)).findById(1);
    }

    @Test
    void testGetCarById_NotFound() {
        when(carRepo.findById(2)).thenReturn(Optional.empty());

        CarEntity foundCar = carService.getCarById(2);

        assertNull(foundCar);
        verify(carRepo, times(1)).findById(2);
    }

    @Test
    void testDeleteCar_Success() {
        when(carRepo.findById(1)).thenReturn(Optional.of(car));
        when(bookingRepo.findByCar(car)).thenReturn(Collections.emptyList());

        carService.deleteCar(1);

        verify(carRepo, times(1)).delete(car);
    }

    @Test
    void testDeleteCar_Failure_CarNotFound() {
        when(carRepo.findById(2)).thenReturn(Optional.empty());

        carService.deleteCar(2);

        verify(carRepo, never()).delete(any(CarEntity.class));
    }

}
