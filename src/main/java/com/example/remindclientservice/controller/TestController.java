package com.example.remindclientservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final Environment env;

    @GetMapping("/user")
    public ResponseEntity<String> getUser(){
        return new ResponseEntity<>("'username': 'gyus', 'mobile':'10-1234-1234'", HttpStatus.OK);
    }

    @GetMapping("/hello")
    public ResponseEntity<String> getHello(){
        return new ResponseEntity<>("Hello, this is user-client", HttpStatus.OK);
    }

    @GetMapping("/properties/all")
    public ResponseEntity<List<String>> getProperties(){
        return new ResponseEntity<>(Arrays.asList(env.getProperty("test.token-expire"), env.getProperty("test.salt")), HttpStatus.OK);
    }
}
