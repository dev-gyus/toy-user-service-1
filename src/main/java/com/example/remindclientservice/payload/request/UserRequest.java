package com.example.remindclientservice.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @Pattern(regexp = "\\d{3}-\\d{4}-\\d{4}")
    @NotBlank
    private String mobile;
    @NotBlank
    private String nickName;
}
