package com.example.toyuserservice.kafka_listener;

import com.example.toyuserservice.constants.KafkaConstants.Topic;
import com.example.toyuserservice.repository.impl.UserRepositoryImpl;
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
    private final UserRepositoryImpl userRepository;

    @KafkaListener(topics = "testTopic", groupId = "client-service")
    public void testListen(String message) throws JsonProcessingException {
        KafkaTestDto dto = new ObjectMapper().readValue(message, KafkaTestDto.class);
        log.info("message data : {}", dto);
    }

    @KafkaListener(topics = Topic.UPDATE_USER, groupId = "client-service")
        public void updateUser(KafkaTestDto.User dto) throws IOException {
            log.info("data:{}", dto);
//        KafkaTestDto.User dto = kafkaObjectMapper.readValue(message, KafkaTestDto.User.class);
//        KafkaTestDto.User userDto = (KafkaTestDto.User) dto;
        if(dto.getUpdate() == null || dto.getUpdate().getUpdateObject().isEmpty()) return;
        userRepository.updateUserDto(dto.getUserId(), dto.getUpdate());
    }

    @KafkaListener(topics = Topic.UPDATE_USER2, groupId = "client-service")
    public void updateUser2(KafkaTestDto.User2 dto) throws IOException {
        log.info("data:{}", dto);
//        KafkaTestDto.User2 dto = kafkaObjectMapper.readValue(message, KafkaTestDto.User2.class);
        log.info(dto.toString());
        if(dto.getUpdate() == null || dto.getUpdate().getUpdateObject().isEmpty()) return;
        userRepository.updateUserDto(dto.getUserId(), dto.getUpdate());
    }

}
