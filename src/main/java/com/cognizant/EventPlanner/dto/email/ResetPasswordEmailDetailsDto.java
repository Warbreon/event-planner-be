package com.cognizant.EventPlanner.dto.email;

import com.cognizant.EventPlanner.constants.EmailType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResetPasswordEmailDetailsDto extends BaseEmailDetailsDto {

    private String name;
    private String resetLink;

    public ResetPasswordEmailDetailsDto(String recipientEmail, String resetLink, String name) {
        super(EmailType.PASSWORD_RESET, recipientEmail);
        this.resetLink = resetLink;
        this.name = name;
    }

}
