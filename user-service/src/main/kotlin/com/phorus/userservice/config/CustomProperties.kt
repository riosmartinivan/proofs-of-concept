package com.phorus.userservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "com.phorus.userservice.services")
class CustomProperties(var petServiceUrl: String = "http://pet-service:8080")