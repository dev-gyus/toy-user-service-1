package com.example.toyuserservice.service;

import com.example.toyuserservice.domain.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserDto.Common createUser(String email, String password, String nickName, String mobile);

    UserDto.Common findUser(Long userId);

    UserDto.Common updateUser(Long userId, String nickName, String mobile);
}
