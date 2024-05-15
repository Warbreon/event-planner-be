package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.dto.email.BaseEmailDetailsDto;

public interface EmailService {

    void sendEmail(BaseEmailDetailsDto emailDetailsDto);

}
