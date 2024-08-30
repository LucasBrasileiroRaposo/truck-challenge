package com.challenge.truckManagement.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity(name = "vehicles")
@Table(name = "vehicles")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(name= "p_year", nullable = false)
    private Integer year;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String category;

    @Column(unique= true)
    private String licensePlate;

    @ManyToOne()
    @JoinColumn(referencedColumnName = "id", name = "owner_id" )
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JsonBackReference
    private Client owner;

    private String color;

    public Vehicle(String name, Integer year, String brand, String category, String licensePlate, Client owner, String color) {
        this.name = name;
        this.year = year;
        this.brand = brand;
        this.category = category;
        this.licensePlate = licensePlate;
        this.owner = owner;
        this.color = color;
    }


    public Client getOwner() {
        if (owner != null) {
            Client clientView = new Client();
            clientView.setId(owner.getId());
            clientView.setBirthDate(owner.getBirthDate());
            clientView.setAddress(owner.getAddress());
            clientView.setCpf(owner.getCpf());
            clientView.setEmail(owner.getEmail());
            clientView.setName(owner.getName());
            clientView.setRole(owner.getRole());
            clientView.setVehicles(null);

            return clientView;
        }
        return owner;
    }
}
