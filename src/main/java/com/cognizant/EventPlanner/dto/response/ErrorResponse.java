package main.java.com.cognizant.EventPlanner.dto.response;

import java.time.LocalDateTime;

public class ErrorResponse {

    private LocalDateTime timeStamp;
    private int status;
    private String error;
    private String message;
    private String path;

}
