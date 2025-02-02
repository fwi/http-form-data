package com.github.fwi.httpformdatademo.security;

import java.util.Collection;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * A data-class containing a list of {@link AppUserProperties}.
 */
@ConfigurationProperties(prefix = AppUsersProperties.USERS_PREFIX)
@Data
public class AppUsersProperties {

    public static final String USERS_PREFIX = "http-form-data-demo.access";

    private Collection<AppUserProperties> users;

}
