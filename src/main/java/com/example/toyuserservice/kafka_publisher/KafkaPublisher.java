package com.example.toyuserservice.kafka_publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void publishTest(String message){
        log.info("카프카 퍼블리쉬 테스트 메소드 진입. message:{}", message);
        Map<String, String> map = new HashMap<>();
        map.put("message", message);
        try {
            String serializedString = new ObjectMapper().writeValueAsString(map);
            kafkaTemplate.send("test-topic", serializedString);
        } catch (JsonProcessingException e) {
            log.info("맵 객체 직렬화 에러");
        }
    }
}
