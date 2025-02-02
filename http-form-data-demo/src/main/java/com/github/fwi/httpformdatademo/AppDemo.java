package com.github.fwi.httpformdatademo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import com.github.fwi.httpformdatademo.security.AppUsersProperties;
import com.github.fwi.httpformdatademo.security.WebSecurityConfig;

@SpringBootConfiguration
@EnableAutoConfiguration
@EnableConfigurationProperties(AppUsersProperties.class)
@Import(WebSecurityConfig.class)
public class AppDemo {

    public static void main(String[] args) {
        
        SpringApplication.run(AppDemo.class, args);
    }

    @Bean
    HomeController homeController() {
        return new HomeController();
    }

    @Bean
    PartController partController() {
        return new PartController();
    }
}
