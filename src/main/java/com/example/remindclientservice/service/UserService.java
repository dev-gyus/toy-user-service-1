package com.example.remindclientservice.service;

import com.example.remindclientservice.domain.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserDto.Common createUser(String email, String password, String nickName, String mobile);

    UserDto.Common findUser(Long userId);
}
