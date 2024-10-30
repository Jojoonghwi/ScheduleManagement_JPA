package com.sparta.schedulemanagement_jpa.domain.user.controller.dto;

public interface UserRequestDto {
	String getUsername();
	String getEmail();
	String getPassword();
	String getRole();
	String getConfirmPassword();
}
