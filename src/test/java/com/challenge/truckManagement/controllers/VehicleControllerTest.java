package com.challenge.truckManagement.controllers;

import com.challenge.truckManagement.DTOs.VehicleDTO;
import com.challenge.truckManagement.entities.Vehicle;
import com.challenge.truckManagement.services.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;;

@WebMvcTest(VehicleController.class)
class VehicleControllerTest {

    @MockBean
    private VehicleService vehicleService;

    @Autowired
    private MockMvc mvc;

    private final String CREATED_VEHICLE_BODY = "";

    private final Vehicle DEFAULT_VEHICLE = new Vehicle("1",
            "Celta",
            2007,
            "Chevrolet",
            "Car",
            "ABC-1234",
            null,
            "Black"
            );
    @BeforeEach
    void setUp() {

    }

    @Test
    void createVehicleShouldWork() throws Exception {
        when(vehicleService.createVehicle(any())).thenReturn(new VehicleDTO(DEFAULT_VEHICLE));
        mvc.perform(post("/vehicles/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CREATED_VEHICLE_BODY)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(CREATED_VEHICLE_BODY));
    }

    @Test
    void createVehicleShouldFailDueConflict() throws Exception {
        when(vehicleService.createVehicle(any())).thenThrow( new Exception());
        mvc.perform(post("/vehicles/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CREATED_VEHICLE_BODY)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }
}