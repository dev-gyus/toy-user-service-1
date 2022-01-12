package com.example.toyuserservice.payload.request;

import com.example.toyuserservice.constants.Constants;
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
    @Pattern(regexp = Constants.REGEX_MOBILE, message = "{params.invalid.mobile}")
    @NotBlank
    private String mobile;
    @NotBlank
    private String nickName;
}
