package com.example.toyuserservice.kafka_publisher;

import com.example.toyuserservice.constants.KafkaConstants;
import com.example.toyuserservice.kafka_listener.KafkaTestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaFailureCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.HashMap;
import java.util.Map;

import static com.example.toyuserservice.constants.KafkaConstants.Topic.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Retryable(value = KafkaException.class, maxAttempts = 1, backoff = @Backoff(value = 500L))
    public void publishTest(String message, String updateTestValue) throws KafkaException {
//        log.info("카프카 퍼블리쉬 테스트 메소드 진입. message:{}", message);
//        Map<String, Object> map = new HashMap<>();
//        map.put("message", message);
//        map.put("update", Update.update("update", updateTestValue).getUpdateObject());
//        map.put("id", 1L);
//        // send message
//        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(UPDATE_USER, map);
//        // async callback
//        future.addCallback(result -> {
//            log.info("message send success.");
//        }, (KafkaFailureCallback<String, Object>) ex -> {
//            log.error("message send failed. message:{}", ex.getLocalizedMessage());
//            throw ex;
//        });
    }
}
