package com.github.fwi.httpformdatademo;

import java.security.Principal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.github.fwi.httpformdatademo.security.UserAccessService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class HomeController {
    
    @GetMapping("/")
    ResponseEntity<String> home() {
        var t = ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) + System.lineSeparator(); 
        return ResponseEntity.ok(t);
    }

    @Secured(UserAccessService.ROLE_USER)
    @GetMapping("/me")
    ResponseEntity<String> me(Principal principal) {
        var u = principal.getName();
        if (principal instanceof UsernamePasswordAuthenticationToken ud) {
            u += " - roles: " + ud.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        }
        u += System.lineSeparator();
        return ResponseEntity.ok(u);
    }
}
