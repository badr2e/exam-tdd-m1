package com.examtdd.examtdd.repository;

import com.examtdd.examtdd.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CarRepositoryTest {

  private CarRepository carRepository;

  @BeforeEach
  void setUp() {
    carRepository = new CarRepository();
    carRepository.addCar(new Car("ABC123", "Toyota", true));
    carRepository.addCar(new Car("XYZ789", "Honda", false));
  }

  @Test
  void getAllCars_shouldReturnAllCars() {
    assertEquals(2, carRepository.getAllCars().size());
  }

  @Test
  void findByRegistrationNumber_existingCar_shouldReturnCar() {
    Optional<Car> car = carRepository.findByRegistrationNumber("ABC123");
    assertTrue(car.isPresent());
    assertEquals("Toyota", car.get().getModel());
  }

  @Test
  void findByRegistrationNumber_nonExistingCar_shouldReturnEmpty() {
    Optional<Car> car = carRepository.findByRegistrationNumber("NONEXISTING");
    assertFalse(car.isPresent());
  }

}