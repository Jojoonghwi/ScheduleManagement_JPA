package com.sparta.schedulemanagement_jpa.domain.user.controller.dto;

import lombok.Getter;
import jakarta.validation.constraints.*;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequestDto implements UserRequestDto {
    private String username;
    private String email;
    private String password;
    private String confirmPassword;

    @Override
    public String getRole() {
        return null;
    }
}
