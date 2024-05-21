package com.cognizant.EventPlanner.services.facade;

import com.cognizant.EventPlanner.dto.response.AddressResponseDto;
import com.cognizant.EventPlanner.mapper.AddressMapper;
import com.cognizant.EventPlanner.services.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressManagementFacade {

    private final AddressService addressService;
    private final AddressMapper addressMapper;

    @Cacheable(value = "addresses", key = "@userDetailsServiceImpl.getCurrentUserEmail()")
    public List<String> getAllCities() {
        return addressService.findAllCities();
    }

    public List<AddressResponseDto> getAllAddresses() {
        return addressService.findAllAddresses().stream().map(addressMapper::addressToDto).toList();
    }
}
