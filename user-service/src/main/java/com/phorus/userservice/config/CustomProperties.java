package com.phorus.userservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration @Getter @Setter
@ConfigurationProperties(prefix = "com.phorus.userservice.services")
public class CustomProperties {

    private String petServiceUrl;
}
