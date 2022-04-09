package com.example.toyuserservice.controller;

import com.example.toyuserservice.constants.Constants;
import com.example.toyuserservice.constants.MessageKeys;
import com.example.toyuserservice.constants.ModelMapperConstants;
import com.example.toyuserservice.domain.dto.UserDto;
import com.example.toyuserservice.payload.model.User;
import com.example.toyuserservice.payload.request.UserRequest;
import com.example.toyuserservice.payload.request.UserUpdateRequest;
import com.example.toyuserservice.payload.response.UserResponse;
import com.example.toyuserservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.InvalidParameterException;
import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final MessageSource messageSource;

    @PostMapping("")
    public ResponseEntity<UserResponse> createUser(@Valid  @RequestBody UserRequest userRequest){
        log.info("email:{} / mobile:{} / nickName:{}", userRequest.getEmail(), userRequest.getMobile(), userRequest.getNickName());
        // User 저장
        UserDto.Common userDto = userService.createUser(userRequest.getEmail(), userRequest.getPassword(), userRequest.getNickName(), userRequest.getMobile());
        // Dto -> Model 바인딩해서 응답
        return new ResponseEntity<>(UserResponse.builder().user(ModelMapperConstants.map(userDto, User.class)).build(), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> createUser(@PathVariable Long userId){
        log.info("userId:{}", userId);
        // 시퀀셜 Id로 User 찾기
        UserDto.Common userDto = userService.findUser(userId);
        // Dto -> Model 바인딩해서 응답
        return new ResponseEntity<>(UserResponse.builder().user(ModelMapperConstants.map(userDto, User.class)).build(), HttpStatus.OK);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long userId,
                                                   @RequestBody UserUpdateRequest userUpdateRequest){
        log.info("userId:{} / userUpdate:{}", userId, userUpdateRequest);
        // 휴대전화번호 수정시 패턴검사
        if(!Pattern.matches(Constants.REGEX_MOBILE, userUpdateRequest.getMobile()))
            throw new InvalidParameterException(messageSource.getMessage(MessageKeys.PARAMS_INVALID_MOBILE, null, LocaleContextHolder.getLocale()));
        // 정보 수정
        userService.updateUser(userId, userUpdateRequest.getNickName(), userUpdateRequest.getMobile());
        return new ResponseEntity<>(UserResponse.builder().user(null).build(), HttpStatus.OK);
    }



}
