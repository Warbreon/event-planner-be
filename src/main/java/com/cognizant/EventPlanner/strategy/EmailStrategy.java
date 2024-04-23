package com.cognizant.EventPlanner.strategy;

import com.cognizant.EventPlanner.constants.EmailType;
import com.cognizant.EventPlanner.dto.email.BaseEmailDetailsDto;
import com.cognizant.EventPlanner.model.User;

import java.util.Map;

public interface EmailStrategy {

    EmailType getEmailType();
    String getSubject();
    Map<String, Object> getProperties(BaseEmailDetailsDto emailDetailsDto);
    String getTemplateName();

}
