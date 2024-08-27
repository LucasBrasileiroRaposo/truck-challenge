package com.challange.truckManagement.repositories;

import com.challange.truckManagement.entities.Client;
import com.challange.truckManagement.entities.User;
import com.challange.truckManagement.entities.UserRole;
import com.challange.truckManagement.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

//    @Query("SELECT u FROM User u WHERE " +
//            "(:cpf IS NULL OR u.cpf = :cpf) AND " +
//            "(:name IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
//            "(:email IS NULL OR u.email = :email) AND " +
//            "(:role IS NULL OR u.role = :role) "
//    )
//    List<User> filterUsers(
//            @Param("cpf") String cpf,
//            @Param("name") String name,
//            @Param("email") String email,
//            @Param("role") UserRole role);

    @Query(value = "SELECT * FROM users u WHERE " +
            "(:cpf IS NULL OR u.cpf = :cpf) AND " +
            "(:name IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:email IS NULL OR u.email = :email) AND " +
            "(:role IS NULL OR u.role = :role) AND " +
            "(:birthDate IS NULL OR u.birth_date = :birthDate) AND " +
            "(:address IS NULL OR LOWER(u.address) LIKE LOWER(CONCAT('%', :address, '%')))",
            nativeQuery = true)
    List<User> filterUsers(
            @Param("cpf") String cpf,
            @Param("name") String name,
            @Param("email") String email,
            @Param("role") String role,
            @Param("birthDate") Date birthDate,
            @Param("address") String address);


//    @Query("SELECT DISTINCT c FROM User c JOIN c.vehicles v WHERE v.id IN :vehicleIds")
//    List<User> findClientsByVehicleIds(@Param("vehicleIds") List<Long> vehicleIds);


}
