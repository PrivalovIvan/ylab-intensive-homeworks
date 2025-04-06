package com.ylab.finance_tracker_spring_boot;

import com.ylab.auditing.annotation.EnableUserAuditing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableAsync
@EnableUserAuditing
public class FinanceTrackerSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinanceTrackerSpringApplication.class, args);
    }

}
