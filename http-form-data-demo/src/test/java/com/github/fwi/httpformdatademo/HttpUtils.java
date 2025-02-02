package com.github.fwi.httpformdatademo;

import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpUtils {

    private HttpUtils() {
        // NO-OP
    }
 
    public static String basicAuth(String username, String password) {
        return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes(UTF_8));
    }

}
