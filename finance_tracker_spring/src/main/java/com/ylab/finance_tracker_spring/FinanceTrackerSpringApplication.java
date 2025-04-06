package com.ylab.finance_tracker_spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class FinanceTrackerSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinanceTrackerSpringApplication.class, args);
    }

}
