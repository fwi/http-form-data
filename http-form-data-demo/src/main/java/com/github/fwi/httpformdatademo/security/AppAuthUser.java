package com.github.fwi.httpformdatademo.security;

import java.util.Collection;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * Internal class used by {@link UserAccessService} for storing user-related security settings.
 * Converted from {@link AppUserProperties} by {@link UserAccessService}.
 */
@Data
@ToString
@AllArgsConstructor
public class AppAuthUser {
    
    String name;
    @ToString.Exclude
    String password;
    Collection<SimpleGrantedAuthority> authorities;
}