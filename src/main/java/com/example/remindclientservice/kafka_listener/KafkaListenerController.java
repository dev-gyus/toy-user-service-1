package com.example.remindclientservice.kafka_listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class KafkaListenerController {

    @KafkaListener(topics = "test-topic", groupId = "client-service")
    public void testListen(String kafkaMessage){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<Object, Object> map = objectMapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {
            });
            log.info("KafkaMessage Parse 성공. parsedString:{}", map.get("message"));
        } catch (JsonProcessingException e) {
            log.error("KafkaMessage Parsing중 에러 발생. message:{}", e.getLocalizedMessage());
        }
    }


}
