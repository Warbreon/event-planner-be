package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.exception.EntityNotFoundException;
import com.cognizant.EventPlanner.model.Address;
import com.cognizant.EventPlanner.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;


    public Address findAddressById(Long id) {
        if (id == null || id == 0) {
            return null;
        }

        return addressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Address.class, id));
    }

    public List<String> findAllCities() {
        return addressRepository.findAllCities();
    }

    public List<Address> findAllAddresses() {
        return addressRepository.findAll();
    }

}
