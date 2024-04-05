package com.cognizant.EventPlanner.dto.response;

import com.cognizant.EventPlanner.dto.request.AddressRequestDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AddressResponseDto extends AddressRequestDto {

    private Long id;

}
