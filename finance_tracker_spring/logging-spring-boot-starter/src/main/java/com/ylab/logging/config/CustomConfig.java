package com.ylab.logging.config;

import com.ylab.logging.aspect.LoggableAspect;
import com.ylab.logging.listener.CustomListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomConfig {
    @Bean
    CustomListener customListener() {
        return new CustomListener();
    }
    @Bean
    LoggableAspect loggableAspect() {
        return new LoggableAspect();
    }
}
