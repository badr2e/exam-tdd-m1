package com.examtdd.examtdd.controller;

import com.examtdd.examtdd.model.Car;
import com.examtdd.examtdd.service.CarRentalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarController.class)
class CarControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CarRentalService carRentalService;

  @Test
  void getAllCars_shouldReturnAllCars() throws Exception {
    // Given
    when(carRentalService.getAllCars()).thenReturn(Arrays.asList(
        new Car("ABC123", "Toyota", true),
        new Car("XYZ789", "Honda", false)));

    // When & Then
    mockMvc.perform(get("/cars"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].registrationNumber").value("ABC123"))
        .andExpect(jsonPath("$[0].model").value("Toyota"))
        .andExpect(jsonPath("$[0].available").value(true))
        .andExpect(jsonPath("$[1].registrationNumber").value("XYZ789"))
        .andExpect(jsonPath("$[1].model").value("Honda"))
        .andExpect(jsonPath("$[1].available").value(false));

    verify(carRentalService, times(1)).getAllCars();
  }

  @Test
  void rentCar_availableCar_shouldReturnTrue() throws Exception {
    // Given
    when(carRentalService.rentCar("ABC123")).thenReturn(true);

    // When & Then
    mockMvc.perform(post("/cars/rent/ABC123"))
        .andExpect(status().isOk())
        .andExpect(content().string("true"));

    verify(carRentalService, times(1)).rentCar("ABC123");
  }

  @Test
  void rentCar_unavailableCar_shouldReturnFalse() throws Exception {
    // Given
    when(carRentalService.rentCar("XYZ789")).thenReturn(false);

    // When & Then
    mockMvc.perform(post("/cars/rent/XYZ789"))
        .andExpect(status().isOk())
        .andExpect(content().string("false"));

    verify(carRentalService, times(1)).rentCar("XYZ789");
  }

  @Test
  void returnCar_shouldCallReturnCarService() throws Exception {
    // Given
    doNothing().when(carRentalService).returnCar("ABC123");

    // When & Then
    mockMvc.perform(post("/cars/return/ABC123"))
        .andExpect(status().isOk());

    verify(carRentalService, times(1)).returnCar("ABC123");
  }
}