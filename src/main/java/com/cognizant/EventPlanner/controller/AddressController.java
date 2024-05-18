package com.cognizant.EventPlanner.controller;

import com.cognizant.EventPlanner.dto.response.AddressResponseDto;
import com.cognizant.EventPlanner.services.facade.AddressManagementFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressManagementFacade addressManagementFacade;

    @GetMapping
    public ResponseEntity<List<AddressResponseDto>> getAllAddresses() {
        List<AddressResponseDto> addresses = addressManagementFacade.getAllAddresses();
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/cities")
    public ResponseEntity<List<String>> getAllCities() {
        List<String> addresses = addressManagementFacade.getAllCities();
        return ResponseEntity.ok(addresses);
    }
}
