package com.example.toyuserservice.kafka_listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class KafkaListenerController {

    @KafkaListener(topics = "testTopic", groupId = "client-service")
    public void testListen(String message) throws JsonProcessingException {
        KafkaTestDto dto = new ObjectMapper().readValue(message, KafkaTestDto.class);
        log.info("message data : {}", dto);
    }


}
