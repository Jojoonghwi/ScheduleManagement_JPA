package com.sparta.schedulemanagement_jpa.domain.schedule.entity;

import com.sparta.schedulemanagement_jpa.domain.schedule.controller.dto.ScheduleRequestDto;
import com.sparta.schedulemanagement_jpa.domain.scheduleUser.entity.ScheduleUser;
import com.sparta.schedulemanagement_jpa.domain.Timestamped;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity // JPA가 관리할 수 있는 Entity 클래스 지정
@Table(name = "schedule") // 매핑할 테이블의 이름을 지정
@Getter
@Setter
@NoArgsConstructor
public class Schedule extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "contents", nullable = false)
    private String contents;

    @Column(name = "commentCount", nullable = false)
    private int commentCount;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.REMOVE)
    private List<Comment> CommentList = new ArrayList<>();

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.REMOVE)
    private List<ScheduleUser> ScheduleUserList = new ArrayList<>();

    @Column(name = "weather", nullable = false)
    private String weather;

    public Schedule(ScheduleRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }

    public void update(ScheduleRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }
}
