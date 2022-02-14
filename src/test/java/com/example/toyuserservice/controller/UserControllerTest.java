package com.example.toyuserservice.controller;

import com.example.toyuserservice.domain.dao.UserDao;
import com.example.toyuserservice.domain.dto.UserDto;
import com.example.toyuserservice.exception.CustomException;
import com.example.toyuserservice.exception.GlobalExceptionHandler;
import com.example.toyuserservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.converters.Auto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
public class UserControllerTest {

    @Autowired
    private UserController userController;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void init(){
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }


    private UserDto.Common getSignUpDao() {
        return UserDto.Common.builder()
                .email("cjsworbehd20@gmail.com")
                .password(passwordEncoder.encode("123456789"))
                .nickName("규스스스")
                .mobile("010-1234-5678")
                .build();
    }

    @Test
    @DisplayName("회원가입")
    public void signUpSuccess() throws Exception{
        //given
        UserDto.Common signUpDto = getSignUpDao();

        //when
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpDto)))
        //then
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 이메일")
    public void signUpFailedByDuplicatedEmail() throws Exception{
        //given
        UserDto.Common signUpDto = getSignUpDao();
        UserDto.Common signUpDto2 = getSignUpDao();
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpDto)));

        //when
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpDto2)))
        //then
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(Objects.requireNonNull(result.getResolvedException()).getClass().isAssignableFrom(CustomException.class)))
                .andDo(print());

    }

}
