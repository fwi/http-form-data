package com.github.fwi.httpformdatademo.security;

import java.util.Collection;
import java.util.HashSet;

import lombok.Data;
import lombok.ToString;

/**
 * A data-class containing the properties for an application user.
 * This class is converted to an {@link AppAuthUser} by {@link UserAccessService}.
 */
@Data
@ToString
public class AppUserProperties {
    
    String username;
    @ToString.Exclude
    String password;
    Collection<String> roles = new HashSet<>();
}