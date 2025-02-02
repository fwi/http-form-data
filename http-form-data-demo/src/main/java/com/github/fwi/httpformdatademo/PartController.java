package com.github.fwi.httpformdatademo;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.github.fwi.httpformdatademo.security.UserAccessService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@Secured(UserAccessService.ROLE_USER)
public class PartController {

    @PostMapping(path = "/parts/1", consumes = "multipart/form-data")
    ResponseEntity<Object> partsOne(@RequestParam String textPartOne, MultipartFile filePartOne) throws IOException
    {
        log.info("1: Received text part: {}", textPartOne);
        var b = filePartOne.getResource().getContentAsByteArray();
        log.info("1: Received file part: {} (size {} bytes)", filePartOne.getOriginalFilename(), b.length);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping(path = "/parts/2", consumes = "multipart/form-data")
    ResponseEntity<Object> partsTwo(@RequestParam("first") MultipartFile one, @RequestParam("message") String two,
            @RequestParam("many") MultipartFile[] alot) throws IOException
    {
        log.info("2: Received file part: {} (size {} bytes)", one.getOriginalFilename(), one.getSize());
        log.info("2: Received text part: {}", two);
        var s = "2: Received " + alot.length + " many part(s)";
        for (var f : alot) {
            s += " | " + f.getOriginalFilename() + " / " + f.getSize(); // NOSONAR
        }
        log.info(s);
        return ResponseEntity.ok().build();
    }

}
