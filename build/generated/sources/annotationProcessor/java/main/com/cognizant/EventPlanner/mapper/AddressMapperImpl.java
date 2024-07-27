package com.cognizant.EventPlanner.mapper;

import com.cognizant.EventPlanner.dto.response.AddressResponseDto;
import com.cognizant.EventPlanner.model.Address;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-28T23:14:49+0300",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.6.jar, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class AddressMapperImpl implements AddressMapper {

    @Override
    public AddressResponseDto addressToDto(Address address) {
        if ( address == null ) {
            return null;
        }

        AddressResponseDto addressResponseDto = new AddressResponseDto();

        addressResponseDto.setId( address.getId() );
        addressResponseDto.setCity( address.getCity() );
        addressResponseDto.setStreet( address.getStreet() );
        addressResponseDto.setBuilding( address.getBuilding() );
        addressResponseDto.setZip( address.getZip() );
        addressResponseDto.setVenueName( address.getVenueName() );

        return addressResponseDto;
    }
}
