package com.sparta.schedulemanagement_jpa.domain.schedule.entity;

import com.sparta.schedulemanagement_jpa.domain.schedule.controller.dto.CommentRequestDto;
import com.sparta.schedulemanagement_jpa.domain.Timestamped;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // JPA가 관리할 수 있는 Entity 클래스 지정
@Table(name = "comment") // 매핑할 테이블의 이름을 지정
@Getter
@Setter
@NoArgsConstructor
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @Column(name = "contents", nullable = false)
    private String contents;

    public Comment(CommentRequestDto requestDto) {
        this.contents = requestDto.getContents();
    }

    public void update(CommentRequestDto requestDto) {
        this.contents = requestDto.getContents();
    }
}
