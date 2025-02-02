package com.github.fwi.httpformdatademo.security;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.metrics.export.prometheus.PrometheusScrapeEndpoint;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(prePostEnabled = false, securedEnabled = true)
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain secure(HttpSecurity http) throws Exception {

        http.csrf(c -> c.disable());
        http.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        
        http.authorizeHttpRequests(r -> {
            r.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll();
            r.requestMatchers("/").permitAll();
            r.requestMatchers(EndpointRequest.to(HealthEndpoint.class, PrometheusScrapeEndpoint.class)).permitAll();
            
            r.anyRequest().authenticated();
        });
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    UserAccessService userAccessService(AppUsersProperties usersProps) {
        return new UserAccessService(usersProps.getUsers());
    }

}
