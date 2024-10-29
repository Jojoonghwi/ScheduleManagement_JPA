package com.sparta.schedulemanagement_jpa.domain.user.controller.dto;

import com.sparta.schedulemanagement_jpa.domain.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<Long> scheduleId;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.createdAt = user.getCreatedAt();
        this.modifiedAt = user.getModifiedAt();
        this.scheduleId = user.getScheduleUserList().stream()
                .map(scheduleUser -> scheduleUser.getUser().getId())
                .collect(Collectors.toList());
    }
}
