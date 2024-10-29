package com.sparta.schedulemanagement_jpa.domain.schedule.controller.dto;

import com.sparta.schedulemanagement_jpa.domain.schedule.entity.Schedule;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ScheduleResponseDto {
    private Long id;
    @Size(max = 10)
    private String title;
    @Size(max = 10)
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int commentCount;
    private List<Long> userIds;
    private String weather;

    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.title = schedule.getTitle();
        this.contents = schedule.getContents();
        this.createdAt = schedule.getCreatedAt();
        this.modifiedAt = schedule.getModifiedAt();
        this.commentCount = schedule.getCommentList().size();
        this.weather = schedule.getWeather();

        this.userIds = schedule.getScheduleUserList().stream()
                .map(scheduleUser -> scheduleUser.getUser().getId())
                .collect(Collectors.toList());
    }

}
