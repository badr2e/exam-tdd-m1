package com.examtdd.examtdd.cucumber;

import com.examtdd.examtdd.model.Car;
import com.examtdd.examtdd.repository.CarRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CarRentalSteps {

  @Autowired
  private CucumberSpringConfiguration configuration;

  @Autowired
  private CarRepository carRepository;

  @Autowired
  private TestRestTemplate restTemplate;

  private List<Car> availableCars;
  private Car testCar;
  private String registrationNumber;
  private boolean rentResult;

  @Before
  public void setup() {
    carRepository.getAllCars().clear();
  }

  @Given("des voitures sont disponibles")
  public void desVoituresSontDisponibles() {
    carRepository.addCar(new Car("TEST001", "Toyota", true));
    carRepository.addCar(new Car("TEST002", "Honda", true));
    carRepository.addCar(new Car("TEST003", "Nissan", true));
  }

  @When("je demande la liste des voitures")
  public void jeDemandeLaListeDesVoitures() {
    String url = "http://localhost:" + configuration.getPort() + "/cars";
    ResponseEntity<List<Car>> response = restTemplate.exchange(
        url,
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<List<Car>>() {
        });
    availableCars = response.getBody();
  }

  @Then("toutes les voitures sont affichées")
  public void toutesLesVoituresSontAffichees() {
    assertNotNull(availableCars);
    assertEquals(3, availableCars.size());
  }

  @Given("une voiture est disponible")
  public void uneVoitureEstDisponible() {
    registrationNumber = "CAR123";
    testCar = new Car(registrationNumber, "Tesla", true);
    carRepository.addCar(testCar);
    assertTrue(testCar.isAvailable());
  }

  @When("je loue cette voiture")
  public void jeLoueVoiture() {
    String url = "http://localhost:" + configuration.getPort() + "/cars/rent/" + registrationNumber;
    rentResult = restTemplate.postForObject(url, null, Boolean.class);
  }

  @Then("la voiture n'est plus disponible")
  public void laVoitureNEstPlusDisponible() {
    assertTrue(rentResult);
    Car updatedCar = carRepository.findByRegistrationNumber(registrationNumber).orElseThrow();
    assertFalse(updatedCar.isAvailable());
  }

  @Given("une voiture est louée")
  public void uneVoitureEstLouee() {
    registrationNumber = "RENT456";
    testCar = new Car(registrationNumber, "BMW", false);
    carRepository.addCar(testCar);
    assertFalse(testCar.isAvailable());
  }

  @When("je retourne cette voiture")
  public void jeRetourneCetteVoiture() {
    String url = "http://localhost:" + configuration.getPort() + "/cars/return/" + registrationNumber;
    restTemplate.postForObject(url, null, Void.class);
  }

  @Then("la voiture est marquée comme disponible")
  public void laVoitureEstMarqueeCommeDisponible() {
    Car updatedCar = carRepository.findByRegistrationNumber(registrationNumber).orElseThrow();
    assertTrue(updatedCar.isAvailable());
  }
}