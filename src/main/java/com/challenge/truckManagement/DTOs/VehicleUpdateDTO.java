package com.challenge.truckManagement.DTOs;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VehicleUpdateDTO {

    @NotEmpty
    private String name;

    @Min(value = 1900, message = "The year must be greater than 1900!")
    @Max(value = 2024, message = "The year must be greater than 1900!")
    private Integer year;

    @Size(min = 2, max = 35, message = "The brand have 2 to 35 characters")
    private String brand;

    @Size(min = 2, max = 50, message = "The category have 2 to 35 characters")
    private String category;

    @Size(min = 4, max = 8)
    private String licensePlate;

    @NotEmpty(message = "An Vehicle must have a color!")
    private String color;

}
