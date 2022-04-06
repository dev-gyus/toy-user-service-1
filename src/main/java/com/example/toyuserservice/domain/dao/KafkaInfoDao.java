package com.example.toyuserservice.domain.dao;

import com.example.toyuserservice.constants.DatabaseConstants;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.example.toyuserservice.constants.DatabaseConstants.Collection.*;

@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Document(NAME_KAFKA_INFO)
public class KafkaInfoDao {
    @Id
    private ObjectId id;
    private Long kafkaInfoId;
    private String info;

    public KafkaInfoDao(Long kafkaInfoId, String info) {
        this.kafkaInfoId = kafkaInfoId;
        this.info = info;
    }
}

