package com.example.toyuserservice.kafka_listener;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.kafka.support.JacksonUtils;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public class KafkaTestDtoUserDeserialize extends JsonDeserializer<KafkaTestDto.User> {

    public KafkaTestDtoUserDeserialize() {
        super(mongoObjectMapper());
    }

    public static ObjectMapper mongoObjectMapper(){
        ObjectMapper objectMapper = JacksonUtils.enhancedObjectMapper();
        SimpleModule module = new SimpleModule("DefaultMongoUpdateDeserializer", new Version(1, 0, 0, null, null, null));
        module.addDeserializer(KafkaTestDto.User.class, new DefaultMongoUpdateDeserializer());
        objectMapper.registerModule(module);
        return objectMapper;
    }
}
