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

}