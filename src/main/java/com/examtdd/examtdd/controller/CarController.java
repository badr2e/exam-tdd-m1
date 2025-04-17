package com.examtdd.examtdd.controller;

import com.examtdd.examtdd.model.Car;
import com.examtdd.examtdd.service.CarRentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarController {

  @Autowired
  private CarRentalService carRentalService;

  @GetMapping
  public List<Car> getAllCars() {
    return carRentalService.getAllCars();
  }

  @PostMapping("/rent/{registrationNumber}")
  public boolean rentCar(@PathVariable String registrationNumber) {
    return carRentalService.rentCar(registrationNumber);
  }

  @PostMapping("/return/{registrationNumber}")
  public void returnCar(@PathVariable String registrationNumber) {
    carRentalService.returnCar(registrationNumber);
  }
}