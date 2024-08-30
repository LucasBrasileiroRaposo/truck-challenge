package com.challenge.truckManagement.repositories;

import com.challenge.truckManagement.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository <Vehicle, String>{

    @Query(value = "SELECT * FROM vehicles v WHERE " +
                  "(:name IS NULL OR LOWER(v.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND "+
                  "(:year IS NULL OR v.p_year = :year) AND " +
                  "(:brand IS NULL OR LOWER(v.brand) LIKE LOWER(CONCAT('%', :brand, '%'))) AND " +
                  "(:category IS NULL OR LOWER(v.category) LIKE LOWER(CONCAT('%', :category, '%'))) AND " +
                  "(:licensePlate IS NULL OR LOWER(v.license_plate) LIKE LOWER(CONCAT('%', :licensePlate, '%'))) AND " +
                  "(:owner IS NULL OR v.owner_id = :owner) AND " +
                  "(:color IS NULL OR LOWER(v.color) LIKE LOWER(CONCAT('%', :color, '%')))",
    nativeQuery = true)
    List<Vehicle> filterVehicles(@Param("name") String name,
                          @Param("year") Integer year,
                          @Param("brand") String brand,
                          @Param("category") String category,
                          @Param("licensePlate") String licensePlate,
                          @Param("owner") String owner,
                          @Param("color") String color);
}
