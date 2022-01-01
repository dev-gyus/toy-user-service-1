package com.example.remindclientservice.domain.auditing;

import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

public class CreateDateDao extends UpdateDateDao{
    @CreatedDate
    private LocalDateTime createDate;
}
