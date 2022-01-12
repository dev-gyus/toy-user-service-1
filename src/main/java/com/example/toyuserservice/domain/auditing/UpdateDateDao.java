package com.example.toyuserservice.domain.auditing;

import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

public class UpdateDateDao {
    @LastModifiedDate
    private LocalDateTime updateDate;
}
