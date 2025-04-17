package com.examtdd.examtdd.repository;

import com.examtdd.examtdd.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

}