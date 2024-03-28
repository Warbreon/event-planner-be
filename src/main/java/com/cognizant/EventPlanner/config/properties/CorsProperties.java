package com.cognizant.EventPlanner.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {

    private String[] allowedOrigins = new String[]{"http://localhost:3000"};
    private String[] allowedMethods = new String[]{"GET", "POST", "PUT", "PATCH", "DELETE"};
    private String[] allowedHeaders = new String[]{"*"};
    private String[] exposedHeaders = new String[]{"*"};

}
