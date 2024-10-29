package com.sparta.schedulemanagement_jpa.domain.user.controller.dto;

import lombok.Getter;
import jakarta.validation.constraints.*;
@Getter
public class UserRequestDto {
    @Size(max = 4, message = "유저명은 최대 4글자까지 가능합니다.")
    private String username;

    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "유효하지 않은 이메일 형식입니다.")
    @NotBlank
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
}
