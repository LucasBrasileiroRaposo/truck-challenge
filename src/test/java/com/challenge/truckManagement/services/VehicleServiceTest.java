package com.challenge.truckManagement.services;

import com.challenge.truckManagement.DTOs.UserDTO;
import com.challenge.truckManagement.DTOs.UserUpdateDTO;
import com.challenge.truckManagement.DTOs.VehicleDTO;
import com.challenge.truckManagement.DTOs.VehicleUpdateDTO;
import com.challenge.truckManagement.entities.Client;
import com.challenge.truckManagement.entities.User;
import com.challenge.truckManagement.entities.UserRole;
import com.challenge.truckManagement.entities.Vehicle;
import com.challenge.truckManagement.repositories.VehicleRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class VehicleServiceTest {

    @InjectMocks
    VehicleService vehicleService;

    @Mock
    VehicleRepository vehicleRepository;

    VehicleDTO defaultVehicleDTO;

    VehicleDTO defaultVehicleDTOReturn;

    Vehicle defaultVehicle;

    Client defaultClient;

    UserDTO defaultUserDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        defaultUserDTO = new UserDTO("id",
                "testClient@example.com",
                "password",
                "12345678900",
                "Test User",
                UserRole.CLIENT,
                List.of(),
                new Date(2000, Calendar.JUNE,28),
                "Rua dos bobos");

        defaultClient = new Client(defaultUserDTO);
        defaultClient.setId(defaultUserDTO.getId());

        defaultVehicle = new Vehicle(    "Generic Name",       // name
                2023,                 // year
                "Generic Brand",      // brand
                "Generic Category",   // category
                "ABC1234",            // licensePlate
                defaultClient,                // owner
                "Red"  );
        defaultVehicle.setId("id");
        defaultVehicleDTO = new VehicleDTO(defaultVehicle);
    }

    @Test
    void createVehicleSuccess() throws BadRequestException {
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(defaultVehicle);

        VehicleDTO vehicleDTO = vehicleService.createVehicle(defaultVehicleDTO);

        assertNotNull(vehicleDTO);
        assertEquals(defaultVehicle.getId(), vehicleDTO.getId());
        assertEquals(defaultVehicle.getYear(),vehicleDTO.getYear());
        assertEquals(defaultVehicle.getLicensePlate(), vehicleDTO.getLicensePlate());
        assertEquals(defaultVehicle.getName(), vehicleDTO.getName());
        assertEquals(defaultVehicle.getBrand(), vehicleDTO.getBrand());
        assertEquals(defaultVehicle.getOwner(), vehicleDTO.getOwner());
        assertEquals(defaultVehicle.getColor(), vehicleDTO.getColor());
        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
    }


    @Test
    void createVehicleFailsDueEntityExistsException() throws BadRequestException {
        when(vehicleRepository.save(any(Vehicle.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(EntityExistsException.class, () -> vehicleService.createVehicle(defaultVehicleDTO));
        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
    }

    @Test
    void createUserFailsDueRunTimeException() {
        when(vehicleRepository.save(any(Vehicle.class))).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> vehicleService.createVehicle(defaultVehicleDTO));
        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
    }

    @Test
    void getVehicleByIdSuccess() {
        when(vehicleRepository.findById(anyString())).thenReturn(Optional.of(defaultVehicle));

        VehicleDTO vehicleDTO = vehicleService.getVehicleById(defaultVehicle.getId());

        assertNotNull(vehicleDTO);
        assertEquals(defaultVehicle.getId(), vehicleDTO.getId());
        assertEquals(defaultVehicle.getYear(),vehicleDTO.getYear());
        assertEquals(defaultVehicle.getLicensePlate(), vehicleDTO.getLicensePlate());
        assertEquals(defaultVehicle.getName(), vehicleDTO.getName());
        assertEquals(defaultVehicle.getBrand(), vehicleDTO.getBrand());
        assertEquals(defaultVehicle.getOwner(), vehicleDTO.getOwner());
        assertEquals(defaultVehicle.getColor(), vehicleDTO.getColor());
        verify(vehicleRepository, times(1)).findById(anyString());
    }

    @Test
    void getVehicleByIdFailsDueEntityNotFoundException() {
        when(vehicleRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> vehicleService.getVehicleById(anyString()));
        verify(vehicleRepository, times(1)).findById(anyString());
    }


    @Test
    void getVehicles() {
        when(vehicleRepository.filterVehicles(anyString(), anyInt(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(List.of(defaultVehicle));

        List<VehicleDTO> vehicles = vehicleService.getVehicles(defaultVehicle.getId(), defaultVehicle.getYear(),"", "", "", defaultClient.getId(), "");

        assertNotNull(vehicles);
        assertEquals(defaultVehicle.getYear(),vehicles.get(0).getYear());
        assertEquals(defaultVehicle.getLicensePlate(), vehicles.get(0).getLicensePlate());
        assertEquals(defaultVehicle.getName(), vehicles.get(0).getName());
        assertEquals(defaultVehicle.getBrand(), vehicles.get(0).getBrand());
        assertEquals(defaultVehicle.getOwner(), vehicles.get(0).getOwner());
        assertEquals(defaultVehicle.getColor(), vehicles.get(0).getColor());
        verify(vehicleRepository, times(1)).filterVehicles(anyString(), anyInt(), anyString(), anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void updateVehicleSuccess() {
        when(vehicleRepository.findById(anyString())).thenReturn(Optional.of(defaultVehicle));
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(defaultVehicle);

        VehicleUpdateDTO updateDTO = new VehicleUpdateDTO();
        updateDTO.setName("updated name");
        updateDTO.setBrand("my brand");
        updateDTO.setColor("yellow");
        updateDTO.setCategory("Bike");
        updateDTO.setLicensePlate("AAAAAA");


        VehicleDTO updatedVehicle = vehicleService.updateVehicle(defaultVehicle.getId(), updateDTO);

        assertNotNull(updatedVehicle);
        assertEquals(defaultVehicle.getId(), updatedVehicle.getId());
        assertEquals(defaultVehicle.getYear(),updatedVehicle.getYear());
        assertEquals(defaultVehicle.getLicensePlate(), updatedVehicle.getLicensePlate());
        assertEquals(defaultVehicle.getName(), updatedVehicle.getName());
        assertEquals(defaultVehicle.getBrand(), updatedVehicle.getBrand());
        assertEquals(defaultVehicle.getOwner(), updatedVehicle.getOwner());
        assertEquals(defaultVehicle.getColor(), updatedVehicle.getColor());
        verify(vehicleRepository, times(1)).findById(anyString());
        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
    }

    @Test
    void updateVehicleFailsDueEntityNotFoundException() {

        when(vehicleRepository.findById(anyString())).thenReturn(Optional.empty());
        VehicleUpdateDTO updateDTO = new VehicleUpdateDTO();
        assertThrows(EntityNotFoundException.class, () -> vehicleService.updateVehicle(anyString(), updateDTO));
        verify(vehicleRepository, times(1)).findById(anyString());
    }

    @Test
    void deleteVehicleSuccess() {
        when(vehicleRepository.findById(anyString())).thenReturn(Optional.of(defaultVehicle));
        doNothing().when(vehicleRepository).deleteById(anyString());

        assertDoesNotThrow(() -> vehicleService.deleteVehicle(defaultVehicle.getId()));
        verify(vehicleRepository, times(1)).findById(anyString());
        verify(vehicleRepository, times(1)).deleteById(anyString());
    }

    @Test
    void deleteVehicleFailsDueEntityNotFoundException() {
        when(vehicleRepository.findById(anyString())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> vehicleService.deleteVehicle(defaultVehicle.getId()));
        verify(vehicleRepository, times(1)).findById(anyString());
    }

    @Test
    void checkVehiclesSuccess() {
        when(vehicleRepository.findById(anyString())).thenReturn(Optional.of(defaultVehicle));

        List<Vehicle> vehicles = vehicleService.checkVehicles(List.of(defaultVehicle.getId()));

        assertNotNull(vehicles);
        assertEquals(defaultVehicle.getYear(),vehicles.get(0).getYear());
        assertEquals(defaultVehicle.getLicensePlate(), vehicles.get(0).getLicensePlate());
        assertEquals(defaultVehicle.getName(), vehicles.get(0).getName());
        assertEquals(defaultVehicle.getBrand(), vehicles.get(0).getBrand());
        assertEquals(defaultVehicle.getOwner(), vehicles.get(0).getOwner());
        assertEquals(defaultVehicle.getColor(), vehicles.get(0).getColor());
        verify(vehicleRepository, times(1)).findById(anyString());
    }

    @Test
    void checkVehiclesFailsDueEntityNotFound() {
        when(vehicleRepository.findById(anyString())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> vehicleService.checkVehicles(List.of(defaultVehicle.getId())));
        verify(vehicleRepository, times(1)).findById(anyString());
    }
}