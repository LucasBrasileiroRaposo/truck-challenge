package com.challenge.truckManagement.controllers;

import com.challenge.truckManagement.DTOs.AuthenticationDTO;
import com.challenge.truckManagement.config.TokenService;
import com.challenge.truckManagement.entities.User;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Validated AuthenticationDTO authenticationDTO) {

        LOGGER.info("Starting login process...");

        UsernamePasswordAuthenticationToken userData =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getLogin(), authenticationDTO.getPassword());

        try {

            Authentication auth = this.authenticationManager.authenticate(userData);
            String token = tokenService.generateToken((User) auth.getPrincipal());

            LOGGER.info("Login done!");
            return new ResponseEntity<>("Login successful!\n This is your token: " + token, HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            LOGGER.error("No User with this username found!");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            LOGGER.error("A unexpected error occurred, sorry, check the error's message and try again!");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
}
