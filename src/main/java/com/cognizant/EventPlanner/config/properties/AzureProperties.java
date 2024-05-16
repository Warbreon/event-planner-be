package com.cognizant.EventPlanner.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "azure")
public class AzureProperties {

    private String endpoint;
    private String sasToken;

}
