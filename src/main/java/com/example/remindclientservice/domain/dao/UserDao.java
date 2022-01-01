package com.example.remindclientservice.domain.dao;

import com.example.remindclientservice.domain.auditing.CreateDateDao;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.annotation.Documented;

@Getter
@Document("user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDao extends CreateDateDao {
    @Id
    private ObjectId id;
    private Long userId;
    private String email;
    private String password;
    private String mobile;
    private String nickName;

    public UserDao(Long userId, String email, String password, String mobile, String nickName) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.nickName = nickName;
    }
}
