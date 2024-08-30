package com.challenge.truckManagement;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title="Swagger TruckManagementAPI", version = "1"))
public class TruckManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(TruckManagementApplication.class, args);
	}

}
