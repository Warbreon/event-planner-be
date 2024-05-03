package com.cognizant.EventPlanner.services.facade;

import com.cognizant.EventPlanner.services.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressManagementFacade {

    private final AddressService addressService;

    @Cacheable(value = "addresses")
    public List<String> getAllCities() {
        return addressService.findAllCities();
    }
}
