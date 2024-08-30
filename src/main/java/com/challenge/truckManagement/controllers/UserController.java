package com.challenge.truckManagement.controllers;

import com.challenge.truckManagement.DTOs.UserDTO;
import com.challenge.truckManagement.DTOs.UserUpdateDTO;
import com.challenge.truckManagement.services.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Tag(name = "Users Controller")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService usersService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Operation(summary = "Creates a User", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "409", description = "User data violating unique DB constraint"),
            @ApiResponse(responseCode = "500", description = "A unexpected error occurred, sorry, check the error's message and try again!"),
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Validated UserDTO userDTO) {

        LOGGER.info("Starting the process of creating an User!");
        try {

            UserDTO createdUser = this.usersService.createUser(userDTO);
            LOGGER.info("User created!");
            return new ResponseEntity<>(createdUser,HttpStatus.CREATED);

        } catch (EntityExistsException userAlreadyExists) {
            LOGGER.error("User's creation failed due unique constraint violation!");
            return new ResponseEntity<>(userAlreadyExists.getMessage(),HttpStatus.CONFLICT);
        } catch (Exception e) {
            LOGGER.error("A unexpected error occurred, sorry, check the error's message and try again!");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get user by ID", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "A unexpected error occurred, sorry, check the error's message and try again!")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable("userId") String idStr) {

        LOGGER.info("Starting the process of getting an User infos!");
        try {

            UserDTO userDTOInfo = this.usersService.getUser(idStr);
            LOGGER.info("Infos of User found!");
            return new ResponseEntity<>(userDTOInfo, HttpStatus.OK);

        }catch (EntityNotFoundException entityNotFoundException) {
            LOGGER.error("User not found in database!");
            return new ResponseEntity<>(entityNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            LOGGER.error("A unexpected error occurred, sorry, check the error's message and try again!");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Filter Users by parameters", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "500", description = "A unexpected error occurred, sorry, check the error's message and try again!")
    })
    @GetMapping("/")
    public ResponseEntity<?> filterUsers(@RequestParam(required = false) String cpf,
                                         @RequestParam(required = false) String name,
                                         @RequestParam(required = false) String email,
                                         @RequestParam(required = false) String role,
                                         @RequestParam(required = false) Date birthDate,
                                         @RequestParam(required = false) String address) {

        LOGGER.info("Starting the process of filtering Users!");
        try {

            Object queryResult = this.usersService.getUsers(cpf, name, email, role, birthDate, address);
            LOGGER.info("Finished filtering Users!");
            return new ResponseEntity<>(queryResult, HttpStatus.OK);

        } catch (Exception e) {
            LOGGER.error("A unexpected error occurred, sorry, check the error's message and try again!");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Operation(summary = "Update user data by ID", method = "PATCH")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation Succeeded"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "409", description = "User data violating unique DB constraint"),
            @ApiResponse(responseCode = "500", description = "A unexpected error occurred, sorry, check the error's message and try again!")
    })
    @PatchMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable("userId") String id,@RequestBody @Validated UserUpdateDTO userPatchDTO) {

        LOGGER.info("Starting the process of updating an User!");
        try {

            UserDTO updatedUser = this.usersService.updateUserGeneralInfo(id, userPatchDTO);
            LOGGER.info("User updated!");
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            LOGGER.error("User's update failed due unique constraint violation!");
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        } catch (DataIntegrityViolationException ex) {
            LOGGER.error("User not found in database!");
            return new ResponseEntity<>("Entity with provided CPF or Email already exists.", HttpStatus.CONFLICT);
        } catch (Exception e) {
            LOGGER.error("A unexpected error occurred, sorry, check the error's message and try again!");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update user password by ID", method = "PATCH")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation Succeeded"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "User new password violates password pattern"),
            @ApiResponse(responseCode = "500", description = "A unexpected error occurred, sorry, check the error's message and try again!")
    })
    @PatchMapping("/{userId}/newPassword")
    public ResponseEntity<?> updateUserPassword(@PathVariable("userId") String id,
                                                @RequestBody JsonNode body) {
        LOGGER.info("Starting the process of updating an User's password!");
        try {

            String newPassword = body.get("newPassword").asText();
            this.usersService.updateUserPassword(id, newPassword);
            LOGGER.info("Finished the process of updating an User's password!");
            return new ResponseEntity<>("Password updated successfully", HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            LOGGER.error("User's password update failed due unique constraint violation!");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BadRequestException e) {
            LOGGER.error("The provided password isn't in a valid structure!");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOGGER.error("A unexpected error occurred, sorry, check the error's message and try again!");
            return new ResponseEntity<>(e.getMessage() ,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "add an vehicle to a user", method = "PATCH")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation Succeeded"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "User id informed isn't from a Client"),
            @ApiResponse(responseCode = "500", description = "A unexpected error occurred, sorry, check the error's message and try again!")
    })
    @PatchMapping("/{userId}/addVehicle")
    public ResponseEntity<?> addVehicleToClient(@PathVariable("userId") String id, @RequestBody List<String> vehicleIds) {

        LOGGER.info("Starting the process of adding an Vehicle to a User!");
        try {

            UserDTO updatedUser = this.usersService.addClientVehicle(id, vehicleIds);
            LOGGER.info("Finished adding Vehicles to User!");
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);

        } catch (EntityNotFoundException entityNotFoundException) {
            LOGGER.error("Adding Vehicle failed due Client or Vehicle not exists on database!");
            return new ResponseEntity<>(entityNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BadRequestException e) {
            LOGGER.error("User is not a Client!");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOGGER.error("A unexpected error occurred, sorry, check the error's message and try again!");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "remove a vehicle from user", method = "PATCH")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation Succeeded"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "User id informed isn't from a Client"),
            @ApiResponse(responseCode = "500", description = "A unexpected error occurred, sorry, check the error's message and try again!")
    })
    @PatchMapping("/{userId}/removeVehicle")
    public ResponseEntity<?> removeVehicleFromClient(@PathVariable("userId") String id, @RequestBody List<String> vehicleIds) {

        LOGGER.info("Starting the process of removing an Vehicle from a User!");
        try {

            UserDTO updatedUser = this.usersService.removeClientVehicle(id, vehicleIds);
            LOGGER.info("Finished removing an Vehicle from a User!");
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);

        } catch (EntityNotFoundException entityNotFoundException) {
            LOGGER.error("Removing Vehicle failed due Client or Vehicle not exists on database!");
            return new ResponseEntity<>(entityNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BadRequestException e) {
            LOGGER.error("User is not a Client!!");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOGGER.error("A unexpected error occurred, sorry, check the error's message and try again!");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Operation(summary = "Delete user by ID", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Operation Succeeded"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "A unexpected error occurred, sorry, check the error's message and try again!")
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") String idStr){

        LOGGER.info("Starting the process of deleting an User!");
        try {

            this.usersService.deleteUser(idStr);
            LOGGER.info("Finished the process of removing an User!");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } catch (EntityNotFoundException entityNotFoundException) {
            LOGGER.error("User not found in database!");
            return new ResponseEntity<>(entityNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            LOGGER.error("A unexpected error occurred, sorry, check the error's message and try again!");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
