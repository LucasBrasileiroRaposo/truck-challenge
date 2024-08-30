package com.challenge.truckManagement.services;

import com.challenge.truckManagement.DTOs.VehicleDTO;
import com.challenge.truckManagement.DTOs.VehicleUpdateDTO;
import com.challenge.truckManagement.entities.Vehicle;
import com.challenge.truckManagement.repositories.VehicleRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public VehicleDTO createVehicle(VehicleDTO vehicleDTO) throws BadRequestException {


        try{
            Vehicle vehicleToSave = new Vehicle(
                    vehicleDTO.getId(),
                    vehicleDTO.getName(),
                    vehicleDTO.getYear(),
                    vehicleDTO.getBrand(),
                    vehicleDTO.getCategory(),
                    vehicleDTO.getLicensePlate(),
                    vehicleDTO.getOwner(),
                    vehicleDTO.getColor());

            VehicleDTO vehicleDTOResult = new VehicleDTO(this.vehicleRepository.save(vehicleToSave));
            return vehicleDTOResult;

        } catch (DataIntegrityViolationException ex) {
            throw new EntityExistsException(ex.getMostSpecificCause());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public VehicleDTO getVehicleById(String vehicleId) {

        Optional<Vehicle> vehicleOptional = this.vehicleRepository.findById(vehicleId);
        if (vehicleOptional.isEmpty()) {
            throw new EntityNotFoundException(String.format("No vehicle exists with the id: %s", vehicleId));
        }

        Vehicle vehicle = vehicleOptional.get();
        return new VehicleDTO(vehicle);
    }

    public List<VehicleDTO> getVehicles(String name, Integer year, String brand, String category, String licensePlate, String owner, String color) {

        List<Vehicle> vehiclesFiltered = this.vehicleRepository.filterVehicles(name, year, brand, category, licensePlate, owner, color);

        List<VehicleDTO> vehicleDTOList = new ArrayList<>();


        for (Vehicle vehicle: vehiclesFiltered) {

            VehicleDTO vehicleDTO = new VehicleDTO(
              vehicle.getId(),
              vehicle.getName(),
              vehicle.getYear(),
              vehicle.getBrand(),
              vehicle.getCategory(),
              vehicle.getLicensePlate(),
              vehicle.getOwner(),
              vehicle.getColor()
            );
            vehicleDTOList.add(vehicleDTO);
        }

        return vehicleDTOList;
    }


    public VehicleDTO updateVehicle(String vehicleId, VehicleUpdateDTO vehicleUpdateDTO) {

        Optional<Vehicle> vehicleOptional = this.vehicleRepository.findById(vehicleId);
        if (vehicleOptional.isEmpty()) {
            throw new EntityNotFoundException(String.format("No vehicle exists with the id: %s", vehicleId));
        }

        Vehicle vehicle = vehicleOptional.get();

        if (vehicleUpdateDTO.getName() != null && !vehicleUpdateDTO.getName().trim().isEmpty()) {
            vehicle.setName(vehicleUpdateDTO.getName());
        }

        if (vehicleUpdateDTO.getYear() != null) {
            vehicle.setYear(vehicleUpdateDTO.getYear());
        }

        if (vehicleUpdateDTO.getBrand() != null && !vehicleUpdateDTO.getBrand().trim().isEmpty()) {
            vehicle.setBrand(vehicleUpdateDTO.getBrand());
        }

        if (vehicleUpdateDTO.getCategory() != null && !vehicleUpdateDTO.getCategory().trim().isEmpty()) {
            vehicle.setCategory(vehicleUpdateDTO.getCategory());
        }

        if (vehicleUpdateDTO.getLicensePlate() != null && !vehicleUpdateDTO.getLicensePlate().trim().isEmpty()) {
            vehicle.setLicensePlate(vehicleUpdateDTO.getLicensePlate());
        }

        if (vehicleUpdateDTO.getColor() != null && !vehicleUpdateDTO.getColor().trim().isEmpty()) {
            vehicle.setColor(vehicleUpdateDTO.getColor());
        }

        Vehicle updatedVehicle = this.vehicleRepository.save(vehicle);

        return new VehicleDTO(updatedVehicle);

    }


    public void deleteVehicle(String vehicleId) {

        Optional<Vehicle> vehicleOptional = this.vehicleRepository.findById(vehicleId);

        if (vehicleOptional.isEmpty()) {
            throw new EntityNotFoundException(String.format("No vehicle exists with the id: %s", vehicleId));
        }

        this.vehicleRepository.deleteById(vehicleId);

    }

    public List<Vehicle> checkVehicles(List<String> vehicleDTOList) {

        List<Vehicle> returnList = new ArrayList<>();
        for (String vehicleDTOId: vehicleDTOList) {
            Optional<Vehicle> optionalVehicle = this.vehicleRepository.findById(vehicleDTOId);
            if(optionalVehicle.isEmpty()) throw new EntityNotFoundException(String.format("No vehicle exists with the id: %s", vehicleDTOId));
            returnList.add(optionalVehicle.get());
        }

        return returnList;

    }
}
