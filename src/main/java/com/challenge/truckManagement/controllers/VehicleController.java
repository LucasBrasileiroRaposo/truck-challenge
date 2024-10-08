package com.challenge.truckManagement.controllers;

import com.challenge.truckManagement.DTOs.VehicleDTO;
import com.challenge.truckManagement.DTOs.VehicleUpdateDTO;
import com.challenge.truckManagement.services.UserService;
import com.challenge.truckManagement.services.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Vehicles Controller")
@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(VehicleController.class);

    @Operation(summary = "Creates a new Vehicle", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vehicle created successfully"),
            @ApiResponse(responseCode = "409", description = "Vehicle data violating unique DB constraint"),
            @ApiResponse(responseCode = "404", description = "Owner not found in database"),
            @ApiResponse(responseCode = "403", description = "You must be a Admin to do this request"),
            @ApiResponse(responseCode = "500", description = "An unexpected error occurred, check the error's message and try again!")
    })
    @PostMapping("/")
    public ResponseEntity<?> createVehicle(@RequestBody @Validated VehicleDTO vehicleDTO) {

        LOGGER.info("Starting the process of creating an User!");
        try {
            if (vehicleDTO.getOwner() != null) this.userService.checkClient(vehicleDTO.getOwner().getId());
            VehicleDTO createdVehicle = this.vehicleService.createVehicle(vehicleDTO);
            LOGGER.info("Vehicle created!");
            return new ResponseEntity<>(createdVehicle, HttpStatus.CREATED);

        } catch (EntityExistsException entityAlreadyExistsException) {
            LOGGER.error("Vehicle's creation failed due unique constraint violation!");
            return new ResponseEntity<>(entityAlreadyExistsException.getMessage(), HttpStatus.CONFLICT);
        } catch (EntityNotFoundException e) {
            LOGGER.error("User not found in database!");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            LOGGER.error("A unexpected error occurred, sorry, check the error's message and try again!");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Gets Vehicle by ID", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle found"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found in database"),
            @ApiResponse(responseCode = "403", description = "You must be authenticated to do this request"),
            @ApiResponse(responseCode = "500", description = "An unexpected error occurred, check the error's message and try again!")
    })
    @GetMapping("/{vehicleId}")
    public ResponseEntity<?> getVehicleById(@PathVariable(value = "vehicleId") String vehicleId) {

        LOGGER.info("Starting the process of getting an Vehicle infos!");
        try {
            VehicleDTO vehicleDTO = this.vehicleService.getVehicleById(vehicleId);
            LOGGER.info("Infos of Vehicle found!");
            return new ResponseEntity<>(vehicleDTO,HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            LOGGER.error("Vehicle not found in database!");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            LOGGER.error("A unexpected error occurred, sorry, check the error's message and try again!");
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Filters Vehicles by criteria", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicles filtered successfully"),
            @ApiResponse(responseCode = "403", description = "You must be authenticated to do this request"),
            @ApiResponse(responseCode = "500", description = "An unexpected error occurred, check the error's message and try again!")
    })
    @GetMapping("/")
    public ResponseEntity<?> filterVehicles(@RequestParam(required = false) String name,
                                            @RequestParam(required = false) Integer year,
                                            @RequestParam(required = false) String brand,
                                            @RequestParam(required = false) String category,
                                            @RequestParam(required = false) String licensePlate,
                                            @RequestParam(required = false) String ownerId,
                                            @RequestParam(required = false) String color){
        LOGGER.info("Starting the process of filtering Vehicles!");
        try {

            Object queryResult = this.vehicleService.getVehicles(name, year, brand, category, licensePlate, ownerId, color);
            LOGGER.info("Finished filtering Vehicles!");
            return new ResponseEntity<>(queryResult, HttpStatus.OK);

        } catch (Exception e) {
            LOGGER.error("A unexpected error occurred, sorry, check the error's message and try again!");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Updates Vehicle by ID", method = "PATCH")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle updated successfully"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found in database"),
            @ApiResponse(responseCode = "403", description = "You must be a Admin to do this request"),
            @ApiResponse(responseCode = "500", description = "An unexpected error occurred, check the error's message and try again!")
    })
    @PatchMapping("/{vehicleId}")
    public ResponseEntity<?> updateVehicle(@PathVariable("vehicleId") String vehicleId, @RequestBody @Validated VehicleUpdateDTO vehicleUpdateDTO) {

        LOGGER.info("Starting the process of updating an Vehicle!");
        try {

            VehicleDTO vehicleDTO = this.vehicleService.updateVehicle(vehicleId, vehicleUpdateDTO);
            LOGGER.info("Vehicle updated!");
            return new ResponseEntity<>(vehicleDTO,HttpStatus.OK);

        } catch (EntityNotFoundException entityNotFoundException) {
            LOGGER.error("Vehicle not found in database!");
            return new ResponseEntity<>(entityNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            LOGGER.error("A unexpected error occurred, sorry, check the error's message and try again!");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Deletes Vehicle by ID", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Vehicle deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found in database"),
            @ApiResponse(responseCode = "403", description = "You must be a Admin to do this request"),
            @ApiResponse(responseCode = "500", description = "An unexpected error occurred, check the error's message and try again!")
    })
    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<?> deleteVehicle(@PathVariable("vehicleId") String vehicleId){

        LOGGER.info("Starting the process of deleting an Vehicle!");
        try {

            this.vehicleService.deleteVehicle(vehicleId);
            LOGGER.info("Finished the process of removing an Vehicle!");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } catch (EntityNotFoundException entityNotFoundException) {
            LOGGER.error("Vehicle not found in database!");
            return new ResponseEntity<>(entityNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            LOGGER.error("A unexpected error occurred, sorry, check the error's message and try again!");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
