package com.example.remindclientservice.repository.template;

import com.example.remindclientservice.constants.DatabaseConstants;
import com.example.remindclientservice.domain.dao.CollectionIdGenerateDao;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class CollectionIdGenerateRepository {
    private final MongoOperations mongoOperations;

    public Long generateId(String collectionName){
        Query query = new Query(new Criteria(DatabaseConstants.Field.NAME_COLLECTION_NAME).is(collectionName));
        Update update = new Update();
        update.inc(DatabaseConstants.Field.NAME_SEQUENTIAL_ID);
        return Objects.requireNonNull(mongoOperations.findAndModify(
                query
                , update
                , FindAndModifyOptions.options().returnNew(true).upsert(true), CollectionIdGenerateDao.class)).getSequentialId();
    }
}
