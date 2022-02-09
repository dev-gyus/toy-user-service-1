package com.example.toyuserservice.kafka_listener;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.Document;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.mongodb.core.query.Update;


@Data
@NoArgsConstructor
@AccessType(AccessType.Type.PROPERTY)
public class KafkaTestDto {
    private String message;
    private Long id;
    private Update update;

    public void setUpdate(Document update) {
        this.update = Update.fromDocument(update);
    }
}
