package com.example.remindclientservice.repository;

import com.example.remindclientservice.domain.dao.UserDao;
import com.example.remindclientservice.domain.dto.UserDto;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserDao, String> {
    boolean existsByEmail(String email);

    boolean existsByNickName(String nickName);

    // Dto로 바로 프로젝션
    UserDto.Common findByUserId(Long userId);
}
