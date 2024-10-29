package com.sparta.schedulemanagement_jpa.domain.scheduleUser.dto;

import com.sparta.schedulemanagement_jpa.domain.scheduleUser.entity.ScheduleUser;

import java.time.LocalDateTime;

public class ScheduleUserResponseDto {
    private Long id;
    private Long scheduleId;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public ScheduleUserResponseDto(ScheduleUser scheduleUser) {
        this.id = scheduleUser.getId();
        this.scheduleId = scheduleUser.getSchedule().getId();
        this.userId = scheduleUser.getUser().getId();
        this.createdAt = scheduleUser.getCreatedAt();
        this.modifiedAt = scheduleUser.getModifiedAt();
    }
}
