package com.cognizant.EventPlanner.dto.email;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ResetPasswordEmailDetailsDto extends BaseEmailDetailsDto {

    private String resetLink;

}
