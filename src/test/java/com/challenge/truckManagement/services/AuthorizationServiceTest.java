package com.challenge.truckManagement.services;

import com.challenge.truckManagement.DTOs.UserDTO;
import com.challenge.truckManagement.entities.Admin;
import com.challenge.truckManagement.entities.User;
import com.challenge.truckManagement.entities.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthorizationServiceTest {

    @InjectMocks
    private AuthorizationService authorizationService;

    @Mock
    private UserService userService;

    private User defaultUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        UserDTO defaultUserDTO = new UserDTO("id",
                "testAdmin@example.com",
                "12345678901",
                "Test User",
                UserRole.ADMIN
        );

        defaultUser = new Admin(defaultUserDTO);
        defaultUser.setId(defaultUserDTO.getId());
        defaultUser.setPassword("12345");
    }

    @Test
    void loadUserByUsernameSuccess() {
        when(userService.findUserByEmail(anyString())).thenReturn(defaultUser);

        UserDetails userDetails = authorizationService.loadUserByUsername(defaultUser.getId());

        assertNotNull(userDetails);
        assertEquals(defaultUser.getEmail(),userDetails.getUsername());
        assertEquals(defaultUser.getPassword(), userDetails.getPassword());
        assertEquals(defaultUser.getAuthorities(), userDetails.getAuthorities());
        verify(userService, times(1)).findUserByEmail(anyString());


    }
}