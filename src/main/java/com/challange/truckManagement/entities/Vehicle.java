package com.challange.truckManagement.entities;
import com.challange.truckManagement.DTOs.VehicleDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;


@Entity(name = "vehicles")
@AllArgsConstructor
@Data
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private Integer year;

    private String brand;

    private String category;

    @Column(unique= true)
    private String licensePlate;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client owner;

    private String color;

    public void Vehicle() {

    }

    public Vehicle(VehicleDTO vehicleDTO) {

    }
}
