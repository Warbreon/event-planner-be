package com.cognizant.EventPlanner.repository;

import com.cognizant.EventPlanner.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("SELECT DISTINCT city FROM Address ORDER BY city")
    List<String> findAllCities();
}
