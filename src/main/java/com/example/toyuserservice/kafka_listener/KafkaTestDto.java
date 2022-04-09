package com.example.toyuserservice.kafka_listener;

import lombok.*;
import org.bson.Document;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;


@Data
@NoArgsConstructor
@AccessType(AccessType.Type.PROPERTY)
public class KafkaTestDto {
    private Long id;
    private Update update;
    private Document document;
    private List<Long> idList;

    public KafkaTestDto(Long id, Document document, List<Long> idList) {
        this.id = id;
        this.document = document;
        this.idList = idList;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @AccessType(AccessType.Type.PROPERTY)
    public static class User extends KafkaTestDto {
        private Long userId;
        private Update update;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @AccessType(AccessType.Type.PROPERTY)
    public static class User2 extends KafkaTestDto {
        private Long userId;
        private Update update;
    }
}
