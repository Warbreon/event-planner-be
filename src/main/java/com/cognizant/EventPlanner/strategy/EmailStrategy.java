package com.cognizant.EventPlanner.strategy;

import com.cognizant.EventPlanner.dto.email.EmailType;
import com.cognizant.EventPlanner.dto.email.BaseEmailDetailsDto;

import java.util.Map;

public interface EmailStrategy {

    EmailType getEmailType();
    String getSubject();
    Map<String, Object> getProperties(BaseEmailDetailsDto emailDetailsDto);
    String getTemplateName();

}
