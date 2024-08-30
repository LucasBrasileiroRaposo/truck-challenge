package com.challenge.truckManagement.services;

import com.challenge.truckManagement.DTOs.UserDTO;
import com.challenge.truckManagement.DTOs.UserUpdateDTO;
import com.challenge.truckManagement.entities.*;
import com.challenge.truckManagement.repositories.UserRepository;
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
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    VehicleService vehicleService;

    @Mock
    UserRepository userRepository;

    UserDTO defaultUserClientDTO;

    UserDTO defaultUserClientDTOReturn;

    Client defaultClient1;

    UserDTO defaultUserAdminDTO;

    UserDTO defaultUserAdminDTOReturn;

    Admin defaultAdmin;

    Vehicle vehicle;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        defaultUserClientDTO = new UserDTO("id",
                "testClient@example.com",
                "password",
                "12345678900",
                "Test User",
                UserRole.CLIENT,
                List.of(),
                new Date(2000, Calendar.JUNE,28),
                "Rua dos bobos");

        defaultUserClientDTOReturn = new UserDTO("id",
                "testClient@example.com",
                "12345678900",
                "Test User",
                UserRole.CLIENT,
                "Rua dos bobos",
                new Date(2000, Calendar.JUNE,28),
                List.of());

        defaultClient1 = new Client(defaultUserClientDTO);
        defaultClient1.setId(defaultUserClientDTO.getId());

        defaultUserAdminDTO = new UserDTO("id",
                "testAdmin@example.com",
                "password",
                "12345678901",
                "Test User",
                UserRole.ADMIN,
                List.of(),
                new Date(2000, Calendar.JUNE,28),
                "Rua dos bobos"
        );

        defaultUserAdminDTOReturn = new UserDTO("id",
                "testAdmin@example.com",
                "12345678901",
                "Test User",
                UserRole.ADMIN
        );

        defaultAdmin = new Admin(defaultUserAdminDTO);
        defaultAdmin.setId(defaultUserAdminDTO.getId());

        vehicle = new Vehicle();
        vehicle.setId("vehicle-id");

    }

    @Test
    void createUserClientSuccess() {
        when(userRepository.save(any(User.class))).thenReturn(defaultClient1);
        UserDTO userDTO = userService.createUser(defaultUserClientDTO);

        assertNotNull(userDTO);
        assertEquals(defaultUserClientDTOReturn.getId(), userDTO.getId());
        assertEquals(defaultUserClientDTOReturn.getEmail(),userDTO.getEmail());
        assertEquals(defaultUserClientDTOReturn.getCpf(), userDTO.getCpf());
        assertEquals(defaultUserClientDTOReturn.getName(), userDTO.getName());
        assertEquals(defaultUserClientDTOReturn.getRole(), userDTO.getRole());
        assertEquals(defaultUserClientDTOReturn.getAddress(), userDTO.getAddress());
        assertEquals(defaultUserClientDTOReturn.getBirthDate(), userDTO.getBirthDate());
    }

    @Test
    void createUserAdminSuccess() {
        when(userRepository.save(any(User.class))).thenReturn(defaultAdmin);
        UserDTO userDTO = userService.createUser(defaultUserAdminDTO);

        assertNotNull(userDTO);
        assertEquals(defaultUserAdminDTOReturn.getId(), userDTO.getId());
        assertEquals(defaultUserAdminDTOReturn.getEmail(),userDTO.getEmail());
        assertEquals(defaultUserAdminDTOReturn.getCpf(), userDTO.getCpf());
        assertEquals(defaultUserAdminDTOReturn.getName(), userDTO.getName());
        assertEquals(defaultUserAdminDTOReturn.getRole(), userDTO.getRole());
        verify(userRepository, times(1)).save(any(User.class));

    }

    @Test
    void createUserFailsDueEntityExistsException() {
        when(userRepository.save(any(User.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(EntityExistsException.class, () -> userService.createUser(defaultUserClientDTO));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUserFailsDueRunTimeException() {
        when(userRepository.save(any(User.class))).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> userService.createUser(defaultUserClientDTO));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void getUserClientSuccess() {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(defaultClient1));

        UserDTO foundUser = userService.getUser("id");

        assertNotNull(foundUser);
        assertEquals(defaultUserClientDTOReturn.getId(), foundUser.getId());
        assertEquals(defaultUserClientDTOReturn.getEmail(),foundUser.getEmail());
        assertEquals(defaultUserClientDTOReturn.getCpf(), foundUser.getCpf());
        assertEquals(defaultUserClientDTOReturn.getName(), foundUser.getName());
        assertEquals(defaultUserClientDTOReturn.getRole(), foundUser.getRole());
        assertEquals(defaultUserClientDTOReturn.getAddress(), foundUser.getAddress());
        assertEquals(defaultUserClientDTOReturn.getBirthDate(), foundUser.getBirthDate());
        verify(userRepository, times(1)).findById(anyString());
    }

    @Test
    void getUserAdminSuccess() {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(defaultAdmin));

        UserDTO foundUser = userService.getUser("id");

        assertNotNull(foundUser);
        assertEquals(defaultUserAdminDTOReturn.getId(), foundUser.getId());
        assertEquals(defaultUserAdminDTOReturn.getEmail(),foundUser.getEmail());
        assertEquals(defaultUserAdminDTOReturn.getCpf(), foundUser.getCpf());
        assertEquals(defaultUserAdminDTOReturn.getName(), foundUser.getName());
        assertEquals(defaultUserAdminDTOReturn.getRole(), foundUser.getRole());
        verify(userRepository, times(1)).findById(anyString());
    }

    @Test
    void getUserEntityNotFoundException() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getUser(anyString()));
        verify(userRepository, times(1)).findById(anyString());
    }

    @Test
    void getUsersSuccess() {

        when(userRepository.filterUsers(anyString(), anyString(), anyString(), anyString(), any(), anyString()))
                .thenReturn(List.of(defaultClient1, defaultAdmin));

        List<UserDTO> users = userService.getUsers("", "Test User","", "", null,"");

        assertNotNull(users);
        assertEquals(defaultUserClientDTOReturn.getId(), users.get(0).getId());
        assertEquals(defaultUserClientDTOReturn.getEmail(),users.get(0).getEmail());
        assertEquals(defaultUserClientDTOReturn.getCpf(), users.get(0).getCpf());
        assertEquals(defaultUserClientDTOReturn.getName(), users.get(0).getName());
        assertEquals(defaultUserClientDTOReturn.getRole(), users.get(0).getRole());
        assertEquals(defaultUserClientDTOReturn.getAddress(), users.get(0).getAddress());
        assertEquals(defaultUserClientDTOReturn.getBirthDate(), users.get(0).getBirthDate());
        assertEquals(defaultUserAdminDTOReturn.getId(), users.get(1).getId());
        assertEquals(defaultUserAdminDTOReturn.getEmail(),users.get(1).getEmail());
        assertEquals(defaultUserAdminDTOReturn.getCpf(), users.get(1).getCpf());
        assertEquals(defaultUserAdminDTOReturn.getName(), users.get(1).getName());
        assertEquals(defaultUserAdminDTOReturn.getRole(), users.get(1).getRole());
        assertEquals(defaultUserAdminDTOReturn.getAddress(), users.get(1).getAddress());
        assertEquals(defaultUserAdminDTOReturn.getBirthDate(), users.get(1).getBirthDate());
        verify(userRepository, times(1)).filterUsers(anyString(), anyString(), anyString(), anyString(), any(), anyString());
    }

    @Test
    void updateUserClientGeneralInfoSuccess() {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(defaultClient1));
        when(userRepository.save(any(User.class))).thenReturn(defaultClient1);

        UserUpdateDTO updateDTO = new UserUpdateDTO();
        updateDTO.setEmail("updated@example.com");
        updateDTO.setAddress("Rua das streets");
        updateDTO.setBirthDate(new Date(2006,Calendar.JANUARY,01));


        UserDTO updatedUser = userService.updateUserGeneralInfo("id", updateDTO);

        assertNotNull(updatedUser);
        assertEquals("updated@example.com", updatedUser.getEmail());
        assertEquals(defaultClient1.getName(), updatedUser.getName());
        assertEquals(defaultClient1.getCpf(), updatedUser.getCpf());
        assertEquals(defaultClient1.getRole(), updatedUser.getRole());
        assertEquals(updateDTO.getAddress(), updatedUser.getAddress());
        assertEquals(defaultClient1.getBirthDate(), updatedUser.getBirthDate());
        assertEquals(defaultClient1.getVehicles(), updatedUser.getVehicles());
        verify(userRepository, times(1)).findById(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUserAdminGeneralInfoSuccess() {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(defaultAdmin));
        when(userRepository.save(any(User.class))).thenReturn(defaultAdmin);

        UserUpdateDTO updateDTO = new UserUpdateDTO();
        updateDTO.setName("updated name");
        updateDTO.setCpf("12345678900");

        UserDTO updatedUser = userService.updateUserGeneralInfo("id", updateDTO);

        assertNotNull(updatedUser);
        assertEquals(defaultAdmin.getEmail(), updatedUser.getEmail());
        assertEquals(defaultAdmin.getName(), updatedUser.getName());
        assertEquals(defaultAdmin.getCpf(), updatedUser.getCpf());
        assertEquals(defaultAdmin.getRole(), updatedUser.getRole());
        verify(userRepository, times(1)).findById(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUserGeneralInfoFailsDueEntityNotFoundException() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());
        UserUpdateDTO updateDTO = new UserUpdateDTO();
        assertThrows(EntityNotFoundException.class, () -> userService.updateUserGeneralInfo(anyString(), updateDTO));
        verify(userRepository, times(1)).findById(anyString());
    }

    @Test
    void updateUserPasswordSuccess() {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(defaultAdmin));
        when(userRepository.save(any(User.class))).thenReturn(defaultAdmin);

        assertDoesNotThrow(() -> userService.updateUserPassword("id", "newPassword"));
        verify(userRepository, times(1)).findById(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUserPasswordFailsDueEntityNotFoundException() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.updateUserPassword(anyString(), "newPassword"));
        verify(userRepository, times(1)).findById(anyString());
    }

    @Test
    void updateUserPasswordFailsDueBadRequestExceptionNotEnoughSize() {

        assertThrows(BadRequestException.class, () -> userService.updateUserPassword("id", "123"));
        verify(userRepository, times(0)).findById(anyString());
    }

    @Test
    void updateUserPasswordFailsDueBadRequestExceptionInvalidPassword() {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(defaultClient1));

        assertThrows(BadRequestException.class, () -> userService.updateUserPassword("id", "          "));
        verify(userRepository, times(1)).findById(anyString());
    }

    @Test
    void deleteUserSuccess() throws BadRequestException {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(defaultClient1));
        doNothing().when(userRepository).deleteById(anyString());

        defaultClient1.addVehicle(vehicle);

        assertDoesNotThrow(() -> userService.deleteUser("id"));
        verify(userRepository, times(1)).findById(anyString());
        verify(userRepository, times(1)).deleteById(anyString());
    }

    @Test
    void deleteUsersFailsDueEntityNotFoundException() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(anyString()));
        verify(userRepository, times(1)).findById(anyString());
    }

    @Test
    void checkClientSuccess() throws BadRequestException {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(defaultClient1));

        Client client = userService.checkClient("id");

        assertNotNull(client);
        assertEquals(UserRole.CLIENT, client.getRole());
        assertEquals(defaultUserClientDTOReturn.getId(), client.getId());
        assertEquals(defaultUserClientDTOReturn.getEmail(),client.getEmail());
        assertEquals(defaultUserClientDTOReturn.getCpf(), client.getCpf());
        assertEquals(defaultUserClientDTOReturn.getName(), client.getName());
        assertEquals(defaultUserClientDTOReturn.getRole(), client.getRole());
        assertEquals(defaultUserClientDTOReturn.getAddress(), client.getAddress());
        assertEquals(defaultUserClientDTOReturn.getBirthDate(), client.getBirthDate());
        verify(userRepository, times(1)).findById(anyString());
    }

    @Test
    void checkClientFailsDueEntityNotFoundException() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.checkClient("1"));
        verify(userRepository, times(1)).findById(anyString());
    }

    @Test
    void checkClientFailsDueBadRequestException() {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(defaultAdmin));

        assertThrows(BadRequestException.class, () -> userService.checkClient("1"));
        verify(userRepository, times(1)).findById(anyString());
    }

    @Test
    void addVehicleToUserSuccess() throws BadRequestException {
        when(userRepository.findById(defaultClient1.getId())).thenReturn(Optional.of(defaultClient1));
        when(vehicleService.checkVehicles(anyList())).thenReturn(List.of(vehicle));
        when(userRepository.save(any(User.class))).thenReturn(defaultClient1);

        String vehicleId = vehicle.getId();

        UserDTO result = userService.addClientVehicle(defaultClient1.getId(), List.of(vehicleId));

        assertTrue(result.getVehicles().contains(vehicle));
        assertEquals(1, result.getVehicles().size());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void addVehicleToClientFailsDueBadRequestDueVehiclesListIsEmpty() {
        assertThrows(BadRequestException.class, () -> userService.addClientVehicle(vehicle.getId(),List.of()));
        verify(userRepository, times(0)).findById(anyString());
    }

    @Test
    void removeVehicleFromClientSuccess() throws BadRequestException {
        when(userRepository.findById(defaultClient1.getId())).thenReturn(Optional.of(defaultClient1));
        when(vehicleService.checkVehicles(anyList())).thenReturn(List.of(vehicle));
        when(userRepository.save(any(User.class))).thenReturn(defaultClient1);

        String vehicleId = vehicle.getId();
        defaultClient1.addVehicle(vehicle);
        assertEquals(1, defaultClient1.getVehicles().size());

        UserDTO result = userService.removeClientVehicle(defaultClient1.getId(), List.of(vehicleId));

        assertFalse(result.getVehicles().contains(vehicle));
        assertEquals(0, result.getVehicles().size());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void removeVehicleFromClientFailsDueVehiclesListIsEmpty() {
        assertThrows(BadRequestException.class, () -> userService.removeClientVehicle(vehicle.getId(),List.of()));
        verify(userRepository, times(0)).findById(anyString());
    }

    @Test
    void findUserByEmailSuccess() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(defaultClient1));

        User foundUser = userService.findUserByEmail(defaultClient1.getEmail());

        assertNotNull(foundUser);
        assertEquals(defaultUserClientDTOReturn.getId(), foundUser.getId());
        assertEquals(defaultUserClientDTOReturn.getEmail(),foundUser.getEmail());
        assertEquals(defaultUserClientDTOReturn.getCpf(), foundUser.getCpf());
        assertEquals(defaultUserClientDTOReturn.getName(), foundUser.getName());
        assertEquals(defaultUserClientDTOReturn.getRole(), foundUser.getRole());
        verify(userRepository, times(1)).findByEmail(anyString());
    }

}