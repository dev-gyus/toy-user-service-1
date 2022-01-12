package com.example.toyuserservice.repository.template;

import com.example.toyuserservice.constants.DatabaseConstants;
import com.example.toyuserservice.domain.dao.CollectionIdGenerateDao;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
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
