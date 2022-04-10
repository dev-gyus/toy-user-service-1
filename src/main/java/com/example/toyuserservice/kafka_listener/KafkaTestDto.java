package com.example.toyuserservice.kafka_listener;

import lombok.*;
import org.bson.Document;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;


public class KafkaTestDto {

    @Data
    @NoArgsConstructor
    @AccessType(AccessType.Type.PROPERTY)
    public static class User {
        private Long userId;
        private Document document;
        private Update update;
        private String child;

        public User(Long userId, Document document, String child) {
            this.userId = userId;
            this.document = document;
            this.child = child;
        }
    }

    @Data
    @NoArgsConstructor
    @AccessType(AccessType.Type.PROPERTY)
    public static class User2{
        private Long userId;
        private Document document;
        private Update update;
        private String child;
        public User2(Long userId, Document document, String child) {
            this.userId = userId;
            this.document = document;
            this.child = child;
        }
    }
}
