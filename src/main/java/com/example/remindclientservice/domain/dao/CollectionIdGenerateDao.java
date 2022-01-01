package com.example.remindclientservice.domain.dao;

import com.example.remindclientservice.constants.DatabaseConstants;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(DatabaseConstants.Collection.NAME_COLLECTION_ID_GENERATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CollectionIdGenerateDao {
    @Id
    private ObjectId id;
    private String collectionName;
    private Long sequentialId;
}
