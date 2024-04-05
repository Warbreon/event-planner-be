package com.cognizant.EventPlanner.mapper;

import com.cognizant.EventPlanner.dto.response.AddressResponseDto;
import com.cognizant.EventPlanner.model.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressResponseDto addressToDto(Address address);
}
