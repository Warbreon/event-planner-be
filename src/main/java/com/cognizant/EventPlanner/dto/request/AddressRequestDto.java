package com.cognizant.EventPlanner.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequestDto {

    private String city;
    private String street;
    private String building;
    private String zip;
    private String venueName;

}
