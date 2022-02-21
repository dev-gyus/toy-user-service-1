package com.example.toyuserservice.kafka_listener;

import com.example.toyuserservice.constants.KafkaConstants.Topic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaListenerController {
    private final ObjectMapper kafkaObjectMapper;

    @KafkaListener(topics = "testTopic", groupId = "client-service")
    public void testListen(String message) throws JsonProcessingException {
        KafkaTestDto dto = new ObjectMapper().readValue(message, KafkaTestDto.class);
        log.info("message data : {}", dto);
    }

    @KafkaListener(topics = Topic.UPDATE_USER, groupId = "client-service")
        public void updateUser(String message) throws IOException {
            log.info("data:{}", message);
        KafkaTestDto.User dto = kafkaObjectMapper.readValue(message, KafkaTestDto.User.class);
    }

    @KafkaListener(topics = Topic.UPDATE_USER2, groupId = "client-service")
    public void updateUser2(String message) throws IOException {
        log.info("data:{}", message);
        KafkaTestDto.User2 dto = kafkaObjectMapper.readValue(message, KafkaTestDto.User2.class);
    }

}
