package com.cognizant.EventPlanner.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class DateValidationUtils {
    public boolean validateDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return startDate.isBefore(endDate);
    }
}
