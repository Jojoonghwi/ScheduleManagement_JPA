package com.sparta.schedulemanagement_jpa.domain.user.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class LoginRequestDto implements UserRequestDto {
	private String email;
	private String password;

	@Override
	public String getUsername() {
		return null;
	}

	@Override
	public String getRole() {
		return null;
	}

	@Override
	public String getConfirmPassword() {
		return null;
	}
}
