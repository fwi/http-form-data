package com.github.fwi.httpformdatademo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalManagementPort;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;

import com.github.fwi.httpformdatademo.security.AppUsersProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = { "management.server.port=0" })
@Slf4j
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
class HomeTest {

    final AppUsersProperties appUsersProperties;

    @LocalServerPort
    int webServerPort;

    @LocalManagementPort
    int managementPort;
    
    @Test
    void home() {

        var users = appUsersProperties.getUsers().size();
        log.info("App started with {} user(s).", users);
        Assertions.assertThat(users).isPositive();

        var webUri = "http://localhost:" + webServerPort;
        log.debug("Requesting {}", webUri);
        var client = RestClient.builder().baseUrl(webUri).build();
        var time = client.get().uri("/").retrieve().body(String.class);
        log.debug("Time: {}", time);
        Assertions.assertThat(time).contains("T");

        var me = client.get().uri("/me").header("Authorization", HttpUtils.basicAuth("demo", "omed")).retrieve().body(String.class);
        log.debug("Me: {}", me);
        Assertions.assertThat(me).contains("ROLE_USER");

        var manUri = "http://localhost:" + managementPort + "/actuator";
        var manClient = RestClient.builder().baseUrl(manUri).build();
        var health = manClient.get().uri("/health").retrieve().body(String.class);
        log.debug("Health: {}", health);
        Assertions.assertThat(health).contains("UP");
    }

}
