package com.examtdd.examtdd.service;

import com.examtdd.examtdd.model.Car;
import com.examtdd.examtdd.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarRentalServiceTest {

  @Mock
  private CarRepository carRepository;

  @InjectMocks
  private CarRentalService carRentalService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getAllCars_shouldReturnAllCars() {
    // Given
    List<Car> cars = Arrays.asList(
        new Car("ABC123", "Toyota", true),
        new Car("XYZ789", "Honda", false));
    when(carRepository.getAllCars()).thenReturn(cars);

    // When
    List<Car> result = carRentalService.getAllCars();

    // Then
    assertEquals(2, result.size());
    verify(carRepository, times(1)).getAllCars();
  }

}