package com.example.toyuserservice.controller;

import com.example.toyuserservice.kafka_publisher.KafkaPublisher;
import com.example.toyuserservice.payload.kafka.KafkaTestRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {
    private final Environment env;
    private final KafkaPublisher kafkaPublisher;
    @GetMapping("/user")
    public ResponseEntity<String> getUser(){
        return new ResponseEntity<>("'username': 'gyus', 'mobile':'10-1234-1234'", HttpStatus.OK);
    }

    @GetMapping("/hello")
    public ResponseEntity<String> getHello(){
        log.info("user-client info: {}", env.getProperty("eureka.instance.instance-id"));
        return new ResponseEntity<>("Hello, this is user-client", HttpStatus.OK);
    }

    @GetMapping("/properties/all")
    public ResponseEntity<List<String>> getProperties(){
        return new ResponseEntity<>(Arrays.asList(env.getProperty("test.token-expire"), env.getProperty("test.salt")), HttpStatus.OK);
    }
//
    @PostMapping("/publish/test")
    public ResponseEntity<Boolean> kafkaPublishTest(@RequestBody KafkaTestRequest kafkaTestRequest){
        log.info("카프카 퍼블리쉬 테스트 메소드 진입");
        kafkaPublisher.publishTest(kafkaTestRequest.getMessage());
        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
    }
}
