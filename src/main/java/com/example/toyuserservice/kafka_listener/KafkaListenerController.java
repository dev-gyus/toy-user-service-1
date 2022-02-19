package com.example.toyuserservice.kafka_listener;

import com.example.toyuserservice.constants.KafkaConstants;
import com.example.toyuserservice.constants.KafkaConstants.Topic;
import com.example.toyuserservice.domain.common.ErrorCode;
import com.example.toyuserservice.exception.CustomException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.bson.Document;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

@Slf4j
@Component
public class KafkaListenerController {

    @KafkaListener(topics = "testTopic", groupId = "client-service")
    public void testListen(String message) throws JsonProcessingException {
        KafkaTestDto dto = new ObjectMapper().readValue(message, KafkaTestDto.class);
        log.info("message data : {}", dto);
    }

    @KafkaListener(topics = Topic.UPDATE_USER, groupId = "client-service")
        public void updateUser(String message) throws IOException {
            log.info("data:{}", message);
        ObjectMapper objectMapper = KafkaTestDtoUserDeserialize.mongoObjectMapper();
        KafkaTestDto.User dto = objectMapper.readValue(message, KafkaTestDto.User.class);
    }

}
