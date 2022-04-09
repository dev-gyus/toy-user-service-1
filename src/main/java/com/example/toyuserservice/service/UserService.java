package com.example.toyuserservice.service;

import com.example.toyuserservice.domain.dto.UserDto;
import org.springframework.kafka.KafkaException;

public interface UserService {
    UserDto.Common createUser(String email, String password, String nickName, String mobile);

    UserDto.Common findUser(Long userId);

    void updateUser(Long userId, String nickName, String mobile) throws KafkaException;
}
