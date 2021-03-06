package com.example.toyuserservice.repository.impl;

import com.example.toyuserservice.constants.DatabaseConstants;
import com.example.toyuserservice.domain.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static com.example.toyuserservice.constants.DatabaseConstants.*;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl {
    private final MongoOperations mongoOperations;

    public UserDto.Common updateUserDto(Long userId, String nickName, String mobile) {
        Query query = Query.query(new Criteria(Field.NAME_USER_ID).is(userId));
        Update update = new Update();
        if(nickName != null && !nickName.replaceAll(" ", "").equals("")) update.set(Field.NAME_NICK_NAME, nickName);
        if(mobile != null && !mobile.replaceAll(" ", "").equals("")) update.set(Field.NAME_MOBILE, mobile);
        // update할게 있을때만 업데이트
        if(!update.getUpdateObject().isEmpty()){
            update.set(Field.NAME_UPDATE_DATE, LocalDateTime.now(ZoneOffset.UTC));
            return mongoOperations.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true), UserDto.Common.class, Collection.NAME_USER);
        }
        return null;
    }

    public void updateUserDto(Long userId, Update update) {
        mongoOperations.updateFirst(Query.query(new Criteria(Field.NAME_USER_ID).is(userId)), update, Collection.NAME_USER);
    }
}
