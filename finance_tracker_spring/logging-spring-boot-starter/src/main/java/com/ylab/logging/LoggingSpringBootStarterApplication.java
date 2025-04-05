package com.ylab.logging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class LoggingSpringBootStarterApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoggingSpringBootStarterApplication.class, args);
	}

}
