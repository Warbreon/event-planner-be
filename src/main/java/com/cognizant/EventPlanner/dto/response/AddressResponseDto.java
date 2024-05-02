package com.cognizant.EventPlanner.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponseDto {

    private Long id;
    private String city;
    private String street;
    private String building;
    private String zip;
    private String venueName;

}
