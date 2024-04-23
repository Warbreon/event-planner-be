package com.cognizant.EventPlanner.dto.email;

import com.cognizant.EventPlanner.constants.EmailType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BaseEmailDetailsDto {

    private EmailType emailType;
    private String recipientEmail;

}
