package com.examtdd.examtdd.service;

import com.examtdd.examtdd.model.Car;
import com.examtdd.examtdd.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarRentalService {

  @Autowired
  private CarRepository carRepository;

  public List<Car> getAllCars() {
    return carRepository.getAllCars();
  }

  public boolean rentCar(String registrationNumber) {
    Optional<Car> car = carRepository.findByRegistrationNumber(registrationNumber);
    if (car.isPresent() && car.get().isAvailable()) {
      car.get().setAvailable(false);
      carRepository.updateCar(car.get());
      return true;
    }
    return false;
  }

  public void returnCar(String registrationNumber) {
    Optional<Car> car = carRepository.findByRegistrationNumber(registrationNumber);
    car.ifPresent(c -> {
      c.setAvailable(true);
      carRepository.updateCar(c);
    });
  }

  public boolean addCar(Car car) {
    // Vérifier si une voiture avec le même numéro d'immatriculation existe déjà
    Optional<Car> existingCar = carRepository.findByRegistrationNumber(car.getRegistrationNumber());
    if (existingCar.isPresent()) {
      return false; // Impossible d'ajouter une voiture avec un numéro déjà existant
    }

    // Ajouter la nouvelle voiture
    carRepository.addCar(car);
    return true;
  }

  public List<Car> findCarsByModel(String model) {
    // Récupérer toutes les voitures et filtrer par modèle
    return carRepository.getAllCars().stream()
        .filter(car -> car.getModel().equals(model))
        .collect(Collectors.toList());
  }
}