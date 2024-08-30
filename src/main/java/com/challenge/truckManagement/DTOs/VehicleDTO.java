package com.challenge.truckManagement.DTOs;

import com.challenge.truckManagement.entities.Client;
import com.challenge.truckManagement.entities.Vehicle;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDTO {

    private String id;

    @NotBlank(message = "An Vehicle must have a name!")
    private String name;

    @NotNull(message = "An Vehicle must have a production year!")
    @Min(value = 1900, message = "The year must be greater than 1900!")
    @Max(value = 2024, message = "The year must be greater than 1900!")
    private Integer year;

    @NotBlank(message = "An Vehicle must have a brand!")
    @Size(min = 2, max = 35, message = "The brand have 2 to 35 characters")
    private String brand;

    @NotBlank(message = "An Vehicle must have a category!")
    @Size(min = 2, max = 50, message = "The category have 2 to 35 characters")
    private String category;

    @Size(min = 4, max = 8)
    private String licensePlate;

    private Client owner;

    @NotBlank(message = "An Vehicle must have a color!")
    private String color;

    public VehicleDTO(Vehicle defaultVehicle) {
        this.id = defaultVehicle.getId();
        this.name = defaultVehicle.getName();
        this.year = defaultVehicle.getYear();
        this.brand = defaultVehicle.getBrand();
        this.category = defaultVehicle.getCategory();
        this.licensePlate = defaultVehicle.getLicensePlate();
        this.owner = defaultVehicle.getOwner();
        this.color = defaultVehicle.getColor();
    }

}
