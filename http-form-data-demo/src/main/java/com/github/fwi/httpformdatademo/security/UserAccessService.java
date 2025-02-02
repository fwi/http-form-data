package com.github.fwi.httpformdatademo.security;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;

import lombok.extern.slf4j.Slf4j;

/**
 * Implement our own user-service which does not use encrypted passwords.
 * The default one from Spring uses password encryption which is very slow 
 * (all requests take about 100ms just to authenticate).
 * 
 * If you do use encrypted passwords, make sure to cache "known good"
 * password hashes to prevent excessive CPU usage and delays.
 * A custom PasswordEncoder and/or AuthenticationManager will be required to do this. 
 */
@Slf4j
public class UserAccessService implements UserDetailsService {
    
    /**
     * Prefix for a permission that is a role.
     */
    public static final String ROLE_PREFIX = "ROLE_";

    public static final String ROLE_USER = ROLE_PREFIX + "USER";
    
    /**
     * Do not do any password encryption / decryption.
     */
    public static final String NOOP_ENCODER = "{noop}";
    
    final Map<String, AppAuthUser> users;
    
    public UserAccessService(Collection<AppUserProperties> users) {
        this.users = convert(users);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        var user = users.get(username);
        if (user == null) {
            log.info("Unknown user [{}].", username);
            return null;
        }
        log.debug("Request from user {}.", username);
        // The password gets nullified after usage, so must create new User each time.
        return new User(user.getName(), user.getPassword(), user.getAuthorities());
    }

    private Map<String, AppAuthUser> convert(Collection<AppUserProperties> users) {
        
        var usersAuth = new HashMap<String, AppAuthUser>();
        if (users == null || users.isEmpty()) {
            log.warn("No users configured.");
            return usersAuth;
        }
        for (var user : users) {
            var authorities = toAuthorities(user.getRoles());
            if (authorities.isEmpty()) {
                log.warn("User {} has no roles.", user.getUsername());
            }
            usersAuth.put(user.getUsername(), 
                    new AppAuthUser(user.getUsername(), NOOP_ENCODER + user.getPassword(), authorities));
        }
        return usersAuth;
    }
    
    private static Collection<SimpleGrantedAuthority> toAuthorities(Collection<String> roles) {
        
        var authorities = new HashSet<SimpleGrantedAuthority>();
        if (roles == null) {
            return authorities;
        }
        for (var authority: roles) {
            authority = StringUtils.trimToNull(authority);
            if (authority != null) {
                authority = ROLE_PREFIX + authority.toUpperCase(Locale.ENGLISH);
                authorities.add(new SimpleGrantedAuthority(authority));
            }
        }
        return authorities;
    }

}
