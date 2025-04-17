
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

  @Test
  void rentCar_availableCar_shouldReturnTrue() {
    // Given
    Car car = new Car("ABC123", "Toyota", true);
    when(carRepository.findByRegistrationNumber("ABC123")).thenReturn(Optional.of(car));

    // When
    boolean result = carRentalService.rentCar("ABC123");

    // Then
    assertTrue(result);
    assertFalse(car.isAvailable());
    verify(carRepository, times(1)).updateCar(car);
  }

  @Test
  void rentCar_unavailableCar_shouldReturnFalse() {
    // Given
    Car car = new Car("XYZ789", "Honda", false);
    when(carRepository.findByRegistrationNumber("XYZ789")).thenReturn(Optional.of(car));

    // When
    boolean result = carRentalService.rentCar("XYZ789");

    // Then
    assertFalse(result);
    verify(carRepository, never()).updateCar(any());
  }

  @Test
  void rentCar_nonExistingCar_shouldReturnFalse() {
    // Given
    when(carRepository.findByRegistrationNumber("NONEXISTING")).thenReturn(Optional.empty());

    // When
    boolean result = carRentalService.rentCar("NONEXISTING");

    // Then
    assertFalse(result);
    verify(carRepository, never()).updateCar(any());
  }

  @Test
  void returnCar_rentedCar_shouldMakeCarAvailable() {
    // Given
    Car car = new Car("XYZ789", "Honda", false);
    when(carRepository.findByRegistrationNumber("XYZ789")).thenReturn(Optional.of(car));

    // When
    carRentalService.returnCar("XYZ789");

    // Then
    assertTrue(car.isAvailable());
    verify(carRepository, times(1)).updateCar(car);
  }

  @Test
  void returnCar_nonExistingCar_shouldDoNothing() {
    // Given
    when(carRepository.findByRegistrationNumber("NONEXISTING")).thenReturn(Optional.empty());

    // When
    carRentalService.returnCar("NONEXISTING");

    // Then
    verify(carRepository, never()).updateCar(any());
  }

  @Test
  void addCar_withUniqueRegistrationNumber_shouldReturnTrue() {
    // Given
    Car newCar = new Car("DEF456", "Nissan", true);
    when(carRepository.findByRegistrationNumber("DEF456")).thenReturn(Optional.empty());

    // When
    boolean result = carRentalService.addCar(newCar);

    // Then
    assertTrue(result);
    verify(carRepository).addCar(newCar);
  }

  @Test
  void addCar_withExistingRegistrationNumber_shouldReturnFalse() {
    // Given
    Car existingCar = new Car("ABC123", "Toyota", true);
    Car newCar = new Car("ABC123", "Nissan", true); // Même numéro d'immatriculation
    when(carRepository.findByRegistrationNumber("ABC123")).thenReturn(Optional.of(existingCar));

    // When
    boolean result = carRentalService.addCar(newCar);

    // Then
    assertFalse(result);
    verify(carRepository, never()).addCar(any(Car.class));
  }

  @Test
  void findCarsByModel_shouldReturnMatchingCars() {
    // Given
    List<Car> allCars = Arrays.asList(
        new Car("ABC123", "Toyota", true),
        new Car("DEF456", "Honda", true),
        new Car("GHI789", "Toyota", false));
    when(carRepository.getAllCars()).thenReturn(allCars);

    // When
    List<Car> toyotaCars = carRentalService.findCarsByModel("Toyota");

    // Then
    assertEquals(2, toyotaCars.size());
    assertTrue(toyotaCars.stream().allMatch(car -> car.getModel().equals("Toyota")));
  }

  @Test
  void findCarsByModel_noMatchingCars_shouldReturnEmptyList() {
    // Given
    List<Car> allCars = Arrays.asList(
        new Car("ABC123", "Toyota", true),
        new Car("DEF456", "Honda", true));
    when(carRepository.getAllCars()).thenReturn(allCars);

    // When
    List<Car> bmwCars = carRentalService.findCarsByModel("BMW");

    // Then
    assertTrue(bmwCars.isEmpty());
  }
}
