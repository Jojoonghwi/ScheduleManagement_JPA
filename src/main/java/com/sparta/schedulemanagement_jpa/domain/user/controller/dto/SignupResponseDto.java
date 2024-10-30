package com.sparta.schedulemanagement_jpa.domain.user.controller.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.sparta.schedulemanagement_jpa.domain.user.entity.User;
import com.sparta.schedulemanagement_jpa.domain.user.entity.UserRole;

import lombok.Getter;

@Getter
public class SignupResponseDto {
	private Long id;
	private String username;
	private String email;
	private UserRole role;
	private LocalDateTime createdAt;


	public SignupResponseDto(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.role = user.getRole();
		this.createdAt = user.getCreatedAt();
	}
}
