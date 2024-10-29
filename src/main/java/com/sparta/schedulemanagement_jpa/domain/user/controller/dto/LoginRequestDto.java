package com.sparta.schedulemanagement_jpa.domain.user.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class LoginRequestDto {
	@Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "유효하지 않은 이메일 형식입니다.")
	@NotBlank
	private String email;

	private String password;
}
