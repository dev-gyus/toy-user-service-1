package com.example.toyuserservice.domain.dao;

import com.example.toyuserservice.domain.auditing.CreateDateDao;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
