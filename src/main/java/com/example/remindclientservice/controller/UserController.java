package com.example.remindclientservice.controller;

import com.example.remindclientservice.constants.ModelMapperConstants;
import com.example.remindclientservice.domain.dto.UserDto;
import com.example.remindclientservice.payload.request.UserRequest;
import com.example.remindclientservice.payload.response.UserResponse;
import com.example.remindclientservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("")
    public ResponseEntity<UserResponse> createUser(@Valid  @RequestBody UserRequest userRequest){
        log.info("email:{} / mobile:{} / nickName:{}", userRequest.getEmail(), userRequest.getMobile(), userRequest.getNickName());
        // User 저장
        UserDto.Common userDto = userService.createUser(userRequest.getEmail(), userRequest.getPassword(), userRequest.getNickName(), userRequest.getMobile());
        // Dto -> Model 바인딩해서 응답
        return new ResponseEntity<>(ModelMapperConstants.map(userDto, UserResponse.class), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> createUser(@PathVariable Long userId){
        log.info("userId:{}", userId);
        // 시퀀셜 Id로 User 찾기
        UserDto.Common userDto = userService.findUser(userId);
        // Dto -> Model 바인딩해서 응답
        return new ResponseEntity<>(ModelMapperConstants.map(userDto, UserResponse.class), HttpStatus.OK);
    }

}
