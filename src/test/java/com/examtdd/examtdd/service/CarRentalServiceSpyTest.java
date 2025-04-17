package com.examtdd.examtdd.service;

import com.examtdd.examtdd.model.Car;
import com.examtdd.examtdd.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;

class CarRentalServiceSpyTest {

  @Spy
  private CarRepository carRepository;

  private CarRentalService carRentalService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    carRentalService = new CarRentalService();

    // Injection manuelle du spy
    java.lang.reflect.Field field;
    try {
      field = carRentalService.getClass().getDeclaredField("carRepository");
      field.setAccessible(true);
      field.set(carRentalService, carRepository);
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Ajouter quelques voitures dans le repository
    carRepository.addCar(new Car("ABC123", "Toyota", true));
    carRepository.addCar(new Car("XYZ789", "Honda", false));
  }

  @Test
  void rentCar_shouldCallFindByRegistrationNumberAndUpdateCar() {
    // When
    carRentalService.rentCar("ABC123");

    // Then
    verify(carRepository).findByRegistrationNumber("ABC123");
    verify(carRepository).updateCar(any(Car.class));
  }

  @Test
  void returnCar_shouldCallFindByRegistrationNumberAndUpdateCar() {
    // Given
    doReturn(Optional.of(new Car("XYZ789", "Honda", false))).when(carRepository).findByRegistrationNumber("XYZ789");

    // When
    carRentalService.returnCar("XYZ789");

    // Then
    verify(carRepository).findByRegistrationNumber("XYZ789");
    verify(carRepository).updateCar(any(Car.class));
  }
}