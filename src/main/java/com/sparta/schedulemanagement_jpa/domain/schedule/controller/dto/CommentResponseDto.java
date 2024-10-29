package com.sparta.schedulemanagement_jpa.domain.schedule.controller.dto;

import com.sparta.schedulemanagement_jpa.domain.schedule.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private Long id;
    private String contents;
    private Long scheduleId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.contents = comment.getContents();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.scheduleId = comment.getSchedule().getId();
    }
}
