package com.phorus.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "com.phorus.gateway")
class RoutePaths(var paths: Map<String, String>?)