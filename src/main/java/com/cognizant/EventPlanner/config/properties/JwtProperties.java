package com.cognizant.EventPlanner.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String secret = "AgSqL8uNJdfQosx0nnChojH6IQ4HATVpC01PppjDSM6cQ7In9EhUi+iIsShjDuPcQU5APFFbGmF20ztMVb0A0A==";
    private long expiration = 86400;

}
