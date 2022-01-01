package com.example.remindclientservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

public class UserDto {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Common{
        @Field("userId")
        private Long id;
        private String email;
        private String password;
        private String mobile;
        private String nickName;
        private LocalDateTime createDate;
        private LocalDateTime updateDate;
    }
}
